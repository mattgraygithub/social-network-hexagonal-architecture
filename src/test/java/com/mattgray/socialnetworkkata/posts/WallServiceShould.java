package com.mattgray.socialnetworkkata.posts;

import com.mattgray.socialnetworkkata.common.ClockService;
import com.mattgray.socialnetworkkata.followees.InMemoryFolloweeRepository;
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
import java.util.Arrays;
import java.util.Collections;

import static com.mattgray.socialnetworkkata.common.TestData.*;
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
        User charlie = new User(CHARLIE_USER_NAME, new InMemoryPostRepository(generatePosts(CHARLIE_USER_NAME, CHARLIE_EXAMPLE_POST, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM))), new InMemoryFolloweeRepository(new ArrayList<>()));
        ArrayList<User> followedUsers = new ArrayList<>();

        wallService.displayWall(charlie, followedUsers, stubbedLocalTimeOf(AT_12PM));

        verify(clockServiceMock).getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM));
    }

    @Test
    void printAUsersWallWithOnePostFromThatUserAndNoFollowedUsers() throws IOException {
        User charlie = new User(CHARLIE_USER_NAME, new InMemoryPostRepository(generatePosts(CHARLIE_USER_NAME, CHARLIE_EXAMPLE_POST, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM))), new InMemoryFolloweeRepository(new ArrayList<>()));
        ArrayList<User> followedUsers = new ArrayList<>();

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(" (5 minutes ago)");

        wallService.displayWall(charlie, followedUsers, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(CHARLIE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + CHARLIE_EXAMPLE_POST + FIVE_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithTwoPostsAndNoFollowedUsers() throws IOException {
        User bob = new User(BOB_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>(Arrays.asList(new Post(BOB_USER_NAME, BOB_EXAMPLE_POST_ONE, stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM)), new Post(BOB_USER_NAME, BOB_EXAMPLE_POST_TWO, stubbedLocalTimeOf(AT_1_MINUTE_BEFORE_12PM))))),
                new InMemoryFolloweeRepository(new ArrayList<>()));

        ArrayList<User> followedUsers = new ArrayList<>();

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_1_MINUTE_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(ONE_MINUTE_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);

        wallService.displayWall(bob, followedUsers, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_TWO + ONE_MINUTE_AGO + NEW_LINE +
                        BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + TWO_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithNoPostsOfTheirOwnButOnePostFromOneFollowedUser() throws IOException {
        User alice = new User(ALICE_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>()),
                new InMemoryFolloweeRepository(new ArrayList<>(Collections.singletonList(BOB_USER_NAME))));

        User bob = new User(BOB_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>(Collections.singletonList(new Post(BOB_USER_NAME, BOB_EXAMPLE_POST_ONE, stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM))))),
                new InMemoryFolloweeRepository(new ArrayList<>()));

        ArrayList<User> followedUsers = new ArrayList<>(Collections.singletonList(bob));

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);

        wallService.displayWall(alice, followedUsers, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + TWO_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithOnePostOfTheirOwnAndOnePostFromOneFollowedUser() throws IOException {
        User alice = new User(ALICE_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>(Collections.singletonList(new Post(ALICE_USER_NAME, ALICE_EXAMPLE_POST, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM))))),
                new InMemoryFolloweeRepository(new ArrayList<>(Collections.singletonList(BOB_USER_NAME))));

        User bob = new User(BOB_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>(Collections.singletonList(new Post(BOB_USER_NAME, BOB_EXAMPLE_POST_ONE, stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM))))),
                new InMemoryFolloweeRepository(new ArrayList<>()));

        ArrayList<User> followedUsers = new ArrayList<>(Collections.singletonList(bob));

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);

        wallService.displayWall(alice, followedUsers, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + TWO_MINUTES_AGO + NEW_LINE +
                        ALICE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + ALICE_EXAMPLE_POST + FIVE_MINUTES_AGO + NEW_LINE);
    }

    @Test
    void printAUsersWallWithOnePostOfTheirOwnAndOnePostFromOneFollowedUserPostedBeforeTheirOwnPost() throws IOException {
        User alice = new User(ALICE_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>(Collections.singletonList(new Post(ALICE_USER_NAME, ALICE_EXAMPLE_POST, stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM))))),
                new InMemoryFolloweeRepository(new ArrayList<>(Collections.singletonList(BOB_USER_NAME))));

        User bob = new User(BOB_USER_NAME,
                new InMemoryPostRepository(new ArrayList<>(Collections.singletonList(new Post(BOB_USER_NAME, BOB_EXAMPLE_POST_ONE, stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM))))),
                new InMemoryFolloweeRepository(new ArrayList<>()));

        ArrayList<User> followedUsers = new ArrayList<>(Collections.singletonList(bob));

        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_2_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(TWO_MINUTES_AGO);
        when(clockServiceMock.getTimeBetween(stubbedLocalTimeOf(AT_5_MINUTES_BEFORE_12PM), stubbedLocalTimeOf(AT_12PM))).thenReturn(FIVE_MINUTES_AGO);

        wallService.displayWall(alice, followedUsers, stubbedLocalTimeOf(AT_12PM));

        assertThat(getConsoleOutput()).isEqualTo(
                ALICE_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + ALICE_EXAMPLE_POST + TWO_MINUTES_AGO + NEW_LINE +
                        BOB_USER_NAME + DELIMITER_BETWEEN_USERNAME_AND_POST + BOB_EXAMPLE_POST_ONE + FIVE_MINUTES_AGO + NEW_LINE);
    }

    private ArrayList<Post> generatePosts(String userName, String post, LocalDateTime time) {
        return new ArrayList<>(Collections.singletonList(new Post(userName, post, time)));
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
