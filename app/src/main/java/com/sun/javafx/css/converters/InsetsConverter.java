package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/InsetsConverter.class */
public final class InsetsConverter extends StyleConverterImpl<ParsedValue[], Insets> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], Insets>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/InsetsConverter$Holder.class */
    private static class Holder {
        static final InsetsConverter INSTANCE = new InsetsConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue[], Insets> getInstance() {
        return Holder.INSTANCE;
    }

    private InsetsConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Insets convert(ParsedValue<ParsedValue[], Insets> value, Font font) {
        ParsedValue[] sides = value.getValue();
        double top = ((Size) sides[0].convert(font)).pixels(font);
        double right = sides.length > 1 ? ((Size) sides[1].convert(font)).pixels(font) : top;
        double bottom = sides.length > 2 ? ((Size) sides[2].convert(font)).pixels(font) : top;
        double left = sides.length > 3 ? ((Size) sides[3].convert(font)).pixels(font) : right;
        return new Insets(top, right, bottom, left);
    }

    public String toString() {
        return "InsetsConverter";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/InsetsConverter$SequenceConverter.class */
    public static final class SequenceConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], Insets>[], Insets[]> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue<ParsedValue[], Insets>[], Insets[]>) parsedValue, font);
        }

        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Insets[] convert(ParsedValue<ParsedValue<ParsedValue[], Insets>[], Insets[]> value, Font font) {
            ParsedValue<ParsedValue[], Insets>[] layers = value.getValue();
            Insets[] insets = new Insets[layers.length];
            for (int layer = 0; layer < layers.length; layer++) {
                insets[layer] = InsetsConverter.getInstance().convert(layers[layer], font);
            }
            return insets;
        }

        public String toString() {
            return "InsetsSequenceConverter";
        }
    }
}
