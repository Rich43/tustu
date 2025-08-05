package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEBoxShadowPeer.class */
public class SSEBoxShadowPeer extends SSEEffectPeer<BoxRenderState> {
    private static native void filterHorizontalBlack(int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5, int i6, int i7, float f2);

    private static native void filterVerticalBlack(int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5, int i6, int i7, float f2);

    private static native void filterVertical(int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5, int i6, int i7, float f2, float[] fArr);

    public SSEBoxShadowPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, BoxRenderState brstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        setRenderState(brstate);
        boolean horizontal = getPass() == 0;
        int hinc = horizontal ? brstate.getBoxPixelSize(0) - 1 : 0;
        int vinc = horizontal ? 0 : brstate.getBoxPixelSize(1) - 1;
        if (hinc < 0) {
            hinc = 0;
        }
        if (vinc < 0) {
            vinc = 0;
        }
        int iterations = brstate.getBlurPasses();
        float spread = brstate.getSpread();
        if (horizontal && (iterations < 1 || (hinc < 1 && vinc < 1))) {
            inputs[0].addref();
            return inputs[0];
        }
        int growx = ((hinc * iterations) + 1) & (-2);
        int growy = ((vinc * iterations) + 1) & (-2);
        HeapImage src = (HeapImage) inputs[0].getUntransformedImage();
        Rectangle srcr = inputs[0].getUntransformedBounds();
        HeapImage cur = src;
        int curw = srcr.width;
        int curh = srcr.height;
        int curscan = cur.getScanlineStride();
        int[] curPixels = cur.getPixelArray();
        int finalw = curw + growx;
        int finalh = curh + growy;
        boolean force = !horizontal;
        while (true) {
            if (force || curw < finalw || curh < finalh) {
                int neww = curw + hinc;
                int newh = curh + vinc;
                if (neww > finalw) {
                    neww = finalw;
                }
                if (newh > finalh) {
                    newh = finalh;
                }
                HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(neww, newh);
                int newscan = dst.getScanlineStride();
                int[] newPixels = dst.getPixelArray();
                if (iterations == 0) {
                    spread = 0.0f;
                }
                if (horizontal) {
                    filterHorizontalBlack(newPixels, neww, newh, newscan, curPixels, curw, curh, curscan, spread);
                } else if (neww < finalw || newh < finalh) {
                    filterVerticalBlack(newPixels, neww, newh, newscan, curPixels, curw, curh, curscan, spread);
                } else {
                    float[] shadowColor = brstate.getShadowColor().getPremultipliedRGBComponents();
                    if (shadowColor[3] == 1.0f && shadowColor[0] == 0.0f && shadowColor[1] == 0.0f && shadowColor[2] == 0.0f) {
                        filterVerticalBlack(newPixels, neww, newh, newscan, curPixels, curw, curh, curscan, spread);
                    } else {
                        filterVertical(newPixels, neww, newh, newscan, curPixels, curw, curh, curscan, spread, shadowColor);
                    }
                }
                if (cur != src) {
                    getRenderer().releaseCompatibleImage(cur);
                }
                iterations--;
                force = false;
                cur = dst;
                curw = neww;
                curh = newh;
                curPixels = newPixels;
                curscan = newscan;
            } else {
                Rectangle dstBounds = new Rectangle(srcr.f11913x - (growx / 2), srcr.f11914y - (growy / 2), curw, curh);
                return new ImageData(getFilterContext(), cur, dstBounds, inputs[0].getTransform());
            }
        }
    }
}
