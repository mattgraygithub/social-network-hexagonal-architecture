package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.console.ConsoleUserController;
import com.mattgray.socialnetworkkata.adapter.console.TimelineServiceConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.console.WallConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.web.HTTPUserController;
import com.mattgray.socialnetworkkata.adapter.web.TimelineServiceHTTPAdapter;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserController;
import com.mattgray.socialnetworkkata.port.UserRepository;
import com.mattgray.socialnetworkkata.port.WallService;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.mattgray.socialnetworkkata.service.clock.ClockServiceImpl;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;

public class SocialNetwork {

    private static final ArrayList<User> USERS = new ArrayList<>();
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository(USERS);
    private static final ClockService CLOCK_SERVICE = new ClockServiceImpl();
    private static final TimelineService CONSOLE_TIMELINE_SERVICE = new TimelineServiceConsoleAdapter(CLOCK_SERVICE);
    private static final TimelineService HTTP_TIMELINE_SERVICE = new TimelineServiceHTTPAdapter(CLOCK_SERVICE);
    private static final WallService WALL_SERVICE = new WallConsoleAdapter(CLOCK_SERVICE);
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, CONSOLE_TIMELINE_SERVICE, WALL_SERVICE);
    private static final int SERVER_PORT = 8000;
    private final UserController userController;
    private final Clock clock;

    public SocialNetwork(UserController userController, Clock clock) {
        this.userController = userController;
        this.clock = clock;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the Social Network. Please enter a command");

        SocialNetwork webApp = new SocialNetwork(new HTTPUserController(USER_SERVICE, HTTP_TIMELINE_SERVICE, SERVER_PORT), Clock.systemDefaultZone());
        webApp.run();

        SocialNetwork consoleApp = new SocialNetwork(new ConsoleUserController(USER_SERVICE, CONSOLE_TIMELINE_SERVICE), Clock.systemDefaultZone());
        consoleApp.run();
    }

    public void run() throws IOException {
        userController.process(clock);
    }
}
