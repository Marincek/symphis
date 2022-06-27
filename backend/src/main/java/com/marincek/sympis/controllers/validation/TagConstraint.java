package com.marincek.sympis.controllers.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint (validatedBy = TagValidator.class)
@Target ({ElementType.METHOD, ElementType.FIELD})
@Retention (RetentionPolicy.RUNTIME)
public @interface TagConstraint {
    String message() default "No HTML tags allowed";

    String messageNullOrEmpty() default "Please provide tags";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
