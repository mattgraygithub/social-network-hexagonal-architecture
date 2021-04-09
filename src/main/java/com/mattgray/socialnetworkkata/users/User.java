package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.followees.FolloweeRepository;
import com.mattgray.socialnetworkkata.timeline.Timeline;

public class User {

    private final String userName;
    private final Timeline timeline;
    private final FolloweeRepository followeeRepository;

    public User(String userName, Timeline timeline, FolloweeRepository followeeRepository) {
        this.userName = userName;
        this.timeline = timeline;
        this.followeeRepository = followeeRepository;
    }

    public String getUserName() {
        return userName;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public FolloweeRepository getFollowees() {
        return followeeRepository;
    }
}
