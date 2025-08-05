package java.util;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.BuddhistCalendar;
import sun.util.calendar.ZoneInfo;
import sun.util.locale.provider.CalendarDataUtility;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.spi.CalendarProvider;

/* loaded from: rt.jar:java/util/Calendar.class */
public abstract class Calendar implements Serializable, Cloneable, Comparable<Calendar> {
    public static final int ERA = 0;
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int WEEK_OF_YEAR = 3;
    public static final int WEEK_OF_MONTH = 4;
    public static final int DATE = 5;
    public static final int DAY_OF_MONTH = 5;
    public static final int DAY_OF_YEAR = 6;
    public static final int DAY_OF_WEEK = 7;
    public static final int DAY_OF_WEEK_IN_MONTH = 8;
    public static final int AM_PM = 9;
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int MILLISECOND = 14;
    public static final int ZONE_OFFSET = 15;
    public static final int DST_OFFSET = 16;
    public static final int FIELD_COUNT = 17;
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
    public static final int JANUARY = 0;
    public static final int FEBRUARY = 1;
    public static final int MARCH = 2;
    public static final int APRIL = 3;
    public static final int MAY = 4;
    public static final int JUNE = 5;
    public static final int JULY = 6;
    public static final int AUGUST = 7;
    public static final int SEPTEMBER = 8;
    public static final int OCTOBER = 9;
    public static final int NOVEMBER = 10;
    public static final int DECEMBER = 11;
    public static final int UNDECIMBER = 12;
    public static final int AM = 0;
    public static final int PM = 1;
    public static final int ALL_STYLES = 0;
    static final int STANDALONE_MASK = 32768;
    public static final int SHORT = 1;
    public static final int LONG = 2;
    public static final int NARROW_FORMAT = 4;
    public static final int NARROW_STANDALONE = 32772;
    public static final int SHORT_FORMAT = 1;
    public static final int LONG_FORMAT = 2;
    public static final int SHORT_STANDALONE = 32769;
    public static final int LONG_STANDALONE = 32770;
    protected int[] fields;
    protected boolean[] isSet;
    private transient int[] stamp;
    protected long time;
    protected boolean isTimeSet;
    protected boolean areFieldsSet;
    transient boolean areAllFieldsSet;
    private boolean lenient;
    private TimeZone zone;
    private transient boolean sharedZone;
    private int firstDayOfWeek;
    private int minimalDaysInFirstWeek;
    private static final ConcurrentMap<Locale, int[]> cachedLocaleData;
    private static final int UNSET = 0;
    private static final int COMPUTED = 1;
    private static final int MINIMUM_USER_STAMP = 2;
    static final int ALL_FIELDS = 131071;
    private int nextStamp;
    static final int currentSerialVersion = 1;
    private int serialVersionOnStream;
    static final long serialVersionUID = -1807547505821590642L;
    static final int ERA_MASK = 1;
    static final int YEAR_MASK = 2;
    static final int MONTH_MASK = 4;
    static final int WEEK_OF_YEAR_MASK = 8;
    static final int WEEK_OF_MONTH_MASK = 16;
    static final int DAY_OF_MONTH_MASK = 32;
    static final int DATE_MASK = 32;
    static final int DAY_OF_YEAR_MASK = 64;
    static final int DAY_OF_WEEK_MASK = 128;
    static final int DAY_OF_WEEK_IN_MONTH_MASK = 256;
    static final int AM_PM_MASK = 512;
    static final int HOUR_MASK = 1024;
    static final int HOUR_OF_DAY_MASK = 2048;
    static final int MINUTE_MASK = 4096;
    static final int SECOND_MASK = 8192;
    static final int MILLISECOND_MASK = 16384;
    static final int ZONE_OFFSET_MASK = 32768;
    static final int DST_OFFSET_MASK = 65536;
    private static final String[] FIELD_NAME;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract void computeTime();

    protected abstract void computeFields();

    public abstract void add(int i2, int i3);

    public abstract void roll(int i2, boolean z2);

    public abstract int getMinimum(int i2);

    public abstract int getMaximum(int i2);

    public abstract int getGreatestMinimum(int i2);

    public abstract int getLeastMaximum(int i2);

