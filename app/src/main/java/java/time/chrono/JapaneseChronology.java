package java.time.chrono;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.LocalGregorianCalendar;

/* loaded from: rt.jar:java/time/chrono/JapaneseChronology.class */
public final class JapaneseChronology extends AbstractChronology implements Serializable {
    static final LocalGregorianCalendar JCAL = (LocalGregorianCalendar) CalendarSystem.forName("japanese");
    static final Locale LOCALE = Locale.forLanguageTag("ja-JP-u-ca-japanese");
    public static final JapaneseChronology INSTANCE = new JapaneseChronology();
    private static final long serialVersionUID = 459996390165777884L;

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public /* bridge */ /* synthetic */ ChronoLocalDate resolveDate(Map map, ResolverStyle resolverStyle) {
        return resolveDate((Map<TemporalField, Long>) map, resolverStyle);
    }

    private JapaneseChronology() {
    }

    @Override // java.time.chrono.Chronology
    public String getId() {
        return "Japanese";
    }

    @Override // java.time.chrono.Chronology
    public String getCalendarType() {
        return "japanese";
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate date(Era era, int i2, int i3, int i4) {
        if (!(era instanceof JapaneseEra)) {
            throw new ClassCastException("Era must be JapaneseEra");
        }
        return JapaneseDate.of((JapaneseEra) era, i2, i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate date(int i2, int i3, int i4) {
        return new JapaneseDate(LocalDate.of(i2, i3, i4));
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate dateYearDay(Era era, int i2, int i3) {
        return JapaneseDate.ofYearDay((JapaneseEra) era, i2, i3);
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate dateYearDay(int i2, int i3) {
        return new JapaneseDate(LocalDate.ofYearDay(i2, i3));
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate dateEpochDay(long j2) {
        return new JapaneseDate(LocalDate.ofEpochDay(j2));
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate dateNow(Clock clock) {
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    @Override // java.time.chrono.Chronology
    public JapaneseDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof JapaneseDate) {
            return (JapaneseDate) temporalAccessor;
        }
        return new JapaneseDate(LocalDate.from(temporalAccessor));
    }

    @Override // java.time.chrono.Chronology
    public ChronoLocalDateTime<JapaneseDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<JapaneseDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<JapaneseDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    @Override // java.time.chrono.Chronology
    public boolean isLeapYear(long j2) {
        return IsoChronology.INSTANCE.isLeapYear(j2);
    }

    @Override // java.time.chrono.Chronology
    public int prolepticYear(Era era, int i2) {
        if (!(era instanceof JapaneseEra)) {
            throw new ClassCastException("Era must be JapaneseEra");
        }
        JapaneseEra japaneseEra = (JapaneseEra) era;
        int year = (japaneseEra.getPrivateEra().getSinceDate().getYear() + i2) - 1;
        if (i2 == 1) {
            return year;
        }
        if (year >= -999999999 && year <= 999999999) {
            LocalGregorianCalendar.Date dateNewCalendarDate = JCAL.newCalendarDate((TimeZone) null);
            dateNewCalendarDate.setEra(japaneseEra.getPrivateEra()).setDate(i2, 1, 1);
            if (JCAL.validate(dateNewCalendarDate)) {
                return year;
            }
        }
        throw new DateTimeException("Invalid yearOfEra value");
    }

    @Override // java.time.chrono.Chronology
    public JapaneseEra eraOf(int i2) {
        return JapaneseEra.of(i2);
    }

    @Override // java.time.chrono.Chronology
    public List<Era> eras() {
        return Arrays.asList(JapaneseEra.values());
    }

    JapaneseEra getCurrentEra() {
        JapaneseEra[] japaneseEraArrValues = JapaneseEra.values();
        return japaneseEraArrValues[japaneseEraArrValues.length - 1];
    }

    @Override // java.time.chrono.Chronology
    public ValueRange range(ChronoField chronoField) {
        switch (chronoField) {
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
            case ALIGNED_WEEK_OF_MONTH:
            case ALIGNED_WEEK_OF_YEAR:
                throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) chronoField));
            case YEAR_OF_ERA:
                Calendar calendar = Calendar.getInstance(LOCALE);
                return ValueRange.of(1L, calendar.getGreatestMinimum(1), calendar.getLeastMaximum(1) + 1, Year.MAX_VALUE - getCurrentEra().getPrivateEra().getSinceDate().getYear());
            case DAY_OF_YEAR:
                Calendar calendar2 = Calendar.getInstance(LOCALE);
                return ValueRange.of(calendar2.getMinimum(6), calendar2.getGreatestMinimum(6), calendar2.getLeastMaximum(6), calendar2.getMaximum(6));
            case YEAR:
                return ValueRange.of(JapaneseDate.MEIJI_6_ISODATE.getYear(), 999999999L);
            case ERA:
                return ValueRange.of(JapaneseEra.MEIJI.getValue(), getCurrentEra().getValue());
            default:
                return chronoField.range();
        }
    }

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public JapaneseDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (JapaneseDate) super.resolveDate(map, resolverStyle);
    }

    @Override // java.time.chrono.AbstractChronology
    ChronoLocalDate resolveYearOfEra(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        Long l2 = map.get(ChronoField.ERA);
        JapaneseEra japaneseEraEraOf = null;
        if (l2 != null) {
            japaneseEraEraOf = eraOf(range(ChronoField.ERA).checkValidIntValue(l2.longValue(), ChronoField.ERA));
        }
        Long l3 = map.get(ChronoField.YEAR_OF_ERA);
        int iCheckValidIntValue = 0;
        if (l3 != null) {
            iCheckValidIntValue = range(ChronoField.YEAR_OF_ERA).checkValidIntValue(l3.longValue(), ChronoField.YEAR_OF_ERA);
        }
        if (japaneseEraEraOf == null && l3 != null && !map.containsKey(ChronoField.YEAR) && resolverStyle != ResolverStyle.STRICT) {
            japaneseEraEraOf = JapaneseEra.values()[JapaneseEra.values().length - 1];
        }
        if (l3 != null && japaneseEraEraOf != null) {
            if (map.containsKey(ChronoField.MONTH_OF_YEAR) && map.containsKey(ChronoField.DAY_OF_MONTH)) {
                return resolveYMD(japaneseEraEraOf, iCheckValidIntValue, map, resolverStyle);
            }
            if (map.containsKey(ChronoField.DAY_OF_YEAR)) {
                return resolveYD(japaneseEraEraOf, iCheckValidIntValue, map, resolverStyle);
            }
            return null;
        }
        return null;
    }

    private int prolepticYearLenient(JapaneseEra japaneseEra, int i2) {
        return (japaneseEra.getPrivateEra().getSinceDate().getYear() + i2) - 1;
    }

    private ChronoLocalDate resolveYMD(JapaneseEra japaneseEra, int i2, Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        JapaneseDate japaneseDateWith;
        map.remove(ChronoField.ERA);
        map.remove(ChronoField.YEAR_OF_ERA);
        if (resolverStyle == ResolverStyle.LENIENT) {
            int iProlepticYearLenient = prolepticYearLenient(japaneseEra, i2);
            return date(iProlepticYearLenient, 1, 1).plus(Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), 1L), (TemporalUnit) ChronoUnit.MONTHS).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH).longValue(), 1L), (TemporalUnit) ChronoUnit.DAYS);
        }
        int iCheckValidIntValue = range(ChronoField.MONTH_OF_YEAR).checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), ChronoField.MONTH_OF_YEAR);
        int iCheckValidIntValue2 = range(ChronoField.DAY_OF_MONTH).checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH).longValue(), ChronoField.DAY_OF_MONTH);
        if (resolverStyle == ResolverStyle.SMART) {
            if (i2 < 1) {
                throw new DateTimeException("Invalid YearOfEra: " + i2);
            }
            int iProlepticYearLenient2 = prolepticYearLenient(japaneseEra, i2);
            try {
                japaneseDateWith = date(iProlepticYearLenient2, iCheckValidIntValue, iCheckValidIntValue2);
            } catch (DateTimeException e2) {
                japaneseDateWith = date(iProlepticYearLenient2, iCheckValidIntValue, 1).with(TemporalAdjusters.lastDayOfMonth());
            }
            if (japaneseDateWith.getEra() != japaneseEra && japaneseDateWith.get(ChronoField.YEAR_OF_ERA) > 1 && i2 > 1) {
                throw new DateTimeException("Invalid YearOfEra for Era: " + ((Object) japaneseEra) + " " + i2);
            }
            return japaneseDateWith;
        }
        return date((Era) japaneseEra, i2, iCheckValidIntValue, iCheckValidIntValue2);
    }

    private ChronoLocalDate resolveYD(JapaneseEra japaneseEra, int i2, Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        map.remove(ChronoField.ERA);
        map.remove(ChronoField.YEAR_OF_ERA);
        if (resolverStyle == ResolverStyle.LENIENT) {
            int iProlepticYearLenient = prolepticYearLenient(japaneseEra, i2);
            return dateYearDay(iProlepticYearLenient, 1).plus(Math.subtractExact(map.remove(ChronoField.DAY_OF_YEAR).longValue(), 1L), (TemporalUnit) ChronoUnit.DAYS);
        }
        return dateYearDay((Era) japaneseEra, i2, range(ChronoField.DAY_OF_YEAR).checkValidIntValue(map.remove(ChronoField.DAY_OF_YEAR).longValue(), ChronoField.DAY_OF_YEAR));
    }

    @Override // java.time.chrono.AbstractChronology
    Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
