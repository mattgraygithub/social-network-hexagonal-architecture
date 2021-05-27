package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.console.WallConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.web.HTTPUserController;
import com.mattgray.socialnetworkkata.adapter.web.TimelineServiceHTTPAdapter;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserRepository;
import com.mattgray.socialnetworkkata.port.WallService;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.mattgray.socialnetworkkata.service.clock.ClockServiceImpl;
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
import java.util.ArrayList;
import java.util.Scanner;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocialNetworkWebAppAcceptanceTest {

    private static final String TIME_PROPERTY_NAME = "timeAgo\":\"";
    private static final String POST_PROPERTY_NAME = "\",\"post\":\"";
    private static final String JSON_ENTRY_DIVIDER = "\"},{\"";
    private static final int PORT_8001 = 8001;
    private static final int PORT_8002 = 8002;
    private static final int PORT_8003 = 8003;

    private static Clock clockStub;
    private static UserService userService;
    private static TimelineService timelineService;

    @BeforeEach
    void setUp() {
        clockStub = mock(Clock.class);
        ClockService clockService = new ClockServiceImpl();
        ArrayList<User> users = new ArrayList<>();
        UserRepository userRepository = new InMemoryUserRepository(users);
        timelineService = new TimelineServiceHTTPAdapter(clockService);
        WallService wallService = new WallConsoleAdapter(clockService);
        userService = new UserService(userRepository, timelineService, wallService);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndAUsersTimelineCanBeRead() throws IOException {
        SocialNetwork socialNetwork = new SocialNetwork(new HTTPUserController(userService, timelineService, PORT_8001), clockStub);
        socialNetwork.run();
        makeAliceAndBobPostRequests(PORT_8001);
        assertThat(makeGetRequestFor(ALICE_USER_NAME, PORT_8001, POSTS_PATH, AT_12PM)).isEqualTo(TIME_PROPERTY_NAME + FIVE_MINUTES_AGO + POST_PROPERTY_NAME + ALICE_EXAMPLE_POST);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndADifferentUsersTimelineCanBeRead() throws IOException {
        SocialNetwork socialNetwork = new SocialNetwork(new HTTPUserController(userService, timelineService, PORT_8002), clockStub);
        socialNetwork.run();
        makeAliceAndBobPostRequests(PORT_8002);
        assertThat(makeGetRequestFor(BOB_USER_NAME, PORT_8002, POSTS_PATH, AT_12PM)).
                isEqualTo(TIME_PROPERTY_NAME + ONE_MINUTE_AGO + POST_PROPERTY_NAME + BOB_EXAMPLE_POST_TWO + JSON_ENTRY_DIVIDER +
                        TIME_PROPERTY_NAME + TWO_MINUTES_AGO + POST_PROPERTY_NAME + BOB_EXAMPLE_POST_ONE);
    }

    @Test
    void usersCanFollowOtherUsersAndViewAnAggregatedListOfTheirsAndTheirFollowedUsersPostsOnTheirWall() throws IOException {
        SocialNetwork socialNetwork = new SocialNetwork(new HTTPUserController(userService, timelineService, PORT_8003), clockStub);
        socialNetwork.run();
        makeAliceAndBobPostRequests(PORT_8003);
        makePostRequestFor(CHARLIE_USER_NAME, ALICE_USER_NAME, PORT_8003, FOLLOW_PATH, AT_12PM);
        makePostRequestFor(CHARLIE_USER_NAME, BOB_USER_NAME, PORT_8003, FOLLOW_PATH, AT_12PM);
        assertThat(makeGetRequestFor(CHARLIE_USER_NAME, PORT_8003, WALL_PATH, AT_12PM)).isEqualTo(CHARLIE_WALL_EXPECTED_JSON_RESPONSE);
    }

    private void makeAliceAndBobPostRequests(int port) throws IOException {
        makePostRequestFor(ALICE_USER_NAME, ALICE_EXAMPLE_POST, port, POSTS_PATH, AT_5_MINUTES_BEFORE_12PM);
        makePostRequestFor(BOB_USER_NAME, BOB_EXAMPLE_POST_ONE, port, POSTS_PATH, AT_2_MINUTES_BEFORE_12PM);
        makePostRequestFor(BOB_USER_NAME, BOB_EXAMPLE_POST_TWO, port, POSTS_PATH, AT_1_MINUTE_BEFORE_12PM);
    }

    private void makePostRequestFor(String userName, String commandString, int port, String path, LocalDateTime timeOfConnection) throws IOException {
        HttpURLConnection connection = setUpHttpURLConnectionFor(userName, POST_REQUEST, port, path, timeOfConnection);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] command = commandString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(command);
        }
        getResponse(connection);
    }

    private String makeGetRequestFor(String userName, int port, String path, LocalDateTime timeOfConnection) throws IOException {
        HttpURLConnection connection = setUpHttpURLConnectionFor(userName, GET_REQUEST, port, path, timeOfConnection);
        String response = getResponse(connection);
        String trimmedResponse = response.substring(3, response.length() - 3);
        return trimmedResponse;
    }

    private HttpURLConnection setUpHttpURLConnectionFor(String userName, String requestMethod, int port, String path, LocalDateTime timeOfConnection) throws IOException {
        setUpClockStubWith(timeOfConnection);
        URL url = new URL(HTTP_LOCALHOST + port + path + userName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        return connection;
    }

    private void setUpClockStubWith(LocalDateTime timeOfCommand) {
        Clock fixedClock = Clock.fixed(timeOfCommand.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        when(clockStub.instant()).thenReturn(fixedClock.instant());
        when(clockStub.getZone()).thenReturn(fixedClock.getZone());
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
