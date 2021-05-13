package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.domain.FormattedPost;
import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HttpUserController implements UserController {

    private final UserService userService;
    private final ClockService clockService;
    private final int serverPort;
    private HttpServer server;

    public HttpUserController(UserService userService, ClockService clockService, int serverPort) {
        this.userService = userService;
        this.clockService = clockService;
        this.serverPort = serverPort;
    }

    @Override
    public void process(Clock clock) throws IOException {

        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        String path = "/";
        server.createContext(path, exchange -> {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    String requestMethod = exchange.getRequestMethod();

                    switch (requestMethod) {
                        case "GET":
                            handleReadWallGetRequest(exchange, clock);
                            break;
                        case "POST":
                            handlePostRequest(exchange, clock);
                            break;
                    }
                }
        );
        server.start();
    }

    private void handleReadWallGetRequest(HttpExchange exchange, Clock clock) throws IOException {
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

    private void handlePostRequest(HttpExchange exchange, Clock clock) throws IOException {
        String userName = exchange.getRequestURI().toString().substring(1);

        InputStream inputStream = exchange.getRequestBody();
        Scanner scanner = new Scanner(inputStream);
        StringBuilder post = new StringBuilder();
        while ((scanner.hasNext())) {
            post.append(scanner.nextLine());
        }

        userService.addPost(userName + " -> " + post, LocalDateTime.now(clock));

        final String responseBody = "Added post: \"" + post + "\" to user: " + userName;
        final byte[] rawResponseBody = responseBody.getBytes();
        exchange.sendResponseHeaders(200, rawResponseBody.length);
        exchange.getResponseBody().write(rawResponseBody);

        exchange.close();
    }
}
