package com.mattgray.socialnetworkkata.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryPostRepository that = (InMemoryPostRepository) o;
        return Objects.equals(posts, that.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posts);
    }
}
