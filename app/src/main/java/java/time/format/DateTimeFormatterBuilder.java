package java.time.format;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.glassfish.external.amx.AMX;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeTextProvider;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.time.zone.ZoneRulesProvider;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder.class */
public final class DateTimeFormatterBuilder {
    private DateTimeFormatterBuilder active;
    private final DateTimeFormatterBuilder parent;
    private final List<DateTimePrinterParser> printerParsers;
    private final boolean optional;
    private int padNextWidth;
    private char padNextChar;
    private int valueParserIndex;
    static final Comparator<String> LENGTH_SORT;
    private static final TemporalQuery<ZoneId> QUERY_REGION_ONLY = temporalAccessor -> {
        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zoneId());
        if (zoneId == null || (zoneId instanceof ZoneOffset)) {
            return null;
        }
        return zoneId;
    };
    private static final Map<Character, TemporalField> FIELD_MAP = new HashMap();

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$DateTimePrinterParser.class */
    interface DateTimePrinterParser {
        boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb);

        int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2);
    }

    static {
        FIELD_MAP.put('G', ChronoField.ERA);
        FIELD_MAP.put('y', ChronoField.YEAR_OF_ERA);
        FIELD_MAP.put('u', ChronoField.YEAR);
        FIELD_MAP.put('Q', IsoFields.QUARTER_OF_YEAR);
        FIELD_MAP.put('q', IsoFields.QUARTER_OF_YEAR);
        FIELD_MAP.put('M', ChronoField.MONTH_OF_YEAR);
        FIELD_MAP.put('L', ChronoField.MONTH_OF_YEAR);
        FIELD_MAP.put('D', ChronoField.DAY_OF_YEAR);
        FIELD_MAP.put('d', ChronoField.DAY_OF_MONTH);
        FIELD_MAP.put('F', ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        FIELD_MAP.put('E', ChronoField.DAY_OF_WEEK);
        FIELD_MAP.put('c', ChronoField.DAY_OF_WEEK);
        FIELD_MAP.put('e', ChronoField.DAY_OF_WEEK);
        FIELD_MAP.put('a', ChronoField.AMPM_OF_DAY);
        FIELD_MAP.put('H', ChronoField.HOUR_OF_DAY);
        FIELD_MAP.put('k', ChronoField.CLOCK_HOUR_OF_DAY);
        FIELD_MAP.put('K', ChronoField.HOUR_OF_AMPM);
        FIELD_MAP.put('h', ChronoField.CLOCK_HOUR_OF_AMPM);
        FIELD_MAP.put('m', ChronoField.MINUTE_OF_HOUR);
        FIELD_MAP.put('s', ChronoField.SECOND_OF_MINUTE);
        FIELD_MAP.put('S', ChronoField.NANO_OF_SECOND);
        FIELD_MAP.put('A', ChronoField.MILLI_OF_DAY);
        FIELD_MAP.put('n', ChronoField.NANO_OF_SECOND);
        FIELD_MAP.put('N', ChronoField.NANO_OF_DAY);
        LENGTH_SORT = new Comparator<String>() { // from class: java.time.format.DateTimeFormatterBuilder.2
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                return str.length() == str2.length() ? str.compareTo(str2) : str.length() - str2.length();
            }
        };
    }

    public static String getLocalizedDateTimePattern(FormatStyle formatStyle, FormatStyle formatStyle2, Chronology chronology, Locale locale) {
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(chronology, "chrono");
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either dateStyle or timeStyle must be non-null");
        }
        return LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(locale).getJavaTimeDateTimePattern(convertStyle(formatStyle2), convertStyle(formatStyle), chronology.getCalendarType());
    }

    private static int convertStyle(FormatStyle formatStyle) {
        if (formatStyle == null) {
            return -1;
        }
        return formatStyle.ordinal();
    }

    public DateTimeFormatterBuilder() {
        this.active = this;
        this.printerParsers = new ArrayList();
        this.valueParserIndex = -1;
        this.parent = null;
        this.optional = false;
    }

    private DateTimeFormatterBuilder(DateTimeFormatterBuilder dateTimeFormatterBuilder, boolean z2) {
        this.active = this;
        this.printerParsers = new ArrayList();
        this.valueParserIndex = -1;
        this.parent = dateTimeFormatterBuilder;
        this.optional = z2;
    }

    public DateTimeFormatterBuilder parseCaseSensitive() {
        appendInternal(SettingsParser.SENSITIVE);
        return this;
    }

    public DateTimeFormatterBuilder parseCaseInsensitive() {
        appendInternal(SettingsParser.INSENSITIVE);
        return this;
    }

    public DateTimeFormatterBuilder parseStrict() {
        appendInternal(SettingsParser.STRICT);
        return this;
    }

    public DateTimeFormatterBuilder parseLenient() {
        appendInternal(SettingsParser.LENIENT);
        return this;
    }

    public DateTimeFormatterBuilder parseDefaulting(TemporalField temporalField, long j2) {
        Objects.requireNonNull(temporalField, "field");
        appendInternal(new DefaultValueParser(temporalField, j2));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField) {
        Objects.requireNonNull(temporalField, "field");
        appendValue(new NumberPrinterParser(temporalField, 1, 19, SignStyle.NORMAL));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField, int i2) {
        Objects.requireNonNull(temporalField, "field");
        if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + i2);
        }
        appendValue(new NumberPrinterParser(temporalField, i2, i2, SignStyle.NOT_NEGATIVE));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField, int i2, int i3, SignStyle signStyle) {
        if (i2 == i3 && signStyle == SignStyle.NOT_NEGATIVE) {
            return appendValue(temporalField, i3);
        }
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(signStyle, "signStyle");
        if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + i2);
        }
        if (i3 < 1 || i3 > 19) {
            throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + i3);
        }
        if (i3 < i2) {
            throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + i3 + " < " + i2);
        }
        appendValue(new NumberPrinterParser(temporalField, i2, i3, signStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendValueReduced(TemporalField temporalField, int i2, int i3, int i4) {
        Objects.requireNonNull(temporalField, "field");
        appendValue(new ReducedPrinterParser(temporalField, i2, i3, i4, null));
        return this;
    }

    public DateTimeFormatterBuilder appendValueReduced(TemporalField temporalField, int i2, int i3, ChronoLocalDate chronoLocalDate) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(chronoLocalDate, "baseDate");
        appendValue(new ReducedPrinterParser(temporalField, i2, i3, 0, chronoLocalDate));
        return this;
    }

    private DateTimeFormatterBuilder appendValue(NumberPrinterParser numberPrinterParser) {
        NumberPrinterParser numberPrinterParserWithFixedWidth;
        if (this.active.valueParserIndex >= 0) {
            int i2 = this.active.valueParserIndex;
            NumberPrinterParser numberPrinterParser2 = (NumberPrinterParser) this.active.printerParsers.get(i2);
            if (numberPrinterParser.minWidth == numberPrinterParser.maxWidth && numberPrinterParser.signStyle == SignStyle.NOT_NEGATIVE) {
                numberPrinterParserWithFixedWidth = numberPrinterParser2.withSubsequentWidth(numberPrinterParser.maxWidth);
                appendInternal(numberPrinterParser.withFixedWidth());
                this.active.valueParserIndex = i2;
            } else {
                numberPrinterParserWithFixedWidth = numberPrinterParser2.withFixedWidth();
                this.active.valueParserIndex = appendInternal(numberPrinterParser);
            }
            this.active.printerParsers.set(i2, numberPrinterParserWithFixedWidth);
        } else {
            this.active.valueParserIndex = appendInternal(numberPrinterParser);
        }
        return this;
    }

    public DateTimeFormatterBuilder appendFraction(TemporalField temporalField, int i2, int i3, boolean z2) {
        appendInternal(new FractionPrinterParser(temporalField, i2, i3, z2));
        return this;
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField) {
        return appendText(temporalField, TextStyle.FULL);
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField, TextStyle textStyle) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(textStyle, "textStyle");
        appendInternal(new TextPrinterParser(temporalField, textStyle, DateTimeTextProvider.getInstance()));
        return this;
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField, Map<Long, String> map) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(map, "textLookup");
        final DateTimeTextProvider.LocaleStore localeStore = new DateTimeTextProvider.LocaleStore(Collections.singletonMap(TextStyle.FULL, new LinkedHashMap(map)));
        appendInternal(new TextPrinterParser(temporalField, TextStyle.FULL, new DateTimeTextProvider() { // from class: java.time.format.DateTimeFormatterBuilder.1
            @Override // java.time.format.DateTimeTextProvider
            public String getText(Chronology chronology, TemporalField temporalField2, long j2, TextStyle textStyle, Locale locale) {
                return localeStore.getText(j2, textStyle);
            }

            @Override // java.time.format.DateTimeTextProvider
            public String getText(TemporalField temporalField2, long j2, TextStyle textStyle, Locale locale) {
                return localeStore.getText(j2, textStyle);
            }

            @Override // java.time.format.DateTimeTextProvider
            public Iterator<Map.Entry<String, Long>> getTextIterator(Chronology chronology, TemporalField temporalField2, TextStyle textStyle, Locale locale) {
                return localeStore.getTextIterator(textStyle);
            }

            @Override // java.time.format.DateTimeTextProvider
            public Iterator<Map.Entry<String, Long>> getTextIterator(TemporalField temporalField2, TextStyle textStyle, Locale locale) {
                return localeStore.getTextIterator(textStyle);
            }
        }));
        return this;
    }

    public DateTimeFormatterBuilder appendInstant() {
        appendInternal(new InstantPrinterParser(-2));
        return this;
    }

    public DateTimeFormatterBuilder appendInstant(int i2) {
        if (i2 < -1 || i2 > 9) {
            throw new IllegalArgumentException("The fractional digits must be from -1 to 9 inclusive but was " + i2);
        }
        appendInternal(new InstantPrinterParser(i2));
        return this;
    }

    public DateTimeFormatterBuilder appendOffsetId() {
        appendInternal(OffsetIdPrinterParser.INSTANCE_ID_Z);
        return this;
    }

    public DateTimeFormatterBuilder appendOffset(String str, String str2) {
        appendInternal(new OffsetIdPrinterParser(str, str2));
        return this;
    }

    public DateTimeFormatterBuilder appendLocalizedOffset(TextStyle textStyle) {
        Objects.requireNonNull(textStyle, Constants.ATTRNAME_STYLE);
        if (textStyle != TextStyle.FULL && textStyle != TextStyle.SHORT) {
            throw new IllegalArgumentException("Style must be either full or short");
        }
        appendInternal(new LocalizedOffsetIdPrinterParser(textStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneId() {
        appendInternal(new ZoneIdPrinterParser(TemporalQueries.zoneId(), "ZoneId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneRegionId() {
        appendInternal(new ZoneIdPrinterParser(QUERY_REGION_ONLY, "ZoneRegionId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneOrOffsetId() {
        appendInternal(new ZoneIdPrinterParser(TemporalQueries.zone(), "ZoneOrOffsetId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneText(TextStyle textStyle) {
        appendInternal(new ZoneTextPrinterParser(textStyle, null));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneText(TextStyle textStyle, Set<ZoneId> set) {
        Objects.requireNonNull(set, "preferredZones");
        appendInternal(new ZoneTextPrinterParser(textStyle, set));
        return this;
    }

    public DateTimeFormatterBuilder appendChronologyId() {
        appendInternal(new ChronoPrinterParser(null));
        return this;
    }

    public DateTimeFormatterBuilder appendChronologyText(TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");
        appendInternal(new ChronoPrinterParser(textStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendLocalized(FormatStyle formatStyle, FormatStyle formatStyle2) {
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either the date or time style must be non-null");
        }
        appendInternal(new LocalizedPrinterParser(formatStyle, formatStyle2));
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(char c2) {
        appendInternal(new CharLiteralPrinterParser(c2));
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(String str) {
        Objects.requireNonNull(str, "literal");
        if (str.length() > 0) {
            if (str.length() == 1) {
                appendInternal(new CharLiteralPrinterParser(str.charAt(0)));
            } else {
                appendInternal(new StringLiteralPrinterParser(str));
            }
        }
        return this;
    }

    public DateTimeFormatterBuilder append(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        appendInternal(dateTimeFormatter.toPrinterParser(false));
        return this;
    }

    public DateTimeFormatterBuilder appendOptional(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        appendInternal(dateTimeFormatter.toPrinterParser(true));
        return this;
    }

    public DateTimeFormatterBuilder appendPattern(String str) {
        Objects.requireNonNull(str, "pattern");
        parsePattern(str);
        return this;
    }

    private void parsePattern(String str) {
        int i2 = 0;
        while (i2 < str.length()) {
            char cCharAt = str.charAt(i2);
            if ((cCharAt >= 'A' && cCharAt <= 'Z') || (cCharAt >= 'a' && cCharAt <= 'z')) {
                int i3 = i2;
                int i4 = i2 + 1;
                while (i4 < str.length() && str.charAt(i4) == cCharAt) {
                    i4++;
                }
                int i5 = i4 - i3;
                if (cCharAt == 'p') {
                    int i6 = 0;
                    if (i4 < str.length()) {
                        cCharAt = str.charAt(i4);
                        if ((cCharAt >= 'A' && cCharAt <= 'Z') || (cCharAt >= 'a' && cCharAt <= 'z')) {
                            i6 = i5;
                            int i7 = i4;
                            i4++;
                            while (i4 < str.length() && str.charAt(i4) == cCharAt) {
                                i4++;
                            }
                            i5 = i4 - i7;
                        }
                    }
                    if (i6 == 0) {
                        throw new IllegalArgumentException("Pad letter 'p' must be followed by valid pad pattern: " + str);
                    }
                    padNext(i6);
                }
                TemporalField temporalField = FIELD_MAP.get(Character.valueOf(cCharAt));
                if (temporalField != null) {
                    parseField(cCharAt, i5, temporalField);
                } else if (cCharAt == 'z') {
                    if (i5 > 4) {
                        throw new IllegalArgumentException("Too many pattern letters: " + cCharAt);
                    }
                    if (i5 == 4) {
                        appendZoneText(TextStyle.FULL);
                    } else {
                        appendZoneText(TextStyle.SHORT);
                    }
                } else if (cCharAt == 'V') {
                    if (i5 != 2) {
                        throw new IllegalArgumentException("Pattern letter count must be 2: " + cCharAt);
                    }
                    appendZoneId();
                } else if (cCharAt == 'Z') {
                    if (i5 < 4) {
                        appendOffset("+HHMM", "+0000");
                    } else if (i5 == 4) {
                        appendLocalizedOffset(TextStyle.FULL);
                    } else if (i5 == 5) {
                        appendOffset("+HH:MM:ss", com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG);
                    } else {
                        throw new IllegalArgumentException("Too many pattern letters: " + cCharAt);
                    }
                } else if (cCharAt == 'O') {
                    if (i5 == 1) {
                        appendLocalizedOffset(TextStyle.SHORT);
                    } else if (i5 == 4) {
                        appendLocalizedOffset(TextStyle.FULL);
                    } else {
                        throw new IllegalArgumentException("Pattern letter count must be 1 or 4: " + cCharAt);
                    }
                } else if (cCharAt == 'X') {
                    if (i5 > 5) {
                        throw new IllegalArgumentException("Too many pattern letters: " + cCharAt);
                    }
                    appendOffset(OffsetIdPrinterParser.PATTERNS[i5 + (i5 == 1 ? 0 : 1)], com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG);
                } else if (cCharAt == 'x') {
                    if (i5 > 5) {
                        throw new IllegalArgumentException("Too many pattern letters: " + cCharAt);
                    }
                    appendOffset(OffsetIdPrinterParser.PATTERNS[i5 + (i5 == 1 ? 0 : 1)], i5 == 1 ? "+00" : i5 % 2 == 0 ? "+0000" : "+00:00");
                } else if (cCharAt == 'W') {
                    if (i5 > 1) {
                        throw new IllegalArgumentException("Too many pattern letters: " + cCharAt);
                    }
                    appendInternal(new WeekBasedFieldPrinterParser(cCharAt, i5));
                } else if (cCharAt == 'w') {
                    if (i5 > 2) {
                        throw new IllegalArgumentException("Too many pattern letters: " + cCharAt);
                    }
                    appendInternal(new WeekBasedFieldPrinterParser(cCharAt, i5));
                } else if (cCharAt == 'Y') {
                    appendInternal(new WeekBasedFieldPrinterParser(cCharAt, i5));
                } else {
                    throw new IllegalArgumentException("Unknown pattern letter: " + cCharAt);
                }
                i2 = i4 - 1;
            } else if (cCharAt == '\'') {
                int i8 = i2;
                i2++;
                while (i2 < str.length()) {
                    if (str.charAt(i2) == '\'') {
                        if (i2 + 1 >= str.length() || str.charAt(i2 + 1) != '\'') {
                            break;
                        } else {
                            i2++;
                        }
                    }
                    i2++;
                }
                if (i2 >= str.length()) {
                    throw new IllegalArgumentException("Pattern ends with an incomplete string literal: " + str);
                }
                String strSubstring = str.substring(i8 + 1, i2);
                if (strSubstring.length() == 0) {
                    appendLiteral('\'');
                } else {
                    appendLiteral(strSubstring.replace("''", PdfOps.SINGLE_QUOTE_TOKEN));
                }
            } else if (cCharAt == '[') {
                optionalStart();
            } else if (cCharAt == ']') {
                if (this.active.parent == null) {
                    throw new IllegalArgumentException("Pattern invalid as it contains ] without previous [");
                }
                optionalEnd();
            } else {
                if (cCharAt == '{' || cCharAt == '}' || cCharAt == '#') {
                    throw new IllegalArgumentException("Pattern includes reserved character: '" + cCharAt + PdfOps.SINGLE_QUOTE_TOKEN);
                }
                appendLiteral(cCharAt);
            }
            i2++;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parseField(char r7, int r8, java.time.temporal.TemporalField r9) {
        /*
            Method dump skipped, instructions count: 844
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.parseField(char, int, java.time.temporal.TemporalField):void");
    }

    public DateTimeFormatterBuilder padNext(int i2) {
        return padNext(i2, ' ');
    }

    public DateTimeFormatterBuilder padNext(int i2, char c2) {
        if (i2 < 1) {
            throw new IllegalArgumentException("The pad width must be at least one but was " + i2);
        }
        this.active.padNextWidth = i2;
        this.active.padNextChar = c2;
        this.active.valueParserIndex = -1;
        return this;
    }

    public DateTimeFormatterBuilder optionalStart() {
        this.active.valueParserIndex = -1;
        this.active = new DateTimeFormatterBuilder(this.active, true);
        return this;
    }

    public DateTimeFormatterBuilder optionalEnd() {
        if (this.active.parent == null) {
            throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
        }
        if (this.active.printerParsers.size() > 0) {
            CompositePrinterParser compositePrinterParser = new CompositePrinterParser(this.active.printerParsers, this.active.optional);
            this.active = this.active.parent;
            appendInternal(compositePrinterParser);
        } else {
            this.active = this.active.parent;
        }
        return this;
    }

    private int appendInternal(DateTimePrinterParser dateTimePrinterParser) {
        Objects.requireNonNull(dateTimePrinterParser, AMX.PARENT_PATH_KEY);
        if (this.active.padNextWidth > 0) {
            if (dateTimePrinterParser != null) {
                dateTimePrinterParser = new PadPrinterParserDecorator(dateTimePrinterParser, this.active.padNextWidth, this.active.padNextChar);
            }
            this.active.padNextWidth = 0;
            this.active.padNextChar = (char) 0;
        }
        this.active.printerParsers.add(dateTimePrinterParser);
        this.active.valueParserIndex = -1;
        return this.active.printerParsers.size() - 1;
    }

    public DateTimeFormatter toFormatter() {
        return toFormatter(Locale.getDefault(Locale.Category.FORMAT));
    }

    public DateTimeFormatter toFormatter(Locale locale) {
        return toFormatter(locale, ResolverStyle.SMART, null);
    }

    DateTimeFormatter toFormatter(ResolverStyle resolverStyle, Chronology chronology) {
        return toFormatter(Locale.getDefault(Locale.Category.FORMAT), resolverStyle, chronology);
    }

    private DateTimeFormatter toFormatter(Locale locale, ResolverStyle resolverStyle, Chronology chronology) {
        Objects.requireNonNull(locale, "locale");
        while (this.active.parent != null) {
            optionalEnd();
        }
        return new DateTimeFormatter(new CompositePrinterParser(this.printerParsers, false), locale, DecimalStyle.STANDARD, resolverStyle, null, chronology, null);
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$CompositePrinterParser.class */
    static final class CompositePrinterParser implements DateTimePrinterParser {
        private final DateTimePrinterParser[] printerParsers;
        private final boolean optional;

        CompositePrinterParser(List<DateTimePrinterParser> list, boolean z2) {
            this((DateTimePrinterParser[]) list.toArray(new DateTimePrinterParser[list.size()]), z2);
        }

        CompositePrinterParser(DateTimePrinterParser[] dateTimePrinterParserArr, boolean z2) {
            this.printerParsers = dateTimePrinterParserArr;
            this.optional = z2;
        }

        public CompositePrinterParser withOptional(boolean z2) {
            if (z2 == this.optional) {
                return this;
            }
            return new CompositePrinterParser(this.printerParsers, z2);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            int length = sb.length();
            if (this.optional) {
                dateTimePrintContext.startOptional();
            }
            try {
                for (DateTimePrinterParser dateTimePrinterParser : this.printerParsers) {
                    if (!dateTimePrinterParser.format(dateTimePrintContext, sb)) {
                        sb.setLength(length);
                        if (this.optional) {
                            dateTimePrintContext.endOptional();
                        }
                        return true;
                    }
                }
            } finally {
                if (this.optional) {
                    dateTimePrintContext.endOptional();
                }
            }
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            if (this.optional) {
                dateTimeParseContext.startOptional();
                int i3 = i2;
                for (DateTimePrinterParser dateTimePrinterParser : this.printerParsers) {
                    i3 = dateTimePrinterParser.parse(dateTimeParseContext, charSequence, i3);
                    if (i3 < 0) {
                        dateTimeParseContext.endOptional(false);
                        return i2;
                    }
                }
                dateTimeParseContext.endOptional(true);
                return i3;
            }
            for (DateTimePrinterParser dateTimePrinterParser2 : this.printerParsers) {
                i2 = dateTimePrinterParser2.parse(dateTimeParseContext, charSequence, i2);
                if (i2 < 0) {
                    break;
                }
            }
            return i2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.printerParsers != null) {
                sb.append(this.optional ? "[" : "(");
                for (DateTimePrinterParser dateTimePrinterParser : this.printerParsers) {
                    sb.append((Object) dateTimePrinterParser);
                }
                sb.append(this.optional ? "]" : ")");
            }
            return sb.toString();
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$PadPrinterParserDecorator.class */
    static final class PadPrinterParserDecorator implements DateTimePrinterParser {
        private final DateTimePrinterParser printerParser;
        private final int padWidth;
        private final char padChar;

        PadPrinterParserDecorator(DateTimePrinterParser dateTimePrinterParser, int i2, char c2) {
            this.printerParser = dateTimePrinterParser;
            this.padWidth = i2;
            this.padChar = c2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            int length = sb.length();
            if (!this.printerParser.format(dateTimePrintContext, sb)) {
                return false;
            }
            int length2 = sb.length() - length;
            if (length2 > this.padWidth) {
                throw new DateTimeException("Cannot print as output of " + length2 + " characters exceeds pad width of " + this.padWidth);
            }
            for (int i2 = 0; i2 < this.padWidth - length2; i2++) {
                sb.insert(length, this.padChar);
            }
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            boolean zIsStrict = dateTimeParseContext.isStrict();
            if (i2 > charSequence.length()) {
                throw new IndexOutOfBoundsException();
            }
            if (i2 == charSequence.length()) {
                return i2 ^ (-1);
            }
            int length = i2 + this.padWidth;
            if (length > charSequence.length()) {
                if (zIsStrict) {
                    return i2 ^ (-1);
                }
                length = charSequence.length();
            }
            int i3 = i2;
            while (i3 < length && dateTimeParseContext.charEquals(charSequence.charAt(i3), this.padChar)) {
                i3++;
            }
            int i4 = this.printerParser.parse(dateTimeParseContext, charSequence.subSequence(0, length), i3);
            if (i4 != length && zIsStrict) {
                return (i2 + i3) ^ (-1);
            }
            return i4;
        }

        public String toString() {
            return "Pad(" + ((Object) this.printerParser) + "," + this.padWidth + (this.padChar == ' ' ? ")" : ",'" + this.padChar + "')");
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$SettingsParser.class */
    enum SettingsParser implements DateTimePrinterParser {
        SENSITIVE,
        INSENSITIVE,
        STRICT,
        LENIENT;

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            switch (this) {
                case SENSITIVE:
                    dateTimeParseContext.setCaseSensitive(true);
                    break;
                case INSENSITIVE:
                    dateTimeParseContext.setCaseSensitive(false);
                    break;
                case STRICT:
                    dateTimeParseContext.setStrict(true);
                    break;
                case LENIENT:
                    dateTimeParseContext.setStrict(false);
                    break;
            }
            return i2;
        }

        @Override // java.lang.Enum
        public String toString() {
            switch (this) {
                case SENSITIVE:
                    return "ParseCaseSensitive(true)";
                case INSENSITIVE:
                    return "ParseCaseSensitive(false)";
                case STRICT:
                    return "ParseStrict(true)";
                case LENIENT:
                    return "ParseStrict(false)";
                default:
                    throw new IllegalStateException("Unreachable");
            }
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$DefaultValueParser.class */
    static class DefaultValueParser implements DateTimePrinterParser {
        private final TemporalField field;
        private final long value;

        DefaultValueParser(TemporalField temporalField, long j2) {
            this.field = temporalField;
            this.value = j2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            if (dateTimeParseContext.getParsed(this.field) == null) {
                dateTimeParseContext.setParsedField(this.field, this.value, i2, i2);
            }
            return i2;
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$CharLiteralPrinterParser.class */
    static final class CharLiteralPrinterParser implements DateTimePrinterParser {
        private final char literal;

        CharLiteralPrinterParser(char c2) {
            this.literal = c2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            sb.append(this.literal);
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            if (i2 == charSequence.length()) {
                return i2 ^ (-1);
            }
            char cCharAt = charSequence.charAt(i2);
            if (cCharAt != this.literal && (dateTimeParseContext.isCaseSensitive() || (Character.toUpperCase(cCharAt) != Character.toUpperCase(this.literal) && Character.toLowerCase(cCharAt) != Character.toLowerCase(this.literal)))) {
                return i2 ^ (-1);
            }
            return i2 + 1;
        }

        public String toString() {
            if (this.literal == '\'') {
                return "''";
            }
            return PdfOps.SINGLE_QUOTE_TOKEN + this.literal + PdfOps.SINGLE_QUOTE_TOKEN;
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$StringLiteralPrinterParser.class */
    static final class StringLiteralPrinterParser implements DateTimePrinterParser {
        private final String literal;

        StringLiteralPrinterParser(String str) {
            this.literal = str;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            sb.append(this.literal);
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            if (i2 > charSequence.length() || i2 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (!dateTimeParseContext.subSequenceEquals(charSequence, i2, this.literal, 0, this.literal.length())) {
                return i2 ^ (-1);
            }
            return i2 + this.literal.length();
        }

        public String toString() {
            return PdfOps.SINGLE_QUOTE_TOKEN + this.literal.replace(PdfOps.SINGLE_QUOTE_TOKEN, "''") + PdfOps.SINGLE_QUOTE_TOKEN;
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$NumberPrinterParser.class */
    static class NumberPrinterParser implements DateTimePrinterParser {
        static final long[] EXCEED_POINTS = {0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, NativeMediaPlayer.ONE_SECOND, 10000000000L};
        final TemporalField field;
        final int minWidth;
        final int maxWidth;
        private final SignStyle signStyle;
        final int subsequentWidth;

        NumberPrinterParser(TemporalField temporalField, int i2, int i3, SignStyle signStyle) {
            this.field = temporalField;
            this.minWidth = i2;
            this.maxWidth = i3;
            this.signStyle = signStyle;
            this.subsequentWidth = 0;
        }

        protected NumberPrinterParser(TemporalField temporalField, int i2, int i3, SignStyle signStyle, int i4) {
            this.field = temporalField;
            this.minWidth = i2;
            this.maxWidth = i3;
            this.signStyle = signStyle;
            this.subsequentWidth = i4;
        }

        NumberPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, -1);
        }

        NumberPrinterParser withSubsequentWidth(int i2) {
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, this.subsequentWidth + i2);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            long value2 = getValue(dateTimePrintContext, value.longValue());
            DecimalStyle decimalStyle = dateTimePrintContext.getDecimalStyle();
            String string = value2 == Long.MIN_VALUE ? "9223372036854775808" : Long.toString(Math.abs(value2));
            if (string.length() > this.maxWidth) {
                throw new DateTimeException("Field " + ((Object) this.field) + " cannot be printed as the value " + value2 + " exceeds the maximum print width of " + this.maxWidth);
            }
            String strConvertNumberToI18N = decimalStyle.convertNumberToI18N(string);
            if (value2 >= 0) {
                switch (this.signStyle) {
                    case EXCEEDS_PAD:
                        if (this.minWidth < 19 && value2 >= EXCEED_POINTS[this.minWidth]) {
                            sb.append(decimalStyle.getPositiveSign());
                            break;
                        }
                        break;
                    case ALWAYS:
                        sb.append(decimalStyle.getPositiveSign());
                        break;
                }
            } else {
                switch (this.signStyle) {
                    case EXCEEDS_PAD:
                    case ALWAYS:
                    case NORMAL:
                        sb.append(decimalStyle.getNegativeSign());
                        break;
                    case NOT_NEGATIVE:
                        throw new DateTimeException("Field " + ((Object) this.field) + " cannot be printed as the value " + value2 + " cannot be negative according to the SignStyle");
                }
            }
            for (int i2 = 0; i2 < this.minWidth - strConvertNumberToI18N.length(); i2++) {
                sb.append(decimalStyle.getZeroDigit());
            }
            sb.append(strConvertNumberToI18N);
            return true;
        }

        long getValue(DateTimePrintContext dateTimePrintContext, long j2) {
            return j2;
        }

        boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            return this.subsequentWidth == -1 || (this.subsequentWidth > 0 && this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            int length = charSequence.length();
            if (i2 == length) {
                return i2 ^ (-1);
            }
            char cCharAt = charSequence.charAt(i2);
            boolean z2 = false;
            boolean z3 = false;
            if (cCharAt == dateTimeParseContext.getDecimalStyle().getPositiveSign()) {
                if (!this.signStyle.parse(true, dateTimeParseContext.isStrict(), this.minWidth == this.maxWidth)) {
                    return i2 ^ (-1);
                }
                z3 = true;
                i2++;
            } else if (cCharAt == dateTimeParseContext.getDecimalStyle().getNegativeSign()) {
                if (!this.signStyle.parse(false, dateTimeParseContext.isStrict(), this.minWidth == this.maxWidth)) {
                    return i2 ^ (-1);
                }
                z2 = true;
                i2++;
            } else if (this.signStyle == SignStyle.ALWAYS && dateTimeParseContext.isStrict()) {
                return i2 ^ (-1);
            }
            int i3 = (dateTimeParseContext.isStrict() || isFixedWidth(dateTimeParseContext)) ? this.minWidth : 1;
            int i4 = i2 + i3;
            if (i4 > length) {
                return i2 ^ (-1);
            }
            int iMax = ((dateTimeParseContext.isStrict() || isFixedWidth(dateTimeParseContext)) ? this.maxWidth : 9) + Math.max(this.subsequentWidth, 0);
            long j2 = 0;
            BigInteger bigIntegerDivide = null;
            int i5 = i2;
            for (int i6 = 0; i6 < 2; i6++) {
                int iMin = Math.min(i5 + iMax, length);
                while (true) {
                    if (i5 >= iMin) {
                        break;
                    }
                    int i7 = i5;
                    i5++;
                    int iConvertToDigit = dateTimeParseContext.getDecimalStyle().convertToDigit(charSequence.charAt(i7));
                    if (iConvertToDigit < 0) {
                        i5--;
                        if (i5 < i4) {
                            return i2 ^ (-1);
                        }
                    } else if (i5 - i2 > 18) {
                        if (bigIntegerDivide == null) {
                            bigIntegerDivide = BigInteger.valueOf(j2);
                        }
                        bigIntegerDivide = bigIntegerDivide.multiply(BigInteger.TEN).add(BigInteger.valueOf(iConvertToDigit));
                    } else {
                        j2 = (j2 * 10) + iConvertToDigit;
                    }
                }
                if (this.subsequentWidth <= 0 || i6 != 0) {
                    break;
                }
                iMax = Math.max(i3, (i5 - i2) - this.subsequentWidth);
                i5 = i2;
                j2 = 0;
                bigIntegerDivide = null;
            }
            if (z2) {
                if (bigIntegerDivide != null) {
                    if (bigIntegerDivide.equals(BigInteger.ZERO) && dateTimeParseContext.isStrict()) {
                        return (i2 - 1) ^ (-1);
                    }
                    bigIntegerDivide = bigIntegerDivide.negate();
                } else {
                    if (j2 == 0 && dateTimeParseContext.isStrict()) {
                        return (i2 - 1) ^ (-1);
                    }
                    j2 = -j2;
                }
            } else if (this.signStyle == SignStyle.EXCEEDS_PAD && dateTimeParseContext.isStrict()) {
                int i8 = i5 - i2;
                if (z3) {
                    if (i8 <= this.minWidth) {
                        return (i2 - 1) ^ (-1);
                    }
                } else if (i8 > this.minWidth) {
                    return i2 ^ (-1);
                }
            }
            if (bigIntegerDivide != null) {
                if (bigIntegerDivide.bitLength() > 63) {
                    bigIntegerDivide = bigIntegerDivide.divide(BigInteger.TEN);
                    i5--;
                }
                return setValue(dateTimeParseContext, bigIntegerDivide.longValue(), i2, i5);
            }
            return setValue(dateTimeParseContext, j2, i2, i5);
        }

        int setValue(DateTimeParseContext dateTimeParseContext, long j2, int i2, int i3) {
            return dateTimeParseContext.setParsedField(this.field, j2, i2, i3);
        }

        public String toString() {
            if (this.minWidth == 1 && this.maxWidth == 19 && this.signStyle == SignStyle.NORMAL) {
                return "Value(" + ((Object) this.field) + ")";
            }
            if (this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE) {
                return "Value(" + ((Object) this.field) + "," + this.minWidth + ")";
            }
            return "Value(" + ((Object) this.field) + "," + this.minWidth + "," + this.maxWidth + "," + ((Object) this.signStyle) + ")";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$ReducedPrinterParser.class */
    static final class ReducedPrinterParser extends NumberPrinterParser {
        static final LocalDate BASE_DATE = LocalDate.of(2000, 1, 1);
        private final int baseValue;
        private final ChronoLocalDate baseDate;

        ReducedPrinterParser(TemporalField temporalField, int i2, int i3, int i4, ChronoLocalDate chronoLocalDate) {
            this(temporalField, i2, i3, i4, chronoLocalDate, 0);
            if (i2 < 1 || i2 > 10) {
                throw new IllegalArgumentException("The minWidth must be from 1 to 10 inclusive but was " + i2);
            }
            if (i3 < 1 || i3 > 10) {
                throw new IllegalArgumentException("The maxWidth must be from 1 to 10 inclusive but was " + i2);
            }
            if (i3 < i2) {
                throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + i3 + " < " + i2);
            }
            if (chronoLocalDate == null) {
                if (!temporalField.range().isValidValue(i4)) {
                    throw new IllegalArgumentException("The base value must be within the range of the field");
                }
                if (i4 + EXCEED_POINTS[i3] > 2147483647L) {
                    throw new DateTimeException("Unable to add printer-parser as the range exceeds the capacity of an int");
                }
            }
        }

        private ReducedPrinterParser(TemporalField temporalField, int i2, int i3, int i4, ChronoLocalDate chronoLocalDate, int i5) {
            super(temporalField, i2, i3, SignStyle.NOT_NEGATIVE, i5);
            this.baseValue = i4;
            this.baseDate = chronoLocalDate;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.NumberPrinterParser
        long getValue(DateTimePrintContext dateTimePrintContext, long j2) {
            long jAbs = Math.abs(j2);
            int i2 = this.baseValue;
            if (this.baseDate != null) {
                i2 = Chronology.from(dateTimePrintContext.getTemporal()).date(this.baseDate).get(this.field);
            }
            if (j2 >= i2 && j2 < i2 + EXCEED_POINTS[this.minWidth]) {
                return jAbs % EXCEED_POINTS[this.minWidth];
            }
            return jAbs % EXCEED_POINTS[this.maxWidth];
        }

        @Override // java.time.format.DateTimeFormatterBuilder.NumberPrinterParser
        int setValue(DateTimeParseContext dateTimeParseContext, long j2, int i2, int i3) {
            int i4 = this.baseValue;
            if (this.baseDate != null) {
                i4 = dateTimeParseContext.getEffectiveChronology().date(this.baseDate).get(this.field);
                dateTimeParseContext.addChronoChangedListener(chronology -> {
                    setValue(dateTimeParseContext, j2, i2, i3);
                });
            }
            if (i3 - i2 == this.minWidth && j2 >= 0) {
                long j3 = EXCEED_POINTS[this.minWidth];
                long j4 = i4 - (i4 % j3);
                if (i4 > 0) {
                    j2 = j4 + j2;
                } else {
                    j2 = j4 - j2;
                }
                if (j2 < i4) {
                    j2 += j3;
                }
            }
            return dateTimeParseContext.setParsedField(this.field, j2, i2, i3);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // java.time.format.DateTimeFormatterBuilder.NumberPrinterParser
        public ReducedPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, -1);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // java.time.format.DateTimeFormatterBuilder.NumberPrinterParser
        public ReducedPrinterParser withSubsequentWidth(int i2) {
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, this.subsequentWidth + i2);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.NumberPrinterParser
        boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            if (!dateTimeParseContext.isStrict()) {
                return false;
            }
            return super.isFixedWidth(dateTimeParseContext);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.NumberPrinterParser
        public String toString() {
            return "ReducedValue(" + ((Object) this.field) + "," + this.minWidth + "," + this.maxWidth + "," + (this.baseDate != null ? this.baseDate : Integer.valueOf(this.baseValue)) + ")";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$FractionPrinterParser.class */
    static final class FractionPrinterParser implements DateTimePrinterParser {
        private final TemporalField field;
        private final int minWidth;
        private final int maxWidth;
        private final boolean decimalPoint;

        FractionPrinterParser(TemporalField temporalField, int i2, int i3, boolean z2) {
            Objects.requireNonNull(temporalField, "field");
            if (!temporalField.range().isFixed()) {
                throw new IllegalArgumentException("Field must have a fixed set of values: " + ((Object) temporalField));
            }
            if (i2 < 0 || i2 > 9) {
                throw new IllegalArgumentException("Minimum width must be from 0 to 9 inclusive but was " + i2);
            }
            if (i3 < 1 || i3 > 9) {
                throw new IllegalArgumentException("Maximum width must be from 1 to 9 inclusive but was " + i3);
            }
            if (i3 < i2) {
                throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + i3 + " < " + i2);
            }
            this.field = temporalField;
            this.minWidth = i2;
            this.maxWidth = i3;
            this.decimalPoint = z2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            DecimalStyle decimalStyle = dateTimePrintContext.getDecimalStyle();
            BigDecimal bigDecimalConvertToFraction = convertToFraction(value.longValue());
            if (bigDecimalConvertToFraction.scale() == 0) {
                if (this.minWidth > 0) {
                    if (this.decimalPoint) {
                        sb.append(decimalStyle.getDecimalSeparator());
                    }
                    for (int i2 = 0; i2 < this.minWidth; i2++) {
                        sb.append(decimalStyle.getZeroDigit());
                    }
                    return true;
                }
                return true;
            }
            String strConvertNumberToI18N = decimalStyle.convertNumberToI18N(bigDecimalConvertToFraction.setScale(Math.min(Math.max(bigDecimalConvertToFraction.scale(), this.minWidth), this.maxWidth), RoundingMode.FLOOR).toPlainString().substring(2));
            if (this.decimalPoint) {
                sb.append(decimalStyle.getDecimalSeparator());
            }
            sb.append(strConvertNumberToI18N);
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            int i3 = dateTimeParseContext.isStrict() ? this.minWidth : 0;
            int i4 = dateTimeParseContext.isStrict() ? this.maxWidth : 9;
            int length = charSequence.length();
            if (i2 == length) {
                return i3 > 0 ? i2 ^ (-1) : i2;
            }
            if (this.decimalPoint) {
                if (charSequence.charAt(i2) != dateTimeParseContext.getDecimalStyle().getDecimalSeparator()) {
                    return i3 > 0 ? i2 ^ (-1) : i2;
                }
                i2++;
            }
            int i5 = i2 + i3;
            if (i5 > length) {
                return i2 ^ (-1);
            }
            int iMin = Math.min(i2 + i4, length);
            int i6 = 0;
            int i7 = i2;
            while (true) {
                if (i7 >= iMin) {
                    break;
                }
                int i8 = i7;
                i7++;
                int iConvertToDigit = dateTimeParseContext.getDecimalStyle().convertToDigit(charSequence.charAt(i8));
                if (iConvertToDigit < 0) {
                    if (i7 < i5) {
                        return i2 ^ (-1);
                    }
                    i7--;
                } else {
                    i6 = (i6 * 10) + iConvertToDigit;
                }
            }
            return dateTimeParseContext.setParsedField(this.field, convertFromFraction(new BigDecimal(i6).movePointLeft(i7 - i2)), i2, i7);
        }

        private BigDecimal convertToFraction(long j2) {
            ValueRange valueRangeRange = this.field.range();
            valueRangeRange.checkValidValue(j2, this.field);
            BigDecimal bigDecimalValueOf = BigDecimal.valueOf(valueRangeRange.getMinimum());
            BigDecimal bigDecimalDivide = BigDecimal.valueOf(j2).subtract(bigDecimalValueOf).divide(BigDecimal.valueOf(valueRangeRange.getMaximum()).subtract(bigDecimalValueOf).add(BigDecimal.ONE), 9, RoundingMode.FLOOR);
            return bigDecimalDivide.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : bigDecimalDivide.stripTrailingZeros();
        }

        private long convertFromFraction(BigDecimal bigDecimal) {
            ValueRange valueRangeRange = this.field.range();
            BigDecimal bigDecimalValueOf = BigDecimal.valueOf(valueRangeRange.getMinimum());
            return bigDecimal.multiply(BigDecimal.valueOf(valueRangeRange.getMaximum()).subtract(bigDecimalValueOf).add(BigDecimal.ONE)).setScale(0, RoundingMode.FLOOR).add(bigDecimalValueOf).longValueExact();
        }

        public String toString() {
            return "Fraction(" + ((Object) this.field) + "," + this.minWidth + "," + this.maxWidth + (this.decimalPoint ? ",DecimalPoint" : "") + ")";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$TextPrinterParser.class */
    static final class TextPrinterParser implements DateTimePrinterParser {
        private final TemporalField field;
        private final TextStyle textStyle;
        private final DateTimeTextProvider provider;
        private volatile NumberPrinterParser numberPrinterParser;

        TextPrinterParser(TemporalField temporalField, TextStyle textStyle, DateTimeTextProvider dateTimeTextProvider) {
            this.field = temporalField;
            this.textStyle = textStyle;
            this.provider = dateTimeTextProvider;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            String text;
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            Chronology chronology = (Chronology) dateTimePrintContext.getTemporal().query(TemporalQueries.chronology());
            if (chronology == null || chronology == IsoChronology.INSTANCE) {
                text = this.provider.getText(this.field, value.longValue(), this.textStyle, dateTimePrintContext.getLocale());
            } else {
                text = this.provider.getText(chronology, this.field, value.longValue(), this.textStyle, dateTimePrintContext.getLocale());
            }
            if (text == null) {
                return numberPrinterParser().format(dateTimePrintContext, sb);
            }
            sb.append(text);
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            Iterator<Map.Entry<String, Long>> textIterator;
            int length = charSequence.length();
            if (i2 < 0 || i2 > length) {
                throw new IndexOutOfBoundsException();
            }
            TextStyle textStyle = dateTimeParseContext.isStrict() ? this.textStyle : null;
            Chronology effectiveChronology = dateTimeParseContext.getEffectiveChronology();
            if (effectiveChronology == null || effectiveChronology == IsoChronology.INSTANCE) {
                textIterator = this.provider.getTextIterator(this.field, textStyle, dateTimeParseContext.getLocale());
            } else {
                textIterator = this.provider.getTextIterator(effectiveChronology, this.field, textStyle, dateTimeParseContext.getLocale());
            }
            if (textIterator != null) {
                while (textIterator.hasNext()) {
                    Map.Entry<String, Long> next = textIterator.next();
                    String key = next.getKey();
                    if (dateTimeParseContext.subSequenceEquals(key, 0, charSequence, i2, key.length())) {
                        return dateTimeParseContext.setParsedField(this.field, next.getValue().longValue(), i2, i2 + key.length());
                    }
                }
                if (dateTimeParseContext.isStrict()) {
                    return i2 ^ (-1);
                }
            }
            return numberPrinterParser().parse(dateTimeParseContext, charSequence, i2);
        }

        private NumberPrinterParser numberPrinterParser() {
            if (this.numberPrinterParser == null) {
                this.numberPrinterParser = new NumberPrinterParser(this.field, 1, 19, SignStyle.NORMAL);
            }
            return this.numberPrinterParser;
        }

        public String toString() {
            if (this.textStyle == TextStyle.FULL) {
                return "Text(" + ((Object) this.field) + ")";
            }
            return "Text(" + ((Object) this.field) + "," + ((Object) this.textStyle) + ")";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$InstantPrinterParser.class */
    static final class InstantPrinterParser implements DateTimePrinterParser {
        private static final long SECONDS_PER_10000_YEARS = 315569520000L;
        private static final long SECONDS_0000_TO_1970 = 62167219200L;
        private final int fractionalDigits;

        InstantPrinterParser(int i2) {
            this.fractionalDigits = i2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue(ChronoField.INSTANT_SECONDS);
            Long lValueOf = null;
            if (dateTimePrintContext.getTemporal().isSupported(ChronoField.NANO_OF_SECOND)) {
                lValueOf = Long.valueOf(dateTimePrintContext.getTemporal().getLong(ChronoField.NANO_OF_SECOND));
            }
            if (value == null) {
                return false;
            }
            long jLongValue = value.longValue();
            int iCheckValidIntValue = ChronoField.NANO_OF_SECOND.checkValidIntValue(lValueOf != null ? lValueOf.longValue() : 0L);
            if (jLongValue >= -62167219200L) {
                long j2 = (jLongValue - SECONDS_PER_10000_YEARS) + SECONDS_0000_TO_1970;
                long jFloorDiv = Math.floorDiv(j2, SECONDS_PER_10000_YEARS) + 1;
                LocalDateTime localDateTimeOfEpochSecond = LocalDateTime.ofEpochSecond(Math.floorMod(j2, SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC);
                if (jFloorDiv > 0) {
                    sb.append('+').append(jFloorDiv);
                }
                sb.append((Object) localDateTimeOfEpochSecond);
                if (localDateTimeOfEpochSecond.getSecond() == 0) {
                    sb.append(":00");
                }
            } else {
                long j3 = jLongValue + SECONDS_0000_TO_1970;
                long j4 = j3 / SECONDS_PER_10000_YEARS;
                long j5 = j3 % SECONDS_PER_10000_YEARS;
                LocalDateTime localDateTimeOfEpochSecond2 = LocalDateTime.ofEpochSecond(j5 - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC);
                int length = sb.length();
                sb.append((Object) localDateTimeOfEpochSecond2);
                if (localDateTimeOfEpochSecond2.getSecond() == 0) {
                    sb.append(":00");
                }
                if (j4 < 0) {
                    if (localDateTimeOfEpochSecond2.getYear() == -10000) {
                        sb.replace(length, length + 2, Long.toString(j4 - 1));
                    } else if (j5 == 0) {
                        sb.insert(length, j4);
                    } else {
                        sb.insert(length + 1, Math.abs(j4));
                    }
                }
            }
            if ((this.fractionalDigits < 0 && iCheckValidIntValue > 0) || this.fractionalDigits > 0) {
                sb.append('.');
                int i2 = 100000000;
                int i3 = 0;
                while (true) {
                    if ((this.fractionalDigits != -1 || iCheckValidIntValue <= 0) && ((this.fractionalDigits != -2 || (iCheckValidIntValue <= 0 && i3 % 3 == 0)) && i3 >= this.fractionalDigits)) {
                        break;
                    }
                    int i4 = iCheckValidIntValue / i2;
                    sb.append((char) (i4 + 48));
                    iCheckValidIntValue -= i4 * i2;
                    i2 /= 10;
                    i3++;
                }
            }
            sb.append('Z');
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            CompositePrinterParser printerParser = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).appendFraction(ChronoField.NANO_OF_SECOND, this.fractionalDigits < 0 ? 0 : this.fractionalDigits, this.fractionalDigits < 0 ? 9 : this.fractionalDigits, true).appendLiteral('Z').toFormatter().toPrinterParser(false);
            DateTimeParseContext dateTimeParseContextCopy = dateTimeParseContext.copy();
            int i3 = printerParser.parse(dateTimeParseContextCopy, charSequence, i2);
            if (i3 < 0) {
                return i3;
            }
            long jLongValue = dateTimeParseContextCopy.getParsed(ChronoField.YEAR).longValue();
            int iIntValue = dateTimeParseContextCopy.getParsed(ChronoField.MONTH_OF_YEAR).intValue();
            int iIntValue2 = dateTimeParseContextCopy.getParsed(ChronoField.DAY_OF_MONTH).intValue();
            int iIntValue3 = dateTimeParseContextCopy.getParsed(ChronoField.HOUR_OF_DAY).intValue();
            int iIntValue4 = dateTimeParseContextCopy.getParsed(ChronoField.MINUTE_OF_HOUR).intValue();
            Long parsed = dateTimeParseContextCopy.getParsed(ChronoField.SECOND_OF_MINUTE);
            Long parsed2 = dateTimeParseContextCopy.getParsed(ChronoField.NANO_OF_SECOND);
            int iIntValue5 = parsed != null ? parsed.intValue() : 0;
            int iIntValue6 = parsed2 != null ? parsed2.intValue() : 0;
            int i4 = 0;
            if (iIntValue3 == 24 && iIntValue4 == 0 && iIntValue5 == 0 && iIntValue6 == 0) {
                iIntValue3 = 0;
                i4 = 1;
            } else if (iIntValue3 == 23 && iIntValue4 == 59 && iIntValue5 == 60) {
                dateTimeParseContext.setParsedLeapSecond();
                iIntValue5 = 59;
            }
            try {
                return dateTimeParseContext.setParsedField(ChronoField.NANO_OF_SECOND, iIntValue6, i2, dateTimeParseContext.setParsedField(ChronoField.INSTANT_SECONDS, LocalDateTime.of(((int) jLongValue) % 10000, iIntValue, iIntValue2, iIntValue3, iIntValue4, iIntValue5, 0).plusDays(i4).toEpochSecond(ZoneOffset.UTC) + Math.multiplyExact(jLongValue / 10000, SECONDS_PER_10000_YEARS), i2, i3));
            } catch (RuntimeException e2) {
                return i2 ^ (-1);
            }
        }

        public String toString() {
            return "Instant()";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$OffsetIdPrinterParser.class */
    static final class OffsetIdPrinterParser implements DateTimePrinterParser {
        static final String[] PATTERNS = {"+HH", "+HHmm", "+HH:mm", "+HHMM", "+HH:MM", "+HHMMss", "+HH:MM:ss", "+HHMMSS", "+HH:MM:SS"};
        static final OffsetIdPrinterParser INSTANCE_ID_Z = new OffsetIdPrinterParser("+HH:MM:ss", com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG);
        static final OffsetIdPrinterParser INSTANCE_ID_ZERO = new OffsetIdPrinterParser("+HH:MM:ss", "0");
        private final String noOffsetText;
        private final int type;

        OffsetIdPrinterParser(String str, String str2) {
            Objects.requireNonNull(str, "pattern");
            Objects.requireNonNull(str2, "noOffsetText");
            this.type = checkPattern(str);
            this.noOffsetText = str2;
        }

        private int checkPattern(String str) {
            for (int i2 = 0; i2 < PATTERNS.length; i2++) {
                if (PATTERNS[i2].equals(str)) {
                    return i2;
                }
            }
            throw new IllegalArgumentException("Invalid zone offset pattern: " + str);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue(ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return false;
            }
            int intExact = Math.toIntExact(value.longValue());
            if (intExact == 0) {
                sb.append(this.noOffsetText);
                return true;
            }
            int iAbs = Math.abs((intExact / 3600) % 100);
            int iAbs2 = Math.abs((intExact / 60) % 60);
            int iAbs3 = Math.abs(intExact % 60);
            int length = sb.length();
            int i2 = iAbs;
            sb.append(intExact < 0 ? LanguageTag.SEP : Marker.ANY_NON_NULL_MARKER).append((char) ((iAbs / 10) + 48)).append((char) ((iAbs % 10) + 48));
            if (this.type >= 3 || (this.type >= 1 && iAbs2 > 0)) {
                sb.append(this.type % 2 == 0 ? CallSiteDescriptor.TOKEN_DELIMITER : "").append((char) ((iAbs2 / 10) + 48)).append((char) ((iAbs2 % 10) + 48));
                i2 += iAbs2;
                if (this.type >= 7 || (this.type >= 5 && iAbs3 > 0)) {
                    sb.append(this.type % 2 == 0 ? CallSiteDescriptor.TOKEN_DELIMITER : "").append((char) ((iAbs3 / 10) + 48)).append((char) ((iAbs3 % 10) + 48));
                    i2 += iAbs3;
                }
            }
            if (i2 == 0) {
                sb.setLength(length);
                sb.append(this.noOffsetText);
                return true;
            }
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:34:0x00b0  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00b8  */
        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int parse(java.time.format.DateTimeParseContext r10, java.lang.CharSequence r11, int r12) {
            /*
                Method dump skipped, instructions count: 253
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.OffsetIdPrinterParser.parse(java.time.format.DateTimeParseContext, java.lang.CharSequence, int):int");
        }

        private boolean parseNumber(int[] iArr, int i2, CharSequence charSequence, boolean z2) {
            if ((this.type + 3) / 2 < i2) {
                return false;
            }
            int i3 = iArr[0];
            if (this.type % 2 == 0 && i2 > 1) {
                if (i3 + 1 > charSequence.length() || charSequence.charAt(i3) != ':') {
                    return z2;
                }
                i3++;
            }
            if (i3 + 2 > charSequence.length()) {
                return z2;
            }
            int i4 = i3;
            int i5 = i3 + 1;
            char cCharAt = charSequence.charAt(i4);
            int i6 = i5 + 1;
            char cCharAt2 = charSequence.charAt(i5);
            if (cCharAt < '0' || cCharAt > '9' || cCharAt2 < '0' || cCharAt2 > '9') {
                return z2;
            }
            int i7 = ((cCharAt - '0') * 10) + (cCharAt2 - '0');
            if (i7 < 0 || i7 > 59) {
                return z2;
            }
            iArr[i2] = i7;
            iArr[0] = i6;
            return false;
        }

        public String toString() {
            return "Offset(" + PATTERNS[this.type] + ",'" + this.noOffsetText.replace(PdfOps.SINGLE_QUOTE_TOKEN, "''") + "')";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$LocalizedOffsetIdPrinterParser.class */
    static final class LocalizedOffsetIdPrinterParser implements DateTimePrinterParser {
        private final TextStyle style;

        LocalizedOffsetIdPrinterParser(TextStyle textStyle) {
            this.style = textStyle;
        }

        private static StringBuilder appendHMS(StringBuilder sb, int i2) {
            return sb.append((char) ((i2 / 10) + 48)).append((char) ((i2 % 10) + 48));
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue(ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return false;
            }
            if ("GMT" != 0) {
                sb.append("GMT");
            }
            int intExact = Math.toIntExact(value.longValue());
            if (intExact != 0) {
                int iAbs = Math.abs((intExact / 3600) % 100);
                int iAbs2 = Math.abs((intExact / 60) % 60);
                int iAbs3 = Math.abs(intExact % 60);
                sb.append(intExact < 0 ? LanguageTag.SEP : Marker.ANY_NON_NULL_MARKER);
                if (this.style == TextStyle.FULL) {
                    appendHMS(sb, iAbs);
                    sb.append(':');
                    appendHMS(sb, iAbs2);
                    if (iAbs3 != 0) {
                        sb.append(':');
                        appendHMS(sb, iAbs3);
                        return true;
                    }
                    return true;
                }
                if (iAbs >= 10) {
                    sb.append((char) ((iAbs / 10) + 48));
                }
                sb.append((char) ((iAbs % 10) + 48));
                if (iAbs2 != 0 || iAbs3 != 0) {
                    sb.append(':');
                    appendHMS(sb, iAbs2);
                    if (iAbs3 != 0) {
                        sb.append(':');
                        appendHMS(sb, iAbs3);
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }

        int getDigit(CharSequence charSequence, int i2) {
            char cCharAt = charSequence.charAt(i2);
            if (cCharAt < '0' || cCharAt > '9') {
                return -1;
            }
            return cCharAt - '0';
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            int i3;
            int i4;
            int digit;
            int length = i2;
            int length2 = length + charSequence.length();
            if ("GMT" != 0) {
                if (!dateTimeParseContext.subSequenceEquals(charSequence, length, "GMT", 0, "GMT".length())) {
                    return i2 ^ (-1);
                }
                length += "GMT".length();
            }
            if (length == length2) {
                return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, i2, length);
            }
            char cCharAt = charSequence.charAt(length);
            if (cCharAt == '+') {
                i3 = 1;
            } else if (cCharAt == '-') {
                i3 = -1;
            } else {
                return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0L, i2, length);
            }
            int i5 = length + 1;
            int i6 = 0;
            int i7 = 0;
            if (this.style == TextStyle.FULL) {
                int i8 = i5 + 1;
                int digit2 = getDigit(charSequence, i5);
                int i9 = i8 + 1;
                int digit3 = getDigit(charSequence, i8);
                if (digit2 >= 0 && digit3 >= 0) {
                    int i10 = i9 + 1;
                    if (charSequence.charAt(i9) == ':') {
                        digit = (digit2 * 10) + digit3;
                        int i11 = i10 + 1;
                        int digit4 = getDigit(charSequence, i10);
                        i4 = i11 + 1;
                        int digit5 = getDigit(charSequence, i11);
                        if (digit4 < 0 || digit5 < 0) {
                            return i2 ^ (-1);
                        }
                        i6 = (digit4 * 10) + digit5;
                        if (i4 + 2 < length2 && charSequence.charAt(i4) == ':') {
                            int digit6 = getDigit(charSequence, i4 + 1);
                            int digit7 = getDigit(charSequence, i4 + 2);
                            if (digit6 >= 0 && digit7 >= 0) {
                                i7 = (digit6 * 10) + digit7;
                                i4 += 3;
                            }
                        }
                    }
                }
                return i2 ^ (-1);
            }
            i4 = i5 + 1;
            digit = getDigit(charSequence, i5);
            if (digit < 0) {
                return i2 ^ (-1);
            }
            if (i4 < length2) {
                int digit8 = getDigit(charSequence, i4);
                if (digit8 >= 0) {
                    digit = (digit * 10) + digit8;
                    i4++;
                }
                if (i4 + 2 < length2 && charSequence.charAt(i4) == ':' && i4 + 2 < length2 && charSequence.charAt(i4) == ':') {
                    int digit9 = getDigit(charSequence, i4 + 1);
                    int digit10 = getDigit(charSequence, i4 + 2);
                    if (digit9 >= 0 && digit10 >= 0) {
                        i6 = (digit9 * 10) + digit10;
                        i4 += 3;
                        if (i4 + 2 < length2 && charSequence.charAt(i4) == ':') {
                            int digit11 = getDigit(charSequence, i4 + 1);
                            int digit12 = getDigit(charSequence, i4 + 2);
                            if (digit11 >= 0 && digit12 >= 0) {
                                i7 = (digit11 * 10) + digit12;
                                i4 += 3;
                            }
                        }
                    }
                }
            }
            return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, i3 * ((digit * 3600) + (i6 * 60) + i7), i2, i4);
        }

        public String toString() {
            return "LocalizedOffset(" + ((Object) this.style) + ")";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$ZoneTextPrinterParser.class */
    static final class ZoneTextPrinterParser extends ZoneIdPrinterParser {
        private final TextStyle textStyle;
        private Set<String> preferredZones;
        private static final int STD = 0;
        private static final int DST = 1;
        private static final int GENERIC = 2;
        private static final Map<String, SoftReference<Map<Locale, String[]>>> cache = new ConcurrentHashMap();
        private final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> cachedTree;
        private final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> cachedTreeCI;

        ZoneTextPrinterParser(TextStyle textStyle, Set<ZoneId> set) {
            super(TemporalQueries.zone(), "ZoneText(" + ((Object) textStyle) + ")");
            this.cachedTree = new HashMap();
            this.cachedTreeCI = new HashMap();
            this.textStyle = (TextStyle) Objects.requireNonNull(textStyle, "textStyle");
            if (set != null && set.size() != 0) {
                this.preferredZones = new HashSet();
                Iterator<ZoneId> it = set.iterator();
                while (it.hasNext()) {
                    this.preferredZones.add(it.next().getId());
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0041 A[PHI: r12
  0x0041: PHI (r12v1 java.util.Map<java.util.Locale, java.lang.String[]>) = 
  (r12v0 java.util.Map<java.util.Locale, java.lang.String[]>)
  (r12v4 java.util.Map<java.util.Locale, java.lang.String[]>)
  (r12v4 java.util.Map<java.util.Locale, java.lang.String[]>)
 binds: [B:7:0x001f, B:9:0x002d, B:11:0x003e] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.lang.String getDisplayName(java.lang.String r7, int r8, java.util.Locale r9) {
            /*
                Method dump skipped, instructions count: 255
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.ZoneTextPrinterParser.getDisplayName(java.lang.String, int, java.util.Locale):java.lang.String");
        }

        @Override // java.time.format.DateTimeFormatterBuilder.ZoneIdPrinterParser, java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            int i2;
            ZoneId zoneId = (ZoneId) dateTimePrintContext.getValue(TemporalQueries.zoneId());
            if (zoneId == null) {
                return false;
            }
            String id = zoneId.getId();
            if (!(zoneId instanceof ZoneOffset)) {
                TemporalAccessor temporal = dateTimePrintContext.getTemporal();
                if (temporal.isSupported(ChronoField.INSTANT_SECONDS)) {
                    i2 = zoneId.getRules().isDaylightSavings(Instant.from(temporal)) ? 1 : 0;
                } else {
                    i2 = 2;
                }
                String displayName = getDisplayName(id, i2, dateTimePrintContext.getLocale());
                if (displayName != null) {
                    id = displayName;
                }
            }
            sb.append(id);
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x007e  */
        @Override // java.time.format.DateTimeFormatterBuilder.ZoneIdPrinterParser
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected java.time.format.DateTimeFormatterBuilder.PrefixTree getTree(java.time.format.DateTimeParseContext r10) {
            /*
                Method dump skipped, instructions count: 404
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.ZoneTextPrinterParser.getTree(java.time.format.DateTimeParseContext):java.time.format.DateTimeFormatterBuilder$PrefixTree");
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$ZoneIdPrinterParser.class */
    static class ZoneIdPrinterParser implements DateTimePrinterParser {
        private final TemporalQuery<ZoneId> query;
        private final String description;
        private static volatile Map.Entry<Integer, PrefixTree> cachedPrefixTree;
        private static volatile Map.Entry<Integer, PrefixTree> cachedPrefixTreeCI;

        ZoneIdPrinterParser(TemporalQuery<ZoneId> temporalQuery, String str) {
            this.query = temporalQuery;
            this.description = str;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            ZoneId zoneId = (ZoneId) dateTimePrintContext.getValue(this.query);
            if (zoneId == null) {
                return false;
            }
            sb.append(zoneId.getId());
            return true;
        }

        protected PrefixTree getTree(DateTimeParseContext dateTimeParseContext) {
            Set<String> availableZoneIds = ZoneRulesProvider.getAvailableZoneIds();
            int size = availableZoneIds.size();
            Map.Entry<Integer, PrefixTree> simpleImmutableEntry = dateTimeParseContext.isCaseSensitive() ? cachedPrefixTree : cachedPrefixTreeCI;
            if (simpleImmutableEntry == null || simpleImmutableEntry.getKey().intValue() != size) {
                synchronized (this) {
                    simpleImmutableEntry = dateTimeParseContext.isCaseSensitive() ? cachedPrefixTree : cachedPrefixTreeCI;
                    if (simpleImmutableEntry == null || simpleImmutableEntry.getKey().intValue() != size) {
                        simpleImmutableEntry = new AbstractMap.SimpleImmutableEntry(Integer.valueOf(size), PrefixTree.newTree(availableZoneIds, dateTimeParseContext));
                        if (dateTimeParseContext.isCaseSensitive()) {
                            cachedPrefixTree = simpleImmutableEntry;
                        } else {
                            cachedPrefixTreeCI = simpleImmutableEntry;
                        }
                    }
                }
            }
            return simpleImmutableEntry.getValue();
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            int length = charSequence.length();
            if (i2 > length) {
                throw new IndexOutOfBoundsException();
            }
            if (i2 == length) {
                return i2 ^ (-1);
            }
            char cCharAt = charSequence.charAt(i2);
            if (cCharAt == '+' || cCharAt == '-') {
                return parseOffsetBased(dateTimeParseContext, charSequence, i2, i2, OffsetIdPrinterParser.INSTANCE_ID_Z);
            }
            if (length >= i2 + 2) {
                char cCharAt2 = charSequence.charAt(i2 + 1);
                if (dateTimeParseContext.charEquals(cCharAt, 'U') && dateTimeParseContext.charEquals(cCharAt2, 'T')) {
                    if (length >= i2 + 3 && dateTimeParseContext.charEquals(charSequence.charAt(i2 + 2), 'C')) {
                        return parseOffsetBased(dateTimeParseContext, charSequence, i2, i2 + 3, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                    }
                    return parseOffsetBased(dateTimeParseContext, charSequence, i2, i2 + 2, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                }
                if (dateTimeParseContext.charEquals(cCharAt, 'G') && length >= i2 + 3 && dateTimeParseContext.charEquals(cCharAt2, 'M') && dateTimeParseContext.charEquals(charSequence.charAt(i2 + 2), 'T')) {
                    return parseOffsetBased(dateTimeParseContext, charSequence, i2, i2 + 3, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                }
            }
            PrefixTree tree = getTree(dateTimeParseContext);
            ParsePosition parsePosition = new ParsePosition(i2);
            String strMatch = tree.match(charSequence, parsePosition);
            if (strMatch == null) {
                if (dateTimeParseContext.charEquals(cCharAt, 'Z')) {
                    dateTimeParseContext.setParsed(ZoneOffset.UTC);
                    return i2 + 1;
                }
                return i2 ^ (-1);
            }
            dateTimeParseContext.setParsed(ZoneId.of(strMatch));
            return parsePosition.getIndex();
        }

        private int parseOffsetBased(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2, int i3, OffsetIdPrinterParser offsetIdPrinterParser) {
            String upperCase = charSequence.toString().substring(i2, i3).toUpperCase();
            if (i3 >= charSequence.length()) {
                dateTimeParseContext.setParsed(ZoneId.of(upperCase));
                return i3;
            }
            if (charSequence.charAt(i3) == '0' || dateTimeParseContext.charEquals(charSequence.charAt(i3), 'Z')) {
                dateTimeParseContext.setParsed(ZoneId.of(upperCase));
                return i3;
            }
            DateTimeParseContext dateTimeParseContextCopy = dateTimeParseContext.copy();
            int i4 = offsetIdPrinterParser.parse(dateTimeParseContextCopy, charSequence, i3);
            try {
                if (i4 < 0) {
                    if (offsetIdPrinterParser == OffsetIdPrinterParser.INSTANCE_ID_Z) {
                        return i2 ^ (-1);
                    }
                    dateTimeParseContext.setParsed(ZoneId.of(upperCase));
                    return i3;
                }
                dateTimeParseContext.setParsed(ZoneId.ofOffset(upperCase, ZoneOffset.ofTotalSeconds((int) dateTimeParseContextCopy.getParsed(ChronoField.OFFSET_SECONDS).longValue())));
                return i4;
            } catch (DateTimeException e2) {
                return i2 ^ (-1);
            }
        }

        public String toString() {
            return this.description;
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$PrefixTree.class */
    static class PrefixTree {
        protected String key;
        protected String value;
        protected char c0;
        protected PrefixTree child;
        protected PrefixTree sibling;

        private PrefixTree(String str, String str2, PrefixTree prefixTree) {
            this.key = str;
            this.value = str2;
            this.child = prefixTree;
            if (str.length() == 0) {
                this.c0 = (char) 65535;
            } else {
                this.c0 = this.key.charAt(0);
            }
        }

        public static PrefixTree newTree(DateTimeParseContext dateTimeParseContext) {
            if (dateTimeParseContext.isCaseSensitive()) {
                return new PrefixTree("", null, null);
            }
            return new CI("", null, null);
        }

        public static PrefixTree newTree(Set<String> set, DateTimeParseContext dateTimeParseContext) {
            PrefixTree prefixTreeNewTree = newTree(dateTimeParseContext);
            for (String str : set) {
                prefixTreeNewTree.add0(str, str);
            }
            return prefixTreeNewTree;
        }

        public PrefixTree copyTree() {
            PrefixTree prefixTree = new PrefixTree(this.key, this.value, null);
            if (this.child != null) {
                prefixTree.child = this.child.copyTree();
            }
            if (this.sibling != null) {
                prefixTree.sibling = this.sibling.copyTree();
            }
            return prefixTree;
        }

        public boolean add(String str, String str2) {
            return add0(str, str2);
        }

        private boolean add0(String str, String str2) {
            String key = toKey(str);
            int iPrefixLength = prefixLength(key);
            if (iPrefixLength == this.key.length()) {
                if (iPrefixLength < key.length()) {
                    String strSubstring = key.substring(iPrefixLength);
                    PrefixTree prefixTree = this.child;
                    while (true) {
                        PrefixTree prefixTree2 = prefixTree;
                        if (prefixTree2 != null) {
                            if (isEqual(prefixTree2.c0, strSubstring.charAt(0))) {
                                return prefixTree2.add0(strSubstring, str2);
                            }
                            prefixTree = prefixTree2.sibling;
                        } else {
                            PrefixTree prefixTreeNewNode = newNode(strSubstring, str2, null);
                            prefixTreeNewNode.sibling = this.child;
                            this.child = prefixTreeNewNode;
                            return true;
                        }
                    }
                } else {
                    this.value = str2;
                    return true;
                }
            } else {
                PrefixTree prefixTreeNewNode2 = newNode(this.key.substring(iPrefixLength), this.value, this.child);
                this.key = key.substring(0, iPrefixLength);
                this.child = prefixTreeNewNode2;
                if (iPrefixLength < key.length()) {
                    this.child.sibling = newNode(key.substring(iPrefixLength), str2, null);
                    this.value = null;
                    return true;
                }
                this.value = str2;
                return true;
            }
        }

        public String match(CharSequence charSequence, int i2, int i3) {
            int length;
            if (!prefixOf(charSequence, i2, i3)) {
                return null;
            }
            if (this.child != null && (length = i2 + this.key.length()) != i3) {
                PrefixTree prefixTree = this.child;
                while (!isEqual(prefixTree.c0, charSequence.charAt(length))) {
                    prefixTree = prefixTree.sibling;
                    if (prefixTree == null) {
                    }
                }
                String strMatch = prefixTree.match(charSequence, length, i3);
                if (strMatch != null) {
                    return strMatch;
                }
                return this.value;
            }
            return this.value;
        }

        public String match(CharSequence charSequence, ParsePosition parsePosition) {
            int index = parsePosition.getIndex();
            int length = charSequence.length();
            if (!prefixOf(charSequence, index, length)) {
                return null;
            }
            int length2 = index + this.key.length();
            if (this.child != null && length2 != length) {
                PrefixTree prefixTree = this.child;
                while (true) {
                    if (isEqual(prefixTree.c0, charSequence.charAt(length2))) {
                        parsePosition.setIndex(length2);
                        String strMatch = prefixTree.match(charSequence, parsePosition);
                        if (strMatch != null) {
                            return strMatch;
                        }
                    } else {
                        prefixTree = prefixTree.sibling;
                        if (prefixTree == null) {
                            break;
                        }
                    }
                }
            }
            parsePosition.setIndex(length2);
            return this.value;
        }

        protected String toKey(String str) {
            return str;
        }

        protected PrefixTree newNode(String str, String str2, PrefixTree prefixTree) {
            return new PrefixTree(str, str2, prefixTree);
        }

        protected boolean isEqual(char c2, char c3) {
            return c2 == c3;
        }

        protected boolean prefixOf(CharSequence charSequence, int i2, int i3) {
            int i4;
            int i5;
            if (charSequence instanceof String) {
                return ((String) charSequence).startsWith(this.key, i2);
            }
            int length = this.key.length();
            if (length > i3 - i2) {
                return false;
            }
            int i6 = 0;
            do {
                int i7 = length;
                length--;
                if (i7 <= 0) {
                    return true;
                }
                i4 = i6;
                i6++;
                i5 = i2;
                i2++;
            } while (isEqual(this.key.charAt(i4), charSequence.charAt(i5)));
            return false;
        }

        private int prefixLength(String str) {
            int i2 = 0;
            while (i2 < str.length() && i2 < this.key.length()) {
                if (!isEqual(str.charAt(i2), this.key.charAt(i2))) {
                    return i2;
                }
                i2++;
            }
            return i2;
        }

        /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$PrefixTree$CI.class */
        private static class CI extends PrefixTree {
            private CI(String str, String str2, PrefixTree prefixTree) {
                super(str, str2, prefixTree);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.time.format.DateTimeFormatterBuilder.PrefixTree
            public CI newNode(String str, String str2, PrefixTree prefixTree) {
                return new CI(str, str2, prefixTree);
            }

            @Override // java.time.format.DateTimeFormatterBuilder.PrefixTree
            protected boolean isEqual(char c2, char c3) {
                return DateTimeParseContext.charEqualsIgnoreCase(c2, c3);
            }

            @Override // java.time.format.DateTimeFormatterBuilder.PrefixTree
            protected boolean prefixOf(CharSequence charSequence, int i2, int i3) {
                int i4;
                int i5;
                int length = this.key.length();
                if (length > i3 - i2) {
                    return false;
                }
                int i6 = 0;
                do {
                    int i7 = length;
                    length--;
                    if (i7 <= 0) {
                        return true;
                    }
                    i4 = i6;
                    i6++;
                    i5 = i2;
                    i2++;
                } while (isEqual(this.key.charAt(i4), charSequence.charAt(i5)));
                return false;
            }
        }

        /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$PrefixTree$LENIENT.class */
        private static class LENIENT extends CI {
            private LENIENT(String str, String str2, PrefixTree prefixTree) {
                super(str, str2, prefixTree);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.time.format.DateTimeFormatterBuilder.PrefixTree.CI, java.time.format.DateTimeFormatterBuilder.PrefixTree
            public CI newNode(String str, String str2, PrefixTree prefixTree) {
                return new LENIENT(str, str2, prefixTree);
            }

            private boolean isLenientChar(char c2) {
                return c2 == ' ' || c2 == '_' || c2 == '/';
            }

            @Override // java.time.format.DateTimeFormatterBuilder.PrefixTree
            protected String toKey(String str) {
                int i2 = 0;
                while (i2 < str.length()) {
                    if (!isLenientChar(str.charAt(i2))) {
                        i2++;
                    } else {
                        StringBuilder sb = new StringBuilder(str.length());
                        sb.append((CharSequence) str, 0, i2);
                        while (true) {
                            i2++;
                            if (i2 < str.length()) {
                                if (!isLenientChar(str.charAt(i2))) {
                                    sb.append(str.charAt(i2));
                                }
                            } else {
                                return sb.toString();
                            }
                        }
                    }
                }
                return str;
            }

            @Override // java.time.format.DateTimeFormatterBuilder.PrefixTree
            public String match(CharSequence charSequence, ParsePosition parsePosition) {
                int index = parsePosition.getIndex();
                int length = charSequence.length();
                int length2 = this.key.length();
                int i2 = 0;
                while (i2 < length2 && index < length) {
                    if (isLenientChar(charSequence.charAt(index))) {
                        index++;
                    } else {
                        int i3 = i2;
                        i2++;
                        int i4 = index;
                        index++;
                        if (!isEqual(this.key.charAt(i3), charSequence.charAt(i4))) {
                            return null;
                        }
                    }
                }
                if (i2 != length2) {
                    return null;
                }
                if (this.child != null && index != length) {
                    int i5 = index;
                    while (i5 < length && isLenientChar(charSequence.charAt(i5))) {
                        i5++;
                    }
                    if (i5 < length) {
                        PrefixTree prefixTree = this.child;
                        while (true) {
                            if (isEqual(prefixTree.c0, charSequence.charAt(i5))) {
                                parsePosition.setIndex(i5);
                                String strMatch = prefixTree.match(charSequence, parsePosition);
                                if (strMatch != null) {
                                    return strMatch;
                                }
                            } else {
                                prefixTree = prefixTree.sibling;
                                if (prefixTree == null) {
                                    break;
                                }
                            }
                        }
                    }
                }
                parsePosition.setIndex(index);
                return this.value;
            }
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$ChronoPrinterParser.class */
    static final class ChronoPrinterParser implements DateTimePrinterParser {
        private final TextStyle textStyle;

        ChronoPrinterParser(TextStyle textStyle) {
            this.textStyle = textStyle;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Chronology chronology = (Chronology) dateTimePrintContext.getValue(TemporalQueries.chronology());
            if (chronology == null) {
                return false;
            }
            if (this.textStyle == null) {
                sb.append(chronology.getId());
                return true;
            }
            sb.append(getChronologyName(chronology, dateTimePrintContext.getLocale()));
            return true;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            String chronologyName;
            if (i2 < 0 || i2 > charSequence.length()) {
                throw new IndexOutOfBoundsException();
            }
            Chronology chronology = null;
            int i3 = -1;
            for (Chronology chronology2 : Chronology.getAvailableChronologies()) {
                if (this.textStyle == null) {
                    chronologyName = chronology2.getId();
                } else {
                    chronologyName = getChronologyName(chronology2, dateTimeParseContext.getLocale());
                }
                int length = chronologyName.length();
                if (length > i3 && dateTimeParseContext.subSequenceEquals(charSequence, i2, chronologyName, 0, length)) {
                    chronology = chronology2;
                    i3 = length;
                }
            }
            if (chronology == null) {
                return i2 ^ (-1);
            }
            dateTimeParseContext.setParsed(chronology);
            return i2 + i3;
        }

        private String getChronologyName(Chronology chronology, Locale locale) {
            String str = (String) DateTimeTextProvider.getLocalizedResource("calendarname." + chronology.getCalendarType(), locale);
            return str != null ? str : chronology.getId();
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$LocalizedPrinterParser.class */
    static final class LocalizedPrinterParser implements DateTimePrinterParser {
        private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap(16, 0.75f, 2);
        private final FormatStyle dateStyle;
        private final FormatStyle timeStyle;

        LocalizedPrinterParser(FormatStyle formatStyle, FormatStyle formatStyle2) {
            this.dateStyle = formatStyle;
            this.timeStyle = formatStyle2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return formatter(dateTimePrintContext.getLocale(), Chronology.from(dateTimePrintContext.getTemporal())).toPrinterParser(false).format(dateTimePrintContext, sb);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            return formatter(dateTimeParseContext.getLocale(), dateTimeParseContext.getEffectiveChronology()).toPrinterParser(false).parse(dateTimeParseContext, charSequence, i2);
        }

        private DateTimeFormatter formatter(Locale locale, Chronology chronology) {
            String str = chronology.getId() + '|' + locale.toString() + '|' + ((Object) this.dateStyle) + ((Object) this.timeStyle);
            DateTimeFormatter formatter = FORMATTER_CACHE.get(str);
            if (formatter == null) {
                formatter = new DateTimeFormatterBuilder().appendPattern(DateTimeFormatterBuilder.getLocalizedDateTimePattern(this.dateStyle, this.timeStyle, chronology, locale)).toFormatter(locale);
                DateTimeFormatter dateTimeFormatterPutIfAbsent = FORMATTER_CACHE.putIfAbsent(str, formatter);
                if (dateTimeFormatterPutIfAbsent != null) {
                    formatter = dateTimeFormatterPutIfAbsent;
                }
            }
            return formatter;
        }

        public String toString() {
            return "Localized(" + (this.dateStyle != null ? this.dateStyle : "") + "," + (this.timeStyle != null ? this.timeStyle : "") + ")";
        }
    }

    /* loaded from: rt.jar:java/time/format/DateTimeFormatterBuilder$WeekBasedFieldPrinterParser.class */
    static final class WeekBasedFieldPrinterParser implements DateTimePrinterParser {
        private char chr;
        private int count;

        WeekBasedFieldPrinterParser(char c2, int i2) {
            this.chr = c2;
            this.count = i2;
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return printerParser(dateTimePrintContext.getLocale()).format(dateTimePrintContext, sb);
        }

        @Override // java.time.format.DateTimeFormatterBuilder.DateTimePrinterParser
        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i2) {
            return printerParser(dateTimeParseContext.getLocale()).parse(dateTimeParseContext, charSequence, i2);
        }

        private DateTimePrinterParser printerParser(Locale locale) {
            TemporalField temporalFieldWeekOfMonth;
            WeekFields weekFieldsOf = WeekFields.of(locale);
            switch (this.chr) {
                case 'W':
                    temporalFieldWeekOfMonth = weekFieldsOf.weekOfMonth();
                    break;
                case 'Y':
                    TemporalField temporalFieldWeekBasedYear = weekFieldsOf.weekBasedYear();
                    if (this.count == 2) {
                        return new ReducedPrinterParser(temporalFieldWeekBasedYear, 2, 2, 0, ReducedPrinterParser.BASE_DATE, 0);
                    }
                    return new NumberPrinterParser(temporalFieldWeekBasedYear, this.count, 19, this.count < 4 ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD, -1);
                case 'c':
                case 'e':
                    temporalFieldWeekOfMonth = weekFieldsOf.dayOfWeek();
                    break;
                case 'w':
                    temporalFieldWeekOfMonth = weekFieldsOf.weekOfWeekBasedYear();
                    break;
                default:
                    throw new IllegalStateException("unreachable");
            }
            return new NumberPrinterParser(temporalFieldWeekOfMonth, this.count == 2 ? 2 : 1, 2, SignStyle.NOT_NEGATIVE);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(30);
            sb.append("Localized(");
            if (this.chr == 'Y') {
                if (this.count == 1) {
                    sb.append("WeekBasedYear");
                } else if (this.count == 2) {
                    sb.append("ReducedValue(WeekBasedYear,2,2,2000-01-01)");
                } else {
                    sb.append("WeekBasedYear,").append(this.count).append(",").append(19).append(",").append((Object) (this.count < 4 ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD));
                }
            } else {
                switch (this.chr) {
                    case 'W':
                        sb.append("WeekOfMonth");
                        break;
                    case 'c':
                    case 'e':
                        sb.append("DayOfWeek");
                        break;
                    case 'w':
                        sb.append("WeekOfWeekBasedYear");
                        break;
                }
                sb.append(",");
                sb.append(this.count);
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
