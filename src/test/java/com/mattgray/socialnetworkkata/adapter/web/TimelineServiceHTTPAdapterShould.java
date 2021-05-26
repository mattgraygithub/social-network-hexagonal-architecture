package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TimelineServiceHTTPAdapterShould {

    private static ClockService clockService;
    private static TimelineService timelineService;

    @BeforeEach
    void setUp() {
        clockService = mock(ClockService.class);
        timelineService = new TimelineServiceHTTPAdapter(clockService);
    }

    @Test
    void callClockServiceToGetTheElapsedTimeBetweenTheTimeOfThePostAndTheTimeOfTheReadRequest() {
        timelineService.getTimeLine(ALICE_EXAMPLE_POST_LIST, AT_12PM);
        verify(clockService).getTimeBetween(AT_5_MINUTES_BEFORE_12PM, AT_12PM);
    }

    @ParameterizedTest
    @MethodSource("getExamplePostListsAndOutputs")
    void convertAListOfPostsIntoAJSONArrayWithTheElapsedTimeAsOnePropertyAndThePostTextAsTheOtherPropertyForEachPost(ArrayList<Post> inputPostList,String expectedOutput) {
        when(clockService.getTimeBetween(AT_1_MINUTE_BEFORE_12PM, AT_12PM)).thenReturn(ONE_MINUTE_AGO);
        when(clockService.getTimeBetween(AT_2_MINUTES_BEFORE_12PM, AT_12PM)).thenReturn(TWO_MINUTES_AGO);
        when(clockService.getTimeBetween(AT_5_MINUTES_BEFORE_12PM, AT_12PM)).thenReturn(FIVE_MINUTES_AGO);
        assertThat(timelineService.getTimeLine(inputPostList, AT_12PM)).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> getExamplePostListsAndOutputs() {
        return Stream.of(
                Arguments.of(ALICE_EXAMPLE_POST_LIST,ALICE_EXPECTED_JSON_RESPONSE),
                Arguments.of(BOB_EXAMPLE_POST_LIST,BOB_EXPECTED_JSON_RESPONSE)
        );
    }
}
