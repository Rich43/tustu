package sun.util.locale.provider;

import java.text.spi.BreakIteratorProvider;
import java.text.spi.CollatorProvider;
import java.text.spi.DateFormatProvider;
import java.text.spi.DateFormatSymbolsProvider;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.text.spi.NumberFormatProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IllformedLocaleException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.spi.CalendarDataProvider;
import java.util.spi.CurrencyNameProvider;
import java.util.spi.LocaleNameProvider;
import java.util.spi.LocaleServiceProvider;
import java.util.spi.TimeZoneNameProvider;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/util/locale/provider/LocaleServiceProviderPool.class */
public final class LocaleServiceProviderPool {
    private ConcurrentMap<LocaleProviderAdapter.Type, LocaleServiceProvider> providers = new ConcurrentHashMap();
    private ConcurrentMap<Locale, List<LocaleProviderAdapter.Type>> providersCache = new ConcurrentHashMap();
    private Set<Locale> availableLocales = null;
    private Class<? extends LocaleServiceProvider> providerClass;
    private static ConcurrentMap<Class<? extends LocaleServiceProvider>, LocaleServiceProviderPool> poolOfPools = new ConcurrentHashMap();
    static final Class<LocaleServiceProvider>[] spiClasses = {BreakIteratorProvider.class, CollatorProvider.class, DateFormatProvider.class, DateFormatSymbolsProvider.class, DecimalFormatSymbolsProvider.class, NumberFormatProvider.class, CurrencyNameProvider.class, LocaleNameProvider.class, TimeZoneNameProvider.class, CalendarDataProvider.class};
    private static List<LocaleProviderAdapter.Type> NULL_LIST = Collections.emptyList();

    /* loaded from: rt.jar:sun/util/locale/provider/LocaleServiceProviderPool$LocalizedObjectGetter.class */
    public interface LocalizedObjectGetter<P extends LocaleServiceProvider, S> {
        S getObject(P p2, Locale locale, String str, Object... objArr);
    }

    public static LocaleServiceProviderPool getPool(Class<? extends LocaleServiceProvider> cls) {
        LocaleServiceProviderPool localeServiceProviderPoolPutIfAbsent = poolOfPools.get(cls);
        if (localeServiceProviderPoolPutIfAbsent == null) {
            LocaleServiceProviderPool localeServiceProviderPool = new LocaleServiceProviderPool(cls);
            localeServiceProviderPoolPutIfAbsent = poolOfPools.putIfAbsent(cls, localeServiceProviderPool);
            if (localeServiceProviderPoolPutIfAbsent == null) {
                localeServiceProviderPoolPutIfAbsent = localeServiceProviderPool;
            }
        }
        return localeServiceProviderPoolPutIfAbsent;
    }

    private LocaleServiceProviderPool(Class<? extends LocaleServiceProvider> cls) {
        LocaleServiceProvider localeServiceProvider;
        this.providerClass = cls;
        for (LocaleProviderAdapter.Type type : LocaleProviderAdapter.getAdapterPreference()) {
            LocaleProviderAdapter localeProviderAdapterForType = LocaleProviderAdapter.forType(type);
            if (localeProviderAdapterForType != null && (localeServiceProvider = localeProviderAdapterForType.getLocaleServiceProvider(cls)) != null) {
                this.providers.putIfAbsent(type, localeServiceProvider);
            }
        }
    }

    static void config(Class<? extends Object> cls, String str) {
        PlatformLogger.getLogger(cls.getCanonicalName()).config(str);
    }

    /* loaded from: rt.jar:sun/util/locale/provider/LocaleServiceProviderPool$AllAvailableLocales.class */
    private static class AllAvailableLocales {
        static final Locale[] allAvailableLocales;

        static {
            HashSet hashSet = new HashSet();
            for (Class<LocaleServiceProvider> cls : LocaleServiceProviderPool.spiClasses) {
                hashSet.addAll(LocaleServiceProviderPool.getPool(cls).getAvailableLocaleSet());
            }
            allAvailableLocales = (Locale[]) hashSet.toArray(new Locale[0]);
        }

        private AllAvailableLocales() {
        }
    }

    public static Locale[] getAllAvailableLocales() {
        return (Locale[]) AllAvailableLocales.allAvailableLocales.clone();
    }

