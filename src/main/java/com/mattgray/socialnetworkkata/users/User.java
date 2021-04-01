package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.timeline.Timeline;

public class User {

    private final String userName;
    private final Timeline timeline;

    public User(String userName, Timeline timeline) {
        this.userName = userName;
        this.timeline = timeline;
    }

    public String getUserName() {
        return userName;
    }

    public Timeline getTimeline() {
        return timeline;
    }
}
