package java.time.temporal;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.locale.provider.CalendarDataUtility;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:java/time/temporal/WeekFields.class */
public final class WeekFields implements Serializable {
    private static final ConcurrentMap<String, WeekFields> CACHE = new ConcurrentHashMap(4, 0.75f, 2);
    public static final WeekFields ISO = new WeekFields(DayOfWeek.MONDAY, 4);
    public static final WeekFields SUNDAY_START = of(DayOfWeek.SUNDAY, 1);
    public static final TemporalUnit WEEK_BASED_YEARS = IsoFields.WEEK_BASED_YEARS;
    private static final long serialVersionUID = -1177360819670808121L;
    private final DayOfWeek firstDayOfWeek;
    private final int minimalDays;
    private final transient TemporalField dayOfWeek = ComputedDayOfField.ofDayOfWeekField(this);
    private final transient TemporalField weekOfMonth = ComputedDayOfField.ofWeekOfMonthField(this);
    private final transient TemporalField weekOfYear = ComputedDayOfField.ofWeekOfYearField(this);
    private final transient TemporalField weekOfWeekBasedYear = ComputedDayOfField.ofWeekOfWeekBasedYearField(this);
    private final transient TemporalField weekBasedYear = ComputedDayOfField.ofWeekBasedYearField(this);

    public static WeekFields of(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        return of(DayOfWeek.SUNDAY.plus(CalendarDataUtility.retrieveFirstDayOfWeek(r0) - 1), CalendarDataUtility.retrieveMinimalDaysInFirstWeek(new Locale(locale.getLanguage(), locale.getCountry())));
    }

    public static WeekFields of(DayOfWeek dayOfWeek, int i2) {
        String str = dayOfWeek.toString() + i2;
        WeekFields weekFields = CACHE.get(str);
        if (weekFields == null) {
            CACHE.putIfAbsent(str, new WeekFields(dayOfWeek, i2));
            weekFields = CACHE.get(str);
        }
        return weekFields;
    }

