package java.util.spi;

import java.util.Locale;

/* loaded from: rt.jar:java/util/spi/TimeZoneNameProvider.class */
public abstract class TimeZoneNameProvider extends LocaleServiceProvider {
    public abstract String getDisplayName(String str, boolean z2, int i2, Locale locale);

    protected TimeZoneNameProvider() {
    }

    public String getGenericDisplayName(String str, int i2, Locale locale) {
        return null;
    }
}
