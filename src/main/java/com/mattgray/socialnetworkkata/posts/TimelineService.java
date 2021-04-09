package com.mattgray.socialnetworkkata.posts;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface TimelineService {

    void displayTimeLine(ArrayList<Post> posts, LocalDateTime time);
}
