package sun.util.locale.provider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/DateFormatProviderImpl.class */
public class DateFormatProviderImpl extends DateFormatProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public DateFormatProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
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

    @Override // java.text.spi.DateFormatProvider
    public DateFormat getTimeInstance(int i2, Locale locale) {
        return getInstance(-1, i2, locale);
    }

    @Override // java.text.spi.DateFormatProvider
    public DateFormat getDateInstance(int i2, Locale locale) {
        return getInstance(i2, -1, locale);
    }

    @Override // java.text.spi.DateFormatProvider
    public DateFormat getDateTimeInstance(int i2, int i3, Locale locale) {
        return getInstance(i2, i3, locale);
    }

    private DateFormat getInstance(int i2, int i3, Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("", locale);
        try {
            simpleDateFormat.applyPattern(LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getDateTimePattern(i3, i2, simpleDateFormat.getCalendar()));
        } catch (MissingResourceException e2) {
            simpleDateFormat.applyPattern("M/d/yy h:mm a");
        }
        return simpleDateFormat;
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }
}
