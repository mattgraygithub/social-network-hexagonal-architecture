package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.clock.ClockServiceImpl;
import com.mattgray.socialnetworkkata.posts.TimelineServiceImpl;
import com.mattgray.socialnetworkkata.posts.WallServiceImpl;
import com.mattgray.socialnetworkkata.users.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.users.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class SocialNetwork {

    private final CommandProcessor commandProcessor;
    private final Clock clock;

    public SocialNetwork(CommandProcessor commandProcessor, Clock clock) {
        this.commandProcessor = commandProcessor;
        this.clock = clock;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Social Network. Please enter a command");

        SocialNetwork socialNetwork = new SocialNetwork(new CommandProcessor(new UserService(new InMemoryUserRepository(new ArrayList<>()), new TimelineServiceImpl(new ClockServiceImpl()), new WallServiceImpl(new ClockServiceImpl()))), Clock.systemDefaultZone());

        socialNetwork.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            commandProcessor.process(scanner.nextLine(), LocalDateTime.now(clock));
        }
    }
}
