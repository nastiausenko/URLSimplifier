package com.linkurlshorter.urlshortener.link;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation for URL format validation.
 * Checks if the entered string matches the URL format using a regular expression.
 *
 * @author Vlas Pototskyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = UrlLongFormatValidatorImpl.class)
public @interface UrlLongFormatValidator {

    /**
     * An error message that will be used if the URL format is incorrect.
     * Default: "Not valid url format!"
     *
     * @return Error message.
     */
    String message() default "Not valid format url!";

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

