package com.sun.javafx.font;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/font/Glyph.class */
public interface Glyph {
    int getGlyphCode();

    RectBounds getBBox();

    float getAdvance();

    Shape getShape();

    byte[] getPixelData();

    byte[] getPixelData(int i2);

    float getPixelXAdvance();

    float getPixelYAdvance();

    boolean isLCDGlyph();

    int getWidth();

    int getHeight();

    int getOriginX();

    int getOriginY();
}
