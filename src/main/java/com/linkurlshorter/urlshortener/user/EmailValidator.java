package com.linkurlshorter.urlshortener.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = EmailValidatorImpl.class)
public @interface EmailValidator {
    String message() default "Email address entered incorrectly!";

    Class<?>[] group() default {};

    Class<? extends Payload>[] payload() default {};
}
