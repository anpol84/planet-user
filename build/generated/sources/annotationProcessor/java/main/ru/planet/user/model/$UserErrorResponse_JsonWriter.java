package ru.planet.user.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.lang.Override;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.json.common.JsonWriter;

@Generated("ru.tinkoff.kora.json.annotation.processor.writer.JsonWriterGenerator")
public final class $UserErrorResponse_JsonWriter implements JsonWriter<UserErrorResponse> {
  private static final SerializedString _message_optimized_field_name = new SerializedString("message");

  public $UserErrorResponse_JsonWriter() {
  }

  @Override
  public final void write(JsonGenerator _gen, @Nullable UserErrorResponse _object) throws
      IOException {
    if (_object == null) {
      _gen.writeNull();
      return;
    }
    _gen.writeStartObject(_object);
    if (_object.message() != null) {
      _gen.writeFieldName(_message_optimized_field_name);
      _gen.writeString(_object.message());
    }
    _gen.writeEndObject();
  }
}
