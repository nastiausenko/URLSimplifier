package com.linkurlshorter.urlshortener.link;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * An annotation {@link UrlShortValidator} used to validate short URLs.
 * Can be applied to fields, methods, or other annotations.
 * Uses the {@link UrlShortValidatorImpl} implementation to perform the validation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = UrlShortValidatorImpl.class)
public @interface UrlShortValidator {

    /**
     * The message to be used to inform about the failed validation.
     * Default: "".
     *
     * @return Error message.
     */
    String message() default "";

    /**
     * Groups to which this constraint belongs. Default: empty array.
     *
     * @return Constraint groups.
     */
    Class<?>[] groups() default {};

    /**
     * Parameters that can be used to configure the constraint.
     * Default: an empty array.
     *
     * @return The parameters of the constraint.
     */
    Class<? extends Payload>[] payload() default {};
}
