package sun.util.locale.provider;

import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.spi.CalendarProvider;

/* loaded from: rt.jar:sun/util/locale/provider/CalendarProviderImpl.class */
public class CalendarProviderImpl extends CalendarProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public CalendarProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(this.langtags);
    }

    @Override // java.util.spi.LocaleServiceProvider
    public boolean isSupportedLocale(Locale locale) {
        return true;
    }

    @Override // sun.util.spi.CalendarProvider
    public Calendar getInstance(TimeZone timeZone, Locale locale) {
        return new Calendar.Builder().setLocale(locale).setTimeZone(timeZone).setInstant(System.currentTimeMillis()).build();
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }
}
