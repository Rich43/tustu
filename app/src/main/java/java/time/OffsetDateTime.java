package java.time;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.IsoChronology;
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
import java.util.Comparator;
import java.util.Objects;

/* loaded from: rt.jar:java/time/OffsetDateTime.class */
public final class OffsetDateTime implements Temporal, TemporalAdjuster, Comparable<OffsetDateTime>, Serializable {
    public static final OffsetDateTime MIN = LocalDateTime.MIN.atOffset(ZoneOffset.MAX);
    public static final OffsetDateTime MAX = LocalDateTime.MAX.atOffset(ZoneOffset.MIN);
    private static final long serialVersionUID = 2287754244819255394L;
    private final LocalDateTime dateTime;
    private final ZoneOffset offset;

    public static Comparator<OffsetDateTime> timeLineOrder() {
        return OffsetDateTime::compareInstant;
    }

    private static int compareInstant(OffsetDateTime offsetDateTime, OffsetDateTime offsetDateTime2) {
        if (offsetDateTime.getOffset().equals(offsetDateTime2.getOffset())) {
            return offsetDateTime.toLocalDateTime().compareTo((ChronoLocalDateTime<?>) offsetDateTime2.toLocalDateTime());
        }
        int iCompare = Long.compare(offsetDateTime.toEpochSecond(), offsetDateTime2.toEpochSecond());
        if (iCompare == 0) {
            iCompare = offsetDateTime.toLocalTime().getNano() - offsetDateTime2.toLocalTime().getNano();
        }
        return iCompare;
    }

    public static OffsetDateTime now() {
        return now(Clock.systemDefaultZone());
    }

