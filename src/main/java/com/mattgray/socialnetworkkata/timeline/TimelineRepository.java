package com.mattgray.socialnetworkkata.timeline;

import java.time.LocalDateTime;

public interface TimelineRepository {

    void add(String post, String userName, LocalDateTime now);
}
