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
    void printATimelineWithOnePost() throws IOException {

        Clock postCommandClock = Clock.fixed(TestCommands.AT_5_MINUTES_BEFORE_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        LocalDateTime timeOfPost = LocalDateTime.now(postCommandClock);
        ArrayList<Post> timeline = new ArrayList<>(Arrays.asList(new Post(TestCommands.ALICE_EXAMPLE_POST, timeOfPost)));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + " (5 minutes ago)" + TestCommands.NEW_LINE);
    }

    @Test
    void printAtTimelineWithTwoPosts() throws IOException {

        Clock postOneClock = Clock.fixed(TestCommands.AT_5_MINUTES_BEFORE_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        LocalDateTime timeOfPostOne = LocalDateTime.now(postOneClock);

        Clock postTwoClock = Clock.fixed(TestCommands.AT_2_MINUTES_BEFORE_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        LocalDateTime timeOfPostTwo = LocalDateTime.now(postTwoClock);

        ArrayList<Post> timeline = new ArrayList<>(Arrays.asList(
                new Post(TestCommands.BOB_EXAMPLE_POST_COMMAND_ONE, timeOfPostOne),
                new Post(TestCommands.BOB_EXAMPLE_POST_COMMAND_TWO, timeOfPostTwo)
        ));

        timelineService.displayTimeLine(timeline, stubbedLocalTimeOf(TestCommands.AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                TestCommands.BOB_EXAMPLE_POST_COMMAND_ONE + " (5 minutes ago)" + TestCommands.NEW_LINE +
                        TestCommands.BOB_EXAMPLE_POST_COMMAND_TWO + " (2 minutes ago)" + TestCommands.NEW_LINE
        );

    }

    private String getConsoleOutput() throws IOException {
        byteArrayOutputStream.flush();
        return new String(byteArrayOutputStream.toByteArray());
    }

    private LocalDateTime stubbedLocalTimeOf(LocalDateTime time) {
        Clock readCommandClock = Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        return LocalDateTime.now(readCommandClock);
    }
}
