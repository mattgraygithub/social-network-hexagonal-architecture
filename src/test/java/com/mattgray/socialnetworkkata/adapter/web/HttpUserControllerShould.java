package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Scanner;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpUserControllerShould {

    private static final Clock FIXED_CLOCK_AT_12PM = Clock.fixed(AT_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    private static final String LOCALHOST = "http://localhost:";
    private static final String POSTS_PATH = "/posts/";
    private static final String POST_REQUEST = "POST";
    private static final int PORT_8001 = 8001;
    private static final int PORT_8002 = 8002;
    private static Clock clockStub;
    private static UserService mockUserService;
    private static ClockService clockServiceStub;

    @BeforeEach
    void setUp() {
        clockStub = mock(Clock.class);
        mockUserService = mock(UserService.class);
        clockServiceStub = mock(ClockService.class);
    }

    @Test
    void callUserServiceToAddPostWhenAPostRequestIsReceivedAtThePostsEndpoint() throws IOException {
        initialiseUserControllerOn(PORT_8001);
        setUpClockStub();
        makeHTTPRequest(LOCALHOST + PORT_8001 + POSTS_PATH + ALICE_USER_NAME, POST_REQUEST, ALICE_EXAMPLE_POST);
        verify(mockUserService).addPost(ALICE_USER_NAME + POST_COMMAND + ALICE_EXAMPLE_POST, LocalDateTime.now(FIXED_CLOCK_AT_12PM));
    }


    @Test
    void getResponseFromPostRequestNotifyingUsThatThePostHasBeenAddedToTheCorrectUser() throws IOException {
        initialiseUserControllerOn(PORT_8002);
        setUpClockStub();
        assertThat(makeHTTPRequest(LOCALHOST + PORT_8002 + POSTS_PATH + ALICE_USER_NAME, POST_REQUEST, ALICE_EXAMPLE_POST)).isEqualTo("Added post: \"" + ALICE_EXAMPLE_POST + "\" to user: " + ALICE_USER_NAME);
    }

    private void initialiseUserControllerOn(int port) throws IOException {
        UserController userController = new HttpUserController(mockUserService, clockServiceStub, port);
        userController.process(clockStub);
    }

    private void setUpClockStub() {
        when(clockStub.instant()).thenReturn(FIXED_CLOCK_AT_12PM.instant());
        when(clockStub.getZone()).thenReturn(FIXED_CLOCK_AT_12PM.getZone());
    }

    private String makeHTTPRequest(String url, String requestMethod, String requestBody) throws IOException {
        HttpURLConnection connection = getHttpURLConnectionFor(url, requestMethod);
        setRequestBodyWith(requestBody, connection);
        return getResponse(connection);
    }

    private HttpURLConnection getHttpURLConnectionFor(String endpointURL, String requestMethod) throws IOException {
        URL url = new URL(endpointURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        return connection;
    }

    private void setRequestBodyWith(String postText, HttpURLConnection connection) throws IOException {
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] command = postText.getBytes(StandardCharsets.UTF_8);
            outputStream.write(command);
        }
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream()) {
            Scanner scanner = new Scanner(inputStream);
            StringBuilder response = new StringBuilder();
            while ((scanner.hasNext())) {
                response.append(scanner.nextLine());
            }
            inputStream.close();
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}