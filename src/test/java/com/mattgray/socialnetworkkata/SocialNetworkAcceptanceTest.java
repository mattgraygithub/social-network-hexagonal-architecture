package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.common.ClockServiceImpl;
import com.mattgray.socialnetworkkata.posts.TimelineServiceImpl;
import com.mattgray.socialnetworkkata.posts.WallServiceImpl;
import com.mattgray.socialnetworkkata.users.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static com.mattgray.socialnetworkkata.common.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocialNetworkAcceptanceTest {

    ByteArrayOutputStream byteArrayOutputStream;
    SocialNetwork socialNetwork;
    Clock clockStub;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        socialNetwork = new SocialNetwork(new CommandProcessor(new UserService(new InMemoryUserRepository(new ArrayList<>()), new TimelineServiceImpl(new ClockServiceImpl()), new WallServiceImpl())), clockStub);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndAUsersTimelineCanBeRead() throws IOException {
        runAliceAndBobPostCommands();

        runCommand(READ_ALICE_TIMELINE, AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(ALICE_EXAMPLE_POST + minutesAgo(5) + NEW_LINE);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndADifferentUsersTimelineCanBeRead() throws IOException {
        runAliceAndBobPostCommands();

        runCommand(READ_BOB_TIMELINE, AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_EXAMPLE_POST_ONE + minutesAgo(2) + NEW_LINE +
                        BOB_EXAMPLE_POST_TWO + minutesAgo(1) + NEW_LINE
        );
    }

    @Test
    void usersCanFollowOtherUsersAndViewAnAggregatedListOfTheirsAndTheirFollowedUsersPostsOnTheirWall() throws IOException {
        runAliceAndBobPostCommands();

        runCommand(CHARLIE_EXAMPLE_POST_COMMAND, AT_15_SECONDS_BEFORE_12PM);

        SocialNetwork.main(new String[]{CHARLIE_FOLLOWS_ALICE, CHARLIE_FOLLOWS_BOB});

        runCommand(READ_CHARLIE_WALL, AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(
                CHARLIE_USER_NAME + " - " + CHARLIE_EXAMPLE_POST + secondsAgo(15) + NEW_LINE +
                        BOB_USER_NAME + " - " + BOB_EXAMPLE_POST_TWO + minutesAgo(1) + NEW_LINE +
                        BOB_USER_NAME + " - " + BOB_EXAMPLE_POST_ONE + minutesAgo(2) + NEW_LINE +
                        ALICE_USER_NAME + " - " + ALICE_EXAMPLE_POST + minutesAgo(5) + NEW_LINE
        );
    }

    private void runAliceAndBobPostCommands() {
        runCommand(ALICE_EXAMPLE_POST_COMMAND, AT_5_MINUTES_BEFORE_12PM);
        runCommand(BOB_EXAMPLE_POST_COMMAND_ONE, AT_2_MINUTES_BEFORE_12PM);
        runCommand(BOB_EXAMPLE_POST_COMMAND_TWO, AT_1_MINUTE_BEFORE_12PM);
    }

    private void runCommand(String command, LocalDateTime timeOfCommand) {
        setUpClockStubWith(timeOfCommand);
        System.setIn(new ByteArrayInputStream(command.getBytes()));
        socialNetwork.run();
    }

    private void setUpClockStubWith(LocalDateTime timeOfCommand) {
        Clock fixedClock = Clock.fixed(timeOfCommand.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        when(clockStub.instant()).thenReturn(fixedClock.instant());
        when(clockStub.getZone()).thenReturn(fixedClock.getZone());
    }

    private String getConsoleOutput() throws IOException {
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toString();
    }

    private String minutesAgo(int minutes) {
        return minutes == 1
                ? " (" + minutes + " minute ago)"
                : " (" + minutes + " minutes ago)";
    }

    private String secondsAgo(int seconds) {
        return " (" + seconds + " seconds ago)";
    }
}
