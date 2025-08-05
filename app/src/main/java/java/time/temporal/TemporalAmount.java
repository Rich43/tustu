package java.time.temporal;

import java.util.List;

/* loaded from: rt.jar:java/time/temporal/TemporalAmount.class */
public interface TemporalAmount {
    long get(TemporalUnit temporalUnit);

    List<TemporalUnit> getUnits();

    Temporal addTo(Temporal temporal);

    Temporal subtractFrom(Temporal temporal);
}
