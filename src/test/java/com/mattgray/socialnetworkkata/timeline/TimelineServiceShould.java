package com.mattgray.socialnetworkkata.timeline;

import com.mattgray.socialnetworkkata.TestCommands;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TimelineServiceShould {

    ByteArrayOutputStream byteArrayOutputStream;
    Clock clockStub;
    TimelineService timelineService;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        timelineService = new TimelineServiceImpl();
    }

    @Test
    void printATimelineWithOnePostPostedMinutesAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(TestCommands.ALICE_EXAMPLE_POST, stubbedLocalTimeOf(TestCommands.AT_5_MINUTES_BEFORE_12PM))));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (5 minutes ago)" + TestCommands.NEW_LINE);
    }

    @Test
    void printATimelineWithTwoPostsPostedMinutesAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Arrays.asList(
                new Post(TestCommands.BOB_EXAMPLE_POST_COMMAND_ONE, stubbedLocalTimeOf(TestCommands.AT_5_MINUTES_BEFORE_12PM)),
                new Post(TestCommands.BOB_EXAMPLE_POST_COMMAND_TWO, stubbedLocalTimeOf(TestCommands.AT_2_MINUTES_BEFORE_12PM))
        ));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                TestCommands.BOB_EXAMPLE_POST_COMMAND_ONE + " (5 minutes ago)" + TestCommands.NEW_LINE +
                        TestCommands.BOB_EXAMPLE_POST_COMMAND_TWO + " (2 minutes ago)" + TestCommands.NEW_LINE
        );
    }

    @Test
    void printATimelineWithOnePostPostedSecondsAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(TestCommands.ALICE_EXAMPLE_POST, stubbedLocalTimeOf(TestCommands.AT_15_SECONDS_BEFORE_12PM))));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (15 seconds ago)" + TestCommands.NEW_LINE);
    }

    @Test
    void printAPostPostedOneSecondAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(TestCommands.ALICE_EXAMPLE_POST, stubbedLocalTimeOf(TestCommands.AT_1_SECOND_BEFORE_12PM))));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (1 second ago)" + TestCommands.NEW_LINE);
    }

    @Test
    void printAPostPostedOneMinuteAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(TestCommands.ALICE_EXAMPLE_POST, stubbedLocalTimeOf(TestCommands.AT_1_MINUTE_BEFORE_12PM))));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (1 minute ago)" + TestCommands.NEW_LINE);
    }

    @Test
    void printAPostPostedHoursAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(TestCommands.ALICE_EXAMPLE_POST, stubbedLocalTimeOf(TestCommands.AT_2_HOURS_BEFORE_12PM))));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (2 hours ago)" + TestCommands.NEW_LINE);
    }

    @Test
    void printAPostPostedOneHourAgo() throws IOException {

        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(TestCommands.ALICE_EXAMPLE_POST, stubbedLocalTimeOf(TestCommands.AT_1_HOUR_BEFORE_12PM))));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (1 hour ago)" + TestCommands.NEW_LINE);
    }

    private String getConsoleOutput() throws IOException {
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toString();
    }

    private LocalDateTime stubbedLocalTimeOf(LocalDateTime time) {
        Clock readCommandClock = Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        return LocalDateTime.now(readCommandClock);
    }
}
