package java.time.chrono;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
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
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/MinguoDate.class */
public final class MinguoDate extends ChronoLocalDateImpl<MinguoDate> implements ChronoLocalDate, Serializable {
    private static final long serialVersionUID = 1300372329181994526L;
    private final transient LocalDate isoDate;

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static MinguoDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static MinguoDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static MinguoDate now(Clock clock) {
        return new MinguoDate(LocalDate.now(clock));
    }

    public static MinguoDate of(int i2, int i3, int i4) {
        return new MinguoDate(LocalDate.of(i2 + 1911, i3, i4));
    }

    public static MinguoDate from(TemporalAccessor temporalAccessor) {
        return MinguoChronology.INSTANCE.date(temporalAccessor);
    }

    MinguoDate(LocalDate localDate) {
        Objects.requireNonNull(localDate, "isoDate");
        this.isoDate = localDate;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public MinguoChronology getChronology() {
        return MinguoChronology.INSTANCE;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public MinguoEra getEra() {
        return getProlepticYear() >= 1 ? MinguoEra.ROC : MinguoEra.BEFORE_ROC;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (isSupported(temporalField)) {
                ChronoField chronoField = (ChronoField) temporalField;
                switch (chronoField) {
                    case DAY_OF_MONTH:
                    case DAY_OF_YEAR:
                    case ALIGNED_WEEK_OF_MONTH:
                        return this.isoDate.range(temporalField);
                    case YEAR_OF_ERA:
                        ValueRange valueRangeRange = ChronoField.YEAR.range();
                        return ValueRange.of(1L, getProlepticYear() <= 0 ? (-valueRangeRange.getMinimum()) + 1 + 1911 : valueRangeRange.getMaximum() - 1911);
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
                case YEAR_OF_ERA:
                    return getProlepticYear() >= 1 ? r0 : 1 - r0;
                case PROLEPTIC_MONTH:
                    return getProlepticMonth();
                case YEAR:
                    return getProlepticYear();
                case ERA:
                    return getProlepticYear() >= 1 ? 1 : 0;
                default:
                    return this.isoDate.getLong(temporalField);
            }
        }
        return temporalField.getFrom(this);
    }

    private long getProlepticMonth() {
        return ((getProlepticYear() * 12) + this.isoDate.getMonthValue()) - 1;
    }

    private int getProlepticYear() {
        return this.isoDate.getYear() - 1911;
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public MinguoDate with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            if (getLong(chronoField) == j2) {
                return this;
            }
            switch (chronoField) {
                case YEAR_OF_ERA:
                case YEAR:
                case ERA:
                    int iCheckValidIntValue = getChronology().range(chronoField).checkValidIntValue(j2, chronoField);
                    switch (chronoField) {
                        case YEAR_OF_ERA:
                            return with(this.isoDate.withYear(getProlepticYear() >= 1 ? iCheckValidIntValue + 1911 : (1 - iCheckValidIntValue) + 1911));
                        case YEAR:
                            return with(this.isoDate.withYear(iCheckValidIntValue + 1911));
                        case ERA:
                            return with(this.isoDate.withYear((1 - getProlepticYear()) + 1911));
                    }
                case PROLEPTIC_MONTH:
                    getChronology().range(chronoField).checkValidValue(j2, chronoField);
                    return plusMonths(j2 - getProlepticMonth());
            }
            return with(this.isoDate.with(temporalField, j2));
        }
        return (MinguoDate) super.with(temporalField, j2);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public MinguoDate with(TemporalAdjuster temporalAdjuster) {
        return (MinguoDate) super.with(temporalAdjuster);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public MinguoDate plus(TemporalAmount temporalAmount) {
        return (MinguoDate) super.plus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public MinguoDate minus(TemporalAmount temporalAmount) {
        return (MinguoDate) super.minus(temporalAmount);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate plusYears(long j2) {
        return with(this.isoDate.plusYears(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate plusMonths(long j2) {
        return with(this.isoDate.plusMonths(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate plusWeeks(long j2) {
        return (MinguoDate) super.plusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate plusDays(long j2) {
        return with(this.isoDate.plusDays(j2));
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public MinguoDate plus(long j2, TemporalUnit temporalUnit) {
        return (MinguoDate) super.plus(j2, temporalUnit);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public MinguoDate minus(long j2, TemporalUnit temporalUnit) {
        return (MinguoDate) super.minus(j2, temporalUnit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate minusYears(long j2) {
        return (MinguoDate) super.minusYears(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate minusMonths(long j2) {
        return (MinguoDate) super.minusMonths(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate minusWeeks(long j2) {
        return (MinguoDate) super.minusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public MinguoDate minusDays(long j2) {
        return (MinguoDate) super.minusDays(j2);
    }

    private MinguoDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new MinguoDate(localDate);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public final ChronoLocalDateTime<MinguoDate> atTime(LocalTime localTime) {
        return super.atTime(localTime);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public ChronoPeriod until(ChronoLocalDate chronoLocalDate) {
        Period periodUntil = this.isoDate.until(chronoLocalDate);
        return getChronology().period(periodUntil.getYears(), periodUntil.getMonths(), periodUntil.getDays());
    }

    @Override // java.time.chrono.ChronoLocalDate
    public long toEpochDay() {
        return this.isoDate.toEpochDay();
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MinguoDate) {
            return this.isoDate.equals(((MinguoDate) obj).isoDate);
        }
        return false;
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public int hashCode() {
        return getChronology().getId().hashCode() ^ this.isoDate.hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    private Object writeReplace() {
        return new Ser((byte) 7, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static MinguoDate readExternal(DataInput dataInput) throws IOException {
        return MinguoChronology.INSTANCE.date(dataInput.readInt(), (int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
