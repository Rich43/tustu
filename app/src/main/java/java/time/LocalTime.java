package java.time;

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
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/time/LocalTime.class */
public final class LocalTime implements Temporal, TemporalAdjuster, Comparable<LocalTime>, Serializable {
    public static final LocalTime MIN;
    public static final LocalTime MAX;
    public static final LocalTime MIDNIGHT;
    public static final LocalTime NOON;
    private static final LocalTime[] HOURS = new LocalTime[24];
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
    private static final long serialVersionUID = 6414437269572265201L;
    private final byte hour;
    private final byte minute;
    private final byte second;
    private final int nano;

    static {
        for (int i2 = 0; i2 < HOURS.length; i2++) {
            HOURS[i2] = new LocalTime(i2, 0, 0, 0);
        }
        MIDNIGHT = HOURS[0];
        NOON = HOURS[12];
        MIN = HOURS[0];
        MAX = new LocalTime(23, 59, 59, Year.MAX_VALUE);
    }

    public static LocalTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalTime now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofNanoOfDay((((int) Math.floorMod(instant.getEpochSecond() + clock.getZone().getRules().getOffset(instant).getTotalSeconds(), 86400L)) * 1000000000) + instant.getNano());
    }

    public static LocalTime of(int i2, int i3) {
        ChronoField.HOUR_OF_DAY.checkValidValue(i2);
        if (i3 == 0) {
            return HOURS[i2];
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue(i3);
        return new LocalTime(i2, i3, 0, 0);
    }

    public static LocalTime of(int i2, int i3, int i4) {
        ChronoField.HOUR_OF_DAY.checkValidValue(i2);
        if ((i3 | i4) == 0) {
            return HOURS[i2];
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue(i3);
        ChronoField.SECOND_OF_MINUTE.checkValidValue(i4);
        return new LocalTime(i2, i3, i4, 0);
    }

    public static LocalTime of(int i2, int i3, int i4, int i5) {
        ChronoField.HOUR_OF_DAY.checkValidValue(i2);
        ChronoField.MINUTE_OF_HOUR.checkValidValue(i3);
        ChronoField.SECOND_OF_MINUTE.checkValidValue(i4);
        ChronoField.NANO_OF_SECOND.checkValidValue(i5);
        return create(i2, i3, i4, i5);
    }

    public static LocalTime ofSecondOfDay(long j2) {
        ChronoField.SECOND_OF_DAY.checkValidValue(j2);
        long j3 = j2 - (r0 * SECONDS_PER_HOUR);
        return create((int) (j2 / 3600), (int) (j3 / 60), (int) (j3 - (r0 * 60)), 0);
    }

    public static LocalTime ofNanoOfDay(long j2) {
        ChronoField.NANO_OF_DAY.checkValidValue(j2);
        int i2 = (int) (j2 / NANOS_PER_HOUR);
        long j3 = j2 - (i2 * NANOS_PER_HOUR);
        int i3 = (int) (j3 / NANOS_PER_MINUTE);
        long j4 = j3 - (i3 * NANOS_PER_MINUTE);
        int i4 = (int) (j4 / 1000000000);
        return create(i2, i3, i4, (int) (j4 - (i4 * 1000000000)));
    }

    public static LocalTime from(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        LocalTime localTime = (LocalTime) temporalAccessor.query(TemporalQueries.localTime());
        if (localTime == null) {
            throw new DateTimeException("Unable to obtain LocalTime from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName());
        }
        return localTime;
    }

    public static LocalTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static LocalTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalTime) dateTimeFormatter.parse(charSequence, LocalTime::from);
    }

    private static LocalTime create(int i2, int i3, int i4, int i5) {
        if ((i3 | i4 | i5) == 0) {
            return HOURS[i2];
        }
        return new LocalTime(i2, i3, i4, i5);
    }

    private LocalTime(int i2, int i3, int i4, int i5) {
        this.hour = (byte) i2;
        this.minute = (byte) i3;
        this.second = (byte) i4;
        this.nano = i5;
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return temporalField.isTimeBased();
        }
        return temporalField != null && temporalField.isSupportedBy(this);
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
        return super.range(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return get0(temporalField);
        }
        return super.get(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.NANO_OF_DAY) {
                return toNanoOfDay();
            }
            if (temporalField == ChronoField.MICRO_OF_DAY) {
                return toNanoOfDay() / 1000;
            }
            return get0(temporalField);
        }
        return temporalField.getFrom(this);
    }

    private int get0(TemporalField temporalField) {
        switch ((ChronoField) temporalField) {
            case NANO_OF_SECOND:
                return this.nano;
            case NANO_OF_DAY:
                throw new UnsupportedTemporalTypeException("Invalid field 'NanoOfDay' for get() method, use getLong() instead");
            case MICRO_OF_SECOND:
                return this.nano / 1000;
            case MICRO_OF_DAY:
                throw new UnsupportedTemporalTypeException("Invalid field 'MicroOfDay' for get() method, use getLong() instead");
            case MILLI_OF_SECOND:
                return this.nano / 1000000;
            case MILLI_OF_DAY:
                return (int) (toNanoOfDay() / 1000000);
            case SECOND_OF_MINUTE:
                return this.second;
            case SECOND_OF_DAY:
                return toSecondOfDay();
            case MINUTE_OF_HOUR:
                return this.minute;
            case MINUTE_OF_DAY:
                return (this.hour * 60) + this.minute;
            case HOUR_OF_AMPM:
                return this.hour % 12;
            case CLOCK_HOUR_OF_AMPM:
                int i2 = this.hour % 12;
                if (i2 % 12 == 0) {
                    return 12;
                }
                return i2;
            case HOUR_OF_DAY:
                return this.hour;
            case CLOCK_HOUR_OF_DAY:
                if (this.hour == 0) {
                    return 24;
                }
                return this.hour;
            case AMPM_OF_DAY:
                return this.hour / 12;
            default:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }

    public int getNano() {
        return this.nano;
    }

    @Override // java.time.temporal.Temporal
    public LocalTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalTime) {
            return (LocalTime) temporalAdjuster;
        }
        return (LocalTime) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalTime with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            chronoField.checkValidValue(j2);
            switch (chronoField) {
                case NANO_OF_SECOND:
                    return withNano((int) j2);
                case NANO_OF_DAY:
                    return ofNanoOfDay(j2);
                case MICRO_OF_SECOND:
                    return withNano(((int) j2) * 1000);
                case MICRO_OF_DAY:
                    return ofNanoOfDay(j2 * 1000);
                case MILLI_OF_SECOND:
                    return withNano(((int) j2) * 1000000);
                case MILLI_OF_DAY:
                    return ofNanoOfDay(j2 * 1000000);
                case SECOND_OF_MINUTE:
                    return withSecond((int) j2);
                case SECOND_OF_DAY:
                    return plusSeconds(j2 - toSecondOfDay());
                case MINUTE_OF_HOUR:
                    return withMinute((int) j2);
                case MINUTE_OF_DAY:
                    return plusMinutes(j2 - ((this.hour * 60) + this.minute));
                case HOUR_OF_AMPM:
                    return plusHours(j2 - (this.hour % 12));
                case CLOCK_HOUR_OF_AMPM:
                    return plusHours((j2 == 12 ? 0L : j2) - (this.hour % 12));
                case HOUR_OF_DAY:
                    return withHour((int) j2);
                case CLOCK_HOUR_OF_DAY:
                    return withHour((int) (j2 == 24 ? 0L : j2));
                case AMPM_OF_DAY:
                    return plusHours((j2 - (this.hour / 12)) * 12);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return (LocalTime) temporalField.adjustInto(this, j2);
    }

    public LocalTime withHour(int i2) {
        if (this.hour == i2) {
            return this;
        }
        ChronoField.HOUR_OF_DAY.checkValidValue(i2);
        return create(i2, this.minute, this.second, this.nano);
    }

    public LocalTime withMinute(int i2) {
        if (this.minute == i2) {
            return this;
        }
        ChronoField.MINUTE_OF_HOUR.checkValidValue(i2);
        return create(this.hour, i2, this.second, this.nano);
    }

    public LocalTime withSecond(int i2) {
        if (this.second == i2) {
            return this;
        }
        ChronoField.SECOND_OF_MINUTE.checkValidValue(i2);
        return create(this.hour, this.minute, i2, this.nano);
    }

    public LocalTime withNano(int i2) {
        if (this.nano == i2) {
            return this;
        }
        ChronoField.NANO_OF_SECOND.checkValidValue(i2);
        return create(this.hour, this.minute, this.second, i2);
    }

    public LocalTime truncatedTo(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.NANOS) {
            return this;
        }
        Duration duration = temporalUnit.getDuration();
        if (duration.getSeconds() > 86400) {
            throw new UnsupportedTemporalTypeException("Unit is too large to be used for truncation");
        }
        long nanos = duration.toNanos();
        if (NANOS_PER_DAY % nanos != 0) {
            throw new UnsupportedTemporalTypeException("Unit must divide into a standard day without remainder");
        }
        return ofNanoOfDay((toNanoOfDay() / nanos) * nanos);
    }

    @Override // java.time.temporal.Temporal
    public LocalTime plus(TemporalAmount temporalAmount) {
        return (LocalTime) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalTime plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return plusNanos(j2);
                case MICROS:
                    return plusNanos((j2 % MICROS_PER_DAY) * 1000);
                case MILLIS:
                    return plusNanos((j2 % 86400000) * 1000000);
                case SECONDS:
                    return plusSeconds(j2);
                case MINUTES:
                    return plusMinutes(j2);
                case HOURS:
                    return plusHours(j2);
                case HALF_DAYS:
                    return plusHours((j2 % 2) * 12);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return (LocalTime) temporalUnit.addTo(this, j2);
    }

    public LocalTime plusHours(long j2) {
        if (j2 == 0) {
            return this;
        }
        return create(((((int) (j2 % 24)) + this.hour) + 24) % 24, this.minute, this.second, this.nano);
    }

    public LocalTime plusMinutes(long j2) {
        if (j2 == 0) {
            return this;
        }
        int i2 = (this.hour * 60) + this.minute;
        int i3 = ((((int) (j2 % 1440)) + i2) + MINUTES_PER_DAY) % MINUTES_PER_DAY;
        if (i2 == i3) {
            return this;
        }
        return create(i3 / 60, i3 % 60, this.second, this.nano);
    }

    public LocalTime plusSeconds(long j2) {
        if (j2 == 0) {
            return this;
        }
        int i2 = (this.hour * SECONDS_PER_HOUR) + (this.minute * 60) + this.second;
        int i3 = ((((int) (j2 % 86400)) + i2) + 86400) % 86400;
        if (i2 == i3) {
            return this;
        }
        return create(i3 / SECONDS_PER_HOUR, (i3 / 60) % 60, i3 % 60, this.nano);
    }

    public LocalTime plusNanos(long j2) {
        if (j2 == 0) {
            return this;
        }
        long nanoOfDay = toNanoOfDay();
        long j3 = (((j2 % NANOS_PER_DAY) + nanoOfDay) + NANOS_PER_DAY) % NANOS_PER_DAY;
        if (nanoOfDay == j3) {
            return this;
        }
        return create((int) (j3 / NANOS_PER_HOUR), (int) ((j3 / NANOS_PER_MINUTE) % 60), (int) ((j3 / 1000000000) % 60), (int) (j3 % 1000000000));
    }

    @Override // java.time.temporal.Temporal
    public LocalTime minus(TemporalAmount temporalAmount) {
        return (LocalTime) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalTime minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public LocalTime minusHours(long j2) {
        return plusHours(-(j2 % 24));
    }

    public LocalTime minusMinutes(long j2) {
        return plusMinutes(-(j2 % 1440));
    }

    public LocalTime minusSeconds(long j2) {
        return plusSeconds(-(j2 % 86400));
    }

    public LocalTime minusNanos(long j2) {
        return plusNanos(-(j2 % NANOS_PER_DAY));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology() || temporalQuery == TemporalQueries.zoneId() || temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.offset()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return this;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.NANOS;
        }
        return temporalQuery.queryFrom(this);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.NANO_OF_DAY, toNanoOfDay());
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        LocalTime localTimeFrom = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            long nanoOfDay = localTimeFrom.toNanoOfDay() - toNanoOfDay();
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return nanoOfDay;
                case MICROS:
                    return nanoOfDay / 1000;
                case MILLIS:
                    return nanoOfDay / 1000000;
                case SECONDS:
                    return nanoOfDay / 1000000000;
                case MINUTES:
                    return nanoOfDay / NANOS_PER_MINUTE;
                case HOURS:
                    return nanoOfDay / NANOS_PER_HOUR;
                case HALF_DAYS:
                    return nanoOfDay / 43200000000000L;
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return temporalUnit.between(this, localTimeFrom);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public LocalDateTime atDate(LocalDate localDate) {
        return LocalDateTime.of(localDate, this);
    }

    public OffsetTime atOffset(ZoneOffset zoneOffset) {
        return OffsetTime.of(this, zoneOffset);
    }

    public int toSecondOfDay() {
        return (this.hour * SECONDS_PER_HOUR) + (this.minute * 60) + this.second;
    }

    public long toNanoOfDay() {
        return (this.hour * NANOS_PER_HOUR) + (this.minute * NANOS_PER_MINUTE) + (this.second * 1000000000) + this.nano;
    }

    @Override // java.lang.Comparable
    public int compareTo(LocalTime localTime) {
        int iCompare = Integer.compare(this.hour, localTime.hour);
        if (iCompare == 0) {
            iCompare = Integer.compare(this.minute, localTime.minute);
            if (iCompare == 0) {
                iCompare = Integer.compare(this.second, localTime.second);
                if (iCompare == 0) {
                    iCompare = Integer.compare(this.nano, localTime.nano);
                }
            }
        }
        return iCompare;
    }

    public boolean isAfter(LocalTime localTime) {
        return compareTo(localTime) > 0;
    }

    public boolean isBefore(LocalTime localTime) {
        return compareTo(localTime) < 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LocalTime) {
            LocalTime localTime = (LocalTime) obj;
            return this.hour == localTime.hour && this.minute == localTime.minute && this.second == localTime.second && this.nano == localTime.nano;
        }
        return false;
    }

    public int hashCode() {
        long nanoOfDay = toNanoOfDay();
        return (int) (nanoOfDay ^ (nanoOfDay >>> 32));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(18);
        byte b2 = this.hour;
        byte b3 = this.minute;
        byte b4 = this.second;
        int i2 = this.nano;
        sb.append(b2 < 10 ? "0" : "").append((int) b2).append(b3 < 10 ? ":0" : CallSiteDescriptor.TOKEN_DELIMITER).append((int) b3);
        if (b4 > 0 || i2 > 0) {
            sb.append(b4 < 10 ? ":0" : CallSiteDescriptor.TOKEN_DELIMITER).append((int) b4);
            if (i2 > 0) {
                sb.append('.');
                if (i2 % 1000000 == 0) {
                    sb.append(Integer.toString((i2 / 1000000) + 1000).substring(1));
                } else if (i2 % 1000 == 0) {
                    sb.append(Integer.toString((i2 / 1000) + 1000000).substring(1));
                } else {
                    sb.append(Integer.toString(i2 + 1000000000).substring(1));
                }
            }
        }
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 4, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        if (this.nano == 0) {
            if (this.second == 0) {
                if (this.minute == 0) {
                    dataOutput.writeByte(this.hour ^ (-1));
                    return;
                } else {
                    dataOutput.writeByte(this.hour);
                    dataOutput.writeByte(this.minute ^ (-1));
                    return;
                }
            }
            dataOutput.writeByte(this.hour);
            dataOutput.writeByte(this.minute);
            dataOutput.writeByte(this.second ^ (-1));
            return;
        }
        dataOutput.writeByte(this.hour);
        dataOutput.writeByte(this.minute);
        dataOutput.writeByte(this.second);
        dataOutput.writeInt(this.nano);
    }

    static LocalTime readExternal(DataInput dataInput) throws IOException {
        int i2 = dataInput.readByte();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        if (i2 < 0) {
            i2 = (i2 ^ (-1)) == true ? 1 : 0;
        } else {
            i3 = dataInput.readByte();
            if (i3 < 0) {
                i3 = (i3 ^ (-1)) == true ? 1 : 0;
            } else {
                i4 = dataInput.readByte();
                if (i4 < 0) {
                    i4 = (i4 ^ (-1)) == true ? 1 : 0;
                } else {
                    i5 = dataInput.readInt();
                }
            }
        }
        return of(i2, i3, i4, i5);
    }
}
