package com.sun.javafx.css.converters;

import com.sun.javafx.css.Size;
import com.sun.javafx.css.StyleConverterImpl;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/EffectConverter.class */
public class EffectConverter extends StyleConverterImpl<ParsedValue[], Effect> {
    private static Map<ParsedValue<ParsedValue[], Effect>, Effect> cache;

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], Effect>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/EffectConverter$Holder.class */
    private static class Holder {
        static final EffectConverter EFFECT_CONVERTER = new EffectConverter();
        static final DropShadowConverter DROP_SHADOW_INSTANCE = new DropShadowConverter();
        static final InnerShadowConverter INNER_SHADOW_INSTANCE = new InnerShadowConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue[], Effect> getInstance() {
        return Holder.EFFECT_CONVERTER;
    }

    @Override // javafx.css.StyleConverter
    public Effect convert(ParsedValue<ParsedValue[], Effect> value, Font font) {
        throw new IllegalArgumentException("Parsed value is not an Effect");
    }

    protected EffectConverter() {
    }

    public String toString() {
        return "EffectConverter";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/EffectConverter$DropShadowConverter.class */
    public static final class DropShadowConverter extends EffectConverter {
        @Override // com.sun.javafx.css.converters.EffectConverter, javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Effect>) parsedValue, font);
        }

        public static DropShadowConverter getInstance() {
            return Holder.DROP_SHADOW_INSTANCE;
        }

        private DropShadowConverter() {
        }

        @Override // com.sun.javafx.css.converters.EffectConverter, javafx.css.StyleConverter
        public Effect convert(ParsedValue<ParsedValue[], Effect> value, Font font) {
            Effect effect = (Effect) super.getCachedValue(value);
            if (effect != null) {
                return effect;
            }
            ParsedValue[] values = value.getValue();
            BlurType blurType = (BlurType) values[0].convert(font);
            Color color = (Color) values[1].convert(font);
            Double radius = Double.valueOf(((Size) values[2].convert(font)).pixels(font));
            Double spread = Double.valueOf(((Size) values[3].convert(font)).pixels(font));
            Double offsetX = Double.valueOf(((Size) values[4].convert(font)).pixels(font));
            Double offsetY = Double.valueOf(((Size) values[5].convert(font)).pixels(font));
            DropShadow dropShadow = new DropShadow();
            if (blurType != null) {
                dropShadow.setBlurType(blurType);
            }
            if (color != null) {
                dropShadow.setColor(color);
            }
            if (spread != null) {
                dropShadow.setSpread(spread.doubleValue());
            }
            if (radius != null) {
                dropShadow.setRadius(radius.doubleValue());
            }
            if (offsetX != null) {
                dropShadow.setOffsetX(offsetX.doubleValue());
            }
            if (offsetY != null) {
                dropShadow.setOffsetY(offsetY.doubleValue());
            }
            super.cacheValue(value, dropShadow);
            return dropShadow;
        }

        @Override // com.sun.javafx.css.converters.EffectConverter
        public String toString() {
            return "DropShadowConverter";
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/EffectConverter$InnerShadowConverter.class */
    public static final class InnerShadowConverter extends EffectConverter {
        @Override // com.sun.javafx.css.converters.EffectConverter, javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue[], Effect>) parsedValue, font);
        }

        public static InnerShadowConverter getInstance() {
            return Holder.INNER_SHADOW_INSTANCE;
        }

        private InnerShadowConverter() {
        }

        @Override // com.sun.javafx.css.converters.EffectConverter, javafx.css.StyleConverter
        public Effect convert(ParsedValue<ParsedValue[], Effect> value, Font font) {
            Effect effect = (Effect) super.getCachedValue(value);
            if (effect != null) {
                return effect;
            }
            ParsedValue[] values = value.getValue();
            BlurType blurType = (BlurType) values[0].convert(font);
            Color color = (Color) values[1].convert(font);
            Double radius = Double.valueOf(((Size) values[2].convert(font)).pixels(font));
            Double choke = Double.valueOf(((Size) values[3].convert(font)).pixels(font));
            Double offsetX = Double.valueOf(((Size) values[4].convert(font)).pixels(font));
            Double offsetY = Double.valueOf(((Size) values[5].convert(font)).pixels(font));
            InnerShadow innerShadow = new InnerShadow();
            if (blurType != null) {
                innerShadow.setBlurType(blurType);
            }
            if (color != null) {
                innerShadow.setColor(color);
            }
            if (radius != null) {
                innerShadow.setRadius(radius.doubleValue());
            }
            if (choke != null) {
                innerShadow.setChoke(choke.doubleValue());
            }
            if (offsetX != null) {
                innerShadow.setOffsetX(offsetX.doubleValue());
            }
            if (offsetY != null) {
                innerShadow.setOffsetY(offsetY.doubleValue());
            }
            super.cacheValue(value, innerShadow);
            return innerShadow;
        }

        @Override // com.sun.javafx.css.converters.EffectConverter
        public String toString() {
            return "InnerShadowConverter";
        }
    }

    public static void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }
}
