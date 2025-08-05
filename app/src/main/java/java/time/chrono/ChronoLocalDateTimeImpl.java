package java.time.chrono;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/ChronoLocalDateTimeImpl.class */
final class ChronoLocalDateTimeImpl<D extends ChronoLocalDate> implements ChronoLocalDateTime<D>, Temporal, TemporalAdjuster, Serializable {
    private static final long serialVersionUID = 4556003607393004514L;
    static final int HOURS_PER_DAY = 24;
    static final int MINUTES_PER_HOUR = 60;
    static final int MINUTES_PER_DAY = 1440;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = 3600;
    static final int SECONDS_PER_DAY = 86400;
    static final long MILLIS_PER_DAY = 86400000;
    static final long MICROS_PER_DAY = 86400000000L;
    static final long NANOS_PER_SECOND = 1000000000;
    static final long NANOS_PER_MINUTE = 60000000000L;
    static final long NANOS_PER_HOUR = 3600000000000L;
    static final long NANOS_PER_DAY = 86400000000000L;
    private final transient D date;
    private final transient LocalTime time;

    static <R extends ChronoLocalDate> ChronoLocalDateTimeImpl<R> of(R r2, LocalTime localTime) {
        return new ChronoLocalDateTimeImpl<>(r2, localTime);
    }

    static <R extends ChronoLocalDate> ChronoLocalDateTimeImpl<R> ensureValid(Chronology chronology, Temporal temporal) {
        ChronoLocalDateTimeImpl<R> chronoLocalDateTimeImpl = (ChronoLocalDateTimeImpl) temporal;
        if (!chronology.equals(chronoLocalDateTimeImpl.getChronology())) {
            throw new ClassCastException("Chronology mismatch, required: " + chronology.getId() + ", actual: " + chronoLocalDateTimeImpl.getChronology().getId());
        }
        return chronoLocalDateTimeImpl;
    }

    private ChronoLocalDateTimeImpl(D d2, LocalTime localTime) {
        Objects.requireNonNull(d2, "date");
        Objects.requireNonNull(localTime, SchemaSymbols.ATTVAL_TIME);
        this.date = d2;
        this.time = localTime;
    }

