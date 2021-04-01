package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.TestCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryShould {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    void addUserAndPostIfUserDoesNotAlreadyExist() {

        userRepository.addPost(TestCommands.ALICE_USER_NAME, TestCommands.ALICE_EXAMPLE_POST, LocalDateTime.now());

        assertThat(userRepository.getTimelineFor(TestCommands.ALICE_USER_NAME).getPosts().size()).isEqualTo(1);
    }

    @Test
    void addPostToTimeLineIfUserAlreadyExists() {

        userRepository.addPost(TestCommands.BOB_USER_NAME, TestCommands.BOB_EXAMPLE_POST_COMMAND_ONE, LocalDateTime.now());

        assertThat(userRepository.getTimelineFor(TestCommands.BOB_USER_NAME).getPosts().size()).isEqualTo(1);

        userRepository.addPost(TestCommands.BOB_USER_NAME, TestCommands.BOB_EXAMPLE_POST_COMMAND_TWO, LocalDateTime.now());

        assertThat(userRepository.getTimelineFor(TestCommands.BOB_USER_NAME).getPosts().size()).isEqualTo(2);
    }
}
