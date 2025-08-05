package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWBoxShadowPeer.class */
public class JSWBoxShadowPeer extends JSWEffectPeer<BoxRenderState> {
    public JSWBoxShadowPeer(FilterContext fctx, Renderer r2, String uniqueName) {
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
                Rectangle resBounds = new Rectangle(srcr.f11913x - (growx / 2), srcr.f11914y - (growy / 2), curw, curh);
                return new ImageData(getFilterContext(), cur, resBounds);
            }
        }
    }

    protected void filterHorizontalBlack(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan, float spread) {
        int hsize = (dstw - srcw) + 1;
        int amax = (int) ((hsize * 255) + ((255 - r0) * spread));
        int kscale = Integer.MAX_VALUE / amax;
        int amin = amax / 255;
        int srcoff = 0;
        int dstoff = 0;
        for (int y2 = 0; y2 < dsth; y2++) {
            int suma = 0;
            int x2 = 0;
            while (x2 < dstw) {
                int rgb = x2 >= hsize ? srcPixels[(srcoff + x2) - hsize] : 0;
                int suma2 = suma - (rgb >>> 24);
                int rgb2 = x2 < srcw ? srcPixels[srcoff + x2] : 0;
                suma = suma2 + (rgb2 >>> 24);
                dstPixels[dstoff + x2] = suma < amin ? 0 : suma >= amax ? -16777216 : ((suma * kscale) >> 23) << 24;
                x2++;
            }
            srcoff += srcscan;
            dstoff += dstscan;
        }
    }

    protected void filterVerticalBlack(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan, float spread) {
        int vsize = (dsth - srch) + 1;
        int amax = (int) ((vsize * 255) + ((255 - r0) * spread));
        int kscale = Integer.MAX_VALUE / amax;
        int amin = amax / 255;
        int voff = vsize * srcscan;
        for (int x2 = 0; x2 < dstw; x2++) {
            int suma = 0;
            int srcoff = x2;
            int dstoff = x2;
            int y2 = 0;
            while (y2 < dsth) {
                int rgb = srcoff >= voff ? srcPixels[srcoff - voff] : 0;
                int suma2 = suma - (rgb >>> 24);
                int rgb2 = y2 < srch ? srcPixels[srcoff] : 0;
                suma = suma2 + (rgb2 >>> 24);
                dstPixels[dstoff] = suma < amin ? 0 : suma >= amax ? -16777216 : ((suma * kscale) >> 23) << 24;
                srcoff += srcscan;
                dstoff += dstscan;
                y2++;
            }
        }
    }

    protected void filterVertical(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan, float spread, float[] shadowColor) {
        int vsize = (dsth - srch) + 1;
        int amax = (int) ((vsize * 255) + ((255 - r0) * spread));
        int kscalea = Integer.MAX_VALUE / amax;
        int kscaler = (int) (kscalea * shadowColor[0]);
        int kscaleg = (int) (kscalea * shadowColor[1]);
        int kscaleb = (int) (kscalea * shadowColor[2]);
        int kscalea2 = (int) (kscalea * shadowColor[3]);
        int amin = amax / 255;
        int voff = vsize * srcscan;
        int shadowRGB = (((int) (shadowColor[0] * 255.0f)) << 16) | (((int) (shadowColor[1] * 255.0f)) << 8) | ((int) (shadowColor[2] * 255.0f)) | (((int) (shadowColor[3] * 255.0f)) << 24);
        for (int x2 = 0; x2 < dstw; x2++) {
            int suma = 0;
            int srcoff = x2;
            int dstoff = x2;
            int y2 = 0;
            while (y2 < dsth) {
                int rgb = srcoff >= voff ? srcPixels[srcoff - voff] : 0;
                int suma2 = suma - (rgb >>> 24);
                int rgb2 = y2 < srch ? srcPixels[srcoff] : 0;
                suma = suma2 + (rgb2 >>> 24);
                dstPixels[dstoff] = suma < amin ? 0 : suma >= amax ? shadowRGB : (((suma * kscalea2) >> 23) << 24) | (((suma * kscaler) >> 23) << 16) | (((suma * kscaleg) >> 23) << 8) | ((suma * kscaleb) >> 23);
                srcoff += srcscan;
                dstoff += dstscan;
                y2++;
            }
        }
    }
}
