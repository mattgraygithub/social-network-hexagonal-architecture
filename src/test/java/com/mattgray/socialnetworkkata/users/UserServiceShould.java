package com.mattgray.socialnetworkkata.users;

import com.mattgray.socialnetworkkata.timeline.Post;
import com.mattgray.socialnetworkkata.timeline.Timeline;
import com.mattgray.socialnetworkkata.timeline.TimelineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static com.mattgray.socialnetworkkata.common.TestData.*;
import static org.mockito.Mockito.*;

class UserServiceShould {

    UserRepository mockUserRepository;
    TimelineServiceImpl mockTimelineService;
    UserService userService;
    LocalDateTime now;


    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        mockUserRepository = mock(UserRepository.class);
        mockTimelineService = mock(TimelineServiceImpl.class);
        userService = new UserService(mockUserRepository, mockTimelineService);
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

        when(mockUserRepository.getTimelineFor((ALICE_USER_NAME))).thenReturn(new Timeline(timeline));

        userService.getTimeLine(ALICE_USER_NAME, now);

        verify(mockTimelineService).displayTimeLine(timeline, now);
    }
}
