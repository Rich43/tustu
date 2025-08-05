package com.sun.javafx.css;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.ColorConverter;
import com.sun.javafx.css.converters.CursorConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.css.parser.DeriveColorConverter;
import com.sun.javafx.css.parser.DeriveSizeConverter;
import com.sun.javafx.css.parser.LadderConverter;
import com.sun.javafx.css.parser.StopConverter;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import com.sun.javafx.util.Logging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleConverterImpl.class */
public class StyleConverterImpl<F, T> extends StyleConverter<F, T> {
    private static Map<ParsedValue, Object> cache;
    private static Map<String, StyleConverter<?, ?>> tmap;

    public T convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
        return null;
    }

    protected StyleConverterImpl() {
    }

    public void writeBinary(DataOutputStream os, StringStore sstore) throws IOException {
        String cname = getClass().getName();
        int index = sstore.addString(cname);
        os.writeShort(index);
    }

    static void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }

    protected T getCachedValue(ParsedValue parsedValue) {
        if (cache != null) {
            return (T) cache.get(parsedValue);
        }
        return null;
    }

    protected void cacheValue(ParsedValue key, Object value) {
        if (cache == null) {
            cache = new WeakHashMap();
        }
        cache.put(key, value);
    }

    public static StyleConverter<?, ?> readBinary(DataInputStream is, String[] strings) throws IOException {
        int index = is.readShort();
        String cname = strings[index];
        if (cname == null || cname.isEmpty()) {
            return null;
        }
        if (cname.startsWith("com.sun.javafx.css.converters.EnumConverter")) {
            return EnumConverter.readBinary(is, strings);
        }
        if (tmap == null || !tmap.containsKey(cname)) {
            StyleConverter<?, ?> converter = getInstance(cname);
            if (converter == null) {
                PlatformLogger logger = Logging.getCSSLogger();
                if (logger.isLoggable(PlatformLogger.Level.SEVERE)) {
                    logger.severe("could not deserialize " + cname);
                }
            }
            if (converter == null) {
                System.err.println("could not deserialize " + cname);
            }
            if (tmap == null) {
                tmap = new HashMap();
            }
            tmap.put(cname, converter);
            return converter;
        }
        return tmap.get(cname);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static StyleConverter<?, ?> getInstance(String converterClass) {
        StyleConverter styleConverter;
        styleConverter = null;
        switch (converterClass) {
            case "com.sun.javafx.css.converters.BooleanConverter":
                styleConverter = BooleanConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.ColorConverter":
                styleConverter = ColorConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.CursorConverter":
                styleConverter = CursorConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.EffectConverter":
                styleConverter = EffectConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.EffectConverter$DropShadowConverter":
                styleConverter = EffectConverter.DropShadowConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.EffectConverter$InnerShadowConverter":
                styleConverter = EffectConverter.InnerShadowConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.FontConverter":
                styleConverter = FontConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.FontConverter$FontStyleConverter":
            case "com.sun.javafx.css.converters.FontConverter$StyleConverter":
                styleConverter = FontConverter.FontStyleConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.FontConverter$FontWeightConverter":
            case "com.sun.javafx.css.converters.FontConverter$WeightConverter":
                styleConverter = FontConverter.FontWeightConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.FontConverter$FontSizeConverter":
            case "com.sun.javafx.css.converters.FontConverter$SizeConverter":
                styleConverter = FontConverter.FontSizeConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.InsetsConverter":
                styleConverter = InsetsConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.InsetsConverter$SequenceConverter":
                styleConverter = InsetsConverter.SequenceConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.PaintConverter":
                styleConverter = PaintConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.PaintConverter$SequenceConverter":
                styleConverter = PaintConverter.SequenceConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.PaintConverter$LinearGradientConverter":
                styleConverter = PaintConverter.LinearGradientConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.PaintConverter$RadialGradientConverter":
                styleConverter = PaintConverter.RadialGradientConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.SizeConverter":
                styleConverter = SizeConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.SizeConverter$SequenceConverter":
                styleConverter = SizeConverter.SequenceConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.StringConverter":
                styleConverter = StringConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.StringConverter$SequenceConverter":
                styleConverter = StringConverter.SequenceConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.URLConverter":
                styleConverter = URLConverter.getInstance();
                break;
            case "com.sun.javafx.css.converters.URLConverter$SequenceConverter":
                styleConverter = URLConverter.SequenceConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BackgroundPositionConverter":
            case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundPositionConverter":
                styleConverter = BackgroundPositionConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BackgroundSizeConverter":
            case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundSizeConverter":
                styleConverter = BackgroundSizeConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BorderImageSliceConverter":
            case "com.sun.javafx.scene.layout.region.BorderImage$SliceConverter":
                styleConverter = BorderImageSliceConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BorderImageWidthConverter":
                styleConverter = BorderImageWidthConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter":
                styleConverter = BorderImageWidthsSequenceConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter":
            case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderStyleSequenceConverter":
                styleConverter = BorderStrokeStyleSequenceConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.BorderStyleConverter":
            case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderStyleConverter":
                styleConverter = BorderStyleConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter":
            case "com.sun.javafx.scene.layout.region.BackgroundImage$LayeredBackgroundPositionConverter":
                styleConverter = LayeredBackgroundPositionConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter":
            case "com.sun.javafx.scene.layout.region.BackgroundImage$LayeredBackgroundSizeConverter":
                styleConverter = LayeredBackgroundSizeConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter":
            case "com.sun.javafx.scene.layout.region.StrokeBorder$LayeredBorderPaintConverter":
                styleConverter = LayeredBorderPaintConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter":
            case "com.sun.javafx.scene.layout.region.StrokeBorder$LayeredBorderStyleConverter":
                styleConverter = LayeredBorderStyleConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.RepeatStructConverter":
            case "com.sun.javafx.scene.layout.region.BackgroundImage$BackgroundRepeatConverter":
            case "com.sun.javafx.scene.layout.region.BorderImage$RepeatConverter":
                styleConverter = RepeatStructConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.SliceSequenceConverter":
            case "com.sun.javafx.scene.layout.region.BorderImage$SliceSequenceConverter":
                styleConverter = SliceSequenceConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter":
            case "com.sun.javafx.scene.layout.region.StrokeBorder$BorderPaintConverter":
                styleConverter = StrokeBorderPaintConverter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.Margins$Converter":
                styleConverter = Margins.Converter.getInstance();
                break;
            case "com.sun.javafx.scene.layout.region.Margins$SequenceConverter":
                styleConverter = Margins.SequenceConverter.getInstance();
                break;
            case "javafx.scene.layout.CornerRadiiConverter":
            case "com.sun.javafx.scene.layout.region.CornerRadiiConverter":
                styleConverter = CornerRadiiConverter.getInstance();
                break;
            case "com.sun.javafx.css.parser.DeriveColorConverter":
                styleConverter = DeriveColorConverter.getInstance();
                break;
            case "com.sun.javafx.css.parser.DeriveSizeConverter":
                styleConverter = DeriveSizeConverter.getInstance();
                break;
            case "com.sun.javafx.css.parser.LadderConverter":
                styleConverter = LadderConverter.getInstance();
                break;
            case "com.sun.javafx.css.parser.StopConverter":
                styleConverter = StopConverter.getInstance();
                break;
            default:
                PlatformLogger logger = Logging.getCSSLogger();
                if (logger.isLoggable(PlatformLogger.Level.SEVERE)) {
                    logger.severe("StyleConverterImpl : converter Class is null for : " + converterClass);
                    break;
                }
                break;
        }
        return styleConverter;
    }
}
