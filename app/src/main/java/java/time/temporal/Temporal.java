package java.time.temporal;

/* loaded from: rt.jar:java/time/temporal/Temporal.class */
public interface Temporal extends TemporalAccessor {
    boolean isSupported(TemporalUnit temporalUnit);

    Temporal with(TemporalField temporalField, long j2);

    Temporal plus(long j2, TemporalUnit temporalUnit);

    long until(Temporal temporal, TemporalUnit temporalUnit);

    default Temporal with(TemporalAdjuster temporalAdjuster) {
        return temporalAdjuster.adjustInto(this);
    }

    default Temporal plus(TemporalAmount temporalAmount) {
        return temporalAmount.addTo(this);
    }

    default Temporal minus(TemporalAmount temporalAmount) {
        return temporalAmount.subtractFrom(this);
    }

    default Temporal minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }
}
