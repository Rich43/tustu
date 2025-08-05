package java.time.chrono;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:java/time/chrono/MinguoChronology.class */
public final class MinguoChronology extends AbstractChronology implements Serializable {
    public static final MinguoChronology INSTANCE = new MinguoChronology();
    private static final long serialVersionUID = 1039765215346859963L;
    static final int YEARS_DIFFERENCE = 1911;

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public /* bridge */ /* synthetic */ ChronoLocalDate resolveDate(Map map, ResolverStyle resolverStyle) {
        return resolveDate((Map<TemporalField, Long>) map, resolverStyle);
    }

    private MinguoChronology() {
    }

    @Override // java.time.chrono.Chronology
    public String getId() {
        return "Minguo";
    }

    @Override // java.time.chrono.Chronology
    public String getCalendarType() {
        return "roc";
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate date(Era era, int i2, int i3, int i4) {
        return date(prolepticYear(era, i2), i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate date(int i2, int i3, int i4) {
        return new MinguoDate(LocalDate.of(i2 + YEARS_DIFFERENCE, i3, i4));
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate dateYearDay(Era era, int i2, int i3) {
        return dateYearDay(prolepticYear(era, i2), i3);
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate dateYearDay(int i2, int i3) {
        return new MinguoDate(LocalDate.ofYearDay(i2 + YEARS_DIFFERENCE, i3));
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate dateEpochDay(long j2) {
        return new MinguoDate(LocalDate.ofEpochDay(j2));
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate dateNow(Clock clock) {
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    @Override // java.time.chrono.Chronology
    public MinguoDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof MinguoDate) {
            return (MinguoDate) temporalAccessor;
        }
        return new MinguoDate(LocalDate.from(temporalAccessor));
    }

    @Override // java.time.chrono.Chronology
    public ChronoLocalDateTime<MinguoDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<MinguoDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<MinguoDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    @Override // java.time.chrono.Chronology
    public boolean isLeapYear(long j2) {
        return IsoChronology.INSTANCE.isLeapYear(j2 + 1911);
    }

    @Override // java.time.chrono.Chronology
    public int prolepticYear(Era era, int i2) {
        if (era instanceof MinguoEra) {
            return era == MinguoEra.ROC ? i2 : 1 - i2;
        }
        throw new ClassCastException("Era must be MinguoEra");
    }

    @Override // java.time.chrono.Chronology
    public MinguoEra eraOf(int i2) {
        return MinguoEra.of(i2);
    }

    @Override // java.time.chrono.Chronology
    public List<Era> eras() {
        return Arrays.asList(MinguoEra.values());
    }

    @Override // java.time.chrono.Chronology
    public ValueRange range(ChronoField chronoField) {
        switch (chronoField) {
            case PROLEPTIC_MONTH:
                ValueRange valueRangeRange = ChronoField.PROLEPTIC_MONTH.range();
                return ValueRange.of(valueRangeRange.getMinimum() - 22932, valueRangeRange.getMaximum() - 22932);
            case YEAR_OF_ERA:
                ValueRange valueRangeRange2 = ChronoField.YEAR.range();
                return ValueRange.of(1L, valueRangeRange2.getMaximum() - 1911, (-valueRangeRange2.getMinimum()) + 1 + 1911);
            case YEAR:
                ValueRange valueRangeRange3 = ChronoField.YEAR.range();
                return ValueRange.of(valueRangeRange3.getMinimum() - 1911, valueRangeRange3.getMaximum() - 1911);
            default:
                return chronoField.range();
        }
    }

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public MinguoDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (MinguoDate) super.resolveDate(map, resolverStyle);
    }

    @Override // java.time.chrono.AbstractChronology
    Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
