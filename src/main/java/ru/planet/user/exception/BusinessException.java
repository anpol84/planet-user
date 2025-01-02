package ru.planet.user.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    private String message;
}
