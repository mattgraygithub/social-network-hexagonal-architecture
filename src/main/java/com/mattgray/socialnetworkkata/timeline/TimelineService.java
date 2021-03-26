package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TimelineService {
    private final TimelineRepository timelineRepository;

    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public void post(String command, LocalDateTime time) {
        timelineRepository.add(extractPostFrom(command), time);
    }

    private String extractPostFrom(String command) {
        String[] commandAsArray = command.split(" ");
        String[] post = Arrays.copyOfRange(command.split(" "), 2, commandAsArray.length);
        return String.join(" ", post);
    }
}
