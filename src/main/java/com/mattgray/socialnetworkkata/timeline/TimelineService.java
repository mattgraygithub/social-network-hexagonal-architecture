package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TimelineService {
    private final TimelineRepository timelineRepository;

    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public void addPost(String command, LocalDateTime time) {
        timelineRepository.add(getPost(command), getUserName(command), time);
    }

    private String getPost(String command) {
        String[] commandAsArray = command.split(" ");
        String[] post = Arrays.copyOfRange(commandAsArray, 2, commandAsArray.length);
        return String.join(" ", post);
    }

    private String getUserName(String command) {
        return command.split(" ")[0];
    }
}
