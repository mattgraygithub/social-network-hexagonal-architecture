package com.mattgray.socialnetworkkata.common;

import java.time.LocalDateTime;

public interface ClockService {
    String getTimeBetween(LocalDateTime timeOfPosting, LocalDateTime timeOfReading);
}
