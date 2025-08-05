package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/StringConverter.class */
public final class StringConverter extends StyleConverterImpl<String, String> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<String, String>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/StringConverter$Holder.class */
    private static class Holder {
        static final StringConverter INSTANCE = new StringConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }

    public static StyleConverter<String, String> getInstance() {
        return Holder.INSTANCE;
    }

    private StringConverter() {
    }

    @Override // javafx.css.StyleConverter
    public String convert(ParsedValue<String, String> value, Font font) {
        String string = value.getValue();
        if (string == null) {
            return null;
        }
        return Utils.convertUnicode(string);
    }

    public String toString() {
        return "StringConverter";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/StringConverter$SequenceConverter.class */
    public static final class SequenceConverter extends StyleConverterImpl<ParsedValue<String, String>[], String[]> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue<String, String>[], String[]>) parsedValue, font);
        }

        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override // javafx.css.StyleConverter
        public String[] convert(ParsedValue<ParsedValue<String, String>[], String[]> value, Font font) {
            ParsedValue<String, String>[] layers = value.getValue();
            String[] strings = new String[layers.length];
            for (int layer = 0; layer < layers.length; layer++) {
                strings[layer] = StringConverter.getInstance().convert(layers[layer], font);
            }
            return strings;
        }

        public String toString() {
            return "String.SequenceConverter";
        }
    }
}
