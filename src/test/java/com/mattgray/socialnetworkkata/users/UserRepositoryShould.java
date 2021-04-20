package com.mattgray.socialnetworkkata.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mattgray.socialnetworkkata.common.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryShould {

    private List<User> usersMock;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        usersMock = new ArrayList<>();
        userRepository = new InMemoryUserRepository(usersMock);
    }

    @Test
    void addUserIfAPostCommandIsReceivedAndTheUserDoesNotAlreadyExist() {
        userRepository.addPost(ALICE_USER_NAME, ALICE_EXAMPLE_POST, LocalDateTime.now());

        assertThat(usersMock.get(0).getUserName()).isEqualTo(ALICE_USER_NAME);
    }

    @Test
    void notAddANewUserIfAPostCommandIsReceivedAndTheUserAlreadyExists() {
        userRepository.addPost(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_ONE, LocalDateTime.now());
        userRepository.addPost(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_TWO, LocalDateTime.now());

        assertThat(usersMock.size()).isEqualTo(1);
    }

    @Test
    void addPostToNewUsersTimelineIfAPostCommandIsReceivedAndTheUserDidNotAlreadyExist() {
        userRepository.addPost(ALICE_USER_NAME, ALICE_EXAMPLE_POST, LocalDateTime.now());

        assertThat(userRepository.getPostsFor(ALICE_USER_NAME).getPosts().size()).isEqualTo(1);
    }

    @Test
    void addPostToExistingUsersTimelineIfAPostCommandIsReceivedAndTheUserDoesAlreadyExist() {
        userRepository.addPost(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_ONE, LocalDateTime.now());
        userRepository.addPost(BOB_USER_NAME, BOB_EXAMPLE_POST_COMMAND_TWO, LocalDateTime.now());

        assertThat(userRepository.getPostsFor(BOB_USER_NAME).getPosts().size()).isEqualTo(2);
    }

    @Test
    void addUserIfAFollowCommandIsReceivedAndTheUserDoesNotAlreadyExist() {
        userRepository.addFollowee(ALICE_USER_NAME, BOB_USER_NAME);

        assertThat(usersMock.get(0).getUserName()).isEqualTo(ALICE_USER_NAME);
    }

    @Test
    void notAddANewUserIfAFollowCommandIsReceivedAndTheUserAlreadyExists() {
        userRepository.addFollowee(ALICE_USER_NAME, BOB_USER_NAME);
        userRepository.addFollowee(ALICE_USER_NAME, CHARLIE_USER_NAME);

        assertThat(usersMock.size()).isEqualTo(1);
    }

    @Test
    void addFolloweeToNewUsersFolloweeRepositoryIfAFollowCommandIsReceivedAndTheUserDidNotAlreadyExist() {
        userRepository.addFollowee(ALICE_USER_NAME, BOB_USER_NAME);

        assertThat(usersMock.get(0).getFollowedUsers().getFollowedUsers().size()).isEqualTo(1);
    }
}
