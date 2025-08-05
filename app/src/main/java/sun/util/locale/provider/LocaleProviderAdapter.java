package sun.util.locale.provider;

import java.text.spi.BreakIteratorProvider;
import java.text.spi.CollatorProvider;
import java.text.spi.DateFormatProvider;
import java.text.spi.DateFormatSymbolsProvider;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.text.spi.NumberFormatProvider;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CalendarNameProvider;
import java.util.spi.CurrencyNameProvider;
import java.util.spi.LocaleNameProvider;
import java.util.spi.LocaleServiceProvider;
import java.util.spi.TimeZoneNameProvider;
import sun.util.spi.CalendarProvider;

/* loaded from: rt.jar:sun/util/locale/provider/LocaleProviderAdapter.class */
public abstract class LocaleProviderAdapter {
    private static final List<Type> adapterPreference;
    private static LocaleProviderAdapter jreLocaleProviderAdapter;
    private static LocaleProviderAdapter spiLocaleProviderAdapter;
    private static LocaleProviderAdapter cldrLocaleProviderAdapter;
    private static LocaleProviderAdapter hostLocaleProviderAdapter;
    private static LocaleProviderAdapter fallbackLocaleProviderAdapter;
    static Type defaultLocaleProviderAdapter;
    private static ConcurrentMap<Class<? extends LocaleServiceProvider>, ConcurrentMap<Locale, LocaleProviderAdapter>> adapterCache;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract Type getAdapterType();

    public abstract <P extends LocaleServiceProvider> P getLocaleServiceProvider(Class<P> cls);

    public abstract BreakIteratorProvider getBreakIteratorProvider();

    public abstract CollatorProvider getCollatorProvider();

    public abstract DateFormatProvider getDateFormatProvider();

    public abstract DateFormatSymbolsProvider getDateFormatSymbolsProvider();

    public abstract DecimalFormatSymbolsProvider getDecimalFormatSymbolsProvider();

    public abstract NumberFormatProvider getNumberFormatProvider();

    public abstract CurrencyNameProvider getCurrencyNameProvider();

    public abstract LocaleNameProvider getLocaleNameProvider();

    public abstract TimeZoneNameProvider getTimeZoneNameProvider();

    public abstract CalendarDataProvider getCalendarDataProvider();

    public abstract CalendarNameProvider getCalendarNameProvider();

    public abstract CalendarProvider getCalendarProvider();

    public abstract LocaleResources getLocaleResources(Locale locale);

