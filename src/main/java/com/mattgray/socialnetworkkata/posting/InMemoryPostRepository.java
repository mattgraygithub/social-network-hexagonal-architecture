package com.mattgray.socialnetworkkata.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class InMemoryPostRepository implements PostRepository {

    private final ArrayList<Post> posts;

    public InMemoryPostRepository(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public void addPost(String userName, String post, LocalDateTime time) {
        posts.add(new Post(userName, post, time));
    }

    @Override
    public ArrayList<Post> getPosts() {
        return posts;
    }
}
