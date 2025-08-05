package javafx.css;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.ColorConverter;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import javafx.geometry.Insets;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/css/StyleConverter.class */
public class StyleConverter<F, T> {
    public T convert(ParsedValue<F, T> parsedValue, Font font) {
        return parsedValue.getValue();
    }

    public static StyleConverter<String, Boolean> getBooleanConverter() {
        return BooleanConverter.getInstance();
    }

    public static StyleConverter<?, Duration> getDurationConverter() {
        return DurationConverter.getInstance();
    }

    public static StyleConverter<String, Color> getColorConverter() {
        return ColorConverter.getInstance();
    }

    public static StyleConverter<ParsedValue[], Effect> getEffectConverter() {
        return EffectConverter.getInstance();
    }

    public static <E extends Enum<E>> StyleConverter<String, ? extends Enum<?>> getEnumConverter(Class<E> enumClass) {
        EnumConverter<E> converter = new EnumConverter<>(enumClass);
        return converter;
    }

    public static StyleConverter<ParsedValue[], Font> getFontConverter() {
        return FontConverter.getInstance();
    }

    public static StyleConverter<ParsedValue[], Insets> getInsetsConverter() {
        return InsetsConverter.getInstance();
    }

    public static StyleConverter<ParsedValue<?, Paint>, Paint> getPaintConverter() {
        return PaintConverter.getInstance();
    }

    public static StyleConverter<?, Number> getSizeConverter() {
        return SizeConverter.getInstance();
    }

    public static StyleConverter<String, String> getStringConverter() {
        return StringConverter.getInstance();
    }

    public static StyleConverter<ParsedValue[], String> getUrlConverter() {
        return URLConverter.getInstance();
    }
}
