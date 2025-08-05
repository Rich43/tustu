package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCPathIterator.class */
public interface WCPathIterator {
    public static final int SEG_MOVETO = 0;
    public static final int SEG_LINETO = 1;
    public static final int SEG_QUADTO = 2;
    public static final int SEG_CUBICTO = 3;
    public static final int SEG_CLOSE = 4;

    int getWindingRule();

    boolean isDone();

    void next();

    int currentSegment(double[] dArr);
}
