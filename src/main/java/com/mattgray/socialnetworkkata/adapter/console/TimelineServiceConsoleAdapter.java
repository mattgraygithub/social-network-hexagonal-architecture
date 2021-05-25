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
    public String getTimeLine(ArrayList<Post> posts, LocalDateTime timeOfReadCommand) {
        ArrayList<Post> usersTimeline = new ArrayList<>(posts);
        Collections.reverse(usersTimeline);
        StringBuilder formattedPosts = new StringBuilder();
        for (Post post : usersTimeline) {
            formattedPosts.append(post.getPost() + clockService.getTimeBetween(post.getTimeOfPost(), timeOfReadCommand) + "\n");
        }
        return formattedPosts.toString();
    }
}