    static {
        $assertionsDisabled = !Calendar.class.desiredAssertionStatus();
        cachedLocaleData = new ConcurrentHashMap(3);
        FIELD_NAME = new String[]{"ERA", "YEAR", "MONTH", "WEEK_OF_YEAR", "WEEK_OF_MONTH", "DAY_OF_MONTH", "DAY_OF_YEAR", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "AM_PM", "HOUR", "HOUR_OF_DAY", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET"};
    }

    /* loaded from: rt.jar:java/util/Calendar$Builder.class */
    public static class Builder {
        private static final int NFIELDS = 18;
        private static final int WEEK_YEAR = 17;
        private long instant;
        private int[] fields;
        private int nextStamp;
        private int maxFieldIndex;
        private String type;
        private TimeZone zone;
        private boolean lenient = true;
        private Locale locale;
        private int firstDayOfWeek;
        private int minimalDaysInFirstWeek;

        public Builder setInstant(long j2) {
            if (this.fields != null) {
                throw new IllegalStateException();
            }
            this.instant = j2;
            this.nextStamp = 1;
            return this;
        }

        public Builder setInstant(Date date) {
            return setInstant(date.getTime());
        }

        public Builder set(int i2, int i3) {
            if (i2 < 0 || i2 >= 17) {
                throw new IllegalArgumentException("field is invalid");
            }
            if (isInstantSet()) {
                throw new IllegalStateException("instant has been set");
            }
            allocateFields();
            internalSet(i2, i3);
            return this;
        }

        public Builder setFields(int... iArr) {
            int length = iArr.length;
            if (length % 2 != 0) {
                throw new IllegalArgumentException();
            }
            if (isInstantSet()) {
                throw new IllegalStateException("instant has been set");
            }
            if (this.nextStamp + (length / 2) < 0) {
                throw new IllegalStateException("stamp counter overflow");
            }
            allocateFields();
            int i2 = 0;
            while (i2 < length) {
                int i3 = i2;
                int i4 = i2 + 1;
                int i5 = iArr[i3];
                if (i5 < 0 || i5 >= 17) {
                    throw new IllegalArgumentException("field is invalid");
                }
                i2 = i4 + 1;
                internalSet(i5, iArr[i4]);
            }
            return this;
        }

        public Builder setDate(int i2, int i3, int i4) {
            return setFields(1, i2, 2, i3, 5, i4);
        }

        public Builder setTimeOfDay(int i2, int i3, int i4) {
            return setTimeOfDay(i2, i3, i4, 0);
        }

        public Builder setTimeOfDay(int i2, int i3, int i4, int i5) {
            return setFields(11, i2, 12, i3, 13, i4, 14, i5);
        }

        public Builder setWeekDate(int i2, int i3, int i4) {
            allocateFields();
            internalSet(17, i2);
            internalSet(3, i3);
            internalSet(7, i4);
            return this;
        }

        public Builder setTimeZone(TimeZone timeZone) {
            if (timeZone == null) {
                throw new NullPointerException();
            }
            this.zone = timeZone;
            return this;
        }

        public Builder setLenient(boolean z2) {
            this.lenient = z2;
            return this;
        }

        public Builder setCalendarType(String str) {
            if (str.equals("gregorian")) {
                str = "gregory";
            }
            if (!Calendar.getAvailableCalendarTypes().contains(str) && !str.equals("iso8601")) {
                throw new IllegalArgumentException("unknown calendar type: " + str);
            }
            if (this.type == null) {
                this.type = str;
            } else if (!this.type.equals(str)) {
                throw new IllegalStateException("calendar type override");
            }
            return this;
        }

        public Builder setLocale(Locale locale) {
            if (locale == null) {
                throw new NullPointerException();
            }
            this.locale = locale;
            return this;
        }

        public Builder setWeekDefinition(int i2, int i3) {
            if (!isValidWeekParameter(i2) || !isValidWeekParameter(i3)) {
                throw new IllegalArgumentException();
            }
            this.firstDayOfWeek = i2;
            this.minimalDaysInFirstWeek = i3;
            return this;
        }

        public Calendar build() {
            Calendar japaneseImperialCalendar;
            if (this.locale == null) {
                this.locale = Locale.getDefault();
            }
            if (this.zone == null) {
                this.zone = TimeZone.getDefault();
            }
            if (this.type == null) {
                this.type = this.locale.getUnicodeLocaleType("ca");
            }
            if (this.type == null) {
                if (this.locale.getCountry() == "TH" && this.locale.getLanguage() == "th") {
                    this.type = "buddhist";
                } else {
                    this.type = "gregory";
                }
            }
            switch (this.type) {
                case "gregory":
                    japaneseImperialCalendar = new GregorianCalendar(this.zone, this.locale, true);
                    break;
                case "iso8601":
                    GregorianCalendar gregorianCalendar = new GregorianCalendar(this.zone, this.locale, true);
                    gregorianCalendar.setGregorianChange(new Date(Long.MIN_VALUE));
                    setWeekDefinition(2, 4);
                    japaneseImperialCalendar = gregorianCalendar;
                    break;
                case "buddhist":
                    japaneseImperialCalendar = new BuddhistCalendar(this.zone, this.locale);
                    japaneseImperialCalendar.clear();
                    break;
                case "japanese":
                    japaneseImperialCalendar = new JapaneseImperialCalendar(this.zone, this.locale, true);
                    break;
                default:
                    throw new IllegalArgumentException("unknown calendar type: " + this.type);
            }
            japaneseImperialCalendar.setLenient(this.lenient);
            if (this.firstDayOfWeek != 0) {
                japaneseImperialCalendar.setFirstDayOfWeek(this.firstDayOfWeek);
                japaneseImperialCalendar.setMinimalDaysInFirstWeek(this.minimalDaysInFirstWeek);
            }
            if (isInstantSet()) {
                japaneseImperialCalendar.setTimeInMillis(this.instant);
                japaneseImperialCalendar.complete();
                return japaneseImperialCalendar;
            }
            if (this.fields != null) {
                boolean z2 = isSet(17) && this.fields[17] > this.fields[1];
                if (z2 && !japaneseImperialCalendar.isWeekDateSupported()) {
                    throw new IllegalArgumentException("week date is unsupported by " + this.type);
                }
                for (int i2 = 2; i2 < this.nextStamp; i2++) {
                    int i3 = 0;
                    while (true) {
                        if (i3 > this.maxFieldIndex) {
                            break;
                        }
                        if (this.fields[i3] != i2) {
                            i3++;
                        } else {
                            japaneseImperialCalendar.set(i3, this.fields[18 + i3]);
                        }
                    }
                }
                if (z2) {
                    japaneseImperialCalendar.setWeekDate(this.fields[35], isSet(3) ? this.fields[21] : 1, isSet(7) ? this.fields[25] : japaneseImperialCalendar.getFirstDayOfWeek());
                }
                japaneseImperialCalendar.complete();
            }
            return japaneseImperialCalendar;
        }

        private void allocateFields() {
            if (this.fields == null) {
                this.fields = new int[36];
                this.nextStamp = 2;
                this.maxFieldIndex = -1;
            }
        }

        private void internalSet(int i2, int i3) {
            int[] iArr = this.fields;
            int i4 = this.nextStamp;
            this.nextStamp = i4 + 1;
            iArr[i2] = i4;
            if (this.nextStamp < 0) {
                throw new IllegalStateException("stamp counter overflow");
            }
            this.fields[18 + i2] = i3;
            if (i2 > this.maxFieldIndex && i2 < 17) {
                this.maxFieldIndex = i2;
            }
        }

        private boolean isInstantSet() {
            return this.nextStamp == 1;
        }

        private boolean isSet(int i2) {
            return this.fields != null && this.fields[i2] > 0;
        }

        private boolean isValidWeekParameter(int i2) {
            return i2 > 0 && i2 <= 7;
        }
    }

    protected Calendar() {
        this(TimeZone.getDefaultRef(), Locale.getDefault(Locale.Category.FORMAT));
        this.sharedZone = true;
    }

    protected Calendar(TimeZone timeZone, Locale locale) {
        this.lenient = true;
        this.sharedZone = false;
        this.nextStamp = 2;
        this.serialVersionOnStream = 1;
        this.fields = new int[17];
        this.isSet = new boolean[17];
        this.stamp = new int[17];
        this.zone = timeZone;
        setWeekCountData(locale);
    }

    public static Calendar getInstance() {
        return createCalendar(TimeZone.getDefault(), Locale.getDefault(Locale.Category.FORMAT));
    }

    public static Calendar getInstance(TimeZone timeZone) {
        return createCalendar(timeZone, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static Calendar getInstance(Locale locale) {
        return createCalendar(TimeZone.getDefault(), locale);
    }

    public static Calendar getInstance(TimeZone timeZone, Locale locale) {
        return createCalendar(timeZone, locale);
    }

    private static Calendar createCalendar(TimeZone timeZone, Locale locale) {
        String unicodeLocaleType;
        CalendarProvider calendarProvider = LocaleProviderAdapter.getAdapter(CalendarProvider.class, locale).getCalendarProvider();
        if (calendarProvider != null) {
            try {
                return calendarProvider.getInstance(timeZone, locale);
            } catch (IllegalArgumentException e2) {
            }
        }
        Calendar gregorianCalendar = null;
        if (locale.hasExtensions() && (unicodeLocaleType = locale.getUnicodeLocaleType("ca")) != null) {
            switch (unicodeLocaleType) {
                case "buddhist":
                    gregorianCalendar = new BuddhistCalendar(timeZone, locale);
                    break;
                case "japanese":
                    gregorianCalendar = new JapaneseImperialCalendar(timeZone, locale);
                    break;
                case "gregory":
                    gregorianCalendar = new GregorianCalendar(timeZone, locale);
                    break;
            }
        }
        if (gregorianCalendar == null) {
            if (locale.getLanguage() == "th" && locale.getCountry() == "TH") {
                gregorianCalendar = new BuddhistCalendar(timeZone, locale);
            } else if (locale.getVariant() == "JP" && locale.getLanguage() == "ja" && locale.getCountry() == "JP") {
                gregorianCalendar = new JapaneseImperialCalendar(timeZone, locale);
            } else {
                gregorianCalendar = new GregorianCalendar(timeZone, locale);
            }
        }
        return gregorianCalendar;
    }

    public static synchronized Locale[] getAvailableLocales() {
        return DateFormat.getAvailableLocales();
    }

    public final Date getTime() {
        return new Date(getTimeInMillis());
    }

    public final void setTime(Date date) {
        setTimeInMillis(date.getTime());
    }

    public long getTimeInMillis() {
        if (!this.isTimeSet) {
            updateTime();
        }
        return this.time;
    }

    public void setTimeInMillis(long j2) {
        if (this.time == j2 && this.isTimeSet && this.areFieldsSet && this.areAllFieldsSet && (this.zone instanceof ZoneInfo) && !((ZoneInfo) this.zone).isDirty()) {
            return;
        }
        this.time = j2;
        this.isTimeSet = true;
        this.areFieldsSet = false;
        computeFields();
        this.areFieldsSet = true;
        this.areAllFieldsSet = true;
    }

    public int get(int i2) {
        complete();
        return internalGet(i2);
    }

    protected final int internalGet(int i2) {
        return this.fields[i2];
    }

    final void internalSet(int i2, int i3) {
        this.fields[i2] = i3;
    }

    public void set(int i2, int i3) {
        if (this.areFieldsSet && !this.areAllFieldsSet) {
            computeFields();
        }
        internalSet(i2, i3);
        this.isTimeSet = false;
        this.areFieldsSet = false;
        this.isSet[i2] = true;
        int[] iArr = this.stamp;
        int i4 = this.nextStamp;
        this.nextStamp = i4 + 1;
        iArr[i2] = i4;
        if (this.nextStamp == Integer.MAX_VALUE) {
            adjustStamp();
        }
    }

    public final void set(int i2, int i3, int i4) {
        set(1, i2);
        set(2, i3);
        set(5, i4);
    }

    public final void set(int i2, int i3, int i4, int i5, int i6) {
        set(1, i2);
        set(2, i3);
        set(5, i4);
        set(11, i5);
        set(12, i6);
    }

    public final void set(int i2, int i3, int i4, int i5, int i6, int i7) {
        set(1, i2);
        set(2, i3);
        set(5, i4);
        set(11, i5);
        set(12, i6);
        set(13, i7);
    }

    public final void clear() {
        int i2 = 0;
        while (i2 < this.fields.length) {
            this.fields[i2] = 0;
            this.stamp[i2] = 0;
            int i3 = i2;
            i2++;
            this.isSet[i3] = false;
        }
        this.areFieldsSet = false;
        this.areAllFieldsSet = false;
        this.isTimeSet = false;
    }

    public final void clear(int i2) {
        this.fields[i2] = 0;
        this.stamp[i2] = 0;
        this.isSet[i2] = false;
        this.areFieldsSet = false;
        this.areAllFieldsSet = false;
        this.isTimeSet = false;
    }

    public final boolean isSet(int i2) {
        return this.stamp[i2] != 0;
    }

    public String getDisplayName(int i2, int i3, Locale locale) {
        if (!checkDisplayNameParams(i2, i3, 1, 4, locale, 645)) {
            return null;
        }
        String calendarType = getCalendarType();
        int i4 = get(i2);
        if (isStandaloneStyle(i3) || isNarrowFormatStyle(i3)) {
            String strRetrieveFieldValueName = CalendarDataUtility.retrieveFieldValueName(calendarType, i2, i4, i3, locale);
            if (strRetrieveFieldValueName == null) {
                if (isNarrowFormatStyle(i3)) {
                    strRetrieveFieldValueName = CalendarDataUtility.retrieveFieldValueName(calendarType, i2, i4, toStandaloneStyle(i3), locale);
                } else if (isStandaloneStyle(i3)) {
                    strRetrieveFieldValueName = CalendarDataUtility.retrieveFieldValueName(calendarType, i2, i4, getBaseStyle(i3), locale);
                }
            }
            return strRetrieveFieldValueName;
        }
        String[] fieldStrings = getFieldStrings(i2, i3, DateFormatSymbols.getInstance(locale));
        if (fieldStrings != null && i4 < fieldStrings.length) {
            return fieldStrings[i4];
        }
        return null;
    }

    public Map<String, Integer> getDisplayNames(int i2, int i3, Locale locale) {
        if (!checkDisplayNameParams(i2, i3, 0, 4, locale, 645)) {
            return null;
        }
        String calendarType = getCalendarType();
        if (i3 == 0 || isStandaloneStyle(i3) || isNarrowFormatStyle(i3)) {
            Map<String, Integer> mapRetrieveFieldValueNames = CalendarDataUtility.retrieveFieldValueNames(calendarType, i2, i3, locale);
            if (mapRetrieveFieldValueNames == null) {
                if (isNarrowFormatStyle(i3)) {
                    mapRetrieveFieldValueNames = CalendarDataUtility.retrieveFieldValueNames(calendarType, i2, toStandaloneStyle(i3), locale);
                } else if (i3 != 0) {
                    mapRetrieveFieldValueNames = CalendarDataUtility.retrieveFieldValueNames(calendarType, i2, getBaseStyle(i3), locale);
                }
            }
            return mapRetrieveFieldValueNames;
        }
        return getDisplayNamesImpl(i2, i3, locale);
    }

    private Map<String, Integer> getDisplayNamesImpl(int i2, int i3, Locale locale) {
        String[] fieldStrings = getFieldStrings(i2, i3, DateFormatSymbols.getInstance(locale));
        if (fieldStrings != null) {
            HashMap map = new HashMap();
            for (int i4 = 0; i4 < fieldStrings.length; i4++) {
                if (fieldStrings[i4].length() != 0) {
                    map.put(fieldStrings[i4], Integer.valueOf(i4));
                }
            }
            return map;
        }
        return null;
    }

    boolean checkDisplayNameParams(int i2, int i3, int i4, int i5, Locale locale, int i6) {
        int baseStyle = getBaseStyle(i3);
        if (i2 < 0 || i2 >= this.fields.length || baseStyle < i4 || baseStyle > i5) {
            throw new IllegalArgumentException();
        }
        if (locale == null) {
            throw new NullPointerException();
        }
        return isFieldSet(i6, i2);
    }

    private String[] getFieldStrings(int i2, int i3, DateFormatSymbols dateFormatSymbols) {
        int baseStyle = getBaseStyle(i3);
        if (baseStyle == 4) {
            return null;
        }
        String[] amPmStrings = null;
        switch (i2) {
            case 0:
                amPmStrings = dateFormatSymbols.getEras();
                break;
            case 2:
                amPmStrings = baseStyle == 2 ? dateFormatSymbols.getMonths() : dateFormatSymbols.getShortMonths();
                break;
            case 7:
                amPmStrings = baseStyle == 2 ? dateFormatSymbols.getWeekdays() : dateFormatSymbols.getShortWeekdays();
                break;
            case 9:
                amPmStrings = dateFormatSymbols.getAmPmStrings();
                break;
        }
        return amPmStrings;
    }

    protected void complete() {
        if (!this.isTimeSet) {
            updateTime();
        }
        if (!this.areFieldsSet || !this.areAllFieldsSet) {
            computeFields();
            this.areFieldsSet = true;
            this.areAllFieldsSet = true;
        }
    }

    final boolean isExternallySet(int i2) {
        return this.stamp[i2] >= 2;
    }

    final int getSetStateFields() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.fields.length; i3++) {
            if (this.stamp[i3] != 0) {
                i2 |= 1 << i3;
            }
        }
        return i2;
    }

    final void setFieldsComputed(int i2) {
        if (i2 == ALL_FIELDS) {
            for (int i3 = 0; i3 < this.fields.length; i3++) {
                this.stamp[i3] = 1;
                this.isSet[i3] = true;
            }
            this.areAllFieldsSet = true;
            this.areFieldsSet = true;
            return;
        }
        for (int i4 = 0; i4 < this.fields.length; i4++) {
            if ((i2 & 1) == 1) {
                this.stamp[i4] = 1;
                this.isSet[i4] = true;
            } else if (this.areAllFieldsSet && !this.isSet[i4]) {
                this.areAllFieldsSet = false;
            }
            i2 >>>= 1;
        }
    }

    final void setFieldsNormalized(int i2) {
        if (i2 != ALL_FIELDS) {
            for (int i3 = 0; i3 < this.fields.length; i3++) {
                if ((i2 & 1) == 0) {
                    this.fields[i3] = 0;
                    this.stamp[i3] = 0;
                    this.isSet[i3] = false;
                }
                i2 >>= 1;
            }
        }
        this.areFieldsSet = true;
        this.areAllFieldsSet = false;
    }

    final boolean isPartiallyNormalized() {
        return this.areFieldsSet && !this.areAllFieldsSet;
    }

    final boolean isFullyNormalized() {
        return this.areFieldsSet && this.areAllFieldsSet;
    }

    final void setUnnormalized() {
        this.areAllFieldsSet = false;
        this.areFieldsSet = false;
    }

    static boolean isFieldSet(int i2, int i3) {
        return (i2 & (1 << i3)) != 0;
    }

    final int selectFields() {
        int i2;
        int i3 = 2;
        if (this.stamp[0] != 0) {
            i3 = 2 | 1;
        }
        int i4 = this.stamp[7];
        int i5 = this.stamp[2];
        int i6 = this.stamp[5];
        int iAggregateStamp = aggregateStamp(this.stamp[4], i4);
        int iAggregateStamp2 = aggregateStamp(this.stamp[8], i4);
        int i7 = this.stamp[6];
        int iAggregateStamp3 = aggregateStamp(this.stamp[3], i4);
        int iMax = i6;
        if (iAggregateStamp > iMax) {
            iMax = iAggregateStamp;
        }
        if (iAggregateStamp2 > iMax) {
            iMax = iAggregateStamp2;
        }
        if (i7 > iMax) {
            iMax = i7;
        }
        if (iAggregateStamp3 > iMax) {
            iMax = iAggregateStamp3;
        }
        if (iMax == 0) {
            iAggregateStamp = this.stamp[4];
            iAggregateStamp2 = Math.max(this.stamp[8], i4);
            iAggregateStamp3 = this.stamp[3];
            iMax = Math.max(Math.max(iAggregateStamp, iAggregateStamp2), iAggregateStamp3);
            if (iMax == 0) {
                i6 = i5;
                iMax = i5;
            }
        }
        if (iMax == i6 || ((iMax == iAggregateStamp && this.stamp[4] >= this.stamp[3]) || (iMax == iAggregateStamp2 && this.stamp[8] >= this.stamp[3]))) {
            i2 = i3 | 4;
            if (iMax == i6) {
                i2 |= 32;
            } else {
                if (!$assertionsDisabled && iMax != iAggregateStamp && iMax != iAggregateStamp2) {
                    throw new AssertionError();
                }
                if (i4 != 0) {
                    i2 |= 128;
                }
                if (iAggregateStamp == iAggregateStamp2) {
                    if (this.stamp[4] >= this.stamp[8]) {
                        i2 |= 16;
                    } else {
                        i2 |= 256;
                    }
                } else if (iMax == iAggregateStamp) {
                    i2 |= 16;
                } else {
                    if (!$assertionsDisabled && iMax != iAggregateStamp2) {
                        throw new AssertionError();
                    }
                    if (this.stamp[8] != 0) {
                        i2 |= 256;
                    }
                }
            }
        } else {
            if (!$assertionsDisabled && iMax != i7 && iMax != iAggregateStamp3 && iMax != 0) {
                throw new AssertionError();
            }
            if (iMax == i7) {
                i2 = i3 | 64;
            } else {
                if (!$assertionsDisabled && iMax != iAggregateStamp3) {
                    throw new AssertionError();
                }
                if (i4 != 0) {
                    i3 |= 128;
                }
                i2 = i3 | 8;
            }
        }
        int i8 = this.stamp[11];
        int iAggregateStamp4 = aggregateStamp(this.stamp[10], this.stamp[9]);
        int iMax2 = iAggregateStamp4 > i8 ? iAggregateStamp4 : i8;
        if (iMax2 == 0) {
            iMax2 = Math.max(this.stamp[10], this.stamp[9]);
        }
        if (iMax2 != 0) {
            if (iMax2 == i8) {
                i2 |= 2048;
            } else {
                i2 |= 1024;
                if (this.stamp[9] != 0) {
                    i2 |= 512;
                }
            }
        }
        if (this.stamp[12] != 0) {
            i2 |= 4096;
        }
        if (this.stamp[13] != 0) {
            i2 |= 8192;
        }
        if (this.stamp[14] != 0) {
            i2 |= 16384;
        }
        if (this.stamp[15] >= 2) {
            i2 |= 32768;
        }
        if (this.stamp[16] >= 2) {
            i2 |= 65536;
        }
        return i2;
    }

    int getBaseStyle(int i2) {
        return i2 & (-32769);
    }

    private int toStandaloneStyle(int i2) {
        return i2 | 32768;
    }

    private boolean isStandaloneStyle(int i2) {
        return (i2 & 32768) != 0;
    }

    private boolean isNarrowStyle(int i2) {
        return i2 == 4 || i2 == 32772;
    }

    private boolean isNarrowFormatStyle(int i2) {
        return i2 == 4;
    }

    private static int aggregateStamp(int i2, int i3) {
        if (i2 == 0 || i3 == 0) {
            return 0;
        }
        return i2 > i3 ? i2 : i3;
    }

    public static Set<String> getAvailableCalendarTypes() {
        return AvailableCalendarTypes.SET;
    }

    /* loaded from: rt.jar:java/util/Calendar$AvailableCalendarTypes.class */
    private static class AvailableCalendarTypes {
        private static final Set<String> SET;

        static {
            HashSet hashSet = new HashSet(3);
            hashSet.add("gregory");
            hashSet.add("buddhist");
            hashSet.add("japanese");
            SET = Collections.unmodifiableSet(hashSet);
        }

        private AvailableCalendarTypes() {
        }
    }

    public String getCalendarType() {
        return getClass().getName();
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005e, code lost:
    
        if (r4.zone.equals(r0.getTimeZone()) != false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r4
            r1 = r5
            if (r0 != r1) goto L7
            r0 = 1
            return r0
        L7:
            r0 = r5
            java.util.Calendar r0 = (java.util.Calendar) r0     // Catch: java.lang.Exception -> L67
            r6 = r0
            r0 = r4
            r1 = r6
            long r1 = getMillisOf(r1)     // Catch: java.lang.Exception -> L67
            int r0 = r0.compareTo(r1)     // Catch: java.lang.Exception -> L67
            if (r0 != 0) goto L65
            r0 = r4
            boolean r0 = r0.lenient     // Catch: java.lang.Exception -> L67
            r1 = r6
            boolean r1 = r1.lenient     // Catch: java.lang.Exception -> L67
            if (r0 != r1) goto L65
            r0 = r4
            int r0 = r0.firstDayOfWeek     // Catch: java.lang.Exception -> L67
            r1 = r6
            int r1 = r1.firstDayOfWeek     // Catch: java.lang.Exception -> L67
            if (r0 != r1) goto L65
            r0 = r4
            int r0 = r0.minimalDaysInFirstWeek     // Catch: java.lang.Exception -> L67
            r1 = r6
            int r1 = r1.minimalDaysInFirstWeek     // Catch: java.lang.Exception -> L67
            if (r0 != r1) goto L65
            r0 = r4
            java.util.TimeZone r0 = r0.zone     // Catch: java.lang.Exception -> L67
            boolean r0 = r0 instanceof sun.util.calendar.ZoneInfo     // Catch: java.lang.Exception -> L67
            if (r0 == 0) goto L53
            r0 = r4
            java.util.TimeZone r0 = r0.zone     // Catch: java.lang.Exception -> L67
            r1 = r6
            java.util.TimeZone r1 = r1.zone     // Catch: java.lang.Exception -> L67
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> L67
            if (r0 == 0) goto L65
            goto L61
        L53:
            r0 = r4
            java.util.TimeZone r0 = r0.zone     // Catch: java.lang.Exception -> L67
            r1 = r6
            java.util.TimeZone r1 = r1.getTimeZone()     // Catch: java.lang.Exception -> L67
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> L67
            if (r0 == 0) goto L65
        L61:
            r0 = 1
            goto L66
        L65:
            r0 = 0
        L66:
            return r0
        L67:
            r6 = move-exception
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Calendar.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int iHashCode = (this.lenient ? 1 : 0) | (this.firstDayOfWeek << 1) | (this.minimalDaysInFirstWeek << 4) | (this.zone.hashCode() << 7);
        long millisOf = getMillisOf(this);
        return (((int) millisOf) ^ ((int) (millisOf >> 32))) ^ iHashCode;
    }

    public boolean before(Object obj) {
        return (obj instanceof Calendar) && compareTo((Calendar) obj) < 0;
    }

    public boolean after(Object obj) {
        return (obj instanceof Calendar) && compareTo((Calendar) obj) > 0;
    }

    @Override // java.lang.Comparable
    public int compareTo(Calendar calendar) {
        return compareTo(getMillisOf(calendar));
    }

    public void roll(int i2, int i3) {
        while (i3 > 0) {
            roll(i2, true);
            i3--;
        }
        while (i3 < 0) {
            roll(i2, false);
            i3++;
        }
    }

    public void setTimeZone(TimeZone timeZone) {
        this.zone = timeZone;
        this.sharedZone = false;
        this.areFieldsSet = false;
        this.areAllFieldsSet = false;
    }

    public TimeZone getTimeZone() {
        if (this.sharedZone) {
            this.zone = (TimeZone) this.zone.clone();
            this.sharedZone = false;
        }
        return this.zone;
    }

    TimeZone getZone() {
        return this.zone;
    }

    void setZoneShared(boolean z2) {
        this.sharedZone = z2;
    }

    public void setLenient(boolean z2) {
        this.lenient = z2;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void setFirstDayOfWeek(int i2) {
        if (this.firstDayOfWeek == i2) {
            return;
        }
        this.firstDayOfWeek = i2;
        invalidateWeekFields();
    }

    public int getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public void setMinimalDaysInFirstWeek(int i2) {
        if (this.minimalDaysInFirstWeek == i2) {
            return;
        }
        this.minimalDaysInFirstWeek = i2;
        invalidateWeekFields();
    }

    public int getMinimalDaysInFirstWeek() {
        return this.minimalDaysInFirstWeek;
    }

    public boolean isWeekDateSupported() {
        return false;
    }

    public int getWeekYear() {
        throw new UnsupportedOperationException();
    }

    public void setWeekDate(int i2, int i3, int i4) {
        throw new UnsupportedOperationException();
    }

    public int getWeeksInWeekYear() {
        throw new UnsupportedOperationException();
    }

    public int getActualMinimum(int i2) {
        int greatestMinimum = getGreatestMinimum(i2);
        int minimum = getMinimum(i2);
        if (greatestMinimum == minimum) {
            return greatestMinimum;
        }
        Calendar calendar = (Calendar) clone();
        calendar.setLenient(true);
        int i3 = greatestMinimum;
        do {
            calendar.set(i2, greatestMinimum);
            if (calendar.get(i2) != greatestMinimum) {
                break;
            }
            i3 = greatestMinimum;
            greatestMinimum--;
        } while (greatestMinimum >= minimum);
        return i3;
    }

    public int getActualMaximum(int i2) {
        int leastMaximum = getLeastMaximum(i2);
        int maximum = getMaximum(i2);
        if (leastMaximum == maximum) {
            return leastMaximum;
        }
        Calendar calendar = (Calendar) clone();
        calendar.setLenient(true);
        if (i2 == 3 || i2 == 4) {
            calendar.set(7, this.firstDayOfWeek);
        }
        int i3 = leastMaximum;
        do {
            calendar.set(i2, leastMaximum);
            if (calendar.get(i2) != leastMaximum) {
                break;
            }
            i3 = leastMaximum;
            leastMaximum++;
        } while (leastMaximum <= maximum);
        return i3;
    }

    public Object clone() {
        try {
            Calendar calendar = (Calendar) super.clone();
            calendar.fields = new int[17];
            calendar.isSet = new boolean[17];
            calendar.stamp = new int[17];
            for (int i2 = 0; i2 < 17; i2++) {
                calendar.fields[i2] = this.fields[i2];
                calendar.stamp[i2] = this.stamp[i2];
                calendar.isSet[i2] = this.isSet[i2];
            }
            calendar.zone = (TimeZone) this.zone.clone();
            return calendar;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    static String getFieldName(int i2) {
        return FIELD_NAME[i2];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(800);
        sb.append(getClass().getName()).append('[');
        appendValue(sb, SchemaSymbols.ATTVAL_TIME, this.isTimeSet, this.time);
        sb.append(",areFieldsSet=").append(this.areFieldsSet);
        sb.append(",areAllFieldsSet=").append(this.areAllFieldsSet);
        sb.append(",lenient=").append(this.lenient);
        sb.append(",zone=").append((Object) this.zone);
        appendValue(sb, ",firstDayOfWeek", true, this.firstDayOfWeek);
        appendValue(sb, ",minimalDaysInFirstWeek", true, this.minimalDaysInFirstWeek);
        for (int i2 = 0; i2 < 17; i2++) {
            sb.append(',');
            appendValue(sb, FIELD_NAME[i2], isSet(i2), this.fields[i2]);
        }
        sb.append(']');
        return sb.toString();
    }

    private static void appendValue(StringBuilder sb, String str, boolean z2, long j2) {
        sb.append(str).append('=');
        if (z2) {
            sb.append(j2);
        } else {
            sb.append('?');
        }
    }

    private void setWeekCountData(Locale locale) {
        int[] iArr = cachedLocaleData.get(locale);
        if (iArr == null) {
            iArr = new int[]{CalendarDataUtility.retrieveFirstDayOfWeek(locale), CalendarDataUtility.retrieveMinimalDaysInFirstWeek(locale)};
            cachedLocaleData.putIfAbsent(locale, iArr);
        }
        this.firstDayOfWeek = iArr[0];
        this.minimalDaysInFirstWeek = iArr[1];
    }

    private void updateTime() {
        computeTime();
        this.isTimeSet = true;
    }

    private int compareTo(long j2) {
        long millisOf = getMillisOf(this);
        if (millisOf > j2) {
            return 1;
        }
        return millisOf == j2 ? 0 : -1;
    }

    private static long getMillisOf(Calendar calendar) {
        if (calendar.isTimeSet) {
            return calendar.time;
        }
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.setLenient(true);
        return calendar2.getTimeInMillis();
    }

    private void adjustStamp() {
        int i2;
        int i3 = 2;
        int i4 = 2;
        do {
            i2 = Integer.MAX_VALUE;
            for (int i5 = 0; i5 < this.stamp.length; i5++) {
                int i6 = this.stamp[i5];
                if (i6 >= i4 && i2 > i6) {
                    i2 = i6;
                }
                if (i3 < i6) {
                    i3 = i6;
                }
            }
            if (i3 != i2 && i2 == Integer.MAX_VALUE) {
                break;
            }
            for (int i7 = 0; i7 < this.stamp.length; i7++) {
                if (this.stamp[i7] == i2) {
                    this.stamp[i7] = i4;
                }
            }
            i4++;
        } while (i2 != i3);
        this.nextStamp = i4;
    }

    private void invalidateWeekFields() {
        int i2;
        int i3;
        if (this.stamp[4] != 1 && this.stamp[3] != 1) {
            return;
        }
        Calendar calendar = (Calendar) clone();
        calendar.setLenient(true);
        calendar.clear(4);
        calendar.clear(3);
        if (this.stamp[4] == 1 && this.fields[4] != (i3 = calendar.get(4))) {
            this.fields[4] = i3;
        }
        if (this.stamp[3] == 1 && this.fields[3] != (i2 = calendar.get(3))) {
            this.fields[3] = i2;
        }
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (!this.isTimeSet) {
            try {
                updateTime();
            } catch (IllegalArgumentException e2) {
            }
        }
        TimeZone timeZone = null;
        if (this.zone instanceof ZoneInfo) {
            SimpleTimeZone lastRuleInstance = ((ZoneInfo) this.zone).getLastRuleInstance();
            if (lastRuleInstance == null) {
                lastRuleInstance = new SimpleTimeZone(this.zone.getRawOffset(), this.zone.getID());
            }
            timeZone = this.zone;
            this.zone = lastRuleInstance;
        }
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(timeZone);
        if (timeZone != null) {
            this.zone = timeZone;
        }
    }

    /* loaded from: rt.jar:java/util/Calendar$CalendarAccessControlContext.class */
    private static class CalendarAccessControlContext {
        private static final AccessControlContext INSTANCE;

        static {
            Permission runtimePermission = new RuntimePermission("accessClassInPackage.sun.util.calendar");
            PermissionCollection permissionCollectionNewPermissionCollection = runtimePermission.newPermissionCollection();
            permissionCollectionNewPermissionCollection.add(runtimePermission);
            INSTANCE = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissionCollectionNewPermissionCollection)});
        }

        private CalendarAccessControlContext() {
        }
    }

    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String id;
        TimeZone timeZone;
        objectInputStream.defaultReadObject();
        this.stamp = new int[17];
        if (this.serialVersionOnStream >= 2) {
            this.isTimeSet = true;
            if (this.fields == null) {
                this.fields = new int[17];
            }
            if (this.isSet == null) {
                this.isSet = new boolean[17];
            }
        } else if (this.serialVersionOnStream >= 0) {
            for (int i2 = 0; i2 < 17; i2++) {
                this.stamp[i2] = this.isSet[i2] ? 1 : 0;
            }
        }
        this.serialVersionOnStream = 1;
        ZoneInfo zoneInfo = null;
        try {
            zoneInfo = (ZoneInfo) AccessController.doPrivileged(new PrivilegedExceptionAction<ZoneInfo>() { // from class: java.util.Calendar.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public ZoneInfo run() throws Exception {
                    return (ZoneInfo) objectInputStream.readObject();
                }
            }, CalendarAccessControlContext.INSTANCE);
        } catch (PrivilegedActionException e2) {
            Exception exception = e2.getException();
            if (!(exception instanceof OptionalDataException)) {
                if (exception instanceof RuntimeException) {
                    throw ((RuntimeException) exception);
                }
                if (exception instanceof IOException) {
                    throw ((IOException) exception);
                }
                if (exception instanceof ClassNotFoundException) {
                    throw ((ClassNotFoundException) exception);
                }
                throw new RuntimeException(exception);
            }
        }
        if (zoneInfo != null) {
            this.zone = zoneInfo;
        }
        if ((this.zone instanceof SimpleTimeZone) && (timeZone = TimeZone.getTimeZone((id = this.zone.getID()))) != null && timeZone.hasSameRules(this.zone) && timeZone.getID().equals(id)) {
            this.zone = timeZone;
        }
    }

    public final Instant toInstant() {
        return Instant.ofEpochMilli(getTimeInMillis());
    }
}
