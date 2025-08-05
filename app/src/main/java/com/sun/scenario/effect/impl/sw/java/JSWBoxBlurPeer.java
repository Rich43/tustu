package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWBoxBlurPeer.class */
public class JSWBoxBlurPeer extends JSWEffectPeer<BoxRenderState> {
    public JSWBoxBlurPeer(FilterContext fctx, Renderer r2, String uniqueName) {
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
                Rectangle resBounds = new Rectangle(srcr.f11913x - (growx / 2), srcr.f11914y - (growy / 2), curw, curh);
                return new ImageData(getFilterContext(), cur, resBounds);
            }
        }
    }

    protected void filterHorizontal(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan) {
        int hsize = (dstw - srcw) + 1;
        int kscale = Integer.MAX_VALUE / (hsize * 255);
        int srcoff = 0;
        int dstoff = 0;
        for (int y2 = 0; y2 < dsth; y2++) {
            int suma = 0;
            int sumr = 0;
            int sumg = 0;
            int sumb = 0;
            int x2 = 0;
            while (x2 < dstw) {
                int rgb = x2 >= hsize ? srcPixels[(srcoff + x2) - hsize] : 0;
                int suma2 = suma - (rgb >>> 24);
                int sumr2 = sumr - ((rgb >> 16) & 255);
                int sumg2 = sumg - ((rgb >> 8) & 255);
                int sumb2 = sumb - (rgb & 255);
                int rgb2 = x2 < srcw ? srcPixels[srcoff + x2] : 0;
                suma = suma2 + (rgb2 >>> 24);
                sumr = sumr2 + ((rgb2 >> 16) & 255);
                sumg = sumg2 + ((rgb2 >> 8) & 255);
                sumb = sumb2 + (rgb2 & 255);
                dstPixels[dstoff + x2] = (((suma * kscale) >> 23) << 24) + (((sumr * kscale) >> 23) << 16) + (((sumg * kscale) >> 23) << 8) + ((sumb * kscale) >> 23);
                x2++;
            }
            srcoff += srcscan;
            dstoff += dstscan;
        }
    }

    protected void filterVertical(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan) {
        int vsize = (dsth - srch) + 1;
        int kscale = Integer.MAX_VALUE / (vsize * 255);
        int voff = vsize * srcscan;
        for (int x2 = 0; x2 < dstw; x2++) {
            int suma = 0;
            int sumr = 0;
            int sumg = 0;
            int sumb = 0;
            int srcoff = x2;
            int dstoff = x2;
            int y2 = 0;
            while (y2 < dsth) {
                int rgb = srcoff >= voff ? srcPixels[srcoff - voff] : 0;
                int suma2 = suma - (rgb >>> 24);
                int sumr2 = sumr - ((rgb >> 16) & 255);
                int sumg2 = sumg - ((rgb >> 8) & 255);
                int sumb2 = sumb - (rgb & 255);
                int rgb2 = y2 < srch ? srcPixels[srcoff] : 0;
                suma = suma2 + (rgb2 >>> 24);
                sumr = sumr2 + ((rgb2 >> 16) & 255);
                sumg = sumg2 + ((rgb2 >> 8) & 255);
                sumb = sumb2 + (rgb2 & 255);
                dstPixels[dstoff] = (((suma * kscale) >> 23) << 24) + (((sumr * kscale) >> 23) << 16) + (((sumg * kscale) >> 23) << 8) + ((sumb * kscale) >> 23);
                srcoff += srcscan;
                dstoff += dstscan;
                y2++;
            }
        }
    }
}
