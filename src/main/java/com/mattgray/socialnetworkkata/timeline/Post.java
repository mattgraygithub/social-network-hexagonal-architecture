package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;

public class Post {

    private final String post;
    private final LocalDateTime timeOfPost;

    public Post(String post, LocalDateTime timeOfPost) {
        this.post = post;
        this.timeOfPost = timeOfPost;
    }

    public String getPost() {
        return post;
    }

    public LocalDateTime getTimeOfPost() {
        return timeOfPost;
    }
}
