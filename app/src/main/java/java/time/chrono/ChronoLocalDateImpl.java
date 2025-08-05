package java.time.chrono;

import java.io.Serializable;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Objects;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/time/chrono/ChronoLocalDateImpl.class */
abstract class ChronoLocalDateImpl<D extends ChronoLocalDate> implements ChronoLocalDate, Temporal, TemporalAdjuster, Serializable {
    private static final long serialVersionUID = 6282433883239719096L;

    abstract D plusYears(long j2);

    abstract D plusMonths(long j2);

    abstract D plusDays(long j2);

    static <D extends ChronoLocalDate> D ensureValid(Chronology chronology, Temporal temporal) {
        D d2 = (D) temporal;
        if (!chronology.equals(d2.getChronology())) {
            throw new ClassCastException("Chronology mismatch, expected: " + chronology.getId() + ", actual: " + d2.getChronology().getId());
        }
        return d2;
    }

    ChronoLocalDateImpl() {
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public D with(TemporalAdjuster temporalAdjuster) {
        return (D) super.with(temporalAdjuster);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public D with(TemporalField temporalField, long j2) {
        return (D) super.with(temporalField, j2);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public D plus(TemporalAmount temporalAmount) {
        return (D) super.plus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public D plus(long j2, TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case DAYS:
                    return (D) plusDays(j2);
                case WEEKS:
                    return (D) plusDays(Math.multiplyExact(j2, 7L));
                case MONTHS:
                    return (D) plusMonths(j2);
                case YEARS:
                    return (D) plusYears(j2);
                case DECADES:
                    return (D) plusYears(Math.multiplyExact(j2, 10L));
                case CENTURIES:
                    return (D) plusYears(Math.multiplyExact(j2, 100L));
                case MILLENNIA:
                    return (D) plusYears(Math.multiplyExact(j2, 1000L));
                case ERAS:
                    return (D) with((TemporalField) ChronoField.ERA, Math.addExact(getLong(ChronoField.ERA), j2));
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        return (D) super.plus(j2, temporalUnit);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public D minus(TemporalAmount temporalAmount) {
        return (D) super.minus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public D minus(long j2, TemporalUnit temporalUnit) {
        return (D) super.minus(j2, temporalUnit);
    }

    D plusWeeks(long j2) {
        return (D) plusDays(Math.multiplyExact(j2, 7L));
    }

    D minusYears(long j2) {
        return j2 == Long.MIN_VALUE ? (D) ((ChronoLocalDateImpl) plusYears(Long.MAX_VALUE)).plusYears(1L) : (D) plusYears(-j2);
    }

    D minusMonths(long j2) {
        return j2 == Long.MIN_VALUE ? (D) ((ChronoLocalDateImpl) plusMonths(Long.MAX_VALUE)).plusMonths(1L) : (D) plusMonths(-j2);
    }

    D minusWeeks(long j2) {
        return j2 == Long.MIN_VALUE ? (D) ((ChronoLocalDateImpl) plusWeeks(Long.MAX_VALUE)).plusWeeks(1L) : (D) plusWeeks(-j2);
    }

    D minusDays(long j2) {
        return j2 == Long.MIN_VALUE ? (D) ((ChronoLocalDateImpl) plusDays(Long.MAX_VALUE)).plusDays(1L) : (D) plusDays(-j2);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public long until(Temporal temporal, TemporalUnit temporalUnit) {
        Objects.requireNonNull(temporal, "endExclusive");
        ChronoLocalDate chronoLocalDateDate = getChronology().date(temporal);
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case DAYS:
                    return daysUntil(chronoLocalDateDate);
                case WEEKS:
                    return daysUntil(chronoLocalDateDate) / 7;
                case MONTHS:
                    return monthsUntil(chronoLocalDateDate);
                case YEARS:
                    return monthsUntil(chronoLocalDateDate) / 12;
                case DECADES:
                    return monthsUntil(chronoLocalDateDate) / 120;
                case CENTURIES:
                    return monthsUntil(chronoLocalDateDate) / 1200;
                case MILLENNIA:
                    return monthsUntil(chronoLocalDateDate) / 12000;
                case ERAS:
                    return chronoLocalDateDate.getLong(ChronoField.ERA) - getLong(ChronoField.ERA);
                default:
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + ((Object) temporalUnit));
            }
        }
        Objects.requireNonNull(temporalUnit, "unit");
        return temporalUnit.between(this, chronoLocalDateDate);
    }

    private long daysUntil(ChronoLocalDate chronoLocalDate) {
        return chronoLocalDate.toEpochDay() - toEpochDay();
    }

    private long monthsUntil(ChronoLocalDate chronoLocalDate) {
        if (getChronology().range(ChronoField.MONTH_OF_YEAR).getMaximum() != 12) {
            throw new IllegalStateException("ChronoLocalDateImpl only supports Chronologies with 12 months per year");
        }
        return (((chronoLocalDate.getLong(ChronoField.PROLEPTIC_MONTH) * 32) + chronoLocalDate.get(ChronoField.DAY_OF_MONTH)) - ((getLong(ChronoField.PROLEPTIC_MONTH) * 32) + get(ChronoField.DAY_OF_MONTH))) / 32;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ChronoLocalDate) && compareTo((ChronoLocalDate) obj) == 0;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int hashCode() {
        long epochDay = toEpochDay();
        return getChronology().hashCode() ^ ((int) (epochDay ^ (epochDay >>> 32)));
    }

    @Override // java.time.chrono.ChronoLocalDate
    public String toString() {
        long j2 = getLong(ChronoField.YEAR_OF_ERA);
        long j3 = getLong(ChronoField.MONTH_OF_YEAR);
        long j4 = getLong(ChronoField.DAY_OF_MONTH);
        StringBuilder sb = new StringBuilder(30);
        sb.append(getChronology().toString()).append(" ").append((Object) getEra()).append(" ").append(j2).append(j3 < 10 ? "-0" : LanguageTag.SEP).append(j3).append(j4 < 10 ? "-0" : LanguageTag.SEP).append(j4);
        return sb.toString();
    }
}
