package java.time;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.chrono.ChronoLocalDateTime;
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
import java.time.temporal.ValueRange;
import java.util.Objects;

/* loaded from: rt.jar:java/time/LocalDateTime.class */
public final class LocalDateTime implements Temporal, TemporalAdjuster, ChronoLocalDateTime<LocalDate>, Serializable {
    public static final LocalDateTime MIN = of(LocalDate.MIN, LocalTime.MIN);
    public static final LocalDateTime MAX = of(LocalDate.MAX, LocalTime.MAX);
    private static final long serialVersionUID = 6207766400415563566L;
    private final LocalDate date;
    private final LocalTime time;

    public static LocalDateTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalDateTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalDateTime now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), clock.getZone().getRules().getOffset(instant));
    }

    public static LocalDateTime of(int i2, Month month, int i3, int i4, int i5) {
        return new LocalDateTime(LocalDate.of(i2, month, i3), LocalTime.of(i4, i5));
    }

    public static LocalDateTime of(int i2, Month month, int i3, int i4, int i5, int i6) {
        return new LocalDateTime(LocalDate.of(i2, month, i3), LocalTime.of(i4, i5, i6));
    }

    public static LocalDateTime of(int i2, Month month, int i3, int i4, int i5, int i6, int i7) {
        return new LocalDateTime(LocalDate.of(i2, month, i3), LocalTime.of(i4, i5, i6, i7));
    }

    public static LocalDateTime of(int i2, int i3, int i4, int i5, int i6) {
        return new LocalDateTime(LocalDate.of(i2, i3, i4), LocalTime.of(i5, i6));
    }

    public static LocalDateTime of(int i2, int i3, int i4, int i5, int i6, int i7) {
        return new LocalDateTime(LocalDate.of(i2, i3, i4), LocalTime.of(i5, i6, i7));
    }

    public static LocalDateTime of(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        return new LocalDateTime(LocalDate.of(i2, i3, i4), LocalTime.of(i5, i6, i7, i8));
    }

    public static LocalDateTime of(LocalDate localDate, LocalTime localTime) {
        Objects.requireNonNull(localDate, "date");
        Objects.requireNonNull(localTime, SchemaSymbols.ATTVAL_TIME);
        return new LocalDateTime(localDate, localTime);
    }

    public static LocalDateTime ofInstant(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), zoneId.getRules().getOffset(instant));
    }

    public static LocalDateTime ofEpochSecond(long j2, int i2, ZoneOffset zoneOffset) {
        Objects.requireNonNull(zoneOffset, "offset");
        ChronoField.NANO_OF_SECOND.checkValidValue(i2);
        return new LocalDateTime(LocalDate.ofEpochDay(Math.floorDiv(j2 + zoneOffset.getTotalSeconds(), 86400L)), LocalTime.ofNanoOfDay((((int) Math.floorMod(r0, 86400L)) * NativeMediaPlayer.ONE_SECOND) + i2));
    }

    public static LocalDateTime from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof LocalDateTime) {
            return (LocalDateTime) temporalAccessor;
        }
        if (temporalAccessor instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporalAccessor).toLocalDateTime();
        }
        if (temporalAccessor instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporalAccessor).toLocalDateTime();
        }
        try {
            return new LocalDateTime(LocalDate.from(temporalAccessor), LocalTime.from(temporalAccessor));
        } catch (DateTimeException e2) {
            throw new DateTimeException("Unable to obtain LocalDateTime from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName(), e2);
        }
    }

    public static LocalDateTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalDateTime) dateTimeFormatter.parse(charSequence, LocalDateTime::from);
    }

    private LocalDateTime(LocalDate localDate, LocalTime localTime) {
        this.date = localDate;
        this.time = localTime;
    }

    private LocalDateTime with(LocalDate localDate, LocalTime localTime) {
        if (this.date == localDate && this.time == localTime) {
            return this;
        }
        return new LocalDateTime(localDate, localTime);
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        if (!(temporalField instanceof ChronoField)) {
            return temporalField != null && temporalField.isSupportedBy(this);
        }
        ChronoField chronoField = (ChronoField) temporalField;
        return chronoField.isDateBased() || chronoField.isTimeBased();
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        return super.isSupported(temporalUnit);
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
        return super.get(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            return ((ChronoField) temporalField).isTimeBased() ? this.time.getLong(temporalField) : this.date.getLong(temporalField);
        }
        return temporalField.getFrom(this);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public LocalDate toLocalDate() {
        return this.date;
    }

    public int getYear() {
        return this.date.getYear();
    }

    public int getMonthValue() {
        return this.date.getMonthValue();
    }

    public Month getMonth() {
        return this.date.getMonth();
    }

    public int getDayOfMonth() {
        return this.date.getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.date.getDayOfYear();
    }

    public DayOfWeek getDayOfWeek() {
        return this.date.getDayOfWeek();
    }

    @Override // java.time.chrono.ChronoLocalDateTime
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
    public LocalDateTime with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return with((LocalDate) temporalAdjuster, this.time);
        }
        if (temporalAdjuster instanceof LocalTime) {
            return with(this.date, (LocalTime) temporalAdjuster);
        }
        if (temporalAdjuster instanceof LocalDateTime) {
            return (LocalDateTime) temporalAdjuster;
        }
        return (LocalDateTime) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalDateTime with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            if (((ChronoField) temporalField).isTimeBased()) {
                return with(this.date, this.time.with(temporalField, j2));
            }
            return with(this.date.with(temporalField, j2), this.time);
        }
        return (LocalDateTime) temporalField.adjustInto(this, j2);
    }

    public LocalDateTime withYear(int i2) {
        return with(this.date.withYear(i2), this.time);
    }

    public LocalDateTime withMonth(int i2) {
        return with(this.date.withMonth(i2), this.time);
    }

    public LocalDateTime withDayOfMonth(int i2) {
        return with(this.date.withDayOfMonth(i2), this.time);
    }

    public LocalDateTime withDayOfYear(int i2) {
        return with(this.date.withDayOfYear(i2), this.time);
    }

    public LocalDateTime withHour(int i2) {
        return with(this.date, this.time.withHour(i2));
    }

    public LocalDateTime withMinute(int i2) {
        return with(this.date, this.time.withMinute(i2));
    }

    public LocalDateTime withSecond(int i2) {
        return with(this.date, this.time.withSecond(i2));
    }

    public LocalDateTime withNano(int i2) {
        return with(this.date, this.time.withNano(i2));
    }

    public LocalDateTime truncatedTo(TemporalUnit temporalUnit) {
        return with(this.date, this.time.truncatedTo(temporalUnit));
    }

    @Override // java.time.temporal.Temporal
    public LocalDateTime plus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return with(this.date.plus(temporalAmount), this.time);
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (LocalDateTime) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalDateTime plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:
                    return plusNanos(j2);
                case MICROS:
                    return plusDays(j2 / 86400000000L).plusNanos((j2 % 86400000000L) * 1000);
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
        return (LocalDateTime) temporalUnit.addTo(this, j2);
    }

    public LocalDateTime plusYears(long j2) {
        return with(this.date.plusYears(j2), this.time);
    }

    public LocalDateTime plusMonths(long j2) {
        return with(this.date.plusMonths(j2), this.time);
    }

    public LocalDateTime plusWeeks(long j2) {
        return with(this.date.plusWeeks(j2), this.time);
    }

    public LocalDateTime plusDays(long j2) {
        return with(this.date.plusDays(j2), this.time);
    }

    public LocalDateTime plusHours(long j2) {
        return plusWithOverflow(this.date, j2, 0L, 0L, 0L, 1);
    }

    public LocalDateTime plusMinutes(long j2) {
        return plusWithOverflow(this.date, 0L, j2, 0L, 0L, 1);
    }

    public LocalDateTime plusSeconds(long j2) {
        return plusWithOverflow(this.date, 0L, 0L, j2, 0L, 1);
    }

    public LocalDateTime plusNanos(long j2) {
        return plusWithOverflow(this.date, 0L, 0L, 0L, j2, 1);
    }

    @Override // java.time.temporal.Temporal
    public LocalDateTime minus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return with(this.date.minus(temporalAmount), this.time);
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (LocalDateTime) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalDateTime minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public LocalDateTime minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1L) : plusYears(-j2);
    }

    public LocalDateTime minusMonths(long j2) {
        return j2 == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1L) : plusMonths(-j2);
    }

    public LocalDateTime minusWeeks(long j2) {
        return j2 == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : plusWeeks(-j2);
    }

    public LocalDateTime minusDays(long j2) {
        return j2 == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1L) : plusDays(-j2);
    }

    public LocalDateTime minusHours(long j2) {
        return plusWithOverflow(this.date, j2, 0L, 0L, 0L, -1);
    }

    public LocalDateTime minusMinutes(long j2) {
        return plusWithOverflow(this.date, 0L, j2, 0L, 0L, -1);
    }

    public LocalDateTime minusSeconds(long j2) {
        return plusWithOverflow(this.date, 0L, 0L, j2, 0L, -1);
    }

    public LocalDateTime minusNanos(long j2) {
        return plusWithOverflow(this.date, 0L, 0L, 0L, j2, -1);
    }

    private LocalDateTime plusWithOverflow(LocalDate localDate, long j2, long j3, long j4, long j5, int i2) {
        if ((j2 | j3 | j4 | j5) == 0) {
            return with(localDate, this.time);
        }
        long j6 = (j5 % 86400000000000L) + ((j4 % 86400) * NativeMediaPlayer.ONE_SECOND) + ((j3 % 1440) * 60000000000L) + ((j2 % 24) * 3600000000000L);
        long nanoOfDay = this.time.toNanoOfDay();
        long j7 = (j6 * i2) + nanoOfDay;
        long jFloorDiv = (((j5 / 86400000000000L) + (j4 / 86400) + (j3 / 1440) + (j2 / 24)) * i2) + Math.floorDiv(j7, 86400000000000L);
        long jFloorMod = Math.floorMod(j7, 86400000000000L);
        return with(localDate.plusDays(jFloorDiv), jFloorMod == nanoOfDay ? this.time : LocalTime.ofNanoOfDay(jFloorMod));
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R) this.date;
        }
        return (R) super.query(temporalQuery);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return super.adjustInto(temporal);
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        long jMultiplyExact;
        long j2;
        LocalDateTime localDateTimeFrom = from((TemporalAccessor) temporal);
        if (temporalUnit instanceof ChronoUnit) {
            if (temporalUnit.isTimeBased()) {
                long jDaysUntil = this.date.daysUntil(localDateTimeFrom.date);
                if (jDaysUntil == 0) {
                    return this.time.until(localDateTimeFrom.time, temporalUnit);
                }
                long nanoOfDay = localDateTimeFrom.time.toNanoOfDay() - this.time.toNanoOfDay();
                if (jDaysUntil > 0) {
                    jMultiplyExact = jDaysUntil - 1;
                    j2 = nanoOfDay + 86400000000000L;
                } else {
                    jMultiplyExact = jDaysUntil + 1;
                    j2 = nanoOfDay - 86400000000000L;
                }
                switch ((ChronoUnit) temporalUnit) {
                    case NANOS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 86400000000000L);
                        break;
                    case MICROS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 86400000000L);
                        j2 /= 1000;
                        break;
                    case MILLIS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 86400000L);
                        j2 /= 1000000;
                        break;
                    case SECONDS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 86400L);
                        j2 /= NativeMediaPlayer.ONE_SECOND;
                        break;
                    case MINUTES:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 1440L);
                        j2 /= 60000000000L;
                        break;
                    case HOURS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 24L);
                        j2 /= 3600000000000L;
                        break;
                    case HALF_DAYS:
                        jMultiplyExact = Math.multiplyExact(jMultiplyExact, 2L);
                        j2 /= 43200000000000L;
                        break;
                }
                return Math.addExact(jMultiplyExact, j2);
            }
            LocalDate localDatePlusDays = localDateTimeFrom.date;
            if (localDatePlusDays.isAfter(this.date) && localDateTimeFrom.time.isBefore(this.time)) {
                localDatePlusDays = localDatePlusDays.minusDays(1L);
            } else if (localDatePlusDays.isBefore(this.date) && localDateTimeFrom.time.isAfter(this.time)) {
                localDatePlusDays = localDatePlusDays.plusDays(1L);
            }
            return this.date.until(localDatePlusDays, temporalUnit);
        }
        return temporalUnit.between(this, localDateTimeFrom);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public OffsetDateTime atOffset(ZoneOffset zoneOffset) {
        return OffsetDateTime.of(this, zoneOffset);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public ZonedDateTime atZone(ZoneId zoneId) {
        return ZonedDateTime.of(this, zoneId);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.time.chrono.ChronoLocalDateTime, java.lang.Comparable
    public int compareTo(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return compareTo0((LocalDateTime) chronoLocalDateTime);
        }
        return super.compareTo(chronoLocalDateTime);
    }

    private int compareTo0(LocalDateTime localDateTime) {
        int iCompareTo0 = this.date.compareTo0(localDateTime.toLocalDate());
        if (iCompareTo0 == 0) {
            iCompareTo0 = this.time.compareTo(localDateTime.toLocalTime());
        }
        return iCompareTo0;
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public boolean isAfter(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return compareTo0((LocalDateTime) chronoLocalDateTime) > 0;
        }
        return super.isAfter(chronoLocalDateTime);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public boolean isBefore(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return compareTo0((LocalDateTime) chronoLocalDateTime) < 0;
        }
        return super.isBefore(chronoLocalDateTime);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public boolean isEqual(ChronoLocalDateTime<?> chronoLocalDateTime) {
        if (chronoLocalDateTime instanceof LocalDateTime) {
            return compareTo0((LocalDateTime) chronoLocalDateTime) == 0;
        }
        return super.isEqual(chronoLocalDateTime);
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) obj;
            return this.date.equals(localDateTime.date) && this.time.equals(localDateTime.time);
        }
        return false;
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public int hashCode() {
        return this.date.hashCode() ^ this.time.hashCode();
    }

    @Override // java.time.chrono.ChronoLocalDateTime
    public String toString() {
        return this.date.toString() + 'T' + this.time.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 5, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        this.date.writeExternal(dataOutput);
        this.time.writeExternal(dataOutput);
    }

    static LocalDateTime readExternal(DataInput dataInput) throws IOException {
        return of(LocalDate.readExternal(dataInput), LocalTime.readExternal(dataInput));
    }
}
