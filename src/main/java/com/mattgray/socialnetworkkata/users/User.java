package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.followees.FolloweeRepository;
import com.mattgray.socialnetworkkata.posts.PostRepository;

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

    public FolloweeRepository getFollowedUsers() {
        return followeeRepository;
    }
}
