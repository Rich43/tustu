package sun.util.locale.provider;

import java.util.Locale;
import java.util.Set;
import java.util.spi.LocaleNameProvider;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/LocaleNameProviderImpl.class */
public class LocaleNameProviderImpl extends LocaleNameProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public LocaleNameProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
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

    @Override // java.util.spi.LocaleNameProvider
    public String getDisplayLanguage(String str, Locale locale) {
        return getDisplayString(str, locale);
    }

    @Override // java.util.spi.LocaleNameProvider
    public String getDisplayScript(String str, Locale locale) {
        return getDisplayString(str, locale);
    }

    @Override // java.util.spi.LocaleNameProvider
    public String getDisplayCountry(String str, Locale locale) {
        return getDisplayString(str, locale);
    }

    @Override // java.util.spi.LocaleNameProvider
    public String getDisplayVariant(String str, Locale locale) {
        return getDisplayString("%%" + str, locale);
    }

    private String getDisplayString(String str, Locale locale) {
        if (str == null || locale == null) {
            throw new NullPointerException();
        }
        return LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getLocaleName(str);
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }
}
