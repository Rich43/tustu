package java.time.chrono;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/IsoChronology.class */
public final class IsoChronology extends AbstractChronology implements Serializable {
    public static final IsoChronology INSTANCE = new IsoChronology();
    private static final long serialVersionUID = -1440403870442975015L;

    @Override // java.time.chrono.AbstractChronology
    /* bridge */ /* synthetic */ ChronoLocalDate resolveYMD(Map map, ResolverStyle resolverStyle) {
        return resolveYMD((Map<TemporalField, Long>) map, resolverStyle);
    }

    @Override // java.time.chrono.AbstractChronology
    /* bridge */ /* synthetic */ ChronoLocalDate resolveYearOfEra(Map map, ResolverStyle resolverStyle) {
        return resolveYearOfEra((Map<TemporalField, Long>) map, resolverStyle);
    }

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public /* bridge */ /* synthetic */ ChronoLocalDate resolveDate(Map map, ResolverStyle resolverStyle) {
        return resolveDate((Map<TemporalField, Long>) map, resolverStyle);
    }

    private IsoChronology() {
    }

    @Override // java.time.chrono.Chronology
    public String getId() {
        return "ISO";
    }

    @Override // java.time.chrono.Chronology
    public String getCalendarType() {
        return "iso8601";
    }

    @Override // java.time.chrono.Chronology
    public LocalDate date(Era era, int i2, int i3, int i4) {
        return date(prolepticYear(era, i2), i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public LocalDate date(int i2, int i3, int i4) {
        return LocalDate.of(i2, i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public LocalDate dateYearDay(Era era, int i2, int i3) {
        return dateYearDay(prolepticYear(era, i2), i3);
    }

    @Override // java.time.chrono.Chronology
    public LocalDate dateYearDay(int i2, int i3) {
        return LocalDate.ofYearDay(i2, i3);
    }

    @Override // java.time.chrono.Chronology
    public LocalDate dateEpochDay(long j2) {
        return LocalDate.ofEpochDay(j2);
    }

    @Override // java.time.chrono.Chronology
    public LocalDate date(TemporalAccessor temporalAccessor) {
        return LocalDate.from(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public LocalDateTime localDateTime(TemporalAccessor temporalAccessor) {
        return LocalDateTime.from(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ZonedDateTime zonedDateTime(TemporalAccessor temporalAccessor) {
        return ZonedDateTime.from(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ZonedDateTime zonedDateTime(Instant instant, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    @Override // java.time.chrono.Chronology
    public LocalDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    @Override // java.time.chrono.Chronology
    public LocalDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    @Override // java.time.chrono.Chronology
    public LocalDate dateNow(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    @Override // java.time.chrono.Chronology
    public boolean isLeapYear(long j2) {
        return (j2 & 3) == 0 && (j2 % 100 != 0 || j2 % 400 == 0);
    }

    @Override // java.time.chrono.Chronology
    public int prolepticYear(Era era, int i2) {
        if (era instanceof IsoEra) {
            return era == IsoEra.CE ? i2 : 1 - i2;
        }
        throw new ClassCastException("Era must be IsoEra");
    }

    @Override // java.time.chrono.Chronology
    public IsoEra eraOf(int i2) {
        return IsoEra.of(i2);
    }

    @Override // java.time.chrono.Chronology
    public List<Era> eras() {
        return Arrays.asList(IsoEra.values());
    }

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public LocalDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (LocalDate) super.resolveDate(map, resolverStyle);
    }

    @Override // java.time.chrono.AbstractChronology
    void resolveProlepticMonth(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        Long lRemove = map.remove(ChronoField.PROLEPTIC_MONTH);
        if (lRemove != null) {
            if (resolverStyle != ResolverStyle.LENIENT) {
                ChronoField.PROLEPTIC_MONTH.checkValidValue(lRemove.longValue());
            }
            addFieldValue(map, ChronoField.MONTH_OF_YEAR, Math.floorMod(lRemove.longValue(), 12L) + 1);
            addFieldValue(map, ChronoField.YEAR, Math.floorDiv(lRemove.longValue(), 12L));
        }
    }

    @Override // java.time.chrono.AbstractChronology
    LocalDate resolveYearOfEra(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        Long lRemove = map.remove(ChronoField.YEAR_OF_ERA);
        if (lRemove == null) {
            if (map.containsKey(ChronoField.ERA)) {
                ChronoField.ERA.checkValidValue(map.get(ChronoField.ERA).longValue());
                return null;
            }
            return null;
        }
        if (resolverStyle != ResolverStyle.LENIENT) {
            ChronoField.YEAR_OF_ERA.checkValidValue(lRemove.longValue());
        }
        Long lRemove2 = map.remove(ChronoField.ERA);
        if (lRemove2 != null) {
            if (lRemove2.longValue() == 1) {
                addFieldValue(map, ChronoField.YEAR, lRemove.longValue());
                return null;
            }
            if (lRemove2.longValue() == 0) {
                addFieldValue(map, ChronoField.YEAR, Math.subtractExact(1L, lRemove.longValue()));
                return null;
            }
            throw new DateTimeException("Invalid value for era: " + ((Object) lRemove2));
        }
        Long l2 = map.get(ChronoField.YEAR);
        if (resolverStyle == ResolverStyle.STRICT) {
            if (l2 != null) {
                addFieldValue(map, ChronoField.YEAR, l2.longValue() > 0 ? lRemove.longValue() : Math.subtractExact(1L, lRemove.longValue()));
                return null;
            }
            map.put(ChronoField.YEAR_OF_ERA, lRemove);
            return null;
        }
        addFieldValue(map, ChronoField.YEAR, (l2 == null || l2.longValue() > 0) ? lRemove.longValue() : Math.subtractExact(1L, lRemove.longValue()));
        return null;
    }

    @Override // java.time.chrono.AbstractChronology
    LocalDate resolveYMD(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        int iCheckValidIntValue = ChronoField.YEAR.checkValidIntValue(map.remove(ChronoField.YEAR).longValue());
        if (resolverStyle == ResolverStyle.LENIENT) {
            return LocalDate.of(iCheckValidIntValue, 1, 1).plusMonths(Math.subtractExact(map.remove(ChronoField.MONTH_OF_YEAR).longValue(), 1L)).plusDays(Math.subtractExact(map.remove(ChronoField.DAY_OF_MONTH).longValue(), 1L));
        }
        int iCheckValidIntValue2 = ChronoField.MONTH_OF_YEAR.checkValidIntValue(map.remove(ChronoField.MONTH_OF_YEAR).longValue());
        int iCheckValidIntValue3 = ChronoField.DAY_OF_MONTH.checkValidIntValue(map.remove(ChronoField.DAY_OF_MONTH).longValue());
        if (resolverStyle == ResolverStyle.SMART) {
            if (iCheckValidIntValue2 == 4 || iCheckValidIntValue2 == 6 || iCheckValidIntValue2 == 9 || iCheckValidIntValue2 == 11) {
                iCheckValidIntValue3 = Math.min(iCheckValidIntValue3, 30);
            } else if (iCheckValidIntValue2 == 2) {
                iCheckValidIntValue3 = Math.min(iCheckValidIntValue3, Month.FEBRUARY.length(Year.isLeap(iCheckValidIntValue)));
            }
        }
        return LocalDate.of(iCheckValidIntValue, iCheckValidIntValue2, iCheckValidIntValue3);
    }

    @Override // java.time.chrono.Chronology
    public ValueRange range(ChronoField chronoField) {
        return chronoField.range();
    }

    @Override // java.time.chrono.Chronology
    public Period period(int i2, int i3, int i4) {
        return Period.of(i2, i3, i4);
    }

    @Override // java.time.chrono.AbstractChronology
    Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
