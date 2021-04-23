package com.mattgray.socialnetworkkata.posting;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post1 = (Post) o;
        return Objects.equals(userName, post1.userName) && Objects.equals(post, post1.post) && Objects.equals(timeOfPost, post1.timeOfPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, post, timeOfPost);
    }
}
