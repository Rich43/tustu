package java.time.chrono;

import java.time.DateTimeException;

/* loaded from: rt.jar:java/time/chrono/MinguoEra.class */
public enum MinguoEra implements Era {
    BEFORE_ROC,
    ROC;

    public static MinguoEra of(int i2) {
        switch (i2) {
            case 0:
                return BEFORE_ROC;
            case 1:
                return ROC;
            default:
                throw new DateTimeException("Invalid era: " + i2);
        }
    }

    @Override // java.time.chrono.Era
    public int getValue() {
        return ordinal();
    }
}
