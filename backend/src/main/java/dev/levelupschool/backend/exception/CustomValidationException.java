package dev.levelupschool.backend.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomValidationException extends Throwable {
    public String field;
    public String reason;
}
