package com.wolox.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class PermissionValidator implements ConstraintValidator<PermissionInterface, String> {

    private PermissionInterface permissionInterface;

    @Override
    public void initialize(PermissionInterface constraintAnnotation) {
        this.permissionInterface = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] enumValues = permissionInterface.enumClass().getEnumConstants();
        return Arrays.stream(enumValues).anyMatch(p -> p.toString().equals(value.toUpperCase()));
    }
}
