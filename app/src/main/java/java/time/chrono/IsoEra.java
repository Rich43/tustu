package java.time.chrono;

import java.time.DateTimeException;

/* loaded from: rt.jar:java/time/chrono/IsoEra.class */
public enum IsoEra implements Era {
    BCE,
    CE;

    public static IsoEra of(int i2) {
        switch (i2) {
            case 0:
                return BCE;
            case 1:
                return CE;
            default:
                throw new DateTimeException("Invalid era: " + i2);
        }
    }

    @Override // java.time.chrono.Era
    public int getValue() {
        return ordinal();
    }
}
