package com.sun.webkit.graphics;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/RenderTheme.class */
public abstract class RenderTheme extends Ref {
    public static final int TEXT_FIELD = 0;
    public static final int BUTTON = 1;
    public static final int CHECK_BOX = 2;
    public static final int RADIO_BUTTON = 3;
    public static final int MENU_LIST = 4;
    public static final int MENU_LIST_BUTTON = 5;
    public static final int SLIDER = 6;
    public static final int PROGRESS_BAR = 7;
    public static final int METER = 8;
    public static final int CHECKED = 1;
    public static final int INDETERMINATE = 2;
    public static final int ENABLED = 4;
    public static final int FOCUSED = 8;
    public static final int PRESSED = 16;
    public static final int HOVERED = 32;
    public static final int READ_ONLY = 64;
    public static final int BACKGROUND = 0;
    public static final int FOREGROUND = 1;

    protected abstract Ref createWidget(long j2, int i2, int i3, int i4, int i5, int i6, ByteBuffer byteBuffer);

    public abstract void drawWidget(WCGraphicsContext wCGraphicsContext, Ref ref, int i2, int i3);

    protected abstract int getRadioButtonSize();

    protected abstract int getSelectionColor(int i2);

    public abstract WCSize getWidgetSize(Ref ref);
}
