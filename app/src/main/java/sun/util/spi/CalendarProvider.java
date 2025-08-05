package sun.util.spi;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.spi.LocaleServiceProvider;

/* loaded from: rt.jar:sun/util/spi/CalendarProvider.class */
public abstract class CalendarProvider extends LocaleServiceProvider {
    public abstract Calendar getInstance(TimeZone timeZone, Locale locale);

    protected CalendarProvider() {
    }
}
