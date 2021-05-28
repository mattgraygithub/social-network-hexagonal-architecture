package com.mattgray.socialnetworkkata.adapter.web.domain;

public class PostForTimeline {

    private final String timeAgo;
    private final String post;

    public PostForTimeline(String timeAgo, String post) {
        this.timeAgo = timeAgo;
        this.post = post;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getPost() {
        return post;
    }
}
