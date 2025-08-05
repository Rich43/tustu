package javafx.util.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/DateTimeStringConverter.class */
public class DateTimeStringConverter extends StringConverter<Date> {
    protected final Locale locale;
    protected final String pattern;
    protected final DateFormat dateFormat;
    protected final int dateStyle;
    protected final int timeStyle;

    public DateTimeStringConverter() {
        this(null, null, null, 2, 2);
    }

    public DateTimeStringConverter(int dateStyle, int timeStyle) {
        this(null, null, null, dateStyle, timeStyle);
    }

    public DateTimeStringConverter(Locale locale) {
        this(locale, null, null, 2, 2);
    }

    public DateTimeStringConverter(Locale locale, int dateStyle, int timeStyle) {
        this(locale, null, null, dateStyle, timeStyle);
    }

    public DateTimeStringConverter(String pattern) {
        this(null, pattern, null, 2, 2);
    }

    public DateTimeStringConverter(Locale locale, String pattern) {
        this(locale, pattern, null, 2, 2);
    }

    public DateTimeStringConverter(DateFormat dateFormat) {
        this(null, null, dateFormat, 2, 2);
    }

    DateTimeStringConverter(Locale locale, String pattern, DateFormat dateFormat, int dateStyle, int timeStyle) {
        this.locale = locale != null ? locale : Locale.getDefault(Locale.Category.FORMAT);
        this.pattern = pattern;
        this.dateFormat = dateFormat;
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public Date fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            String value2 = value.trim();
            if (value2.length() < 1) {
                return null;
            }
            DateFormat parser = getDateFormat();
            return parser.parse(value2);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override // javafx.util.StringConverter
    public String toString(Date value) {
        if (value == null) {
            return "";
        }
        DateFormat formatter = getDateFormat();
        return formatter.format(value);
    }

    protected DateFormat getDateFormat() {
        DateFormat df;
        if (this.dateFormat != null) {
            return this.dateFormat;
        }
        if (this.pattern != null) {
            df = new SimpleDateFormat(this.pattern, this.locale);
        } else {
            df = DateFormat.getDateTimeInstance(this.dateStyle, this.timeStyle, this.locale);
        }
        df.setLenient(false);
        return df;
    }
}
