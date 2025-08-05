package sun.java2d.pipe;

import java.awt.Rectangle;
import java.awt.Shape;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/CompositePipe.class */
public interface CompositePipe {
    Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr);

    boolean needTile(Object obj, int i2, int i3, int i4, int i5);

    void renderPathTile(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7);

    void skipTile(Object obj, int i2, int i3);

    void endSequence(Object obj);
}
