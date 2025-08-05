package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/ShapeConverter.class */
public class ShapeConverter extends StyleConverterImpl<String, Shape> {
    private static final ShapeConverter INSTANCE = new ShapeConverter();
    private static Map<ParsedValue<String, Shape>, Shape> cache;

    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<String, Shape>) parsedValue, font);
    }

    public static StyleConverter<String, Shape> getInstance() {
        return INSTANCE;
    }

    @Override // javafx.css.StyleConverter
    public Shape convert(ParsedValue<String, Shape> value, Font font) {
        Shape shape = (Shape) super.getCachedValue(value);
        if (shape != null) {
            return shape;
        }
        String svg = value.getValue();
        if (svg == null || svg.isEmpty()) {
            return null;
        }
        SVGPath path = new SVGPath();
        path.setContent(svg);
        super.cacheValue(value, path);
        return path;
    }

    public static void clearCache() {
        if (cache != null) {
            cache.clear();
        }
    }
}