    private ChronoLocalDateTimeImpl<D> with(Temporal temporal, LocalTime localTime) {
        if (this.date == temporal && this.time == localTime) {
            return this;
        }
        return new ChronoLocalDateTimeImpl<>(ChronoLocalDateImpl.ensureValid(this.date.getChronology(), temporal), localTime);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public D toLocalDate() {
        return this.date;
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public LocalTime toLocalTime() {
        return this.time;
    }

    @Override // java.time.chrono.ChronoLocalDateTime, java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField != null && temporalField.isSupportedBy(this);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        return chronoField.isDateBased() || chronoField.isTimeBased();
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField) temporalField).isTimeBased() ? this.time.range(temporalField) : this.date.range(temporalField);
        }
        return temporalField.rangeRefinedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField) temporalField).isTimeBased() ? this.time.get(temporalField) : this.date.get(temporalField);
        }
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField) temporalField).isTimeBased() ? this.time.getLong(temporalField) : this.date.getLong(temporalField);
        }
        return temporalField.getFrom(this);
    }

    @Override // java.time.chrono.ChronoLocalDateTime, java.time.temporal.Temporal
    public ChronoLocalDateTimeImpl<D> with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof ChronoLocalDate) {
            return with((ChronoLocalDate) temporalAdjuster, this.time);
        }
        if (temporalAdjuster instanceof LocalTime) {
            return with(this.date, (LocalTime) temporalAdjuster);
        }
        if (temporalAdjuster instanceof ChronoLocalDateTimeImpl) {
            return ensureValid(this.date.getChronology(), (ChronoLocalDateTimeImpl) temporalAdjuster);
        }
        return ensureValid(this.date.getChronology(), (ChronoLocalDateTimeImpl) temporalAdjuster.adjustInto(this));
    }

    @Override // java.time.chrono.ChronoLocalDateTime, java.time.temporal.Temporal
    public ChronoLocalDateTimeImpl<D> with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            if (((ChronoField) temporalField).isTimeBased()) {
                return with(this.date, this.time.with(temporalField, j2));
            }
            return with(this.date.with(temporalField, j2), this.time);
        }
        return ensureValid(this.date.getChronology(), temporalField.adjustInto(this, j2));
    }

    @Override // java.time.chrono.ChronoLocalDateTime, java.time.temporal.Temporal
    public ChronoLocalDateTimeImpl<D> plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return plusNanos(j2);
                case MICROS:
                    return plusDays(j2 / MICROS_PER_DAY).plusNanos((j2 % MICROS_PER_DAY) * 1000);
                case MILLIS:
                    return plusDays(j2 / 86400000).plusNanos((j2 % 86400000) * 1000000);
                case SECONDS:
                    return plusSeconds(j2);
                case MINUTES:
                    return plusMinutes(j2);
                case HOURS:
                    return plusHours(j2);
                case HALF_DAYS:
                    return plusDays(j2 / 256).plusHours((j2 % 256) * 12);
                default:
                    return with(this.date.plus(j2, temporalUnit), this.time);
            }
        }
        return ensureValid(this.date.getChronology(), temporalUnit.addTo(this, j2));
    }

    private ChronoLocalDateTimeImpl<D> plusDays(long j2) {
        return with(this.date.plus(j2, ChronoUnit.DAYS), this.time);
    }

    private ChronoLocalDateTimeImpl<D> plusHours(long j2) {
        return plusWithOverflow(this.date, j2, 0L, 0L, 0L);
    }

    private ChronoLocalDateTimeImpl<D> plusMinutes(long j2) {
        return plusWithOverflow(this.date, 0L, j2, 0L, 0L);
    }

    ChronoLocalDateTimeImpl<D> plusSeconds(long j2) {
        return plusWithOverflow(this.date, 0L, 0L, j2, 0L);
    }

    private ChronoLocalDateTimeImpl<D> plusNanos(long j2) {
        return plusWithOverflow(this.date, 0L, 0L, 0L, j2);
    }

    private ChronoLocalDateTimeImpl<D> plusWithOverflow(D d2, long j2, long j3, long j4, long j5) {
        if ((j2 | j3 | j4 | j5) == 0) {
            return with(d2, this.time);
        }
        long j6 = (j5 / NANOS_PER_DAY) + (j4 / 86400) + (j3 / 1440) + (j2 / 24);
        long j7 = (j5 % NANOS_PER_DAY) + ((j4 % 86400) * 1000000000) + ((j3 % 1440) * NANOS_PER_MINUTE) + ((j2 % 24) * NANOS_PER_HOUR);
        long nanoOfDay = this.time.toNanoOfDay();
        long j8 = j7 + nanoOfDay;
        long jFloorDiv = j6 + Math.floorDiv(j8, NANOS_PER_DAY);
        long jFloorMod = Math.floorMod(j8, NANOS_PER_DAY);
        return with(d2.plus(jFloorDiv, ChronoUnit.DAYS), jFloorMod == nanoOfDay ? this.time : LocalTime.ofNanoOfDay(jFloorMod));
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public ChronoZonedDateTime<D> atZone(ZoneId zoneId) {
        return ChronoZonedDateTimeImpl.ofBest(this, zoneId, null);
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        ChronoLocalDateTime<? extends ChronoLocalDate> chronoLocalDateTimeLocalDateTime = getChronology().localDateTime(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            if (temporalUnit.isTimeBased()) {
                long jMultiplyExact = chronoLocalDateTimeLocalDateTime.getLong(ChronoField.EPOCH_DAY) - this.date.getLong(ChronoField.EPOCH_DAY);
                switch ((ChronoUnit) temporalUnit) {
                    case NANOS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, NANOS_PER_DAY);
                        break;
                    case MICROS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, MICROS_PER_DAY);
                        break;
                    case MILLIS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 86400000L);
                        break;
                    case SECONDS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 86400L);
                        break;
                    case MINUTES:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 1440L);
                        break;
                    case HOURS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 24L);
                        break;
                    case HALF_DAYS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 2L);
                        break;
                }
                return Math.addExact(jMultiplyExact, this.time.until(chronoLocalDateTimeLocalDateTime.toLocalTime(), temporalUnit));
            }
            ChronoLocalDate localDate = chronoLocalDateTimeLocalDateTime.toLocalDate();
            if (chronoLocalDateTimeLocalDateTime.toLocalTime().isBefore(this.time)) {
                localDate = localDate.minus(1L, (TemporalUnit) ChronoUnit.DAYS);
            }
            return this.date.until(localDate, temporalUnit);
        }
        Objects.requireNonNull(temporalUnit, "unit");
        return temporalUnit.between(this, chronoLocalDateTimeLocalDateTime);
    }

    private Object writeReplace() {
        return new Ser((byte) 2, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.date);
        objectOutput.writeObject(this.time);
    }

    static ChronoLocalDateTime<?> readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((ChronoLocalDate) objectInput.readObject()).atTime((LocalTime) objectInput.readObject());
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ChronoLocalDateTime) && compareTo((ChronoLocalDateTime<?>) obj) == 0;
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public int hashCode() {
        return toLocalDate().hashCode() ^ toLocalTime().hashCode();
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public String toString() {
        return toLocalDate().toString() + 'T' + toLocalTime().toString();
    }
}
