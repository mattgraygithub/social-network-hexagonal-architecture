package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface WallService {
    void displayWall(User user, ArrayList<User> followedUsers, LocalDateTime time);
}
