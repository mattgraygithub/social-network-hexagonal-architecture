package com.mattgray.socialnetworkkata.timeline;

import com.mattgray.socialnetworkkata.common.ClockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.mattgray.socialnetworkkata.common.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class PostRepositoryServiceShould {

    private static final String FIVE_MINUTES_AGO = " (5 minutes ago)";
    private static final String TWO_MINUTES_AGO = " (2 minutes ago)";
    ByteArrayOutputStream byteArrayOutputStream;
    Clock clockStub;
    ClockService clockServiceMock;
    TimelineService timelineService;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        clockServiceMock = mock(ClockService.class);
        timelineService = new TimelineServiceImpl(clockServiceMock);
    }

    @Test
    void callClockServiceToGetTimeBetweenPostAndReadCommand() {
        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(ALICE_EXAMPLE_POST, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM))));
        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(AT_12PM));

        verify(clockServiceMock).getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM));
    }

    @Test
    void printATimelineWithOnePost() throws IOException {
        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(ALICE_EXAMPLE_POST, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM))));
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);
        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(ALICE_EXAMPLE_POST + FIVE_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printATimelineMultiplePosts() throws IOException {
        ArrayList<Post> timeline = new ArrayList<>(Arrays.asList(
                new Post(BOB_EXAMPLE_POST_COMMAND_ONE, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM)),
                new Post(BOB_EXAMPLE_POST_COMMAND_TWO, stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM))
        ));
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_EXAMPLE_POST_COMMAND_ONE + " (5 minutes ago)" + NEW_LINE +
                        BOB_EXAMPLE_POST_COMMAND_TWO + TWO_MINUTES_AGO + NEW_LINE
        );
    }

    private LocalDateTime stubbedLocalTimeOf(LocalDateTime time) {
        Clock readCommandClock = Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        return LocalDateTime.now(readCommandClock);
    }

    private String getConsoleOutput() throws IOException {
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toString();
    }
}
