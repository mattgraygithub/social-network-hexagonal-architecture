package com.mattgray.socialnetworkkata.clock;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class ClockServiceImpl implements ClockService {

    @Override
    public String getTimeBetween(LocalDateTime timeOfPosting, LocalDateTime timeOfReading) {
        long timeDifference = ChronoUnit.SECONDS.between(timeOfPosting, timeOfReading);

        return Stream.of(Clock.values())
                .filter(c -> timeDifference < c.getTimeUnitUpperLimit())
                .findAny().orElseThrow()
                .getFormattedTimeDifference(timeDifference);
    }
}
