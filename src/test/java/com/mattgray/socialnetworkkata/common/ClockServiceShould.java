package com.mattgray.socialnetworkkata.common;

import org.junit.jupiter.api.BeforeEach;

import java.time.Clock;

import static org.mockito.Mockito.mock;

class ClockServiceShould {

    Clock clockStub;
    ClockService clockService;

    @BeforeEach
    void setUp() {
        clockStub = mock(Clock.class);
        clockService = new ClockServiceImpl();
    }

}
