package com.mattgray.socialnetworkkata.clock;

public enum Clock {

    SECONDS(60) {
        @Override
        public String getFormattedTimeDifference(long timeDifferenceInSeconds) {
            return isSingular(timeDifferenceInSeconds) ? " (1 second ago)" : " (" + timeDifferenceInSeconds + " seconds ago)";
        }
    },

    MINUTES(3600) {
        @Override
        public String getFormattedTimeDifference(long timeDifferenceInSeconds) {
            long timeDifferenceInMinutes = timeDifferenceInSeconds / SECONDS.timeUnitUpperLimit;
            return isSingular(timeDifferenceInMinutes) ? " (1 minute ago)" : " (" + timeDifferenceInMinutes + " minutes ago)";
        }
    },

    HOURS(216000) {
        @Override
        public String getFormattedTimeDifference(long timeDifferenceInSeconds) {
            long timeDifferenceInHours = timeDifferenceInSeconds / MINUTES.timeUnitUpperLimit;
            return isSingular(timeDifferenceInHours) ? " (1 hour ago)" : " (" + timeDifferenceInHours + " hours ago)";
        }
    };

    private final long timeUnitUpperLimit;

    Clock(long timeUnitUpperLimit) {
        this.timeUnitUpperLimit = timeUnitUpperLimit;
    }

    public long getTimeUnitUpperLimit() {
        return this.timeUnitUpperLimit;
    }

    public boolean isSingular(long timeDifference) {
        return timeDifference == 1;
    }

    public abstract String getFormattedTimeDifference(long timeDifferenceInSeconds);
}
