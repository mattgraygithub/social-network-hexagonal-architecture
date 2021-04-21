package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.common.ClockService;
import com.mattgray.socialnetworkkata.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class WallServiceImpl implements WallService {

    private static final String DELIMITER_BETWEEN_USERNAME_AND_POST = " - ";
    private final ClockService clockService;

    public WallServiceImpl(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayWall(User user, ArrayList<User> followedUsers, LocalDateTime timeOfCommand) {

        ArrayList<Post> posts = user.getPosts().getPosts();

        for (int i = posts.size() - 1; i >= 0; i--) {
            System.out.println(user.getUserName() + DELIMITER_BETWEEN_USERNAME_AND_POST + posts.get(i).getPost() + clockService.getTimeBetween(posts.get(i).getTimeOfPost(), timeOfCommand));
        }
    }
}
