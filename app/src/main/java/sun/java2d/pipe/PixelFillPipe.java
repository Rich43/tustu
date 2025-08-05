package sun.java2d.pipe;

import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/PixelFillPipe.class */
public interface PixelFillPipe {
    void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5);

    void fillRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7);

    void fillOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5);

    void fillArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7);

    void fillPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2);
}
