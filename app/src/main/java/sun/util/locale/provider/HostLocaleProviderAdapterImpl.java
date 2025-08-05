package sun.util.locale.provider;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.text.spi.DateFormatSymbolsProvider;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.text.spi.NumberFormatProvider;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CurrencyNameProvider;
import java.util.spi.LocaleNameProvider;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;
import sun.util.spi.CalendarProvider;

/* loaded from: rt.jar:sun/util/locale/provider/HostLocaleProviderAdapterImpl.class */
public class HostLocaleProviderAdapterImpl {
    private static final int CAT_DISPLAY = 0;
    private static final int CAT_FORMAT = 1;
    private static final int NF_NUMBER = 0;
    private static final int NF_CURRENCY = 1;
    private static final int NF_PERCENT = 2;
    private static final int NF_INTEGER = 3;
    private static final int NF_MAX = 3;
    private static final int CD_FIRSTDAYOFWEEK = 0;
    private static final int CD_MINIMALDAYSINFIRSTWEEK = 1;
    private static final int DN_CURRENCY_NAME = 0;
    private static final int DN_CURRENCY_SYMBOL = 1;
    private static final int DN_LOCALE_LANGUAGE = 2;
    private static final int DN_LOCALE_SCRIPT = 3;
    private static final int DN_LOCALE_REGION = 4;
    private static final int DN_LOCALE_VARIANT = 5;
    private static final String[] calIDToLDML = {"", "gregory", "gregory_en-US", "japanese", "roc", "", "islamic", "buddhist", "hebrew", "gregory_fr", "gregory_ar", "gregory_en", "gregory_fr"};
    private static ConcurrentMap<Locale, SoftReference<AtomicReferenceArray<String>>> dateFormatCache = new ConcurrentHashMap();
    private static ConcurrentMap<Locale, SoftReference<DateFormatSymbols>> dateFormatSymbolsCache = new ConcurrentHashMap();
    private static ConcurrentMap<Locale, SoftReference<AtomicReferenceArray<String>>> numberFormatCache = new ConcurrentHashMap();
    private static ConcurrentMap<Locale, SoftReference<DecimalFormatSymbols>> decimalFormatSymbolsCache = new ConcurrentHashMap();
    private static final Set<Locale> supportedLocaleSet;
    private static final String nativeDisplayLanguage;
    private static final Locale[] supportedLocale;

    private static native boolean initialize();

