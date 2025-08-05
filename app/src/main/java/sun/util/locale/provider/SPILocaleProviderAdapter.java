package sun.util.locale.provider;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.BreakIterator;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.spi.BreakIteratorProvider;
import java.text.spi.CollatorProvider;
import java.text.spi.DateFormatProvider;
import java.text.spi.DateFormatSymbolsProvider;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.text.spi.NumberFormatProvider;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CalendarNameProvider;
import java.util.spi.CurrencyNameProvider;
import java.util.spi.LocaleNameProvider;
import java.util.spi.LocaleServiceProvider;
import java.util.spi.TimeZoneNameProvider;
import javafx.fxml.FXMLLoader;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter.class */
public class SPILocaleProviderAdapter extends AuxLocaleProviderAdapter {

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$Delegate.class */
    interface Delegate<P extends LocaleServiceProvider> {
        void addImpl(P p2);

        P getImpl(Locale locale);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleProviderAdapter.Type getAdapterType() {
        return LocaleProviderAdapter.Type.SPI;
    }

    @Override // sun.util.locale.provider.AuxLocaleProviderAdapter
    protected <P extends LocaleServiceProvider> P findInstalledProvider(final Class<P> cls) {
        try {
            return (P) AccessController.doPrivileged(new PrivilegedExceptionAction<P>() { // from class: sun.util.locale.provider.SPILocaleProviderAdapter.1
                /* JADX WARN: Incorrect return type in method signature: ()TP; */
                @Override // java.security.PrivilegedExceptionAction
                public LocaleServiceProvider run() {
                    LocaleServiceProvider localeServiceProvider = null;
                    Iterator it = ServiceLoader.loadInstalled(cls).iterator();
                    while (it.hasNext()) {
                        LocaleServiceProvider localeServiceProvider2 = (LocaleServiceProvider) it.next();
                        if (localeServiceProvider == null) {
                            try {
                                localeServiceProvider = (LocaleServiceProvider) Class.forName(SPILocaleProviderAdapter.class.getCanonicalName() + FXMLLoader.EXPRESSION_PREFIX + cls.getSimpleName() + "Delegate").newInstance();
                            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
                                LocaleServiceProviderPool.config(SPILocaleProviderAdapter.class, e2.toString());
                                return null;
                            }
                        }
                        ((Delegate) localeServiceProvider).addImpl(localeServiceProvider2);
                    }
                    return localeServiceProvider;
                }
            });
        } catch (PrivilegedActionException e2) {
            LocaleServiceProviderPool.config(SPILocaleProviderAdapter.class, e2.toString());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <P extends LocaleServiceProvider> P getImpl(Map<Locale, P> map, Locale locale) {
        Iterator<Locale> it = LocaleServiceProviderPool.getLookupLocales(locale).iterator();
        while (it.hasNext()) {
            P p2 = map.get(it.next());
            if (p2 != null) {
                return p2;
            }
        }
        return null;
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$BreakIteratorProviderDelegate.class */
    static class BreakIteratorProviderDelegate extends BreakIteratorProvider implements Delegate<BreakIteratorProvider> {
        private ConcurrentMap<Locale, BreakIteratorProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        BreakIteratorProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(BreakIteratorProvider breakIteratorProvider) {
            for (Locale locale : breakIteratorProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, breakIteratorProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public BreakIteratorProvider getImpl(Locale locale) {
            return (BreakIteratorProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.text.spi.BreakIteratorProvider
        public BreakIterator getWordInstance(Locale locale) {
            BreakIteratorProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getWordInstance(locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.BreakIteratorProvider
        public BreakIterator getLineInstance(Locale locale) {
            BreakIteratorProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getLineInstance(locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.BreakIteratorProvider
        public BreakIterator getCharacterInstance(Locale locale) {
            BreakIteratorProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getCharacterInstance(locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.BreakIteratorProvider
        public BreakIterator getSentenceInstance(Locale locale) {
            BreakIteratorProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getSentenceInstance(locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$CollatorProviderDelegate.class */
    static class CollatorProviderDelegate extends CollatorProvider implements Delegate<CollatorProvider> {
        private ConcurrentMap<Locale, CollatorProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        CollatorProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(CollatorProvider collatorProvider) {
            for (Locale locale : collatorProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, collatorProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public CollatorProvider getImpl(Locale locale) {
            return (CollatorProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.text.spi.CollatorProvider
        public Collator getInstance(Locale locale) {
            CollatorProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getInstance(locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$DateFormatProviderDelegate.class */
    static class DateFormatProviderDelegate extends DateFormatProvider implements Delegate<DateFormatProvider> {
        private ConcurrentMap<Locale, DateFormatProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        DateFormatProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(DateFormatProvider dateFormatProvider) {
            for (Locale locale : dateFormatProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, dateFormatProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public DateFormatProvider getImpl(Locale locale) {
            return (DateFormatProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.text.spi.DateFormatProvider
        public DateFormat getTimeInstance(int i2, Locale locale) {
            DateFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getTimeInstance(i2, locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.DateFormatProvider
        public DateFormat getDateInstance(int i2, Locale locale) {
            DateFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDateInstance(i2, locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.DateFormatProvider
        public DateFormat getDateTimeInstance(int i2, int i3, Locale locale) {
            DateFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDateTimeInstance(i2, i3, locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$DateFormatSymbolsProviderDelegate.class */
    static class DateFormatSymbolsProviderDelegate extends DateFormatSymbolsProvider implements Delegate<DateFormatSymbolsProvider> {
        private ConcurrentMap<Locale, DateFormatSymbolsProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        DateFormatSymbolsProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(DateFormatSymbolsProvider dateFormatSymbolsProvider) {
            for (Locale locale : dateFormatSymbolsProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, dateFormatSymbolsProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public DateFormatSymbolsProvider getImpl(Locale locale) {
            return (DateFormatSymbolsProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.text.spi.DateFormatSymbolsProvider
        public DateFormatSymbols getInstance(Locale locale) {
            DateFormatSymbolsProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getInstance(locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$DecimalFormatSymbolsProviderDelegate.class */
    static class DecimalFormatSymbolsProviderDelegate extends DecimalFormatSymbolsProvider implements Delegate<DecimalFormatSymbolsProvider> {
        private ConcurrentMap<Locale, DecimalFormatSymbolsProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        DecimalFormatSymbolsProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(DecimalFormatSymbolsProvider decimalFormatSymbolsProvider) {
            for (Locale locale : decimalFormatSymbolsProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, decimalFormatSymbolsProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public DecimalFormatSymbolsProvider getImpl(Locale locale) {
            return (DecimalFormatSymbolsProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.text.spi.DecimalFormatSymbolsProvider
        public DecimalFormatSymbols getInstance(Locale locale) {
            DecimalFormatSymbolsProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getInstance(locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$NumberFormatProviderDelegate.class */
    static class NumberFormatProviderDelegate extends NumberFormatProvider implements Delegate<NumberFormatProvider> {
        private ConcurrentMap<Locale, NumberFormatProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        NumberFormatProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(NumberFormatProvider numberFormatProvider) {
            for (Locale locale : numberFormatProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, numberFormatProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public NumberFormatProvider getImpl(Locale locale) {
            return (NumberFormatProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.text.spi.NumberFormatProvider
        public NumberFormat getCurrencyInstance(Locale locale) {
            NumberFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getCurrencyInstance(locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.NumberFormatProvider
        public NumberFormat getIntegerInstance(Locale locale) {
            NumberFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getIntegerInstance(locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.NumberFormatProvider
        public NumberFormat getNumberInstance(Locale locale) {
            NumberFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getNumberInstance(locale);
            }
            throw new AssertionError();
        }

        @Override // java.text.spi.NumberFormatProvider
        public NumberFormat getPercentInstance(Locale locale) {
            NumberFormatProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getPercentInstance(locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$CalendarDataProviderDelegate.class */
    static class CalendarDataProviderDelegate extends CalendarDataProvider implements Delegate<CalendarDataProvider> {
        private ConcurrentMap<Locale, CalendarDataProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        CalendarDataProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(CalendarDataProvider calendarDataProvider) {
            for (Locale locale : calendarDataProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, calendarDataProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public CalendarDataProvider getImpl(Locale locale) {
            return (CalendarDataProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.util.spi.CalendarDataProvider
        public int getFirstDayOfWeek(Locale locale) {
            CalendarDataProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getFirstDayOfWeek(locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.CalendarDataProvider
        public int getMinimalDaysInFirstWeek(Locale locale) {
            CalendarDataProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getMinimalDaysInFirstWeek(locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$CalendarNameProviderDelegate.class */
    static class CalendarNameProviderDelegate extends CalendarNameProvider implements Delegate<CalendarNameProvider> {
        private ConcurrentMap<Locale, CalendarNameProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        CalendarNameProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(CalendarNameProvider calendarNameProvider) {
            for (Locale locale : calendarNameProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, calendarNameProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public CalendarNameProvider getImpl(Locale locale) {
            return (CalendarNameProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.util.spi.CalendarNameProvider
        public String getDisplayName(String str, int i2, int i3, int i4, Locale locale) {
            CalendarNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayName(str, i2, i3, i4, locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.CalendarNameProvider
        public Map<String, Integer> getDisplayNames(String str, int i2, int i3, Locale locale) {
            CalendarNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayNames(str, i2, i3, locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$CurrencyNameProviderDelegate.class */
    static class CurrencyNameProviderDelegate extends CurrencyNameProvider implements Delegate<CurrencyNameProvider> {
        private ConcurrentMap<Locale, CurrencyNameProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        CurrencyNameProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(CurrencyNameProvider currencyNameProvider) {
            for (Locale locale : currencyNameProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, currencyNameProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public CurrencyNameProvider getImpl(Locale locale) {
            return (CurrencyNameProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.util.spi.CurrencyNameProvider
        public String getSymbol(String str, Locale locale) {
            CurrencyNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getSymbol(str, locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.CurrencyNameProvider
        public String getDisplayName(String str, Locale locale) {
            CurrencyNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayName(str, locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$LocaleNameProviderDelegate.class */
    static class LocaleNameProviderDelegate extends LocaleNameProvider implements Delegate<LocaleNameProvider> {
        private ConcurrentMap<Locale, LocaleNameProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        LocaleNameProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(LocaleNameProvider localeNameProvider) {
            for (Locale locale : localeNameProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, localeNameProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public LocaleNameProvider getImpl(Locale locale) {
            return (LocaleNameProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.util.spi.LocaleNameProvider
        public String getDisplayLanguage(String str, Locale locale) {
            LocaleNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayLanguage(str, locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.LocaleNameProvider
        public String getDisplayScript(String str, Locale locale) {
            LocaleNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayScript(str, locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.LocaleNameProvider
        public String getDisplayCountry(String str, Locale locale) {
            LocaleNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayCountry(str, locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.LocaleNameProvider
        public String getDisplayVariant(String str, Locale locale) {
            LocaleNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayVariant(str, locale);
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/util/locale/provider/SPILocaleProviderAdapter$TimeZoneNameProviderDelegate.class */
    static class TimeZoneNameProviderDelegate extends TimeZoneNameProvider implements Delegate<TimeZoneNameProvider> {
        private ConcurrentMap<Locale, TimeZoneNameProvider> map = new ConcurrentHashMap();
        static final /* synthetic */ boolean $assertionsDisabled;

        TimeZoneNameProviderDelegate() {
        }

        static {
            $assertionsDisabled = !SPILocaleProviderAdapter.class.desiredAssertionStatus();
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public void addImpl(TimeZoneNameProvider timeZoneNameProvider) {
            for (Locale locale : timeZoneNameProvider.getAvailableLocales()) {
                this.map.putIfAbsent(locale, timeZoneNameProvider);
            }
        }

        @Override // sun.util.locale.provider.SPILocaleProviderAdapter.Delegate
        public TimeZoneNameProvider getImpl(Locale locale) {
            return (TimeZoneNameProvider) SPILocaleProviderAdapter.getImpl(this.map, locale);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return (Locale[]) this.map.keySet().toArray(new Locale[0]);
        }

        @Override // java.util.spi.LocaleServiceProvider
        public boolean isSupportedLocale(Locale locale) {
            return this.map.containsKey(locale);
        }

        @Override // java.util.spi.TimeZoneNameProvider
        public String getDisplayName(String str, boolean z2, int i2, Locale locale) {
            TimeZoneNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getDisplayName(str, z2, i2, locale);
            }
            throw new AssertionError();
        }

        @Override // java.util.spi.TimeZoneNameProvider
        public String getGenericDisplayName(String str, int i2, Locale locale) {
            TimeZoneNameProvider impl = getImpl(locale);
            if ($assertionsDisabled || impl != null) {
                return impl.getGenericDisplayName(str, i2, locale);
            }
            throw new AssertionError();
        }
    }
}
