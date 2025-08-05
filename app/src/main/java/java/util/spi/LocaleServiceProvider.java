package java.util.spi;

import java.util.Locale;

/* loaded from: rt.jar:java/util/spi/LocaleServiceProvider.class */
public abstract class LocaleServiceProvider {
    public abstract Locale[] getAvailableLocales();

    protected LocaleServiceProvider() {
    }

    public boolean isSupportedLocale(Locale locale) {
        Locale localeStripExtensions = locale.stripExtensions();
        for (Locale locale2 : getAvailableLocales()) {
            if (localeStripExtensions.equals(locale2.stripExtensions())) {
                return true;
            }
        }
        return false;
    }
}
