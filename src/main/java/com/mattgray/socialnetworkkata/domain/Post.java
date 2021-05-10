package com.mattgray.socialnetworkkata.domain;

import java.time.LocalDateTime;

public class Post {

    private final String userName;
    private final String post;
    private final LocalDateTime timeOfPost;

    public Post(String userName, String post, LocalDateTime timeOfPost) {
        this.userName = userName;
        this.post = post;
        this.timeOfPost = timeOfPost;
    }

    public String getPost() {
        return post;
    }

    public LocalDateTime getTimeOfPost() {
        return timeOfPost;
    }

    public String getUserName() {
        return userName;
    }
}
