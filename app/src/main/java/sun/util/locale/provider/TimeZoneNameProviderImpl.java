package sun.util.locale.provider;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.spi.TimeZoneNameProvider;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/TimeZoneNameProviderImpl.class */
public class TimeZoneNameProviderImpl extends TimeZoneNameProvider {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TimeZoneNameProviderImpl.class.desiredAssertionStatus();
    }

    TimeZoneNameProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
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

    @Override // java.util.spi.TimeZoneNameProvider
    public String getDisplayName(String str, boolean z2, int i2, Locale locale) {
        String[] displayNameArray = getDisplayNameArray(str, locale);
        if (Objects.nonNull(displayNameArray)) {
            if (!$assertionsDisabled && displayNameArray.length < 7) {
                throw new AssertionError();
            }
            int i3 = z2 ? 3 : 1;
            if (i2 == 0) {
                i3++;
            }
            return displayNameArray[i3];
        }
        return null;
    }

    @Override // java.util.spi.TimeZoneNameProvider
    public String getGenericDisplayName(String str, int i2, Locale locale) {
        String[] displayNameArray = getDisplayNameArray(str, locale);
        if (Objects.nonNull(displayNameArray)) {
            if ($assertionsDisabled || displayNameArray.length >= 7) {
                return displayNameArray[i2 == 1 ? (char) 5 : (char) 6];
            }
            throw new AssertionError();
        }
        return null;
    }

    private String[] getDisplayNameArray(String str, Locale locale) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(locale);
        return LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getTimeZoneNames(str);
    }

    String[][] getZoneStrings(Locale locale) {
        return LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getZoneStrings();
    }
}
