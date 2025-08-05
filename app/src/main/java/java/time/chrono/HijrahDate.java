package java.time.chrono;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

/* loaded from: rt.jar:java/time/chrono/HijrahDate.class */
public final class HijrahDate extends ChronoLocalDateImpl<HijrahDate> implements ChronoLocalDate, Serializable {
    private static final long serialVersionUID = -5207853542612002020L;
    private final transient HijrahChronology chrono;
    private final transient int prolepticYear;
    private final transient int monthOfYear;
    private final transient int dayOfMonth;

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    static HijrahDate of(HijrahChronology hijrahChronology, int i2, int i3, int i4) {
        return new HijrahDate(hijrahChronology, i2, i3, i4);
    }

    static HijrahDate ofEpochDay(HijrahChronology hijrahChronology, long j2) {
        return new HijrahDate(hijrahChronology, j2);
    }

    public static HijrahDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static HijrahDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static HijrahDate now(Clock clock) {
        return ofEpochDay(HijrahChronology.INSTANCE, LocalDate.now(clock).toEpochDay());
    }

    public static HijrahDate of(int i2, int i3, int i4) {
        return HijrahChronology.INSTANCE.date(i2, i3, i4);
    }

    public static HijrahDate from(TemporalAccessor temporalAccessor) {
        return HijrahChronology.INSTANCE.date(temporalAccessor);
    }

    private HijrahDate(HijrahChronology hijrahChronology, int i2, int i3, int i4) {
        hijrahChronology.getEpochDay(i2, i3, i4);
        this.chrono = hijrahChronology;
        this.prolepticYear = i2;
        this.monthOfYear = i3;
        this.dayOfMonth = i4;
    }

    private HijrahDate(HijrahChronology hijrahChronology, long j2) {
        int[] hijrahDateInfo = hijrahChronology.getHijrahDateInfo((int) j2);
        this.chrono = hijrahChronology;
        this.prolepticYear = hijrahDateInfo[0];
        this.monthOfYear = hijrahDateInfo[1];
        this.dayOfMonth = hijrahDateInfo[2];
    }

