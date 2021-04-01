package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.users.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.timeline.TimelineServiceImpl;
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
        socialNetwork = new SocialNetwork(new CommandProcessor(new UserService(new InMemoryUserRepository(), new TimelineServiceImpl())), clockStub);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndAUsersTimelineCanBeRead() throws IOException {

        runAliceAndBobPostCommands();

        runCommand(TestCommands.READ_ALICE_TIMELINE, TestCommands.AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(TestCommands.ALICE_EXAMPLE_POST + minutesAgo(5) + TestCommands.NEW_LINE);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndADifferentUsersTimelineCanBeRead() throws IOException {

        runAliceAndBobPostCommands();

        runCommand(TestCommands.READ_BOB_TIMELINE, TestCommands.AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(
                TestCommands.BOB_EXAMPLE_POST_TWO + minutesAgo(1) + TestCommands.NEW_LINE +
                        TestCommands.BOB_EXAMPLE_POST_ONE + minutesAgo(2) + TestCommands.NEW_LINE
        );
    }

    @Test
    void usersCanFollowOtherUsersAndViewAnAggregatedListOfTheirsAndTheirFollowedUsersPostsOnTheirWall() throws IOException {

        runAliceAndBobPostCommands();

        runCommand(TestCommands.CHARLIE_EXAMPLE_POST_COMMAND, TestCommands.AT_15_SECONDS_BEFORE_12PM);

        SocialNetwork.main(new String[]{TestCommands.CHARLIE_FOLLOWS_ALICE, TestCommands.CHARLIE_FOLLOWS_BOB});

        runCommand(TestCommands.READ_CHARLIE_WALL, TestCommands.AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(
                TestCommands.CHARLIE_USER_NAME + " - " + TestCommands.CHARLIE_EXAMPLE_POST + secondsAgo(15) + TestCommands.NEW_LINE +
                        TestCommands.BOB_USER_NAME + " - " + TestCommands.BOB_EXAMPLE_POST_TWO + minutesAgo(1) + TestCommands.NEW_LINE +
                        TestCommands.BOB_USER_NAME + " - " + TestCommands.BOB_EXAMPLE_POST_ONE + minutesAgo(2) + TestCommands.NEW_LINE +
                        TestCommands.ALICE_USER_NAME + " - " + TestCommands.ALICE_EXAMPLE_POST + minutesAgo(5) + TestCommands.NEW_LINE
        );
    }

    private void runAliceAndBobPostCommands() {
        runCommand(TestCommands.ALICE_EXAMPLE_POST_COMMAND, TestCommands.AT_5_MINUTES_BEFORE_12PM);
        runCommand(TestCommands.BOB_EXAMPLE_POST_COMMAND_ONE, TestCommands.AT_2_MINUTES_BEFORE_12PM);
        runCommand(TestCommands.BOB_EXAMPLE_POST_COMMAND_TWO, TestCommands.AT_1_MINUTE_BEFORE_12PM);
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
        return new String(byteArrayOutputStream.toByteArray());
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
