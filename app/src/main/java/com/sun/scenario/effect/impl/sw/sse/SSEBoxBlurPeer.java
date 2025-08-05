package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEBoxBlurPeer.class */
public class SSEBoxBlurPeer extends SSEEffectPeer<BoxRenderState> {
    private static native void filterHorizontal(int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5, int i6, int i7);

    private static native void filterVertical(int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5, int i6, int i7);

    public SSEBoxBlurPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, BoxRenderState brstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        setRenderState(brstate);
        boolean horizontal = getPass() == 0;
        int hinc = horizontal ? brstate.getBoxPixelSize(0) - 1 : 0;
        int vinc = horizontal ? 0 : brstate.getBoxPixelSize(1) - 1;
        int iterations = brstate.getBlurPasses();
        if (iterations < 1 || (hinc < 1 && vinc < 1)) {
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
        while (true) {
            if (curw < finalw || curh < finalh) {
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
                if (horizontal) {
                    filterHorizontal(newPixels, neww, newh, newscan, curPixels, curw, curh, curscan);
                } else {
                    filterVertical(newPixels, neww, newh, newscan, curPixels, curw, curh, curscan);
                }
                if (cur != src) {
                    getRenderer().releaseCompatibleImage(cur);
                }
                cur = dst;
                curw = neww;
                curh = newh;
                curPixels = newPixels;
                curscan = newscan;
            } else {
                Rectangle dstBounds = new Rectangle(srcr.f11913x - (growx / 2), srcr.f11914y - (growy / 2), curw, curh);
                return new ImageData(getFilterContext(), cur, dstBounds);
            }
        }
    }
}
