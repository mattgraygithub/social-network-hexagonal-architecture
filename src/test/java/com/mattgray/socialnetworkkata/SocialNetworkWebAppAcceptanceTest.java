package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.console.TimelineConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.console.WallConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.web.HttpUserController;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserRepository;
import com.mattgray.socialnetworkkata.port.WallService;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.mattgray.socialnetworkkata.service.clock.ClockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
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

    private static final String POST_REQUEST = "POST";
    private static final String GET_REQUEST = "GET";

    private static Clock clockStub;
    private static SocialNetwork webApp;

    @BeforeEach
    void setUp() {
        clockStub = mock(Clock.class);
        ArrayList<User> users = new ArrayList<>();
        UserRepository userRepository = new InMemoryUserRepository(users);
        ClockService clockService = new ClockServiceImpl();
        TimelineService timelineService = new TimelineConsoleAdapter(clockService);
        WallService wallService = new WallConsoleAdapter(clockService);
        UserService userService = new UserService(userRepository, timelineService, wallService);
        webApp = new SocialNetwork(new HttpUserController(userService, clockService), clockStub);
    }

    @Test
    void usersCanPostMessagesToTheirTimeLinesAndAUsersTimelineCanBeRead() throws IOException {
        runApplication();
        makeAliceAndBobPostRequests();
        assertThat(makeGetRequestFor(ALICE_USER_NAME, AT_12PM)).isEqualTo("[{\"timeAgo\":\" (5 minutes ago)\",\"post\":\"I love the weather today\"}]");
    }

    private void runApplication() throws IOException {
        webApp.run();
    }

    private void makeAliceAndBobPostRequests() throws IOException {
        makePostRequestFor(ALICE_USER_NAME, ALICE_EXAMPLE_POST, AT_5_MINUTES_BEFORE_12PM);
        makePostRequestFor(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_ONE, AT_2_MINUTES_BEFORE_12PM);
        makePostRequestFor(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_TWO, AT_1_MINUTE_BEFORE_12PM);
    }

    private void makePostRequestFor(String userName, String commandString, LocalDateTime timeOfConnection) throws IOException {
        HttpURLConnection connection = setUpHttpURLConnectionFor(userName, POST_REQUEST, timeOfConnection);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] command = commandString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(command, 0, command.length);
        }
        getResponse(connection);
    }

    private String makeGetRequestFor(String userName, LocalDateTime timeOfConnection) throws IOException {
        HttpURLConnection connection = setUpHttpURLConnectionFor(userName, GET_REQUEST, timeOfConnection);

        try (InputStream inputStream = connection.getInputStream()) {
            Scanner scanner = new Scanner(inputStream);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNext()) {
                content.append(scanner.nextLine());
            }
            return content.toString();
        }
    }

    private HttpURLConnection setUpHttpURLConnectionFor(String userName, String requestMethod, LocalDateTime timeOfConnection) throws IOException {
        setUpClockStubWith(timeOfConnection);
        URL url = new URL("http://localhost:8000/" + userName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
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
            return response.toString();
        }
    }
}
