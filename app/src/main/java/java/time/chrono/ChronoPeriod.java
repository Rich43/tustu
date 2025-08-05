package java.time.chrono;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/ChronoPeriod.class */
public interface ChronoPeriod extends TemporalAmount {
    @Override // java.time.temporal.TemporalAmount
    long get(TemporalUnit temporalUnit);

    @Override // java.time.temporal.TemporalAmount
    List<TemporalUnit> getUnits();

    Chronology getChronology();

    ChronoPeriod plus(TemporalAmount temporalAmount);

    ChronoPeriod minus(TemporalAmount temporalAmount);

    ChronoPeriod multipliedBy(int i2);

    ChronoPeriod normalized();

    @Override // java.time.temporal.TemporalAmount
    Temporal addTo(Temporal temporal);

    @Override // java.time.temporal.TemporalAmount
    Temporal subtractFrom(Temporal temporal);

    boolean equals(Object obj);

    int hashCode();

    String toString();

    static ChronoPeriod between(ChronoLocalDate chronoLocalDate, ChronoLocalDate chronoLocalDate2) {
        Objects.requireNonNull(chronoLocalDate, "startDateInclusive");
        Objects.requireNonNull(chronoLocalDate2, "endDateExclusive");
        return chronoLocalDate.until(chronoLocalDate2);
    }

    default boolean isZero() {
        Iterator<TemporalUnit> it = getUnits().iterator();
        while (it.hasNext()) {
            if (get(it.next()) != 0) {
                return false;
            }
        }
        return true;
    }

    default boolean isNegative() {
        Iterator<TemporalUnit> it = getUnits().iterator();
        while (it.hasNext()) {
            if (get(it.next()) < 0) {
                return true;
            }
        }
        return false;
    }

    default ChronoPeriod negated() {
        return multipliedBy(-1);
    }
}
