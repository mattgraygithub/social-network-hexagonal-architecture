package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.following.FolloweeRepository;
import com.mattgray.socialnetworkkata.posting.PostRepository;

import java.util.Objects;

public class User {

    private final String userName;
    private final PostRepository postRepository;
    private final FolloweeRepository followeeRepository;

    public User(String userName, PostRepository postRepository, FolloweeRepository followeeRepository) {
        this.userName = userName;
        this.postRepository = postRepository;
        this.followeeRepository = followeeRepository;
    }

    public String getUserName() {
        return userName;
    }

    public PostRepository getPosts() {
        return postRepository;
    }

    public FolloweeRepository getFolloweeRepository() {
        return followeeRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(postRepository, user.postRepository) && Objects.equals(followeeRepository, user.followeeRepository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, postRepository, followeeRepository);
    }
}
