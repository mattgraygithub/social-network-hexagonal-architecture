package com.mattgray.socialnetworkkata.clock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

class ClockServiceShould {

    private static ClockService clockService;

    @BeforeEach
    void setUp() {
        clockService = new ClockServiceImpl();
    }

    @Test
    void getFormattedElapsedTimeInSeconds() {
        assertThat(clockService.getTimeBetween(stubbedLocalTime(AT_15_SECONDS_BEFORE_12PM), stubbedLocalTime(AT_12PM))).isEqualTo(" (15 seconds ago)");
    }

    @Test
    void getFormattedElapsedTimeInMinutes() {
        assertThat(clockService.getTimeBetween(stubbedLocalTime(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTime(AT_12PM))).isEqualTo(TWO_MINUTES_AGO);
    }

    @Test
    void getFormattedElapsedTimeInHours() {
        assertThat(clockService.getTimeBetween(stubbedLocalTime(AT_2_HOURS_BEFORE_12PM), stubbedLocalTime(AT_12PM))).isEqualTo(" (2 hours ago)");
    }

    private LocalDateTime stubbedLocalTime(LocalDateTime time) {
        Clock readCommandClock = Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        return LocalDateTime.now(readCommandClock);
    }
}
