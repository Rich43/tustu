package javafx.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/* loaded from: jfxrt.jar:javafx/util/converter/CurrencyStringConverter.class */
public class CurrencyStringConverter extends NumberStringConverter {
    public CurrencyStringConverter() {
        this(Locale.getDefault());
    }

    public CurrencyStringConverter(Locale locale) {
        this(locale, null);
    }

    public CurrencyStringConverter(String pattern) {
        this(Locale.getDefault(), pattern);
    }

    public CurrencyStringConverter(Locale locale, String pattern) {
        super(locale, pattern, null);
    }

    public CurrencyStringConverter(NumberFormat numberFormat) {
        super(null, null, numberFormat);
    }

    @Override // javafx.util.converter.NumberStringConverter
    protected NumberFormat getNumberFormat() {
        Locale _locale = this.locale == null ? Locale.getDefault() : this.locale;
        if (this.numberFormat != null) {
            return this.numberFormat;
        }
        if (this.pattern != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(_locale);
            return new DecimalFormat(this.pattern, symbols);
        }
        return NumberFormat.getCurrencyInstance(_locale);
    }
}
