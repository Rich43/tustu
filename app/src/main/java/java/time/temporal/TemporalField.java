package java.time.temporal;

import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/* loaded from: rt.jar:java/time/temporal/TemporalField.class */
public interface TemporalField {
    TemporalUnit getBaseUnit();

    TemporalUnit getRangeUnit();

    ValueRange range();

    boolean isDateBased();

    boolean isTimeBased();

    boolean isSupportedBy(TemporalAccessor temporalAccessor);

    ValueRange rangeRefinedBy(TemporalAccessor temporalAccessor);

    long getFrom(TemporalAccessor temporalAccessor);

    <R extends Temporal> R adjustInto(R r2, long j2);

    String toString();

    default String getDisplayName(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        return toString();
    }

    default TemporalAccessor resolve(Map<TemporalField, Long> map, TemporalAccessor temporalAccessor, ResolverStyle resolverStyle) {
        return null;
    }
}
