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

/* loaded from: rt.jar:java/time/Year.class */
public final class Year implements Temporal, TemporalAdjuster, Comparable<Year>, Serializable {
    public static final int MIN_VALUE = -999999999;
    public static final int MAX_VALUE = 999999999;
    private static final long serialVersionUID = -23038383694477807L;
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).toFormatter();
    private final int year;

    public static Year now() {
        return now(Clock.systemDefaultZone());
    }

    public static Year now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static Year now(Clock clock) {
        return of(LocalDate.now(clock).getYear());
    }

    public static Year of(int i2) {
        ChronoField.YEAR.checkValidValue(i2);
        return new Year(i2);
    }

    public static Year from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Year) {
            return (Year) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        try {
            if (!IsoChronology.INSTANCE.equals(Chronology.from(temporalAccessor))) {
                temporalAccessor = LocalDate.from(temporalAccessor);
            }
            return of(temporalAccessor.get(ChronoField.YEAR));
        } catch (DateTimeException e2) {
            throw new DateTimeException("Unable to obtain Year from TemporalAccessor: " + ((Object) temporalAccessor) + " of type " + temporalAccessor.getClass().getName(), e2);
        }
    }

    public static Year parse(CharSequence charSequence) {
        return parse(charSequence, PARSER);
    }

    public static Year parse(CharSequence charSequence, DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return (Year) dateTimeFormatter.parse(charSequence, Year::from);
    }

    public static boolean isLeap(long j2) {
        return (j2 & 3) == 0 && (j2 % 100 != 0 || j2 % 400 == 0);
    }

    private Year(int i2) {
        this.year = i2;
    }

    public int getValue() {
        return this.year;
    }

    @Override // java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        return temporalField instanceof ChronoField ? temporalField == ChronoField.YEAR || temporalField == ChronoField.YEAR_OF_ERA || temporalField == ChronoField.ERA : temporalField != null && temporalField.isSupportedBy(this);
    }

    @Override // java.time.temporal.Temporal
    public boolean isSupported(TemporalUnit temporalUnit) {
        return temporalUnit instanceof ChronoUnit ? temporalUnit == ChronoUnit.YEARS || temporalUnit == ChronoUnit.DECADES || temporalUnit == ChronoUnit.CENTURIES || temporalUnit == ChronoUnit.MILLENNIA || temporalUnit == ChronoUnit.ERAS : temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField == ChronoField.YEAR_OF_ERA) {
            return this.year <= 0 ? ValueRange.of(1L, NativeMediaPlayer.ONE_SECOND) : ValueRange.of(1L, 999999999L);
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

    public boolean isLeap() {
        return isLeap(this.year);
    }

    public boolean isValidMonthDay(MonthDay monthDay) {
        return monthDay != null && monthDay.isValidYear(this.year);
    }

    public int length() {
        return isLeap() ? 366 : 365;
    }

    @Override // java.time.temporal.Temporal
    public Year with(TemporalAdjuster temporalAdjuster) {
        return (Year) temporalAdjuster.adjustInto(this);
    }

    @Override // java.time.temporal.Temporal
    public Year with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            chronoField.checkValidValue(j2);
            switch (chronoField) {
                case YEAR_OF_ERA:
                    return of((int) (this.year < 1 ? 1 - j2 : j2));
                case YEAR:
                    return of((int) j2);
                case ERA:
                    return getLong(ChronoField.ERA) == j2 ? this : of(1 - this.year);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return (Year) temporalField.adjustInto(this, j2);
    }

    @Override // java.time.temporal.Temporal
    public Year plus(TemporalAmount temporalAmount) {
        return (Year) temporalAmount.addTo(this);
    }

    @Override // java.time.temporal.Temporal
    public Year plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
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
        return (Year) temporalUnit.addTo(this, j2);
    }

    public Year plusYears(long j2) {
        if (j2 == 0) {
            return this;
        }
        return of(ChronoField.YEAR.checkValidIntValue(this.year + j2));
    }

    @Override // java.time.temporal.Temporal
    public Year minus(TemporalAmount temporalAmount) {
        return (Year) temporalAmount.subtractFrom(this);
    }

    @Override // java.time.temporal.Temporal
    public Year minus(long j2, TemporalUnit temporalUnit) {
        return j2 == Long.MIN_VALUE ? plus(Long.MAX_VALUE, temporalUnit).plus(1L, temporalUnit) : plus(-j2, temporalUnit);
    }

    public Year minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1L) : plusYears(-j2);
    }

    @Override // java.time.temporal.TemporalAccessor
    public <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R) IsoChronology.INSTANCE;
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.YEARS;
        }
        return (R) super.query(temporalQuery);
    }

    @Override // java.time.temporal.TemporalAdjuster
    public Temporal adjustInto(Temporal temporal) {
        if (!Chronology.from(temporal).equals(IsoChronology.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        return temporal.with(ChronoField.YEAR, this.year);
    }

    @Override // java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        Year yearFrom = from(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            long j2 = yearFrom.year - this.year;
            switch ((ChronoUnit) temporalUnit) {
                case YEARS:
                    return j2;
                case DECADES:
                    return j2 / 10;
                case CENTURIES:
                    return j2 / 100;
                case MILLENNIA:
                    return j2 / 1000;
                case ERAS:
                    return yearFrom.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return temporalUnit.between(this, yearFrom);
    }

    public String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    public LocalDate atDay(int i2) {
        return LocalDate.ofYearDay(this.year, i2);
    }

    public YearMonth atMonth(Month month) {
        return YearMonth.of(this.year, month);
    }

    public YearMonth atMonth(int i2) {
        return YearMonth.of(this.year, i2);
    }

    public LocalDate atMonthDay(MonthDay monthDay) {
        return monthDay.atYear(this.year);
    }

    @Override // java.lang.Comparable
    public int compareTo(Year year) {
        return this.year - year.year;
    }

    public boolean isAfter(Year year) {
        return this.year > year.year;
    }

    public boolean isBefore(Year year) {
        return this.year < year.year;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Year) && this.year == ((Year) obj).year;
    }

    public int hashCode() {
        return this.year;
    }

    public String toString() {
        return Integer.toString(this.year);
    }

    private Object writeReplace() {
        return new Ser((byte) 11, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
    }

    static Year readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt());
    }
}
