package com.sun.javafx.scene.text;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import javafx.scene.shape.PathElement;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/text/TextLayout.class */
public interface TextLayout {
    public static final int FLAGS_LINES_VALID = 1;
    public static final int FLAGS_ANALYSIS_VALID = 2;
    public static final int FLAGS_HAS_TABS = 4;
    public static final int FLAGS_HAS_BIDI = 8;
    public static final int FLAGS_HAS_COMPLEX = 16;
    public static final int FLAGS_HAS_EMBEDDED = 32;
    public static final int FLAGS_HAS_CJK = 64;
    public static final int FLAGS_WRAPPED = 128;
    public static final int FLAGS_RTL_BASE = 256;
    public static final int FLAGS_CACHED_UNDERLINE = 512;
    public static final int FLAGS_CACHED_STRIKETHROUGH = 1024;
    public static final int FLAGS_LAST = 2048;
    public static final int ANALYSIS_MASK = 2047;
    public static final int ALIGN_LEFT = 262144;
    public static final int ALIGN_CENTER = 524288;
    public static final int ALIGN_RIGHT = 1048576;
    public static final int ALIGN_JUSTIFY = 2097152;
    public static final int ALIGN_MASK = 3932160;
    public static final int DIRECTION_LTR = 1024;
    public static final int DIRECTION_RTL = 2048;
    public static final int DIRECTION_DEFAULT_LTR = 4096;
    public static final int DIRECTION_DEFAULT_RTL = 8192;
    public static final int DIRECTION_MASK = 15360;
    public static final int BOUNDS_CENTER = 16384;
    public static final int BOUNDS_MASK = 16384;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_UNDERLINE = 2;
    public static final int TYPE_STRIKETHROUGH = 4;
    public static final int TYPE_BASELINE = 8;
    public static final int TYPE_TOP = 16;
    public static final int TYPE_BEARINGS = 32;

    boolean setContent(TextSpan[] textSpanArr);

    boolean setContent(String str, Object obj);

    boolean setAlignment(int i2);

    boolean setWrapWidth(float f2);

    boolean setLineSpacing(float f2);

    boolean setDirection(int i2);

    boolean setBoundsType(int i2);

    BaseBounds getBounds();

    BaseBounds getBounds(TextSpan textSpan, BaseBounds baseBounds);

    BaseBounds getVisualBounds(int i2);

    TextLine[] getLines();

    GlyphList[] getRuns();

    Shape getShape(int i2, TextSpan textSpan);

    HitInfo getHitInfo(float f2, float f3);

    PathElement[] getCaretShape(int i2, boolean z2, float f2, float f3);

    PathElement[] getRange(int i2, int i3, int i4, float f2, float f3);
}
