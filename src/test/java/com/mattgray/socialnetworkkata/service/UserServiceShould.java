package com.mattgray.socialnetworkkata.service;

import com.mattgray.socialnetworkkata.adapter.InMemoryFolloweeRepository;
import com.mattgray.socialnetworkkata.adapter.InMemoryPostRepository;
import com.mattgray.socialnetworkkata.domain.Post;
import com.mattgray.socialnetworkkata.domain.User;
import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.port.UserRepository;
import com.mattgray.socialnetworkkata.port.WallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.mockito.Mockito.*;

class UserServiceShould {

    private static UserRepository mockUserRepository;
    private static TimelineService mockTimelineService;
    private static UserService userService;
    private static LocalDateTime now;
    private static WallService mockWallService;

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
        ArrayList<Post> posts = generatePosts(ALICE_USER_NAME, ALICE_EXAMPLE_POST);
        when(mockUserRepository.getPostsFor((ALICE_USER_NAME))).thenReturn(new InMemoryPostRepository(posts));

        userService.getTimeLine(ALICE_USER_NAME, now);

        verify(mockTimelineService).displayTimeLine(posts, now);
    }

    @Test
    void callUserRepositoryToAddFollowee() {
        userService.addFollowee(CHARLIE_FOLLOWS_ALICE);

        verify(mockUserRepository).addFollowee(CHARLIE_USER_NAME, ALICE_USER_NAME);
    }

    @Test
    void callWallServiceToPrintWallForAUser() {
        User charlie = new User(CHARLIE_USER_NAME, new InMemoryPostRepository(generatePosts(CHARLIE_USER_NAME, CHARLIE_EXAMPLE_POST)), new InMemoryFolloweeRepository(new ArrayList<>()));
        when(mockUserRepository.getUser(CHARLIE_USER_NAME)).thenReturn(charlie);

        ArrayList<User> followedUsers = new ArrayList<>(Collections.singletonList(new User(ALICE_USER_NAME, new InMemoryPostRepository(generatePosts(ALICE_USER_NAME, ALICE_EXAMPLE_POST)), new InMemoryFolloweeRepository(new ArrayList<>()))));
        when(mockUserRepository.getFollowedUsersFor(CHARLIE_USER_NAME)).thenReturn(followedUsers);

        userService.getWall(READ_CHARLIE_WALL, now);

        verify(mockWallService).displayWall(charlie, followedUsers, now);
    }

    private ArrayList<Post> generatePosts(String userName, String post) {
        return new ArrayList<>(Collections.singletonList(new Post(userName, post, now)));
    }
}
