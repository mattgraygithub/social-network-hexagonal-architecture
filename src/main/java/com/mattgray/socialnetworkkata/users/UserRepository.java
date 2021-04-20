package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.followees.FolloweeRepository;
import com.mattgray.socialnetworkkata.posts.PostRepository;

import java.time.LocalDateTime;

public interface UserRepository {

    void addPost(String userName, String examplePost, LocalDateTime now);

    PostRepository getPostsFor(String userName);

    void addFollowee(String userName, String followee);

    FolloweeRepository getFollowedUsersFor(String userName);
}
