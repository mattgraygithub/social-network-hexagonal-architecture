package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.domain.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TestData {
    public static final String POST_COMMAND = " -> ";
    public static final String FOLLOW_COMMAND = " follows ";
    public static final String READ_WALL_COMMAND = " wall";
    public static final String ALICE_USER_NAME = "alice";
    public static final String ALICE_EXAMPLE_POST = "I love the weather today";
    public static final String ALICE_EXAMPLE_POST_COMMAND = ALICE_USER_NAME + POST_COMMAND + ALICE_EXAMPLE_POST;
    public static final String READ_ALICE_TIMELINE = "alice";
    public static final String BOB_USER_NAME = "bob";
    public static final String BOB_EXAMPLE_POST_ONE = "Damn! We lost!";
    public static final String BOB_EXAMPLE_POST_COMMAND_ONE = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_ONE;
    public static final String BOB_EXAMPLE_POST_TWO = "Good game though.";
    public static final String BOB_EXAMPLE_POST_COMMAND_TWO = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_TWO;
    public static final String BOB_EXAMPLE_POST_THREE = "Never mind.";
    public static final String BOB_EXAMPLE_POST_COMMAND_THREE = BOB_USER_NAME + POST_COMMAND + BOB_EXAMPLE_POST_THREE;
    public static final String READ_BOB_TIMELINE = "bob";
    public static final String CHARLIE_USER_NAME = "charlie";
    public static final String CHARLIE_EXAMPLE_POST = "I'm in New York today! Anyone want to have a coffee?";
    public static final String CHARLIE_EXAMPLE_POST_COMMAND = CHARLIE_USER_NAME + POST_COMMAND + CHARLIE_EXAMPLE_POST;
    public static final String CHARLIE_FOLLOWS_ALICE = CHARLIE_USER_NAME + FOLLOW_COMMAND + ALICE_USER_NAME;
    public static final String CHARLIE_FOLLOWS_BOB = CHARLIE_USER_NAME + FOLLOW_COMMAND + BOB_USER_NAME;
    public static final String READ_CHARLIE_WALL = CHARLIE_USER_NAME + READ_WALL_COMMAND;
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String DELIMITER_BETWEEN_USERNAME_AND_POST = " - ";
    public static final String HTTP_LOCALHOST = "http://localhost:";
    public static final String POST_REQUEST = "POST";
    public static final String GET_REQUEST = "GET";
    public static final String POSTS_PATH = "/posts/";
    public static final String FOLLOW_PATH = "/follow/";
    public static final LocalDateTime AT_12PM = LocalDateTime.of(2019, 6, 21, 12, 0, 0);
    public static final LocalDateTime AT_15_SECONDS_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 59, 45);
    public static final LocalDateTime AT_5_MINUTES_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 55, 0);
    public static final LocalDateTime AT_2_MINUTES_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 58, 0);
    public static final LocalDateTime AT_1_MINUTE_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 11, 59, 0);
    public static final LocalDateTime AT_2_HOURS_BEFORE_12PM = LocalDateTime.of(2019, 6, 21, 10, 0, 0);
    public static final String ONE_MINUTE_AGO = " (1 minute ago)";
    public static final String TWO_MINUTES_AGO = " (2 minutes ago)";
    public static final String FIVE_MINUTES_AGO = " (5 minutes ago)";
    public static final ArrayList<Post> ALICE_EXAMPLE_POST_LIST = new ArrayList<>(Collections.singletonList(new Post(ALICE_USER_NAME, ALICE_EXAMPLE_POST, AT_5_MINUTES_BEFORE_12PM)));
    public static final String ALICE_EXPECTED_JSON_RESPONSE = "[{\"timeAgo\":\" (5 minutes ago)\",\"post\":\"" + ALICE_EXAMPLE_POST + "\"}]";

}
