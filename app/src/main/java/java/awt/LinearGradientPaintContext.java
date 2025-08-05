package java.awt;

import java.awt.MultipleGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/* loaded from: rt.jar:java/awt/LinearGradientPaintContext.class */
final class LinearGradientPaintContext extends MultipleGradientPaintContext {
    private float dgdX;
    private float dgdY;
    private float gc;

    LinearGradientPaintContext(LinearGradientPaint linearGradientPaint, ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints, Point2D point2D, Point2D point2D2, float[] fArr, Color[] colorArr, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType) {
        super(linearGradientPaint, colorModel, rectangle, rectangle2D, affineTransform, renderingHints, fArr, colorArr, cycleMethod, colorSpaceType);
        float x2 = (float) point2D.getX();
        float y2 = (float) point2D.getY();
        float x3 = (float) point2D2.getX();
        float f2 = x3 - x2;
        float y3 = ((float) point2D2.getY()) - y2;
        float f3 = (f2 * f2) + (y3 * y3);
        float f4 = f2 / f3;
        float f5 = y3 / f3;
        this.dgdX = (this.a00 * f4) + (this.a10 * f5);
        this.dgdY = (this.a01 * f4) + (this.a11 * f5);
        this.gc = ((this.a02 - x2) * f4) + ((this.a12 - y2) * f5);
    }

    @Override // java.awt.MultipleGradientPaintContext
    protected void fillRaster(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = i2 + i6;
        float f2 = (this.dgdX * i4) + this.gc;
        for (int i9 = 0; i9 < i7; i9++) {
            float f3 = f2;
            float f4 = this.dgdY * (i5 + i9);
            while (true) {
                float f5 = f3 + f4;
                if (i2 < i8) {
                    int i10 = i2;
                    i2++;
                    iArr[i10] = indexIntoGradientsArrays(f5);
                    f3 = f5;
                    f4 = this.dgdX;
                }
            }
            i2 += i3;
            i8 = i2 + i6;
        }
    }
}
