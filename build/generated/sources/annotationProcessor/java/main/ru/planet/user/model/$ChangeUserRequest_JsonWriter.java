package ru.planet.user.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.lang.Override;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.json.common.JsonWriter;

@Generated("ru.tinkoff.kora.json.annotation.processor.writer.JsonWriterGenerator")
public final class $ChangeUserRequest_JsonWriter implements JsonWriter<ChangeUserRequest> {
  private static final SerializedString _login_optimized_field_name = new SerializedString("login");

  private static final SerializedString _city_optimized_field_name = new SerializedString("city");

  public $ChangeUserRequest_JsonWriter() {
  }

  @Override
  public final void write(JsonGenerator _gen, @Nullable ChangeUserRequest _object) throws
      IOException {
    if (_object == null) {
      _gen.writeNull();
      return;
    }
    _gen.writeStartObject(_object);
    if (_object.login() != null) {
      _gen.writeFieldName(_login_optimized_field_name);
      _gen.writeString(_object.login());
    }
    if (_object.city() != null) {
      _gen.writeFieldName(_city_optimized_field_name);
      _gen.writeString(_object.city());
    }
    _gen.writeEndObject();
  }
}
