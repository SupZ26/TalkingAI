package com.example.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomStringValidator implements ConstraintValidator<ValidString, String> {

    private int minLength;
    private int maxLength;

    @Override
    public void initialize(ValidString constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.length() >= minLength && value.length() <= maxLength;
    }
}
