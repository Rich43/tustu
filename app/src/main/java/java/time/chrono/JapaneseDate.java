package java.time.chrono;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
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
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.LocalGregorianCalendar;

/* loaded from: rt.jar:java/time/chrono/JapaneseDate.class */
public final class JapaneseDate extends ChronoLocalDateImpl<JapaneseDate> implements ChronoLocalDate, Serializable {
    private static final long serialVersionUID = -305327627230580483L;
    private final transient LocalDate isoDate;
    private transient JapaneseEra era;
    private transient int yearOfEra;
    static final LocalDate MEIJI_6_ISODATE = LocalDate.of(1873, 1, 1);

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public /* bridge */ /* synthetic */ long until(Temporal temporal, TemporalUnit temporalUnit) {
        return super.until(temporal, temporalUnit);
    }

    public static JapaneseDate now() {
        return now(Clock.systemDefaultZone());
    }

    public static JapaneseDate now(ZoneId zoneId) {
        return now(Clock.system(zoneId));
    }

    public static JapaneseDate now(Clock clock) {
        return new JapaneseDate(LocalDate.now(clock));
    }

    public static JapaneseDate of(JapaneseEra japaneseEra, int i2, int i3, int i4) {
        Objects.requireNonNull(japaneseEra, "era");
        LocalGregorianCalendar.Date dateNewCalendarDate = JapaneseChronology.JCAL.newCalendarDate((TimeZone) null);
        dateNewCalendarDate.setEra(japaneseEra.getPrivateEra()).setDate(i2, i3, i4);
        if (!JapaneseChronology.JCAL.validate(dateNewCalendarDate)) {
            throw new DateTimeException("year, month, and day not valid for Era");
        }
        return new JapaneseDate(japaneseEra, i2, LocalDate.of(dateNewCalendarDate.getNormalizedYear(), i3, i4));
    }

    public static JapaneseDate of(int i2, int i3, int i4) {
        return new JapaneseDate(LocalDate.of(i2, i3, i4));
    }

    static JapaneseDate ofYearDay(JapaneseEra japaneseEra, int i2, int i3) {
        Objects.requireNonNull(japaneseEra, "era");
        CalendarDate sinceDate = japaneseEra.getPrivateEra().getSinceDate();
        LocalGregorianCalendar.Date dateNewCalendarDate = JapaneseChronology.JCAL.newCalendarDate((TimeZone) null);
        dateNewCalendarDate.setEra(japaneseEra.getPrivateEra());
        if (i2 == 1) {
            dateNewCalendarDate.setDate(i2, sinceDate.getMonth(), (sinceDate.getDayOfMonth() + i3) - 1);
        } else {
            dateNewCalendarDate.setDate(i2, 1, i3);
        }
        JapaneseChronology.JCAL.normalize(dateNewCalendarDate);
        if (japaneseEra.getPrivateEra() != dateNewCalendarDate.getEra() || i2 != dateNewCalendarDate.getYear()) {
            throw new DateTimeException("Invalid parameters");
        }
        return new JapaneseDate(japaneseEra, i2, LocalDate.of(dateNewCalendarDate.getNormalizedYear(), dateNewCalendarDate.getMonth(), dateNewCalendarDate.getDayOfMonth()));
    }

    public static JapaneseDate from(TemporalAccessor temporalAccessor) {
        return JapaneseChronology.INSTANCE.date(temporalAccessor);
    }

    JapaneseDate(LocalDate localDate) {
        if (localDate.isBefore(MEIJI_6_ISODATE)) {
            throw new DateTimeException("JapaneseDate before Meiji 6 is not supported");
        }
        LocalGregorianCalendar.Date privateJapaneseDate = toPrivateJapaneseDate(localDate);
        this.era = JapaneseEra.toJapaneseEra(privateJapaneseDate.getEra());
        this.yearOfEra = privateJapaneseDate.getYear();
        this.isoDate = localDate;
    }

