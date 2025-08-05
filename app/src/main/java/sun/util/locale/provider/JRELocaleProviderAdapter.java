package sun.util.locale.provider;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.spi.BreakIteratorProvider;
import java.text.spi.CollatorProvider;
import java.text.spi.DateFormatProvider;
import java.text.spi.DateFormatSymbolsProvider;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.text.spi.NumberFormatProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CalendarNameProvider;
import java.util.spi.CurrencyNameProvider;
import java.util.spi.LocaleNameProvider;
import java.util.spi.LocaleServiceProvider;
import java.util.spi.TimeZoneNameProvider;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetPropertyAction;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.resources.LocaleData;
import sun.util.spi.CalendarProvider;

/* loaded from: rt.jar:sun/util/locale/provider/JRELocaleProviderAdapter.class */
public class JRELocaleProviderAdapter extends LocaleProviderAdapter implements ResourceBundleBasedAdapter {
    private static final String LOCALE_DATA_JAR_NAME = "localedata.jar";
    private volatile LocaleData localeData;
    private static volatile Boolean isNonENSupported = null;
    private final ConcurrentMap<String, Set<String>> langtagSets = new ConcurrentHashMap();
    private final ConcurrentMap<Locale, LocaleResources> localeResourcesMap = new ConcurrentHashMap();
    private volatile BreakIteratorProvider breakIteratorProvider = null;
    private volatile CollatorProvider collatorProvider = null;
    private volatile DateFormatProvider dateFormatProvider = null;
    private volatile DateFormatSymbolsProvider dateFormatSymbolsProvider = null;
    private volatile DecimalFormatSymbolsProvider decimalFormatSymbolsProvider = null;
    private volatile NumberFormatProvider numberFormatProvider = null;
    private volatile CurrencyNameProvider currencyNameProvider = null;
    private volatile LocaleNameProvider localeNameProvider = null;
    private volatile TimeZoneNameProvider timeZoneNameProvider = null;
    private volatile CalendarDataProvider calendarDataProvider = null;
    private volatile CalendarNameProvider calendarNameProvider = null;
    private volatile CalendarProvider calendarProvider = null;

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleProviderAdapter.Type getAdapterType() {
        return LocaleProviderAdapter.Type.JRE;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public <P extends LocaleServiceProvider> P getLocaleServiceProvider(Class<P> cls) throws SecurityException {
        switch (cls.getSimpleName()) {
            case "BreakIteratorProvider":
                return getBreakIteratorProvider();
            case "CollatorProvider":
                return getCollatorProvider();
            case "DateFormatProvider":
                return getDateFormatProvider();
            case "DateFormatSymbolsProvider":
                return getDateFormatSymbolsProvider();
            case "DecimalFormatSymbolsProvider":
                return getDecimalFormatSymbolsProvider();
            case "NumberFormatProvider":
                return getNumberFormatProvider();
            case "CurrencyNameProvider":
                return getCurrencyNameProvider();
            case "LocaleNameProvider":
                return getLocaleNameProvider();
            case "TimeZoneNameProvider":
                return getTimeZoneNameProvider();
            case "CalendarDataProvider":
                return getCalendarDataProvider();
            case "CalendarNameProvider":
                return getCalendarNameProvider();
            case "CalendarProvider":
                return getCalendarProvider();
            default:
                throw new InternalError("should not come down here");
        }
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public BreakIteratorProvider getBreakIteratorProvider() {
        if (this.breakIteratorProvider == null) {
            BreakIteratorProviderImpl breakIteratorProviderImpl = new BreakIteratorProviderImpl(getAdapterType(), getLanguageTagSet("FormatData"));
            synchronized (this) {
                if (this.breakIteratorProvider == null) {
                    this.breakIteratorProvider = breakIteratorProviderImpl;
                }
            }
        }
        return this.breakIteratorProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CollatorProvider getCollatorProvider() {
        if (this.collatorProvider == null) {
            CollatorProviderImpl collatorProviderImpl = new CollatorProviderImpl(getAdapterType(), getLanguageTagSet("CollationData"));
            synchronized (this) {
                if (this.collatorProvider == null) {
                    this.collatorProvider = collatorProviderImpl;
                }
            }
        }
        return this.collatorProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public DateFormatProvider getDateFormatProvider() {
        if (this.dateFormatProvider == null) {
            DateFormatProviderImpl dateFormatProviderImpl = new DateFormatProviderImpl(getAdapterType(), getLanguageTagSet("FormatData"));
            synchronized (this) {
                if (this.dateFormatProvider == null) {
                    this.dateFormatProvider = dateFormatProviderImpl;
                }
            }
        }
        return this.dateFormatProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public DateFormatSymbolsProvider getDateFormatSymbolsProvider() {
        if (this.dateFormatSymbolsProvider == null) {
            DateFormatSymbolsProviderImpl dateFormatSymbolsProviderImpl = new DateFormatSymbolsProviderImpl(getAdapterType(), getLanguageTagSet("FormatData"));
            synchronized (this) {
                if (this.dateFormatSymbolsProvider == null) {
                    this.dateFormatSymbolsProvider = dateFormatSymbolsProviderImpl;
                }
            }
        }
        return this.dateFormatSymbolsProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public DecimalFormatSymbolsProvider getDecimalFormatSymbolsProvider() {
        if (this.decimalFormatSymbolsProvider == null) {
            DecimalFormatSymbolsProviderImpl decimalFormatSymbolsProviderImpl = new DecimalFormatSymbolsProviderImpl(getAdapterType(), getLanguageTagSet("FormatData"));
            synchronized (this) {
                if (this.decimalFormatSymbolsProvider == null) {
                    this.decimalFormatSymbolsProvider = decimalFormatSymbolsProviderImpl;
                }
            }
        }
        return this.decimalFormatSymbolsProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public NumberFormatProvider getNumberFormatProvider() {
        if (this.numberFormatProvider == null) {
            NumberFormatProviderImpl numberFormatProviderImpl = new NumberFormatProviderImpl(getAdapterType(), getLanguageTagSet("FormatData"));
            synchronized (this) {
                if (this.numberFormatProvider == null) {
                    this.numberFormatProvider = numberFormatProviderImpl;
                }
            }
        }
        return this.numberFormatProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CurrencyNameProvider getCurrencyNameProvider() {
        if (this.currencyNameProvider == null) {
            CurrencyNameProviderImpl currencyNameProviderImpl = new CurrencyNameProviderImpl(getAdapterType(), getLanguageTagSet("CurrencyNames"));
            synchronized (this) {
                if (this.currencyNameProvider == null) {
                    this.currencyNameProvider = currencyNameProviderImpl;
                }
            }
        }
        return this.currencyNameProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleNameProvider getLocaleNameProvider() {
        if (this.localeNameProvider == null) {
            LocaleNameProviderImpl localeNameProviderImpl = new LocaleNameProviderImpl(getAdapterType(), getLanguageTagSet("LocaleNames"));
            synchronized (this) {
                if (this.localeNameProvider == null) {
                    this.localeNameProvider = localeNameProviderImpl;
                }
            }
        }
        return this.localeNameProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public TimeZoneNameProvider getTimeZoneNameProvider() {
        if (this.timeZoneNameProvider == null) {
            TimeZoneNameProviderImpl timeZoneNameProviderImpl = new TimeZoneNameProviderImpl(getAdapterType(), getLanguageTagSet("TimeZoneNames"));
            synchronized (this) {
                if (this.timeZoneNameProvider == null) {
                    this.timeZoneNameProvider = timeZoneNameProviderImpl;
                }
            }
        }
        return this.timeZoneNameProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CalendarDataProvider getCalendarDataProvider() {
        if (this.calendarDataProvider == null) {
            CalendarDataProviderImpl calendarDataProviderImpl = new CalendarDataProviderImpl(getAdapterType(), getLanguageTagSet("CalendarData"));
            synchronized (this) {
                if (this.calendarDataProvider == null) {
                    this.calendarDataProvider = calendarDataProviderImpl;
                }
            }
        }
        return this.calendarDataProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CalendarNameProvider getCalendarNameProvider() {
        if (this.calendarNameProvider == null) {
            CalendarNameProviderImpl calendarNameProviderImpl = new CalendarNameProviderImpl(getAdapterType(), getLanguageTagSet("FormatData"));
            synchronized (this) {
                if (this.calendarNameProvider == null) {
                    this.calendarNameProvider = calendarNameProviderImpl;
                }
            }
        }
        return this.calendarNameProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CalendarProvider getCalendarProvider() {
        if (this.calendarProvider == null) {
            CalendarProviderImpl calendarProviderImpl = new CalendarProviderImpl(getAdapterType(), getLanguageTagSet("CalendarData"));
            synchronized (this) {
                if (this.calendarProvider == null) {
                    this.calendarProvider = calendarProviderImpl;
                }
            }
        }
        return this.calendarProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleResources getLocaleResources(Locale locale) {
        LocaleResources localeResources = this.localeResourcesMap.get(locale);
        if (localeResources == null) {
            localeResources = new LocaleResources(this, locale);
            LocaleResources localeResourcesPutIfAbsent = this.localeResourcesMap.putIfAbsent(locale, localeResources);
            if (localeResourcesPutIfAbsent != null) {
                localeResources = localeResourcesPutIfAbsent;
            }
        }
        return localeResources;
    }

    @Override // sun.util.locale.provider.ResourceBundleBasedAdapter
    public LocaleData getLocaleData() {
        if (this.localeData == null) {
            synchronized (this) {
                if (this.localeData == null) {
                    this.localeData = new LocaleData(getAdapterType());
                }
            }
        }
        return this.localeData;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public Locale[] getAvailableLocales() {
        return (Locale[]) AvailableJRELocales.localeList.clone();
    }

    public Set<String> getLanguageTagSet(String str) {
        Set<String> setCreateLanguageTagSet = this.langtagSets.get(str);
        if (setCreateLanguageTagSet == null) {
            setCreateLanguageTagSet = createLanguageTagSet(str);
            Set<String> setPutIfAbsent = this.langtagSets.putIfAbsent(str, setCreateLanguageTagSet);
            if (setPutIfAbsent != null) {
                setCreateLanguageTagSet = setPutIfAbsent;
            }
        }
        return setCreateLanguageTagSet;
    }

    protected Set<String> createLanguageTagSet(String str) {
        String supportedLocaleString = LocaleDataMetaInfo.getSupportedLocaleString(str);
        if (supportedLocaleString == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        StringTokenizer stringTokenizer = new StringTokenizer(supportedLocaleString);
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.equals(CallSiteDescriptor.OPERATOR_DELIMITER)) {
                if (!isNonENLangSupported()) {
                    break;
                }
            } else {
                hashSet.add(strNextToken);
            }
        }
        return hashSet;
    }

    /* loaded from: rt.jar:sun/util/locale/provider/JRELocaleProviderAdapter$AvailableJRELocales.class */
    private static class AvailableJRELocales {
        private static final Locale[] localeList = JRELocaleProviderAdapter.createAvailableLocales();

        private AvailableJRELocales() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Locale[] createAvailableLocales() {
        StringTokenizer stringTokenizer;
        String strNextToken;
        String supportedLocaleString = LocaleDataMetaInfo.getSupportedLocaleString("AvailableLocales");
        if (supportedLocaleString.length() == 0) {
            throw new InternalError("No available locales for JRE");
        }
        int iIndexOf = supportedLocaleString.indexOf(124);
        if (isNonENLangSupported()) {
            stringTokenizer = new StringTokenizer(supportedLocaleString.substring(0, iIndexOf) + supportedLocaleString.substring(iIndexOf + 1));
        } else {
            stringTokenizer = new StringTokenizer(supportedLocaleString.substring(0, iIndexOf));
        }
        int iCountTokens = stringTokenizer.countTokens();
        Locale[] localeArr = new Locale[iCountTokens + 1];
        localeArr[0] = Locale.ROOT;
        for (int i2 = 1; i2 <= iCountTokens; i2++) {
            strNextToken = stringTokenizer.nextToken();
            switch (strNextToken) {
                case "ja-JP-JP":
                    localeArr[i2] = JRELocaleConstants.JA_JP_JP;
                    break;
                case "no-NO-NY":
                    localeArr[i2] = JRELocaleConstants.NO_NO_NY;
                    break;
                case "th-TH-TH":
                    localeArr[i2] = JRELocaleConstants.TH_TH_TH;
                    break;
                default:
                    localeArr[i2] = Locale.forLanguageTag(strNextToken);
                    break;
            }
        }
        return localeArr;
    }

    private static boolean isNonENLangSupported() {
        if (isNonENSupported == null) {
            synchronized (JRELocaleProviderAdapter.class) {
                if (isNonENSupported == null) {
                    String str = File.separator;
                    final File file = new File(((String) AccessController.doPrivileged(new GetPropertyAction("java.home"))) + str + "lib" + str + "ext" + str + LOCALE_DATA_JAR_NAME);
                    isNonENSupported = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.util.locale.provider.JRELocaleProviderAdapter.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Boolean run2() {
                            return Boolean.valueOf(file.exists());
                        }
                    });
                }
            }
        }
        return isNonENSupported.booleanValue();
    }
}
