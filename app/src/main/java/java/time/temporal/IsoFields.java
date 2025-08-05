package java.time.temporal;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:java/time/temporal/IsoFields.class */
public final class IsoFields {
    public static final TemporalField DAY_OF_QUARTER = Field.DAY_OF_QUARTER;
    public static final TemporalField QUARTER_OF_YEAR = Field.QUARTER_OF_YEAR;
    public static final TemporalField WEEK_OF_WEEK_BASED_YEAR = Field.WEEK_OF_WEEK_BASED_YEAR;
    public static final TemporalField WEEK_BASED_YEAR = Field.WEEK_BASED_YEAR;
    public static final TemporalUnit WEEK_BASED_YEARS = Unit.WEEK_BASED_YEARS;
    public static final TemporalUnit QUARTER_YEARS = Unit.QUARTER_YEARS;

    private IsoFields() {
        throw new AssertionError((Object) "Not instantiable");
    }

    /* loaded from: rt.jar:java/time/temporal/IsoFields$Field.class */
    private enum Field implements TemporalField {
        DAY_OF_QUARTER { // from class: java.time.temporal.IsoFields.Field.1
            @Override // java.time.temporal.TemporalField
            public /* bridge */ /* synthetic */ TemporalAccessor resolve(Map map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                return resolve((Map<TemporalField, Long>) map, temporalAccessor, resolverStyle);
            }

            @Override // java.time.temporal.TemporalField
            public TemporalUnit getBaseUnit() {
                return ChronoUnit.DAYS;
            }

            @Override // java.time.temporal.TemporalField
            public TemporalUnit getRangeUnit() {
                return IsoFields.QUARTER_YEARS;
            }

            @Override // java.time.temporal.TemporalField
            public ValueRange range() {
                return ValueRange.of(1L, 90L, 92L);
            }

            @Override // java.time.temporal.TemporalField
            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.DAY_OF_YEAR) && temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && temporalAccessor.isSupported(ChronoField.YEAR) && Field.isIso(temporalAccessor);
            }