    private WeekFields(DayOfWeek dayOfWeek, int i2) {
        Objects.requireNonNull(dayOfWeek, CalendarDataUtility.FIRST_DAY_OF_WEEK);
        if (i2 < 1 || i2 > 7) {
            throw new IllegalArgumentException("Minimal number of days is invalid");
        }
        this.firstDayOfWeek = dayOfWeek;
        this.minimalDays = i2;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.firstDayOfWeek == null) {
            throw new InvalidObjectException("firstDayOfWeek is null");
        }
        if (this.minimalDays < 1 || this.minimalDays > 7) {
            throw new InvalidObjectException("Minimal number of days is invalid");
        }
    }

    private Object readResolve() throws InvalidObjectException {
        try {
            return of(this.firstDayOfWeek, this.minimalDays);
        } catch (IllegalArgumentException e2) {
            throw new InvalidObjectException("Invalid serialized WeekFields: " + e2.getMessage());
        }
    }

    public DayOfWeek getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDays;
    }

    public TemporalField dayOfWeek() {
        return this.dayOfWeek;
    }

    public TemporalField weekOfMonth() {
        return this.weekOfMonth;
    }

    public TemporalField weekOfYear() {
        return this.weekOfYear;
    }

    public TemporalField weekOfWeekBasedYear() {
        return this.weekOfWeekBasedYear;
    }

    public TemporalField weekBasedYear() {
        return this.weekBasedYear;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof WeekFields) && hashCode() == obj.hashCode();
    }

    public int hashCode() {
        return (this.firstDayOfWeek.ordinal() * 7) + this.minimalDays;
    }

    public String toString() {
        return "WeekFields[" + ((Object) this.firstDayOfWeek) + ',' + this.minimalDays + ']';
    }

    /* loaded from: rt.jar:java/time/temporal/WeekFields$ComputedDayOfField.class */
    static class ComputedDayOfField implements TemporalField {
        private final String name;
        private final WeekFields weekDef;
        private final TemporalUnit baseUnit;
        private final TemporalUnit rangeUnit;
        private final ValueRange range;
        private static final ValueRange DAY_OF_WEEK_RANGE = ValueRange.of(1, 7);
        private static final ValueRange WEEK_OF_MONTH_RANGE = ValueRange.of(0, 1, 4, 6);
        private static final ValueRange WEEK_OF_YEAR_RANGE = ValueRange.of(0, 1, 52, 54);
        private static final ValueRange WEEK_OF_WEEK_BASED_YEAR_RANGE = ValueRange.of(1, 52, 53);

        @Override // java.time.temporal.TemporalField
        public /* bridge */ /* synthetic */ TemporalAccessor resolve(Map map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            return resolve((Map<TemporalField, Long>) map, temporalAccessor, resolverStyle);
        }

        static ComputedDayOfField ofDayOfWeekField(WeekFields weekFields) {
            return new ComputedDayOfField("DayOfWeek", weekFields, ChronoUnit.DAYS, ChronoUnit.WEEKS, DAY_OF_WEEK_RANGE);
        }

        static ComputedDayOfField ofWeekOfMonthField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfMonth", weekFields, ChronoUnit.WEEKS, ChronoUnit.MONTHS, WEEK_OF_MONTH_RANGE);
        }

        static ComputedDayOfField ofWeekOfYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfYear", weekFields, ChronoUnit.WEEKS, ChronoUnit.YEARS, WEEK_OF_YEAR_RANGE);
        }

        static ComputedDayOfField ofWeekOfWeekBasedYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekOfWeekBasedYear", weekFields, ChronoUnit.WEEKS, IsoFields.WEEK_BASED_YEARS, WEEK_OF_WEEK_BASED_YEAR_RANGE);
        }

        static ComputedDayOfField ofWeekBasedYearField(WeekFields weekFields) {
            return new ComputedDayOfField("WeekBasedYear", weekFields, IsoFields.WEEK_BASED_YEARS, ChronoUnit.FOREVER, ChronoField.YEAR.range());
        }

        private ChronoLocalDate ofWeekBasedYear(Chronology chronology, int i2, int i3, int i4) {
            ChronoLocalDate chronoLocalDateDate = chronology.date(i2, 1, 1);
            int iStartOfWeekOffset = startOfWeekOffset(1, localizedDayOfWeek(chronoLocalDateDate));
            return chronoLocalDateDate.plus((-iStartOfWeekOffset) + (i4 - 1) + ((Math.min(i3, computeWeek(iStartOfWeekOffset, chronoLocalDateDate.lengthOfYear() + this.weekDef.getMinimalDaysInFirstWeek()) - 1) - 1) * 7), (TemporalUnit) ChronoUnit.DAYS);
        }

        private ComputedDayOfField(String str, WeekFields weekFields, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, ValueRange valueRange) {
            this.name = str;
            this.weekDef = weekFields;
            this.baseUnit = temporalUnit;
            this.rangeUnit = temporalUnit2;
            this.range = valueRange;
        }

        @Override // java.time.temporal.TemporalField
        public long getFrom(TemporalAccessor temporalAccessor) {
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return localizedDayOfWeek(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                return localizedWeekOfMonth(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return localizedWeekOfYear(temporalAccessor);
            }
            if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                return localizedWeekOfWeekBasedYear(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return localizedWeekBasedYear(temporalAccessor);
            }
            throw new IllegalStateException("unreachable, rangeUnit: " + ((Object) this.rangeUnit) + ", this: " + ((Object) this));
        }

        private int localizedDayOfWeek(TemporalAccessor temporalAccessor) {
            return Math.floorMod(temporalAccessor.get(ChronoField.DAY_OF_WEEK) - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
        }

        private int localizedDayOfWeek(int i2) {
            return Math.floorMod(i2 - this.weekDef.getFirstDayOfWeek().getValue(), 7) + 1;
        }

        private long localizedWeekOfMonth(TemporalAccessor temporalAccessor) {
            int iLocalizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i2 = temporalAccessor.get(ChronoField.DAY_OF_MONTH);
            return computeWeek(startOfWeekOffset(i2, iLocalizedDayOfWeek), i2);
        }

        private long localizedWeekOfYear(TemporalAccessor temporalAccessor) {
            int iLocalizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i2 = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            return computeWeek(startOfWeekOffset(i2, iLocalizedDayOfWeek), i2);
        }

        private int localizedWeekBasedYear(TemporalAccessor temporalAccessor) {
            int iLocalizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i2 = temporalAccessor.get(ChronoField.YEAR);
            int i3 = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            int iStartOfWeekOffset = startOfWeekOffset(i3, iLocalizedDayOfWeek);
            int iComputeWeek = computeWeek(iStartOfWeekOffset, i3);
            if (iComputeWeek == 0) {
                return i2 - 1;
            }
            if (iComputeWeek >= computeWeek(iStartOfWeekOffset, ((int) temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum()) + this.weekDef.getMinimalDaysInFirstWeek())) {
                return i2 + 1;
            }
            return i2;
        }

        private int localizedWeekOfWeekBasedYear(TemporalAccessor temporalAccessor) {
            int iComputeWeek;
            int iLocalizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i2 = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            int iStartOfWeekOffset = startOfWeekOffset(i2, iLocalizedDayOfWeek);
            int iComputeWeek2 = computeWeek(iStartOfWeekOffset, i2);
            if (iComputeWeek2 == 0) {
                return localizedWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).minus(i2, (TemporalUnit) ChronoUnit.DAYS));
            }
            if (iComputeWeek2 > 50 && iComputeWeek2 >= (iComputeWeek = computeWeek(iStartOfWeekOffset, ((int) temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum()) + this.weekDef.getMinimalDaysInFirstWeek()))) {
                iComputeWeek2 = (iComputeWeek2 - iComputeWeek) + 1;
            }
            return iComputeWeek2;
        }

        private int startOfWeekOffset(int i2, int i3) {
            int iFloorMod = Math.floorMod(i2 - i3, 7);
            int i4 = -iFloorMod;
            if (iFloorMod + 1 > this.weekDef.getMinimalDaysInFirstWeek()) {
                i4 = 7 - iFloorMod;
            }
            return i4;
        }

        private int computeWeek(int i2, int i3) {
            return ((7 + i2) + (i3 - 1)) / 7;
        }

        @Override // java.time.temporal.TemporalField
        public <R extends Temporal> R adjustInto(R r2, long j2) {
            if (this.range.checkValidIntValue(j2, this) == r2.get(this)) {
                return r2;
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return ofWeekBasedYear(Chronology.from(r2), (int) j2, r2.get(this.weekDef.weekOfWeekBasedYear), r2.get(this.weekDef.dayOfWeek));
            }
            return (R) r2.plus(r0 - r0, this.baseUnit);
        }

        @Override // java.time.temporal.TemporalField
        public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            long jLongValue = map.get(this).longValue();
            int intExact = Math.toIntExact(jLongValue);
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                long jFloorMod = Math.floorMod((this.weekDef.getFirstDayOfWeek().getValue() - 1) + (this.range.checkValidIntValue(jLongValue, this) - 1), 7) + 1;
                map.remove(this);
                map.put(ChronoField.DAY_OF_WEEK, Long.valueOf(jFloorMod));
                return null;
            }
            if (!map.containsKey(ChronoField.DAY_OF_WEEK)) {
                return null;
            }
            int iLocalizedDayOfWeek = localizedDayOfWeek(ChronoField.DAY_OF_WEEK.checkValidIntValue(map.get(ChronoField.DAY_OF_WEEK).longValue()));
            Chronology chronologyFrom = Chronology.from(temporalAccessor);
            if (!map.containsKey(ChronoField.YEAR)) {
                if ((this.rangeUnit == WeekFields.WEEK_BASED_YEARS || this.rangeUnit == ChronoUnit.FOREVER) && map.containsKey(this.weekDef.weekBasedYear) && map.containsKey(this.weekDef.weekOfWeekBasedYear)) {
                    return resolveWBY(map, chronologyFrom, iLocalizedDayOfWeek, resolverStyle);
                }
                return null;
            }
            int iCheckValidIntValue = ChronoField.YEAR.checkValidIntValue(map.get(ChronoField.YEAR).longValue());
            if (this.rangeUnit == ChronoUnit.MONTHS && map.containsKey(ChronoField.MONTH_OF_YEAR)) {
                return resolveWoM(map, chronologyFrom, iCheckValidIntValue, map.get(ChronoField.MONTH_OF_YEAR).longValue(), intExact, iLocalizedDayOfWeek, resolverStyle);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return resolveWoY(map, chronologyFrom, iCheckValidIntValue, intExact, iLocalizedDayOfWeek, resolverStyle);
            }
            return null;
        }

        private ChronoLocalDate resolveWoM(Map<TemporalField, Long> map, Chronology chronology, int i2, long j2, long j3, int i3, ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDatePlus;
            if (resolverStyle == ResolverStyle.LENIENT) {
                ChronoLocalDate chronoLocalDatePlus2 = chronology.date(i2, 1, 1).plus(Math.subtractExact(j2, 1L), (TemporalUnit) ChronoUnit.MONTHS);
                chronoLocalDatePlus = chronoLocalDatePlus2.plus(Math.addExact(Math.multiplyExact(Math.subtractExact(j3, localizedWeekOfMonth(chronoLocalDatePlus2)), 7L), i3 - localizedDayOfWeek(chronoLocalDatePlus2)), (TemporalUnit) ChronoUnit.DAYS);
            } else {
                ChronoLocalDate chronoLocalDateDate = chronology.date(i2, ChronoField.MONTH_OF_YEAR.checkValidIntValue(j2), 1);
                int iCheckValidIntValue = (int) (this.range.checkValidIntValue(j3, this) - localizedWeekOfMonth(chronoLocalDateDate));
                chronoLocalDatePlus = chronoLocalDateDate.plus((iCheckValidIntValue * 7) + (i3 - localizedDayOfWeek(chronoLocalDateDate)), (TemporalUnit) ChronoUnit.DAYS);
                if (resolverStyle == ResolverStyle.STRICT && chronoLocalDatePlus.getLong(ChronoField.MONTH_OF_YEAR) != j2) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different month");
                }
            }
            map.remove(this);
            map.remove(ChronoField.YEAR);
            map.remove(ChronoField.MONTH_OF_YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDatePlus;
        }

        private ChronoLocalDate resolveWoY(Map<TemporalField, Long> map, Chronology chronology, int i2, long j2, int i3, ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDatePlus;
            ChronoLocalDate chronoLocalDateDate = chronology.date(i2, 1, 1);
            if (resolverStyle == ResolverStyle.LENIENT) {
                chronoLocalDatePlus = chronoLocalDateDate.plus(Math.addExact(Math.multiplyExact(Math.subtractExact(j2, localizedWeekOfYear(chronoLocalDateDate)), 7L), i3 - localizedDayOfWeek(chronoLocalDateDate)), (TemporalUnit) ChronoUnit.DAYS);
            } else {
                int iCheckValidIntValue = (int) (this.range.checkValidIntValue(j2, this) - localizedWeekOfYear(chronoLocalDateDate));
                chronoLocalDatePlus = chronoLocalDateDate.plus((iCheckValidIntValue * 7) + (i3 - localizedDayOfWeek(chronoLocalDateDate)), (TemporalUnit) ChronoUnit.DAYS);
                if (resolverStyle == ResolverStyle.STRICT && chronoLocalDatePlus.getLong(ChronoField.YEAR) != i2) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different year");
                }
            }
            map.remove(this);
            map.remove(ChronoField.YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDatePlus;
        }

        private ChronoLocalDate resolveWBY(Map<TemporalField, Long> map, Chronology chronology, int i2, ResolverStyle resolverStyle) {
            ChronoLocalDate chronoLocalDateOfWeekBasedYear;
            int iCheckValidIntValue = this.weekDef.weekBasedYear.range().checkValidIntValue(map.get(this.weekDef.weekBasedYear).longValue(), this.weekDef.weekBasedYear);
            if (resolverStyle == ResolverStyle.LENIENT) {
                chronoLocalDateOfWeekBasedYear = ofWeekBasedYear(chronology, iCheckValidIntValue, 1, i2).plus(Math.subtractExact(map.get(this.weekDef.weekOfWeekBasedYear).longValue(), 1L), (TemporalUnit) ChronoUnit.WEEKS);
            } else {
                chronoLocalDateOfWeekBasedYear = ofWeekBasedYear(chronology, iCheckValidIntValue, this.weekDef.weekOfWeekBasedYear.range().checkValidIntValue(map.get(this.weekDef.weekOfWeekBasedYear).longValue(), this.weekDef.weekOfWeekBasedYear), i2);
                if (resolverStyle == ResolverStyle.STRICT && localizedWeekBasedYear(chronoLocalDateOfWeekBasedYear) != iCheckValidIntValue) {
                    throw new DateTimeException("Strict mode rejected resolved date as it is in a different week-based-year");
                }
            }
            map.remove(this);
            map.remove(this.weekDef.weekBasedYear);
            map.remove(this.weekDef.weekOfWeekBasedYear);
            map.remove(ChronoField.DAY_OF_WEEK);
            return chronoLocalDateOfWeekBasedYear;
        }

        @Override // java.time.temporal.TemporalField
        public String getDisplayName(Locale locale) {
            Objects.requireNonNull(locale, "locale");
            if (this.rangeUnit == ChronoUnit.YEARS) {
                ResourceBundle javaTimeFormatData = LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeFormatData();
                return javaTimeFormatData.containsKey("field.week") ? javaTimeFormatData.getString("field.week") : this.name;
            }
            return this.name;
        }

        @Override // java.time.temporal.TemporalField
        public TemporalUnit getBaseUnit() {
            return this.baseUnit;
        }

        @Override // java.time.temporal.TemporalField
        public TemporalUnit getRangeUnit() {
            return this.rangeUnit;
        }

        @Override // java.time.temporal.TemporalField
        public boolean isDateBased() {
            return true;
        }

        @Override // java.time.temporal.TemporalField
        public boolean isTimeBased() {
            return false;
        }

        @Override // java.time.temporal.TemporalField
        public ValueRange range() {
            return this.range;
        }

        @Override // java.time.temporal.TemporalField
        public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
            if (temporalAccessor.isSupported(ChronoField.DAY_OF_WEEK)) {
                if (this.rangeUnit == ChronoUnit.WEEKS) {
                    return true;
                }
                if (this.rangeUnit == ChronoUnit.MONTHS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH);
                }
                if (this.rangeUnit == ChronoUnit.YEARS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
                }
                if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                    return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR);
                }
                if (this.rangeUnit == ChronoUnit.FOREVER) {
                    return temporalAccessor.isSupported(ChronoField.YEAR);
                }
                return false;
            }
            return false;
        }

        @Override // java.time.temporal.TemporalField
        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            if (this.rangeUnit == ChronoUnit.WEEKS) {
                return this.range;
            }
            if (this.rangeUnit == ChronoUnit.MONTHS) {
                return rangeByWeek(temporalAccessor, ChronoField.DAY_OF_MONTH);
            }
            if (this.rangeUnit == ChronoUnit.YEARS) {
                return rangeByWeek(temporalAccessor, ChronoField.DAY_OF_YEAR);
            }
            if (this.rangeUnit == WeekFields.WEEK_BASED_YEARS) {
                return rangeWeekOfWeekBasedYear(temporalAccessor);
            }
            if (this.rangeUnit == ChronoUnit.FOREVER) {
                return ChronoField.YEAR.range();
            }
            throw new IllegalStateException("unreachable, rangeUnit: " + ((Object) this.rangeUnit) + ", this: " + ((Object) this));
        }

        private ValueRange rangeByWeek(TemporalAccessor temporalAccessor, TemporalField temporalField) {
            int iStartOfWeekOffset = startOfWeekOffset(temporalAccessor.get(temporalField), localizedDayOfWeek(temporalAccessor));
            ValueRange valueRangeRange = temporalAccessor.range(temporalField);
            return ValueRange.of(computeWeek(iStartOfWeekOffset, (int) valueRangeRange.getMinimum()), computeWeek(iStartOfWeekOffset, (int) valueRangeRange.getMaximum()));
        }

        private ValueRange rangeWeekOfWeekBasedYear(TemporalAccessor temporalAccessor) {
            if (!temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR)) {
                return WEEK_OF_YEAR_RANGE;
            }
            int iLocalizedDayOfWeek = localizedDayOfWeek(temporalAccessor);
            int i2 = temporalAccessor.get(ChronoField.DAY_OF_YEAR);
            int iStartOfWeekOffset = startOfWeekOffset(i2, iLocalizedDayOfWeek);
            int iComputeWeek = computeWeek(iStartOfWeekOffset, i2);
            if (iComputeWeek == 0) {
                return rangeWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).minus(i2 + 7, (TemporalUnit) ChronoUnit.DAYS));
            }
            if (iComputeWeek >= computeWeek(iStartOfWeekOffset, ((int) temporalAccessor.range(ChronoField.DAY_OF_YEAR).getMaximum()) + this.weekDef.getMinimalDaysInFirstWeek())) {
                return rangeWeekOfWeekBasedYear(Chronology.from(temporalAccessor).date(temporalAccessor).plus((r0 - i2) + 1 + 7, (TemporalUnit) ChronoUnit.DAYS));
            }
            return ValueRange.of(1L, r0 - 1);
        }

        @Override // java.time.temporal.TemporalField
        public String toString() {
            return this.name + "[" + this.weekDef.toString() + "]";
        }
    }
}
