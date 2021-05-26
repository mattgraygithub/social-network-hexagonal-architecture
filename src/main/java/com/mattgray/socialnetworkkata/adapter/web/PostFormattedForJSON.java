package com.mattgray.socialnetworkkata.adapter.web;

public class PostFormattedForJSON {

    private final String post;
    private final String timeAgo;

    public PostFormattedForJSON(String post, String timeAgo) {
        this.post = post;
        this.timeAgo = timeAgo;
    }

    public String getPost() {
        return post;
    }

    public String getTimeAgo() {
        return timeAgo;
    }
}
