package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.timeline.TimelineService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CommandProcessor {

    static final String POST_COMMAND = "->";

    private final TimelineService timelineService;
    private final FollowerService followerService;

    public CommandProcessor(TimelineService timelineService, FollowerService followerService) {
        this.timelineService = timelineService;
        this.followerService = followerService;
    }

    public void process(String command, LocalDateTime time) {

        if (Arrays.asList(command.split(" ")).contains(POST_COMMAND)){
            timelineService.post(command, time);
        }
    }
}
