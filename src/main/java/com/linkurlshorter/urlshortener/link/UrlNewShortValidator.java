package com.linkurlshorter.urlshortener.link;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * An annotation {@link UrlNewShortValidator} used to validate short URLs.
 * Primarily validates new short links such as in edit content and create operations.
 * Can be applied to fields, methods, or other annotations.
 * Uses the {@link UrlNewShortValidatorImpl} implementation to perform the validation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = UrlNewShortValidatorImpl.class)
public @interface UrlNewShortValidator {

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