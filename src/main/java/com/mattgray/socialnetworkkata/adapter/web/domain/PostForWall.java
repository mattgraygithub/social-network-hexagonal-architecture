package com.mattgray.socialnetworkkata.adapter.web.domain;

public class PostForWall {

    private final String userName;
    private final String timeAgo;
    private final String post;

    public PostForWall(String userName, String timeAgo, String post) {
        this.userName = userName;
        this.timeAgo = timeAgo;
        this.post = post;
    }

    public String getUserName() {
        return userName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public String getPost() {
        return post;
    }
}
