package com.sun.javafx.scene.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/text/GlyphList.class */
public interface GlyphList {
    int getGlyphCount();

    int getGlyphCode(int i2);

    float getPosX(int i2);

    float getPosY(int i2);

    float getWidth();

    float getHeight();

    RectBounds getLineBounds();

    Point2D getLocation();

    int getCharOffset(int i2);

    boolean isComplex();

    TextSpan getTextSpan();
}
