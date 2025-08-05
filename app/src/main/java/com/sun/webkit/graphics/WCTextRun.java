package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCTextRun.class */
public interface WCTextRun {
    boolean isLeftToRight();

    float[] getGlyphPosAndAdvance(int i2);

    int getCharOffset(int i2);

    int getEnd();

    int getGlyph(int i2);

    int getGlyphCount();

    int getStart();
}
