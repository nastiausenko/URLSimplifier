package com.linkurlshorter.urlshortener.link.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EndTimeLinkValidatorImpl} class.
 * These tests ensure that the {@link EndTimeLinkValidatorImpl} class correctly validates link expiration times.
 *
 * @author Vlas Pototskyi
 */
class EndTimeLinkValidatorImplTest {
    private EndTimeLinkValidatorImpl endTimeLinkValidator;

    @BeforeEach
    void setUp() {
        endTimeLinkValidator = new EndTimeLinkValidatorImpl();
    }

    /**
     * Test to verify if the current time is greater than the link expiration time.
     */
    @Test
    void whenCurrentTimeIsGreaterThanTheLink() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime greaterTime = time.plusMinutes(30);
        assertThat(endTimeLinkValidator.isValid(greaterTime, null)).isTrue();
    }

    /**
     * Test to verify if the current time is less than the link expiration time.
     */
    @Test
    void whenCurrentTimeIsLessThanTheLink() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime lessTime = time.minusMinutes(30);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate(null)).thenReturn(builder);

        assertThat(endTimeLinkValidator.isValid(lessTime, context)).isFalse();
    }
}
