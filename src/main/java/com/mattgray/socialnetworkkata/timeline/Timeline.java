package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Timeline {

    private final ArrayList<Post> posts;

    public Timeline(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void addPost(String post, LocalDateTime time) {
        posts.add(new Post(post, time));
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
