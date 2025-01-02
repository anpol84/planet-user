package ru.planet.user.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.lang.Override;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.json.common.JsonWriter;

@Generated("ru.tinkoff.kora.json.annotation.processor.writer.JsonWriterGenerator")
public final class $GetUser400Response_JsonWriter implements JsonWriter<GetUser400Response> {
  private static final SerializedString __error_optimized_field_name = new SerializedString("error");

  public $GetUser400Response_JsonWriter() {
  }

  @Override
  public final void write(JsonGenerator _gen, @Nullable GetUser400Response _object) throws
      IOException {
    if (_object == null) {
      _gen.writeNull();
      return;
    }
    _gen.writeStartObject(_object);
    if (_object._error() != null) {
      _gen.writeFieldName(__error_optimized_field_name);
      _gen.writeString(_object._error());
    }
    _gen.writeEndObject();
  }
}
