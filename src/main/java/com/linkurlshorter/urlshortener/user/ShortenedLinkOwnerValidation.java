package com.linkurlshorter.urlshortener.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The annotation used to validate user ownership of the shortened link.
 * The validation is performed using {@link ShortenedLinkOwnerValidationImpl}.
 *
 * @author Vlas Potoskyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = ShortenedLinkOwnerValidationImpl.class)
public @interface ShortenedLinkOwnerValidation {
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
