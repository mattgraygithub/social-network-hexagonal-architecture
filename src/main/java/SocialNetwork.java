import java.io.InputStream;
import java.util.Scanner;

public class SocialNetwork {

    private CommandProcessor commandProcessor;
    private Clock clock;

    public SocialNetwork(CommandProcessor commandProcessor, Clock clock) {

        this.commandProcessor = commandProcessor;
        this.clock = clock;
    }

    public void run() {

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            commandProcessor.process(scanner.nextLine(), clock.now());
        }
    }

    public static void main(String[] args) {

        SocialNetwork socialNetwork = new SocialNetwork(new CommandProcessor(new TimelineService(), new FollowerService()), new Clock());

        socialNetwork.run();
    }
}
