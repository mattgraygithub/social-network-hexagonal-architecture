package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.timeline.Post;
import com.mattgray.socialnetworkkata.timeline.Timeline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public void addPost(String userName, String post, LocalDateTime time) {
        if (userExists(userName)) addPostToExistingUser(userName, post, time);
        else addPostForNewUser(userName, post, time);
    }

    @Override
    public Timeline getTimelineFor(String userName) {
        return users.get(getUserIndexOf(userName)).getTimeline();
    }

    private boolean userExists(String userName) {
        return users.stream().map(User::getUserName).anyMatch(userName::equals);
    }

    private int getUserIndexOf(String userName) {
        return users.indexOf(users.stream().filter(i -> userName.equals(i.getUserName())).findAny().orElse(null));
    }

    private void addPostToExistingUser(String userName, String post, LocalDateTime time) {

        Timeline userTimeline = getTimelineFor(userName);
        userTimeline.addPost(post, time);

        users.set(getUserIndexOf(userName), new User(userName, userTimeline));
    }

    private void addPostForNewUser(String userName, String post, LocalDateTime time) {

        Timeline timeline = new Timeline(new ArrayList<>(Collections.singletonList(new Post(post, time))));
        users.add(new User(userName, timeline));
    }
}
