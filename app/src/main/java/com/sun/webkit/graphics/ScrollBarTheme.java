package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/ScrollBarTheme.class */
public abstract class ScrollBarTheme extends Ref {
    public static final int NO_PART = 0;
    public static final int BACK_BUTTON_START_PART = 1;
    public static final int FORWARD_BUTTON_START_PART = 2;
    public static final int BACK_TRACK_PART = 4;
    public static final int THUMB_PART = 8;
    public static final int FORWARD_TRACK_PART = 16;
    public static final int BACK_BUTTON_END_PART = 32;
    public static final int FORWARD_BUTTON_END_PART = 64;
    public static final int SCROLLBAR_BG_PART = 128;
    public static final int TRACK_BG_PART = 256;
    public static final int HORIZONTAL_SCROLLBAR = 0;
    public static final int VERTICAL_SCROLLBAR = 1;
    private static int thickness;

    protected abstract Ref createWidget(long j2, int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void paint(WCGraphicsContext wCGraphicsContext, Ref ref, int i2, int i3, int i4, int i5);

    protected abstract void getScrollBarPartRect(long j2, int i2, int[] iArr);

    public abstract WCSize getWidgetSize(Ref ref);

    public static int getThickness() {
        if (thickness > 0) {
            return thickness;
        }
        return 12;
    }

    public static void setThickness(int value) {
        thickness = value;
    }
}
