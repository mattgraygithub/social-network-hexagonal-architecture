package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CommandProcessor {

    private static final String POST_COMMAND = "->";
    private static final String FOLLOW_COMMAND = "follows";
    private static final String WALL_COMMAND = "wall";

    private final UserService userService;

    public CommandProcessor(UserService userService) {
        this.userService = userService;
    }

    public void process(String command, LocalDateTime time) {
        if (isPost(command)) {
            userService.addPost(command, time);
        }

        if (isRead(command)) {
            userService.getTimeLine(command, time);
        }

        if (isFollow(command)) {
            userService.addFollowee(command);
        }

        if (isWall(command)) {
            userService.getWall(command, time);
        }
    }

    private boolean isPost(String command) {
        return Arrays.asList(command.split(" ")).contains(POST_COMMAND);
    }

    private boolean isRead(String command) {
        return Arrays.asList(command.split(" ")).size() == 1;
    }

    private boolean isFollow(String command) {
        return Arrays.asList(command.split(" ")).contains(FOLLOW_COMMAND);
    }

    private boolean isWall(String command) {
        return Arrays.asList(command.split(" ")).contains(WALL_COMMAND);
    }
}
