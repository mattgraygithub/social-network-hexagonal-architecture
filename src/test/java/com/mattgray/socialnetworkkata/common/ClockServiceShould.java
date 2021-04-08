package com.mattgray.socialnetworkkata.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.mattgray.socialnetworkkata.TestCommands.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ClockServiceShould {

    Clock clockStub;
    ClockService clockService;

    @BeforeEach
    void setUp() {
        clockStub = mock(Clock.class);
        clockService = new ClockServiceImpl();
    }

    @Test
    void getFormattedTimeInSeconds() {
        assertThat(clockService.getTimeBetween(stubbedLocalTimeOf(AT_15_SECONDS_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).isEqualTo(" (15 seconds ago)");
    }

    @Test
    void getFormattedTimeInMinutes() {
        assertThat(clockService.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).isEqualTo(" (2 minutes ago)");
    }

    @Test
    void getFormattedTimeInHours() {
        assertThat(clockService.getTimeBetween(stubbedLocalTimeOf(AT_2_HOURS_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).isEqualTo(" (2 hours ago)");
    }

    private LocalDateTime stubbedLocalTimeOf(LocalDateTime time) {
        Clock readCommandClock = Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        return LocalDateTime.now(readCommandClock);
    }
}
