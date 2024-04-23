package com.linkurlshorter.urlshortener.link;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(endTimeLinkValidator.isValid(lessTime, null)).isFalse();
    }
}
