package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.users.InMemoryUserRepository;
import com.mattgray.socialnetworkkata.users.TimelineService;
import com.mattgray.socialnetworkkata.users.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SocialNetwork {

    private final CommandProcessor commandProcessor;
    private final Clock clock;

    public SocialNetwork(CommandProcessor commandProcessor, Clock clock) {
        this.commandProcessor = commandProcessor;
        this.clock = clock;
    }

    public static void main(String[] args) {

        SocialNetwork socialNetwork = new SocialNetwork(new CommandProcessor(new UserService(new InMemoryUserRepository(), new TimelineService())), Clock.systemDefaultZone());

        socialNetwork.run();
    }

    public void run() {

        System.out.println("Welcome to the social network. Please enter a command");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            commandProcessor.process(scanner.nextLine(), LocalDateTime.now(clock));
        }
    }
}
