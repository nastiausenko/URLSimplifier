package com.linkurlshorter.urlshortener.link;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating the short URL format.
 * Checks if the short URL conforms to the specified format.
 *
 * @author Vlas Pototskyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = UrlShortFormatValidatorImpl.class)
public @interface UrlShortFormatValidator {
    /**
     * An error message that will be used if the URL format is incorrect.
     * <p>
     * Default: "Not valid url format!"
     *
     * @return Error message.
     */
    String message() default "Not valid format!";

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
