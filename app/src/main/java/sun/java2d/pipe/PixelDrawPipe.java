package sun.java2d.pipe;

import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/PixelDrawPipe.class */
public interface PixelDrawPipe {
    void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5);

    void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5);

    void drawRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7);

    void drawOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5);

    void drawArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7);

    void drawPolyline(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2);

    void drawPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2);
}
