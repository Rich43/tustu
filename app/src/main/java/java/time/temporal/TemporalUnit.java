package java.time.temporal;

import java.time.Duration;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;

/* loaded from: rt.jar:java/time/temporal/TemporalUnit.class */
public interface TemporalUnit {
    Duration getDuration();

    boolean isDurationEstimated();

    boolean isDateBased();

    boolean isTimeBased();

    <R extends Temporal> R addTo(R r2, long j2);

    long between(Temporal temporal, Temporal temporal2);

    String toString();

    default boolean isSupportedBy(Temporal temporal) {
        if (temporal instanceof LocalTime) {
            return isTimeBased();
        }
        if (temporal instanceof ChronoLocalDate) {
            return isDateBased();
        }
        if ((temporal instanceof ChronoLocalDateTime) || (temporal instanceof ChronoZonedDateTime)) {
            return true;
        }
        try {
            temporal.plus(1L, this);
            return true;
        } catch (UnsupportedTemporalTypeException e2) {
            return false;
        } catch (RuntimeException e3) {
            try {
                temporal.plus(-1L, this);
                return true;
            } catch (RuntimeException e4) {
                return false;
            }
        }
    }
}
