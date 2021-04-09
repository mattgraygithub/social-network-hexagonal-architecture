package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.followees.FolloweeRepository;
import com.mattgray.socialnetworkkata.followees.InMemoryFolloweeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mattgray.socialnetworkkata.common.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserRepositoryShould {

    private FolloweeRepository mockFolloweeRepository;
    private List<User> usersMock;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockFolloweeRepository = mock(InMemoryFolloweeRepository.class);
        usersMock = new ArrayList<>();
        userRepository = new InMemoryUserRepository(mockFolloweeRepository, usersMock);
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

    @Test
    void addUserIfAFollowCommandIsReceivedAndTheUserDoesNotAlreadyExist() {
        userRepository.addFollowee(ALICE_USER_NAME, BOB_USER_NAME);

        assertThat(usersMock.get(0).getUserName()).isEqualTo(ALICE_USER_NAME);
    }

    @Test
    void callFolloweeRepositoryToAddFolloweeWhenAFollowCommandIsReceived() {
        userRepository.addFollowee(ALICE_USER_NAME, BOB_USER_NAME);

        verify(mockFolloweeRepository).addFollowee(BOB_USER_NAME);
    }

    @Test
    void onlyAddNewUserIfUserDoesNotAlreadyExistWhenFollowCommandIsReceived() {
        userRepository.addFollowee(ALICE_USER_NAME, BOB_USER_NAME);
        userRepository.addFollowee(ALICE_USER_NAME, CHARLIE_USER_NAME);

        assertThat(usersMock.size()).isEqualTo(1);
    }
}
