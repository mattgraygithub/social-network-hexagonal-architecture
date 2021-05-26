package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import org.json.JSONArray;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class TimelineServiceHTTPAdapter implements TimelineService {

    private final ClockService clockService;

    public TimelineServiceHTTPAdapter(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public String getTimeLine(ArrayList<Post> posts, LocalDateTime time) {
        ArrayList<PostFormattedForJSON> formattedPosts = new ArrayList<>();
        posts.forEach(post -> formattedPosts.add(new PostFormattedForJSON(post.getPost(), clockService.getTimeBetween(post.getTimeOfPost(), time))));
        Collections.reverse(formattedPosts);

        return new JSONArray(formattedPosts).toString();
    }
}
