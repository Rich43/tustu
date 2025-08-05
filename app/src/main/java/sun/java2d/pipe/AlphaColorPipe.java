package sun.java2d.pipe;

import java.awt.Rectangle;
import java.awt.Shape;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/AlphaColorPipe.class */
public class AlphaColorPipe implements CompositePipe, ParallelogramPipe {
    @Override // sun.java2d.pipe.CompositePipe
    public Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr) {
        return sunGraphics2D;
    }

    @Override // sun.java2d.pipe.CompositePipe
    public boolean needTile(Object obj, int i2, int i3, int i4, int i5) {
        return true;
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void renderPathTile(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        SunGraphics2D sunGraphics2D = (SunGraphics2D) obj;
        sunGraphics2D.alphafill.MaskFill(sunGraphics2D, sunGraphics2D.getSurfaceData(), sunGraphics2D.composite, i4, i5, i6, i7, bArr, i2, i3);
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void skipTile(Object obj, int i2, int i3) {
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void endSequence(Object obj) {
    }

    @Override // sun.java2d.pipe.ParallelogramPipe
    public void fillParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11) {
        sunGraphics2D.alphafill.FillAAPgram(sunGraphics2D, sunGraphics2D.getSurfaceData(), sunGraphics2D.composite, d6, d7, d8, d9, d10, d11);
    }

    @Override // sun.java2d.pipe.ParallelogramPipe
    public void drawParallelogram(SunGraphics2D sunGraphics2D, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        sunGraphics2D.alphafill.DrawAAPgram(sunGraphics2D, sunGraphics2D.getSurfaceData(), sunGraphics2D.composite, d6, d7, d8, d9, d10, d11, d12, d13);
    }
}
