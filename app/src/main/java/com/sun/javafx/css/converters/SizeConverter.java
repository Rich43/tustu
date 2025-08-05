package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/SizeConverter.class */
public final class SizeConverter extends StyleConverterImpl<ParsedValue<?, Size>, Number> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<?, Size>, Number>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/SizeConverter$Holder.class */
    private static class Holder {
        static final SizeConverter INSTANCE = new SizeConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue<?, Size>, Number> getInstance() {
        return Holder.INSTANCE;
    }

    private SizeConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Number convert(ParsedValue<ParsedValue<?, Size>, Number> value, Font font) {
        ParsedValue<?, Size> size = value.getValue();
        return Double.valueOf(size.convert(font).pixels(font));
    }

    public String toString() {
        return "SizeConverter";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/SizeConverter$SequenceConverter.class */
    public static final class SequenceConverter extends StyleConverterImpl<ParsedValue[], Number[]> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Number[]>) parsedValue, font);
        }

        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Number[] convert(ParsedValue<ParsedValue[], Number[]> value, Font font) {
            ParsedValue[] sizes = value.getValue();
            Number[] doubles = new Number[sizes.length];
            for (int i2 = 0; i2 < sizes.length; i2++) {
                doubles[i2] = Double.valueOf(((Size) sizes[i2].convert(font)).pixels(font));
            }
            return doubles;
        }

        public String toString() {
            return "Size.SequenceConverter";
        }
    }
}
