package javafx.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/NumberStringConverter.class */
public class NumberStringConverter extends StringConverter<Number> {
    final Locale locale;
    final String pattern;
    final NumberFormat numberFormat;

    public NumberStringConverter() {
        this(Locale.getDefault());
    }

    public NumberStringConverter(Locale locale) {
        this(locale, null);
    }

    public NumberStringConverter(String pattern) {
        this(Locale.getDefault(), pattern);
    }

    public NumberStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null);
    }

    public NumberStringConverter(NumberFormat numberFormat) {
        this(null, null, numberFormat);
    }

    NumberStringConverter(Locale locale, String pattern, NumberFormat numberFormat) {
        this.locale = locale;
        this.pattern = pattern;
        this.numberFormat = numberFormat;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Number fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            String value2 = value.trim();
            if (value2.length() < 1) {
                return null;
            }
            NumberFormat parser = getNumberFormat();
            return parser.parse(value2);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // javafx.util.StringConverter
    public String toString(Number value) {
        if (value == null) {
            return "";
        }
        NumberFormat formatter = getNumberFormat();
        return formatter.format(value);
    }

    protected NumberFormat getNumberFormat() {
        Locale _locale = this.locale == null ? Locale.getDefault() : this.locale;
        if (this.numberFormat != null) {
            return this.numberFormat;
        }
        if (this.pattern != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(_locale);
            return new DecimalFormat(this.pattern, symbols);
        }
        return NumberFormat.getNumberInstance(_locale);
    }
}
