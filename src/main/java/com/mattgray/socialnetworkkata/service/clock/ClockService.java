package com.mattgray.socialnetworkkata.service.clock;

import java.time.LocalDateTime;

public interface ClockService {
    String getTimeBetween(LocalDateTime timeOfPosting, LocalDateTime timeOfReading);
}
