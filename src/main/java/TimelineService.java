import java.time.LocalDateTime;
import java.util.Arrays;

public class TimelineService {
    private TimelineRepository timelineRepository;

    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }

    public void post(String command, LocalDateTime time) {
        timelineRepository.addPost(extractPostFrom(command), time);
    }

    private String extractPostFrom(String command) {
        String[] commandList = command.split(" ");
        String[] postList = Arrays.copyOfRange(commandList, 2, commandList.length);
        return String.join(" ", postList);
    }
}