    public static OffsetDateTime now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static OffsetDateTime now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        Instant instant = clock.instant();
        return ofInstant(instant, clock.getZone().getRules().getOffset(instant));
    }

    public static OffsetDateTime of(LocalDate localDate, LocalTime localTime, ZoneOffset zoneOffset) {
        return new OffsetDateTime(LocalDateTime.of(localDate, localTime), zoneOffset);
    }

    public static OffsetDateTime of(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        return new OffsetDateTime(localDateTime, zoneOffset);
    }

    public static OffsetDateTime of(int i2, int i3, int i4, int i5, int i6, int i7, int i8, ZoneOffset zoneOffset) {
        return new OffsetDateTime(LocalDateTime.of(i2, i3, i4, i5, i6, i7, i8), zoneOffset);
    }

    public static OffsetDateTime ofInstant(Instant instant, ZoneId zoneId) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zoneId, "zone");
        ZoneOffset offset = zoneId.getRules().getOffset(instant);
        return new OffsetDateTime(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset), offset);
    }

    public static OffsetDateTime from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof OffsetDateTime) {
            return (OffsetDateTime) temporalAccessor;
        }
        try {
            ZoneOffset zoneOffsetFrom = ZoneOffset.from(temporalAccessor);
            LocalDate localDate = (LocalDate) temporalAccessor.query(TemporalQueries.localDate());
            LocalTime localTime = (LocalTime) temporalAccessor.query(TemporalQueries.localTime());
            if (localDate != null && localTime != null) {
                return of(localDate, localTime, zoneOffsetFrom);
            }
            return ofInstant(Instant.from(temporalAccessor), zoneOffsetFrom);
        } catch (DateTimeException e2) {
            throw new DateTimeException("Unable to obtain OffsetDateTime from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName(), e2);
        }
    }

    public static OffsetDateTime parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static OffsetDateTime parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (OffsetDateTime) dateTimeFormatter.parse(charSequence, OffsetDateTime::from);
    }

    private OffsetDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        this.dateTime = (LocalDateTime) Objects.requireNonNull(localDateTime, SchemaSymbols.ATTVAL_DATETIME);
        this.offset = (ZoneOffset) Objects.requireNonNull(zoneOffset, "offset");
    }

    private OffsetDateTime with(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
        if (this.dateTime == localDateTime && this.offset.equals(zoneOffset)) {
            return this;
        }
        return new OffsetDateTime(localDateTime, zoneOffset);
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return (temporalField instanceof ChronoField) || (temporalField != null && temporalField.isSupportedBy(this));
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        return temporalUnit instanceof ChronoUnit ? temporalUnit != ChronoUnit.FOREVER : temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.OFFSET_SECONDS) {
                return temporalField.range();
            }
            return this.dateTime.range(temporalField);
        }
        return temporalField.rangeRefinedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case INSTANT_SECONDS:
                    throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
                case OFFSET_SECONDS:
                    return getOffset().getTotalSeconds();
                default:
                    return this.dateTime.get(temporalField);
            }
        }
        return super.get(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case INSTANT_SECONDS:
                    return toEpochSecond();
                case OFFSET_SECONDS:
                    return getOffset().getTotalSeconds();
                default:
                    return this.dateTime.getLong(temporalField);
            }
        }
        return temporalField.getFrom(this);
    }

    public ZoneOffset getOffset() {
        return this.offset;
    }

    public OffsetDateTime withOffsetSameLocal(ZoneOffset zoneOffset) {
        return with(this.dateTime, zoneOffset);
    }

    public OffsetDateTime withOffsetSameInstant(ZoneOffset zoneOffset) {
        if (zoneOffset.equals(this.offset)) {
            return this;
        }
        return new OffsetDateTime(this.dateTime.plusSeconds(zoneOffset.getTotalSeconds() - this.offset.getTotalSeconds()), zoneOffset);
    }

    public LocalDateTime toLocalDateTime() {
        return this.dateTime;
    }

    public LocalDate toLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public int getYear() {
        return this.dateTime.getYear();
    }

    public int getMonthValue() {
        return this.dateTime.getMonthValue();
    }

    public Month getMonth() {
        return this.dateTime.getMonth();
    }

    public int getDayOfMonth() {
        return this.dateTime.getDayOfMonth();
    }

    public int getDayOfYear() {
        return this.dateTime.getDayOfYear();
    }

    public DayOfWeek getDayOfWeek() {
        return this.dateTime.getDayOfWeek();
    }

    public LocalTime toLocalTime() {
        return this.dateTime.toLocalTime();
    }

    public int getHour() {
        return this.dateTime.getHour();
    }

    public int getMinute() {
        return this.dateTime.getMinute();
    }

    public int getSecond() {
        return this.dateTime.getSecond();
    }

    public int getNano() {
        return this.dateTime.getNano();
    }

    @Override // java.time.temporal.Temporal
    public OffsetDateTime with(TemporalAdjuster temporalAdjuster) {
        if ((temporalAdjuster instanceof LocalDate) || (temporalAdjuster instanceof LocalTime) || (temporalAdjuster instanceof LocalDateTime)) {
            return with(this.dateTime.with(temporalAdjuster), this.offset);
        }
        if (temporalAdjuster instanceof Instant) {
            return ofInstant((Instant) temporalAdjuster, this.offset);
        }
        if (temporalAdjuster instanceof ZoneOffset) {
            return with(this.dateTime, (ZoneOffset) temporalAdjuster);
        }
        if (temporalAdjuster instanceof OffsetDateTime) {
            return (OffsetDateTime) temporalAdjuster;
        }
        return (OffsetDateTime) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public OffsetDateTime with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            switch (chronoField) {
                case INSTANT_SECONDS:
                    return ofInstant(Instant.ofEpochSecond(j2, getNano()), this.offset);
                case OFFSET_SECONDS:
                    return with(this.dateTime, ZoneOffset.ofTotalSeconds(chronoField.checkValidIntValue(j2)));
                default:
                    return with(this.dateTime.with(temporalField, j2), this.offset);
            }
        }
        return (OffsetDateTime) temporalField.adjustInto(this, j2);
    }

    public OffsetDateTime withYear(int i2) {
        return with(this.dateTime.withYear(i2), this.offset);
    }

    public OffsetDateTime withMonth(int i2) {
        return with(this.dateTime.withMonth(i2), this.offset);
    }

    public OffsetDateTime withDayOfMonth(int i2) {
        return with(this.dateTime.withDayOfMonth(i2), this.offset);
    }

    public OffsetDateTime withDayOfYear(int i2) {
        return with(this.dateTime.withDayOfYear(i2), this.offset);
    }

    public OffsetDateTime withHour(int i2) {
        return with(this.dateTime.withHour(i2), this.offset);
    }

    public OffsetDateTime withMinute(int i2) {
        return with(this.dateTime.withMinute(i2), this.offset);
    }

    public OffsetDateTime withSecond(int i2) {
        return with(this.dateTime.withSecond(i2), this.offset);
    }

    public OffsetDateTime withNano(int i2) {
        return with(this.dateTime.withNano(i2), this.offset);
    }

    public OffsetDateTime truncatedTo(TemporalUnit temporalUnit) {
        return with(this.dateTime.truncatedTo(temporalUnit), this.offset);
    }

    @Override // java.time.temporal.Temporal
    public OffsetDateTime plus(TemporalAmount temporalAmount) {
        return (OffsetDateTime) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public OffsetDateTime plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            return with(this.dateTime.plus(j2, temporalUnit), this.offset);
        }
        return (OffsetDateTime) temporalUnit.addTo(this, j2);
    }

    public OffsetDateTime plusYears(long j2) {
        return with(this.dateTime.plusYears(j2), this.offset);
    }

    public OffsetDateTime plusMonths(long j2) {
        return with(this.dateTime.plusMonths(j2), this.offset);
    }

    public OffsetDateTime plusWeeks(long j2) {
        return with(this.dateTime.plusWeeks(j2), this.offset);
    }

    public OffsetDateTime plusDays(long j2) {
        return with(this.dateTime.plusDays(j2), this.offset);
    }

    public OffsetDateTime plusHours(long j2) {
        return with(this.dateTime.plusHours(j2), this.offset);
    }

    public OffsetDateTime plusMinutes(long j2) {
        return with(this.dateTime.plusMinutes(j2), this.offset);
    }

    public OffsetDateTime plusSeconds(long j2) {
        return with(this.dateTime.plusSeconds(j2), this.offset);
    }

    public OffsetDateTime plusNanos(long j2) {
        return with(this.dateTime.plusNanos(j2), this.offset);
    }

    @Override // java.time.temporal.Temporal
    public OffsetDateTime minus(TemporalAmount temporalAmount) {
        return (OffsetDateTime) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public OffsetDateTime minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public OffsetDateTime minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1L) : plusYears(-j2);
    }

    public OffsetDateTime minusMonths(long j2) {
        return j2 == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1L) : plusMonths(-j2);
    }

    public OffsetDateTime minusWeeks(long j2) {
        return j2 == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : plusWeeks(-j2);
    }

    public OffsetDateTime minusDays(long j2) {
        return j2 == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1L) : plusDays(-j2);
    }

    public OffsetDateTime minusHours(long j2) {
        return j2 == Long.MIN_VALUE ? plusHours(Long.MAX_VALUE).plusHours(1L) : plusHours(-j2);
    }

    public OffsetDateTime minusMinutes(long j2) {
        return j2 == Long.MIN_VALUE ? plusMinutes(Long.MAX_VALUE).plusMinutes(1L) : plusMinutes(-j2);
    }

    public OffsetDateTime minusSeconds(long j2) {
        return j2 == Long.MIN_VALUE ? plusSeconds(Long.MAX_VALUE).plusSeconds(1L) : plusSeconds(-j2);
    }

    public OffsetDateTime minusNanos(long j2) {
        return j2 == Long.MIN_VALUE ? plusNanos(Long.MAX_VALUE).plusNanos(1L) : plusNanos(-j2);
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.offset() || temporalQuery == TemporalQueries.zone()) {
            return (R) getOffset();
        }
        if (temporalQuery == TemporalQueries.zoneId()) {
            return null;
        }
        if (temporalQuery == TemporalQueries.localDate()) {
            return (R) toLocalDate();
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R) toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R) IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.NANOS;
        }
        return temporalQuery.queryFrom(this);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(ChronoField.EPOCH_DAY, toLocalDate().toEpochDay()).with(ChronoField.NANO_OF_DAY, toLocalTime().toNanoOfDay()).with(ChronoField.OFFSET_SECONDS, getOffset().getTotalSeconds());
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        OffsetDateTime offsetDateTimeFrom = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            return this.dateTime.until(offsetDateTimeFrom.withOffsetSameInstant(this.offset).dateTime, temporalUnit);
        }
        return temporalUnit.between(this, offsetDateTimeFrom);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public ZonedDateTime atZoneSameInstant(ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this.dateTime, this.offset, zoneId);
    }

    public ZonedDateTime atZoneSimilarLocal(ZoneId zoneId) {
        return ZonedDateTime.ofLocal(this.dateTime, zoneId, this.offset);
    }

    public OffsetTime toOffsetTime() {
        return OffsetTime.of(this.dateTime.toLocalTime(), this.offset);
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.of(this.dateTime, this.offset);
    }

    public Instant toInstant() {
        return this.dateTime.toInstant(this.offset);
    }

    public long toEpochSecond() {
        return this.dateTime.toEpochSecond(this.offset);
    }

    @Override // java.lang.Comparable
    public int compareTo(OffsetDateTime offsetDateTime) {
        int iCompareInstant = compareInstant(this, offsetDateTime);
        if (iCompareInstant == 0) {
            iCompareInstant = toLocalDateTime().compareTo((ChronoLocalDateTime<?>) offsetDateTime.toLocalDateTime());
        }
        return iCompareInstant;
    }

    public boolean isAfter(OffsetDateTime offsetDateTime) {
        long epochSecond = toEpochSecond();
        long epochSecond2 = offsetDateTime.toEpochSecond();
        return epochSecond > epochSecond2 || (epochSecond == epochSecond2 && toLocalTime().getNano() > offsetDateTime.toLocalTime().getNano());
    }

    public boolean isBefore(OffsetDateTime offsetDateTime) {
        long epochSecond = toEpochSecond();
        long epochSecond2 = offsetDateTime.toEpochSecond();
        return epochSecond < epochSecond2 || (epochSecond == epochSecond2 && toLocalTime().getNano() < offsetDateTime.toLocalTime().getNano());
    }

    public boolean isEqual(OffsetDateTime offsetDateTime) {
        return toEpochSecond() == offsetDateTime.toEpochSecond() && toLocalTime().getNano() == offsetDateTime.toLocalTime().getNano();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof OffsetDateTime) {
            OffsetDateTime offsetDateTime = (OffsetDateTime) obj;
            return this.dateTime.equals(offsetDateTime.dateTime) && this.offset.equals(offsetDateTime.offset);
        }
        return false;
    }

    public int hashCode() {
        return this.dateTime.hashCode() ^ this.offset.hashCode();
    }

    public String toString() {
        return this.dateTime.toString() + this.offset.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 10, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        this.dateTime.writeExternal(objectOutput);
        this.offset.writeExternal(objectOutput);
    }

    static OffsetDateTime readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return of(LocalDateTime.readExternal(objectInput), ZoneOffset.readExternal(objectInput));
    }
}
