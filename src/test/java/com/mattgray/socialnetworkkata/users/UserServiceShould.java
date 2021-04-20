package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.posts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static com.mattgray.socialnetworkkata.common.TestData.*;
import static org.mockito.Mockito.*;

class UserServiceShould {

    UserRepository mockUserRepository;
    TimelineService mockTimelineService;
    UserService userService;
    LocalDateTime now;
    WallService mockWallService;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        mockUserRepository = mock(UserRepository.class);
        mockTimelineService = mock(TimelineService.class);
        mockWallService = mock(WallService.class);
        userService = new UserService(mockUserRepository, mockTimelineService, mockWallService);
    }

    @Test
    void callUserRepositoryToAddPost() {
        userService.addPost(ALICE_EXAMPLE_POST_COMMAND, now);

        verify(mockUserRepository).addPost(ALICE_USER_NAME, ALICE_EXAMPLE_POST, now);
    }

    @Test
    void callTimelineServiceToPrintTimelineForAUser() {
        userService.addPost(ALICE_EXAMPLE_POST_COMMAND, now);
        ArrayList<Post> timeline = new ArrayList<>(Collections.singletonList(new Post(ALICE_EXAMPLE_POST, now)));

        when(mockUserRepository.getTimelineFor((ALICE_USER_NAME))).thenReturn(new InMemoryPostRepository(timeline));

        userService.getTimeLine(ALICE_USER_NAME, now);

        verify(mockTimelineService).displayTimeLine(timeline, now);
    }

    @Test
    void callUserRepositoryToAddFollowee() {
        userService.addFollowee(CHARLIE_FOLLOWS_ALICE);

        verify(mockUserRepository).addFollowee(CHARLIE_USER_NAME, ALICE_USER_NAME);
    }

    @Test
    void callWallServiceToConstructAndPrintWallForAUser() {
        userService.getWall(READ_CHARLIE_WALL, now);

        verify(mockWallService).displayWall(CHARLIE_USER_NAME, now);
    }
}
