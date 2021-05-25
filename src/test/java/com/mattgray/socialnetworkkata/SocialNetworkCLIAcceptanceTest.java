package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.console.CommandProcessor;
import com.mattgray.socialnetworkkata.adapter.console.TimelineServiceConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.console.WallConsoleAdapter;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserRepository;
import com.mattgray.socialnetworkkata.port.WallService;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.mattgray.socialnetworkkata.service.clock.ClockServiceImpl;
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

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocialNetworkCLIAcceptanceTest {

    private static ByteArrayOutputStream byteArrayOutputStream;
    private static Clock clockStub;
    private static SocialNetwork socialNetwork;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        ArrayList<User> users = new ArrayList<>();
        UserRepository userRepository = new InMemoryUserRepository(users);
        ClockService clockService = new ClockServiceImpl();
        TimelineService timelineService = new TimelineServiceConsoleAdapter(clockService);
        WallService wallService = new WallConsoleAdapter(clockService);
        UserService userService = new UserService(userRepository, timelineService, wallService);
        socialNetwork = new SocialNetwork(new CommandProcessor(userService, timelineService), clockStub);
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
                BOB_EXAMPLE_POST_TWO + minutesAgo(1) + NEW_LINE +
                        BOB_EXAMPLE_POST_ONE + minutesAgo(2) + NEW_LINE
        );
    }

    @Test
    void usersCanFollowOtherUsersAndViewAnAggregatedListOfTheirsAndTheirFollowedUsersPostsOnTheirWall() throws IOException {
        runAliceAndBobPostCommands();

        runCommand(CHARLIE_EXAMPLE_POST_COMMAND, AT_15_SECONDS_BEFORE_12PM);
        runCommand(CHARLIE_FOLLOWS_ALICE, AT_12PM);
        runCommand(CHARLIE_FOLLOWS_BOB, AT_12PM);
        runCommand(READ_CHARLIE_WALL, AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(
                CHARLIE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + CHARLIE_EXAMPLE_POST + fifteenSecondsAgo() + NEW_LINE +
                        BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_TWO + minutesAgo(1) + NEW_LINE +
                        BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + minutesAgo(2) + NEW_LINE +
                        ALICE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + ALICE_EXAMPLE_POST + minutesAgo(5) + NEW_LINE
        );
    }

    @Test
    void timelinesAreAlwaysDisplayedInReverseOrderEvenAfterDifferentCommandsAreEntered() throws IOException {
        runCommand(BOB_EXAMPLE_POST_COMMAND_ONE, AT_2_HOURS_BEFORE_12PM);
        runCommand(BOB_EXAMPLE_POST_COMMAND_TWO, AT_5_MINUTES_BEFORE_12PM);
        runCommand(READ_BOB_TIMELINE, AT_2_MINUTES_BEFORE_12PM);

        runCommand(BOB_EXAMPLE_POST_COMMAND_THREE, AT_15_SECONDS_BEFORE_12PM);
        runCommand(READ_BOB_TIMELINE, AT_12PM);

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_EXAMPLE_POST_TWO + minutesAgo(3) + NEW_LINE +
                        BOB_EXAMPLE_POST_ONE + hoursAgo(1) + NEW_LINE +

                        BOB_EXAMPLE_POST_THREE + fifteenSecondsAgo() + NEW_LINE +
                        BOB_EXAMPLE_POST_TWO + minutesAgo(5) + NEW_LINE +
                        BOB_EXAMPLE_POST_ONE + hoursAgo(2) + NEW_LINE
        );
    }

    private void runAliceAndBobPostCommands() throws IOException {
        runCommand(ALICE_EXAMPLE_POST_COMMAND, AT_5_MINUTES_BEFORE_12PM);
        runCommand(BOB_EXAMPLE_POST_COMMAND_ONE, AT_2_MINUTES_BEFORE_12PM);
        runCommand(BOB_EXAMPLE_POST_COMMAND_TWO, AT_1_MINUTE_BEFORE_12PM);
    }

    private void runCommand(String command, LocalDateTime timeOfCommand) throws IOException {
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

    private String fifteenSecondsAgo() {
        return " (15 seconds ago)";
    }

    private String hoursAgo(int hours) {
        return hours == 1
                ? " (" + hours + " hour ago)"
                : " (" + hours + " hours ago)";
    }
}
