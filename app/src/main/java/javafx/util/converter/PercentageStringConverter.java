package javafx.util.converter;

import java.text.NumberFormat;
import java.util.Locale;

/* loaded from: jfxrt.jar:javafx/util/converter/PercentageStringConverter.class */
public class PercentageStringConverter extends NumberStringConverter {
    public PercentageStringConverter() {
        this(Locale.getDefault());
    }

    public PercentageStringConverter(Locale locale) {
        super(locale, null, null);
    }

    public PercentageStringConverter(NumberFormat numberFormat) {
        super(null, null, numberFormat);
    }

    @Override // javafx.util.converter.NumberStringConverter
    public NumberFormat getNumberFormat() {
        Locale _locale = this.locale == null ? Locale.getDefault() : this.locale;
        if (this.numberFormat != null) {
            return this.numberFormat;
        }
        return NumberFormat.getPercentInstance(_locale);
    }
}
