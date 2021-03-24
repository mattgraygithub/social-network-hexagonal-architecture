public class CommandProcessor {

    private TimelineService timelineService;
    private FollowerService followerService;

    public CommandProcessor(TimelineService timelineService, FollowerService followerService) {
        this.timelineService = timelineService;
        this.followerService = followerService;
    }

    public void process(String command, String time) {

        throw new UnsupportedOperationException();
    }
}
