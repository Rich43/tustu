package java.time.temporal;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import java.time.Duration;

/* loaded from: rt.jar:java/time/temporal/ChronoUnit.class */
public enum ChronoUnit implements TemporalUnit {
    NANOS("Nanos", Duration.ofNanos(1)),
    MICROS("Micros", Duration.ofNanos(1000)),
    MILLIS("Millis", Duration.ofNanos(1000000)),
    SECONDS("Seconds", Duration.ofSeconds(1)),
    MINUTES("Minutes", Duration.ofSeconds(60)),
    HOURS("Hours", Duration.ofSeconds(3600)),
    HALF_DAYS("HalfDays", Duration.ofSeconds(43200)),
    DAYS("Days", Duration.ofSeconds(86400)),
    WEEKS("Weeks", Duration.ofSeconds(604800)),
    MONTHS("Months", Duration.ofSeconds(2629746)),
    YEARS("Years", Duration.ofSeconds(31556952)),
    DECADES("Decades", Duration.ofSeconds(315569520)),
    CENTURIES("Centuries", Duration.ofSeconds(3155695200L)),
    MILLENNIA("Millennia", Duration.ofSeconds(Sync.ONE_YEAR)),
    ERAS("Eras", Duration.ofSeconds(31556952000000000L)),
    FOREVER("Forever", Duration.ofSeconds(Long.MAX_VALUE, 999999999));

    private final String name;
    private final Duration duration;

    ChronoUnit(String str, Duration duration) {
        this.name = str;
        this.duration = duration;
    }

    @Override // java.time.temporal.TemporalUnit
    public Duration getDuration() {
        return this.duration;
    }

    @Override // java.time.temporal.TemporalUnit
    public boolean isDurationEstimated() {
        return compareTo(DAYS) >= 0;
    }

    @Override // java.time.temporal.TemporalUnit
    public boolean isDateBased() {
        return compareTo(DAYS) >= 0 && this != FOREVER;
    }

    @Override // java.time.temporal.TemporalUnit
    public boolean isTimeBased() {
        return compareTo(DAYS) < 0;
    }

    @Override // java.time.temporal.TemporalUnit
    public boolean isSupportedBy(Temporal temporal) {
        return temporal.isSupported(this);
    }

    @Override // java.time.temporal.TemporalUnit
    public <R extends Temporal> R addTo(R r2, long j2) {
        return (R) r2.plus(j2, this);
    }

    @Override // java.time.temporal.TemporalUnit
    public long between(Temporal temporal, Temporal temporal2) {
        return temporal.until(temporal2, this);
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }
}
