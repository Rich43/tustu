package java.time.format;

import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: rt.jar:java/time/format/DateTimeParseContext.class */
final class DateTimeParseContext {
    private DateTimeFormatter formatter;
    private boolean caseSensitive = true;
    private boolean strict = true;
    private final ArrayList<Parsed> parsed = new ArrayList<>();
    private ArrayList<Consumer<Chronology>> chronoListeners = null;

    DateTimeParseContext(DateTimeFormatter dateTimeFormatter) {
        this.formatter = dateTimeFormatter;
        this.parsed.add(new Parsed());
    }

    DateTimeParseContext copy() {
        DateTimeParseContext dateTimeParseContext = new DateTimeParseContext(this.formatter);
        dateTimeParseContext.caseSensitive = this.caseSensitive;
        dateTimeParseContext.strict = this.strict;
        return dateTimeParseContext;
    }

    Locale getLocale() {
        return this.formatter.getLocale();
    }

    DecimalStyle getDecimalStyle() {
        return this.formatter.getDecimalStyle();
    }

    Chronology getEffectiveChronology() {
        Chronology chronology = currentParsed().chrono;
        if (chronology == null) {
            chronology = this.formatter.getChronology();
            if (chronology == null) {
                chronology = IsoChronology.INSTANCE;
            }
        }
        return chronology;
    }

    boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    void setCaseSensitive(boolean z2) {
        this.caseSensitive = z2;
    }

    boolean subSequenceEquals(CharSequence charSequence, int i2, CharSequence charSequence2, int i3, int i4) {
        if (i2 + i4 > charSequence.length() || i3 + i4 > charSequence2.length()) {
            return false;
        }
        if (isCaseSensitive()) {
            for (int i5 = 0; i5 < i4; i5++) {
                if (charSequence.charAt(i2 + i5) != charSequence2.charAt(i3 + i5)) {
                    return false;
                }
            }
            return true;
        }
        for (int i6 = 0; i6 < i4; i6++) {
            char cCharAt = charSequence.charAt(i2 + i6);
            char cCharAt2 = charSequence2.charAt(i3 + i6);
            if (cCharAt != cCharAt2 && Character.toUpperCase(cCharAt) != Character.toUpperCase(cCharAt2) && Character.toLowerCase(cCharAt) != Character.toLowerCase(cCharAt2)) {
                return false;
            }
        }
        return true;
    }

    boolean charEquals(char c2, char c3) {
        if (isCaseSensitive()) {
            return c2 == c3;
        }
        return charEqualsIgnoreCase(c2, c3);
    }

    static boolean charEqualsIgnoreCase(char c2, char c3) {
        return c2 == c3 || Character.toUpperCase(c2) == Character.toUpperCase(c3) || Character.toLowerCase(c2) == Character.toLowerCase(c3);
    }

    boolean isStrict() {
        return this.strict;
    }

    void setStrict(boolean z2) {
        this.strict = z2;
    }

    void startOptional() {
        this.parsed.add(currentParsed().copy());
    }

    void endOptional(boolean z2) {
        if (z2) {
            this.parsed.remove(this.parsed.size() - 2);
        } else {
            this.parsed.remove(this.parsed.size() - 1);
        }
    }

    private Parsed currentParsed() {
        return this.parsed.get(this.parsed.size() - 1);
    }

    Parsed toUnresolved() {
        return currentParsed();
    }

    TemporalAccessor toResolved(ResolverStyle resolverStyle, Set<TemporalField> set) {
        Parsed parsedCurrentParsed = currentParsed();
        parsedCurrentParsed.chrono = getEffectiveChronology();
        parsedCurrentParsed.zone = parsedCurrentParsed.zone != null ? parsedCurrentParsed.zone : this.formatter.getZone();
        return parsedCurrentParsed.resolve(resolverStyle, set);
    }

    Long getParsed(TemporalField temporalField) {
        return currentParsed().fieldValues.get(temporalField);
    }

    int setParsedField(TemporalField temporalField, long j2, int i2, int i3) {
        Objects.requireNonNull(temporalField, "field");
        Long lPut = currentParsed().fieldValues.put(temporalField, Long.valueOf(j2));
        return (lPut == null || lPut.longValue() == j2) ? i3 : i2 ^ (-1);
    }

    void setParsed(Chronology chronology) {
        Objects.requireNonNull(chronology, "chrono");
        currentParsed().chrono = chronology;
        if (this.chronoListeners != null && !this.chronoListeners.isEmpty()) {
            Consumer[] consumerArr = (Consumer[]) this.chronoListeners.toArray(new Consumer[1]);
            this.chronoListeners.clear();
            for (Consumer consumer : consumerArr) {
                consumer.accept(chronology);
            }
        }
    }

    void addChronoChangedListener(Consumer<Chronology> consumer) {
        if (this.chronoListeners == null) {
            this.chronoListeners = new ArrayList<>();
        }
        this.chronoListeners.add(consumer);
    }

    void setParsed(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        currentParsed().zone = zoneId;
    }

    void setParsedLeapSecond() {
        currentParsed().leapSecond = true;
    }

    public String toString() {
        return currentParsed().toString();
    }
}
