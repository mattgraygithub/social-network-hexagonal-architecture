package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.users.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CommandProcessor {

    static final String POST_COMMAND = "->";

    private final UserService userService;

    public CommandProcessor(UserService userService) {
        this.userService = userService;
    }

    public void process(String command, LocalDateTime time) {

        if (Arrays.asList(command.split(" ")).contains(POST_COMMAND)) {
            userService.addPost(command, time);
        }

        if (Arrays.asList(command.split(" ")).size() == 1) {
            userService.getTimeLine(command, time);
        }
    }
}
