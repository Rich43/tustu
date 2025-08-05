package java.time.chrono;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/ChronoPeriodImpl.class */
final class ChronoPeriodImpl implements ChronoPeriod, Serializable {
    private static final long serialVersionUID = 57387258289L;
    private static final List<TemporalUnit> SUPPORTED_UNITS = Collections.unmodifiableList(Arrays.asList(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS));
    private final Chronology chrono;
    final int years;
    final int months;
    final int days;

    ChronoPeriodImpl(Chronology chronology, int i2, int i3, int i4) {
        Objects.requireNonNull(chronology, "chrono");
        this.chrono = chronology;
        this.years = i2;
        this.months = i3;
        this.days = i4;
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public long get(TemporalUnit temporalUnit) {
        if (temporalUnit == ChronoUnit.YEARS) {
            return this.years;
        }
        if (temporalUnit == ChronoUnit.MONTHS) {
            return this.months;
        }
        if (temporalUnit == ChronoUnit.DAYS) {
            return this.days;
        }
        throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public List<TemporalUnit> getUnits() {
        return SUPPORTED_UNITS;
    }

    @Override // java.time.chrono.ChronoPeriod
    public Chronology getChronology() {
        return this.chrono;
    }

    @Override // java.time.chrono.ChronoPeriod
    public boolean isZero() {
        return this.years == 0 && this.months == 0 && this.days == 0;
    }

    @Override // java.time.chrono.ChronoPeriod
    public boolean isNegative() {
        return this.years < 0 || this.months < 0 || this.days < 0;
    }

    @Override // java.time.chrono.ChronoPeriod
    public ChronoPeriod plus(TemporalAmount temporalAmount) {
        ChronoPeriodImpl chronoPeriodImplValidateAmount = validateAmount(temporalAmount);
        return new ChronoPeriodImpl(this.chrono, Math.addExact(this.years, chronoPeriodImplValidateAmount.years), Math.addExact(this.months, chronoPeriodImplValidateAmount.months), Math.addExact(this.days, chronoPeriodImplValidateAmount.days));
    }

    @Override // java.time.chrono.ChronoPeriod
    public ChronoPeriod minus(TemporalAmount temporalAmount) {
        ChronoPeriodImpl chronoPeriodImplValidateAmount = validateAmount(temporalAmount);
        return new ChronoPeriodImpl(this.chrono, Math.subtractExact(this.years, chronoPeriodImplValidateAmount.years), Math.subtractExact(this.months, chronoPeriodImplValidateAmount.months), Math.subtractExact(this.days, chronoPeriodImplValidateAmount.days));
    }

    private ChronoPeriodImpl validateAmount(TemporalAmount temporalAmount) {
        Objects.requireNonNull(temporalAmount, Constants.ATTRNAME_AMOUNT);
        if (!(temporalAmount instanceof ChronoPeriodImpl)) {
            throw new DateTimeException("Unable to obtain ChronoPeriod from TemporalAmount: " + ((Object) temporalAmount.getClass()));
        }
        ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) temporalAmount;
        if (!this.chrono.equals(chronoPeriodImpl.getChronology())) {
            throw new ClassCastException("Chronology mismatch, expected: " + this.chrono.getId() + ", actual: " + chronoPeriodImpl.getChronology().getId());
        }
        return chronoPeriodImpl;
    }

    @Override // java.time.chrono.ChronoPeriod
    public ChronoPeriod multipliedBy(int i2) {
        if (isZero() || i2 == 1) {
            return this;
        }
        return new ChronoPeriodImpl(this.chrono, Math.multiplyExact(this.years, i2), Math.multiplyExact(this.months, i2), Math.multiplyExact(this.days, i2));
    }

    @Override // java.time.chrono.ChronoPeriod
    public ChronoPeriod normalized() {
        long jMonthRange = monthRange();
        if (jMonthRange > 0) {
            long j2 = (this.years * jMonthRange) + this.months;
            long j3 = j2 / jMonthRange;
            int i2 = (int) (j2 % jMonthRange);
            if (j3 == this.years && i2 == this.months) {
                return this;
            }
            return new ChronoPeriodImpl(this.chrono, Math.toIntExact(j3), i2, this.days);
        }
        return this;
    }

    private long monthRange() {
        ValueRange valueRangeRange = this.chrono.range(ChronoField.MONTH_OF_YEAR);
        if (valueRangeRange.isFixed() && valueRangeRange.isIntValue()) {
            return (valueRangeRange.getMaximum() - valueRangeRange.getMinimum()) + 1;
        }
        return -1L;
    }

    @Override // java.time.chrono.ChronoPeriod, java.time.temporal.TemporalAmount
    public Temporal addTo(Temporal temporal) {
        validateChrono(temporal);
        if (this.months == 0) {
            if (this.years != 0) {
                temporal = temporal.plus(this.years, ChronoUnit.YEARS);
            }
        } else {
            long jMonthRange = monthRange();
            if (jMonthRange > 0) {
                temporal = temporal.plus((this.years * jMonthRange) + this.months, ChronoUnit.MONTHS);
            } else {
                if (this.years != 0) {
                    temporal = temporal.plus(this.years, ChronoUnit.YEARS);
                }
                temporal = temporal.plus(this.months, ChronoUnit.MONTHS);
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
            long jMonthRange = monthRange();
            if (jMonthRange > 0) {
                temporal = temporal.minus((this.years * jMonthRange) + this.months, ChronoUnit.MONTHS);
            } else {
                if (this.years != 0) {
                    temporal = temporal.minus(this.years, ChronoUnit.YEARS);
                }
                temporal = temporal.minus(this.months, ChronoUnit.MONTHS);
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
        if (chronology != null && !this.chrono.equals(chronology)) {
            throw new DateTimeException("Chronology mismatch, expected: " + this.chrono.getId() + ", actual: " + chronology.getId());
        }
    }

    @Override // java.time.chrono.ChronoPeriod
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ChronoPeriodImpl) {
            ChronoPeriodImpl chronoPeriodImpl = (ChronoPeriodImpl) obj;
            return this.years == chronoPeriodImpl.years && this.months == chronoPeriodImpl.months && this.days == chronoPeriodImpl.days && this.chrono.equals(chronoPeriodImpl.chrono);
        }
        return false;
    }

    @Override // java.time.chrono.ChronoPeriod
    public int hashCode() {
        return ((this.years + Integer.rotateLeft(this.months, 8)) + Integer.rotateLeft(this.days, 16)) ^ this.chrono.hashCode();
    }

    @Override // java.time.chrono.ChronoPeriod
    public String toString() {
        if (isZero()) {
            return getChronology().toString() + " P0D";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getChronology().toString()).append(' ').append('P');
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

    protected Object writeReplace() {
        return new Ser((byte) 9, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ObjectStreamException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.chrono.getId());
        dataOutput.writeInt(this.years);
        dataOutput.writeInt(this.months);
        dataOutput.writeInt(this.days);
    }

    static ChronoPeriodImpl readExternal(DataInput dataInput) throws IOException {
        return new ChronoPeriodImpl(Chronology.of(dataInput.readUTF()), dataInput.readInt(), dataInput.readInt(), dataInput.readInt());
    }
}
