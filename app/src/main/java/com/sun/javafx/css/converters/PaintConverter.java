package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.image.Image;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter.class */
public final class PaintConverter extends StyleConverterImpl<ParsedValue<?, Paint>, Paint> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<?, Paint>, Paint>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter$Holder.class */
    private static class Holder {
        static final PaintConverter INSTANCE = new PaintConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();
        static final LinearGradientConverter LINEAR_GRADIENT_INSTANCE = new LinearGradientConverter();
        static final ImagePatternConverter IMAGE_PATTERN_INSTANCE = new ImagePatternConverter();
        static final RepeatingImagePatternConverter REPEATING_IMAGE_PATTERN_INSTANCE = new RepeatingImagePatternConverter();
        static final RadialGradientConverter RADIAL_GRADIENT_INSTANCE = new RadialGradientConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue<?, Paint>, Paint> getInstance() {
        return Holder.INSTANCE;
    }

    private PaintConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Paint convert(ParsedValue<ParsedValue<?, Paint>, Paint> value, Font font) {
        Object obj = value.getValue();
        if (obj instanceof Paint) {
            return (Paint) obj;
        }
        return value.getValue().convert(font);
    }

    public String toString() {
        return "PaintConverter";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter$SequenceConverter.class */
    public static final class SequenceConverter extends StyleConverterImpl<ParsedValue<?, Paint>[], Paint[]> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue<?, Paint>[], Paint[]>) parsedValue, font);
        }

        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Paint[] convert(ParsedValue<ParsedValue<?, Paint>[], Paint[]> value, Font font) {
            ParsedValue<?, Paint>[] values = value.getValue();
            Paint[] paints = new Paint[values.length];
            for (int p2 = 0; p2 < values.length; p2++) {
                paints[p2] = values[p2].convert(font);
            }
            return paints;
        }

        public String toString() {
            return "Paint.SequenceConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter$LinearGradientConverter.class */
    public static final class LinearGradientConverter extends StyleConverterImpl<ParsedValue[], Paint> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Paint>) parsedValue, font);
        }

        public static LinearGradientConverter getInstance() {
            return Holder.LINEAR_GRADIENT_INSTANCE;
        }

        private LinearGradientConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Paint convert(ParsedValue<ParsedValue[], Paint> value, Font font) {
            Paint paint = (Paint) super.getCachedValue(value);
            if (paint != null) {
                return paint;
            }
            ParsedValue[] values = value.getValue();
            int v2 = 0 + 1;
            Size startX = (Size) values[0].convert(font);
            int v3 = v2 + 1;
            Size startY = (Size) values[v2].convert(font);
            int v4 = v3 + 1;
            Size endX = (Size) values[v3].convert(font);
            int v5 = v4 + 1;
            Size endY = (Size) values[v4].convert(font);
            boolean proportional = startX.getUnits() == SizeUnits.PERCENT && startX.getUnits() == startY.getUnits() && startX.getUnits() == endX.getUnits() && startX.getUnits() == endY.getUnits();
            int v6 = v5 + 1;
            CycleMethod cycleMethod = (CycleMethod) values[v5].convert(font);
            Stop[] stops = new Stop[values.length - v6];
            for (int s2 = v6; s2 < values.length; s2++) {
                stops[s2 - v6] = (Stop) values[s2].convert(font);
            }
            Paint paint2 = new LinearGradient(startX.pixels(font), startY.pixels(font), endX.pixels(font), endY.pixels(font), proportional, cycleMethod, stops);
            super.cacheValue(value, paint2);
            return paint2;
        }

        public String toString() {
            return "LinearGradientConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter$ImagePatternConverter.class */
    public static final class ImagePatternConverter extends StyleConverterImpl<ParsedValue[], Paint> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Paint>) parsedValue, font);
        }

        public static ImagePatternConverter getInstance() {
            return Holder.IMAGE_PATTERN_INSTANCE;
        }

        private ImagePatternConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Paint convert(ParsedValue<ParsedValue[], Paint> value, Font font) {
            Paint paint = (Paint) super.getCachedValue(value);
            if (paint != null) {
                return paint;
            }
            ParsedValue<?, ?>[] values = value.getValue();
            ParsedValue<?, ?> urlParsedValue = values[0];
            String url = (String) urlParsedValue.convert(font);
            if (values.length == 1) {
                return new ImagePattern(StyleManager.getInstance().getCachedImage(url));
            }
            Size x2 = (Size) values[1].convert(font);
            Size y2 = (Size) values[2].convert(font);
            Size w2 = (Size) values[3].convert(font);
            Size h2 = (Size) values[4].convert(font);
            boolean p2 = values.length < 6 ? true : ((Boolean) values[5].getValue()).booleanValue();
            Paint paint2 = new ImagePattern(new Image(url), x2.getValue(), y2.getValue(), w2.getValue(), h2.getValue(), p2);
            super.cacheValue(value, paint2);
            return paint2;
        }

        public String toString() {
            return "ImagePatternConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter$RepeatingImagePatternConverter.class */
    public static final class RepeatingImagePatternConverter extends StyleConverterImpl<ParsedValue[], Paint> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Paint>) parsedValue, font);
        }

        public static RepeatingImagePatternConverter getInstance() {
            return Holder.REPEATING_IMAGE_PATTERN_INSTANCE;
        }

        private RepeatingImagePatternConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Paint convert(ParsedValue<ParsedValue[], Paint> value, Font font) {
            Paint paint = (Paint) super.getCachedValue(value);
            if (paint != null) {
                return paint;
            }
            ParsedValue<?, ?>[] values = value.getValue();
            ParsedValue<?, ?> url = values[0];
            String u2 = (String) url.convert(font);
            if (u2 == null) {
                return null;
            }
            Image image = new Image(u2);
            Paint paint2 = new ImagePattern(image, 0.0d, 0.0d, image.getWidth(), image.getHeight(), false);
            super.cacheValue(value, paint2);
            return paint2;
        }

        public String toString() {
            return "RepeatingImagePatternConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/PaintConverter$RadialGradientConverter.class */
    public static final class RadialGradientConverter extends StyleConverterImpl<ParsedValue[], Paint> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Paint>) parsedValue, font);
        }

        public static RadialGradientConverter getInstance() {
            return Holder.RADIAL_GRADIENT_INSTANCE;
        }

        private RadialGradientConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Paint convert(ParsedValue<ParsedValue[], Paint> value, Font font) {
            Paint paint = (Paint) super.getCachedValue(value);
            if (paint != null) {
                return paint;
            }
            ParsedValue[] values = value.getValue();
            int v2 = 0 + 1;
            Size focusAngle = values[0] != null ? (Size) values[v2 - 1].convert(font) : null;
            int v3 = v2 + 1;
            Size focusDistance = values[v2] != null ? (Size) values[v3 - 1].convert(font) : null;
            int v4 = v3 + 1;
            Size centerX = values[v3] != null ? (Size) values[v4 - 1].convert(font) : null;
            int v5 = v4 + 1;
            Size centerY = values[v4] != null ? (Size) values[v5 - 1].convert(font) : null;
            int v6 = v5 + 1;
            Size radius = (Size) values[v5].convert(font);
            boolean proportional = radius.getUnits().equals(SizeUnits.PERCENT);
            boolean z2 = centerX == null || proportional == centerX.getUnits().equals(SizeUnits.PERCENT);
            boolean unitsAgree = z2;
            boolean z3 = !unitsAgree || centerY == null || proportional == centerY.getUnits().equals(SizeUnits.PERCENT);
            boolean unitsAgree2 = z3;
            if (!unitsAgree2) {
                throw new IllegalArgumentException("units do not agree");
            }
            int v7 = v6 + 1;
            CycleMethod cycleMethod = (CycleMethod) values[v6].convert(font);
            Stop[] stops = new Stop[values.length - v7];
            for (int s2 = v7; s2 < values.length; s2++) {
                stops[s2 - v7] = (Stop) values[s2].convert(font);
            }
            double fa = 0.0d;
            if (focusAngle != null) {
                fa = focusAngle.pixels(font);
                if (focusAngle.getUnits().equals(SizeUnits.PERCENT)) {
                    fa = (fa * 360.0d) % 360.0d;
                }
            }
            Paint paint2 = new RadialGradient(fa, focusDistance != null ? focusDistance.pixels() : 0.0d, centerX != null ? centerX.pixels() : 0.0d, centerY != null ? centerY.pixels() : 0.0d, radius != null ? radius.pixels() : 1.0d, proportional, cycleMethod, stops);
            super.cacheValue(value, paint2);
            return paint2;
        }

        public String toString() {
            return "RadialGradientConverter";
        }
    }
}
