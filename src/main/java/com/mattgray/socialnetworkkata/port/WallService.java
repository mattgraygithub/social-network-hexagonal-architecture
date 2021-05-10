package com.mattgray.socialnetworkkata.port;

import com.mattgray.socialnetworkkata.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface WallService {
    void displayWall(User user, ArrayList<User> followedUsers, LocalDateTime time);
}
