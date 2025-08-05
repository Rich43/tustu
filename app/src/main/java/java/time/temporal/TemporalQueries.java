package java.time.temporal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.Chronology;

/* loaded from: rt.jar:java/time/temporal/TemporalQueries.class */
public final class TemporalQueries {
    static final TemporalQuery<ZoneId> ZONE_ID = temporalAccessor -> {
        return (ZoneId) temporalAccessor.query(ZONE_ID);
    };
    static final TemporalQuery<Chronology> CHRONO = temporalAccessor -> {
        return (Chronology) temporalAccessor.query(CHRONO);
    };
    static final TemporalQuery<TemporalUnit> PRECISION = temporalAccessor -> {
        return (TemporalUnit) temporalAccessor.query(PRECISION);
    };
    static final TemporalQuery<ZoneOffset> OFFSET = temporalAccessor -> {
        if (temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)) {
            return ZoneOffset.ofTotalSeconds(temporalAccessor.get(ChronoField.OFFSET_SECONDS));
        }
        return null;
    };
    static final TemporalQuery<ZoneId> ZONE = temporalAccessor -> {
        ZoneId zoneId = (ZoneId) temporalAccessor.query(ZONE_ID);
        return zoneId != null ? zoneId : (ZoneId) temporalAccessor.query(OFFSET);
    };
    static final TemporalQuery<LocalDate> LOCAL_DATE = temporalAccessor -> {
        if (temporalAccessor.isSupported(ChronoField.EPOCH_DAY)) {
            return LocalDate.ofEpochDay(temporalAccessor.getLong(ChronoField.EPOCH_DAY));
        }
        return null;
    };
    static final TemporalQuery<LocalTime> LOCAL_TIME = temporalAccessor -> {
        if (temporalAccessor.isSupported(ChronoField.NANO_OF_DAY)) {
            return LocalTime.ofNanoOfDay(temporalAccessor.getLong(ChronoField.NANO_OF_DAY));
        }
        return null;
    };

    private TemporalQueries() {
    }

    public static TemporalQuery<ZoneId> zoneId() {
        return ZONE_ID;
    }

    public static TemporalQuery<Chronology> chronology() {
        return CHRONO;
    }

    public static TemporalQuery<TemporalUnit> precision() {
        return PRECISION;
    }

    public static TemporalQuery<ZoneId> zone() {
        return ZONE;
    }

    public static TemporalQuery<ZoneOffset> offset() {
        return OFFSET;
    }

    public static TemporalQuery<LocalDate> localDate() {
        return LOCAL_DATE;
    }

    public static TemporalQuery<LocalTime> localTime() {
        return LOCAL_TIME;
    }
}
