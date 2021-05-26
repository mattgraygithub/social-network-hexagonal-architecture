package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class HTTPUserController implements UserController {

    private static final String POST_COMMAND = " -> ";
    private static final String FOLLOW_COMMAND = " follows ";
    private static final String POSTS_PATH = "/posts/";
    private static final String FOLLOW_PATH = "/follow/";
    private static final String POST_REQUEST = "POST";
    private static final String GET_REQUEST = "GET";
    private static final int OK_STATUS_CODE = 200;

    private final UserService userService;
    private final TimelineService timelineService;
    private final int serverPort;
    private Clock clock;
    private String userName;

    public HTTPUserController(UserService userService, TimelineService timelineService, int serverPort) {
        this.userService = userService;
        this.timelineService = timelineService;
        this.serverPort = serverPort;
    }

    @Override
    public void process(Clock clock) throws IOException {
        this.clock = clock;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        createContextForAddingAndReadingPosts(server);
        createContextForAddingFollowedUsers(server);
        server.start();
    }

    private void createContextForAddingAndReadingPosts(HttpServer server) {
        server.createContext(POSTS_PATH, exchange -> {
                    setResponseHeader(exchange);
                    this.userName = getUserNameFromURL(POSTS_PATH, exchange);
                    switch (exchange.getRequestMethod()) {
                        case GET_REQUEST:
                            handleReadTimelineGetRequest(exchange);
                            break;
                        case POST_REQUEST:
                            handlePostingPostRequest(exchange);
                            break;
                    }
                    exchange.close();
                }
        );
    }

    private void createContextForAddingFollowedUsers(HttpServer server) {
        server.createContext(FOLLOW_PATH, exchange -> {
                    this.userName = getUserNameFromURL(FOLLOW_PATH, exchange);
                    setResponseHeader(exchange);
                    if (POST_REQUEST.equals(exchange.getRequestMethod())) {
                        handleAddFollowedUserPostRequest(exchange);
                    }
                    exchange.close();
                }
        );
    }

    private String getUserNameFromURL(String path, HttpExchange exchange) {
        return exchange.getRequestURI().toString().substring(path.length());
    }

    private void setResponseHeader(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
    }

    private void handleReadTimelineGetRequest(HttpExchange exchange) throws IOException {
        ArrayList<Post> posts = userService.getPosts(userName);
        String formattedPosts = timelineService.getTimeLine(posts, LocalDateTime.now(clock));
        writeResponseBody(exchange, formattedPosts);
    }

    private void handlePostingPostRequest(HttpExchange exchange) throws IOException {
        String post = getRequestBody(exchange);
        userService.addPost(userName + POST_COMMAND + post, LocalDateTime.now(clock));
        writeResponseBody(exchange, "Added post: \"" + post + "\" to user: " + userName);
    }

    private void handleAddFollowedUserPostRequest(HttpExchange exchange) throws IOException {
        String followedUser = getRequestBody(exchange);
        userService.addFollowee(userName + FOLLOW_COMMAND + followedUser);
        writeResponseBody(exchange, "Added user: \"" + followedUser + "\" to list of followed users for user: " + userName);
    }

    private void writeResponseBody(HttpExchange exchange, String responseBody) throws IOException {
        final byte[] rawResponseBody = responseBody.getBytes();
        exchange.sendResponseHeaders(OK_STATUS_CODE, rawResponseBody.length);
        exchange.getResponseBody().write(rawResponseBody);
    }

    private String getRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        Scanner scanner = new Scanner(inputStream);
        return scanner.nextLine();
    }


}
