/**
 * NOTE: This class is auto generated by Kora OpenAPI Generator (https://openapi-generator.tech) (7.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package ru.planet.user.model;

import jakarta.annotation.Nullable;


/**
 * CreateUserResponse
 * @param token token
 */
@ru.tinkoff.kora.common.annotation.Generated("openapi generator kora java")
@ru.tinkoff.kora.json.common.annotation.JsonWriter
@ru.tinkoff.kora.validation.common.annotation.Valid
public record CreateUserResponse(
  @ru.tinkoff.kora.json.common.annotation.JsonField("token")
  @Nullable String token
){

  public CreateUserResponse(
  ) {
    this(null);
  }

  @ru.tinkoff.kora.json.common.annotation.JsonReader
  public CreateUserResponse {   }



  public CreateUserResponse withToken(@Nullable String token) {
    if (this.token == token) return this;
    return new CreateUserResponse(
      token
    );
  }
}

