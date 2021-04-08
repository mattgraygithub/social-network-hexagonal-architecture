package com.mattgray.socialnetworkkata.common;

public enum Clock {

    SECONDS(60) {
        @Override
        public String getFormattedTimeDifference(long timeDifferenceInSeconds) {
            return timeDifferenceInSeconds == 1 ? " (1 second ago)" : " (" + timeDifferenceInSeconds + " seconds ago)";
        }
    },

    MINUTES(3600) {
        @Override
        public String getFormattedTimeDifference(long timeDifferenceInSeconds) {
            long TimeDifferenceInMinutes = timeDifferenceInSeconds / SECONDS.timeUnitUpperLimit;
            return TimeDifferenceInMinutes == 1 ? " (1 minute ago)" : " (" + TimeDifferenceInMinutes + " minutes ago)";
        }
    },

    HOURS(216000) {
        @Override
        public String getFormattedTimeDifference(long timeDifferenceInSeconds) {
            long TimeDifferenceInHours = timeDifferenceInSeconds / MINUTES.timeUnitUpperLimit;
            return TimeDifferenceInHours == 1 ? " (1 hour ago)" : " (" + TimeDifferenceInHours + " hours ago)";
        }
    };

    private final long timeUnitUpperLimit;

    Clock(long timeUnitUpperLimit) {
        this.timeUnitUpperLimit = timeUnitUpperLimit;
    }

    public long getTimeUnitUpperLimit() {
        return this.timeUnitUpperLimit;
    }

    public abstract String getFormattedTimeDifference(long timeDifferenceInSeconds);
}
