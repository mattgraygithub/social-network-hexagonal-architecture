package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimelineServiceImpl implements TimelineService {

    @Override
    public void displayTimeLine(ArrayList<Post> timeline, LocalDateTime timeOfCommand) {
        for (Post post : timeline) {
            System.out.println(post.getPost() + " (" + ChronoUnit.MINUTES.between(post.getTimeOfPost(), timeOfCommand) + " minutes ago)");
        }
    }
}
