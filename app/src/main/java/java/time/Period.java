package java.time;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/time/Period.class */
public final class Period implements ChronoPeriod, Serializable {
    private static final long serialVersionUID = -3587258372562876L;
    private final int years;
    private final int months;
    private final int days;
    public static final Period ZERO = new Period(0, 0, 0);
    private static final Pattern PATTERN = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?", 2);
    private static final List<TemporalUnit> SUPPORTED_UNITS = Collections.unmodifiableList(Arrays.asList(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS));

    public static Period ofYears(int i2) {
        return create(i2, 0, 0);
    }

    public static Period ofMonths(int i2) {
        return create(0, i2, 0);
    }

    public static Period ofWeeks(int i2) {
        return create(0, 0, Math.multiplyExact(i2, 7));
    }

    public static Period ofDays(int i2) {
        return create(0, 0, i2);
    }

    public static Period of(int i2, int i3, int i4) {
        return create(i2, i3, i4);
    }

    public static Period from(TemporalAmount temporalAmount) {
        if (temporalAmount instanceof Period) {
            return (Period) temporalAmount;
        }
        if ((temporalAmount instanceof ChronoPeriod) && !IsoChronology.INSTANCE.equals(((ChronoPeriod) temporalAmount).getChronology())) {
            throw new DateTimeException("Period requires ISO chronology: " + ((Object) temporalAmount));
        }
        Objects.requireNonNull(temporalAmount, Constants.ATTRNAME_AMOUNT);
        int intExact = 0;
        int intExact2 = 0;
        int intExact3 = 0;
        for (TemporalUnit temporalUnit : temporalAmount.getUnits()) {
            long j2 = temporalAmount.get(temporalUnit);
            if (temporalUnit == ChronoUnit.YEARS) {
                intExact = Math.toIntExact(j2);
            } else if (temporalUnit == ChronoUnit.MONTHS) {
                intExact2 = Math.toIntExact(j2);
            } else if (temporalUnit == ChronoUnit.DAYS) {
                intExact3 = Math.toIntExact(j2);
            } else {
                throw new DateTimeException("Unit must be Years, Months or Days, but was " + ((Object) temporalUnit));
            }
        }
        return create(intExact, intExact2, intExact3);
    }

