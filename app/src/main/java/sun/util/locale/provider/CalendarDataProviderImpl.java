package sun.util.locale.provider;

import java.util.Locale;
import java.util.Set;
import java.util.spi.CalendarDataProvider;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/CalendarDataProviderImpl.class */
public class CalendarDataProviderImpl extends CalendarDataProvider implements AvailableLanguageTags {
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public CalendarDataProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // java.util.spi.CalendarDataProvider
    public int getFirstDayOfWeek(Locale locale) {
        return LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getCalendarData(CalendarDataUtility.FIRST_DAY_OF_WEEK);
    }

    @Override // java.util.spi.CalendarDataProvider
    public int getMinimalDaysInFirstWeek(Locale locale) {
        return LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getCalendarData(CalendarDataUtility.MINIMAL_DAYS_IN_FIRST_WEEK);
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.toLocaleArray(this.langtags);
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }
}
