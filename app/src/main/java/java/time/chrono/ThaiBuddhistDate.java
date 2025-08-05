package java.time.chrono;

import com.sun.glass.events.WindowEvent;
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

/* loaded from: rt.jar:java/time/chrono/ThaiBuddhistDate.class */
public final class ThaiBuddhistDate extends ChronoLocalDateImpl<ThaiBuddhistDate> implements ChronoLocalDate, Serializable {
    private static final long serialVersionUID = -8722293800195731463L;
    private final transient LocalDate isoDate;

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static ThaiBuddhistDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static ThaiBuddhistDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static ThaiBuddhistDate now(Clock clock) {
        return new ThaiBuddhistDate(LocalDate.now(clock));
    }

    public static ThaiBuddhistDate of(int i2, int i3, int i4) {
        return new ThaiBuddhistDate(LocalDate.of(i2 - WindowEvent.FOCUS_GAINED_FORWARD, i3, i4));
    }

    public static ThaiBuddhistDate from(TemporalAccessor temporalAccessor) {
        return ThaiBuddhistChronology.INSTANCE.date(temporalAccessor);
    }

    ThaiBuddhistDate(LocalDate localDate) {
        Objects.requireNonNull(localDate, "isoDate");
        this.isoDate = localDate;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public ThaiBuddhistChronology getChronology() {
        return ThaiBuddhistChronology.INSTANCE;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public ThaiBuddhistEra getEra() {
        return getProlepticYear() >= 1 ? ThaiBuddhistEra.BE : ThaiBuddhistEra.BEFORE_BE;
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
                        return ValueRange.of(1L, getProlepticYear() <= 0 ? (-(valueRangeRange.getMinimum() + 543)) + 1 : valueRangeRange.getMaximum() + 543);
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
        return this.isoDate.getYear() + WindowEvent.FOCUS_GAINED_FORWARD;
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public ThaiBuddhistDate with(TemporalField temporalField, long j2) {
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
                            return with(this.isoDate.withYear((getProlepticYear() >= 1 ? iCheckValidIntValue : 1 - iCheckValidIntValue) - WindowEvent.FOCUS_GAINED_FORWARD));
                        case YEAR:
                            return with(this.isoDate.withYear(iCheckValidIntValue - WindowEvent.FOCUS_GAINED_FORWARD));
                        case ERA:
                            return with(this.isoDate.withYear((1 - getProlepticYear()) - WindowEvent.FOCUS_GAINED_FORWARD));
                    }
                case PROLEPTIC_MONTH:
                    getChronology().range(chronoField).checkValidValue(j2, chronoField);
                    return plusMonths(j2 - getProlepticMonth());
            }
            return with(this.isoDate.with(temporalField, j2));
        }
        return (ThaiBuddhistDate) super.with(temporalField, j2);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public ThaiBuddhistDate with(TemporalAdjuster temporalAdjuster) {
        return (ThaiBuddhistDate) super.with(temporalAdjuster);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public ThaiBuddhistDate plus(TemporalAmount temporalAmount) {
        return (ThaiBuddhistDate) super.plus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public ThaiBuddhistDate minus(TemporalAmount temporalAmount) {
        return (ThaiBuddhistDate) super.minus(temporalAmount);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate plusYears(long j2) {
        return with(this.isoDate.plusYears(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate plusMonths(long j2) {
        return with(this.isoDate.plusMonths(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate plusWeeks(long j2) {
        return (ThaiBuddhistDate) super.plusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate plusDays(long j2) {
        return with(this.isoDate.plusDays(j2));
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public ThaiBuddhistDate plus(long j2, TemporalUnit temporalUnit) {
        return (ThaiBuddhistDate) super.plus(j2, temporalUnit);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public ThaiBuddhistDate minus(long j2, TemporalUnit temporalUnit) {
        return (ThaiBuddhistDate) super.minus(j2, temporalUnit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate minusYears(long j2) {
        return (ThaiBuddhistDate) super.minusYears(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate minusMonths(long j2) {
        return (ThaiBuddhistDate) super.minusMonths(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate minusWeeks(long j2) {
        return (ThaiBuddhistDate) super.minusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public ThaiBuddhistDate minusDays(long j2) {
        return (ThaiBuddhistDate) super.minusDays(j2);
    }

    private ThaiBuddhistDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new ThaiBuddhistDate(localDate);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public final ChronoLocalDateTime<ThaiBuddhistDate> atTime(LocalTime localTime) {
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
        if (obj instanceof ThaiBuddhistDate) {
            return this.isoDate.equals(((ThaiBuddhistDate) obj).isoDate);
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
        return new Ser((byte) 8, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static ThaiBuddhistDate readExternal(DataInput dataInput) throws IOException {
        return ThaiBuddhistChronology.INSTANCE.date(dataInput.readInt(), (int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
