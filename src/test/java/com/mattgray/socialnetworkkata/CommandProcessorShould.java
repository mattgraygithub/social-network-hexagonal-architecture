package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

        commandProcessor.process(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockUserService).addPost(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);
    }

    @Test
    void delegateReadTimelineCommandsToUserService() {

        commandProcessor.process(TestCommands.READ_ALICE_TIMELINE, now);

        verify(mockUserService).getTimeLine(TestCommands.ALICE_USER_NAME, now);
    }
}
