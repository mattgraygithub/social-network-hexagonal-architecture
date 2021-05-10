package com.mattgray.socialnetworkkata.port;

import com.mattgray.socialnetworkkata.posting.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface TimelineService {

    void displayTimeLine(ArrayList<Post> posts, LocalDateTime time);
}