    private static native String getDefaultLocale(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getDateTimePattern(int i2, int i3, String str);

    private static native int getCalendarID(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] getAmPmStrings(String str, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] getEras(String str, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] getMonths(String str, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] getShortMonths(String str, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] getWeekdays(String str, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] getShortWeekdays(String str, String[] strArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getNumberPattern(int i2, String str);

    private static native boolean isNativeDigit(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getCurrencySymbol(String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getDecimalSeparator(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getGroupingSeparator(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getInfinity(String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getInternationalCurrencySymbol(String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getMinusSign(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getMonetaryDecimalSeparator(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getNaN(String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getPercent(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getPerMill(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native char getZeroDigit(String str, char c2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getCalendarDataValue(String str, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getDisplayString(String str, int i2, String str2);

    static {
        HashSet hashSet = new HashSet();
        if (initialize()) {
            ResourceBundle.Control noFallbackControl = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT);
            String defaultLocale = getDefaultLocale(0);
            Locale localeForLanguageTag = Locale.forLanguageTag(defaultLocale.replace('_', '-'));
            hashSet.addAll(noFallbackControl.getCandidateLocales("", localeForLanguageTag));
            nativeDisplayLanguage = localeForLanguageTag.getLanguage();
            String defaultLocale2 = getDefaultLocale(1);
            if (!defaultLocale2.equals(defaultLocale)) {
                hashSet.addAll(noFallbackControl.getCandidateLocales("", Locale.forLanguageTag(defaultLocale2.replace('_', '-'))));
            }
        } else {
            nativeDisplayLanguage = "";
        }
        supportedLocaleSet = Collections.unmodifiableSet(hashSet);
        supportedLocale = (Locale[]) supportedLocaleSet.toArray(new Locale[0]);
    }

    public static DateFormatProvider getDateFormatProvider() {
        return new DateFormatProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.1
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.getSupportedCalendarLocales();
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.isSupportedCalendarLocale(locale);
            }

            @Override // java.text.spi.DateFormatProvider
            public DateFormat getDateInstance(int i2, Locale locale) {
                return new SimpleDateFormat(getDateTimePatterns(locale).get(i2 / 2), HostLocaleProviderAdapterImpl.getCalendarLocale(locale));
            }

            @Override // java.text.spi.DateFormatProvider
            public DateFormat getTimeInstance(int i2, Locale locale) {
                return new SimpleDateFormat(getDateTimePatterns(locale).get((i2 / 2) + 2), HostLocaleProviderAdapterImpl.getCalendarLocale(locale));
            }

            @Override // java.text.spi.DateFormatProvider
            public DateFormat getDateTimeInstance(int i2, int i3, Locale locale) {
                AtomicReferenceArray<String> dateTimePatterns = getDateTimePatterns(locale);
                return new SimpleDateFormat(dateTimePatterns.get(i2 / 2) + " " + dateTimePatterns.get((i3 / 2) + 2), HostLocaleProviderAdapterImpl.getCalendarLocale(locale));
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            private java.util.concurrent.atomic.AtomicReferenceArray<java.lang.String> getDateTimePatterns(java.util.Locale r8) {
                /*
                    r7 = this;
                    java.util.concurrent.ConcurrentMap r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$300()
                    r1 = r8
                    java.lang.Object r0 = r0.get(r1)
                    java.lang.ref.SoftReference r0 = (java.lang.ref.SoftReference) r0
                    r10 = r0
                    r0 = r10
                    if (r0 == 0) goto L1d
                    r0 = r10
                    java.lang.Object r0 = r0.get()
                    java.util.concurrent.atomic.AtomicReferenceArray r0 = (java.util.concurrent.atomic.AtomicReferenceArray) r0
                    r1 = r0
                    r9 = r1
                    if (r0 != 0) goto L87
                L1d:
                    r0 = r8
                    java.util.Locale r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$400(r0)
                    java.lang.String r0 = r0.toLanguageTag()
                    r11 = r0
                    java.util.concurrent.atomic.AtomicReferenceArray r0 = new java.util.concurrent.atomic.AtomicReferenceArray
                    r1 = r0
                    r2 = 4
                    r1.<init>(r2)
                    r9 = r0
                    r0 = r9
                    r1 = 0
                    r2 = 0
                    r3 = 1
                    r4 = -1
                    r5 = r11
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$500(r3, r4, r5)
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$600(r3)
                    boolean r0 = r0.compareAndSet(r1, r2, r3)
                    r0 = r9
                    r1 = 1
                    r2 = 0
                    r3 = 3
                    r4 = -1
                    r5 = r11
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$500(r3, r4, r5)
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$600(r3)
                    boolean r0 = r0.compareAndSet(r1, r2, r3)
                    r0 = r9
                    r1 = 2
                    r2 = 0
                    r3 = -1
                    r4 = 1
                    r5 = r11
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$500(r3, r4, r5)
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$600(r3)
                    boolean r0 = r0.compareAndSet(r1, r2, r3)
                    r0 = r9
                    r1 = 3
                    r2 = 0
                    r3 = -1
                    r4 = 3
                    r5 = r11
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$500(r3, r4, r5)
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$600(r3)
                    boolean r0 = r0.compareAndSet(r1, r2, r3)
                    java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference
                    r1 = r0
                    r2 = r9
                    r1.<init>(r2)
                    r10 = r0
                    java.util.concurrent.ConcurrentMap r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$300()
                    r1 = r8
                    r2 = r10
                    java.lang.Object r0 = r0.put(r1, r2)
                L87:
                    r0 = r9
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.HostLocaleProviderAdapterImpl.AnonymousClass1.getDateTimePatterns(java.util.Locale):java.util.concurrent.atomic.AtomicReferenceArray");
            }
        };
    }

    public static DateFormatSymbolsProvider getDateFormatSymbolsProvider() {
        return new DateFormatSymbolsProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.2
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.getSupportedCalendarLocales();
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.isSupportedCalendarLocale(locale);
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
            @Override // java.text.spi.DateFormatSymbolsProvider
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.text.DateFormatSymbols getInstance(java.util.Locale r5) {
                /*
                    r4 = this;
                    java.util.concurrent.ConcurrentMap r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$700()
                    r1 = r5
                    java.lang.Object r0 = r0.get(r1)
                    java.lang.ref.SoftReference r0 = (java.lang.ref.SoftReference) r0
                    r7 = r0
                    r0 = r7
                    if (r0 == 0) goto L1d
                    r0 = r7
                    java.lang.Object r0 = r0.get()
                    java.text.DateFormatSymbols r0 = (java.text.DateFormatSymbols) r0
                    r1 = r0
                    r6 = r1
                    if (r0 != 0) goto L91
                L1d:
                    java.text.DateFormatSymbols r0 = new java.text.DateFormatSymbols
                    r1 = r0
                    r2 = r5
                    r1.<init>(r2)
                    r6 = r0
                    r0 = r5
                    java.util.Locale r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$400(r0)
                    java.lang.String r0 = r0.toLanguageTag()
                    r8 = r0
                    r0 = r6
                    r1 = r8
                    r2 = r6
                    java.lang.String[] r2 = r2.getAmPmStrings()
                    java.lang.String[] r1 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$800(r1, r2)
                    r0.setAmPmStrings(r1)
                    r0 = r6
                    r1 = r8
                    r2 = r6
                    java.lang.String[] r2 = r2.getEras()
                    java.lang.String[] r1 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$900(r1, r2)
                    r0.setEras(r1)
                    r0 = r6
                    r1 = r8
                    r2 = r6
                    java.lang.String[] r2 = r2.getMonths()
                    java.lang.String[] r1 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1000(r1, r2)
                    r0.setMonths(r1)
                    r0 = r6
                    r1 = r8
                    r2 = r6
                    java.lang.String[] r2 = r2.getShortMonths()
                    java.lang.String[] r1 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1100(r1, r2)
                    r0.setShortMonths(r1)
                    r0 = r6
                    r1 = r8
                    r2 = r6
                    java.lang.String[] r2 = r2.getWeekdays()
                    java.lang.String[] r1 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1200(r1, r2)
                    r0.setWeekdays(r1)
                    r0 = r6
                    r1 = r8
                    r2 = r6
                    java.lang.String[] r2 = r2.getShortWeekdays()
                    java.lang.String[] r1 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1300(r1, r2)
                    r0.setShortWeekdays(r1)
                    java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference
                    r1 = r0
                    r2 = r6
                    r1.<init>(r2)
                    r7 = r0
                    java.util.concurrent.ConcurrentMap r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$700()
                    r1 = r5
                    r2 = r7
                    java.lang.Object r0 = r0.put(r1, r2)
                L91:
                    r0 = r6
                    java.lang.Object r0 = r0.clone()
                    java.text.DateFormatSymbols r0 = (java.text.DateFormatSymbols) r0
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.HostLocaleProviderAdapterImpl.AnonymousClass2.getInstance(java.util.Locale):java.text.DateFormatSymbols");
            }
        };
    }

    public static NumberFormatProvider getNumberFormatProvider() {
        return new NumberFormatProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.3
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.getSupportedNativeDigitLocales();
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.isSupportedNativeDigitLocale(locale);
            }

            @Override // java.text.spi.NumberFormatProvider
            public NumberFormat getCurrencyInstance(Locale locale) {
                return new DecimalFormat(getNumberPatterns(locale).get(1), DecimalFormatSymbols.getInstance(locale));
            }

            @Override // java.text.spi.NumberFormatProvider
            public NumberFormat getIntegerInstance(Locale locale) {
                return new DecimalFormat(getNumberPatterns(locale).get(3), DecimalFormatSymbols.getInstance(locale));
            }

            @Override // java.text.spi.NumberFormatProvider
            public NumberFormat getNumberInstance(Locale locale) {
                return new DecimalFormat(getNumberPatterns(locale).get(0), DecimalFormatSymbols.getInstance(locale));
            }

            @Override // java.text.spi.NumberFormatProvider
            public NumberFormat getPercentInstance(Locale locale) {
                return new DecimalFormat(getNumberPatterns(locale).get(2), DecimalFormatSymbols.getInstance(locale));
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            private java.util.concurrent.atomic.AtomicReferenceArray<java.lang.String> getNumberPatterns(java.util.Locale r7) {
                /*
                    r6 = this;
                    java.util.concurrent.ConcurrentMap r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1600()
                    r1 = r7
                    java.lang.Object r0 = r0.get(r1)
                    java.lang.ref.SoftReference r0 = (java.lang.ref.SoftReference) r0
                    r9 = r0
                    r0 = r9
                    if (r0 == 0) goto L1d
                    r0 = r9
                    java.lang.Object r0 = r0.get()
                    java.util.concurrent.atomic.AtomicReferenceArray r0 = (java.util.concurrent.atomic.AtomicReferenceArray) r0
                    r1 = r0
                    r8 = r1
                    if (r0 != 0) goto L5e
                L1d:
                    r0 = r7
                    java.lang.String r0 = r0.toLanguageTag()
                    r10 = r0
                    java.util.concurrent.atomic.AtomicReferenceArray r0 = new java.util.concurrent.atomic.AtomicReferenceArray
                    r1 = r0
                    r2 = 4
                    r1.<init>(r2)
                    r8 = r0
                    r0 = 0
                    r11 = r0
                L2f:
                    r0 = r11
                    r1 = 3
                    if (r0 > r1) goto L4a
                    r0 = r8
                    r1 = r11
                    r2 = 0
                    r3 = r11
                    r4 = r10
                    java.lang.String r3 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1700(r3, r4)
                    boolean r0 = r0.compareAndSet(r1, r2, r3)
                    int r11 = r11 + 1
                    goto L2f
                L4a:
                    java.lang.ref.SoftReference r0 = new java.lang.ref.SoftReference
                    r1 = r0
                    r2 = r8
                    r1.<init>(r2)
                    r9 = r0
                    java.util.concurrent.ConcurrentMap r0 = sun.util.locale.provider.HostLocaleProviderAdapterImpl.access$1600()
                    r1 = r7
                    r2 = r9
                    java.lang.Object r0 = r0.put(r1, r2)
                L5e:
                    r0 = r8
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.HostLocaleProviderAdapterImpl.AnonymousClass3.getNumberPatterns(java.util.Locale):java.util.concurrent.atomic.AtomicReferenceArray");
            }
        };
    }

    public static DecimalFormatSymbolsProvider getDecimalFormatSymbolsProvider() {
        return new DecimalFormatSymbolsProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.4
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.getSupportedNativeDigitLocales();
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.isSupportedNativeDigitLocale(locale);
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
            @Override // java.text.spi.DecimalFormatSymbolsProvider
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.text.DecimalFormatSymbols getInstance(java.util.Locale r5) {
                /*
                    Method dump skipped, instructions count: 221
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.HostLocaleProviderAdapterImpl.AnonymousClass4.getInstance(java.util.Locale):java.text.DecimalFormatSymbols");
            }
        };
    }

    public static CalendarDataProvider getCalendarDataProvider() {
        return new CalendarDataProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.5
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.getSupportedCalendarLocales();
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.isSupportedCalendarLocale(locale);
            }

            @Override // java.util.spi.CalendarDataProvider
            public int getFirstDayOfWeek(Locale locale) {
                int calendarDataValue = HostLocaleProviderAdapterImpl.getCalendarDataValue(HostLocaleProviderAdapterImpl.removeExtensions(locale).toLanguageTag(), 0);
                if (calendarDataValue != -1) {
                    return ((calendarDataValue + 1) % 7) + 1;
                }
                return 0;
            }

            @Override // java.util.spi.CalendarDataProvider
            public int getMinimalDaysInFirstWeek(Locale locale) {
                return 0;
            }
        };
    }

    public static CalendarProvider getCalendarProvider() {
        return new CalendarProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.6
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.getSupportedCalendarLocales();
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.isSupportedCalendarLocale(locale);
            }

            @Override // sun.util.spi.CalendarProvider
            public Calendar getInstance(TimeZone timeZone, Locale locale) {
                return new Calendar.Builder().setLocale(HostLocaleProviderAdapterImpl.getCalendarLocale(locale)).setTimeZone(timeZone).setInstant(System.currentTimeMillis()).build();
            }
        };
    }

    public static CurrencyNameProvider getCurrencyNameProvider() {
        return new CurrencyNameProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.7
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.supportedLocale;
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.supportedLocaleSet.contains(locale.stripExtensions()) && locale.getLanguage().equals(HostLocaleProviderAdapterImpl.nativeDisplayLanguage);
            }

            @Override // java.util.spi.CurrencyNameProvider
            public String getSymbol(String str, Locale locale) {
                try {
                    if (Currency.getInstance(locale).getCurrencyCode().equals(str)) {
                        return HostLocaleProviderAdapterImpl.getDisplayString(locale.toLanguageTag(), 1, str);
                    }
                    return null;
                } catch (IllegalArgumentException e2) {
                    return null;
                }
            }

            @Override // java.util.spi.CurrencyNameProvider
            public String getDisplayName(String str, Locale locale) {
                try {
                    if (Currency.getInstance(locale).getCurrencyCode().equals(str)) {
                        return HostLocaleProviderAdapterImpl.getDisplayString(locale.toLanguageTag(), 0, str);
                    }
                    return null;
                } catch (IllegalArgumentException e2) {
                    return null;
                }
            }
        };
    }

    public static LocaleNameProvider getLocaleNameProvider() {
        return new LocaleNameProvider() { // from class: sun.util.locale.provider.HostLocaleProviderAdapterImpl.8
            @Override // java.util.spi.LocaleServiceProvider
            public Locale[] getAvailableLocales() {
                return HostLocaleProviderAdapterImpl.supportedLocale;
            }

            @Override // java.util.spi.LocaleServiceProvider
            public boolean isSupportedLocale(Locale locale) {
                return HostLocaleProviderAdapterImpl.supportedLocaleSet.contains(locale.stripExtensions()) && locale.getLanguage().equals(HostLocaleProviderAdapterImpl.nativeDisplayLanguage);
            }

            @Override // java.util.spi.LocaleNameProvider
            public String getDisplayLanguage(String str, Locale locale) {
                return HostLocaleProviderAdapterImpl.getDisplayString(locale.toLanguageTag(), 2, str);
            }

            @Override // java.util.spi.LocaleNameProvider
            public String getDisplayCountry(String str, Locale locale) {
                return HostLocaleProviderAdapterImpl.getDisplayString(locale.toLanguageTag(), 4, HostLocaleProviderAdapterImpl.nativeDisplayLanguage + LanguageTag.SEP + str);
            }

            @Override // java.util.spi.LocaleNameProvider
            public String getDisplayScript(String str, Locale locale) {
                return null;
            }

            @Override // java.util.spi.LocaleNameProvider
            public String getDisplayVariant(String str, Locale locale) {
                return null;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String convertDateTimePattern(String str) {
        return str.replaceAll("dddd", "EEEE").replaceAll("ddd", "EEE").replaceAll("tt", "aa").replaceAll(PdfOps.g_TOKEN, "GG");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Locale[] getSupportedCalendarLocales() {
        if (supportedLocale.length != 0 && supportedLocaleSet.contains(Locale.JAPAN) && isJapaneseCalendar()) {
            Locale[] localeArr = new Locale[supportedLocale.length + 1];
            localeArr[0] = JRELocaleConstants.JA_JP_JP;
            System.arraycopy(supportedLocale, 0, localeArr, 1, supportedLocale.length);
            return localeArr;
        }
        return supportedLocale;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSupportedCalendarLocale(Locale locale) {
        int calendarID;
        Locale localeBuild = locale;
        if (localeBuild.hasExtensions() || localeBuild.getVariant() != "") {
            localeBuild = new Locale.Builder().setLocale(locale).clearExtensions().build();
        }
        if (!supportedLocaleSet.contains(localeBuild) || (calendarID = getCalendarID(localeBuild.toLanguageTag())) <= 0 || calendarID >= calIDToLDML.length) {
            return false;
        }
        String unicodeLocaleType = locale.getUnicodeLocaleType("ca");
        String strReplaceFirst = calIDToLDML[calendarID].replaceFirst("_.*", "");
        if (unicodeLocaleType == null) {
            return Calendar.getAvailableCalendarTypes().contains(strReplaceFirst);
        }
        return unicodeLocaleType.equals(strReplaceFirst);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Locale[] getSupportedNativeDigitLocales() {
        if (supportedLocale.length != 0 && supportedLocaleSet.contains(JRELocaleConstants.TH_TH) && isNativeDigit("th-TH")) {
            Locale[] localeArr = new Locale[supportedLocale.length + 1];
            localeArr[0] = JRELocaleConstants.TH_TH_TH;
            System.arraycopy(supportedLocale, 0, localeArr, 1, supportedLocale.length);
            return localeArr;
        }
        return supportedLocale;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSupportedNativeDigitLocale(Locale locale) {
        if (JRELocaleConstants.TH_TH_TH.equals(locale)) {
            return isNativeDigit("th-TH");
        }
        String unicodeLocaleType = null;
        Locale localeStripExtensions = locale;
        if (locale.hasExtensions()) {
            unicodeLocaleType = locale.getUnicodeLocaleType("nu");
            localeStripExtensions = locale.stripExtensions();
        }
        if (supportedLocaleSet.contains(localeStripExtensions)) {
            if (unicodeLocaleType == null || unicodeLocaleType.equals("latn")) {
                return true;
            }
            return locale.getLanguage().equals("th") && "thai".equals(unicodeLocaleType) && isNativeDigit(locale.toLanguageTag());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Locale removeExtensions(Locale locale) {
        return new Locale.Builder().setLocale(locale).clearExtensions().build();
    }

    private static boolean isJapaneseCalendar() {
        return getCalendarID("ja-JP") == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Locale getCalendarLocale(Locale locale) {
        int calendarID = getCalendarID(locale.toLanguageTag());
        if (calendarID > 0 && calendarID < calIDToLDML.length) {
            Locale.Builder builder = new Locale.Builder();
            String[] strArrSplit = calIDToLDML[calendarID].split("_");
            if (strArrSplit.length > 1) {
                builder.setLocale(Locale.forLanguageTag(strArrSplit[1]));
            } else {
                builder.setLocale(locale);
            }
            builder.setUnicodeLocaleKeyword("ca", strArrSplit[0]);
            return builder.build();
        }
        return locale;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Locale getNumberLocale(Locale locale) {
        if (JRELocaleConstants.TH_TH.equals(locale) && isNativeDigit("th-TH")) {
            Locale.Builder locale2 = new Locale.Builder().setLocale(locale);
            locale2.setUnicodeLocaleKeyword("nu", "thai");
            return locale2.build();
        }
        return locale;
    }
}
