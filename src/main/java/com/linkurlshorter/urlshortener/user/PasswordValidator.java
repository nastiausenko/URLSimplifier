package com.linkurlshorter.urlshortener.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation to password format validation.
 * Checks if the password entered by the user matches a regular expression.
 *
 * @author Vlas Pototskyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = PasswordValidatorImpl.class)
public @interface PasswordValidator {
    /**
     * An error message that will be used if the password format is incorrect.
     * <p>
     * Default: "Password must be at least 8 characters long and contain at least one digit,
     * one uppercase letter, and one lowercase letter. No spaces are allowed".
     *
     * @return Error message.
     */
    String message() default "Password must be at least 8 characters long and contain at least one digit, " +
            "one uppercase letter, and one lowercase letter. No spaces are allowed.";

    /**
     * Groups to which this constraint belongs. Default: empty array.
     *
     * @return Constraint groups.
     */
    Class<?>[] group() default {};

    /**
     * Parameters that can be used to configure the constraint.
     * Default: an empty array.
     *
     * @return The parameters of the constraint.
     */
    Class<? extends Payload>[] payload() default {};
}
