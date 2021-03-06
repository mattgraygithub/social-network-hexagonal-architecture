package com.mattgray.socialnetworkkata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.mattgray.socialnetworkkata.TestData.ALICE_USER_NAME;
import static com.mattgray.socialnetworkkata.TestData.AT_12PM;
import static org.mockito.Mockito.*;

class SocialNetworkShould {

    private static SocialNetwork socialNetwork;
    private static CommandProcessor mockCommandProcessor;
    private static Clock clockStub;

    @BeforeEach
    void setUp() {
        mockCommandProcessor = mock(CommandProcessor.class);
        clockStub = mock(Clock.class);
        socialNetwork = new SocialNetwork(mockCommandProcessor, clockStub);
    }

    @Test
    void callCommandProcessorWithCommandEnteredAndTimeOfCommandWhenAnythingIsEnteredInConsole() {
        Clock fixedClock = Clock.fixed(AT_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        when(clockStub.instant()).thenReturn(fixedClock.instant());
        when(clockStub.getZone()).thenReturn(fixedClock.getZone());

        System.setIn(new ByteArrayInputStream(ALICE_USER_NAME.getBytes()));
        socialNetwork.run();

        verify(mockCommandProcessor).process(ALICE_USER_NAME, LocalDateTime.now(clockStub));
    }
}
