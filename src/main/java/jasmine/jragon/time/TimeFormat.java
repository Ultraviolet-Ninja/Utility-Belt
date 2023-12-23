package jasmine.jragon.time;

import java.time.Duration;

public final class TimeFormat {
    public static String convertTime(long duration) {
        var nanos = Duration.ofNanos(duration);

        long hours = nanos.toHours();
        long minutes = nanos.toMinutes();
        long seconds = nanos.getSeconds() % 60;
        long millis = nanos.toMillis() % 1000;

        if (hours != 0) {
            return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, millis);
        } else if (minutes != 0) {
            return String.format("%02d:%02d.%d", minutes, seconds, millis);
        }

        return String.format("%02d.%03d sec", seconds, millis);
    }
}
