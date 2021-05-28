package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.adapter.web.domain.PostForTimeline;
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
        ArrayList<PostForTimeline> formattedPosts = new ArrayList<>();
        posts.forEach(post -> formattedPosts.add(new PostForTimeline(clockService.getTimeBetween(post.getTimeOfPost(), time), post.getPost())));
        Collections.reverse(formattedPosts);

        return new JSONArray(formattedPosts).toString();
    }
}
