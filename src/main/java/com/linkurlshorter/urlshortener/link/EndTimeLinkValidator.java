package com.linkurlshorter.urlshortener.link;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The {@link EndTimeLinkValidator} annotation is used to validate the link's end time.
 * This annotation can be applied to fields, methods, or other annotations.
 * It uses the UrlShortValidatorImpl implementation to perform the validation.
 *
 * @author Vlas Pototskyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = UrlShortValidatorImpl.class)
public @interface EndTimeLinkValidator {

    /**
     * The message to be used to inform about the failed validation.
     * Default: "Link time is not valid!".
     *
     * @return Error message.
     */
    String message() default "Link time is not valid!";

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
