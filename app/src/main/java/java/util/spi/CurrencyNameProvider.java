package java.util.spi;

import java.util.Locale;
import java.util.ResourceBundle;

/* loaded from: rt.jar:java/util/spi/CurrencyNameProvider.class */
public abstract class CurrencyNameProvider extends LocaleServiceProvider {
    public abstract String getSymbol(String str, Locale locale);

    protected CurrencyNameProvider() {
    }

    public String getDisplayName(String str, Locale locale) {
        if (str == null || locale == null) {
            throw new NullPointerException();
        }
        char[] charArray = str.toCharArray();
        if (charArray.length != 3) {
            throw new IllegalArgumentException("The currencyCode is not in the form of three upper-case letters.");
        }
        for (char c2 : charArray) {
            if (c2 < 'A' || c2 > 'Z') {
                throw new IllegalArgumentException("The currencyCode is not in the form of three upper-case letters.");
            }
        }
        ResourceBundle.Control noFallbackControl = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT);
        for (Locale locale2 : getAvailableLocales()) {
            if (noFallbackControl.getCandidateLocales("", locale2).contains(locale)) {
                return null;
            }
        }
        throw new IllegalArgumentException("The locale is not available");
    }
}
