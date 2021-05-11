package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.domain.FormattedPost;
import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class HttpUserController implements UserController {

    private final UserService userService;
    private final ClockService clockService;

    public HttpUserController(UserService userService, ClockService clockService) {
        this.userService = userService;
        this.clockService = clockService;
    }

    @Override
    public void process(Clock clock) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        String path = "/";
        server.createContext(path, exchange -> {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");

                    String userName = exchange.getRequestURI().toString().substring(1);
                    ArrayList<Post> posts = userService.getPosts(userName);

                    ArrayList<FormattedPost> formattedPosts = new ArrayList<>();

                    posts.forEach(post -> formattedPosts.add(new FormattedPost(post.getPost(), clockService.getTimeBetween(post.getTimeOfPost(), LocalDateTime.now(clock)))));

                    Collections.reverse(formattedPosts);

                    final String responseBody = new JSONArray(formattedPosts).toString();

                    final byte[] rawResponseBody = responseBody.getBytes();

                    exchange.sendResponseHeaders(200, rawResponseBody.length);
                    exchange.getResponseBody().write(rawResponseBody);

                    exchange.close();
                }
        );
        server.start();
    }
}