            @Override // java.time.temporal.IsoFields.Field, java.time.temporal.TemporalField
            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (!isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
                }
                long j2 = temporalAccessor.getLong(QUARTER_OF_YEAR);
                if (j2 == 1) {
                    return IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? ValueRange.of(1L, 91L) : ValueRange.of(1L, 90L);
                }
                if (j2 == 2) {
                    return ValueRange.of(1L, 91L);
                }
                if (j2 == 3 || j2 == 4) {
                    return ValueRange.of(1L, 92L);
                }
                return range();
            }

            @Override // java.time.temporal.TemporalField
            public long getFrom(TemporalAccessor temporalAccessor) {
                if (!isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: DayOfQuarter");
                }
                return temporalAccessor.get(ChronoField.DAY_OF_YEAR) - Field.QUARTER_DAYS[((temporalAccessor.get(ChronoField.MONTH_OF_YEAR) - 1) / 3) + (IsoChronology.INSTANCE.isLeapYear(temporalAccessor.getLong(ChronoField.YEAR)) ? 4 : 0)];
            }

            @Override // java.time.temporal.TemporalField
            public <R extends Temporal> R adjustInto(R r2, long j2) {
                long from = getFrom(r2);
                range().checkValidValue(j2, this);
                return (R) r2.with(ChronoField.DAY_OF_YEAR, r2.getLong(ChronoField.DAY_OF_YEAR) + (j2 - from));
            }

            @Override // java.time.temporal.TemporalField
            public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                LocalDate localDateOf;
                long jSubtractExact;
                Long l2 = map.get(ChronoField.YEAR);
                Long l3 = map.get(QUARTER_OF_YEAR);
                if (l2 == null || l3 == null) {
                    return null;
                }
                int iCheckValidIntValue = ChronoField.YEAR.checkValidIntValue(l2.longValue());
                long jLongValue = map.get(DAY_OF_QUARTER).longValue();
                Field.ensureIso(temporalAccessor);
                if (resolverStyle == ResolverStyle.LENIENT) {
                    localDateOf = LocalDate.of(iCheckValidIntValue, 1, 1).plusMonths(Math.multiplyExact(Math.subtractExact(l3.longValue(), 1L), 3L));
                    jSubtractExact = Math.subtractExact(jLongValue, 1L);
                } else {
                    localDateOf = LocalDate.of(iCheckValidIntValue, ((QUARTER_OF_YEAR.range().checkValidIntValue(l3.longValue(), QUARTER_OF_YEAR) - 1) * 3) + 1, 1);
                    if (jLongValue < 1 || jLongValue > 90) {
                        if (resolverStyle == ResolverStyle.STRICT) {
                            rangeRefinedBy(localDateOf).checkValidValue(jLongValue, this);
                        } else {
                            range().checkValidValue(jLongValue, this);
                        }
                    }
                    jSubtractExact = jLongValue - 1;
                }
                map.remove(this);
                map.remove(ChronoField.YEAR);
                map.remove(QUARTER_OF_YEAR);
                return localDateOf.plusDays(jSubtractExact);
            }

            @Override // java.lang.Enum
            public String toString() {
                return "DayOfQuarter";
            }
        },
        QUARTER_OF_YEAR { // from class: java.time.temporal.IsoFields.Field.2
            @Override // java.time.temporal.TemporalField
            public TemporalUnit getBaseUnit() {
                return IsoFields.QUARTER_YEARS;
            }

            @Override // java.time.temporal.TemporalField
            public TemporalUnit getRangeUnit() {
                return ChronoUnit.YEARS;
            }

            @Override // java.time.temporal.TemporalField
            public ValueRange range() {
                return ValueRange.of(1L, 4L);
            }

            @Override // java.time.temporal.TemporalField
            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR) && Field.isIso(temporalAccessor);
            }

            @Override // java.time.temporal.TemporalField
            public long getFrom(TemporalAccessor temporalAccessor) {
                if (!isSupportedBy(temporalAccessor)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: QuarterOfYear");
                }
                return (temporalAccessor.getLong(ChronoField.MONTH_OF_YEAR) + 2) / 3;
            }

            @Override // java.time.temporal.TemporalField
            public <R extends Temporal> R adjustInto(R r2, long j2) {
                long from = getFrom(r2);
                range().checkValidValue(j2, this);
                return (R) r2.with(ChronoField.MONTH_OF_YEAR, r2.getLong(ChronoField.MONTH_OF_YEAR) + ((j2 - from) * 3));
            }

            @Override // java.lang.Enum
            public String toString() {
                return "QuarterOfYear";
            }
        },
        WEEK_OF_WEEK_BASED_YEAR { // from class: java.time.temporal.IsoFields.Field.3
            @Override // java.time.temporal.TemporalField
            public /* bridge */ /* synthetic */ TemporalAccessor resolve(Map map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                return resolve((Map<TemporalField, Long>) map, temporalAccessor, resolverStyle);
            }

            @Override // java.time.temporal.TemporalField
            public String getDisplayName(Locale locale) {
                Objects.requireNonNull(locale, "locale");
                ResourceBundle javaTimeFormatData = LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeFormatData();
                return javaTimeFormatData.containsKey("field.week") ? javaTimeFormatData.getString("field.week") : toString();
            }

            @Override // java.time.temporal.TemporalField
            public TemporalUnit getBaseUnit() {
                return ChronoUnit.WEEKS;
            }

            @Override // java.time.temporal.TemporalField
            public TemporalUnit getRangeUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }

            @Override // java.time.temporal.TemporalField
            public ValueRange range() {
                return ValueRange.of(1L, 52L, 53L);
            }

            @Override // java.time.temporal.TemporalField
            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) && Field.isIso(temporalAccessor);
            }

            @Override // java.time.temporal.IsoFields.Field, java.time.temporal.TemporalField
            public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return Field.getWeekRange(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
            }

            @Override // java.time.temporal.TemporalField
            public long getFrom(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return Field.getWeek(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekOfWeekBasedYear");
            }

            @Override // java.time.temporal.TemporalField
            public <R extends Temporal> R adjustInto(R r2, long j2) {
                range().checkValidValue(j2, this);
                return (R) r2.plus(Math.subtractExact(j2, getFrom(r2)), ChronoUnit.WEEKS);
            }

            @Override // java.time.temporal.TemporalField
            public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
                LocalDate localDateWith;
                Long l2 = map.get(WEEK_BASED_YEAR);
                Long l3 = map.get(ChronoField.DAY_OF_WEEK);
                if (l2 == null || l3 == null) {
                    return null;
                }
                int iCheckValidIntValue = WEEK_BASED_YEAR.range().checkValidIntValue(l2.longValue(), WEEK_BASED_YEAR);
                long jLongValue = map.get(WEEK_OF_WEEK_BASED_YEAR).longValue();
                Field.ensureIso(temporalAccessor);
                LocalDate localDateOf = LocalDate.of(iCheckValidIntValue, 1, 4);
                if (resolverStyle == ResolverStyle.LENIENT) {
                    long jLongValue2 = l3.longValue();
                    if (jLongValue2 > 7) {
                        localDateOf = localDateOf.plusWeeks((jLongValue2 - 1) / 7);
                        jLongValue2 = ((jLongValue2 - 1) % 7) + 1;
                    } else if (jLongValue2 < 1) {
                        localDateOf = localDateOf.plusWeeks(Math.subtractExact(jLongValue2, 7L) / 7);
                        jLongValue2 = ((jLongValue2 + 6) % 7) + 1;
                    }
                    localDateWith = localDateOf.plusWeeks(Math.subtractExact(jLongValue, 1L)).with((TemporalField) ChronoField.DAY_OF_WEEK, jLongValue2);
                } else {
                    int iCheckValidIntValue2 = ChronoField.DAY_OF_WEEK.checkValidIntValue(l3.longValue());
                    if (jLongValue < 1 || jLongValue > 52) {
                        if (resolverStyle == ResolverStyle.STRICT) {
                            Field.getWeekRange(localDateOf).checkValidValue(jLongValue, this);
                        } else {
                            range().checkValidValue(jLongValue, this);
                        }
                    }
                    localDateWith = localDateOf.plusWeeks(jLongValue - 1).with((TemporalField) ChronoField.DAY_OF_WEEK, iCheckValidIntValue2);
                }
                map.remove(this);
                map.remove(WEEK_BASED_YEAR);
                map.remove(ChronoField.DAY_OF_WEEK);
                return localDateWith;
            }

            @Override // java.lang.Enum
            public String toString() {
                return "WeekOfWeekBasedYear";
            }
        },
        WEEK_BASED_YEAR { // from class: java.time.temporal.IsoFields.Field.4
            @Override // java.time.temporal.TemporalField
            public TemporalUnit getBaseUnit() {
                return IsoFields.WEEK_BASED_YEARS;
            }

            @Override // java.time.temporal.TemporalField
            public TemporalUnit getRangeUnit() {
                return ChronoUnit.FOREVER;
            }

            @Override // java.time.temporal.TemporalField
            public ValueRange range() {
                return ChronoField.YEAR.range();
            }

            @Override // java.time.temporal.TemporalField
            public boolean isSupportedBy(TemporalAccessor temporalAccessor) {
                return temporalAccessor.isSupported(ChronoField.EPOCH_DAY) && Field.isIso(temporalAccessor);
            }

            @Override // java.time.temporal.TemporalField
            public long getFrom(TemporalAccessor temporalAccessor) {
                if (isSupportedBy(temporalAccessor)) {
                    return Field.getWeekBasedYear(LocalDate.from(temporalAccessor));
                }
                throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
            }

            @Override // java.time.temporal.TemporalField
            public <R extends Temporal> R adjustInto(R r2, long j2) {
                if (!isSupportedBy(r2)) {
                    throw new UnsupportedTemporalTypeException("Unsupported field: WeekBasedYear");
                }
                int iCheckValidIntValue = range().checkValidIntValue(j2, WEEK_BASED_YEAR);
                LocalDate localDateFrom = LocalDate.from((TemporalAccessor) r2);
                int i2 = localDateFrom.get(ChronoField.DAY_OF_WEEK);
                int week = Field.getWeek(localDateFrom);
                if (week == 53 && Field.getWeekRange(iCheckValidIntValue) == 52) {
                    week = 52;
                }
                return (R) r2.with(LocalDate.of(iCheckValidIntValue, 1, 4).plusDays((i2 - r0.get(ChronoField.DAY_OF_WEEK)) + ((week - 1) * 7)));
            }

            @Override // java.lang.Enum
            public String toString() {
                return "WeekBasedYear";
            }
        };

        private static final int[] QUARTER_DAYS = {0, 90, 181, 273, 0, 91, 182, 274};

        @Override // java.time.temporal.TemporalField
        public boolean isDateBased() {
            return true;
        }

        @Override // java.time.temporal.TemporalField
        public boolean isTimeBased() {
            return false;
        }

        @Override // java.time.temporal.TemporalField
        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            return range();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isIso(TemporalAccessor temporalAccessor) {
            return Chronology.from(temporalAccessor).equals(IsoChronology.INSTANCE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void ensureIso(TemporalAccessor temporalAccessor) {
            if (!isIso(temporalAccessor)) {
                throw new DateTimeException("Resolve requires IsoChronology");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ValueRange getWeekRange(LocalDate localDate) {
            return ValueRange.of(1L, getWeekRange(getWeekBasedYear(localDate)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int getWeekRange(int i2) {
            LocalDate localDateOf = LocalDate.of(i2, 1, 1);
            if (localDateOf.getDayOfWeek() == DayOfWeek.THURSDAY) {
                return 53;
            }
            if (localDateOf.getDayOfWeek() == DayOfWeek.WEDNESDAY && localDateOf.isLeapYear()) {
                return 53;
            }
            return 52;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int getWeek(LocalDate localDate) {
            int iOrdinal = localDate.getDayOfWeek().ordinal();
            int dayOfYear = localDate.getDayOfYear() - 1;
            int i2 = dayOfYear + (3 - iOrdinal);
            int i3 = (i2 - ((i2 / 7) * 7)) - 3;
            if (i3 < -3) {
                i3 += 7;
            }
            if (dayOfYear < i3) {
                return (int) getWeekRange(localDate.withDayOfYear(180).minusYears(1L)).getMaximum();
            }
            int i4 = ((dayOfYear - i3) / 7) + 1;
            if (i4 == 53) {
                if (!(i3 == -3 || (i3 == -2 && localDate.isLeapYear()))) {
                    i4 = 1;
                }
            }
            return i4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static int getWeekBasedYear(LocalDate localDate) {
            int year = localDate.getYear();
            int dayOfYear = localDate.getDayOfYear();
            if (dayOfYear <= 3) {
                if (dayOfYear - localDate.getDayOfWeek().ordinal() < -2) {
                    year--;
                }
            } else if (dayOfYear >= 363) {
                if (((dayOfYear - 363) - (localDate.isLeapYear() ? 1 : 0)) - localDate.getDayOfWeek().ordinal() >= 0) {
                    year++;
                }
            }
            return year;
        }
    }

    /* loaded from: rt.jar:java/time/temporal/IsoFields$Unit.class */
    private enum Unit implements TemporalUnit {
        WEEK_BASED_YEARS("WeekBasedYears", Duration.ofSeconds(31556952)),
        QUARTER_YEARS("QuarterYears", Duration.ofSeconds(7889238));

        private final String name;
        private final Duration duration;

        Unit(String str, Duration duration) {
            this.name = str;
            this.duration = duration;
        }

        @Override // java.time.temporal.TemporalUnit
        public Duration getDuration() {
            return this.duration;
        }

        @Override // java.time.temporal.TemporalUnit
        public boolean isDurationEstimated() {
            return true;
        }

        @Override // java.time.temporal.TemporalUnit
        public boolean isDateBased() {
            return true;
        }

        @Override // java.time.temporal.TemporalUnit
        public boolean isTimeBased() {
            return false;
        }

        @Override // java.time.temporal.TemporalUnit
        public boolean isSupportedBy(Temporal temporal) {
            return temporal.isSupported(ChronoField.EPOCH_DAY);
        }

        @Override // java.time.temporal.TemporalUnit
        public <R extends Temporal> R addTo(R r2, long j2) {
            switch (this) {
                case WEEK_BASED_YEARS:
                    return (R) r2.with(IsoFields.WEEK_BASED_YEAR, Math.addExact(r2.get(IsoFields.WEEK_BASED_YEAR), j2));
                case QUARTER_YEARS:
                    return (R) r2.plus(j2 / 4, ChronoUnit.YEARS).plus((j2 % 4) * 3, ChronoUnit.MONTHS);
                default:
                    throw new IllegalStateException("Unreachable");
            }
        }

        @Override // java.time.temporal.TemporalUnit
        public long between(Temporal temporal, Temporal temporal2) {
            if (temporal.getClass() != temporal2.getClass()) {
                return temporal.until(temporal2, this);
            }
            switch (this) {
                case WEEK_BASED_YEARS:
                    return Math.subtractExact(temporal2.getLong(IsoFields.WEEK_BASED_YEAR), temporal.getLong(IsoFields.WEEK_BASED_YEAR));
                case QUARTER_YEARS:
                    return temporal.until(temporal2, ChronoUnit.MONTHS) / 3;
                default:
                    throw new IllegalStateException("Unreachable");
            }
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }
}
