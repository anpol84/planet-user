package ru.planet.user.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.SerializedString;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.json.common.JsonReader;

@Generated("ru.tinkoff.kora.json.annotation.processor.reader.JsonReaderGenerator")
public final class $GetUserResponse_JsonReader implements JsonReader<GetUserResponse> {
  private static final int ALL_FIELDS_RECEIVED = 0b1111;

  private static final int NULLABLE_FIELDS_RECEIVED = 0b1111;

  private static final SerializedString _id_optimized_field_name = new SerializedString("id");

  private static final SerializedString _login_optimized_field_name = new SerializedString("login");

  private static final SerializedString _city_optimized_field_name = new SerializedString("city");

  private static final SerializedString _roles_optimized_field_name = new SerializedString("roles");

  private final JsonReader<List<GetUserResponse.RolesEnum>> rolesReader;

  public $GetUserResponse_JsonReader(JsonReader<List<GetUserResponse.RolesEnum>> rolesReader) {
    this.rolesReader = rolesReader;
  }

  private static Long read_id(JsonParser __parser, int[] __receivedFields) throws IOException {
    var __token = __parser.nextToken();
    __receivedFields[0] = __receivedFields[0] | (1 << 0);
    if (__token == JsonToken.VALUE_NUMBER_INT) {
      return __parser.getLongValue();
    } else if (__token == JsonToken.VALUE_NULL) {
      return null;
    } else {
      throw new JsonParseException(__parser, "Expecting [VALUE_NUMBER_INT, VALUE_NULL] token for field 'id', got " + __token);
    }
  }

  private static String read_login(JsonParser __parser, int[] __receivedFields) throws IOException {
    var __token = __parser.nextToken();
    __receivedFields[0] = __receivedFields[0] | (1 << 1);
    if (__token == JsonToken.VALUE_STRING) {
      return __parser.getText();
    } else if (__token == JsonToken.VALUE_NULL) {
      return null;
    } else {
      throw new JsonParseException(__parser, "Expecting [VALUE_STRING, VALUE_NULL] token for field 'login', got " + __token);
    }
  }

  private static String read_city(JsonParser __parser, int[] __receivedFields) throws IOException {
    var __token = __parser.nextToken();
    __receivedFields[0] = __receivedFields[0] | (1 << 2);
    if (__token == JsonToken.VALUE_STRING) {
      return __parser.getText();
    } else if (__token == JsonToken.VALUE_NULL) {
      return null;
    } else {
      throw new JsonParseException(__parser, "Expecting [VALUE_STRING, VALUE_NULL] token for field 'city', got " + __token);
    }
  }

  private List<GetUserResponse.RolesEnum> read_roles(JsonParser __parser, int[] __receivedFields)
      throws IOException {
    var __token = __parser.nextToken();
    if (__token == JsonToken.VALUE_NULL) {
      return null;
    }
    return rolesReader.read(__parser);
  }

  @Override
  @Nullable
  public final GetUserResponse read(JsonParser __parser) throws IOException {
    var __token = __parser.currentToken();
    if (__token == JsonToken.VALUE_NULL) 
      return null;
    if (__token != JsonToken.START_OBJECT) 
      throw new JsonParseException(__parser, "Expecting START_OBJECT token, got " + __token);
    var __receivedFields = new int[]{NULLABLE_FIELDS_RECEIVED};

    Long id = null;
    String login = null;
    String city = null;
    List<GetUserResponse.RolesEnum> roles = null;
    if (__parser.nextFieldName(_id_optimized_field_name)) {
      id = read_id(__parser, __receivedFields);
      if (__parser.nextFieldName(_login_optimized_field_name)) {
        login = read_login(__parser, __receivedFields);
        if (__parser.nextFieldName(_city_optimized_field_name)) {
          city = read_city(__parser, __receivedFields);
          if (__parser.nextFieldName(_roles_optimized_field_name)) {
            roles = read_roles(__parser, __receivedFields);
            __token = __parser.nextToken();
            while (__token != JsonToken.END_OBJECT) {
                __parser.nextToken();
                __parser.skipChildren();
                __token = __parser.nextToken();
            }
            return new GetUserResponse(id, login, city, roles);
          }
        }
      }
    }
    __token = __parser.currentToken();
    while (__token != JsonToken.END_OBJECT) {
      if (__token != JsonToken.FIELD_NAME) 
        throw new JsonParseException(__parser, "Expecting FIELD_NAME token, got " + __token);
      var __fieldName = __parser.currentName();
      switch (__fieldName) {
        case "id" -> {
          id = read_id(__parser, __receivedFields);
        }
        case "login" -> {
          login = read_login(__parser, __receivedFields);
        }
        case "city" -> {
          city = read_city(__parser, __receivedFields);
        }
        case "roles" -> {
          roles = read_roles(__parser, __receivedFields);
        }
        default -> {
          __parser.nextToken();
          __parser.skipChildren();
        }
      }
      __token = __parser.nextToken();
    }
    if (__receivedFields[0] != ALL_FIELDS_RECEIVED) {
      var _nonReceivedFields = (~__receivedFields[0]) & ALL_FIELDS_RECEIVED;
      var __error = new StringBuilder("Some of required json fields were not received:");
      for (int __i = 0; __i < 4; __i++) {
        if ((_nonReceivedFields & (1 << __i)) != 0) {
          __error.append(" ").append(switch (__i) {
          case 0 -> "id(id)";
          case 1 -> "login(login)";
          case 2 -> "city(city)";
          case 3 -> "roles(roles)";
          default -> "";
        });
        }
      }
      throw new JsonParseException(__parser, __error.toString());
    }
    return new GetUserResponse(id, login, city, roles);
  }
}
