package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.following.WallService;
import com.mattgray.socialnetworkkata.posting.TimelineService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class UserService {
    private final UserRepository userRepository;
    private final TimelineService timelineService;
    private final WallService wallService;

    public UserService(UserRepository userRepository, TimelineService timelineService, WallService wallService) {
        this.userRepository = userRepository;
        this.timelineService = timelineService;
        this.wallService = wallService;
    }

    public void addPost(String command, LocalDateTime time) {
        userRepository.addPost(getUserName(command), getCommandArgument(command), time);
    }

    public void getTimeLine(String userName, LocalDateTime time) {
        timelineService.displayTimeLine(userRepository.getPostsFor(userName).getPosts(), time);
    }

    public void addFollowee(String command) {
        userRepository.addFollowee(getUserName(command), getCommandArgument(command));
    }

    public void getWall(String command, LocalDateTime time) {
        String user = getUserName(command);
        wallService.displayWall(userRepository.getUser(user), userRepository.getFollowedUsersFor(user), time);
    }

    private String getUserName(String command) {
        return command.split(" ")[0];
    }

    private String getCommandArgument(String command) {
        String[] commandAsArray = command.split(" ");
        String[] post = Arrays.copyOfRange(commandAsArray, 2, commandAsArray.length);
        return String.join(" ", post);
    }
}
