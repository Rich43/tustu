package java.time;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
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
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/time/YearMonth.class */
public final class YearMonth implements Temporal, TemporalAdjuster, Comparable<YearMonth>, Serializable {
    private static final long serialVersionUID = 4183400860270640070L;
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).toFormatter();
    private final int year;
    private final int month;

    public static YearMonth now() {
        return now(Clock.systemDefaultZone());
    }

    public static YearMonth now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static YearMonth now(Clock clock) {
        LocalDate localDateNow = LocalDate.now(clock);
        return of(localDateNow.getYear(), localDateNow.getMonth());
    }

    public static YearMonth of(int i2, Month month) {
        Objects.requireNonNull(month, "month");
        return of(i2, month.getValue());
    }

    public static YearMonth of(int i2, int i3) {
        ChronoField.YEAR.checkValidValue(i2);
        ChronoField.MONTH_OF_YEAR.checkValidValue(i3);
        return new YearMonth(i2, i3);
    }

    public static YearMonth from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof YearMonth) {
            return (YearMonth) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(temporalAccessor))) {
                temporalAccessor = LocalDate.from(temporalAccessor);
            }
            return of(temporalAccessor.get(ChronoField.YEAR), temporalAccessor.get(ChronoField.MONTH_OF_YEAR));
        } catch (DateTimeException e2) {
            throw new DateTimeException("Unable to obtain YearMonth from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName(), e2);
        }
    }

    public static YearMonth parse(CharSequence charSequence) {
        return parse(charSequence, PARSER);
    }

    public static YearMonth parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (YearMonth) dateTimeFormatter.parse(charSequence, YearMonth::from);
    }

    private YearMonth(int i2, int i3) {
        this.year = i2;
        this.month = i3;
    }

    private YearMonth with(int i2, int i3) {
        if (this.year == i2 && this.month == i3) {
            return this;
        }
        return new YearMonth(i2, i3);
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return temporalField instanceof ChronoField ? temporalField == ChronoField.YEAR || temporalField == ChronoField.MONTH_OF_YEAR || temporalField == ChronoField.PROLEPTIC_MONTH || temporalField == ChronoField.YEAR_OF_ERA || temporalField == ChronoField.ERA : temporalField != null && temporalField.isSupportedBy(this);
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        return temporalUnit instanceof ChronoUnit ? temporalUnit == ChronoUnit.MONTHS || temporalUnit == ChronoUnit.YEARS || temporalUnit == ChronoUnit.DECADES || temporalUnit == ChronoUnit.CENTURIES || temporalUnit == ChronoUnit.MILLENNIA || temporalUnit == ChronoUnit.ERAS : temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.YEAR_OF_ERA) {
            return getYear() <= 0 ? ValueRange.of(1L, NativeMediaPlayer.ONE_SECOND) : ValueRange.of(1L, 999999999L);
        }
        return super.range(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public int get(TemporalField temporalField) {
        return range(temporalField).checkValidIntValue(getLong(temporalField), temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case MONTH_OF_YEAR:
                    return this.month;
                case PROLEPTIC_MONTH:
                    return getProlepticMonth();
                case YEAR_OF_ERA:
                    return this.year < 1 ? 1 - this.year : this.year;
                case YEAR:
                    return this.year;
                case ERA:
                    return this.year < 1 ? 0 : 1;
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return temporalField.getFrom(this);
    }

    private long getProlepticMonth() {
        return ((this.year * 12) + this.month) - 1;
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

    public boolean isLeapYear() {
        return IsoChronology.INSTANCE.isLeapYear(this.year);
    }

    public boolean isValidDay(int i2) {
        return i2 >= 1 && i2 <= lengthOfMonth();
    }

    public int lengthOfMonth() {
        return getMonth().length(isLeapYear());
    }

    public int lengthOfYear() {
        return isLeapYear() ? 366 : 365;
    }

    @Override // java.time.temporal.Temporal
    public YearMonth with(TemporalAdjuster temporalAdjuster) {
        return (YearMonth) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public YearMonth with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            chronoField.checkValidValue(j2);
            switch (chronoField) {
                case MONTH_OF_YEAR:
                    return withMonth((int) j2);
                case PROLEPTIC_MONTH:
                    return plusMonths(j2 - getProlepticMonth());
                case YEAR_OF_ERA:
                    return withYear((int) (this.year < 1 ? 1 - j2 : j2));
                case YEAR:
                    return withYear((int) j2);
                case ERA:
                    return getLong(ChronoField.ERA) == j2 ? this : withYear(1 - this.year);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return (YearMonth) temporalField.adjustInto(this, j2);
    }

    public YearMonth withYear(int i2) {
        ChronoField.YEAR.checkValidValue(i2);
        return with(i2, this.month);
    }

    public YearMonth withMonth(int i2) {
        ChronoField.MONTH_OF_YEAR.checkValidValue(i2);
        return with(this.year, i2);
    }

    @Override // java.time.temporal.Temporal
    public YearMonth plus(TemporalAmount temporalAmount) {
        return (YearMonth) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public YearMonth plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
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
        return (YearMonth) temporalUnit.addTo(this, j2);
    }

    public YearMonth plusYears(long j2) {
        if (j2 == 0) {
            return this;
        }
        return with(ChronoField.YEAR.checkValidIntValue(this.year + j2), this.month);
    }

    public YearMonth plusMonths(long j2) {
        if (j2 == 0) {
            return this;
        }
        long j3 = (this.year * 12) + (this.month - 1) + j2;
        return with(ChronoField.YEAR.checkValidIntValue(Math.floorDiv(j3, 12L)), ((int) Math.floorMod(j3, 12L)) + 1);
    }

    @Override // java.time.temporal.Temporal
    public YearMonth minus(TemporalAmount temporalAmount) {
        return (YearMonth) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public YearMonth minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public YearMonth minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1L) : plusYears(-j2);
    }

    public YearMonth minusMonths(long j2) {
        return j2 == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1L) : plusMonths(-j2);
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R) IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.MONTHS;
        }
        return (R) super.query(temporalQuery);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        if (!Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        return temporal.with(ChronoField.PROLEPTIC_MONTH, getProlepticMonth());
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        YearMonth yearMonthFrom = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            long prolepticMonth = yearMonthFrom.getProlepticMonth() - getProlepticMonth();
            switch ((ChronoUnit) temporalUnit) {
                case MONTHS:
                    return prolepticMonth;
                case YEARS:
                    return prolepticMonth / 12;
                case DECADES:
                    return prolepticMonth / 120;
                case CENTURIES:
                    return prolepticMonth / 1200;
                case MILLENNIA:
                    return prolepticMonth / 12000;
                case ERAS:
                    return yearMonthFrom.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return temporalUnit.between(this, yearMonthFrom);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public LocalDate atDay(int i2) {
        return LocalDate.of(this.year, this.month, i2);
    }

    public LocalDate atEndOfMonth() {
        return LocalDate.of(this.year, this.month, lengthOfMonth());
    }

    @Override // java.lang.Comparable
    public int compareTo(YearMonth yearMonth) {
        int i2 = this.year - yearMonth.year;
        if (i2 == 0) {
            i2 = this.month - yearMonth.month;
        }
        return i2;
    }

    public boolean isAfter(YearMonth yearMonth) {
        return compareTo(yearMonth) > 0;
    }

    public boolean isBefore(YearMonth yearMonth) {
        return compareTo(yearMonth) < 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof YearMonth) {
            YearMonth yearMonth = (YearMonth) obj;
            return this.year == yearMonth.year && this.month == yearMonth.month;
        }
        return false;
    }

    public int hashCode() {
        return this.year ^ (this.month << 27);
    }

    public String toString() {
        int iAbs = Math.abs(this.year);
        StringBuilder sb = new StringBuilder(9);
        if (iAbs < 1000) {
            if (this.year < 0) {
                sb.append(this.year - 10000).deleteCharAt(1);
            } else {
                sb.append(this.year + 10000).deleteCharAt(0);
            }
        } else {
            sb.append(this.year);
        }
        return sb.append(this.month < 10 ? "-0" : LanguageTag.SEP).append(this.month).toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 12, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeByte(this.month);
    }

    static YearMonth readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readByte());
    }
}
