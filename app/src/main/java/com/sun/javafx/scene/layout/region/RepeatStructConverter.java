package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.EnumConverter;
import javafx.css.ParsedValue;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/layout/region/RepeatStructConverter.class */
public final class RepeatStructConverter extends StyleConverterImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> {
    private static final RepeatStructConverter REPEAT_STRUCT_CONVERTER = new RepeatStructConverter();
    private final EnumConverter<BackgroundRepeat> repeatConverter = new EnumConverter<>(BackgroundRepeat.class);

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]>) parsedValue, font);
    }

    public static RepeatStructConverter getInstance() {
        return REPEAT_STRUCT_CONVERTER;
    }

    private RepeatStructConverter() {
    }

    @Override // javafx.css.StyleConverter
    public RepeatStruct[] convert(ParsedValue<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> value, Font font) {
        ParsedValue<String, BackgroundRepeat>[][] layers = value.getValue();
        RepeatStruct[] backgroundRepeat = new RepeatStruct[layers.length];
        for (int l2 = 0; l2 < layers.length; l2++) {
            ParsedValue<String, BackgroundRepeat>[] repeats = layers[l2];
            BackgroundRepeat horizontal = (BackgroundRepeat) this.repeatConverter.convert((ParsedValue) repeats[0], (Font) null);
            BackgroundRepeat vertical = (BackgroundRepeat) this.repeatConverter.convert((ParsedValue) repeats[1], (Font) null);
            backgroundRepeat[l2] = new RepeatStruct(horizontal, vertical);
        }
        return backgroundRepeat;
    }

    public String toString() {
        return "RepeatStructConverter";
    }
}
