import java.time.LocalDateTime;

public class TestCommands {
    static final String POST_COMMAND = " -> ";
    static final String FOLLOW_COMMAND = " follows ";
    static final String READ_WALL_COMMAND = " wall ";
    static final String ALICE_USER_NAME = "Alice";
    static final String ALICE_EXAMPLE_POST = "I love the weather today";
    static final String ALICE_EXAMPLE_POST_COMMAND = ALICE_USER_NAME + POST_COMMAND + ALICE_EXAMPLE_POST;
    static final String READ_ALICE_TIMELINE = "Alice";
    static final String BOB_USER_NAME = "Bob";
    static final String BOB_EXAMPLE_POST_ONE = "Damn! We lost!";
    static final String BOB_EXAMPLE_POST_COMMAND_ONE = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_ONE;
    static final String BOB_EXAMPLE_POST_TWO = "Good game though.";
    static final String BOB_EXAMPLE_POST_COMMAND_TWO = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_TWO;
    static final String READ_BOB_TIMELINE = "Bob";
    static final String CHARLIE_USER_NAME = "Charlie";
    static final String CHARLIE_EXAMPLE_POST = "I'm in New York today! Anyone want to have a coffee?";
    static final String CHARLIE_EXAMPLE_POST_COMMAND = CHARLIE_USER_NAME + POST_COMMAND + CHARLIE_EXAMPLE_POST;
    static final String CHARLIE_FOLLOWS_ALICE = CHARLIE_USER_NAME + FOLLOW_COMMAND + ALICE_USER_NAME;
    static final String CHARLIE_FOLLOWS_BOB = CHARLIE_USER_NAME + FOLLOW_COMMAND + BOB_USER_NAME;
    static final String READ_CHARLIE_WALL = CHARLIE_USER_NAME + READ_WALL_COMMAND;
    static final String NEW_LINE = System.getProperty("line.separator");
    static final LocalDateTime AT_12PM = LocalDateTime.of(2019, 6, 21, 12, 0, 0);
    static final LocalDateTime AT_15_SECONDS_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 59, 45);
    static final LocalDateTime AT_5_MINUTES_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 55, 0);
    static final LocalDateTime AT_2_MINUTES_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 58, 0);
    static final LocalDateTime AT_1_MINUTE_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 59, 0);
}