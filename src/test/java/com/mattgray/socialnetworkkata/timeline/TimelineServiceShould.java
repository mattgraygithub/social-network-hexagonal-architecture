package com.mattgray.socialnetworkkata.timeline;

import com.mattgray.socialnetworkkata.TestCommands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TimelineServiceShould {

    TimelineRepositoryImpl mockTimelineRepository;
    TimelineService timelineService;

    @BeforeEach
    void setUp() {
        mockTimelineRepository = mock(TimelineRepositoryImpl.class);
        timelineService = new TimelineService(mockTimelineRepository);
    }

    @Test
    void saveMessagesToTimelineRepository() {

        LocalDateTime now = LocalDateTime.now();

        timelineService.post(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockTimelineRepository).add(TestCommands.ALICE_EXAMPLE_POST, now);
    }
}
