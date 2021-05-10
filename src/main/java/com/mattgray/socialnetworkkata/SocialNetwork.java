package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.console.TimelineConsoleAdapter;
import com.mattgray.socialnetworkkata.adapter.console.WallConsoleAdapter;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserRepository;
import com.mattgray.socialnetworkkata.port.WallService;
import com.mattgray.socialnetworkkata.service.UserService;
import com.mattgray.socialnetworkkata.service.clock.ClockService;
import com.mattgray.socialnetworkkata.service.clock.ClockServiceImpl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class SocialNetwork {

    private static final ArrayList<User> USERS = new ArrayList<>();
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository(USERS);
    private static final ClockService CLOCK_SERVICE = new ClockServiceImpl();
    private static final TimelineService TIMELINE_SERVICE = new TimelineConsoleAdapter(CLOCK_SERVICE);
    private static final WallService WALL_SERVICE = new WallConsoleAdapter(CLOCK_SERVICE);
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, TIMELINE_SERVICE, WALL_SERVICE);
    private final CommandProcessor commandProcessor;
    private final Clock clock;

    public SocialNetwork(CommandProcessor commandProcessor, Clock clock) {
        this.commandProcessor = commandProcessor;
        this.clock = clock;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Social Network. Please enter a command");

        SocialNetwork socialNetwork = new SocialNetwork(new CommandProcessor(USER_SERVICE), Clock.systemDefaultZone());

        socialNetwork.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            commandProcessor.process(scanner.nextLine(), LocalDateTime.now(clock));
        }
    }
}
