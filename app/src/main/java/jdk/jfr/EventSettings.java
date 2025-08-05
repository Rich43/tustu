package jdk.jfr;

import java.time.Duration;
import java.util.Map;

/* loaded from: jfr.jar:jdk/jfr/EventSettings.class */
public abstract class EventSettings {
    public abstract EventSettings with(String str, String str2);

    abstract Map<String, String> toMap();

    EventSettings() {
    }

    public final EventSettings withStackTrace() {
        return with("stackTrace", "true");
    }

    public final EventSettings withoutStackTrace() {
        return with("stackTrace", "false");
    }

    public final EventSettings withoutThreshold() {
        return with(Threshold.NAME, "0 s");
    }

    public final EventSettings withPeriod(Duration duration) {
        return with("period", duration.toNanos() + " ns");
    }

    public final EventSettings withThreshold(Duration duration) {
        if (duration == null) {
            return with(Threshold.NAME, "0 ns");
        }
        return with(Threshold.NAME, duration.toNanos() + " ns");
    }
}
