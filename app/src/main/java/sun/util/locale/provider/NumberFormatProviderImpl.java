package sun.util.locale.provider;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/NumberFormatProviderImpl.class */
public class NumberFormatProviderImpl extends NumberFormatProvider implements AvailableLanguageTags {
    private static final int NUMBERSTYLE = 0;
    private static final int CURRENCYSTYLE = 1;
    private static final int PERCENTSTYLE = 2;
    private static final int SCIENTIFICSTYLE = 3;
    private static final int INTEGERSTYLE = 4;
    private final LocaleProviderAdapter.Type type;
    private final Set<String> langtags;

    public NumberFormatProviderImpl(LocaleProviderAdapter.Type type, Set<String> set) {
        this.type = type;
        this.langtags = set;
    }

    @Override // java.util.spi.LocaleServiceProvider
    public Locale[] getAvailableLocales() {
        return LocaleProviderAdapter.forType(this.type).getAvailableLocales();
    }

    @Override // java.util.spi.LocaleServiceProvider
    public boolean isSupportedLocale(Locale locale) {
        return LocaleProviderAdapter.isSupportedLocale(locale, this.type, this.langtags);
    }

    @Override // java.text.spi.NumberFormatProvider
    public NumberFormat getCurrencyInstance(Locale locale) {
        return getInstance(locale, 1);
    }

    @Override // java.text.spi.NumberFormatProvider
    public NumberFormat getIntegerInstance(Locale locale) {
        return getInstance(locale, 4);
    }

    @Override // java.text.spi.NumberFormatProvider
    public NumberFormat getNumberInstance(Locale locale) {
        return getInstance(locale, 0);
    }

    @Override // java.text.spi.NumberFormatProvider
    public NumberFormat getPercentInstance(Locale locale) {
        return getInstance(locale, 2);
    }

    private NumberFormat getInstance(Locale locale, int i2) {
        if (locale == null) {
            throw new NullPointerException();
        }
        String[] numberPatterns = LocaleProviderAdapter.forType(this.type).getLocaleResources(locale).getNumberPatterns();
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);
        DecimalFormat decimalFormat = new DecimalFormat(numberPatterns[i2 == 4 ? 0 : i2], decimalFormatSymbols);
        if (i2 == 4) {
            decimalFormat.setMaximumFractionDigits(0);
            decimalFormat.setDecimalSeparatorAlwaysShown(false);
            decimalFormat.setParseIntegerOnly(true);
        } else if (i2 == 1) {
            adjustForCurrencyDefaultFractionDigits(decimalFormat, decimalFormatSymbols);
        }
        return decimalFormat;
    }

    private static void adjustForCurrencyDefaultFractionDigits(DecimalFormat decimalFormat, DecimalFormatSymbols decimalFormatSymbols) {
        int defaultFractionDigits;
        Currency currency = decimalFormatSymbols.getCurrency();
        if (currency == null) {
            try {
                currency = Currency.getInstance(decimalFormatSymbols.getInternationalCurrencySymbol());
            } catch (IllegalArgumentException e2) {
            }
        }
        if (currency != null && (defaultFractionDigits = currency.getDefaultFractionDigits()) != -1) {
            int minimumFractionDigits = decimalFormat.getMinimumFractionDigits();
            if (minimumFractionDigits == decimalFormat.getMaximumFractionDigits()) {
                decimalFormat.setMinimumFractionDigits(defaultFractionDigits);
                decimalFormat.setMaximumFractionDigits(defaultFractionDigits);
            } else {
                decimalFormat.setMinimumFractionDigits(Math.min(defaultFractionDigits, minimumFractionDigits));
                decimalFormat.setMaximumFractionDigits(defaultFractionDigits);
            }
        }
    }

    @Override // sun.util.locale.provider.AvailableLanguageTags
    public Set<String> getAvailableLanguageTags() {
        return this.langtags;
    }
}
