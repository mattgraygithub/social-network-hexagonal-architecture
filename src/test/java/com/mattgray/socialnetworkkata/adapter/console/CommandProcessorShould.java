package com.mattgray.socialnetworkkata.adapter.console;

import com.mattgray.socialnetworkkata.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommandProcessorShould {

    private static LocalDateTime now;
    private static CommandProcessor commandProcessor;
    private static UserService mockUserService;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        mockUserService = mock(UserService.class);
        commandProcessor = new CommandProcessor(mockUserService);
    }

    @Test
    void delegatePostCommandsToUserService() {
        runCommand(ALICE_EXAMPLE_POST_COMMAND);

        verify(mockUserService).addPost(ALICE_EXAMPLE_POST_COMMAND, now);
    }

    @Test
    void delegateReadTimelineCommandsToUserService() {
        runCommand(READ_ALICE_TIMELINE);

        verify(mockUserService).getTimeLine(ALICE_USER_NAME, now);
    }

    @Test
    void delegateFollowCommandsToUserService() {
        runCommand(CHARLIE_FOLLOWS_ALICE);

        verify(mockUserService).addFollowee(CHARLIE_FOLLOWS_ALICE);
    }

    @Test
    void delegateReadWallCommandsToUserService() {
        runCommand(READ_CHARLIE_WALL);

        verify(mockUserService).getWall(READ_CHARLIE_WALL, now);
    }

    private void runCommand(String command) {
        System.setIn(new ByteArrayInputStream(command.getBytes()));
        commandProcessor.process(now);
    }
}