    public static Period parse(CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        Matcher matcher = PATTERN.matcher(charSequence);
        if (matcher.matches()) {
            int i2 = LanguageTag.SEP.equals(matcher.group(1)) ? -1 : 1;
            String strGroup = matcher.group(2);
            String strGroup2 = matcher.group(3);
            String strGroup3 = matcher.group(4);
            String strGroup4 = matcher.group(5);
            if (strGroup != null || strGroup2 != null || strGroup4 != null || strGroup3 != null) {
                try {
                    return create(parseNumber(charSequence, strGroup, i2), parseNumber(charSequence, strGroup2, i2), Math.addExact(parseNumber(charSequence, strGroup4, i2), Math.multiplyExact(parseNumber(charSequence, strGroup3, i2), 7)));
                } catch (NumberFormatException e2) {
                    throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0, e2);
                }
            }
        }
        throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0);
    }

    private static int parseNumber(CharSequence charSequence, String str, int i2) {
        if (str == null) {
            return 0;
        }
        try {
            return Math.multiplyExact(Integer.parseInt(str), i2);
        } catch (ArithmeticException e2) {
            throw new DateTimeParseException("Text cannot be parsed to a Period", charSequence, 0, e2);
        }
    }

    public static Period between(LocalDate localDate, LocalDate localDate2) {
        return localDate.until((ChronoLocalDate) localDate2);
    }

    private static Period create(int i2, int i3, int i4) {
        if ((i2 | i3 | i4) == 0) {
            return ZERO;
        }
        return new Period(i2, i3, i4);
    }

    private Period(int i2, int i3, int i4) {
        this.years = i2;
        this.months = i3;
        this.days = i4;
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.YEARS) {
            return getYears();
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return getMonths();
        }
        if (temporalUnit == ChronoUnit.DAYS) {
            return getDays();
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public List<TemporalUnit> getUnits() {
        return SUPPORTED_UNITS;
    }

    @Override // java.time.chrono.ChronoPeriod
    public IsoChronology getChronology() {
        return IsoChronology.INSTANCE;
    }

    @Override // java.time.chrono.ChronoPeriod
    public boolean isZero() {
        return this == ZERO;
    }

    @Override // java.time.chrono.ChronoPeriod
    public boolean isNegative() {
        return this.years < 0 || this.months < 0 || this.days < 0;
    }

    public int getYears() {
        return this.years;
    }

    public int getMonths() {
        return this.months;
    }

    public int getDays() {
        return this.days;
    }

    public Period withYears(int i2) {
        if (i2 == this.years) {
            return this;
        }
        return create(i2, this.months, this.days);
    }

    public Period withMonths(int i2) {
        if (i2 == this.months) {
            return this;
        }
        return create(this.years, i2, this.days);
    }

    public Period withDays(int i2) {
        if (i2 == this.days) {
            return this;
        }
        return create(this.years, this.months, i2);
    }

    @Override // java.time.chrono.ChronoPeriod
    public Period plus(TemporalAmount temporalAmount) {
        Period periodFrom = from(temporalAmount);
        return create(Math.addExact(this.years, periodFrom.years), Math.addExact(this.months, periodFrom.months), Math.addExact(this.days, periodFrom.days));
    }

    public Period plusYears(long j2) {
        if (j2 == 0) {
            return this;
        }
        return create(Math.toIntExact(Math.addExact(this.years, j2)), this.months, this.days);
    }

    public Period plusMonths(long j2) {
        if (j2 == 0) {
            return this;
        }
        return create(this.years, Math.toIntExact(Math.addExact(this.months, j2)), this.days);
    }

    public Period plusDays(long j2) {
        if (j2 == 0) {
            return this;
        }
        return create(this.years, this.months, Math.toIntExact(Math.addExact(this.days, j2)));
    }

    @Override // java.time.chrono.ChronoPeriod
    public Period minus(TemporalAmount temporalAmount) {
        Period periodFrom = from(temporalAmount);
        return create(Math.subtractExact(this.years, periodFrom.years), Math.subtractExact(this.months, periodFrom.months), Math.subtractExact(this.days, periodFrom.days));
    }

    public Period minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1L) : plusYears(-j2);
    }

    public Period minusMonths(long j2) {
        return j2 == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1L) : plusMonths(-j2);
    }

    public Period minusDays(long j2) {
        return j2 == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1L) : plusDays(-j2);
    }

    @Override // java.time.chrono.ChronoPeriod
    public Period multipliedBy(int i2) {
        if (this == ZERO || i2 == 1) {
            return this;
        }
        return create(Math.multiplyExact(this.years, i2), Math.multiplyExact(this.months, i2), Math.multiplyExact(this.days, i2));
    }

    @Override // java.time.chrono.ChronoPeriod
    public Period negated() {
        return multipliedBy(-1);
    }

    @Override // java.time.chrono.ChronoPeriod
    public Period normalized() {
        long totalMonths = toTotalMonths();
        long j2 = totalMonths / 12;
        int i2 = (int) (totalMonths % 12);
        if (j2 == this.years && i2 == this.months) {
            return this;
        }
        return create(Math.toIntExact(j2), i2, this.days);
    }

    public long toTotalMonths() {
        return (this.years * 12) + this.months;
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public Temporal addTo(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.plus(this.years, ChronoUnit.YEARS);
            }
        } else {
            long totalMonths = toTotalMonths();
            if (totalMonths != 0) {
                temporal = temporal.plus(totalMonths, ChronoUnit.MONTHS);
            }
        }
        if (this.days != 0) {
            temporal = temporal.plus(this.days, ChronoUnit.DAYS);
        }
        return temporal;
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public Temporal subtractFrom(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.minus(this.years, ChronoUnit.YEARS);
            }
        } else {
            long totalMonths = toTotalMonths();
            if (totalMonths != 0) {
                temporal = temporal.minus(totalMonths, ChronoUnit.MONTHS);
            }
        }
        if (this.days != 0) {
            temporal = temporal.minus(this.days, ChronoUnit.DAYS);
        }
        return temporal;
    }

    private void validateChrono(TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology != null && !IsoChronology.INSTANCE.equals(chronology)) {
            throw new DateTimeException("Chronology mismatch, expected: ISO, actual: " + chronology.getId());
        }
    }

    @Override // java.time.chrono.ChronoPeriod
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Period) {
            Period period = (Period) obj;
            return this.years == period.years && this.months == period.months && this.days == period.days;
        }
        return false;
    }

    @Override // java.time.chrono.ChronoPeriod
    public int hashCode() {
        return this.years + Integer.rotateLeft(this.months, 8) + Integer.rotateLeft(this.days, 16);
    }

    @Override // java.time.chrono.ChronoPeriod
    public String toString() {
        if (this == ZERO) {
            return "P0D";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('P');
        if (this.years != 0) {
            sb.append(this.years).append('Y');
        }
        if (this.months != 0) {
            sb.append(this.months).append('M');
        }
        if (this.days != 0) {
            sb.append(this.days).append('D');
        }
        return sb.toString();
    }

    private Object writeReplace() {
        return new Ser((byte) 14, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.years);
        dataOutput.writeInt(this.months);
        dataOutput.writeInt(this.days);
    }

    static Period readExternal(DataInput dataInput) throws IOException {
        return of(dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
    }
}
