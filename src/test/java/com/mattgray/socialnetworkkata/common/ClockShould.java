package com.mattgray.socialnetworkkata.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClockShould {

    private static final int ONE_SECOND = 1;
    private static final int ONE_MINUTE = 60;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;

    private static Stream<Arguments> singularTimeUnitsProvider() {
        return Stream.of(
                Arguments.of(Clock.SECONDS.getFormattedTimeDifference(ONE_SECOND), " (1 second ago)"),
                Arguments.of(Clock.MINUTES.getFormattedTimeDifference(ONE_MINUTE), " (1 minute ago)"),
                Arguments.of(Clock.HOURS.getFormattedTimeDifference(ONE_HOUR), " (1 hour ago)")
        );
    }

    @Test
    void giveCorrectlyFormattedElapsedTimeInSeconds() {
        long thirtySeconds = 30;
        assertThat(Clock.SECONDS.getFormattedTimeDifference(thirtySeconds)).isEqualTo(" (30 seconds ago)");
    }

    @Test
    void giveCorrectlyFormattedElapsedTimeInMinutes() {
        long twentyMinutes = 20 * ONE_MINUTE;
        assertThat(Clock.MINUTES.getFormattedTimeDifference(twentyMinutes)).isEqualTo(" (20 minutes ago)");
    }

    @Test
    void giveCorrectlyFormattedElapsedTimeInHours() {
        long fiveHours = 5 * ONE_HOUR;
        assertThat(Clock.HOURS.getFormattedTimeDifference(fiveHours)).isEqualTo(" (5 hours ago)");
    }

    @ParameterizedTest
    @MethodSource("singularTimeUnitsProvider")
    void giveCorrectFormattingForSingularTimeUnits(String formattedTimeDifference, String expectedOutput) {
        assertThat(formattedTimeDifference).isEqualTo(expectedOutput);
    }
}
