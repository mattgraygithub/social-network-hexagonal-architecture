package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.common.ClockService;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TimelineServiceImpl implements TimelineService {

    private final ClockService clockService;

    public TimelineServiceImpl(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayTimeLine(ArrayList<Post> posts, LocalDateTime timeOfReadCommand) {
        for (Post post : posts) {
            System.out.println(post.getPost() + clockService.getTimeBetween(post.getTimeOfPost(), timeOfReadCommand));
        }
    }
}