    @Override // java.time.chrono.ChronoLocalDate
    public HijrahChronology getChronology() {
        return this.chrono;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public HijrahEra getEra() {
        return HijrahEra.AH;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfMonth() {
        return this.chrono.getMonthLength(this.prolepticYear, this.monthOfYear);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfYear() {
        return this.chrono.getYearLength(this.prolepticYear);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (isSupported(temporalField)) {
                ChronoField chronoField = (ChronoField) temporalField;
                switch (chronoField) {
                    case DAY_OF_MONTH:
                        return ValueRange.of(1L, lengthOfMonth());
                    case DAY_OF_YEAR:
                        return ValueRange.of(1L, lengthOfYear());
                    case ALIGNED_WEEK_OF_MONTH:
                        return ValueRange.of(1L, 5L);
                    default:
                        return getChronology().range(chronoField);
                }
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
        }
        return temporalField.rangeRefinedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    public long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case DAY_OF_MONTH:
                    return this.dayOfMonth;
                case DAY_OF_YEAR:
                    return getDayOfYear();
                case ALIGNED_WEEK_OF_MONTH:
                    return ((this.dayOfMonth - 1) / 7) + 1;
                case DAY_OF_WEEK:
                    return getDayOfWeek();
                case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                    return ((getDayOfWeek() - 1) % 7) + 1;
                case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                    return ((getDayOfYear() - 1) % 7) + 1;
                case EPOCH_DAY:
                    return toEpochDay();
                case ALIGNED_WEEK_OF_YEAR:
                    return ((getDayOfYear() - 1) / 7) + 1;
                case MONTH_OF_YEAR:
                    return this.monthOfYear;
                case PROLEPTIC_MONTH:
                    return getProlepticMonth();
                case YEAR_OF_ERA:
                    return this.prolepticYear;
                case YEAR:
                    return this.prolepticYear;
                case ERA:
                    return getEraValue();
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return temporalField.getFrom(this);
    }

    private long getProlepticMonth() {
        return ((this.prolepticYear * 12) + this.monthOfYear) - 1;
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public HijrahDate with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            this.chrono.range(chronoField).checkValidValue(j2, chronoField);
            int i2 = (int) j2;
            switch (chronoField) {
                case DAY_OF_MONTH:
                    return resolvePreviousValid(this.prolepticYear, this.monthOfYear, i2);
                case DAY_OF_YEAR:
                    return plusDays(Math.min(i2, lengthOfYear()) - getDayOfYear());
                case ALIGNED_WEEK_OF_MONTH:
                    return plusDays((j2 - getLong(ChronoField.ALIGNED_WEEK_OF_MONTH)) * 7);
                case DAY_OF_WEEK:
                    return plusDays(j2 - getDayOfWeek());
                case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                    return plusDays(j2 - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
                case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                    return plusDays(j2 - getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
                case EPOCH_DAY:
                    return new HijrahDate(this.chrono, j2);
                case ALIGNED_WEEK_OF_YEAR:
                    return plusDays((j2 - getLong(ChronoField.ALIGNED_WEEK_OF_YEAR)) * 7);
                case MONTH_OF_YEAR:
                    return resolvePreviousValid(this.prolepticYear, i2, this.dayOfMonth);
                case PROLEPTIC_MONTH:
                    return plusMonths(j2 - getProlepticMonth());
                case YEAR_OF_ERA:
                    return resolvePreviousValid(this.prolepticYear >= 1 ? i2 : 1 - i2, this.monthOfYear, this.dayOfMonth);
                case YEAR:
                    return resolvePreviousValid(i2, this.monthOfYear, this.dayOfMonth);
                case ERA:
                    return resolvePreviousValid(1 - this.prolepticYear, this.monthOfYear, this.dayOfMonth);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
            }
        }
        return (HijrahDate) super.with(temporalField, j2);
    }

    private HijrahDate resolvePreviousValid(int i2, int i3, int i4) {
        int monthLength = this.chrono.getMonthLength(i2, i3);
        if (i4 > monthLength) {
            i4 = monthLength;
        }
        return of(this.chrono, i2, i3, i4);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public HijrahDate with(TemporalAdjuster temporalAdjuster) {
        return (HijrahDate) super.with(temporalAdjuster);
    }

    public HijrahDate withVariant(HijrahChronology hijrahChronology) {
        if (this.chrono == hijrahChronology) {
            return this;
        }
        int dayOfYear = hijrahChronology.getDayOfYear(this.prolepticYear, this.monthOfYear);
        return of(hijrahChronology, this.prolepticYear, this.monthOfYear, this.dayOfMonth > dayOfYear ? dayOfYear : this.dayOfMonth);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public HijrahDate plus(TemporalAmount temporalAmount) {
        return (HijrahDate) super.plus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public HijrahDate minus(TemporalAmount temporalAmount) {
        return (HijrahDate) super.minus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public long toEpochDay() {
        return this.chrono.getEpochDay(this.prolepticYear, this.monthOfYear, this.dayOfMonth);
    }

    private int getDayOfYear() {
        return this.chrono.getDayOfYear(this.prolepticYear, this.monthOfYear) + this.dayOfMonth;
    }

    private int getDayOfWeek() {
        return ((int) Math.floorMod(toEpochDay() + 3, 7L)) + 1;
    }

    private int getEraValue() {
        return this.prolepticYear > 1 ? 1 : 0;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean isLeapYear() {
        return this.chrono.isLeapYear(this.prolepticYear);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate plusYears(long j2) {
        if (j2 == 0) {
            return this;
        }
        return resolvePreviousValid(Math.addExact(this.prolepticYear, (int) j2), this.monthOfYear, this.dayOfMonth);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate plusMonths(long j2) {
        if (j2 == 0) {
            return this;
        }
        long j3 = (this.prolepticYear * 12) + (this.monthOfYear - 1) + j2;
        return resolvePreviousValid(this.chrono.checkValidYear(Math.floorDiv(j3, 12L)), ((int) Math.floorMod(j3, 12L)) + 1, this.dayOfMonth);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate plusWeeks(long j2) {
        return (HijrahDate) super.plusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate plusDays(long j2) {
        return new HijrahDate(this.chrono, toEpochDay() + j2);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public HijrahDate plus(long j2, TemporalUnit temporalUnit) {
        return (HijrahDate) super.plus(j2, temporalUnit);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public HijrahDate minus(long j2, TemporalUnit temporalUnit) {
        return (HijrahDate) super.minus(j2, temporalUnit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate minusYears(long j2) {
        return (HijrahDate) super.minusYears(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate minusMonths(long j2) {
        return (HijrahDate) super.minusMonths(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate minusWeeks(long j2) {
        return (HijrahDate) super.minusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public HijrahDate minusDays(long j2) {
        return (HijrahDate) super.minusDays(j2);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public final ChronoLocalDateTime<HijrahDate> atTime(LocalTime localTime) {
        return super.atTime(localTime);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        HijrahDate hijrahDateDate = getChronology().date((TemporalAccessor) chronoLocalDate);
        long j2 = ((hijrahDateDate.prolepticYear - this.prolepticYear) * 12) + (hijrahDateDate.monthOfYear - this.monthOfYear);
        int iLengthOfMonth = hijrahDateDate.dayOfMonth - this.dayOfMonth;
        if (j2 > 0 && iLengthOfMonth < 0) {
            j2--;
            iLengthOfMonth = (int) (hijrahDateDate.toEpochDay() - plusMonths(j2).toEpochDay());
        } else if (j2 < 0 && iLengthOfMonth > 0) {
            j2++;
            iLengthOfMonth -= hijrahDateDate.lengthOfMonth();
        }
        return getChronology().period(Math.toIntExact(j2 / 12), (int) (j2 % 12), iLengthOfMonth);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof HijrahDate) {
            HijrahDate hijrahDate = (HijrahDate) obj;
            return this.prolepticYear == hijrahDate.prolepticYear && this.monthOfYear == hijrahDate.monthOfYear && this.dayOfMonth == hijrahDate.dayOfMonth && getChronology().equals(hijrahDate.getChronology());
        }
        return false;
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public int hashCode() {
        int i2 = this.prolepticYear;
        int i3 = this.monthOfYear;
        return (getChronology().getId().hashCode() ^ (i2 & (-2048))) ^ (((i2 << 11) + (i3 << 6)) + this.dayOfMonth);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 6, this);
    }

    void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(getChronology());
        objectOutput.writeInt(get(ChronoField.YEAR));
        objectOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        objectOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static HijrahDate readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return ((HijrahChronology) objectInput.readObject()).date(objectInput.readInt(), (int) objectInput.readByte(), (int) objectInput.readByte());
    }
}
