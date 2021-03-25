import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class SocialNetworkShould {

    SocialNetwork socialNetwork;
    CommandProcessor mockCommandProcessor;
    Clock clockStub;

    @BeforeEach
    void setUp() {
        mockCommandProcessor = mock(CommandProcessor.class);
        clockStub = mock(Clock.class);
        socialNetwork = new SocialNetwork(mockCommandProcessor, clockStub);
    }

    @Test
    void callCommandProcessorWithCommandEnteredAndTimeOfCommandWhenAnythingIsEnteredInConsole() {

        Clock fixedClock = Clock.fixed(TestCommands.AT_12PM.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        when(clockStub.instant()).thenReturn(fixedClock.instant());
        when(clockStub.getZone()).thenReturn(fixedClock.getZone());

        System.setIn(new ByteArrayInputStream(TestCommands.ALICE_USER_NAME.getBytes()));
        socialNetwork.run();

        verify(mockCommandProcessor).process(TestCommands.ALICE_USER_NAME, LocalDateTime.now(clockStub));
    }
}