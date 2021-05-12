package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.console.CommandProcessor;
import com.mattgray.socialnetworkkata.adapter.console.TimelineConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.console.WallConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.web.HttpUserController;
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
    private static final TimelineService TIMELINE_SERVICE = new TimelineConsoleAdapter(CLOCK_SERVICE);
    private static final WallService WALL_SERVICE = new WallConsoleAdapter(CLOCK_SERVICE);
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, TIMELINE_SERVICE, WALL_SERVICE);
    private final UserController userController;
    private final Clock clock;

    public SocialNetwork(UserController userController, Clock clock) {
        this.userController = userController;
        this.clock = clock;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the Social Network. Please enter a command");

        SocialNetwork webApp = new SocialNetwork(new HttpUserController(USER_SERVICE, CLOCK_SERVICE), Clock.systemDefaultZone());
        webApp.run();

        SocialNetwork consoleApp = new SocialNetwork(new CommandProcessor(USER_SERVICE, TIMELINE_SERVICE), Clock.systemDefaultZone());
        consoleApp.run();
    }

    public void run() throws IOException {
        userController.process(clock);
    }
}
