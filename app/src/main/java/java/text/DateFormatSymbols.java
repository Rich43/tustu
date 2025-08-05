package java.text;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleServiceProviderPool;
import sun.util.locale.provider.TimeZoneNameUtility;

/* loaded from: rt.jar:java/text/DateFormatSymbols.class */
public class DateFormatSymbols implements Serializable, Cloneable {
    static final String patternChars = "GyMdkHmsSEDFwWahKzZYuXL";
    static final int PATTERN_ERA = 0;
    static final int PATTERN_YEAR = 1;
    static final int PATTERN_MONTH = 2;
    static final int PATTERN_DAY_OF_MONTH = 3;
    static final int PATTERN_HOUR_OF_DAY1 = 4;
    static final int PATTERN_HOUR_OF_DAY0 = 5;
    static final int PATTERN_MINUTE = 6;
    static final int PATTERN_SECOND = 7;
    static final int PATTERN_MILLISECOND = 8;
    static final int PATTERN_DAY_OF_WEEK = 9;
    static final int PATTERN_DAY_OF_YEAR = 10;
    static final int PATTERN_DAY_OF_WEEK_IN_MONTH = 11;
    static final int PATTERN_WEEK_OF_YEAR = 12;
    static final int PATTERN_WEEK_OF_MONTH = 13;
    static final int PATTERN_AM_PM = 14;
    static final int PATTERN_HOUR1 = 15;
    static final int PATTERN_HOUR0 = 16;
    static final int PATTERN_ZONE_NAME = 17;
    static final int PATTERN_ZONE_VALUE = 18;
    static final int PATTERN_WEEK_YEAR = 19;
    static final int PATTERN_ISO_DAY_OF_WEEK = 20;
    static final int PATTERN_ISO_ZONE = 21;
    static final int PATTERN_MONTH_STANDALONE = 22;
    static final long serialVersionUID = -5987973545549424702L;
    static final int millisPerHour = 3600000;
    private static final ConcurrentMap<Locale, SoftReference<DateFormatSymbols>> cachedInstances = new ConcurrentHashMap(3);
    String[] eras = null;
    String[] months = null;
    String[] shortMonths = null;
    String[] weekdays = null;
    String[] shortWeekdays = null;
    String[] ampms = null;
    String[][] zoneStrings = (String[][]) null;
    transient boolean isZoneStringsSet = false;
    String localPatternChars = null;
    Locale locale = null;
    private transient int lastZoneIndex = 0;
    volatile transient int cachedHashCode = 0;

    public DateFormatSymbols() {
        initializeData(Locale.getDefault(Locale.Category.FORMAT));
    }

    public DateFormatSymbols(Locale locale) {
        initializeData(locale);
    }

    private DateFormatSymbols(boolean z2) {
    }

    public static Locale[] getAvailableLocales() {
        return LocaleServiceProviderPool.getPool(DateFormatSymbolsProvider.class).getAvailableLocales();
    }

