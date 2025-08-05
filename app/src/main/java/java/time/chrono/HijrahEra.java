package java.time.chrono;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;

/* loaded from: rt.jar:java/time/chrono/HijrahEra.class */
public enum HijrahEra implements Era {
    AH;

    public static HijrahEra of(int i2) {
        if (i2 == 1) {
            return AH;
        }
        throw new DateTimeException("Invalid era: " + i2);
    }

    @Override // java.time.chrono.Era
    public int getValue() {
        return 1;
    }

    @Override // java.time.chrono.Era, java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.ERA) {
            return ValueRange.of(1L, 1L);
        }
        return super.range(temporalField);
    }
}
