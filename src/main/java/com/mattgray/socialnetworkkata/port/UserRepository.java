package com.mattgray.socialnetworkkata.port;

import com.mattgray.socialnetworkkata.port.PostRepository;
import com.mattgray.socialnetworkkata.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface UserRepository {

    void addPost(String userName, String examplePost, LocalDateTime now);

    PostRepository getPostsFor(String userName);

    void addFollowee(String userName, String followee);

    User getUser(String userName);

    ArrayList<User> getFollowedUsersFor(String userName);
}
