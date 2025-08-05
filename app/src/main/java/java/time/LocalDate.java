package java.time;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Era;
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
import java.time.zone.ZoneOffsetTransition;
import java.util.Objects;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/time/LocalDate.class */
public final class LocalDate implements Temporal, TemporalAdjuster, ChronoLocalDate, Serializable {
    public static final LocalDate MIN = of(Year.MIN_VALUE, 1, 1);
    public static final LocalDate MAX = of(Year.MAX_VALUE, 12, 31);
    private static final long serialVersionUID = 2942565459149668126L;
    private static final int DAYS_PER_CYCLE = 146097;
    static final long DAYS_0000_TO_1970 = 719528;
    private final int year;
    private final short month;
    private final short day;

    public static LocalDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static LocalDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static LocalDate now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return ofEpochDay(Math.floorDiv(clock.instant().getEpochSecond() + clock.getZone().getRules().getOffset(r0).getTotalSeconds(), 86400L));
    }

    public static LocalDate of(int i2, Month month, int i3) {
        ChronoField.YEAR.checkValidValue(i2);
        Objects.requireNonNull(month, "month");
        ChronoField.DAY_OF_MONTH.checkValidValue(i3);
        return create(i2, month.getValue(), i3);
    }

    public static LocalDate of(int i2, int i3, int i4) {
        ChronoField.YEAR.checkValidValue(i2);
        ChronoField.MONTH_OF_YEAR.checkValidValue(i3);
        ChronoField.DAY_OF_MONTH.checkValidValue(i4);
        return create(i2, i3, i4);
    }

    public static LocalDate ofYearDay(int i2, int i3) {
        ChronoField.YEAR.checkValidValue(i2);
        ChronoField.DAY_OF_YEAR.checkValidValue(i3);
        boolean zIsLeapYear = IsoChronology.INSTANCE.isLeapYear(i2);
        if (i3 == 366 && !zIsLeapYear) {
            throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + i2 + "' is not a leap year");
        }
        Month monthOf = Month.of(((i3 - 1) / 31) + 1);
        if (i3 > (monthOf.firstDayOfYear(zIsLeapYear) + monthOf.length(zIsLeapYear)) - 1) {
            monthOf = monthOf.plus(1L);
        }
        return new LocalDate(i2, monthOf.getValue(), (i3 - monthOf.firstDayOfYear(zIsLeapYear)) + 1);
    }

    public static LocalDate ofEpochDay(long j2) {
        long j3 = (j2 + DAYS_0000_TO_1970) - 60;
        long j4 = 0;
        if (j3 < 0) {
            long j5 = ((j3 + 1) / 146097) - 1;
            j4 = j5 * 400;
            j3 += (-j5) * 146097;
        }
        long j6 = ((400 * j3) + 591) / 146097;
        long j7 = j3 - ((((365 * j6) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        if (j7 < 0) {
            j6--;
            j7 = j3 - ((((365 * j6) + (j6 / 4)) - (j6 / 100)) + (j6 / 400));
        }
        int i2 = (int) j7;
        int i3 = ((i2 * 5) + 2) / 153;
        return new LocalDate(ChronoField.YEAR.checkValidIntValue(j6 + j4 + (i3 / 10)), ((i3 + 2) % 12) + 1, (i2 - (((i3 * 306) + 5) / 10)) + 1);
    }

    public static LocalDate from(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        LocalDate localDate = (LocalDate) temporalAccessor.query(TemporalQueries.localDate());
        if (localDate == null) {
            throw new DateTimeException("Unable to obtain LocalDate from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName());
        }
        return localDate;
    }

    public static LocalDate parse(CharSequence charSequence) {
        return parse(charSequence, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (LocalDate) dateTimeFormatter.parse(charSequence, LocalDate::from);
    }

    private static LocalDate create(int i2, int i3, int i4) {
        if (i4 > 28) {
            int i5 = 31;
            switch (i3) {
                case 2:
                    i5 = IsoChronology.INSTANCE.isLeapYear((long) i2) ? 29 : 28;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    i5 = 30;
                    break;
            }
            if (i4 > i5) {
                if (i4 == 29) {
                    throw new DateTimeException("Invalid date 'February 29' as '" + i2 + "' is not a leap year");
                }
                throw new DateTimeException("Invalid date '" + Month.of(i3).name() + " " + i4 + PdfOps.SINGLE_QUOTE_TOKEN);
            }
        }
        return new LocalDate(i2, i3, i4);
    }

    private static LocalDate resolvePreviousValid(int i2, int i3, int i4) {
        switch (i3) {
            case 2:
                i4 = Math.min(i4, IsoChronology.INSTANCE.isLeapYear((long) i2) ? 29 : 28);
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                i4 = Math.min(i4, 30);
                break;
        }
        return new LocalDate(i2, i3, i4);
    }

    private LocalDate(int i2, int i3, int i4) {
        this.year = i2;
        this.month = (short) i3;
        this.day = (short) i4;
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return super.isSupported(temporalField);
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        return super.isSupported(temporalUnit);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            if (chronoField.isDateBased()) {
                switch (chronoField) {
                    case DAY_OF_MONTH:
                        return ValueRange.of(1L, lengthOfMonth());
                    case DAY_OF_YEAR:
                        return ValueRange.of(1L, lengthOfYear());
                    case ALIGNED_WEEK_OF_MONTH:
                        return ValueRange.of(1L, (getMonth() != Month.FEBRUARY || isLeapYear()) ? 5L : 4L);
                    case YEAR_OF_ERA:
                        return getYear() <= 0 ? ValueRange.of(1L, NativeMediaPlayer.ONE_SECOND) : ValueRange.of(1L, 999999999L);
                    default:
                        return temporalField.range();
                }
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
        return temporalField.rangeRefinedBy(this);
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
            if (temporalField == ChronoField.EPOCH_DAY) {
                return toEpochDay();
            }
            if (temporalField == ChronoField.PROLEPTIC_MONTH) {
                return getProlepticMonth();
            }
            return get0(temporalField);
        }
        return temporalField.getFrom(this);
    }

    private int get0(TemporalField temporalField) {
        switch ((ChronoField) temporalField) {
            case DAY_OF_MONTH:
                return this.day;
            case DAY_OF_YEAR:
                return getDayOfYear();
            case ALIGNED_WEEK_OF_MONTH:
                return ((this.day - 1) / 7) + 1;
            case YEAR_OF_ERA:
                return this.year >= 1 ? this.year : 1 - this.year;
            case DAY_OF_WEEK:
                return getDayOfWeek().getValue();
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                return ((this.day - 1) % 7) + 1;
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                return ((getDayOfYear() - 1) % 7) + 1;
            case EPOCH_DAY:
                throw new UnsupportedTemporalTypeException("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case ALIGNED_WEEK_OF_YEAR:
                return ((getDayOfYear() - 1) / 7) + 1;
            case MONTH_OF_YEAR:
                return this.month;
            case PROLEPTIC_MONTH:
                throw new UnsupportedTemporalTypeException("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case YEAR:
                return this.year;
            case ERA:
                return this.year >= 1 ? 1 : 0;
            default:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
    }

    private long getProlepticMonth() {
        return ((this.year * 12) + this.month) - 1;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public Era getEra() {
        return super.getEra();
    }

    public int getYear() {
        return this.year;
    }

    public int getMonthValue() {
        return this.month;
    }

    public Month getMonth() {
        return Month.of(this.month);
    }

    public int getDayOfMonth() {
        return this.day;
    }

    public int getDayOfYear() {
        return (getMonth().firstDayOfYear(isLeapYear()) + this.day) - 1;
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.of(((int) Math.floorMod(toEpochDay() + 3, 7L)) + 1);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear(this.year);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfMonth() {
        switch (this.month) {
            case 2:
                return isLeapYear() ? 29 : 28;
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            default:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    @Override // java.time.temporal.Temporal
    public LocalDate with(TemporalAdjuster temporalAdjuster) {
        if (temporalAdjuster instanceof LocalDate) {
            return (LocalDate) temporalAdjuster;
        }
        return (LocalDate) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalDate with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            chronoField.checkValidValue(j2);
            switch (chronoField) {
                case DAY_OF_MONTH:
                    return withDayOfMonth((int) j2);
                case DAY_OF_YEAR:
                    return withDayOfYear((int) j2);
                case ALIGNED_WEEK_OF_MONTH:
                    return plusWeeks(j2 - getLong(ChronoField.ALIGNED_WEEK_OF_MONTH));
                case YEAR_OF_ERA:
                    return withYear((int) (this.year >= 1 ? j2 : 1 - j2));
                case DAY_OF_WEEK:
                    return plusDays(j2 - getDayOfWeek().getValue());
                case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                    return plusDays(j2 - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
                case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                    return plusDays(j2 - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
                case EPOCH_DAY:
                    return ofEpochDay(j2);
                case ALIGNED_WEEK_OF_YEAR:
                    return plusWeeks(j2 - getLong(ChronoField.ALIGNED_WEEK_OF_YEAR));
                case MONTH_OF_YEAR:
                    return withMonth((int) j2);
                case PROLEPTIC_MONTH:
                    return plusMonths(j2 - getProlepticMonth());
                case YEAR:
                    return withYear((int) j2);
                case ERA:
                    return getLong(ChronoField.ERA) == j2 ? this : withYear(1 - this.year);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return (LocalDate) temporalField.adjustInto(this, j2);
    }

    public LocalDate withYear(int i2) {
        if (this.year == i2) {
            return this;
        }
        ChronoField.YEAR.checkValidValue(i2);
        return resolvePreviousValid(i2, this.month, this.day);
    }

    public LocalDate withMonth(int i2) {
        if (this.month == i2) {
            return this;
        }
        ChronoField.MONTH_OF_YEAR.checkValidValue(i2);
        return resolvePreviousValid(this.year, i2, this.day);
    }

    public LocalDate withDayOfMonth(int i2) {
        if (this.day == i2) {
            return this;
        }
        return of(this.year, this.month, i2);
    }

    public LocalDate withDayOfYear(int i2) {
        if (getDayOfYear() == i2) {
            return this;
        }
        return ofYearDay(this.year, i2);
    }

    @Override // java.time.temporal.Temporal
    public LocalDate plus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return plusMonths(((Period) temporalAmount).toTotalMonths()).plusDays(r0.getDays());
        }
        Objects.requireNonNull(temporalAmount, "amountToAdd");
        return (LocalDate) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalDate plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case DAYS:
                    return plusDays(j2);
                case WEEKS:
                    return plusWeeks(j2);
                case MONTHS:
                    return plusMonths(j2);
                case YEARS:
                    return plusYears(j2);
                case DECADES:
                    return plusYears(Math.multiplyExact(j2, 10L));
                case CENTURIES:
                    return plusYears(Math.multiplyExact(j2, 100L));
                case MILLENNIA:
                    return plusYears(Math.multiplyExact(j2, 1000L));
                case ERAS:
                    return with((TemporalField) ChronoField.ERA, Math.addExact(getLong(ChronoField.ERA), j2));
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return (LocalDate) temporalUnit.addTo(this, j2);
    }

    public LocalDate plusYears(long j2) {
        if (j2 == 0) {
            return this;
        }
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(this.year + j2), this.month, this.day);
    }

    public LocalDate plusMonths(long j2) {
        if (j2 == 0) {
            return this;
        }
        long j3 = (this.year * 12) + (this.month - 1) + j2;
        return resolvePreviousValid(ChronoField.YEAR.checkValidIntValue(Math.floorDiv(j3, 12L)), ((int) Math.floorMod(j3, 12L)) + 1, this.day);
    }

    public LocalDate plusWeeks(long j2) {
        return plusDays(Math.multiplyExact(j2, 7L));
    }

    public LocalDate plusDays(long j2) {
        if (j2 == 0) {
            return this;
        }
        return ofEpochDay(Math.addExact(toEpochDay(), j2));
    }

    @Override // java.time.temporal.Temporal
    public LocalDate minus(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return minusMonths(((Period) temporalAmount).toTotalMonths()).minusDays(r0.getDays());
        }
        Objects.requireNonNull(temporalAmount, "amountToSubtract");
        return (LocalDate) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public LocalDate minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public LocalDate minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1L) : plusYears(-j2);
    }

    public LocalDate minusMonths(long j2) {
        return j2 == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1L) : plusMonths(-j2);
    }

    public LocalDate minusWeeks(long j2) {
        return j2 == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1L) : plusWeeks(-j2);
    }

    public LocalDate minusDays(long j2) {
        return j2 == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1L) : plusDays(-j2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.localDate()) {
            return this;
        }
        return (R) super.query(temporalQuery);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        return super.adjustInto(temporal);
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        LocalDate localDateFrom = from((TemporalAccessor) temporal);
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case DAYS:
                    return daysUntil(localDateFrom);
                case WEEKS:
                    return daysUntil(localDateFrom) / 7;
                case MONTHS:
                    return monthsUntil(localDateFrom);
                case YEARS:
                    return monthsUntil(localDateFrom) / 12;
                case DECADES:
                    return monthsUntil(localDateFrom) / 120;
                case CENTURIES:
                    return monthsUntil(localDateFrom) / 1200;
                case MILLENNIA:
                    return monthsUntil(localDateFrom) / 12000;
                case ERAS:
                    return localDateFrom.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return temporalUnit.between(this, localDateFrom);
    }

    long daysUntil(LocalDate localDate) {
        return localDate.toEpochDay() - toEpochDay();
    }

    private long monthsUntil(LocalDate localDate) {
        return (((localDate.getProlepticMonth() * 32) + localDate.getDayOfMonth()) - ((getProlepticMonth() * 32) + getDayOfMonth())) / 32;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public Period until(ChronoLocalDate chronoLocalDate) {
        LocalDate localDateFrom = from((TemporalAccessor) chronoLocalDate);
        long prolepticMonth = localDateFrom.getProlepticMonth() - getProlepticMonth();
        int iLengthOfMonth = localDateFrom.day - this.day;
        if (prolepticMonth > 0 && iLengthOfMonth < 0) {
            prolepticMonth--;
            iLengthOfMonth = (int) (localDateFrom.toEpochDay() - plusMonths(prolepticMonth).toEpochDay());
        } else if (prolepticMonth < 0 && iLengthOfMonth > 0) {
            prolepticMonth++;
            iLengthOfMonth -= localDateFrom.lengthOfMonth();
        }
        return Period.of(Math.toIntExact(prolepticMonth / 12), (int) (prolepticMonth % 12), iLengthOfMonth);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public LocalDateTime atTime(LocalTime localTime) {
        return LocalDateTime.of(this, localTime);
    }

    public LocalDateTime atTime(int i2, int i3) {
        return atTime(LocalTime.of(i2, i3));
    }

    public LocalDateTime atTime(int i2, int i3, int i4) {
        return atTime(LocalTime.of(i2, i3, i4));
    }

    public LocalDateTime atTime(int i2, int i3, int i4, int i5) {
        return atTime(LocalTime.of(i2, i3, i4, i5));
    }

    public OffsetDateTime atTime(OffsetTime offsetTime) {
        return OffsetDateTime.of(LocalDateTime.of(this, offsetTime.toLocalTime()), offsetTime.getOffset());
    }

    public LocalDateTime atStartOfDay() {
        return LocalDateTime.of(this, LocalTime.MIDNIGHT);
    }

    public ZonedDateTime atStartOfDay(ZoneId zoneId) {
        ZoneOffsetTransition transition;
        Objects.requireNonNull(zoneId, "zone");
        LocalDateTime localDateTimeAtTime = atTime(LocalTime.MIDNIGHT);
        if (!(zoneId instanceof ZoneOffset) && (transition = zoneId.getRules().getTransition(localDateTimeAtTime)) != null && transition.isGap()) {
            localDateTimeAtTime = transition.getDateTimeAfter();
        }
        return ZonedDateTime.of(localDateTimeAtTime, zoneId);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public long toEpochDay() {
        long j2;
        long j3 = this.year;
        long j4 = this.month;
        long j5 = 0 + (365 * j3);
        if (j3 >= 0) {
            j2 = j5 + (((j3 + 3) / 4) - ((j3 + 99) / 100)) + ((j3 + 399) / 400);
        } else {
            j2 = j5 - (((j3 / (-4)) - (j3 / (-100))) + (j3 / (-400)));
        }
        long j6 = j2 + (((367 * j4) - 362) / 12) + (this.day - 1);
        if (j4 > 2) {
            j6--;
            if (!isLeapYear()) {
                j6--;
            }
        }
        return j6 - DAYS_0000_TO_1970;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.time.chrono.ChronoLocalDate, java.lang.Comparable
    public int compareTo(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate);
        }
        return super.compareTo(chronoLocalDate);
    }

    int compareTo0(LocalDate localDate) {
        int i2 = this.year - localDate.year;
        if (i2 == 0) {
            i2 = this.month - localDate.month;
            if (i2 == 0) {
                i2 = this.day - localDate.day;
            }
        }
        return i2;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean isAfter(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate) > 0;
        }
        return super.isAfter(chronoLocalDate);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean isBefore(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate) < 0;
        }
        return super.isBefore(chronoLocalDate);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean isEqual(ChronoLocalDate chronoLocalDate) {
        if (chronoLocalDate instanceof LocalDate) {
            return compareTo0((LocalDate) chronoLocalDate) == 0;
        }
        return super.isEqual(chronoLocalDate);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof LocalDate) && compareTo0((LocalDate) obj) == 0;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int hashCode() {
        int i2 = this.year;
        return (i2 & (-2048)) ^ (((i2 << 11) + (this.month << 6)) + this.day);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public String toString() {
        int i2 = this.year;
        short s2 = this.month;
        short s3 = this.day;
        int iAbs = Math.abs(i2);
        StringBuilder sb = new StringBuilder(10);
        if (iAbs < 1000) {
            if (i2 < 0) {
                sb.append(i2 - 10000).deleteCharAt(1);
            } else {
                sb.append(i2 + 10000).deleteCharAt(0);
            }
        } else {
            if (i2 > 9999) {
                sb.append('+');
            }
            sb.append(i2);
        }
        return sb.append(s2 < 10 ? "-0" : LanguageTag.SEP).append((int) s2).append(s3 < 10 ? "-0" : LanguageTag.SEP).append((int) s3).toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 3, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
        dataOutput.writeByte(this.day);
    }

    static LocalDate readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readByte(), dataInput.readByte());
    }
}
