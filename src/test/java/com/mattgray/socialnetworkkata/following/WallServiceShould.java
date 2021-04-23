package com.mattgray.socialnetworkkata.following;

import com.mattgray.socialnetworkkata.clock.ClockService;
import com.mattgray.socialnetworkkata.posting.InMemoryPostRepository;
import com.mattgray.socialnetworkkata.posting.Post;
import com.mattgray.socialnetworkkata.posting.PostRepository;
import com.mattgray.socialnetworkkata.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class WallServiceShould {

    ByteArrayOutputStream byteArrayOutputStream;
    Clock clockStub;
    ClockService clockServiceMock;
    LocalDateTime now;
    WallService wallService;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        clockServiceMock = mock(ClockService.class);
        wallService = new WallServiceImpl(clockServiceMock);
    }

    @Test
    void callClockServiceToGetTimeBetweenPostAndWallCommand() {
        wallService.displayWall(testUserWithOnePostAndNoFollowedUsers(), emptyListOfFollowedUsers(), stubbedLocalTimeOf(AT_12PM));

        verify(clockServiceMock).getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM));
    }

    @Test
    void printAUsersWallWithOnePostFromThatUserAndNoFollowedUsers() throws IOException {
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);

        wallService.displayWall(testUserWithOnePostAndNoFollowedUsers(), emptyListOfFollowedUsers(), stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(CHARLIE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + CHARLIE_EXAMPLE_POST + FIVE_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithTwoPostsAndNoFollowedUsers() throws IOException {
        PostRepository postRepository = getTestPostRepository(BOB_USER_NAME, new String[]{BOB_EXAMPLE_POST_TWO, BOB_EXAMPLE_POST_ONE}, new LocalDateTime[]{AT_1_MINUTE_BEFORE_12PM, AT_2_MINUTES_BEFORE_12PM});
        User bob = getTestUser(BOB_USER_NAME, postRepository, emptyFolloweeRepository());

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_1_MINUTE_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(ONE_MINUTE_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);

        wallService.displayWall(bob, emptyListOfFollowedUsers(), stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_TWO + ONE_MINUTE_AGO + NEW_LINE +
                        BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + TWO_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithNoPostsOfTheirOwnButOnePostFromOneFollowedUser() throws IOException {
        PostRepository alicePostRepository = new InMemoryPostRepository(new ArrayList<>());
        User alice = getTestUser(ALICE_USER_NAME, alicePostRepository, getTestFolloweeRepository(new ArrayList<>(Collections.singletonList(BOB_USER_NAME))));

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);

        wallService.displayWall(alice, getListOfOneFollowedUserWIthOnePost(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + TWO_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithOnePostOfTheirOwnAndOnePostFromOneFollowedUser() throws IOException {
        PostRepository alicePostRepository = getTestPostRepository(ALICE_USER_NAME, new String[]{ALICE_EXAMPLE_POST}, new LocalDateTime[]{AT_5_MINUTES_BEFORE_12PM});
        FolloweeRepository followeeRepository = getTestFolloweeRepository(new ArrayList<>(Collections.singletonList(BOB_USER_NAME)));
        User alice = getTestUser(ALICE_USER_NAME, alicePostRepository, followeeRepository);

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);

        wallService.displayWall(alice, getListOfOneFollowedUserWIthOnePost(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + TWO_MINUTES_AGO + NEW_LINE +
                        ALICE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + ALICE_EXAMPLE_POST + FIVE_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithOnePostOfTheirOwnAndOnePostFromOneFollowedUserPostedBeforeTheirOwnPost() throws IOException {
        PostRepository alicePostRepository = getTestPostRepository(ALICE_USER_NAME, new String[]{ALICE_EXAMPLE_POST}, new LocalDateTime[]{AT_2_MINUTES_BEFORE_12PM});
        FolloweeRepository followeeRepository = getTestFolloweeRepository(new ArrayList<>(Collections.singletonList(BOB_USER_NAME)));
        User alice = getTestUser(ALICE_USER_NAME, alicePostRepository, followeeRepository);

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);

        wallService.displayWall(alice, getListOfOneFollowedUserWIthOnePost(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                ALICE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + ALICE_EXAMPLE_POST + TWO_MINUTES_AGO + NEW_LINE +
                        BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + FIVE_MINUTES_AGO + NEW_LINE);
    }

    private User testUserWithOnePostAndNoFollowedUsers() {
        PostRepository postRepository = getTestPostRepository(CHARLIE_USER_NAME, new String[]{CHARLIE_EXAMPLE_POST}, new LocalDateTime[]{AT_5_MINUTES_BEFORE_12PM});
        return getTestUser(CHARLIE_USER_NAME, postRepository, emptyFolloweeRepository());
    }

    private User getTestUser(String username, PostRepository postRepository, FolloweeRepository followeeRepository) {
        return new User(username, postRepository, followeeRepository);
    }

    private PostRepository getTestPostRepository(String userName, String[] textOfPosts, LocalDateTime[] timesOfPosts) {
        ArrayList<Post> posts = new ArrayList<>();

        for (int i = 0; i < textOfPosts.length; i++) {
            posts.add(new Post(userName, textOfPosts[i], stubbedLocalTimeOf(timesOfPosts[i])));
        }

        return new InMemoryPostRepository(posts);
    }

    private ArrayList<User> getListOfOneFollowedUserWIthOnePost(LocalDateTime timeOfPost) {
        PostRepository bobPostRepository = getTestPostRepository(BOB_USER_NAME, new String[]{BOB_EXAMPLE_POST_ONE}, new LocalDateTime[]{timeOfPost});
        User bob = getTestUser(BOB_USER_NAME, bobPostRepository, emptyFolloweeRepository());
        return new ArrayList<>(Collections.singletonList(bob));
    }

    private FolloweeRepository getTestFolloweeRepository(ArrayList<String> followedUsers) {
        return new InMemoryFolloweeRepository(followedUsers);
    }

    private FolloweeRepository emptyFolloweeRepository() {
        return new InMemoryFolloweeRepository(new ArrayList<>());
    }

    private ArrayList<User> emptyListOfFollowedUsers() {
        return new ArrayList<>();
    }

    private LocalDateTime stubbedLocalTimeOf(LocalDateTime time) {
        Clock readCommandClock = Clock.fixed(time.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        return LocalDateTime.now(readCommandClock);
    }

    private String getConsoleOutput() throws IOException {
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toString();
    }
}
