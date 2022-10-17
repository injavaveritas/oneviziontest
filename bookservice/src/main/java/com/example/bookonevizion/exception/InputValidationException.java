package com.example.bookonevizion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Unexpected value provided")
public class InputValidationException extends RuntimeException {
}
