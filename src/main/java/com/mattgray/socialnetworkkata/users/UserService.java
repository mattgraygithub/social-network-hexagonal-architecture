package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.timeline.TimelineService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class UserService {
    private final UserRepository userRepository;
    private final TimelineService timelineService;

    public UserService(UserRepository userRepository, TimelineService timelineService) {
        this.userRepository = userRepository;
        this.timelineService = timelineService;
    }

    public void addPost(String command, LocalDateTime time) {
        userRepository.addPost(getUserName(command), getCommandArgument(command), time);
    }

    public void addFollowee(String command) {
        userRepository.addFollowee(getUserName(command), getCommandArgument(command));
    }

    private String getUserName(String command) {
        return command.split(" ")[0];
    }

    private String getCommandArgument(String command) {
        String[] commandAsArray = command.split(" ");
        String[] post = Arrays.copyOfRange(commandAsArray, 2, commandAsArray.length);
        return String.join(" ", post);
    }

    public void getTimeLine(String userName, LocalDateTime time) {
        timelineService.displayTimeLine(userRepository.getTimelineFor(userName).getPosts(), time);
    }
}
