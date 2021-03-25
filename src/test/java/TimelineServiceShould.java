import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TimelineServiceShould {

    TimelineRepository mockTimelineRepository;
    TimelineService timelineService;

    @BeforeEach
    void setUp() {
        mockTimelineRepository = mock(TimelineRepository.class);
        timelineService = new TimelineService(mockTimelineRepository);
    }

    @Test
    void saveMessagesToTimelineRepository() {

        LocalDateTime now = LocalDateTime.now();

        timelineService.post(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockTimelineRepository).addPost(TestCommands.ALICE_EXAMPLE_POST, now);
    }
}
