package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/DurationConverter.class */
public final class DurationConverter extends StyleConverterImpl<ParsedValue<?, Size>, Duration> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<?, Size>, Duration>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/DurationConverter$Holder.class */
    private static class Holder {
        static final DurationConverter INSTANCE = new DurationConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue<?, Size>, Duration> getInstance() {
        return Holder.INSTANCE;
    }

    private DurationConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Duration convert(ParsedValue<ParsedValue<?, Size>, Duration> value, Font font) {
        Duration duration;
        ParsedValue<?, Size> parsedValue = value.getValue();
        Size size = parsedValue.convert(font);
        double time = size.getValue();
        if (time < Double.POSITIVE_INFINITY) {
            switch (size.getUnits()) {
                case S:
                    duration = Duration.seconds(time);
                    break;
                case MS:
                    duration = Duration.millis(time);
                    break;
                default:
                    duration = Duration.UNKNOWN;
                    break;
            }
        } else {
            duration = Duration.INDEFINITE;
        }
        return duration;
    }

    public String toString() {
        return "DurationConverter";
    }
}
