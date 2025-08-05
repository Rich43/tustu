package com.sun.javafx.font;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontStrike.class */
public interface FontStrike {
    FontResource getFontResource();

    float getSize();

    BaseTransform getTransform();

    boolean drawAsShapes();

    int getQuantizedPosition(Point2D point2D);

    Metrics getMetrics();

    Glyph getGlyph(char c2);

    Glyph getGlyph(int i2);

    void clearDesc();

    int getAAMode();

    float getCharAdvance(char c2);

    Shape getOutline(GlyphList glyphList, BaseTransform baseTransform);
}
