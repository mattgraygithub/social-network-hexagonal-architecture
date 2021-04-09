package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.followees.FolloweeRepository;
import com.mattgray.socialnetworkkata.followees.InMemoryFolloweeRepository;
import com.mattgray.socialnetworkkata.timeline.Post;
import com.mattgray.socialnetworkkata.timeline.Timeline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final FolloweeRepository followeeRepository;
    private final List<User> users;

    public InMemoryUserRepository(FolloweeRepository followeeRepository, List<User> users) {
        this.followeeRepository = followeeRepository;
        this.users = users;
    }

    @Override
    public void addPost(String userName, String post, LocalDateTime time) {
        if (userExists(userName)) {
            addPostToExistingUser(userName, post, time);
        } else {
            addNewUserAndPost(userName, post, time);
        }
    }

    @Override
    public Timeline getTimelineFor(String userName) {
        return users.get(getUserIndexOf(userName)).getTimeline();
    }

    @Override
    public void addFollowee(String userName, String followeeUserName) {
        if (userExists(userName)) {
            addFolloweeToExistingUser(userName, followeeUserName);
        } else {
            addNewUserAndFollowee(userName, followeeUserName);
        }
    }

    @Override
    public FolloweeRepository getFollowedUsersFor(String userName) {
        throw new UnsupportedOperationException();
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
        FolloweeRepository followeeRepository = users.get(getUserIndexOf(userName)).getFollowees();
        updateUser(userName, userTimeline, followeeRepository);
    }

    private void addNewUserAndPost(String userName, String post, LocalDateTime time) {
        Timeline timeline = new Timeline(new ArrayList<>(Collections.singletonList(new Post(post, time))));
        addUser(userName, timeline, new InMemoryFolloweeRepository());
    }

    private void addFolloweeToExistingUser(String userName, String followeeUserName) {
        Timeline userTimeline = getTimelineFor(userName);
        FolloweeRepository followeeRepository = users.get(getUserIndexOf(userName)).getFollowees();
        followeeRepository.addFollowee(followeeUserName);
        updateUser(userName, userTimeline, followeeRepository);
    }

    private void addNewUserAndFollowee(String userName, String followeeUserName) {
        followeeRepository.addFollowee(followeeUserName);
        users.add(new User(userName, new Timeline(new ArrayList<>()), followeeRepository));
    }

    private void updateUser(String userName, Timeline userTimeline, FolloweeRepository followeeRepository) {
        users.set(getUserIndexOf(userName), new User(userName, userTimeline, followeeRepository));
    }

    private void addUser(String userName, Timeline timeline, FolloweeRepository followeeRepository) {
        users.add(new User(userName, timeline, followeeRepository));
    }
}
