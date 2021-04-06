package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimelineServiceImpl implements TimelineService {

    public TimelineServiceImpl() {

    }

    @Override
    public void displayTimeLine(ArrayList<Post> timeline, LocalDateTime timeOfCommand) {

        System.out.println(timeline.get(0).getPost() + " (" + ChronoUnit.MINUTES.between(timeline.get(0).getTimeOfPost(), timeOfCommand) + " minutes ago)");
    }


}
