package java.time.chrono;

import java.time.DateTimeException;

/* loaded from: rt.jar:java/time/chrono/ThaiBuddhistEra.class */
public enum ThaiBuddhistEra implements Era {
    BEFORE_BE,
    BE;

    public static ThaiBuddhistEra of(int i2) {
        switch (i2) {
            case 0:
                return BEFORE_BE;
            case 1:
                return BE;
            default:
                throw new DateTimeException("Invalid era: " + i2);
        }
    }

    @Override // java.time.chrono.Era
    public int getValue() {
        return ordinal();
    }
}
