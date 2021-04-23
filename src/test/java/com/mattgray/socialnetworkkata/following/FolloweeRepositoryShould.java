package com.mattgray.socialnetworkkata.following;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.mattgray.socialnetworkkata.TestData.ALICE_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class FolloweeRepositoryShould {

    @Mock
    ArrayList<String> followedUsersMock;

    FolloweeRepository followeeRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        followeeRepository = new InMemoryFolloweeRepository(followedUsersMock);
    }

    @Test
    void addUserNameOfFollowedUsersToFolloweeRepository() {
        followeeRepository.addFollowee(ALICE_USER_NAME);

        verify(followedUsersMock).add(ALICE_USER_NAME);
    }

    @Test
    void getListOfFollowedUsers() {
        assertThat(followeeRepository.getFollowedUsers()).isEqualTo(followedUsersMock);
    }
}