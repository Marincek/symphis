package com.marincek.sympis.controllers.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TagValidator implements ConstraintValidator<TagConstraint, List<String>> {

    private String messageNullOrEmpty;
    private String messageHtmlTag;

    @Override
    public void initialize(TagConstraint constraintAnnotation) {
        messageNullOrEmpty = constraintAnnotation.messageNullOrEmpty();
        messageHtmlTag = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (values == null || values.isEmpty()) {
            context.buildConstraintViolationWithTemplate(messageNullOrEmpty).addConstraintViolation();
            return false;
        }
        if (values.stream().anyMatch(s -> s.matches("<[^>]*>"))) {
            context.buildConstraintViolationWithTemplate(messageHtmlTag).addConstraintViolation();
            return false;
        }
        return true;
    }
}
