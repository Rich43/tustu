package sun.util.locale.provider;

import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Locale;
import java.util.Set;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/DateFormatSymbolsProviderImpl.class */
public class DateFormatSymbolsProviderImpl extends DateFormatSymbolsProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public DateFormatSymbolsProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(this.langtags);
    }

    @Override // java.util.spi.LocaleServiceProvider
    public boolean isSupportedLocale(Locale locale) {
        return LocaleProviderAdapter.isSupportedLocale(locale, this.type, this.langtags);
    }

    @Override // java.text.spi.DateFormatSymbolsProvider
    public DateFormatSymbols getInstance(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        return new DateFormatSymbols(locale);
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }
}
