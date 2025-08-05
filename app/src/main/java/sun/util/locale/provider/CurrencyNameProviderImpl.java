package sun.util.locale.provider;

import java.util.Locale;
import java.util.Set;
import java.util.spi.CurrencyNameProvider;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/CurrencyNameProviderImpl.class */
public class CurrencyNameProviderImpl extends CurrencyNameProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public CurrencyNameProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(this.langtags);
    }

    @Override // java.util.spi.CurrencyNameProvider
    public String getSymbol(String str, Locale locale) {
        return getString(str.toUpperCase(Locale.ROOT), locale);
    }

    @Override // java.util.spi.CurrencyNameProvider
    public String getDisplayName(String str, Locale locale) {
        return getString(str.toLowerCase(Locale.ROOT), locale);
    }

    private String getString(String str, Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        return LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getCurrencyName(str);
    }
}
