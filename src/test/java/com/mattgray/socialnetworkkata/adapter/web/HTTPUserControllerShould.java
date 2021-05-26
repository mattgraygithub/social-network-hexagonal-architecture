package com.mattgray.socialnetworkkata.adapter.web;

import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.service.UserService;
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

public class HTTPUserControllerShould {

    private static final Clock FIXED_CLOCK_AT_12PM = Clock.fixed(AT_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    private static final String LOCALHOST = "http://localhost:";
    private static final String POSTS_PATH = "/posts/";
    private static final String POST_REQUEST = "POST";
    private static final String GET_REQUEST = "GET";
    private static final int PORT_8004 = 8004;
    private static final int PORT_8005 = 8005;
    private static final int PORT_8006 = 8006;
    private static final int PORT_8007 = 8007;
    private static final String ALICE_EXPECTED_JSON_RESPONSE = "[ { \"timeAgo\": \" (5 minutes ago)\", \"post\": \" " + ALICE_EXAMPLE_POST + "\" }]";
    private static Clock clockStub;
    private static UserService mockUserService;
    private static TimelineService mockHTTPTimelineService;

    @BeforeEach
    void setUp() {
        clockStub = mock(Clock.class);
        mockUserService = mock(UserService.class);
        mockHTTPTimelineService = mock(TimelineServiceHTTPAdapter.class);
    }

    @Test
    void callUserServiceToAddPostWhenAPostRequestIsReceivedAtThePostsEndpoint() throws IOException {
        initialiseUserControllerOn(PORT_8004);
        setUpClockStubWith(AT_12PM);
        makePostRequest(LOCALHOST + PORT_8004 + POSTS_PATH + ALICE_USER_NAME, ALICE_EXAMPLE_POST);
        verify(mockUserService).addPost(ALICE_USER_NAME + POST_COMMAND + ALICE_EXAMPLE_POST, LocalDateTime.now(FIXED_CLOCK_AT_12PM));
    }

    @Test
    void giveResponseToPostRequestNotifyingUsThatThePostHasBeenAddedToTheCorrectUser() throws IOException {
        initialiseUserControllerOn(PORT_8005);
        setUpClockStubWith(AT_12PM);
        assertThat(makePostRequest(LOCALHOST + PORT_8005 + POSTS_PATH + ALICE_USER_NAME, ALICE_EXAMPLE_POST)).isEqualTo("Added post: \"" + ALICE_EXAMPLE_POST + "\" to user: " + ALICE_USER_NAME);
    }

    @Test
    void callUserServiceToGetPostWhenAGetRequestIsReceivedAtThePostsEndpoint() throws IOException {
        when(mockUserService.getPosts(ALICE_USER_NAME)).thenReturn(ALICE_EXAMPLE_POST_LIST);
        setUpClockStubWith(AT_12PM);
        when(mockHTTPTimelineService.getTimeLine(ALICE_EXAMPLE_POST_LIST, LocalDateTime.now(clockStub))).thenReturn(
                ALICE_EXPECTED_JSON_RESPONSE
        );

        initialiseUserControllerOn(PORT_8006);
        makeGetRequest(LOCALHOST + PORT_8006 + POSTS_PATH + ALICE_USER_NAME);

        verify(mockUserService).getPosts(ALICE_USER_NAME);
    }

    @Test
    void returnPostsAsJSONForAGivenUserWhenAGetRequestIsReceivedAtThePostsEndpoint() throws IOException {
        when(mockUserService.getPosts(ALICE_USER_NAME)).thenReturn(ALICE_EXAMPLE_POST_LIST);
        setUpClockStubWith(AT_12PM);
        when(mockHTTPTimelineService.getTimeLine(ALICE_EXAMPLE_POST_LIST, LocalDateTime.now(clockStub))).thenReturn(
                ALICE_EXPECTED_JSON_RESPONSE
        );
        initialiseUserControllerOn(PORT_8007);

        assertThat(makeGetRequest(LOCALHOST + PORT_8007 + POSTS_PATH + ALICE_USER_NAME)).isEqualTo(ALICE_EXPECTED_JSON_RESPONSE);
    }

    private void initialiseUserControllerOn(int port) throws IOException {
        UserController userController = new HTTPUserController(mockUserService, mockHTTPTimelineService, port);
        userController.process(clockStub);
    }

    private void setUpClockStubWith(LocalDateTime timeOfRequest) {
        Clock fixedClock = Clock.fixed(timeOfRequest.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        when(clockStub.instant()).thenReturn(fixedClock.instant());
        when(clockStub.getZone()).thenReturn(fixedClock.getZone());
    }

    private String makePostRequest(String url, String requestBody) throws IOException {
        HttpURLConnection connection = getHttpURLConnectionFor(url, POST_REQUEST);
        setRequestBodyWith(requestBody, connection);
        return getResponse(connection);
    }

    private String makeGetRequest(String url) throws IOException {
        HttpURLConnection connection = getHttpURLConnectionFor(url, GET_REQUEST);
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