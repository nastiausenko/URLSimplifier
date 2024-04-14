package com.linkurlshorter.urlshortener.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation to email format validation.
 * Checks whether the email entered by the user matches a regular
 * expression and whether the user already exists in our database.
 *
 * @author Vlas Pototskyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = EmailValidatorImpl.class)
public @interface EmailValidator {
    /**
     * An error message that will be used if the email format is incorrect.
     * <p>
     * Default: "Email address entered incorrectly!".
     *
     * @return Error message.
     */
    String message() default "Email address entered incorrectly!";

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
