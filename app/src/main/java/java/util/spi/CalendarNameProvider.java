package java.util.spi;

import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:java/util/spi/CalendarNameProvider.class */
public abstract class CalendarNameProvider extends LocaleServiceProvider {
    public abstract String getDisplayName(String str, int i2, int i3, int i4, Locale locale);

    public abstract Map<String, Integer> getDisplayNames(String str, int i2, int i3, Locale locale);

    protected CalendarNameProvider() {
    }
}
