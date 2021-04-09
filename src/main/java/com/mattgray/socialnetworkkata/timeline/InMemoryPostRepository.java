package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class InMemoryPostRepository implements PostRepository {

    private final ArrayList<Post> posts;

    public InMemoryPostRepository(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public void addPost(String post, LocalDateTime time) {
        posts.add(new Post(post, time));
    }

    @Override
    public ArrayList<Post> getPosts() {
        return posts;
    }
}
