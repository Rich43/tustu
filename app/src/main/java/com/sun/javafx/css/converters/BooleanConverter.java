package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/BooleanConverter.class */
public final class BooleanConverter extends StyleConverterImpl<String, Boolean> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<String, Boolean>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/BooleanConverter$Holder.class */
    private static class Holder {
        static final BooleanConverter INSTANCE = new BooleanConverter();

        private Holder() {
        }
    }

    public static StyleConverter<String, Boolean> getInstance() {
        return Holder.INSTANCE;
    }

    private BooleanConverter() {
    }

    @Override // javafx.css.StyleConverter
    public Boolean convert(ParsedValue<String, Boolean> value, Font not_used) {
        String str = value.getValue();
        return Boolean.valueOf(str);
    }

    public String toString() {
        return "BooleanConverter";
    }
}
