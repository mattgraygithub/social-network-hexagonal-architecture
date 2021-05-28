package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.adapter.web.domain.PostForWall;
import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.WallService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class WallServiceHTTPAdapter implements WallService {

    private static final int OK_STATUS_CODE = 200;
    private HttpExchange exchange;
    private ClockService clockService;

    public WallServiceHTTPAdapter(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public void displayWall(User user, ArrayList<User> followedUsers, LocalDateTime time){
        ArrayList<Post> posts = new ArrayList<>(user.getPosts().getPosts());

        for (User followedUser : followedUsers) {
            posts.addAll(followedUser.getPosts().getPosts());
        }

        posts.sort(Comparator.comparing(Post::getTimeOfPost).reversed());

        ArrayList<PostForWall> formattedPosts = new ArrayList<>();

        posts.forEach(post -> formattedPosts.add(new PostForWall(post.getUserName(), clockService.getTimeBetween(post.getTimeOfPost(), time),post.getPost())));

        writeResponseBody(exchange, new JSONArray(formattedPosts).toString());
    }

    public void setExchange(HttpExchange exchange) {
        this.exchange = exchange;
    }

    private void writeResponseBody(HttpExchange exchange, String responseBody){
        final byte[] rawResponseBody = responseBody.getBytes();
        try {
            exchange.sendResponseHeaders(OK_STATUS_CODE, rawResponseBody.length);
            exchange.getResponseBody().write(rawResponseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
