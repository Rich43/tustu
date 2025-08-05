package javafx.util.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/LocalTimeStringConverter.class */
public class LocalTimeStringConverter extends StringConverter<LocalTime> {
    LocalDateTimeStringConverter.LdtConverter<LocalTime> ldtConverter;

    public LocalTimeStringConverter() {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalTime.class, null, null, null, null, null, null);
    }

    public LocalTimeStringConverter(FormatStyle timeStyle) {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalTime.class, null, null, null, timeStyle, null, null);
    }

    public LocalTimeStringConverter(FormatStyle timeStyle, Locale locale) {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalTime.class, null, null, null, timeStyle, locale, null);
    }

    public LocalTimeStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalTime.class, formatter, parser, null, null, null, null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public LocalTime fromString(String value) {
        return (LocalTime) this.ldtConverter.fromString(value);
    }

    @Override // javafx.util.StringConverter
    public String toString(LocalTime value) {
        return this.ldtConverter.toString((LocalDateTimeStringConverter.LdtConverter<LocalTime>) value);
    }
}
