package java.text;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.icepdf.core.util.PdfOps;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.ZoneInfoFile;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:java/text/SimpleDateFormat.class */
public class SimpleDateFormat extends DateFormat {
    static final long serialVersionUID = 4774881970558875024L;
    static final int currentSerialVersion = 1;
    private int serialVersionOnStream;
    private String pattern;
    private transient NumberFormat originalNumberFormat;
    private transient String originalNumberPattern;
    private transient char minusSign;
    private transient boolean hasFollowingMinusSign;
    private transient boolean forceStandaloneForm;
    private transient char[] compiledPattern;
    private static final int TAG_QUOTE_ASCII_CHAR = 100;
    private static final int TAG_QUOTE_CHARS = 101;
    private transient char zeroDigit;
    private DateFormatSymbols formatData;
    private Date defaultCenturyStart;
    private transient int defaultCenturyStartYear;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final String GMT = "GMT";
    private static final ConcurrentMap<Locale, NumberFormat> cachedNumberFormatData;
    private Locale locale;
    transient boolean useDateFormatSymbols;
    private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD;
    private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD;
    private static final DateFormat.Field[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID;
    private static final int[] REST_OF_STYLES;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SimpleDateFormat.class.desiredAssertionStatus();
        cachedNumberFormatData = new ConcurrentHashMap(3);
        PATTERN_INDEX_TO_CALENDAR_FIELD = new int[]{0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 6, 8, 3, 4, 9, 10, 10, 15, 15, 17, 1000, 15, 2};
        PATTERN_INDEX_TO_DATE_FORMAT_FIELD = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 17, 1, 9, 17, 2};
        PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID = new DateFormat.Field[]{DateFormat.Field.ERA, DateFormat.Field.YEAR, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_MONTH, DateFormat.Field.HOUR_OF_DAY1, DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.MINUTE, DateFormat.Field.SECOND, DateFormat.Field.MILLISECOND, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.DAY_OF_YEAR, DateFormat.Field.DAY_OF_WEEK_IN_MONTH, DateFormat.Field.WEEK_OF_YEAR, DateFormat.Field.WEEK_OF_MONTH, DateFormat.Field.AM_PM, DateFormat.Field.HOUR1, DateFormat.Field.HOUR0, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.TIME_ZONE, DateFormat.Field.MONTH};
        REST_OF_STYLES = new int[]{Calendar.SHORT_STANDALONE, 2, Calendar.LONG_STANDALONE};
    }

    public SimpleDateFormat() {
        this("", Locale.getDefault(Locale.Category.FORMAT));
        applyPatternImpl(LocaleProviderAdapter.getResourceBundleBased().getLocaleResources(this.locale).getDateTimePattern(3, 3, this.calendar));
    }

    public SimpleDateFormat(String str) {
        this(str, Locale.getDefault(Locale.Category.FORMAT));
    }

    public SimpleDateFormat(String str, Locale locale) {
        this.serialVersionOnStream = 1;
        this.minusSign = '-';
        this.hasFollowingMinusSign = false;
        this.forceStandaloneForm = false;
        if (str == null || locale == null) {
            throw new NullPointerException();
        }
        initializeCalendar(locale);
        this.pattern = str;
        this.formatData = DateFormatSymbols.getInstanceRef(locale);
        this.locale = locale;
        initialize(locale);
    }

    public SimpleDateFormat(String str, DateFormatSymbols dateFormatSymbols) {
        this.serialVersionOnStream = 1;
        this.minusSign = '-';
        this.hasFollowingMinusSign = false;
        this.forceStandaloneForm = false;
        if (str == null || dateFormatSymbols == null) {
            throw new NullPointerException();
        }
        this.pattern = str;
        this.formatData = (DateFormatSymbols) dateFormatSymbols.clone();
        this.locale = Locale.getDefault(Locale.Category.FORMAT);
        initializeCalendar(this.locale);
        initialize(this.locale);
        this.useDateFormatSymbols = true;
    }

    private void initialize(Locale locale) {
        this.compiledPattern = compile(this.pattern);
        this.numberFormat = cachedNumberFormatData.get(locale);
        if (this.numberFormat == null) {
            this.numberFormat = NumberFormat.getIntegerInstance(locale);
            this.numberFormat.setGroupingUsed(false);
            cachedNumberFormatData.putIfAbsent(locale, this.numberFormat);
        }
        this.numberFormat = (NumberFormat) this.numberFormat.clone();
        initializeDefaultCentury();
    }

    private void initializeCalendar(Locale locale) {
        if (this.calendar == null) {
            if (!$assertionsDisabled && locale == null) {
                throw new AssertionError();
            }
            this.calendar = Calendar.getInstance(TimeZone.getDefault(), locale);
        }
    }

    private char[] compile(String str) {
        char cCharAt;
        char cCharAt2;
        int length = str.length();
        boolean z2 = false;
        StringBuilder sb = new StringBuilder(length * 2);
        StringBuilder sb2 = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = -1;
        int i5 = -1;
        int i6 = 0;
        while (i6 < length) {
            char cCharAt3 = str.charAt(i6);
            if (cCharAt3 == '\'') {
                if (i6 + 1 < length && (cCharAt2 = str.charAt(i6 + 1)) == '\'') {
                    i6++;
                    if (i2 != 0) {
                        encode(i4, i2, sb);
                        i3++;
                        i5 = i4;
                        i4 = -1;
                        i2 = 0;
                    }
                    if (z2) {
                        sb2.append(cCharAt2);
                    } else {
                        sb.append((char) (25600 | cCharAt2));
                    }
                } else if (!z2) {
                    if (i2 != 0) {
                        encode(i4, i2, sb);
                        i3++;
                        i5 = i4;
                        i4 = -1;
                        i2 = 0;
                    }
                    if (sb2 == null) {
                        sb2 = new StringBuilder(length);
                    } else {
                        sb2.setLength(0);
                    }
                    z2 = true;
                } else {
                    int length2 = sb2.length();
                    if (length2 == 1) {
                        char cCharAt4 = sb2.charAt(0);
                        if (cCharAt4 < 128) {
                            sb.append((char) (25600 | cCharAt4));
                        } else {
                            sb.append((char) 25857);
                            sb.append(cCharAt4);
                        }
                    } else {
                        encode(101, length2, sb);
                        sb.append((CharSequence) sb2);
                    }
                    z2 = false;
                }
            } else if (z2) {
                sb2.append(cCharAt3);
            } else if ((cCharAt3 < 'a' || cCharAt3 > 'z') && (cCharAt3 < 'A' || cCharAt3 > 'Z')) {
                if (i2 != 0) {
                    encode(i4, i2, sb);
                    i3++;
                    i5 = i4;
                    i4 = -1;
                    i2 = 0;
                }
                if (cCharAt3 < 128) {
                    sb.append((char) (25600 | cCharAt3));
                } else {
                    int i7 = i6 + 1;
                    while (i7 < length && (cCharAt = str.charAt(i7)) != '\'' && ((cCharAt < 'a' || cCharAt > 'z') && (cCharAt < 'A' || cCharAt > 'Z'))) {
                        i7++;
                    }
                    sb.append((char) (25856 | (i7 - i6)));
                    while (i6 < i7) {
                        sb.append(str.charAt(i6));
                        i6++;
                    }
                    i6--;
                }
            } else {
                int iIndexOf = "GyMdkHmsSEDFwWahKzZYuXL".indexOf(cCharAt3);
                if (iIndexOf == -1) {
                    throw new IllegalArgumentException("Illegal pattern character '" + cCharAt3 + PdfOps.SINGLE_QUOTE_TOKEN);
                }
                if (i4 == -1 || i4 == iIndexOf) {
                    i4 = iIndexOf;
                    i2++;
                } else {
                    encode(i4, i2, sb);
                    i3++;
                    i5 = i4;
                    i4 = iIndexOf;
                    i2 = 1;
                }
            }
            i6++;
        }
        if (z2) {
            throw new IllegalArgumentException("Unterminated quote");
        }
        if (i2 != 0) {
            encode(i4, i2, sb);
            i3++;
            i5 = i4;
        }
        this.forceStandaloneForm = i3 == 1 && i5 == 2;
        int length3 = sb.length();
        char[] cArr = new char[length3];
        sb.getChars(0, length3, cArr, 0);
        return cArr;
    }

    private static void encode(int i2, int i3, StringBuilder sb) {
        if (i2 == 21 && i3 >= 4) {
            throw new IllegalArgumentException("invalid ISO 8601 format: length=" + i3);
        }
        if (i3 < 255) {
            sb.append((char) ((i2 << 8) | i3));
            return;
        }
        sb.append((char) ((i2 << 8) | 255));
        sb.append((char) (i3 >>> 16));
        sb.append((char) (i3 & 65535));
    }

    private void initializeDefaultCentury() {
        this.calendar.setTimeInMillis(System.currentTimeMillis());
        this.calendar.add(1, -80);
        parseAmbiguousDatesAsAfter(this.calendar.getTime());
    }

    private void parseAmbiguousDatesAsAfter(Date date) {
        this.defaultCenturyStart = date;
        this.calendar.setTime(date);
        this.defaultCenturyStartYear = this.calendar.get(1);
    }

    public void set2DigitYearStart(Date date) {
        parseAmbiguousDatesAsAfter(new Date(date.getTime()));
    }

    public Date get2DigitYearStart() {
        return (Date) this.defaultCenturyStart.clone();
    }

    @Override // java.text.DateFormat
    public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        fieldPosition.endIndex = 0;
        fieldPosition.beginIndex = 0;
        return format(date, stringBuffer, fieldPosition.getFieldDelegate());
    }

    private StringBuffer format(Date date, StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate) {
        this.calendar.setTime(date);
        boolean zUseDateFormatSymbols = useDateFormatSymbols();
        int i2 = 0;
        while (i2 < this.compiledPattern.length) {
            int i3 = this.compiledPattern[i2] >>> '\b';
            int i4 = i2;
            i2++;
            int i5 = this.compiledPattern[i4] & 255;
            if (i5 == 255) {
                int i6 = i2 + 1;
                int i7 = this.compiledPattern[i2] << 16;
                i2 = i6 + 1;
                i5 = i7 | this.compiledPattern[i6];
            }
            switch (i3) {
                case 100:
                    stringBuffer.append((char) i5);
                    break;
                case 101:
                    stringBuffer.append(this.compiledPattern, i2, i5);
                    i2 += i5;
                    break;
                default:
                    subFormat(i3, i5, fieldDelegate, stringBuffer, zUseDateFormatSymbols);
                    break;
            }
        }
        return stringBuffer;
    }

    @Override // java.text.Format
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        StringBuffer stringBuffer = new StringBuffer();
        CharacterIteratorFieldDelegate characterIteratorFieldDelegate = new CharacterIteratorFieldDelegate();
        if (obj instanceof Date) {
            format((Date) obj, stringBuffer, characterIteratorFieldDelegate);
        } else if (obj instanceof Number) {
            format(new Date(((Number) obj).longValue()), stringBuffer, characterIteratorFieldDelegate);
        } else {
            if (obj == null) {
                throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
            }
            throw new IllegalArgumentException("Cannot format given Object as a Date");
        }
        return characterIteratorFieldDelegate.getIterator(stringBuffer.toString());
    }

    private void subFormat(int i2, int i3, Format.FieldDelegate fieldDelegate, StringBuffer stringBuffer, boolean z2) {
        int iSODayOfWeek;
        String displayName = null;
        int length = stringBuffer.length();
        int i4 = PATTERN_INDEX_TO_CALENDAR_FIELD[i2];
        if (i4 == 17) {
            if (this.calendar.isWeekDateSupported()) {
                iSODayOfWeek = this.calendar.getWeekYear();
            } else {
                i2 = 1;
                i4 = PATTERN_INDEX_TO_CALENDAR_FIELD[1];
                iSODayOfWeek = this.calendar.get(i4);
            }
        } else if (i4 == 1000) {
            iSODayOfWeek = CalendarBuilder.toISODayOfWeek(this.calendar.get(7));
        } else {
            iSODayOfWeek = this.calendar.get(i4);
        }
        int i5 = i3 >= 4 ? 2 : 1;
        if (!z2 && i4 < 15 && i2 != 22) {
            displayName = this.calendar.getDisplayName(i4, i5, this.locale);
        }
        switch (i2) {
            case 0:
                if (z2) {
                    String[] eras = this.formatData.getEras();
                    if (iSODayOfWeek < eras.length) {
                        displayName = eras[iSODayOfWeek];
                    }
                }
                if (displayName == null) {
                    displayName = "";
                    break;
                }
                break;
            case 1:
            case 19:
                if (this.calendar instanceof GregorianCalendar) {
                    if (i3 != 2) {
                        zeroPaddingNumber(iSODayOfWeek, i3, Integer.MAX_VALUE, stringBuffer);
                        break;
                    } else {
                        zeroPaddingNumber(iSODayOfWeek, 2, 2, stringBuffer);
                        break;
                    }
                } else if (displayName == null) {
                    zeroPaddingNumber(iSODayOfWeek, i5 == 2 ? 1 : i3, Integer.MAX_VALUE, stringBuffer);
                    break;
                }
                break;
            case 2:
                if (z2) {
                    if (i3 >= 4) {
                        displayName = this.formatData.getMonths()[iSODayOfWeek];
                    } else if (i3 == 3) {
                        displayName = this.formatData.getShortMonths()[iSODayOfWeek];
                    }
                } else if (i3 < 3) {
                    displayName = null;
                } else if (this.forceStandaloneForm) {
                    displayName = this.calendar.getDisplayName(i4, i5 | 32768, this.locale);
                    if (displayName == null) {
                        displayName = this.calendar.getDisplayName(i4, i5, this.locale);
                    }
                }
                if (displayName == null) {
                    zeroPaddingNumber(iSODayOfWeek + 1, i3, Integer.MAX_VALUE, stringBuffer);
                    break;
                }
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 16:
            case 20:
            default:
                if (displayName == null) {
                    zeroPaddingNumber(iSODayOfWeek, i3, Integer.MAX_VALUE, stringBuffer);
                    break;
                }
                break;
            case 4:
                if (displayName == null) {
                    if (iSODayOfWeek == 0) {
                        zeroPaddingNumber(this.calendar.getMaximum(11) + 1, i3, Integer.MAX_VALUE, stringBuffer);
                        break;
                    } else {
                        zeroPaddingNumber(iSODayOfWeek, i3, Integer.MAX_VALUE, stringBuffer);
                        break;
                    }
                }
                break;
            case 9:
                if (z2) {
                    if (i3 >= 4) {
                        displayName = this.formatData.getWeekdays()[iSODayOfWeek];
                        break;
                    } else {
                        displayName = this.formatData.getShortWeekdays()[iSODayOfWeek];
                        break;
                    }
                }
                break;
            case 14:
                if (z2) {
                    displayName = this.formatData.getAmPmStrings()[iSODayOfWeek];
                    break;
                }
                break;
            case 15:
                if (displayName == null) {
                    if (iSODayOfWeek == 0) {
                        zeroPaddingNumber(this.calendar.getLeastMaximum(10) + 1, i3, Integer.MAX_VALUE, stringBuffer);
                        break;
                    } else {
                        zeroPaddingNumber(iSODayOfWeek, i3, Integer.MAX_VALUE, stringBuffer);
                        break;
                    }
                }
                break;
            case 17:
                if (displayName == null) {
                    if (this.formatData.locale == null || this.formatData.isZoneStringsSet) {
                        int zoneIndex = this.formatData.getZoneIndex(this.calendar.getTimeZone().getID());
                        if (zoneIndex == -1) {
                            stringBuffer.append(ZoneInfoFile.toCustomID(this.calendar.get(15) + this.calendar.get(16)));
                            break;
                        } else {
                            int i6 = this.calendar.get(16) == 0 ? 1 : 3;
                            if (i3 < 4) {
                                i6++;
                            }
                            stringBuffer.append(this.formatData.getZoneStringsWrapper()[zoneIndex][i6]);
                            break;
                        }
                    } else {
                        stringBuffer.append(this.calendar.getTimeZone().getDisplayName(this.calendar.get(16) != 0, i3 < 4 ? 0 : 1, this.formatData.locale));
                        break;
                    }
                }
                break;
            case 18:
                int i7 = (this.calendar.get(15) + this.calendar.get(16)) / MILLIS_PER_MINUTE;
                int i8 = 4;
                if (i7 >= 0) {
                    stringBuffer.append('+');
                } else {
                    i8 = 4 + 1;
                }
                CalendarUtils.sprintf0d(stringBuffer, ((i7 / 60) * 100) + (i7 % 60), i8);
                break;
            case 21:
                int i9 = this.calendar.get(15) + this.calendar.get(16);
                if (i9 == 0) {
                    stringBuffer.append('Z');
                    break;
                } else {
                    int i10 = i9 / MILLIS_PER_MINUTE;
                    if (i10 >= 0) {
                        stringBuffer.append('+');
                    } else {
                        stringBuffer.append('-');
                        i10 = -i10;
                    }
                    CalendarUtils.sprintf0d(stringBuffer, i10 / 60, 2);
                    if (i3 != 1) {
                        if (i3 == 3) {
                            stringBuffer.append(':');
                        }
                        CalendarUtils.sprintf0d(stringBuffer, i10 % 60, 2);
                        break;
                    }
                }
                break;
            case 22:
                if (!$assertionsDisabled && displayName != null) {
                    throw new AssertionError();
                }
                if (this.locale == null) {
                    if (i3 >= 4) {
                        displayName = this.formatData.getMonths()[iSODayOfWeek];
                    } else if (i3 == 3) {
                        displayName = this.formatData.getShortMonths()[iSODayOfWeek];
                    }
                } else if (i3 >= 3) {
                    displayName = this.calendar.getDisplayName(i4, i5 | 32768, this.locale);
                }
                if (displayName == null) {
                    zeroPaddingNumber(iSODayOfWeek + 1, i3, Integer.MAX_VALUE, stringBuffer);
                    break;
                }
                break;
        }
        if (displayName != null) {
            stringBuffer.append(displayName);
        }
        int i11 = PATTERN_INDEX_TO_DATE_FORMAT_FIELD[i2];
        DateFormat.Field field = PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID[i2];
        fieldDelegate.formatted(i11, field, field, length, stringBuffer.length(), stringBuffer);
    }

    private void zeroPaddingNumber(int i2, int i3, int i4, StringBuffer stringBuffer) {
        try {
            if (this.zeroDigit == 0) {
                this.zeroDigit = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols().getZeroDigit();
            }
            if (i2 >= 0) {
                if (i2 < 100 && i3 >= 1 && i3 <= 2) {
                    if (i2 < 10) {
                        if (i3 == 2) {
                            stringBuffer.append(this.zeroDigit);
                        }
                        stringBuffer.append((char) (this.zeroDigit + i2));
                        return;
                    } else {
                        stringBuffer.append((char) (this.zeroDigit + (i2 / 10)));
                        stringBuffer.append((char) (this.zeroDigit + (i2 % 10)));
                        return;
                    }
                }
                if (i2 >= 1000 && i2 < 10000) {
                    if (i3 == 4) {
                        stringBuffer.append((char) (this.zeroDigit + (i2 / 1000)));
                        int i5 = i2 % 1000;
                        stringBuffer.append((char) (this.zeroDigit + (i5 / 100)));
                        int i6 = i5 % 100;
                        stringBuffer.append((char) (this.zeroDigit + (i6 / 10)));
                        stringBuffer.append((char) (this.zeroDigit + (i6 % 10)));
                        return;
                    }
                    if (i3 == 2 && i4 == 2) {
                        zeroPaddingNumber(i2 % 100, 2, 2, stringBuffer);
                        return;
                    }
                }
            }
        } catch (Exception e2) {
        }
        this.numberFormat.setMinimumIntegerDigits(i3);
        this.numberFormat.setMaximumIntegerDigits(i4);
        this.numberFormat.format(i2, stringBuffer, DontCareFieldPosition.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:75:0x0027, code lost:
    
        continue;
     */
    @Override // java.text.DateFormat
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Date parse(java.lang.String r12, java.text.ParsePosition r13) {
        /*
            Method dump skipped, instructions count: 456
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.parse(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    private int matchString(String str, int i2, int i3, String[] strArr, CalendarBuilder calendarBuilder) {
        int i4 = 0;
        int length = strArr.length;
        if (i3 == 7) {
            i4 = 1;
        }
        int i5 = 0;
        int i6 = -1;
        while (i4 < length) {
            int length2 = strArr[i4].length();
            if (length2 > i5 && str.regionMatches(true, i2, strArr[i4], 0, length2)) {
                i6 = i4;
                i5 = length2;
            }
            i4++;
        }
        if (i6 >= 0) {
            calendarBuilder.set(i3, i6);
            return i2 + i5;
        }
        return -i2;
    }

    private int matchString(String str, int i2, int i3, Map<String, Integer> map, CalendarBuilder calendarBuilder) {
        if (map != null) {
            if (map instanceof SortedMap) {
                for (String str2 : map.keySet()) {
                    if (str.regionMatches(true, i2, str2, 0, str2.length())) {
                        calendarBuilder.set(i3, map.get(str2).intValue());
                        return i2 + str2.length();
                    }
                }
                return -i2;
            }
            String str3 = null;
            for (String str4 : map.keySet()) {
                int length = str4.length();
                if (str3 == null || length > str3.length()) {
                    if (str.regionMatches(true, i2, str4, 0, length)) {
                        str3 = str4;
                    }
                }
            }
            if (str3 != null) {
                calendarBuilder.set(i3, map.get(str3).intValue());
                return i2 + str3.length();
            }
        }
        return -i2;
    }

    private int matchZoneString(String str, int i2, String[] strArr) {
        for (int i3 = 1; i3 <= 4; i3++) {
            String str2 = strArr[i3];
            if (str.regionMatches(true, i2, str2, 0, str2.length())) {
                return i3;
            }
        }
        return -1;
    }

    private boolean matchDSTString(String str, int i2, int i3, int i4, String[][] strArr) {
        String str2 = strArr[i3][i4 + 2];
        if (str.regionMatches(true, i2, str2, 0, str2.length())) {
            return true;
        }
        return false;
    }

    private int subParseZoneString(String str, int i2, CalendarBuilder calendarBuilder) {
        int zoneIndex;
        boolean zEqualsIgnoreCase = false;
        TimeZone timeZone = getTimeZone();
        int zoneIndex2 = this.formatData.getZoneIndex(timeZone.getID());
        TimeZone timeZone2 = null;
        String[][] zoneStringsWrapper = this.formatData.getZoneStringsWrapper();
        String[] strArr = null;
        int i3 = 0;
        if (zoneIndex2 != -1) {
            strArr = zoneStringsWrapper[zoneIndex2];
            int iMatchZoneString = matchZoneString(str, i2, strArr);
            i3 = iMatchZoneString;
            if (iMatchZoneString > 0) {
                if (i3 <= 2) {
                    zEqualsIgnoreCase = strArr[i3].equalsIgnoreCase(strArr[i3 + 2]);
                }
                timeZone2 = TimeZone.getTimeZone(strArr[0]);
            }
        }
        if (timeZone2 == null && (zoneIndex = this.formatData.getZoneIndex(TimeZone.getDefault().getID())) != -1) {
            strArr = zoneStringsWrapper[zoneIndex];
            int iMatchZoneString2 = matchZoneString(str, i2, strArr);
            i3 = iMatchZoneString2;
            if (iMatchZoneString2 > 0) {
                if (i3 <= 2) {
                    zEqualsIgnoreCase = strArr[i3].equalsIgnoreCase(strArr[i3 + 2]);
                }
                timeZone2 = TimeZone.getTimeZone(strArr[0]);
            }
        }
        if (timeZone2 == null) {
            int length = zoneStringsWrapper.length;
            int i4 = 0;
            while (true) {
                if (i4 >= length) {
                    break;
                }
                strArr = zoneStringsWrapper[i4];
                int iMatchZoneString3 = matchZoneString(str, i2, strArr);
                i3 = iMatchZoneString3;
                if (iMatchZoneString3 <= 0) {
                    i4++;
                } else {
                    if (i3 <= 2) {
                        zEqualsIgnoreCase = strArr[i3].equalsIgnoreCase(strArr[i3 + 2]);
                    }
                    timeZone2 = TimeZone.getTimeZone(strArr[0]);
                }
            }
        }
        if (timeZone2 != null) {
            if (!timeZone2.equals(timeZone)) {
                setTimeZone(timeZone2);
            }
            int dSTSavings = i3 >= 3 ? timeZone2.getDSTSavings() : 0;
            if (!zEqualsIgnoreCase && (i3 < 3 || dSTSavings != 0)) {
                calendarBuilder.clear(15).set(16, dSTSavings);
            }
            return i2 + strArr[i3].length();
        }
        return -i2;
    }

    private int subParseNumericZone(String str, int i2, int i3, int i4, boolean z2, CalendarBuilder calendarBuilder) {
        int i5 = i2;
        try {
            i5++;
            char cCharAt = str.charAt(i5);
            if (isDigit(cCharAt)) {
                int i6 = cCharAt - 48;
                i5++;
                char cCharAt2 = str.charAt(i5);
                if (isDigit(cCharAt2)) {
                    i6 = (i6 * 10) + (cCharAt2 - 48);
                } else if (i4 <= 0 && z2) {
                    i5--;
                }
                if (i6 <= 23) {
                    int i7 = 0;
                    if (i4 != 1) {
                        int i8 = i5;
                        i5++;
                        char cCharAt3 = str.charAt(i8);
                        if (z2) {
                            if (cCharAt3 == ':') {
                                i5++;
                                cCharAt3 = str.charAt(i5);
                            }
                        }
                        if (isDigit(cCharAt3)) {
                            int i9 = cCharAt3 - 48;
                            int i10 = i5;
                            i5++;
                            char cCharAt4 = str.charAt(i10);
                            if (isDigit(cCharAt4)) {
                                i7 = (i9 * 10) + (cCharAt4 - 48);
                                if (i7 > 59) {
                                }
                            }
                        }
                    }
                    calendarBuilder.set(15, (i7 + (i6 * 60)) * MILLIS_PER_MINUTE * i3).set(16, 0);
                    return i5;
                }
            }
        } catch (IndexOutOfBoundsException e2) {
        }
        return 1 - i5;
    }

    private boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x02ae  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0323  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x035d  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0432  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01ea  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int subParse(java.lang.String r9, int r10, int r11, int r12, boolean r13, boolean[] r14, java.text.ParsePosition r15, boolean r16, java.text.CalendarBuilder r17) {
        /*
            Method dump skipped, instructions count: 1765
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.subParse(java.lang.String, int, int, int, boolean, boolean[], java.text.ParsePosition, boolean, java.text.CalendarBuilder):int");
    }

    private boolean useDateFormatSymbols() {
        return this.useDateFormatSymbols || this.locale == null;
    }

    private String translatePattern(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (z2) {
                if (cCharAt == '\'') {
                    z2 = false;
                }
            } else if (cCharAt == '\'') {
                z2 = true;
            } else if ((cCharAt >= 'a' && cCharAt <= 'z') || (cCharAt >= 'A' && cCharAt <= 'Z')) {
                int iIndexOf = str2.indexOf(cCharAt);
                if (iIndexOf >= 0) {
                    if (iIndexOf < str3.length()) {
                        cCharAt = str3.charAt(iIndexOf);
                    }
                } else {
                    throw new IllegalArgumentException("Illegal pattern  character '" + cCharAt + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
            sb.append(cCharAt);
        }
        if (z2) {
            throw new IllegalArgumentException("Unfinished quote in pattern");
        }
        return sb.toString();
    }

    public String toPattern() {
        return this.pattern;
    }

    public String toLocalizedPattern() {
        return translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzZYuXL", this.formatData.getLocalPatternChars());
    }

    public void applyPattern(String str) {
        applyPatternImpl(str);
    }

    private void applyPatternImpl(String str) {
        this.compiledPattern = compile(str);
        this.pattern = str;
    }

    public void applyLocalizedPattern(String str) {
        String strTranslatePattern = translatePattern(str, this.formatData.getLocalPatternChars(), "GyMdkHmsSEDFwWahKzZYuXL");
        this.compiledPattern = compile(strTranslatePattern);
        this.pattern = strTranslatePattern;
    }

    public DateFormatSymbols getDateFormatSymbols() {
        return (DateFormatSymbols) this.formatData.clone();
    }

    public void setDateFormatSymbols(DateFormatSymbols dateFormatSymbols) {
        this.formatData = (DateFormatSymbols) dateFormatSymbols.clone();
        this.useDateFormatSymbols = true;
    }

    @Override // java.text.DateFormat, java.text.Format
    public Object clone() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) super.clone();
        simpleDateFormat.formatData = (DateFormatSymbols) this.formatData.clone();
        return simpleDateFormat;
    }

    @Override // java.text.DateFormat
    public int hashCode() {
        return this.pattern.hashCode();
    }

    @Override // java.text.DateFormat
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) obj;
        return this.pattern.equals(simpleDateFormat.pattern) && this.formatData.equals(simpleDateFormat.formatData);
    }

    private Map<String, Integer> getDisplayNamesMap(int i2, Locale locale) {
        Map<String, Integer> displayNames = this.calendar.getDisplayNames(i2, 1, locale);
        for (int i3 : REST_OF_STYLES) {
            Map<String, Integer> displayNames2 = this.calendar.getDisplayNames(i2, i3, locale);
            if (displayNames2 != null) {
                displayNames.putAll(displayNames2);
            }
        }
        return displayNames;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String id;
        TimeZone timeZone;
        objectInputStream.defaultReadObject();
        try {
            this.compiledPattern = compile(this.pattern);
            if (this.serialVersionOnStream < 1) {
                initializeDefaultCentury();
            } else {
                parseAmbiguousDatesAsAfter(this.defaultCenturyStart);
            }
            this.serialVersionOnStream = 1;
            TimeZone timeZone2 = getTimeZone();
            if ((timeZone2 instanceof SimpleTimeZone) && (timeZone = TimeZone.getTimeZone((id = timeZone2.getID()))) != null && timeZone.hasSameRules(timeZone2) && timeZone.getID().equals(id)) {
                setTimeZone(timeZone);
            }
        } catch (Exception e2) {
            throw new InvalidObjectException("invalid pattern");
        }
    }

    private void checkNegativeNumberExpression() {
        int iIndexOf;
        if ((this.numberFormat instanceof DecimalFormat) && !this.numberFormat.equals(this.originalNumberFormat)) {
            String pattern = ((DecimalFormat) this.numberFormat).toPattern();
            if (!pattern.equals(this.originalNumberPattern)) {
                this.hasFollowingMinusSign = false;
                int iIndexOf2 = pattern.indexOf(59);
                if (iIndexOf2 > -1 && (iIndexOf = pattern.indexOf(45, iIndexOf2)) > pattern.lastIndexOf(48) && iIndexOf > pattern.lastIndexOf(35)) {
                    this.hasFollowingMinusSign = true;
                    this.minusSign = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols().getMinusSign();
                }
                this.originalNumberPattern = pattern;
            }
            this.originalNumberFormat = this.numberFormat;
        }
    }
}
