package java.time;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Objects;

/* loaded from: rt.jar:java/time/OffsetTime.class */
public final class OffsetTime implements Temporal, TemporalAdjuster, Comparable<OffsetTime>, Serializable {
    public static final OffsetTime MIN = LocalTime.MIN.atOffset(ZoneOffset.MAX);
    public static final OffsetTime MAX = LocalTime.MAX.atOffset(ZoneOffset.MIN);
    private static final long serialVersionUID = 7264499704384272492L;
    private final LocalTime time;
    private final ZoneOffset offset;

    public static OffsetTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static OffsetTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static OffsetTime now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofInstant(instant, clock.getZone().getRules().getOffset(instant));
    }

    public static OffsetTime of(LocalTime localTime, ZoneOffset zoneOffset) {
        return new OffsetTime(localTime, zoneOffset);
    }

    public static OffsetTime of(int i2, int i3, int i4, int i5, ZoneOffset zoneOffset) {
        return new OffsetTime(LocalTime.of(i2, i3, i4, i5), zoneOffset);
    }

    public static OffsetTime ofInstant(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return new OffsetTime(LocalTime.ofNanoOfDay((((int) Math.floorMod(instant.getEpochSecond() + r0.getTotalSeconds(), 86400L)) * NativeMediaPlayer.ONE_SECOND) + instant.getNano()), zoneId.getRules().getOffset(instant));
    }

    public static OffsetTime from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof OffsetTime) {
            return (OffsetTime) temporalAccessor;
        }
        try {
            return new OffsetTime(LocalTime.from(temporalAccessor), ZoneOffset.from(temporalAccessor));
        } catch (DateTimeException e2) {
            throw new DateTimeException("Unable to obtain OffsetTime from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName(), e2);
        }
    }

    public static OffsetTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_OFFSET_TIME);
    }

    public static OffsetTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (OffsetTime) dateTimeFormatter.parse(charSequence, OffsetTime::from);
    }

    private OffsetTime(LocalTime localTime, ZoneOffset zoneOffset) {
        this.time = (LocalTime) Objects.requireNonNull(localTime, SchemaSymbols.ATTVAL_TIME);
        this.offset = (ZoneOffset) Objects.requireNonNull(zoneOffset, "offset");
    }

    private OffsetTime with(LocalTime localTime, ZoneOffset zoneOffset) {
        if (this.time == localTime && this.offset.equals(zoneOffset)) {
            return this;
        }
        return new OffsetTime(localTime, zoneOffset);
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return temporalField instanceof ChronoField ? temporalField.isTimeBased() || temporalField == ChronoField.OFFSET_SECONDS : temporalField != null && temporalField.isSupportedBy(this);
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return temporalUnit.isTimeBased();
        }
        return temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.OFFSET_SECONDS) {
                return temporalField.range();
            }
            return this.time.range(temporalField);
        }
        return temporalField.rangeRefinedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        return super.get(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.OFFSET_SECONDS) {
                return this.offset.getTotalSeconds();
            }
            return this.time.getLong(temporalField);
        }
        return temporalField.getFrom(this);
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public OffsetTime withOffsetSameLocal(ZoneOffset zoneOffset) {
        return (zoneOffset == null || !zoneOffset.equals(this.offset)) ? new OffsetTime(this.time, zoneOffset) : this;
    }

    public OffsetTime withOffsetSameInstant(ZoneOffset zoneOffset) {
        if (zoneOffset.equals(this.offset)) {
            return this;
        }
        return new OffsetTime(this.time.plusSeconds(zoneOffset.getTotalSeconds() - this.offset.getTotalSeconds()), zoneOffset);
    }

    public LocalTime toLocalTime() {
        return this.time;
    }

    public int getHour() {
        return this.time.getHour();
    }

    public int getMinute() {
        return this.time.getMinute();
    }

    public int getSecond() {
        return this.time.getSecond();
    }

    public int getNano() {
        return this.time.getNano();
    }

    @Override // java.time.temporal.Temporal
    public OffsetTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalTime) {
            return with((LocalTime) temporalAdjuster, this.offset);
        }
        if (temporalAdjuster instanceof ZoneOffset) {
            return with(this.time, (ZoneOffset) temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetTime) {
            return (OffsetTime) temporalAdjuster;
        }
        return (OffsetTime) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public OffsetTime with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.OFFSET_SECONDS) {
                return with(this.time, ZoneOffset.ofTotalSeconds(((ChronoField) temporalField).checkValidIntValue(j2)));
            }
            return with(this.time.with(temporalField, j2), this.offset);
        }
        return (OffsetTime) temporalField.adjustInto(this, j2);
    }

    public OffsetTime withHour(int i2) {
        return with(this.time.withHour(i2), this.offset);
    }

    public OffsetTime withMinute(int i2) {
        return with(this.time.withMinute(i2), this.offset);
    }

    public OffsetTime withSecond(int i2) {
        return with(this.time.withSecond(i2), this.offset);
    }

    public OffsetTime withNano(int i2) {
        return with(this.time.withNano(i2), this.offset);
    }

    public OffsetTime truncatedTo(TemporalUnit temporalUnit) {
        return with(this.time.truncatedTo(temporalUnit), this.offset);
    }

    @Override // java.time.temporal.Temporal
    public OffsetTime plus(TemporalAmount temporalAmount) {
        return (OffsetTime) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public OffsetTime plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with(this.time.plus(j2, temporalUnit), this.offset);
        }
        return (OffsetTime) temporalUnit.addTo(this, j2);
    }

    public OffsetTime plusHours(long j2) {
        return with(this.time.plusHours(j2), this.offset);
    }

    public OffsetTime plusMinutes(long j2) {
        return with(this.time.plusMinutes(j2), this.offset);
    }

    public OffsetTime plusSeconds(long j2) {
        return with(this.time.plusSeconds(j2), this.offset);
    }

    public OffsetTime plusNanos(long j2) {
        return with(this.time.plusNanos(j2), this.offset);
    }

    @Override // java.time.temporal.Temporal
    public OffsetTime minus(TemporalAmount temporalAmount) {
        return (OffsetTime) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public OffsetTime minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public OffsetTime minusHours(long j2) {
        return with(this.time.minusHours(j2), this.offset);
    }

    public OffsetTime minusMinutes(long j2) {
        return with(this.time.minusMinutes(j2), this.offset);
    }

    public OffsetTime minusSeconds(long j2) {
        return with(this.time.minusSeconds(j2), this.offset);
    }

    public OffsetTime minusNanos(long j2) {
        return with(this.time.minusNanos(j2), this.offset);
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) {
            return (R) this.offset;
        }
        if (((temporalQuery == TemporalQueries.zoneId()) | (temporalQuery == TemporalQueries.chronology())) || temporalQuery == TemporalQueries.localDate()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R) this.time;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.NANOS;
        }
        return temporalQuery.queryFrom(this);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.NANO_OF_DAY, this.time.toNanoOfDay()).with(ChronoField.OFFSET_SECONDS, this.offset.getTotalSeconds());
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        OffsetTime offsetTimeFrom = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            long epochNano = offsetTimeFrom.toEpochNano() - toEpochNano();
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return epochNano;
                case MICROS:
                    return epochNano / 1000;
                case MILLIS:
                    return epochNano / 1000000;
                case SECONDS:
                    return epochNano / NativeMediaPlayer.ONE_SECOND;
                case MINUTES:
                    return epochNano / 60000000000L;
                case HOURS:
                    return epochNano / 3600000000000L;
                case HALF_DAYS:
                    return epochNano / 43200000000000L;
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return temporalUnit.between(this, offsetTimeFrom);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public OffsetDateTime atDate(LocalDate localDate) {
        return OffsetDateTime.of(localDate, this.time, this.offset);
    }

    private long toEpochNano() {
        return this.time.toNanoOfDay() - (this.offset.getTotalSeconds() * NativeMediaPlayer.ONE_SECOND);
    }

    @Override // java.lang.Comparable
    public int compareTo(OffsetTime offsetTime) {
        if (this.offset.equals(offsetTime.offset)) {
            return this.time.compareTo(offsetTime.time);
        }
        int iCompare = Long.compare(toEpochNano(), offsetTime.toEpochNano());
        if (iCompare == 0) {
            iCompare = this.time.compareTo(offsetTime.time);
        }
        return iCompare;
    }

    public boolean isAfter(OffsetTime offsetTime) {
        return toEpochNano() > offsetTime.toEpochNano();
    }

    public boolean isBefore(OffsetTime offsetTime) {
        return toEpochNano() < offsetTime.toEpochNano();
    }

    public boolean isEqual(OffsetTime offsetTime) {
        return toEpochNano() == offsetTime.toEpochNano();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof OffsetTime) {
            OffsetTime offsetTime = (OffsetTime) obj;
            return this.time.equals(offsetTime.time) && this.offset.equals(offsetTime.offset);
        }
        return false;
    }

    public int hashCode() {
        return this.time.hashCode() ^ this.offset.hashCode();
    }

    public String toString() {
        return this.time.toString() + this.offset.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 9, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        this.time.writeExternal(objectOutput);
        this.offset.writeExternal(objectOutput);
    }

    static OffsetTime readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return of(LocalTime.readExternal(objectInput), ZoneOffset.readExternal(objectInput));
    }
}
