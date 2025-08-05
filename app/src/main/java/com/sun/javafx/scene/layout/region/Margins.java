package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Logging;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/Margins.class */
public class Margins {
    final double top;
    final double right;
    final double bottom;
    final double left;
    final boolean proportional;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/Margins$Holder.class */
    private static class Holder {
        static Converter CONVERTER_INSTANCE = new Converter();
        static SequenceConverter SEQUENCE_CONVERTER_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }

    public final double getTop() {
        return this.top;
    }

    public final double getRight() {
        return this.right;
    }

    public final double getBottom() {
        return this.bottom;
    }

    public final double getLeft() {
        return this.left;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    public Margins(double top, double right, double bottom, double left, boolean proportional) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.proportional = proportional;
    }

    public String toString() {
        return "top: " + this.top + "\nright: " + this.right + "\nbottom: " + this.bottom + "\nleft: " + this.left;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/Margins$Converter.class */
    public static final class Converter extends StyleConverterImpl<ParsedValue[], Margins> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Margins>) parsedValue, font);
        }

        public static Converter getInstance() {
            return Holder.CONVERTER_INSTANCE;
        }

        private Converter() {
        }

        @Override // javafx.css.StyleConverter
        public Margins convert(ParsedValue<ParsedValue[], Margins> value, Font font) {
            ParsedValue<?, Size>[] sides = value.getValue();
            Size topSz = sides.length > 0 ? sides[0].convert(font) : new Size(0.0d, SizeUnits.PX);
            Size rightSz = sides.length > 1 ? sides[1].convert(font) : topSz;
            Size bottomSz = sides.length > 2 ? sides[2].convert(font) : topSz;
            Size leftSz = sides.length > 3 ? sides[3].convert(font) : rightSz;
            boolean proportional = topSz.getUnits() == SizeUnits.PERCENT || rightSz.getUnits() == SizeUnits.PERCENT || bottomSz.getUnits() == SizeUnits.PERCENT || leftSz.getUnits() == SizeUnits.PERCENT;
            boolean unitsMatch = !proportional || (topSz.getUnits() == SizeUnits.PERCENT && rightSz.getUnits() == SizeUnits.PERCENT && bottomSz.getUnits() == SizeUnits.PERCENT && leftSz.getUnits() == SizeUnits.PERCENT);
            if (!unitsMatch) {
                PlatformLogger LOGGER = Logging.getCSSLogger();
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    String msg = "units do no match: " + topSz.toString() + " ," + rightSz.toString() + " ," + bottomSz.toString() + " ," + leftSz.toString();
                    LOGGER.warning(msg);
                }
            }
            boolean proportional2 = proportional && unitsMatch;
            double top = topSz.pixels(font);
            double right = rightSz.pixels(font);
            double bottom = bottomSz.pixels(font);
            double left = leftSz.pixels(font);
            return new Margins(top, right, bottom, left, proportional2);
        }

        public String toString() {
            return "MarginsConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/Margins$SequenceConverter.class */
    public static final class SequenceConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], Margins>[], Margins[]> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue<ParsedValue[], Margins>[], Margins[]>) parsedValue, font);
        }

        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_CONVERTER_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override // javafx.css.StyleConverter
        public Margins[] convert(ParsedValue<ParsedValue<ParsedValue[], Margins>[], Margins[]> value, Font font) {
            ParsedValue<ParsedValue[], Margins>[] layers = value.getValue();
            Margins[] margins = new Margins[layers.length];
            for (int layer = 0; layer < layers.length; layer++) {
                margins[layer] = Converter.getInstance().convert(layers[layer], font);
            }
            return margins;
        }

        public String toString() {
            return "MarginsSequenceConverter";
        }
    }
}
