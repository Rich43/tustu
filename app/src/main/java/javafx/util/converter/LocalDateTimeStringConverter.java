package javafx.util.converter;

import com.sun.javafx.binding.Logging;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/util/converter/LocalDateTimeStringConverter.class */
public class LocalDateTimeStringConverter extends StringConverter<LocalDateTime> {
    LdtConverter<LocalDateTime> ldtConverter;

    public LocalDateTimeStringConverter() {
        this.ldtConverter = new LdtConverter<>(LocalDateTime.class, null, null, null, null, null, null);
    }

    public LocalDateTimeStringConverter(FormatStyle dateStyle, FormatStyle timeStyle) {
        this.ldtConverter = new LdtConverter<>(LocalDateTime.class, null, null, dateStyle, timeStyle, null, null);
    }

    public LocalDateTimeStringConverter(DateTimeFormatter formatter, DateTimeFormatter parser) {
        this.ldtConverter = new LdtConverter<>(LocalDateTime.class, formatter, parser, null, null, null, null);
    }

    public LocalDateTimeStringConverter(FormatStyle dateStyle, FormatStyle timeStyle, Locale locale, Chronology chronology) {
        this.ldtConverter = new LdtConverter<>(LocalDateTime.class, null, null, dateStyle, timeStyle, locale, chronology);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.StringConverter
    public LocalDateTime fromString(String value) {
        return (LocalDateTime) this.ldtConverter.fromString(value);
    }

    @Override // javafx.util.StringConverter
    public String toString(LocalDateTime value) {
        return this.ldtConverter.toString((LdtConverter<LocalDateTime>) value);
    }

    /* loaded from: jfxrt.jar:javafx/util/converter/LocalDateTimeStringConverter$LdtConverter.class */
    static class LdtConverter<T extends Temporal> extends StringConverter<T> {
        private Class<T> type;
        Locale locale;
        Chronology chronology;
        DateTimeFormatter formatter;
        DateTimeFormatter parser;
        FormatStyle dateStyle;
        FormatStyle timeStyle;

        LdtConverter(Class<T> type, DateTimeFormatter formatter, DateTimeFormatter parser, FormatStyle dateStyle, FormatStyle timeStyle, Locale locale, Chronology chronology) {
            this.type = type;
            this.formatter = formatter;
            this.parser = parser != null ? parser : formatter;
            this.locale = locale != null ? locale : Locale.getDefault(Locale.Category.FORMAT);
            this.chronology = chronology != null ? chronology : IsoChronology.INSTANCE;
            if (type == LocalDate.class || type == LocalDateTime.class) {
                this.dateStyle = dateStyle != null ? dateStyle : FormatStyle.SHORT;
            }
            if (type == LocalTime.class || type == LocalDateTime.class) {
                this.timeStyle = timeStyle != null ? timeStyle : FormatStyle.SHORT;
            }
        }

        @Override // javafx.util.StringConverter
        public T fromString(String text) {
            if (text == null || text.isEmpty()) {
                return null;
            }
            String text2 = text.trim();
            if (this.parser == null) {
                this.parser = getDefaultParser();
            }
            TemporalAccessor temporal = this.parser.parse(text2);
            if (this.type == LocalDate.class) {
                return LocalDate.from((TemporalAccessor) this.chronology.date(temporal));
            }
            if (this.type == LocalTime.class) {
                return LocalTime.from(temporal);
            }
            return LocalDateTime.from((TemporalAccessor) this.chronology.localDateTime(temporal));
        }

        @Override // javafx.util.StringConverter
        public String toString(T value) {
            ChronoLocalDate cDate;
            ChronoLocalDateTime<? extends ChronoLocalDate> cDateTime;
            if (value == null) {
                return "";
            }
            if (this.formatter == null) {
                this.formatter = getDefaultFormatter();
            }
            if (value instanceof LocalDate) {
                try {
                    cDate = this.chronology.date(value);
                } catch (DateTimeException ex) {
                    Logging.getLogger().warning("Converting LocalDate " + ((Object) value) + " to " + ((Object) this.chronology) + " failed, falling back to IsoChronology.", ex);
                    this.chronology = IsoChronology.INSTANCE;
                    cDate = (LocalDate) value;
                }
                return this.formatter.format(cDate);
            }
            if (value instanceof LocalDateTime) {
                try {
                    cDateTime = this.chronology.localDateTime(value);
                } catch (DateTimeException ex2) {
                    Logging.getLogger().warning("Converting LocalDateTime " + ((Object) value) + " to " + ((Object) this.chronology) + " failed, falling back to IsoChronology.", ex2);
                    this.chronology = IsoChronology.INSTANCE;
                    cDateTime = (LocalDateTime) value;
                }
                return this.formatter.format(cDateTime);
            }
            return this.formatter.format(value);
        }

        private DateTimeFormatter getDefaultParser() {
            String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(this.dateStyle, this.timeStyle, this.chronology, this.locale);
            return new DateTimeFormatterBuilder().parseLenient().appendPattern(pattern).toFormatter().withChronology(this.chronology).withDecimalStyle(DecimalStyle.of(this.locale));
        }

        private DateTimeFormatter getDefaultFormatter() {
            DateTimeFormatter formatter;
            if (this.dateStyle != null && this.timeStyle != null) {
                formatter = DateTimeFormatter.ofLocalizedDateTime(this.dateStyle, this.timeStyle);
            } else if (this.dateStyle != null) {
                formatter = DateTimeFormatter.ofLocalizedDate(this.dateStyle);
            } else {
                formatter = DateTimeFormatter.ofLocalizedTime(this.timeStyle);
            }
            DateTimeFormatter formatter2 = formatter.withLocale(this.locale).withChronology(this.chronology).withDecimalStyle(DecimalStyle.of(this.locale));
            if (this.dateStyle != null) {
                formatter2 = fixFourDigitYear(formatter2, this.dateStyle, this.timeStyle, this.chronology, this.locale);
            }
            return formatter2;
        }

        private DateTimeFormatter fixFourDigitYear(DateTimeFormatter formatter, FormatStyle dateStyle, FormatStyle timeStyle, Chronology chronology, Locale locale) {
            String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(dateStyle, timeStyle, chronology, locale);
            if (pattern.contains("yy") && !pattern.contains("yyy")) {
                String newPattern = pattern.replace("yy", "yyyy");
                formatter = DateTimeFormatter.ofPattern(newPattern).withDecimalStyle(DecimalStyle.of(locale));
            }
            return formatter;
        }
    }
}
