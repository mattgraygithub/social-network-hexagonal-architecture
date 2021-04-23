package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.clock.ClockService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TimelineServiceImpl implements TimelineService {

    private final ClockService clockService;

    public TimelineServiceImpl(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayTimeLine(ArrayList<Post> posts, LocalDateTime timeOfReadCommand) {
        Collections.reverse(posts);
        for (Post post : posts) {
            System.out.println(post.getPost() + clockService.getTimeBetween(post.getTimeOfPost(), timeOfReadCommand));
        }
    }
}