    public Locale[] getAvailableLocales() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(getAvailableLocaleSet());
        hashSet.addAll(Arrays.asList(LocaleProviderAdapter.forJRE().getAvailableLocales()));
        Locale[] localeArr = new Locale[hashSet.size()];
        hashSet.toArray(localeArr);
        return localeArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Set<Locale> getAvailableLocaleSet() {
        if (this.availableLocales == null) {
            this.availableLocales = new HashSet();
            Iterator<LocaleServiceProvider> it = this.providers.values().iterator();
            while (it.hasNext()) {
                for (Locale locale : it.next().getAvailableLocales()) {
                    this.availableLocales.add(getLookupLocale(locale));
                }
            }
        }
        return this.availableLocales;
    }

    boolean hasProviders() {
        return this.providers.size() != 1 || (this.providers.get(LocaleProviderAdapter.Type.JRE) == null && this.providers.get(LocaleProviderAdapter.Type.FALLBACK) == null);
    }

    public <P extends LocaleServiceProvider, S> S getLocalizedObject(LocalizedObjectGetter<P, S> localizedObjectGetter, Locale locale, Object... objArr) {
        return (S) getLocalizedObjectImpl(localizedObjectGetter, locale, true, null, objArr);
    }

    public <P extends LocaleServiceProvider, S> S getLocalizedObject(LocalizedObjectGetter<P, S> localizedObjectGetter, Locale locale, String str, Object... objArr) {
        return (S) getLocalizedObjectImpl(localizedObjectGetter, locale, false, str, objArr);
    }

    private <P extends LocaleServiceProvider, S> S getLocalizedObjectImpl(LocalizedObjectGetter<P, S> localizedObjectGetter, Locale locale, boolean z2, String str, Object... objArr) {
        if (locale == null) {
            throw new NullPointerException();
        }
        if (!hasProviders()) {
            return localizedObjectGetter.getObject(this.providers.get(LocaleProviderAdapter.defaultLocaleProviderAdapter), locale, str, objArr);
        }
        List<Locale> lookupLocales = getLookupLocales(locale);
        Set<Locale> availableLocaleSet = getAvailableLocaleSet();
        for (Locale locale2 : lookupLocales) {
            if (availableLocaleSet.contains(locale2)) {
                Iterator<LocaleProviderAdapter.Type> it = findProviders(locale2).iterator();
                while (it.hasNext()) {
                    LocaleServiceProvider localeServiceProvider = this.providers.get(it.next());
                    S object = localizedObjectGetter.getObject(localeServiceProvider, locale, str, objArr);
                    if (object != null) {
                        return object;
                    }
                    if (z2) {
                        config(LocaleServiceProviderPool.class, "A locale sensitive service provider returned null for a localized objects,  which should not happen.  provider: " + ((Object) localeServiceProvider) + " locale: " + ((Object) locale));
                    }
                }
            }
        }
        return null;
    }

    private List<LocaleProviderAdapter.Type> findProviders(Locale locale) {
        List<LocaleProviderAdapter.Type> arrayList = this.providersCache.get(locale);
        if (arrayList == null) {
            for (LocaleProviderAdapter.Type type : LocaleProviderAdapter.getAdapterPreference()) {
                LocaleServiceProvider localeServiceProvider = this.providers.get(type);
                if (localeServiceProvider != null && localeServiceProvider.isSupportedLocale(locale)) {
                    if (arrayList == null) {
                        arrayList = new ArrayList(2);
                    }
                    arrayList.add(type);
                }
            }
            if (arrayList == null) {
                arrayList = NULL_LIST;
            }
            List<LocaleProviderAdapter.Type> listPutIfAbsent = this.providersCache.putIfAbsent(locale, arrayList);
            if (listPutIfAbsent != null) {
                arrayList = listPutIfAbsent;
            }
        }
        return arrayList;
    }

    static List<Locale> getLookupLocales(Locale locale) {
        return ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT).getCandidateLocales("", locale);
    }

    static Locale getLookupLocale(Locale locale) {
        Locale locale2 = locale;
        if (locale.hasExtensions() && !locale.equals(JRELocaleConstants.JA_JP_JP) && !locale.equals(JRELocaleConstants.TH_TH_TH)) {
            Locale.Builder builder = new Locale.Builder();
            try {
                builder.setLocale(locale);
                builder.clearExtensions();
                locale2 = builder.build();
            } catch (IllformedLocaleException e2) {
                config(LocaleServiceProviderPool.class, "A locale(" + ((Object) locale) + ") has non-empty extensions, but has illformed fields.");
                locale2 = new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant());
            }
        }
        return locale2;
    }
}
