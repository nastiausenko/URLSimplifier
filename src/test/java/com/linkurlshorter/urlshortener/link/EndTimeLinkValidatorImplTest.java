package com.linkurlshorter.urlshortener.link;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EndTimeLinkValidatorImplTest {
    private EndTimeLinkValidatorImpl endTimeLinkValidator;

    @BeforeEach
    void setUp() {
        endTimeLinkValidator = new EndTimeLinkValidatorImpl();
    }
    @Test
    void checkingCorrectCurrentTimeInRelationOursLink(){
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime beforeTime = timeNow.plusMinutes(30);
        assertThat(endTimeLinkValidator.isValid(beforeTime, null)).isTrue();
    }
    @Test
    void checkingExpirationTimeBeforeCurrentTimeOursLink(){
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime expiredTime = timeNow.minusMinutes(30);
        assertThat(endTimeLinkValidator.isValid(expiredTime, null)).isFalse();
    }
}
