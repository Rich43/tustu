package javafx.util.converter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/LocalDateStringConverter.class */
public class LocalDateStringConverter extends StringConverter<LocalDate> {
    LocalDateTimeStringConverter.LdtConverter<LocalDate> ldtConverter;

    public LocalDateStringConverter() {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalDate.class, null, null, null, null, null, null);
    }

    public LocalDateStringConverter(FormatStyle dateStyle) {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalDate.class, null, null, dateStyle, null, null, null);
    }

    public LocalDateStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalDate.class, formatter, parser, null, null, null, null);
    }

    public LocalDateStringConverter(FormatStyle dateStyle, Locale locale, Chronology chronology) {
        this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter<>(LocalDate.class, null, null, dateStyle, null, locale, chronology);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public LocalDate fromString(String value) {
        return (LocalDate) this.ldtConverter.fromString(value);
    }

    @Override // javafx.util.StringConverter
    public String toString(LocalDate value) {
        return this.ldtConverter.toString((LocalDateTimeStringConverter.LdtConverter<LocalDate>) value);
    }
}
