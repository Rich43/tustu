package sun.util.locale.provider;

import java.text.spi.BreakIteratorProvider;
import java.text.spi.CollatorProvider;
import java.text.spi.DateFormatProvider;
import java.text.spi.DateFormatSymbolsProvider;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.text.spi.NumberFormatProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CalendarNameProvider;
import java.util.spi.CurrencyNameProvider;
import java.util.spi.LocaleNameProvider;
import java.util.spi.LocaleServiceProvider;
import java.util.spi.TimeZoneNameProvider;
import sun.util.spi.CalendarProvider;

/* loaded from: rt.jar:sun/util/locale/provider/AuxLocaleProviderAdapter.class */
public abstract class AuxLocaleProviderAdapter extends LocaleProviderAdapter {
    private ConcurrentMap<Class<? extends LocaleServiceProvider>, LocaleServiceProvider> providersMap = new ConcurrentHashMap();
    private static Locale[] availableLocales = null;
    private static NullProvider NULL_PROVIDER = new NullProvider();

    protected abstract <P extends LocaleServiceProvider> P findInstalledProvider(Class<P> cls);

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public <P extends LocaleServiceProvider> P getLocaleServiceProvider(Class<P> cls) {
        LocaleServiceProvider localeServiceProviderFindInstalledProvider = this.providersMap.get(cls);
        if (localeServiceProviderFindInstalledProvider == null) {
            localeServiceProviderFindInstalledProvider = findInstalledProvider(cls);
            this.providersMap.putIfAbsent(cls, localeServiceProviderFindInstalledProvider == null ? NULL_PROVIDER : localeServiceProviderFindInstalledProvider);
        }
        return (P) localeServiceProviderFindInstalledProvider;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public BreakIteratorProvider getBreakIteratorProvider() {
        return (BreakIteratorProvider) getLocaleServiceProvider(BreakIteratorProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CollatorProvider getCollatorProvider() {
        return (CollatorProvider) getLocaleServiceProvider(CollatorProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public DateFormatProvider getDateFormatProvider() {
        return (DateFormatProvider) getLocaleServiceProvider(DateFormatProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public DateFormatSymbolsProvider getDateFormatSymbolsProvider() {
        return (DateFormatSymbolsProvider) getLocaleServiceProvider(DateFormatSymbolsProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public DecimalFormatSymbolsProvider getDecimalFormatSymbolsProvider() {
        return (DecimalFormatSymbolsProvider) getLocaleServiceProvider(DecimalFormatSymbolsProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public NumberFormatProvider getNumberFormatProvider() {
        return (NumberFormatProvider) getLocaleServiceProvider(NumberFormatProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CurrencyNameProvider getCurrencyNameProvider() {
        return (CurrencyNameProvider) getLocaleServiceProvider(CurrencyNameProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleNameProvider getLocaleNameProvider() {
        return (LocaleNameProvider) getLocaleServiceProvider(LocaleNameProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public TimeZoneNameProvider getTimeZoneNameProvider() {
        return (TimeZoneNameProvider) getLocaleServiceProvider(TimeZoneNameProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CalendarDataProvider getCalendarDataProvider() {
        return (CalendarDataProvider) getLocaleServiceProvider(CalendarDataProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CalendarNameProvider getCalendarNameProvider() {
        return (CalendarNameProvider) getLocaleServiceProvider(CalendarNameProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public CalendarProvider getCalendarProvider() {
        return (CalendarProvider) getLocaleServiceProvider(CalendarProvider.class);
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleResources getLocaleResources(Locale locale) {
        return null;
    }

    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public Locale[] getAvailableLocales() {
        if (availableLocales == null) {
            HashSet hashSet = new HashSet();
            for (Class<LocaleServiceProvider> cls : LocaleServiceProviderPool.spiClasses) {
                LocaleServiceProvider localeServiceProvider = getLocaleServiceProvider(cls);
                if (localeServiceProvider != null) {
                    hashSet.addAll(Arrays.asList(localeServiceProvider.getAvailableLocales()));
                }
            }
            availableLocales = (Locale[]) hashSet.toArray(new Locale[0]);
        }
        return availableLocales;
    }

    /* loaded from: rt.jar:sun/util/locale/provider/AuxLocaleProviderAdapter$NullProvider.class */
    private static class NullProvider extends LocaleServiceProvider {
        private NullProvider() {
        }

        @Override // java.util.spi.LocaleServiceProvider
        public Locale[] getAvailableLocales() {
            return new Locale[0];
        }
    }
}
