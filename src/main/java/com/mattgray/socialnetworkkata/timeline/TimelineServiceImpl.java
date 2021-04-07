package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimelineServiceImpl implements TimelineService {

    @Override
    public void displayTimeLine(ArrayList<Post> timeline, LocalDateTime timeOfReadCommand) {
        for (Post post : timeline) {
            System.out.println(post.getPost() + getTimeBetween(post.getTimeOfPost(), timeOfReadCommand));
        }
    }

    private String getTimeBetween(LocalDateTime timeOfPost, LocalDateTime timeOfCommand) {

        long timeDifference = ChronoUnit.SECONDS.between(timeOfPost, timeOfCommand);

        if (timeDifference < 60) {
            return " (" + timeDifference + " seconds ago)";
        } else {
            return " (" + (timeDifference / 60) + " minutes ago)";
        }
    }
}
