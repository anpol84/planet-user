package ru.planet.user.model;

import java.lang.Override;
import java.util.ArrayList;
import java.util.List;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.validation.common.ValidationContext;
import ru.tinkoff.kora.validation.common.Validator;
import ru.tinkoff.kora.validation.common.Violation;

@Generated("ru.tinkoff.kora.validation.annotation.processor.ValidatorGenerator")
public final class $ChangeUserRequest_Validator implements Validator<ChangeUserRequest> {
  public $ChangeUserRequest_Validator() {
  }

  @Override
  public List<Violation> validate(ChangeUserRequest value, ValidationContext context) {
    if (value == null) {
        return List.of(context.violates("ChangeUserRequest input must be non null, but was null"));
    }

    final List<Violation> _violations = new ArrayList<>();



    return _violations;
  }
}
