package com.mattgray.socialnetworkkata.posts;

import java.time.LocalDateTime;

public interface WallService {
    void displayWall(String userName, LocalDateTime now);
}
