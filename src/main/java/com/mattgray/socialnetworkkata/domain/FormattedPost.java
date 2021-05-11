package com.mattgray.socialnetworkkata.domain;

public class FormattedPost {

    private final String post;
    private final String timeAgo;

    public FormattedPost(String post, String timeAgo) {
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