    public abstract Locale[] getAvailableLocales();

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00de A[Catch: IllegalArgumentException | UnsupportedOperationException -> 0x00ea, TryCatch #0 {IllegalArgumentException | UnsupportedOperationException -> 0x00ea, blocks: (B:14:0x007e, B:15:0x0097, B:16:0x00b0, B:18:0x00b6, B:19:0x00c3, B:21:0x00c9, B:22:0x00d3, B:24:0x00de), top: B:38:0x007e }] */
    static {
        /*
            Method dump skipped, instructions count: 345
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.locale.provider.LocaleProviderAdapter.m6960clinit():void");
    }

    /* loaded from: rt.jar:sun/util/locale/provider/LocaleProviderAdapter$Type.class */
    public enum Type {
        JRE("sun.util.resources", "sun.text.resources"),
        CLDR("sun.util.resources.cldr", "sun.text.resources.cldr"),
        SPI,
        HOST,
        FALLBACK("sun.util.resources", "sun.text.resources");

        private final String UTIL_RESOURCES_PACKAGE;
        private final String TEXT_RESOURCES_PACKAGE;

        Type() {
            this(null, null);
        }

        Type(String str, String str2) {
            this.UTIL_RESOURCES_PACKAGE = str;
            this.TEXT_RESOURCES_PACKAGE = str2;
        }

        public String getUtilResourcesPackage() {
            return this.UTIL_RESOURCES_PACKAGE;
        }

        public String getTextResourcesPackage() {
            return this.TEXT_RESOURCES_PACKAGE;
        }
    }

    public static LocaleProviderAdapter forType(Type type) {
        switch (type) {
            case CLDR:
                return cldrLocaleProviderAdapter;
            case HOST:
                return hostLocaleProviderAdapter;
            case JRE:
                return jreLocaleProviderAdapter;
            case SPI:
                return spiLocaleProviderAdapter;
            case FALLBACK:
                return fallbackLocaleProviderAdapter;
            default:
                throw new InternalError("unknown locale data adapter type");
        }
    }

    public static LocaleProviderAdapter forJRE() {
        return jreLocaleProviderAdapter;
    }

    public static LocaleProviderAdapter getResourceBundleBased() {
        for (Type type : getAdapterPreference()) {
            if (type == Type.JRE || type == Type.CLDR || type == Type.FALLBACK) {
                return forType(type);
            }
        }
        throw new InternalError();
    }

    public static List<Type> getAdapterPreference() {
        return adapterPreference;
    }

    public static LocaleProviderAdapter getAdapter(Class<? extends LocaleServiceProvider> cls, Locale locale) {
        LocaleProviderAdapter localeProviderAdapterFindAdapter;
        ConcurrentMap<Locale, LocaleProviderAdapter> concurrentHashMap = adapterCache.get(cls);
        if (concurrentHashMap != null) {
            LocaleProviderAdapter localeProviderAdapter = concurrentHashMap.get(locale);
            if (localeProviderAdapter != null) {
                return localeProviderAdapter;
            }
        } else {
            concurrentHashMap = new ConcurrentHashMap();
            adapterCache.putIfAbsent(cls, concurrentHashMap);
        }
        LocaleProviderAdapter localeProviderAdapterFindAdapter2 = findAdapter(cls, locale);
        if (localeProviderAdapterFindAdapter2 != null) {
            concurrentHashMap.putIfAbsent(locale, localeProviderAdapterFindAdapter2);
            return localeProviderAdapterFindAdapter2;
        }
        for (Locale locale2 : ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT).getCandidateLocales("", locale)) {
            if (!locale2.equals(locale) && (localeProviderAdapterFindAdapter = findAdapter(cls, locale2)) != null) {
                concurrentHashMap.putIfAbsent(locale, localeProviderAdapterFindAdapter);
                return localeProviderAdapterFindAdapter;
            }
        }
        concurrentHashMap.putIfAbsent(locale, fallbackLocaleProviderAdapter);
        return fallbackLocaleProviderAdapter;
    }

    private static LocaleProviderAdapter findAdapter(Class<? extends LocaleServiceProvider> cls, Locale locale) {
        Iterator<Type> it = getAdapterPreference().iterator();
        while (it.hasNext()) {
            LocaleProviderAdapter localeProviderAdapterForType = forType(it.next());
            LocaleServiceProvider localeServiceProvider = localeProviderAdapterForType.getLocaleServiceProvider(cls);
            if (localeServiceProvider != null && localeServiceProvider.isSupportedLocale(locale)) {
                return localeProviderAdapterForType;
            }
        }
        return null;
    }

    public static boolean isSupportedLocale(Locale locale, Type type, Set<String> set) {
        if (!$assertionsDisabled && type != Type.JRE && type != Type.CLDR && type != Type.FALLBACK) {
            throw new AssertionError();
        }
        if (Locale.ROOT.equals(locale)) {
            return true;
        }
        if (type == Type.FALLBACK) {
            return false;
        }
        Locale localeStripExtensions = locale.stripExtensions();
        if (set.contains(localeStripExtensions.toLanguageTag())) {
            return true;
        }
        if (type == Type.JRE) {
            String strReplace = localeStripExtensions.toString().replace('_', '-');
            return set.contains(strReplace) || "ja-JP-JP".equals(strReplace) || "th-TH-TH".equals(strReplace) || "no-NO-NY".equals(strReplace);
        }
        return false;
    }

    public static Locale[] toLocaleArray(Set<String> set) {
        Locale[] localeArr = new Locale[set.size() + 1];
        int i2 = 0 + 1;
        localeArr[0] = Locale.ROOT;
        for (String str : set) {
            switch (str) {
                case "ja-JP-JP":
                    int i3 = i2;
                    i2++;
                    localeArr[i3] = JRELocaleConstants.JA_JP_JP;
                    break;
                case "th-TH-TH":
                    int i4 = i2;
                    i2++;
                    localeArr[i4] = JRELocaleConstants.TH_TH_TH;
                    break;
                default:
                    int i5 = i2;
                    i2++;
                    localeArr[i5] = Locale.forLanguageTag(str);
                    break;
            }
        }
        return localeArr;
    }
}
