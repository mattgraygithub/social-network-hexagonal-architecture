package com.mattgray.socialnetworkkata.adapter.console;

import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ConsoleUserController implements UserController {

    private static final String POST_COMMAND = "->";
    private static final String FOLLOW_COMMAND = "follows";
    private static final String WALL_COMMAND = "wall";

    private final UserService userService;
    private final TimelineService timelineService;

    public ConsoleUserController(UserService userService, TimelineService timelineService) {
        this.userService = userService;
        this.timelineService = timelineService;
    }

    @Override
    public void process(Clock clock) {

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {

            String command = scanner.nextLine();
            if (isPost(command)) {
                userService.addPost(command, LocalDateTime.now(clock));
            }

            if (isRead(command)) {
                ArrayList<Post> posts = userService.getPosts(command);
                System.out.print(timelineService.getTimeLine(posts, LocalDateTime.now(clock)));
            }

            if (isFollow(command)) {
                userService.addFollowee(command);
            }

            if (isWall(command)) {
                userService.getWall(command, LocalDateTime.now(clock));
            }
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
