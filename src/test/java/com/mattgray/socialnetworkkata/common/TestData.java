package com.mattgray.socialnetworkkata.common;

import java.time.LocalDateTime;

public class TestData {
    public static final String POST_COMMAND = " -> ";
    public static final String FOLLOW_COMMAND = " follows ";
    public static final String READ_WALL_COMMAND = " wall";
    public static final String ALICE_USER_NAME = "Alice";
    public static final String ALICE_EXAMPLE_POST = "I love the weather today";
    public static final String ALICE_EXAMPLE_POST_COMMAND = ALICE_USER_NAME + POST_COMMAND + ALICE_EXAMPLE_POST;
    public static final String READ_ALICE_TIMELINE = "Alice";
    public static final String BOB_USER_NAME = "Bob";
    public static final String BOB_EXAMPLE_POST_ONE = "Damn! We lost!";
    public static final String BOB_EXAMPLE_POST_COMMAND_ONE = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_ONE;
    public static final String BOB_EXAMPLE_POST_TWO = "Good game though.";
    public static final String BOB_EXAMPLE_POST_COMMAND_TWO = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_TWO;
    public static final String READ_BOB_TIMELINE = "Bob";
    public static final String CHARLIE_USER_NAME = "Charlie";
    public static final String CHARLIE_EXAMPLE_POST = "I'm in New York today! Anyone want to have a coffee?";
    public static final String CHARLIE_EXAMPLE_POST_COMMAND = CHARLIE_USER_NAME + POST_COMMAND + CHARLIE_EXAMPLE_POST;
    public static final String CHARLIE_FOLLOWS_ALICE = CHARLIE_USER_NAME + FOLLOW_COMMAND + ALICE_USER_NAME;
    public static final String CHARLIE_FOLLOWS_BOB = CHARLIE_USER_NAME + FOLLOW_COMMAND + BOB_USER_NAME;
    public static final String READ_CHARLIE_WALL = CHARLIE_USER_NAME + READ_WALL_COMMAND;
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final LocalDateTime AT_12PM = LocalDateTime.of(2019, 6, 21, 12, 0, 0);
    public static final LocalDateTime AT_15_SECONDS_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 59, 45);
    public static final LocalDateTime AT_5_MINUTES_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 55, 0);
    public static final LocalDateTime AT_2_MINUTES_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 58, 0);
    public static final LocalDateTime AT_1_MINUTE_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 59, 0);
    public static final LocalDateTime AT_2_HOURS_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 10, 0, 0);
}
