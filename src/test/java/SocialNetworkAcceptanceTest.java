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

    private static final String POST_COMMAND = " -> ";
    private static final String FOLLOW_COMMAND = " follows ";
    private static final String READ_WALL_COMMAND = " wall ";
    private static final String ALICE_USER_NAME = "Alice";
    private static final String ALICE_EXAMPLE_POST = "I love the weather today";
    private static final String ALICE_EXAMPLE_POST_COMMAND = ALICE_USER_NAME + POST_COMMAND + ALICE_EXAMPLE_POST;
    private static final String READ_ALICE_TIMELINE = "Alice";
    private static final String BOB_USER_NAME = "Bob";
    private static final String BOB_EXAMPLE_POST_ONE = "Damn! We lost!";
    private static final String BOB_EXAMPLE_POST_COMMAND_ONE = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_ONE;
    private static final String BOB_EXAMPLE_POST_TWO = "Good game though.";
    private static final String BOB_EXAMPLE_POST_COMMAND_TWO = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_TWO;
    private static final String READ_BOB_TIMELINE = "Bob";
    private static final String CHARLIE_USER_NAME = "Charlie";
    private static final String CHARLIE_EXAMPLE_POST = "I'm in New York today! Anyone want to have a coffee?";
    private static final String CHARLIE_EXAMPLE_POST_COMMAND = CHARLIE_USER_NAME + POST_COMMAND + CHARLIE_EXAMPLE_POST;
    private static final String CHARLIE_FOLLOWS_ALICE = CHARLIE_USER_NAME + FOLLOW_COMMAND + ALICE_USER_NAME;
    private static final String CHARLIE_FOLLOWS_BOB = CHARLIE_USER_NAME + FOLLOW_COMMAND + BOB_USER_NAME;
    private static final String READ_CHARLIE_WALL = CHARLIE_USER_NAME + READ_WALL_COMMAND;
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final LocalDateTime AT_12PM = LocalDateTime.of(2019,6,21,12,0,0);
    private static final LocalDateTime AT_15_SECONDS_BEFORE_12PM = LocalDateTime.of(2019,6,21,11,59,45);
    private static final LocalDateTime AT_5_MINUTES_BEFORE_12PM = LocalDateTime.of(2019,6,21,11,55,0);
    private static final LocalDateTime AT_2_MINUTES_BEFORE_12PM = LocalDateTime.of(2019,6,21,11,58,0);
    private static final LocalDateTime AT_1_MINUTE_BEFORE_12PM = LocalDateTime.of(2019,6,21,11,59,0);

    ByteArrayOutputStream byteArrayOutputStream;
    SocialNetwork socialNetwork;
    Clock clockStub;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        socialNetwork = new SocialNetwork(new CommandProcessor(new TimelineService(), new FollowerService()), clockStub);
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
        Clock fixedClock = Clock.fixed(timeOfCommand.toInstant(ZoneOffset.UTC),ZoneId.systemDefault());
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
