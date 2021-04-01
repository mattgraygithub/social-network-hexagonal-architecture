package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommandProcessorShould {

    CommandProcessor commandProcessor;
    UserService mockUserService;

    @BeforeEach
    void setUp() {
        mockUserService = mock(UserService.class);
        commandProcessor = new CommandProcessor(mockUserService);
    }

    @Test
    void delegatePostCommandsToTimelineService() {

        LocalDateTime now = LocalDateTime.now();

        commandProcessor.process(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockUserService).addPost(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);
    }
}
