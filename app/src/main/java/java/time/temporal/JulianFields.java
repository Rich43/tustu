package java.time.temporal;

import java.time.DateTimeException;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.ResolverStyle;
import java.util.Map;

/* loaded from: rt.jar:java/time/temporal/JulianFields.class */
public final class JulianFields {
    private static final long JULIAN_DAY_OFFSET = 2440588;
    public static final TemporalField JULIAN_DAY = Field.JULIAN_DAY;
    public static final TemporalField MODIFIED_JULIAN_DAY = Field.MODIFIED_JULIAN_DAY;
    public static final TemporalField RATA_DIE = Field.RATA_DIE;

    private JulianFields() {
        throw new AssertionError((Object) "Not instantiable");
    }

    /* loaded from: rt.jar:java/time/temporal/JulianFields$Field.class */
    private enum Field implements TemporalField {
        JULIAN_DAY("JulianDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, JulianFields.JULIAN_DAY_OFFSET),
        MODIFIED_JULIAN_DAY("ModifiedJulianDay", ChronoUnit.DAYS, ChronoUnit.FOREVER, 40587),
        RATA_DIE("RataDie", ChronoUnit.DAYS, ChronoUnit.FOREVER, 719163);

        private static final long serialVersionUID = -7501623920830201812L;
        private final transient String name;
        private final transient TemporalUnit baseUnit;
        private final transient TemporalUnit rangeUnit;
        private final transient ValueRange range;
        private final transient long offset;

        @Override // java.time.temporal.TemporalField
        public /* bridge */ /* synthetic */ TemporalAccessor resolve(Map map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            return resolve((Map<TemporalField, Long>) map, temporalAccessor, resolverStyle);
        }

        Field(String str, TemporalUnit temporalUnit, TemporalUnit temporalUnit2, long j2) {
            this.name = str;
            this.baseUnit = temporalUnit;
            this.rangeUnit = temporalUnit2;
            this.range = ValueRange.of((-365243219162L) + j2, 365241780471L + j2);
            this.offset = j2;
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
            return temporalAccessor.isSupported(ChronoField.EPOCH_DAY);
        }

        @Override // java.time.temporal.TemporalField
        public ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor) {
            if (!isSupportedBy(temporalAccessor)) {
                throw new DateTimeException("Unsupported field: " + ((Object) this));
            }
            return range();
        }

        @Override // java.time.temporal.TemporalField
        public long getFrom(TemporalAccessor temporalAccessor) {
            return temporalAccessor.getLong(ChronoField.EPOCH_DAY) + this.offset;
        }

        @Override // java.time.temporal.TemporalField
        public <R extends Temporal> R adjustInto(R r2, long j2) {
            if (!range().isValidValue(j2)) {
                throw new DateTimeException("Invalid value: " + this.name + " " + j2);
            }
            return (R) r2.with(ChronoField.EPOCH_DAY, Math.subtractExact(j2, this.offset));
        }

        @Override // java.time.temporal.TemporalField
        public ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
            long jLongValue = map.remove(this).longValue();
            Chronology chronologyFrom = Chronology.from(temporalAccessor);
            if (resolverStyle == ResolverStyle.LENIENT) {
                return chronologyFrom.dateEpochDay(Math.subtractExact(jLongValue, this.offset));
            }
            range().checkValidValue(jLongValue, this);
            return chronologyFrom.dateEpochDay(jLongValue - this.offset);
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }
}
