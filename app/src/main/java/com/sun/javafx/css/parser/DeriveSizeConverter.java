package com.sun.javafx.css.parser;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/parser/DeriveSizeConverter.class */
public final class DeriveSizeConverter extends StyleConverterImpl<ParsedValue<Size, Size>[], Size> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<Size, Size>[], Size>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/parser/DeriveSizeConverter$Holder.class */
    private static class Holder {
        static final DeriveSizeConverter INSTANCE = new DeriveSizeConverter();

        private Holder() {
        }
    }

    public static DeriveSizeConverter getInstance() {
        return Holder.INSTANCE;
    }

    private DeriveSizeConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Size convert(ParsedValue<ParsedValue<Size, Size>[], Size> value, Font font) {
        ParsedValue<Size, Size>[] sizes = value.getValue();
        double px1 = sizes[0].convert(font).pixels(font);
        double px2 = sizes[1].convert(font).pixels(font);
        return new Size(px1 + px2, SizeUnits.PX);
    }

    public String toString() {
        return "DeriveSizeConverter";
    }
}
