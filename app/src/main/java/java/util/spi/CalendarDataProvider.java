package java.util.spi;

import java.util.Locale;

/* loaded from: rt.jar:java/util/spi/CalendarDataProvider.class */
public abstract class CalendarDataProvider extends LocaleServiceProvider {
    public abstract int getFirstDayOfWeek(Locale locale);

    public abstract int getMinimalDaysInFirstWeek(Locale locale);

    protected CalendarDataProvider() {
    }
}
