package com.sun.javafx.text;

import com.sun.javafx.font.PGFont;

/* loaded from: jfxrt.jar:com/sun/javafx/text/LayoutCache.class */
class LayoutCache {
    int[] glyphs;
    float[] advances;
    boolean valid;
    int analysis;
    char[] text;
    PGFont font;
    TextRun[] runs;
    int runCount;
    TextLine[] lines;
    float layoutWidth;
    float layoutHeight;

    LayoutCache() {
    }
}
