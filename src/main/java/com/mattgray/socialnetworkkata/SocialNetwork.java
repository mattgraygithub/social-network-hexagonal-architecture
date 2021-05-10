package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.clock.ClockService;
import com.mattgray.socialnetworkkata.clock.ClockServiceImpl;
import com.mattgray.socialnetworkkata.following.WallService;
import com.mattgray.socialnetworkkata.following.WallServiceImpl;
import com.mattgray.socialnetworkkata.posting.TimelineService;
import com.mattgray.socialnetworkkata.posting.TimelineServiceImpl;
import com.mattgray.socialnetworkkata.users.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.users.User;
import com.mattgray.socialnetworkkata.users.UserRepository;
import com.mattgray.socialnetworkkata.users.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class SocialNetwork {

    private static final ArrayList<User> USERS = new ArrayList<>();
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository(USERS);
    private static final ClockService CLOCK_SERVICE = new ClockServiceImpl();
    private static final TimelineService TIMELINE_SERVICE = new TimelineServiceImpl(CLOCK_SERVICE);
    private static final WallService WALL_SERVICE = new WallServiceImpl(CLOCK_SERVICE);
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
