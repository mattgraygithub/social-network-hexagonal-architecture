package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.common.ClockService;
import com.mattgray.socialnetworkkata.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class WallServiceImpl implements WallService {

    private static final String DELIMITER_BETWEEN_USERNAME_AND_POST = " - ";
    private final ClockService clockService;

    public WallServiceImpl(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayWall(User user, ArrayList<User> followedUsers, LocalDateTime timeOfCommand) {
        ArrayList<Post> posts = new ArrayList<>(user.getPosts().getPosts());

        for (User followedUser : followedUsers) {
            posts.addAll(followedUser.getPosts().getPosts());
        }

        posts.sort(Comparator.comparing(Post::getTimeOfPost).reversed());

        for (Post post : posts) {
            System.out.println(post.getUserName() + DELIMITER_BETWEEN_USERNAME_AND_POST + post.getPost() + clockService.getTimeBetween(post.getTimeOfPost(), timeOfCommand));
        }
    }
}
