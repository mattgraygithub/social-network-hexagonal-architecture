package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.port.UserController;

import java.time.LocalDateTime;

public class HttpUserController implements UserController {

    @Override
    public void process(LocalDateTime time) {
//        int serverPort = 8000;
//        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
//        String path = "/api";
//        server.createContext(path, (exchange -> {
//            String name = exchange.getRequestURI().toString().substring(5);
//            String respText = "Hello " + name + "!";
//            exchange.sendResponseHeaders(200, respText.getBytes().length);
//            OutputStream output = exchange.getResponseBody();
//            output.write(respText.getBytes());
//            output.flush();
//            exchange.close();
//        }));
//        server.setExecutor(null); // creates a default executor
//        server.start();
    }
}
