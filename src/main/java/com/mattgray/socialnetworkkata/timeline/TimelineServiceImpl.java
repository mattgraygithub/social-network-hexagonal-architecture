package com.mattgray.socialnetworkkata.timeline;

import com.mattgray.socialnetworkkata.common.ClockService;
import com.mattgray.socialnetworkkata.common.ClockServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TimelineServiceImpl implements TimelineService {

    private final ClockService clockService = new ClockServiceImpl();

    @Override
    public void displayTimeLine(ArrayList<Post> timeline, LocalDateTime timeOfReadCommand) {
        for (Post post : timeline) {
            System.out.println(post.getPost() + clockService.getTimeBetween(post.getTimeOfPost(), timeOfReadCommand));
        }
    }
}