    public static final DateFormatSymbols getInstance() {
        return getInstance(Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormatSymbols getInstance(Locale locale) {
        DateFormatSymbols providerInstance = getProviderInstance(locale);
        if (providerInstance != null) {
            return providerInstance;
        }
        throw new RuntimeException("DateFormatSymbols instance creation failed.");
    }

    static final DateFormatSymbols getInstanceRef(Locale locale) {
        DateFormatSymbols providerInstance = getProviderInstance(locale);
        if (providerInstance != null) {
            return providerInstance;
        }
        throw new RuntimeException("DateFormatSymbols instance creation failed.");
    }

    private static DateFormatSymbols getProviderInstance(Locale locale) {
        DateFormatSymbols dateFormatSymbolsProvider = LocaleProviderAdapter.getAdapter(DateFormatSymbolsProvider.class, locale).getDateFormatSymbolsProvider().getInstance(locale);
        if (dateFormatSymbolsProvider == null) {
            dateFormatSymbolsProvider = LocaleProviderAdapter.forJRE().getDateFormatSymbolsProvider().getInstance(locale);
        }
        return dateFormatSymbolsProvider;
    }

    public String[] getEras() {
        return (String[]) Arrays.copyOf(this.eras, this.eras.length);
    }

    public void setEras(String[] strArr) {
        this.eras = (String[]) Arrays.copyOf(strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getMonths() {
        return (String[]) Arrays.copyOf(this.months, this.months.length);
    }

    public void setMonths(String[] strArr) {
        this.months = (String[]) Arrays.copyOf(strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getShortMonths() {
        return (String[]) Arrays.copyOf(this.shortMonths, this.shortMonths.length);
    }

    public void setShortMonths(String[] strArr) {
        this.shortMonths = (String[]) Arrays.copyOf(strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getWeekdays() {
        return (String[]) Arrays.copyOf(this.weekdays, this.weekdays.length);
    }

    public void setWeekdays(String[] strArr) {
        this.weekdays = (String[]) Arrays.copyOf(strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getShortWeekdays() {
        return (String[]) Arrays.copyOf(this.shortWeekdays, this.shortWeekdays.length);
    }

    public void setShortWeekdays(String[] strArr) {
        this.shortWeekdays = (String[]) Arrays.copyOf(strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[] getAmPmStrings() {
        return (String[]) Arrays.copyOf(this.ampms, this.ampms.length);
    }

    public void setAmPmStrings(String[] strArr) {
        this.ampms = (String[]) Arrays.copyOf(strArr, strArr.length);
        this.cachedHashCode = 0;
    }

    public String[][] getZoneStrings() {
        return getZoneStringsImpl(true);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.String[], java.lang.String[][]] */
    public void setZoneStrings(String[][] strArr) {
        ?? r0 = new String[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            int length = strArr[i2].length;
            if (length < 5) {
                throw new IllegalArgumentException();
            }
            r0[i2] = (String[]) Arrays.copyOf(strArr[i2], length);
        }
        this.zoneStrings = r0;
        this.isZoneStringsSet = true;
        this.cachedHashCode = 0;
    }

    public String getLocalPatternChars() {
        return this.localPatternChars;
    }

    public void setLocalPatternChars(String str) {
        this.localPatternChars = str.toString();
        this.cachedHashCode = 0;
    }

    public Object clone() {
        try {
            DateFormatSymbols dateFormatSymbols = (DateFormatSymbols) super.clone();
            copyMembers(this, dateFormatSymbols);
            return dateFormatSymbols;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public int hashCode() {
        int iHashCode = this.cachedHashCode;
        if (iHashCode == 0) {
            iHashCode = (11 * ((11 * ((11 * ((11 * ((11 * ((11 * ((11 * ((11 * 5) + Arrays.hashCode(this.eras))) + Arrays.hashCode(this.months))) + Arrays.hashCode(this.shortMonths))) + Arrays.hashCode(this.weekdays))) + Arrays.hashCode(this.shortWeekdays))) + Arrays.hashCode(this.ampms))) + Arrays.deepHashCode(getZoneStringsWrapper()))) + Objects.hashCode(this.localPatternChars);
            this.cachedHashCode = iHashCode;
        }
        return iHashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DateFormatSymbols dateFormatSymbols = (DateFormatSymbols) obj;
        return Arrays.equals(this.eras, dateFormatSymbols.eras) && Arrays.equals(this.months, dateFormatSymbols.months) && Arrays.equals(this.shortMonths, dateFormatSymbols.shortMonths) && Arrays.equals(this.weekdays, dateFormatSymbols.weekdays) && Arrays.equals(this.shortWeekdays, dateFormatSymbols.shortWeekdays) && Arrays.equals(this.ampms, dateFormatSymbols.ampms) && Arrays.deepEquals(getZoneStringsWrapper(), dateFormatSymbols.getZoneStringsWrapper()) && ((this.localPatternChars != null && this.localPatternChars.equals(dateFormatSymbols.localPatternChars)) || (this.localPatternChars == null && dateFormatSymbols.localPatternChars == null));
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void initializeData(java.util.Locale r6) {
        /*
            Method dump skipped, instructions count: 368
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.DateFormatSymbols.initializeData(java.util.Locale):void");
    }

    private static String[] toOneBasedArray(String[] strArr) {
        int length = strArr.length;
        String[] strArr2 = new String[length + 1];
        strArr2[0] = "";
        for (int i2 = 0; i2 < length; i2++) {
            strArr2[i2 + 1] = strArr[i2];
        }
        return strArr2;
    }

    final int getZoneIndex(String str) {
        String[][] zoneStringsWrapper = getZoneStringsWrapper();
        if (this.lastZoneIndex < zoneStringsWrapper.length && str.equals(zoneStringsWrapper[this.lastZoneIndex][0])) {
            return this.lastZoneIndex;
        }
        for (int i2 = 0; i2 < zoneStringsWrapper.length; i2++) {
            if (str.equals(zoneStringsWrapper[i2][0])) {
                this.lastZoneIndex = i2;
                return i2;
            }
        }
        return -1;
    }

    final String[][] getZoneStringsWrapper() {
        if (isSubclassObject()) {
            return getZoneStrings();
        }
        return getZoneStringsImpl(false);
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [java.lang.String[], java.lang.String[][]] */
    private String[][] getZoneStringsImpl(boolean z2) {
        if (this.zoneStrings == null) {
            this.zoneStrings = TimeZoneNameUtility.getZoneStrings(this.locale);
        }
        if (!z2) {
            return this.zoneStrings;
        }
        int length = this.zoneStrings.length;
        ?? r0 = new String[length];
        for (int i2 = 0; i2 < length; i2++) {
            r0[i2] = (String[]) Arrays.copyOf(this.zoneStrings[i2], this.zoneStrings[i2].length);
        }
        return r0;
    }

    private boolean isSubclassObject() {
        return !getClass().getName().equals("java.text.DateFormatSymbols");
    }

    private void copyMembers(DateFormatSymbols dateFormatSymbols, DateFormatSymbols dateFormatSymbols2) {
        dateFormatSymbols2.locale = dateFormatSymbols.locale;
        dateFormatSymbols2.eras = (String[]) Arrays.copyOf(dateFormatSymbols.eras, dateFormatSymbols.eras.length);
        dateFormatSymbols2.months = (String[]) Arrays.copyOf(dateFormatSymbols.months, dateFormatSymbols.months.length);
        dateFormatSymbols2.shortMonths = (String[]) Arrays.copyOf(dateFormatSymbols.shortMonths, dateFormatSymbols.shortMonths.length);
        dateFormatSymbols2.weekdays = (String[]) Arrays.copyOf(dateFormatSymbols.weekdays, dateFormatSymbols.weekdays.length);
        dateFormatSymbols2.shortWeekdays = (String[]) Arrays.copyOf(dateFormatSymbols.shortWeekdays, dateFormatSymbols.shortWeekdays.length);
        dateFormatSymbols2.ampms = (String[]) Arrays.copyOf(dateFormatSymbols.ampms, dateFormatSymbols.ampms.length);
        if (dateFormatSymbols.zoneStrings != null) {
            dateFormatSymbols2.zoneStrings = dateFormatSymbols.getZoneStringsImpl(true);
        } else {
            dateFormatSymbols2.zoneStrings = (String[][]) null;
        }
        dateFormatSymbols2.localPatternChars = dateFormatSymbols.localPatternChars;
        dateFormatSymbols2.cachedHashCode = 0;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.zoneStrings == null) {
            this.zoneStrings = TimeZoneNameUtility.getZoneStrings(this.locale);
        }
        objectOutputStream.defaultWriteObject();
    }
}
