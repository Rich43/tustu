package java.time;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
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

/* loaded from: rt.jar:java/time/Instant.class */
public final class Instant implements Temporal, TemporalAdjuster, Comparable<Instant>, Serializable {
    private static final long serialVersionUID = -665713676816604388L;
    private final long seconds;
    private final int nanos;
    public static final Instant EPOCH = new Instant(0, 0);
    private static final long MIN_SECOND = -31557014167219200L;
    public static final Instant MIN = ofEpochSecond(MIN_SECOND, 0);
    private static final long MAX_SECOND = 31556889864403199L;
    public static final Instant MAX = ofEpochSecond(MAX_SECOND, 999999999);

    public static Instant now() {
        return Clock.systemUTC().instant();
    }

    public static Instant now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return clock.instant();
    }

    public static Instant ofEpochSecond(long j2) {
        return create(j2, 0);
    }

    public static Instant ofEpochSecond(long j2, long j3) {
        return create(Math.addExact(j2, Math.floorDiv(j3, NativeMediaPlayer.ONE_SECOND)), (int) Math.floorMod(j3, NativeMediaPlayer.ONE_SECOND));
    }

    public static Instant ofEpochMilli(long j2) {
        return create(Math.floorDiv(j2, 1000L), ((int) Math.floorMod(j2, 1000L)) * 1000000);
    }

    public static Instant from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Instant) {
            return (Instant) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        try {
            return ofEpochSecond(temporalAccessor.getLong(ChronoField.INSTANT_SECONDS), temporalAccessor.get(ChronoField.NANO_OF_SECOND));
        } catch (DateTimeException e2) {
            throw new DateTimeException("Unable to obtain Instant from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName(), e2);
        }
    }

    public static Instant parse(CharSequence charSequence) {
        return (Instant) DateTimeFormatter.ISO_INSTANT.parse(charSequence, Instant::from);
    }

    private static Instant create(long j2, int i2) {
        if ((j2 | i2) == 0) {
            return EPOCH;
        }
        if (j2 < MIN_SECOND || j2 > MAX_SECOND) {
            throw new DateTimeException("Instant exceeds minimum or maximum instant");
        }
        return new Instant(j2, i2);
    }

    private Instant(long j2, int i2) {
        this.seconds = j2;
        this.nanos = i2;
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return temporalField instanceof ChronoField ? temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.NANO_OF_SECOND || temporalField == ChronoField.MICRO_OF_SECOND || temporalField == ChronoField.MILLI_OF_SECOND : temporalField != null && temporalField.isSupportedBy(this);
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        return temporalUnit instanceof ChronoUnit ? temporalUnit.isTimeBased() || temporalUnit == ChronoUnit.DAYS : temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        return super.range(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case NANO_OF_SECOND:
                    return this.nanos;
                case MICRO_OF_SECOND:
                    return this.nanos / 1000;
                case MILLI_OF_SECOND:
                    return this.nanos / 1000000;
                case INSTANT_SECONDS:
                    ChronoField.INSTANT_SECONDS.checkValidIntValue(this.seconds);
                    break;
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
        return range(temporalField).checkValidIntValue(temporalField.getFrom(this), temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case NANO_OF_SECOND:
                    return this.nanos;
                case MICRO_OF_SECOND:
                    return this.nanos / 1000;
                case MILLI_OF_SECOND:
                    return this.nanos / 1000000;
                case INSTANT_SECONDS:
                    return this.seconds;
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return temporalField.getFrom(this);
    }

    public long getEpochSecond() {
        return this.seconds;
    }

    public int getNano() {
        return this.nanos;
    }

    @Override // java.time.temporal.Temporal
    public Instant with(TemporalAdjuster temporalAdjuster) {
        return (Instant) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public Instant with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            chronoField.checkValidValue(j2);
            switch (chronoField) {
                case NANO_OF_SECOND:
                    return j2 != ((long) this.nanos) ? create(this.seconds, (int) j2) : this;
                case MICRO_OF_SECOND:
                    int i2 = ((int) j2) * 1000;
                    return i2 != this.nanos ? create(this.seconds, i2) : this;
                case MILLI_OF_SECOND:
                    int i3 = ((int) j2) * 1000000;
                    return i3 != this.nanos ? create(this.seconds, i3) : this;
                case INSTANT_SECONDS:
                    return j2 != this.seconds ? create(j2, this.nanos) : this;
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return (Instant) temporalField.adjustInto(this, j2);
    }

    public Instant truncatedTo(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.NANOS) {
            return this;
        }
        Duration duration = temporalUnit.getDuration();
        if (duration.getSeconds() > 86400) {
            throw new UnsupportedTemporalTypeException("Unit is too large to be used for truncation");
        }
        long nanos = duration.toNanos();
        if (86400000000000L % nanos != 0) {
            throw new UnsupportedTemporalTypeException("Unit must divide into a standard day without remainder");
        }
        long j2 = ((this.seconds % 86400) * NativeMediaPlayer.ONE_SECOND) + this.nanos;
        return plusNanos(((j2 / nanos) * nanos) - j2);
    }

    @Override // java.time.temporal.Temporal
    public Instant plus(TemporalAmount temporalAmount) {
        return (Instant) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public Instant plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return plusNanos(j2);
                case MICROS:
                    return plus(j2 / 1000000, (j2 % 1000000) * 1000);
                case MILLIS:
                    return plusMillis(j2);
                case SECONDS:
                    return plusSeconds(j2);
                case MINUTES:
                    return plusSeconds(Math.multiplyExact(j2, 60L));
                case HOURS:
                    return plusSeconds(Math.multiplyExact(j2, 3600L));
                case HALF_DAYS:
                    return plusSeconds(Math.multiplyExact(j2, 43200L));
                case DAYS:
                    return plusSeconds(Math.multiplyExact(j2, 86400L));
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return (Instant) temporalUnit.addTo(this, j2);
    }

    public Instant plusSeconds(long j2) {
        return plus(j2, 0L);
    }

    public Instant plusMillis(long j2) {
        return plus(j2 / 1000, (j2 % 1000) * 1000000);
    }

    public Instant plusNanos(long j2) {
        return plus(0L, j2);
    }

    private Instant plus(long j2, long j3) {
        if ((j2 | j3) == 0) {
            return this;
        }
        return ofEpochSecond(Math.addExact(Math.addExact(this.seconds, j2), j3 / NativeMediaPlayer.ONE_SECOND), this.nanos + (j3 % NativeMediaPlayer.ONE_SECOND));
    }

    @Override // java.time.temporal.Temporal
    public Instant minus(TemporalAmount temporalAmount) {
        return (Instant) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public Instant minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public Instant minusSeconds(long j2) {
        if (j2 == Long.MIN_VALUE) {
            return plusSeconds(Long.MAX_VALUE).plusSeconds(1L);
        }
        return plusSeconds(-j2);
    }

    public Instant minusMillis(long j2) {
        if (j2 == Long.MIN_VALUE) {
            return plusMillis(Long.MAX_VALUE).plusMillis(1L);
        }
        return plusMillis(-j2);
    }

    public Instant minusNanos(long j2) {
        if (j2 == Long.MIN_VALUE) {
            return plusNanos(Long.MAX_VALUE).plusNanos(1L);
        }
        return plusNanos(-j2);
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.NANOS;
        }
        if (temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.localDate() || temporalQuery == TemporalQueries.localTime()) {
            return null;
        }
        return temporalQuery.queryFrom(this);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.INSTANT_SECONDS, this.seconds).with(ChronoField.NANO_OF_SECOND, this.nanos);
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        Instant instantFrom = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return nanosUntil(instantFrom);
                case MICROS:
                    return nanosUntil(instantFrom) / 1000;
                case MILLIS:
                    return Math.subtractExact(instantFrom.toEpochMilli(), toEpochMilli());
                case SECONDS:
                    return secondsUntil(instantFrom);
                case MINUTES:
                    return secondsUntil(instantFrom) / 60;
                case HOURS:
                    return secondsUntil(instantFrom) / 3600;
                case HALF_DAYS:
                    return secondsUntil(instantFrom) / 43200;
                case DAYS:
                    return secondsUntil(instantFrom) / 86400;
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return temporalUnit.between(this, instantFrom);
    }

    private long nanosUntil(Instant instant) {
        return Math.addExact(Math.multiplyExact(Math.subtractExact(instant.seconds, this.seconds), NativeMediaPlayer.ONE_SECOND), instant.nanos - this.nanos);
    }

    private long secondsUntil(Instant instant) {
        long jSubtractExact = Math.subtractExact(instant.seconds, this.seconds);
        long j2 = instant.nanos - this.nanos;
        if (jSubtractExact > 0 && j2 < 0) {
            jSubtractExact--;
        } else if (jSubtractExact < 0 && j2 > 0) {
            jSubtractExact++;
        }
        return jSubtractExact;
    }

    public OffsetDateTime atOffset(ZoneOffset zoneOffset) {
        return OffsetDateTime.ofInstant(this, zoneOffset);
    }

    public ZonedDateTime atZone(ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this, zoneId);
    }

    public long toEpochMilli() {
        if (this.seconds < 0 && this.nanos > 0) {
            return Math.addExact(Math.multiplyExact(this.seconds + 1, 1000L), (this.nanos / 1000000) - 1000);
        }
        return Math.addExact(Math.multiplyExact(this.seconds, 1000L), this.nanos / 1000000);
    }

    @Override // java.lang.Comparable
    public int compareTo(Instant instant) {
        int iCompare = Long.compare(this.seconds, instant.seconds);
        if (iCompare != 0) {
            return iCompare;
        }
        return this.nanos - instant.nanos;
    }

    public boolean isAfter(Instant instant) {
        return compareTo(instant) > 0;
    }

    public boolean isBefore(Instant instant) {
        return compareTo(instant) < 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Instant) {
            Instant instant = (Instant) obj;
            return this.seconds == instant.seconds && this.nanos == instant.nanos;
        }
        return false;
    }

    public int hashCode() {
        return ((int) (this.seconds ^ (this.seconds >>> 32))) + (51 * this.nanos);
    }

    public String toString() {
        return DateTimeFormatter.ISO_INSTANT.format(this);
    }

    private Object writeReplace() {
        return new Ser((byte) 2, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.seconds);
        dataOutput.writeInt(this.nanos);
    }

    static Instant readExternal(DataInput dataInput) throws IOException {
        return ofEpochSecond(dataInput.readLong(), dataInput.readInt());
    }
}
