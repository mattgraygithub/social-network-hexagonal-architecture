package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.util.ArrayList;

public class HttpUserController implements UserController {

    private final UserService userService;

    public HttpUserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void process(Clock clock) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        String path = "/getPosts/";
        server.createContext(path, (exchange -> {
            String name = exchange.getRequestURI().toString().substring(10);
            ArrayList<Post> posts = userService.getPosts(name);
            String respText = posts.toString();
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        }));
        server.setExecutor(null);
        server.start();
    }
}
