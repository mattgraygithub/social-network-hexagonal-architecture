package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.TestCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceShould {

    UserRepository mockUserRepository;
    UserService userService;

    @BeforeEach
    void setUp() {
        mockUserRepository = mock(UserRepository.class);
        userService = new UserService(mockUserRepository);
    }

    @Test
    void callsUserRepositoryToAddPost() {

        LocalDateTime now = LocalDateTime.now();

        userService.addPost(TestCommands.ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockUserRepository).addPost(TestCommands.ALICE_USER_NAME, TestCommands.ALICE_EXAMPLE_POST, now);
    }
}
