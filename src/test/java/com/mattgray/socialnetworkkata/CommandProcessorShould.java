package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommandProcessorShould {

    LocalDateTime now;
    CommandProcessor commandProcessor;
    UserService mockUserService;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        mockUserService = mock(UserService.class);
        commandProcessor = new CommandProcessor(mockUserService);
    }

    @Test
    void delegatePostCommandsToUserService() {
        commandProcessor.process(ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockUserService).addPost(ALICE_EXAMPLE_POST_COMMAND, now);
    }

    @Test
    void delegateReadTimelineCommandsToUserService() {
        commandProcessor.process(READ_ALICE_TIMELINE, now);

        verify(mockUserService).getTimeLine(ALICE_USER_NAME, now);
    }

    @Test
    void delegateFollowCommandsToUserService() {
        commandProcessor.process(CHARLIE_FOLLOWS_ALICE, now);

        verify(mockUserService).addFollowee(CHARLIE_FOLLOWS_ALICE);
    }

    @Test
    void delegateReadWallCommandsToUserService() {
        commandProcessor.process(READ_CHARLIE_WALL, now);

        verify(mockUserService).getWall(READ_CHARLIE_WALL, now);
    }
}
