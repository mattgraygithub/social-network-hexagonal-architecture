package com.mattgray.socialnetworkkata.port;

import com.mattgray.socialnetworkkata.domain.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface TimelineService {

    String getTimeLine(ArrayList<Post> posts, LocalDateTime time);
}
