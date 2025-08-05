package javafx.css;

import com.sun.javafx.css.converters.EnumConverter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

/* loaded from: jfxrt.jar:javafx/css/StyleablePropertyFactory.class */
public class StyleablePropertyFactory<S extends Styleable> {
    private final Map<String, Pair<Class, CssMetaData<S, ?>>> metaDataMap;
    private final List<CssMetaData<? extends Styleable, ?>> metaDataList = new ArrayList();
    private final List<CssMetaData<? extends Styleable, ?>> unmodifiableMetaDataList = Collections.unmodifiableList(this.metaDataList);

    public StyleablePropertyFactory(List<CssMetaData<? extends Styleable, ?>> parentCssMetaData) {
        if (parentCssMetaData != null) {
            this.metaDataList.addAll(parentCssMetaData);
        }
        this.metaDataMap = new HashMap();
    }

    public final List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return this.unmodifiableMetaDataList;
    }

    public final StyleableProperty<Boolean> createStyleableBooleanProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Boolean>> function, boolean initialValue, boolean inherits) {
        CssMetaData<S, Boolean> cssMetaData = createBooleanCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableBooleanProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Boolean> createStyleableBooleanProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Boolean>> function, boolean initialValue) {
        return createStyleableBooleanProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<Boolean> createStyleableBooleanProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Boolean>> function) {
        return createStyleableBooleanProperty(styleable, propertyName, cssProperty, function, false, false);
    }

    public final StyleableProperty<Boolean> createStyleableBooleanProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Boolean.class, cssProperty);
        return new SimpleStyleableBooleanProperty(cssMetaData, styleable, propertyName, ((Boolean) cssMetaData.getInitialValue(styleable)).booleanValue());
    }

    public final StyleableProperty<Color> createStyleableColorProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Color>> function, Color initialValue, boolean inherits) {
        CssMetaData<S, Color> cssMetaData = createColorCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Color> createStyleableColorProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Color>> function, Color initialValue) {
        return createStyleableColorProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<Color> createStyleableColorProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Color>> function) {
        return createStyleableColorProperty(styleable, propertyName, cssProperty, function, Color.BLACK, false);
    }

    public final StyleableProperty<Color> createStyleableColorProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Color.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<Duration> createStyleableDurationProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Duration>> function, Duration initialValue, boolean inherits) {
        CssMetaData<S, Duration> cssMetaData = createDurationCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Duration> createStyleableDurationProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Duration>> function, Duration initialValue) {
        return createStyleableDurationProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<Duration> createStyleableDurationProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Duration>> function) {
        return createStyleableDurationProperty(styleable, propertyName, cssProperty, function, Duration.UNKNOWN, false);
    }

    public final StyleableProperty<Duration> createStyleableDurationProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Duration.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final <E extends Effect> StyleableProperty<E> createStyleableEffectProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, E initialValue, boolean inherits) {
        CssMetaData<S, E> cssMetaData = createEffectCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final <E extends Effect> StyleableProperty<E> createStyleableEffectProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, E initialValue) {
        return createStyleableEffectProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final <E extends Effect> StyleableProperty<E> createStyleableEffectProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function) {
        return createStyleableEffectProperty(styleable, propertyName, cssProperty, function, null, false);
    }

    public final StyleableProperty<Effect> createStyleableEffectProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Effect.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final <E extends Enum<E>> StyleableProperty<E> createStyleableEnumProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, Class<E> enumClass, E initialValue, boolean inherits) {
        CssMetaData<S, E> cssMetaData = createEnumCssMetaData(enumClass, cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final <E extends Enum<E>> StyleableProperty<E> createStyleableEnumProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, Class<E> enumClass, E initialValue) {
        return createStyleableEnumProperty(styleable, propertyName, cssProperty, function, enumClass, initialValue, false);
    }

    public final <E extends Enum<E>> StyleableProperty<E> createStyleableEnumProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<E>> function, Class<E> enumClass) {
        return createStyleableEnumProperty(styleable, propertyName, cssProperty, function, enumClass, null, false);
    }

    public final <E extends Enum<E>> StyleableProperty<E> createStyleableEffectProperty(S styleable, String propertyName, String cssProperty, Class<E> enumClass) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(enumClass, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<Font> createStyleableFontProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Font>> function, Font initialValue, boolean inherits) {
        CssMetaData<S, Font> cssMetaData = createFontCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Font> createStyleableFontProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Font>> function, Font initialValue) {
        return createStyleableFontProperty(styleable, propertyName, cssProperty, function, initialValue, true);
    }

    public final StyleableProperty<Font> createStyleableFontProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Font>> function) {
        return createStyleableFontProperty(styleable, propertyName, cssProperty, function, Font.getDefault(), true);
    }

    public final StyleableProperty<Font> createStyleableFontProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Font.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<Insets> createStyleableInsetsProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Insets>> function, Insets initialValue, boolean inherits) {
        CssMetaData<S, Insets> cssMetaData = createInsetsCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Insets> createStyleableInsetsProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Insets>> function, Insets initialValue) {
        return createStyleableInsetsProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<Insets> createStyleableInsetsProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Insets>> function) {
        return createStyleableInsetsProperty(styleable, propertyName, cssProperty, function, Insets.EMPTY, false);
    }

    public final StyleableProperty<Insets> createStyleableInsetsProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Insets.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<Paint> createStyleablePaintProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Paint>> function, Paint initialValue, boolean inherits) {
        CssMetaData<S, Paint> cssMetaData = createPaintCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Paint> createStyleablePaintProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Paint>> function, Paint initialValue) {
        return createStyleablePaintProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<Paint> createStyleablePaintProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Paint>> function) {
        return createStyleablePaintProperty(styleable, propertyName, cssProperty, function, Color.BLACK, false);
    }

    public final StyleableProperty<Paint> createStyleablePaintProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Paint.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<Number> createStyleableNumberProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Number>> function, Number initialValue, boolean inherits) {
        CssMetaData<S, Number> cssMetaData = createSizeCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<Number> createStyleableNumberProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Number>> function, Number initialValue) {
        return createStyleableNumberProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<Number> createStyleableNumberProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<Number>> function) {
        return createStyleableNumberProperty(styleable, propertyName, cssProperty, function, Double.valueOf(0.0d), false);
    }

    public final StyleableProperty<Number> createStyleableNumberProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(Number.class, cssProperty);
        return new SimpleStyleableObjectProperty(cssMetaData, styleable, propertyName, cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<String> createStyleableStringProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<String>> function, String initialValue, boolean inherits) {
        CssMetaData<S, String> cssMetaData = createStringCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<String> createStyleableStringProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<String>> function, String initialValue) {
        return createStyleableStringProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<String> createStyleableStringProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<String>> function) {
        return createStyleableStringProperty(styleable, propertyName, cssProperty, function, null, false);
    }

    public final StyleableProperty<String> createStyleableStringProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(String.class, cssProperty);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, (String) cssMetaData.getInitialValue(styleable));
    }

    public final StyleableProperty<String> createStyleableUrlProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<String>> function, String initialValue, boolean inherits) {
        CssMetaData<S, String> cssMetaData = createUrlCssMetaData(cssProperty, function, initialValue, inherits);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, initialValue);
    }

    public final StyleableProperty<String> createStyleableUrlProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<String>> function, String initialValue) {
        return createStyleableUrlProperty(styleable, propertyName, cssProperty, function, initialValue, false);
    }

    public final StyleableProperty<String> createStyleableUrlProperty(S styleable, String propertyName, String cssProperty, Function<S, StyleableProperty<String>> function) {
        return createStyleableUrlProperty(styleable, propertyName, cssProperty, function, null, false);
    }

    public final StyleableProperty<String> createStyleableUrlProperty(S styleable, String propertyName, String cssProperty) {
        if (cssProperty == null || cssProperty.isEmpty()) {
            throw new IllegalArgumentException("cssProperty cannot be null or empty string");
        }
        CssMetaData<S, ?> cssMetaData = getCssMetaData(String.class, cssProperty);
        return new SimpleStyleableStringProperty(cssMetaData, styleable, propertyName, (String) cssMetaData.getInitialValue(styleable));
    }

    public final CssMetaData<S, Boolean> createBooleanCssMetaData(String str, Function<S, StyleableProperty<Boolean>> function, boolean z2, boolean z3) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Boolean.class, str, key -> {
            StyleConverter<String, Boolean> converter = StyleConverter.getBooleanConverter();
            return new SimpleCssMetaData(key, function, converter, Boolean.valueOf(z2), z3);
        });
    }

    public final CssMetaData<S, Boolean> createBooleanCssMetaData(String property, Function<S, StyleableProperty<Boolean>> function, boolean initialValue) {
        return createBooleanCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, Boolean> createBooleanCssMetaData(String property, Function<S, StyleableProperty<Boolean>> function) {
        return createBooleanCssMetaData(property, function, false, false);
    }

    public final CssMetaData<S, Color> createColorCssMetaData(String str, Function<S, StyleableProperty<Color>> function, Color color, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Color.class, str, key -> {
            StyleConverter<String, Color> converter = StyleConverter.getColorConverter();
            return new SimpleCssMetaData(str, function, converter, color, z2);
        });
    }

    public final CssMetaData<S, Color> createColorCssMetaData(String property, Function<S, StyleableProperty<Color>> function, Color initialValue) {
        return createColorCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, Color> createColorCssMetaData(String property, Function<S, StyleableProperty<Color>> function) {
        return createColorCssMetaData(property, function, Color.BLACK, false);
    }

    public final CssMetaData<S, Duration> createDurationCssMetaData(String str, Function<S, StyleableProperty<Duration>> function, Duration duration, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Duration.class, str, key -> {
            StyleConverter<?, Duration> converter = StyleConverter.getDurationConverter();
            return new SimpleCssMetaData(str, function, converter, duration, z2);
        });
    }

    public final CssMetaData<S, Duration> createDurationCssMetaData(String property, Function<S, StyleableProperty<Duration>> function, Duration initialValue) {
        return createDurationCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, Duration> createDurationCssMetaData(String property, Function<S, StyleableProperty<Duration>> function) {
        return createDurationCssMetaData(property, function, Duration.UNKNOWN, false);
    }

    public final <E extends Effect> CssMetaData<S, E> createEffectCssMetaData(String str, Function<S, StyleableProperty<E>> function, E e2, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Effect.class, str, key -> {
            StyleConverter<ParsedValue[], Effect> converter = StyleConverter.getEffectConverter();
            return new SimpleCssMetaData(str, function, converter, e2, z2);
        });
    }

    public final <E extends Effect> CssMetaData<S, E> createEffectCssMetaData(String property, Function<S, StyleableProperty<E>> function, E initialValue) {
        return createEffectCssMetaData(property, function, initialValue, false);
    }

    public final <E extends Effect> CssMetaData<S, E> createEffectCssMetaData(String property, Function<S, StyleableProperty<E>> function) {
        return createEffectCssMetaData(property, function, null, false);
    }

    public final <E extends Enum<E>> CssMetaData<S, E> createEnumCssMetaData(Class<? extends Enum> cls, String str, Function<S, StyleableProperty<E>> function, E e2, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(cls, str, key -> {
            return new SimpleCssMetaData(str, function, new EnumConverter(cls), e2, z2);
        });
    }

    public final <E extends Enum<E>> CssMetaData<S, E> createEnumCssMetaData(Class<? extends Enum> enumClass, String property, Function<S, StyleableProperty<E>> function, E initialValue) {
        return createEnumCssMetaData(enumClass, property, function, initialValue, false);
    }

    public final <E extends Enum<E>> CssMetaData<S, E> createEnumCssMetaData(Class<? extends Enum> enumClass, String property, Function<S, StyleableProperty<E>> function) {
        return createEnumCssMetaData(enumClass, property, function, null, false);
    }

    public final CssMetaData<S, Font> createFontCssMetaData(String str, Function<S, StyleableProperty<Font>> function, Font font, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Font.class, str, key -> {
            StyleConverter<ParsedValue[], Font> converter = StyleConverter.getFontConverter();
            return new SimpleCssMetaData(str, function, converter, font, z2);
        });
    }

    public final CssMetaData<S, Font> createFontCssMetaData(String property, Function<S, StyleableProperty<Font>> function, Font initialValue) {
        return createFontCssMetaData(property, function, initialValue, true);
    }

    public final CssMetaData<S, Font> createFontCssMetaData(String property, Function<S, StyleableProperty<Font>> function) {
        return createFontCssMetaData(property, function, Font.getDefault(), true);
    }

    public final CssMetaData<S, Insets> createInsetsCssMetaData(String str, Function<S, StyleableProperty<Insets>> function, Insets insets, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Insets.class, str, key -> {
            StyleConverter<ParsedValue[], Insets> converter = StyleConverter.getInsetsConverter();
            return new SimpleCssMetaData(str, function, converter, insets, z2);
        });
    }

    public final CssMetaData<S, Insets> createInsetsCssMetaData(String property, Function<S, StyleableProperty<Insets>> function, Insets initialValue) {
        return createInsetsCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, Insets> createInsetsCssMetaData(String property, Function<S, StyleableProperty<Insets>> function) {
        return createInsetsCssMetaData(property, function, Insets.EMPTY, false);
    }

    public final CssMetaData<S, Paint> createPaintCssMetaData(String str, Function<S, StyleableProperty<Paint>> function, Paint paint, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Paint.class, str, key -> {
            StyleConverter<ParsedValue<?, Paint>, Paint> converter = StyleConverter.getPaintConverter();
            return new SimpleCssMetaData(str, function, converter, paint, z2);
        });
    }

    public final CssMetaData<S, Paint> createPaintCssMetaData(String property, Function<S, StyleableProperty<Paint>> function, Paint initialValue) {
        return createPaintCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, Paint> createPaintCssMetaData(String property, Function<S, StyleableProperty<Paint>> function) {
        return createPaintCssMetaData(property, function, Color.BLACK, false);
    }

    public final CssMetaData<S, Number> createSizeCssMetaData(String str, Function<S, StyleableProperty<Number>> function, Number number, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(Number.class, str, key -> {
            StyleConverter<?, Number> converter = StyleConverter.getSizeConverter();
            return new SimpleCssMetaData(str, function, converter, number, z2);
        });
    }

    public final CssMetaData<S, Number> createSizeCssMetaData(String property, Function<S, StyleableProperty<Number>> function, Number initialValue) {
        return createSizeCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, Number> createSizeCssMetaData(String property, Function<S, StyleableProperty<Number>> function) {
        return createSizeCssMetaData(property, function, Double.valueOf(0.0d), false);
    }

    public final CssMetaData<S, String> createStringCssMetaData(String str, Function<S, StyleableProperty<String>> function, String str2, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(String.class, str, key -> {
            StyleConverter<String, String> converter = StyleConverter.getStringConverter();
            return new SimpleCssMetaData(str, function, converter, str2, z2);
        });
    }

    public final CssMetaData<S, String> createStringCssMetaData(String property, Function<S, StyleableProperty<String>> function, String initialValue) {
        return createStringCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, String> createStringCssMetaData(String property, Function<S, StyleableProperty<String>> function) {
        return createStringCssMetaData(property, function, null, false);
    }

    public final CssMetaData<S, String> createUrlCssMetaData(String str, Function<S, StyleableProperty<String>> function, String str2, boolean z2) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("property cannot be null or empty string");
        }
        if (function == null) {
            throw new IllegalArgumentException("function cannot be null");
        }
        return getCssMetaData(URL.class, str, key -> {
            StyleConverter<ParsedValue[], String> converter = StyleConverter.getUrlConverter();
            return new SimpleCssMetaData(str, function, converter, str2, z2);
        });
    }

    public final CssMetaData<S, String> createUrlCssMetaData(String property, Function<S, StyleableProperty<String>> function, String initialValue) {
        return createUrlCssMetaData(property, function, initialValue, false);
    }

    public final CssMetaData<S, String> createUrlCssMetaData(String property, Function<S, StyleableProperty<String>> function) {
        return createUrlCssMetaData(property, function, null, false);
    }

    /* loaded from: jfxrt.jar:javafx/css/StyleablePropertyFactory$SimpleCssMetaData.class */
    private static class SimpleCssMetaData<S extends Styleable, V> extends CssMetaData<S, V> {
        private final Function<S, StyleableProperty<V>> function;

        SimpleCssMetaData(String property, Function<S, StyleableProperty<V>> function, StyleConverter<?, V> converter, V initialValue, boolean inherits) {
            super(property, converter, initialValue, inherits);
            this.function = function;
        }

        @Override // javafx.css.CssMetaData
        public final boolean isSettable(S styleable) {
            StyleableProperty<V> prop = getStyleableProperty(styleable);
            return prop instanceof Property ? !((Property) prop).isBound() : prop != null;
        }

        @Override // javafx.css.CssMetaData
        public final StyleableProperty<V> getStyleableProperty(S styleable) {
            if (styleable != null) {
                StyleableProperty<V> property = this.function.apply(styleable);
                return property;
            }
            return null;
        }
    }

    void clearDataForTesting() {
        this.metaDataMap.clear();
        this.metaDataList.clear();
    }

    private CssMetaData<S, ?> getCssMetaData(Class ofClass, String property) {
        return getCssMetaData(ofClass, property, null);
    }

    private CssMetaData<S, ?> getCssMetaData(Class ofClass, String property, Function<String, CssMetaData<S, ?>> createFunction) {
        String key = property.toLowerCase();
        Pair<Class, CssMetaData<S, ?>> entry = this.metaDataMap.get(key);
        if (entry != null) {
            if (entry.getKey() == ofClass) {
                return entry.getValue();
            }
            throw new ClassCastException("CssMetaData value is not " + ((Object) ofClass) + ": " + ((Object) entry.getValue()));
        }
        if (createFunction == null) {
            throw new NoSuchElementException("No CssMetaData for " + key);
        }
        CssMetaData<S, ?> cssMetaData = createFunction.apply(key);
        this.metaDataMap.put(key, new Pair<>(ofClass, cssMetaData));
        this.metaDataList.add(cssMetaData);
        return cssMetaData;
    }
}