    JapaneseDate(JapaneseEra japaneseEra, int i2, LocalDate localDate) {
        if (localDate.isBefore(MEIJI_6_ISODATE)) {
            throw new DateTimeException("JapaneseDate before Meiji 6 is not supported");
        }
        this.era = japaneseEra;
        this.yearOfEra = i2;
        this.isoDate = localDate;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public JapaneseChronology getChronology() {
        return JapaneseChronology.INSTANCE;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public JapaneseEra getEra() {
        return this.era;
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfMonth() {
        return this.isoDate.lengthOfMonth();
    }

    @Override // java.time.chrono.ChronoLocalDate
    public int lengthOfYear() {
        Calendar calendar = Calendar.getInstance(JapaneseChronology.LOCALE);
        calendar.set(0, this.era.getValue() + 2);
        calendar.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
        return calendar.getActualMaximum(6);
    }

    @Override // java.time.chrono.ChronoLocalDate, java.time.temporal.TemporalAccessor
    public boolean isSupported(TemporalField temporalField) {
        if (temporalField == ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH || temporalField == ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR || temporalField == ChronoField.ALIGNED_WEEK_OF_MONTH || temporalField == ChronoField.ALIGNED_WEEK_OF_YEAR) {
            return false;
        }
        return super.isSupported(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    public ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (isSupported(temporalField)) {
                ChronoField chronoField = (ChronoField) temporalField;
                switch (chronoField) {
                    case DAY_OF_MONTH:
                        return ValueRange.of(1L, lengthOfMonth());
                    case DAY_OF_YEAR:
                        return ValueRange.of(1L, lengthOfYear());
                    case YEAR_OF_ERA:
                        Calendar calendar = Calendar.getInstance(JapaneseChronology.LOCALE);
                        calendar.set(0, this.era.getValue() + 2);
                        calendar.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
                        return ValueRange.of(1L, calendar.getActualMaximum(1));
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
                case DAY_OF_YEAR:
                    Calendar calendar = Calendar.getInstance(JapaneseChronology.LOCALE);
                    calendar.set(0, this.era.getValue() + 2);
                    calendar.set(this.yearOfEra, this.isoDate.getMonthValue() - 1, this.isoDate.getDayOfMonth());
                    return calendar.get(6);
                case YEAR_OF_ERA:
                    return this.yearOfEra;
                case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                case ALIGNED_WEEK_OF_MONTH:
                case ALIGNED_WEEK_OF_YEAR:
                    throw new UnsupportedTemporalTypeException("Unsupported field: " + ((Object) temporalField));
                case ERA:
                    return this.era.getValue();
                default:
                    return this.isoDate.getLong(temporalField);
            }
        }
        return temporalField.getFrom(this);
    }

    private static LocalGregorianCalendar.Date toPrivateJapaneseDate(LocalDate localDate) {
        LocalGregorianCalendar.Date dateNewCalendarDate = JapaneseChronology.JCAL.newCalendarDate((TimeZone) null);
        sun.util.calendar.Era eraPrivateEraFrom = JapaneseEra.privateEraFrom(localDate);
        int year = localDate.getYear();
        if (eraPrivateEraFrom != null) {
            year -= eraPrivateEraFrom.getSinceDate().getYear() - 1;
        }
        dateNewCalendarDate.setEra(eraPrivateEraFrom).setYear(year).setMonth(localDate.getMonthValue()).setDayOfMonth(localDate.getDayOfMonth());
        JapaneseChronology.JCAL.normalize(dateNewCalendarDate);
        return dateNewCalendarDate;
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public JapaneseDate with(TemporalField temporalField, long j2) {
        if (temporalField instanceof ChronoField) {
            ChronoField chronoField = (ChronoField) temporalField;
            if (getLong(chronoField) == j2) {
                return this;
            }
            switch (chronoField) {
                case YEAR_OF_ERA:
                case ERA:
                case YEAR:
                    int iCheckValidIntValue = getChronology().range(chronoField).checkValidIntValue(j2, chronoField);
                    switch (chronoField) {
                        case YEAR_OF_ERA:
                            return withYear(iCheckValidIntValue);
                        case ERA:
                            return withYear(JapaneseEra.of(iCheckValidIntValue), this.yearOfEra);
                        case YEAR:
                            return with(this.isoDate.withYear(iCheckValidIntValue));
                    }
            }
            return with(this.isoDate.with(temporalField, j2));
        }
        return (JapaneseDate) super.with(temporalField, j2);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public JapaneseDate with(TemporalAdjuster temporalAdjuster) {
        return (JapaneseDate) super.with(temporalAdjuster);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public JapaneseDate plus(TemporalAmount temporalAmount) {
        return (JapaneseDate) super.plus(temporalAmount);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public JapaneseDate minus(TemporalAmount temporalAmount) {
        return (JapaneseDate) super.minus(temporalAmount);
    }

    private JapaneseDate withYear(JapaneseEra japaneseEra, int i2) {
        return with(this.isoDate.withYear(JapaneseChronology.INSTANCE.prolepticYear(japaneseEra, i2)));
    }

    private JapaneseDate withYear(int i2) {
        return withYear(getEra(), i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate plusYears(long j2) {
        return with(this.isoDate.plusYears(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate plusMonths(long j2) {
        return with(this.isoDate.plusMonths(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate plusWeeks(long j2) {
        return with(this.isoDate.plusWeeks(j2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate plusDays(long j2) {
        return with(this.isoDate.plusDays(j2));
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public JapaneseDate plus(long j2, TemporalUnit temporalUnit) {
        return (JapaneseDate) super.plus(j2, temporalUnit);
    }

    @Override // java.time.chrono.ChronoLocalDateImpl, java.time.chrono.ChronoLocalDate, java.time.temporal.Temporal
    public JapaneseDate minus(long j2, TemporalUnit temporalUnit) {
        return (JapaneseDate) super.minus(j2, temporalUnit);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate minusYears(long j2) {
        return (JapaneseDate) super.minusYears(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate minusMonths(long j2) {
        return (JapaneseDate) super.minusMonths(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate minusWeeks(long j2) {
        return (JapaneseDate) super.minusWeeks(j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.time.chrono.ChronoLocalDateImpl
    public JapaneseDate minusDays(long j2) {
        return (JapaneseDate) super.minusDays(j2);
    }

    private JapaneseDate with(LocalDate localDate) {
        return localDate.equals(this.isoDate) ? this : new JapaneseDate(localDate);
    }

    @Override // java.time.chrono.ChronoLocalDate
    public final ChronoLocalDateTime<JapaneseDate> atTime(LocalTime localTime) {
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
        if (obj instanceof JapaneseDate) {
            return this.isoDate.equals(((JapaneseDate) obj).isoDate);
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
        return new Ser((byte) 4, this);
    }

    void writeExternal(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(get(ChronoField.YEAR));
        dataOutput.writeByte(get(ChronoField.MONTH_OF_YEAR));
        dataOutput.writeByte(get(ChronoField.DAY_OF_MONTH));
    }

    static JapaneseDate readExternal(DataInput dataInput) throws IOException {
        return JapaneseChronology.INSTANCE.date(dataInput.readInt(), (int) dataInput.readByte(), (int) dataInput.readByte());
    }
}
