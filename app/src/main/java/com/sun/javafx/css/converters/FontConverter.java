package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Utils;
import java.util.Locale;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter.class */
public final class FontConverter extends StyleConverterImpl<ParsedValue[], Font> {
    @Override // com.sun.javafx.css.StyleConverterImpl
    public /* bridge */ /* synthetic */ Font convert(Map map) {
        return convert((Map<CssMetaData<? extends Styleable, ?>, Object>) map);
    }

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], Font>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$Holder.class */
    private static class Holder {
        static final FontConverter INSTANCE = new FontConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue[], Font> getInstance() {
        return Holder.INSTANCE;
    }

    private FontConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Font convert(ParsedValue<ParsedValue[], Font> value, Font font) {
        ParsedValue[] values = value.getValue();
        Font aFont = font != null ? font : Font.getDefault();
        String family = values[0] != null ? Utils.stripQuotes((String) values[0].convert(aFont)) : aFont.getFamily();
        double fsize = aFont.getSize();
        if (values[1] != null) {
            ParsedValue<?, ?> pv = (ParsedValue) values[1].getValue();
            Size size = (Size) pv.convert(aFont);
            fsize = size.pixels(aFont.getSize(), aFont);
        }
        FontWeight weight = values[2] != null ? (FontWeight) values[2].convert(aFont) : FontWeight.NORMAL;
        FontPosture style = values[3] != null ? (FontPosture) values[3].convert(aFont) : FontPosture.REGULAR;
        Font f2 = Font.font(family, weight, style, fsize);
        return f2;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.StyleConverterImpl
    public Font convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
        Font font = Font.getDefault();
        double size = font.getSize();
        String family = font.getFamily();
        FontWeight weight = FontWeight.NORMAL;
        FontPosture style = FontPosture.REGULAR;
        for (Map.Entry<CssMetaData<? extends Styleable, ?>, Object> entry : convertedValues.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                String prop = entry.getKey().getProperty();
                if (prop.endsWith("font-size")) {
                    size = ((Number) value).doubleValue();
                } else if (prop.endsWith("font-family")) {
                    family = Utils.stripQuotes((String) value);
                } else if (prop.endsWith("font-weight")) {
                    weight = (FontWeight) value;
                } else if (prop.endsWith("font-style")) {
                    style = (FontPosture) value;
                }
            }
        }
        Font f2 = Font.font(family, weight, style, size);
        return f2;
    }

    public String toString() {
        return "FontConverter";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$FontStyleConverter.class */
    public static final class FontStyleConverter extends StyleConverterImpl<String, FontPosture> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<String, FontPosture>) parsedValue, font);
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$FontStyleConverter$Holder.class */
        private static class Holder {
            static final FontStyleConverter INSTANCE = new FontStyleConverter();

            private Holder() {
            }
        }

        public static FontStyleConverter getInstance() {
            return Holder.INSTANCE;
        }

        private FontStyleConverter() {
        }

        @Override // javafx.css.StyleConverter
        public FontPosture convert(ParsedValue<String, FontPosture> value, Font font) {
            Object val = value.getValue();
            FontPosture style = null;
            if (val instanceof String) {
                try {
                    String sval = ((String) val).toUpperCase(Locale.ROOT);
                    style = (FontPosture) Enum.valueOf(FontPosture.class, sval);
                } catch (IllegalArgumentException e2) {
                    style = FontPosture.REGULAR;
                } catch (NullPointerException e3) {
                    style = FontPosture.REGULAR;
                }
            } else if (val instanceof FontPosture) {
                style = (FontPosture) val;
            }
            return style;
        }

        public String toString() {
            return "FontConverter.StyleConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$FontWeightConverter.class */
    public static final class FontWeightConverter extends StyleConverterImpl<String, FontWeight> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<String, FontWeight>) parsedValue, font);
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$FontWeightConverter$Holder.class */
        private static class Holder {
            static final FontWeightConverter INSTANCE = new FontWeightConverter();

            private Holder() {
            }
        }

        public static FontWeightConverter getInstance() {
            return Holder.INSTANCE;
        }

        private FontWeightConverter() {
        }

        @Override // javafx.css.StyleConverter
        public FontWeight convert(ParsedValue<String, FontWeight> value, Font font) {
            Object val = value.getValue();
            FontWeight weight = null;
            if (val instanceof String) {
                try {
                    String sval = ((String) val).toUpperCase(Locale.ROOT);
                    weight = (FontWeight) Enum.valueOf(FontWeight.class, sval);
                } catch (IllegalArgumentException e2) {
                    weight = FontWeight.NORMAL;
                } catch (NullPointerException e3) {
                    weight = FontWeight.NORMAL;
                }
            } else if (val instanceof FontWeight) {
                weight = (FontWeight) val;
            }
            return weight;
        }

        public String toString() {
            return "FontConverter.WeightConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$FontSizeConverter.class */
    public static final class FontSizeConverter extends StyleConverterImpl<ParsedValue<?, Size>, Number> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue<?, Size>, Number>) parsedValue, font);
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/FontConverter$FontSizeConverter$Holder.class */
        private static class Holder {
            static final FontSizeConverter INSTANCE = new FontSizeConverter();

            private Holder() {
            }
        }

        public static FontSizeConverter getInstance() {
            return Holder.INSTANCE;
        }

        private FontSizeConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Number convert(ParsedValue<ParsedValue<?, Size>, Number> value, Font font) {
            ParsedValue<?, Size> size = value.getValue();
            return Double.valueOf(size.convert(font).pixels(font.getSize(), font));
        }

        public String toString() {
            return "FontConverter.FontSizeConverter";
        }
    }
}
