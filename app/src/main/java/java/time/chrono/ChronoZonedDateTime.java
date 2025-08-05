package java.time.chrono;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Comparator;
import java.util.Objects;

/* loaded from: rt.jar:java/time/chrono/ChronoZonedDateTime.class */
public interface ChronoZonedDateTime<D extends ChronoLocalDate> extends Temporal, Comparable<ChronoZonedDateTime<?>> {
    ChronoLocalDateTime<D> toLocalDateTime();

    ZoneOffset getOffset();

    ZoneId getZone();

    ChronoZonedDateTime<D> withEarlierOffsetAtOverlap();

    ChronoZonedDateTime<D> withLaterOffsetAtOverlap();

    ChronoZonedDateTime<D> withZoneSameLocal(ZoneId zoneId);

    ChronoZonedDateTime<D> withZoneSameInstant(ZoneId zoneId);

    @Override // java.time.temporal.TemporalAccessor
    boolean isSupported(TemporalField temporalField);

    @Override // java.time.temporal.Temporal
    ChronoZonedDateTime<D> with(TemporalField temporalField, long j2);

    @Override // java.time.temporal.Temporal
    ChronoZonedDateTime<D> plus(long j2, TemporalUnit temporalUnit);

    boolean equals(Object obj);

    int hashCode();

    String toString();

    static Comparator<ChronoZonedDateTime<?>> timeLineOrder() {
        return AbstractChronology.INSTANT_ORDER;
    }

    static ChronoZonedDateTime<?> from(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ChronoZonedDateTime) {
            return (ChronoZonedDateTime) temporalAccessor;
        }
        Objects.requireNonNull(temporalAccessor, "temporal");
        Chronology chronology = (Chronology) temporalAccessor.query(TemporalQueries.chronology());
        if (chronology == null) {
            throw new DateTimeException("Unable to obtain ChronoZonedDateTime from TemporalAccessor: " + ((Object) temporalAccessor.getClass()));
        }
        return chronology.zonedDateTime(temporalAccessor);
    }

    @Override // java.time.temporal.TemporalAccessor
    default ValueRange range(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            if (temporalField == ChronoField.INSTANT_SECONDS || temporalField == ChronoField.OFFSET_SECONDS) {
                return temporalField.range();
            }
            return toLocalDateTime().range(temporalField);
        }
        return temporalField.rangeRefinedBy(this);
    }

    @Override // java.time.temporal.TemporalAccessor
    default int get(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case INSTANT_SECONDS:
                    throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead");
                case OFFSET_SECONDS:
                    return getOffset().getTotalSeconds();
                default:
                    return toLocalDateTime().get(temporalField);
            }
        }
        return super.get(temporalField);
    }

    @Override // java.time.temporal.TemporalAccessor
    default long getLong(TemporalField temporalField) {
        if (temporalField instanceof ChronoField) {
            switch ((ChronoField) temporalField) {
                case INSTANT_SECONDS:
                    return toEpochSecond();
                case OFFSET_SECONDS:
                    return getOffset().getTotalSeconds();
                default:
                    return toLocalDateTime().getLong(temporalField);
            }
        }
        return temporalField.getFrom(this);
    }

    default D toLocalDate() {
        return (D) toLocalDateTime().toLocalDate();
    }

    default LocalTime toLocalTime() {
        return toLocalDateTime().toLocalTime();
    }

    default Chronology getChronology() {
        return toLocalDate().getChronology();
    }

    @Override // java.time.temporal.Temporal
    default boolean isSupported(TemporalUnit temporalUnit) {
        return temporalUnit instanceof ChronoUnit ? temporalUnit != ChronoUnit.FOREVER : temporalUnit != null && temporalUnit.isSupportedBy(this);
    }

    @Override // java.time.temporal.Temporal
    default ChronoZonedDateTime<D> with(TemporalAdjuster temporalAdjuster) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.with(temporalAdjuster));
    }

    @Override // java.time.temporal.Temporal
    default ChronoZonedDateTime<D> plus(TemporalAmount temporalAmount) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.plus(temporalAmount));
    }

    @Override // java.time.temporal.Temporal
    default ChronoZonedDateTime<D> minus(TemporalAmount temporalAmount) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.minus(temporalAmount));
    }

    @Override // java.time.temporal.Temporal
    default ChronoZonedDateTime<D> minus(long j2, TemporalUnit temporalUnit) {
        return ChronoZonedDateTimeImpl.ensureValid(getChronology(), super.minus(j2, temporalUnit));
    }

    @Override // java.time.temporal.TemporalAccessor
    default <R> R query(TemporalQuery<R> temporalQuery) {
        if (temporalQuery == TemporalQueries.zone() || temporalQuery == TemporalQueries.zoneId()) {
            return (R) getZone();
        }
        if (temporalQuery == TemporalQueries.offset()) {
            return (R) getOffset();
        }
        if (temporalQuery == TemporalQueries.localTime()) {
            return (R) toLocalTime();
        }
        if (temporalQuery == TemporalQueries.chronology()) {
            return (R) getChronology();
        }
        if (temporalQuery == TemporalQueries.precision()) {
            return (R) ChronoUnit.NANOS;
        }
        return temporalQuery.queryFrom(this);
    }

    default String format(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        return dateTimeFormatter.format(this);
    }

    default Instant toInstant() {
        return Instant.ofEpochSecond(toEpochSecond(), toLocalTime().getNano());
    }

    default long toEpochSecond() {
        return ((toLocalDate().toEpochDay() * 86400) + toLocalTime().toSecondOfDay()) - getOffset().getTotalSeconds();
    }

    @Override // java.lang.Comparable
    default int compareTo(ChronoZonedDateTime<?> chronoZonedDateTime) {
        int iCompare = Long.compare(toEpochSecond(), chronoZonedDateTime.toEpochSecond());
        if (iCompare == 0) {
            iCompare = toLocalTime().getNano() - chronoZonedDateTime.toLocalTime().getNano();
            if (iCompare == 0) {
                iCompare = toLocalDateTime().compareTo(chronoZonedDateTime.toLocalDateTime());
                if (iCompare == 0) {
                    iCompare = getZone().getId().compareTo(chronoZonedDateTime.getZone().getId());
                    if (iCompare == 0) {
                        iCompare = getChronology().compareTo(chronoZonedDateTime.getChronology());
                    }
                }
            }
        }
        return iCompare;
    }

    default boolean isBefore(ChronoZonedDateTime<?> chronoZonedDateTime) {
        long epochSecond = toEpochSecond();
        long epochSecond2 = chronoZonedDateTime.toEpochSecond();
        return epochSecond < epochSecond2 || (epochSecond == epochSecond2 && toLocalTime().getNano() < chronoZonedDateTime.toLocalTime().getNano());
    }

    default boolean isAfter(ChronoZonedDateTime<?> chronoZonedDateTime) {
        long epochSecond = toEpochSecond();
        long epochSecond2 = chronoZonedDateTime.toEpochSecond();
        return epochSecond > epochSecond2 || (epochSecond == epochSecond2 && toLocalTime().getNano() > chronoZonedDateTime.toLocalTime().getNano());
    }

    default boolean isEqual(ChronoZonedDateTime<?> chronoZonedDateTime) {
        return toEpochSecond() == chronoZonedDateTime.toEpochSecond() && toLocalTime().getNano() == chronoZonedDateTime.toLocalTime().getNano();
    }
}
