package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.followees.InMemoryFolloweeRepository;
import com.mattgray.socialnetworkkata.posts.InMemoryPostRepository;
import com.mattgray.socialnetworkkata.posts.Post;
import com.mattgray.socialnetworkkata.posts.TimelineService;
import com.mattgray.socialnetworkkata.posts.WallService;
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
        ArrayList<Post> posts = generatePosts(ALICE_EXAMPLE_POST);
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
        ArrayList<Post> posts = generatePosts(CHARLIE_EXAMPLE_POST);
        when(mockUserRepository.getPostsFor((CHARLIE_USER_NAME))).thenReturn(new InMemoryPostRepository(posts));

        ArrayList<Post> alicePosts = generatePosts(ALICE_EXAMPLE_POST);
        ArrayList<User> followedUsers = new ArrayList<>(Collections.singletonList(new User(ALICE_USER_NAME, new InMemoryPostRepository(alicePosts), new InMemoryFolloweeRepository(new ArrayList<>()))));
        when(mockUserRepository.getFollowedUsersFor(CHARLIE_USER_NAME)).thenReturn(followedUsers);

        userService.getWall(READ_CHARLIE_WALL, now);

        verify(mockWallService).displayWall(posts, followedUsers, now);
    }

    private ArrayList<Post> generatePosts(String post) {
        return new ArrayList<>(Collections.singletonList(new Post(post, now)));
    }
}
