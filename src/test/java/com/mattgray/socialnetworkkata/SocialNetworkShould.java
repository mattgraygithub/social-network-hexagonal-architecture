package com.mattgray.socialnetworkkata;

import com.mattgray.socialnetworkkata.port.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static com.mattgray.socialnetworkkata.TestData.AT_12PM;
import static org.mockito.Mockito.*;

class SocialNetworkShould {

    private static SocialNetwork socialNetwork;
    private static UserController mockUserController;
    private static Clock clockStub;

    @BeforeEach
    void setUp() {
        mockUserController = mock(UserController.class);
        clockStub = mock(Clock.class);
        socialNetwork = new SocialNetwork(mockUserController, clockStub);
    }

    @Test
    void callCommandProcessorWithCommandEnteredAndTimeOfCommandWhenAnythingIsEnteredInConsole() {
        Clock fixedClock = Clock.fixed(AT_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        when(clockStub.instant()).thenReturn(fixedClock.instant());
        when(clockStub.getZone()).thenReturn(fixedClock.getZone());

        socialNetwork.runCLI();

        verify(mockUserController).process(LocalDateTime.now(clockStub));
    }
}
