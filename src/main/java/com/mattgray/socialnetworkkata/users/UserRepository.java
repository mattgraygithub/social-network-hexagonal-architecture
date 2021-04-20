package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.posts.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface UserRepository {

    void addPost(String userName, String examplePost, LocalDateTime now);

    PostRepository getPostsFor(String userName);

    void addFollowee(String userName, String followee);

    ArrayList<User> getFollowedUsersFor(String userName);
}
