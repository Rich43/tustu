package java.time.format;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.ValueRange;
import java.util.Locale;
import java.util.Objects;

/* loaded from: rt.jar:java/time/format/DateTimePrintContext.class */
final class DateTimePrintContext {
    private TemporalAccessor temporal;
    private DateTimeFormatter formatter;
    private int optional;

    DateTimePrintContext(TemporalAccessor temporalAccessor, DateTimeFormatter dateTimeFormatter) {
        this.temporal = adjust(temporalAccessor, dateTimeFormatter);
        this.formatter = dateTimeFormatter;
    }

    private static TemporalAccessor adjust(final TemporalAccessor temporalAccessor, DateTimeFormatter dateTimeFormatter) {
        ChronoLocalDate chronoLocalDateDate;
        Chronology chronology = dateTimeFormatter.getChronology();
        ZoneId zone = dateTimeFormatter.getZone();
        if (chronology == null && zone == null) {
            return temporalAccessor;
        }
        Chronology chronology2 = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zoneId());
        if (Objects.equals(chronology, chronology2)) {
            chronology = null;
        }
        if (Objects.equals(zone, zoneId)) {
            zone = null;
        }
        if (chronology == null && zone == null) {
            return temporalAccessor;
        }
        final Chronology chronology3 = chronology != null ? chronology : chronology2;
        if (zone != null) {
            if (temporalAccessor.isSupported(ChronoField.INSTANT_SECONDS)) {
                return (chronology3 != null ? chronology3 : IsoChronology.INSTANCE).zonedDateTime(Instant.from(temporalAccessor), zone);
            }
            if ((zone.normalized() instanceof ZoneOffset) && temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS) && temporalAccessor.get(ChronoField.OFFSET_SECONDS) != zone.getRules().getOffset(Instant.EPOCH).getTotalSeconds()) {
                throw new DateTimeException("Unable to apply override zone '" + ((Object) zone) + "' because the temporal object being formatted has a different offset but does not represent an instant: " + ((Object) temporalAccessor));
            }
        }
        final ZoneId zoneId2 = zone != null ? zone : zoneId;
        if (chronology != null) {
            if (temporalAccessor.isSupported(ChronoField.EPOCH_DAY)) {
                chronoLocalDateDate = chronology3.date(temporalAccessor);
            } else {
                if (chronology != IsoChronology.INSTANCE || chronology2 != null) {
                    for (ChronoField chronoField : ChronoField.values()) {
                        if (chronoField.isDateBased() && temporalAccessor.isSupported(chronoField)) {
                            throw new DateTimeException("Unable to apply override chronology '" + ((Object) chronology) + "' because the temporal object being formatted contains date fields but does not represent a whole date: " + ((Object) temporalAccessor));
                        }
                    }
                }
                chronoLocalDateDate = null;
            }
        } else {
            chronoLocalDateDate = null;
        }
        final ChronoLocalDate chronoLocalDate = chronoLocalDateDate;
        return new TemporalAccessor() { // from class: java.time.format.DateTimePrintContext.1
            @Override // java.time.temporal.TemporalAccessor
            public boolean isSupported(TemporalField temporalField) {
                if (chronoLocalDate != null && temporalField.isDateBased()) {
                    return chronoLocalDate.isSupported(temporalField);
                }
                return temporalAccessor.isSupported(temporalField);
            }

            @Override // java.time.temporal.TemporalAccessor
            public ValueRange range(TemporalField temporalField) {
                if (chronoLocalDate != null && temporalField.isDateBased()) {
                    return chronoLocalDate.range(temporalField);
                }
                return temporalAccessor.range(temporalField);
            }

            @Override // java.time.temporal.TemporalAccessor
            public long getLong(TemporalField temporalField) {
                if (chronoLocalDate != null && temporalField.isDateBased()) {
                    return chronoLocalDate.getLong(temporalField);
                }
                return temporalAccessor.getLong(temporalField);
            }

            @Override // java.time.temporal.TemporalAccessor
            public <R> R query(TemporalQuery<R> temporalQuery) {
                if (temporalQuery == TemporalQueries.chronology()) {
                    return (R) chronology3;
                }
                if (temporalQuery == TemporalQueries.zoneId()) {
                    return (R) zoneId2;
                }
                if (temporalQuery == TemporalQueries.precision()) {
                    return (R) temporalAccessor.query(temporalQuery);
                }
                return temporalQuery.queryFrom(this);
            }
        };
    }

    TemporalAccessor getTemporal() {
        return this.temporal;
    }

    Locale getLocale() {
        return this.formatter.getLocale();
    }

    DecimalStyle getDecimalStyle() {
        return this.formatter.getDecimalStyle();
    }

    void startOptional() {
        this.optional++;
    }

    void endOptional() {
        this.optional--;
    }

    <R> R getValue(TemporalQuery<R> temporalQuery) {
        R r2 = (R) this.temporal.query(temporalQuery);
        if (r2 == null && this.optional == 0) {
            throw new DateTimeException("Unable to extract value: " + ((Object) this.temporal.getClass()));
        }
        return r2;
    }

    Long getValue(TemporalField temporalField) {
        try {
            return Long.valueOf(this.temporal.getLong(temporalField));
        } catch (DateTimeException e2) {
            if (this.optional > 0) {
                return null;
            }
            throw e2;
        }
    }

    public String toString() {
        return this.temporal.toString();
    }
}
