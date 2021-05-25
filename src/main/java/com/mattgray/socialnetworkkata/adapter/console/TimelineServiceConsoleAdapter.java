package com.mattgray.socialnetworkkata.adapter.console;

import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TimelineServiceConsoleAdapter implements TimelineService {

    private final ClockService clockService;

    public TimelineServiceConsoleAdapter(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayTimeLine(ArrayList<Post> posts, LocalDateTime timeOfReadCommand) {
        ArrayList<Post> usersTimeline = new ArrayList<>(posts);
        Collections.reverse(usersTimeline);
        for (Post post : usersTimeline) {
            System.out.println(post.getPost() + clockService.getTimeBetween(post.getTimeOfPost(), timeOfReadCommand));
        }
    }
}
