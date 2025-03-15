package ru.planet.gpt.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import ru.planet.gpt.cache.TokenCache;
import ru.planet.gpt.dto.GigaChatDto;
import ru.planet.gpt.dto.TokenResponse;
import ru.planet.gpt.properties.SberProperties;
import ru.tinkoff.kora.common.Component;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.UUID;

@Component
public class GigaChatDialog {

    private final SberProperties sberProperties;
    private OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final TokenCache tokenCache;

    public GigaChatDialog(SberProperties sberProperties, TokenCache tokenCache) {
        this.sberProperties = sberProperties;
        this.objectMapper = new ObjectMapper();
        this.tokenCache = tokenCache;
        try {
            this.client = createHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResponse(String message) {
        GigaChatDto gigaChatDto = new GigaChatDto("GigaChat",
                List.of(new GigaChatDto.Message("system", sberProperties.systemQuery()),  new GigaChatDto.Message("user", message)) );
        RequestBody body = null;
        try {
            body = RequestBody.create(objectMapper.writeValueAsString(gigaChatDto), MediaType.get("application/json; charset=utf-8"));
        } catch (Exception ignored) {

        }

        Request request = new Request.Builder()
                .url(sberProperties.askUrl())
                .post(body)
                .addHeader("Authorization", "Bearer " + getToken())
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return new String(response.body().bytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getToken() {
        String token = tokenCache.get(1L);
        if (token != null) {
            return token;
        }
        Request request = createRequest();
        OkHttpClient client = createHttpClient();

        try (Response response = client.newCall(request).execute()) {
            token = handleResponse(response);
            tokenCache.put(1L, token);
            return token;
        } catch (IOException e) {
            return null;
        }
    }

    private Request createRequest() {
        RequestBody formBody = new FormBody.Builder()
                .add("scope", "GIGACHAT_API_PERS")
                .build();

        return new Request.Builder()
                .url(sberProperties.tokenUrl())
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .addHeader("RqUID", UUID.randomUUID().toString())
                .addHeader("Authorization", "Basic " + sberProperties.token())
                .build();
    }

    private OkHttpClient createHttpClient() {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream certInput = new FileInputStream(sberProperties.certPath());
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(certInput);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("caCert", caCert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HTTP client", e);
        }
    }

    private String handleResponse(Response response) throws IOException {
        if (response.isSuccessful() && response.body() != null) {
            TokenResponse tokenResponse = objectMapper.readValue(response.body().string(), TokenResponse.class);
            return tokenResponse.accessToken();
        } else {
            return null;
        }
    }
}