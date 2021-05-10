package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.adapter.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.adapter.TimelineServiceImpl;
import com.mattgray.socialnetworkkata.adapter.WallServiceImpl;
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

    private static final ArrayList<User> users = new ArrayList<>();
    private static final UserRepository userRepository = new InMemoryUserRepository(users);
    private static final ClockService clockService = new ClockServiceImpl();
    private static final TimelineService timelineService = new TimelineServiceImpl(clockService);
    private static final WallService wallService = new WallServiceImpl(clockService);
    private static final UserService userService = new UserService(userRepository, timelineService, wallService);
    private final CommandProcessor commandProcessor;
    private final Clock clock;

    public SocialNetwork(CommandProcessor commandProcessor, Clock clock) {
        this.commandProcessor = commandProcessor;
        this.clock = clock;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Social Network. Please enter a command");

        SocialNetwork socialNetwork = new SocialNetwork(new CommandProcessor(userService), Clock.systemDefaultZone());

        socialNetwork.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            commandProcessor.process(scanner.nextLine(), LocalDateTime.now(clock));
        }
    }
}
