package com.mattgray.socialnetworkkata.clock;

import java.time.LocalDateTime;

public interface ClockService {
    String getTimeBetween(LocalDateTime timeOfPosting, LocalDateTime timeOfReading);
}
