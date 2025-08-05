package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.LinearGradient;

/* loaded from: jfxrt.jar:com/sun/prism/impl/paint/LinearGradientContext.class */
final class LinearGradientContext extends MultipleGradientContext {
    private float dgdX;
    private float dgdY;
    private float gc;

    LinearGradientContext(LinearGradient paint, BaseTransform t2, float startx, float starty, float endx, float endy, float[] fractions, Color[] colors, int cycleMethod) {
        super(paint, t2, fractions, colors, cycleMethod);
        float dx = endx - startx;
        float dy = endy - starty;
        float dSq = (dx * dx) + (dy * dy);
        float constX = dx / dSq;
        float constY = dy / dSq;
        this.dgdX = (this.a00 * constX) + (this.a10 * constY);
        this.dgdY = (this.a01 * constX) + (this.a11 * constY);
        this.gc = ((this.a02 - startx) * constX) + ((this.a12 - starty) * constY);
    }

    @Override // com.sun.prism.impl.paint.MultipleGradientContext
    protected void fillRaster(int[] pixels, int off, int adjust, int x2, int y2, int w2, int h2) {
        int rowLimit = off + w2;
        float initConst = (this.dgdX * x2) + this.gc;
        for (int i2 = 0; i2 < h2; i2++) {
            float f2 = initConst;
            float f3 = this.dgdY * (y2 + i2);
            while (true) {
                float g2 = f2 + f3;
                if (off < rowLimit) {
                    int i3 = off;
                    off++;
                    pixels[i3] = indexIntoGradientsArrays(g2);
                    f2 = g2;
                    f3 = this.dgdX;
                }
            }
            off += adjust;
            rowLimit = off + w2;
        }
    }
}
