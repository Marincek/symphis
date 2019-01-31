package com.marincek.sympis.controllers.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TagValidator implements ConstraintValidator<TagConstraint, List<String>> {

    @Override
    public void initialize(TagConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        return values.stream().noneMatch(s -> s.matches("<[^>]*>"));
    }
}
