package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCGraphicsContext.class */
public abstract class WCGraphicsContext {
    public static final int COMPOSITE_CLEAR = 0;
    public static final int COMPOSITE_COPY = 1;
    public static final int COMPOSITE_SOURCE_OVER = 2;
    public static final int COMPOSITE_SOURCE_IN = 3;
    public static final int COMPOSITE_SOURCE_OUT = 4;
    public static final int COMPOSITE_SOURCE_ATOP = 5;
    public static final int COMPOSITE_DESTINATION_OVER = 6;
    public static final int COMPOSITE_DESTINATION_IN = 7;
    public static final int COMPOSITE_DESTINATION_OUT = 8;
    public static final int COMPOSITE_DESTINATION_ATOP = 9;
    public static final int COMPOSITE_XOR = 10;
    public static final int COMPOSITE_PLUS_DARKER = 11;
    public static final int COMPOSITE_HIGHLIGHT = 12;
    public static final int COMPOSITE_PLUS_LIGHTER = 13;

    public abstract void fillRect(float f2, float f3, float f4, float f5, Color color);

    public abstract void clearRect(float f2, float f3, float f4, float f5);

    public abstract void setFillColor(Color color);

    public abstract void setFillGradient(WCGradient wCGradient);

    public abstract void fillRoundedRect(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, Color color);

    public abstract void setTextMode(boolean z2, boolean z3, boolean z4);

    public abstract void setFontSmoothingType(int i2);

    public abstract int getFontSmoothingType();

    public abstract void setStrokeStyle(int i2);

    public abstract void setStrokeColor(Color color);

    public abstract void setStrokeWidth(float f2);

    public abstract void setStrokeGradient(WCGradient wCGradient);

    public abstract void setLineDash(float f2, float... fArr);

    public abstract void setLineCap(int i2);

    public abstract void setLineJoin(int i2);

    public abstract void setMiterLimit(float f2);

    public abstract void drawPolygon(WCPath wCPath, boolean z2);

    public abstract void drawLine(int i2, int i3, int i4, int i5);

    public abstract void drawImage(WCImage wCImage, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    public abstract void drawIcon(WCIcon wCIcon, int i2, int i3);

    public abstract void drawPattern(WCImage wCImage, WCRectangle wCRectangle, WCTransform wCTransform, WCPoint wCPoint, WCRectangle wCRectangle2);

    public abstract void drawBitmapImage(ByteBuffer byteBuffer, int i2, int i3, int i4, int i5);

    public abstract void translate(float f2, float f3);

    public abstract void scale(float f2, float f3);

    public abstract void rotate(float f2);

    public abstract void setPerspectiveTransform(WCTransform wCTransform);

    public abstract void setTransform(WCTransform wCTransform);

    public abstract WCTransform getTransform();

    public abstract void concatTransform(WCTransform wCTransform);

    public abstract void saveState();

    public abstract void restoreState();

    public abstract void setClip(WCPath wCPath, boolean z2);

    public abstract void setClip(int i2, int i3, int i4, int i5);

    public abstract void setClip(WCRectangle wCRectangle);

    public abstract WCRectangle getClip();

    public abstract void drawRect(int i2, int i3, int i4, int i5);

    public abstract void setComposite(int i2);

    public abstract void strokeArc(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void drawEllipse(int i2, int i3, int i4, int i5);

    public abstract void drawFocusRing(int i2, int i3, int i4, int i5, Color color);

    public abstract void setAlpha(float f2);

    public abstract float getAlpha();

    public abstract void beginTransparencyLayer(float f2);

    public abstract void endTransparencyLayer();

    public abstract void strokePath(WCPath wCPath);

    public abstract void strokeRect(float f2, float f3, float f4, float f5, float f6);

    public abstract void fillPath(WCPath wCPath);

    public abstract void setShadow(float f2, float f3, float f4, Color color);

    public abstract void drawString(WCFont wCFont, String str, boolean z2, int i2, int i3, float f2, float f3);

    public abstract void drawString(WCFont wCFont, int[] iArr, float[] fArr, float f2, float f3);

    public abstract void drawWidget(RenderTheme renderTheme, Ref ref, int i2, int i3);

    public abstract void drawScrollbar(ScrollBarTheme scrollBarTheme, Ref ref, int i2, int i3, int i4, int i5);

    public abstract WCImage getImage();

    public abstract Object getPlatformGraphics();

    public abstract WCGradient createLinearGradient(WCPoint wCPoint, WCPoint wCPoint2);

    public abstract WCGradient createRadialGradient(WCPoint wCPoint, float f2, WCPoint wCPoint2, float f3);

    public abstract void flush();

    public abstract boolean isValid();

    public abstract void dispose();
}
