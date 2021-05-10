package com.mattgray.socialnetworkkata.adapter;

import com.mattgray.socialnetworkkata.port.FolloweeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.mattgray.socialnetworkkata.TestData.ALICE_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class FolloweeRepositoryShould {

    private static FolloweeRepository followeeRepository;

    @Mock
    private static ArrayList<String> followedUsersMock;

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