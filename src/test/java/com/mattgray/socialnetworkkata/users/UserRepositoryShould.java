package com.mattgray.socialnetworkkata.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.mattgray.socialnetworkkata.TestCommands.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryShould {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    void addUserAndPostIfUserDoesNotAlreadyExist() {

        userRepository.addPost(ALICE_USER_NAME, ALICE_EXAMPLE_POST, LocalDateTime.now());

        assertThat(userRepository.getTimelineFor(ALICE_USER_NAME).getPosts().size()).isEqualTo(1);
    }

    @Test
    void addPostToTimeLineIfUserAlreadyExists() {

        userRepository.addPost(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_ONE, LocalDateTime.now());

        assertThat(userRepository.getTimelineFor(BOB_USER_NAME).getPosts().size()).isEqualTo(1);

        userRepository.addPost(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_TWO, LocalDateTime.now());

        assertThat(userRepository.getTimelineFor(BOB_USER_NAME).getPosts().size()).isEqualTo(2);
    }
}
