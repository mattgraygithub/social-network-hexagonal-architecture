package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.common.ClockService;
import com.mattgray.socialnetworkkata.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class WallServiceImpl implements WallService {

    private final ClockService clockService;

    public WallServiceImpl(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayWall(User user, ArrayList<User> followedUsers, LocalDateTime timeOfCommand) {

        String post = user.getPosts().getPosts().get(0).getPost();
        LocalDateTime timeOfPost = user.getPosts().getPosts().get(0).getTimeOfPost();

        System.out.println(user.getUserName() + " - " + post + clockService.getTimeBetween(timeOfPost, timeOfCommand));
    }
}
