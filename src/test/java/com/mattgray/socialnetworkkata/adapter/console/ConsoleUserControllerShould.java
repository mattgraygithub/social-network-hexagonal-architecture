package com.mattgray.socialnetworkkata.adapter.console;

import com.mattgray.socialnetworkkata.port.TimelineService;
import com.mattgray.socialnetworkkata.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConsoleUserControllerShould {

    private static final Clock FIXED_CLOCK_AT_12PM = Clock.fixed(AT_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    private static ByteArrayOutputStream byteArrayOutputStream;
    private static Clock clockStub;
    private static ConsoleUserController commandProcessor;
    private static UserService mockUserService;
    private static TimelineService mockTimelineService;

    @BeforeEach
    void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        clockStub = mock(Clock.class);
        mockUserService = mock(UserService.class);
        mockTimelineService = mock(TimelineService.class);
        commandProcessor = new ConsoleUserController(mockUserService, mockTimelineService);
    }

    @Test
    void delegatePostCommandsToUserService() {
        runCommand(ALICE_EXAMPLE_POST_COMMAND);

        verify(mockUserService).addPost(ALICE_EXAMPLE_POST_COMMAND, LocalDateTime.now(FIXED_CLOCK_AT_12PM));
    }

    @Test
    void callUserServiceToGetPostsForAGivenUserNameWhenAReadTimelineCommandIsReceived() {
        runCommand(READ_ALICE_TIMELINE);

        verify(mockUserService).getPosts(ALICE_USER_NAME);
    }

    @Test
    void callTimelineServiceToGetCorrectlyFormattedTimelineWhenAReadTimelineCommandIsReceived() {
        when(mockUserService.getPosts(ALICE_USER_NAME)).thenReturn(ALICE_EXAMPLE_POST_LIST);
        runCommand(READ_ALICE_TIMELINE);

        verify(mockTimelineService).getTimeLine(ALICE_EXAMPLE_POST_LIST, LocalDateTime.now(FIXED_CLOCK_AT_12PM));
    }

    @Test
    void printTimelineToConsoleWhenAReadTimelineCommandIsReceived() throws IOException {
        when(mockUserService.getPosts(ALICE_USER_NAME)).thenReturn(ALICE_EXAMPLE_POST_LIST);

        setUpClockStub();
        when(mockTimelineService.getTimeLine(ALICE_EXAMPLE_POST_LIST, LocalDateTime.now(clockStub))).thenReturn(ALICE_EXAMPLE_POST + FIVE_MINUTES_AGO);

        runCommand(READ_ALICE_TIMELINE);

        assertThat(getConsoleOutput()).isEqualTo(ALICE_EXAMPLE_POST + FIVE_MINUTES_AGO);
    }

    @Test
    void delegateFollowCommandsToUserService() {
        runCommand(CHARLIE_FOLLOWS_ALICE);

        verify(mockUserService).addFollowee(CHARLIE_FOLLOWS_ALICE);
    }

    @Test
    void delegateReadWallCommandsToUserService() {
        runCommand(READ_CHARLIE_WALL);

        verify(mockUserService).getWall(READ_CHARLIE_WALL, LocalDateTime.now(FIXED_CLOCK_AT_12PM));
    }

    private void runCommand(String command) {
        setUpClockStub();
        System.setIn(new ByteArrayInputStream(command.getBytes()));
        commandProcessor.process(clockStub);
    }

    private void setUpClockStub() {
        when(clockStub.instant()).thenReturn(FIXED_CLOCK_AT_12PM.instant());
        when(clockStub.getZone()).thenReturn(FIXED_CLOCK_AT_12PM.getZone());
    }

    private String getConsoleOutput() throws IOException {
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toString();
    }
}
