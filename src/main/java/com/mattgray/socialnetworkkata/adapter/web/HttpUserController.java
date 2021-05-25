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

    public HttpUserController(UserService userService, ClockService clockService, int serverPort) {
        this.userService = userService;
        this.clockService = clockService;
        this.serverPort = serverPort;
    }

    @Override
    public void process(Clock clock) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        createContextForAddingAndReadingPosts(clock, server);
        createContextForAddingFollowedUsers(server);
        server.start();
    }

    private void createContextForAddingAndReadingPosts(Clock clock, HttpServer server) {
        String path = "/posts/";
        server.createContext(path, exchange -> {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    String requestMethod = exchange.getRequestMethod();

                    switch (requestMethod) {
                        case "GET":
                            handleReadTimelineGetRequest(exchange, path, clock);
                            break;
                        case "POST":
                            handlePostingPostRequest(exchange, path, clock);
                            break;
                    }
                }
        );
    }

    private void createContextForAddingFollowedUsers(HttpServer server) {
        String path = "/follow/";
        server.createContext(path, exchange -> {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    if ("POST".equals(exchange.getRequestMethod())) {
                        handleAddFollowedUserPostRequest(exchange, path);
                    }
                }
        );
    }

    private void handleReadTimelineGetRequest(HttpExchange exchange, String path, Clock clock) throws IOException {
        String userName = exchange.getRequestURI().toString().substring(path.length());
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

    private void handlePostingPostRequest(HttpExchange exchange, String path, Clock clock) throws IOException {
        String userName = exchange.getRequestURI().toString().substring(path.length());

        InputStream inputStream = exchange.getRequestBody();
        Scanner scanner = new Scanner(inputStream);
        String post = scanner.nextLine();

        userService.addPost(userName + " -> " + post, LocalDateTime.now(clock));

        final String responseBody = "Added post: \"" + post + "\" to user: " + userName;
        final byte[] rawResponseBody = responseBody.getBytes();
        exchange.sendResponseHeaders(200, rawResponseBody.length);
        exchange.getResponseBody().write(rawResponseBody);

        exchange.close();
    }

    private void handleAddFollowedUserPostRequest(HttpExchange exchange, String path) throws IOException {
        String userName = exchange.getRequestURI().toString().substring(path.length());

        InputStream inputStream = exchange.getRequestBody();
        Scanner scanner = new Scanner(inputStream);
        String followedUser = scanner.nextLine();

        userService.addFollowee(userName + " follows " + followedUser);

        final String responseBody = "Added user: \"" + followedUser + "\" to list of followed users for user: " + userName;
        final byte[] rawResponseBody = responseBody.getBytes();
        exchange.sendResponseHeaders(200, rawResponseBody.length);
        exchange.getResponseBody().write(rawResponseBody);

        exchange.close();
    }
}
