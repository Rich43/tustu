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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:java/time/chrono/ThaiBuddhistChronology.class */
public final class ThaiBuddhistChronology extends AbstractChronology implements Serializable {
    private static final long serialVersionUID = 2775954514031616474L;
    static final int YEARS_DIFFERENCE = 543;
    private static final String FALLBACK_LANGUAGE = "en";
    private static final String TARGET_LANGUAGE = "th";
    public static final ThaiBuddhistChronology INSTANCE = new ThaiBuddhistChronology();
    private static final HashMap<String, String[]> ERA_NARROW_NAMES = new HashMap<>();
    private static final HashMap<String, String[]> ERA_SHORT_NAMES = new HashMap<>();
    private static final HashMap<String, String[]> ERA_FULL_NAMES = new HashMap<>();

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public /* bridge */ /* synthetic */ ChronoLocalDate resolveDate(Map map, ResolverStyle resolverStyle) {
        return resolveDate((Map<TemporalField, Long>) map, resolverStyle);
    }

    static {
        ERA_NARROW_NAMES.put(FALLBACK_LANGUAGE, new String[]{"BB", "BE"});
        ERA_NARROW_NAMES.put(TARGET_LANGUAGE, new String[]{"BB", "BE"});
        ERA_SHORT_NAMES.put(FALLBACK_LANGUAGE, new String[]{"B.B.", "B.E."});
        ERA_SHORT_NAMES.put(TARGET_LANGUAGE, new String[]{"พ.ศ.", "ปีก่อนคริสต์กาลที่"});
        ERA_FULL_NAMES.put(FALLBACK_LANGUAGE, new String[]{"Before Buddhist", "Budhhist Era"});
        ERA_FULL_NAMES.put(TARGET_LANGUAGE, new String[]{"พุทธศักราช", "ปีก่อนคริสต์กาลที่"});
    }

    private ThaiBuddhistChronology() {
    }

    @Override // java.time.chrono.Chronology
    public String getId() {
        return "ThaiBuddhist";
    }

    @Override // java.time.chrono.Chronology
    public String getCalendarType() {
        return "buddhist";
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate date(Era era, int i2, int i3, int i4) {
        return date(prolepticYear(era, i2), i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate date(int i2, int i3, int i4) {
        return new ThaiBuddhistDate(LocalDate.of(i2 - 543, i3, i4));
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate dateYearDay(Era era, int i2, int i3) {
        return dateYearDay(prolepticYear(era, i2), i3);
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate dateYearDay(int i2, int i3) {
        return new ThaiBuddhistDate(LocalDate.ofYearDay(i2 - 543, i3));
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate dateEpochDay(long j2) {
        return new ThaiBuddhistDate(LocalDate.ofEpochDay(j2));
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate dateNow(Clock clock) {
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ThaiBuddhistDate) {
            return (ThaiBuddhistDate) temporalAccessor;
        }
        return new ThaiBuddhistDate(LocalDate.from(temporalAccessor));
    }

    @Override // java.time.chrono.Chronology
    public ChronoLocalDateTime<ThaiBuddhistDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<ThaiBuddhistDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<ThaiBuddhistDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    @Override // java.time.chrono.Chronology
    public boolean isLeapYear(long j2) {
        return IsoChronology.INSTANCE.isLeapYear(j2 - 543);
    }

    @Override // java.time.chrono.Chronology
    public int prolepticYear(Era era, int i2) {
        if (era instanceof ThaiBuddhistEra) {
            return era == ThaiBuddhistEra.BE ? i2 : 1 - i2;
        }
        throw new ClassCastException("Era must be BuddhistEra");
    }

    @Override // java.time.chrono.Chronology
    public ThaiBuddhistEra eraOf(int i2) {
        return ThaiBuddhistEra.of(i2);
    }

    @Override // java.time.chrono.Chronology
    public List<Era> eras() {
        return Arrays.asList(ThaiBuddhistEra.values());
    }

    @Override // java.time.chrono.Chronology
    public ValueRange range(ChronoField chronoField) {
        switch (chronoField) {
            case PROLEPTIC_MONTH:
                ValueRange valueRangeRange = ChronoField.PROLEPTIC_MONTH.range();
                return ValueRange.of(valueRangeRange.getMinimum() + 6516, valueRangeRange.getMaximum() + 6516);
            case YEAR_OF_ERA:
                ValueRange valueRangeRange2 = ChronoField.YEAR.range();
                return ValueRange.of(1L, (-(valueRangeRange2.getMinimum() + 543)) + 1, valueRangeRange2.getMaximum() + 543);
            case YEAR:
                ValueRange valueRangeRange3 = ChronoField.YEAR.range();
                return ValueRange.of(valueRangeRange3.getMinimum() + 543, valueRangeRange3.getMaximum() + 543);
            default:
                return chronoField.range();
        }
    }

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public ThaiBuddhistDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (ThaiBuddhistDate) super.resolveDate(map, resolverStyle);
    }

    @Override // java.time.chrono.AbstractChronology
    Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
