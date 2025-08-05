package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWLinearConvolveShadowPeer.class */
public class JSWLinearConvolveShadowPeer extends JSWLinearConvolvePeer {
    public JSWLinearConvolveShadowPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private float[] getShadowColor() {
        return ((LinearConvolveRenderState) getRenderState()).getPassShadowColorComponents();
    }

    @Override // com.sun.scenario.effect.impl.sw.java.JSWLinearConvolvePeer
    protected void filterVector(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan, float[] weights, int count, float srcx0, float srcy0, float offsetx, float offsety, float deltax, float deltay, float dxcol, float dycol, float dxrow, float dyrow) {
        float[] shadowColor = getShadowColor();
        int dstrow = 0;
        float srcx02 = srcx0 + ((dxrow + dxcol) * 0.5f);
        float srcy02 = srcy0 + ((dyrow + dycol) * 0.5f);
        for (int dy = 0; dy < dsth; dy++) {
            float srcx = srcx02;
            float srcy = srcy02;
            for (int dx = 0; dx < dstw; dx++) {
                float sum = 0.0f;
                float sampx = srcx + offsetx;
                float sampy = srcy + offsety;
                for (int i2 = 0; i2 < count; i2++) {
                    if (sampx >= 0.0f && sampy >= 0.0f) {
                        int ix = (int) sampx;
                        int iy = (int) sampy;
                        if (ix < srcw && iy < srch) {
                            int argb = srcPixels[(iy * srcscan) + ix];
                            sum += (argb >>> 24) * weights[i2];
                        }
                    }
                    sampx += deltax;
                    sampy += deltay;
                }
                float sum2 = sum < 0.0f ? 0.0f : sum > 255.0f ? 255.0f : sum;
                dstPixels[dstrow + dx] = (((int) (shadowColor[0] * sum2)) << 16) | (((int) (shadowColor[1] * sum2)) << 8) | ((int) (shadowColor[2] * sum2)) | (((int) (shadowColor[3] * sum2)) << 24);
                srcx += dxcol;
                srcy += dycol;
            }
            srcx02 += dxrow;
            srcy02 += dyrow;
            dstrow += dstscan;
        }
    }

    @Override // com.sun.scenario.effect.impl.sw.java.JSWLinearConvolvePeer
    protected void filterHV(int[] dstPixels, int dstcols, int dstrows, int dcolinc, int drowinc, int[] srcPixels, int srccols, int srcrows, int scolinc, int srowinc, float[] weights) {
        float[] shadowColor = getShadowColor();
        int kernelSize = weights.length / 2;
        float[] avals = new float[kernelSize];
        int dstrow = 0;
        int srcrow = 0;
        int[] shadowRGBs = new int[256];
        for (int i2 = 0; i2 < shadowRGBs.length; i2++) {
            shadowRGBs[i2] = (((int) (shadowColor[0] * i2)) << 16) | (((int) (shadowColor[1] * i2)) << 8) | ((int) (shadowColor[2] * i2)) | (((int) (shadowColor[3] * i2)) << 24);
        }
        for (int r2 = 0; r2 < dstrows; r2++) {
            int dstoff = dstrow;
            int srcoff = srcrow;
            for (int i3 = 0; i3 < avals.length; i3++) {
                avals[i3] = 0.0f;
            }
            int koff = kernelSize;
            int c2 = 0;
            while (c2 < dstcols) {
                avals[kernelSize - koff] = (c2 < srccols ? srcPixels[srcoff] : 0) >>> 24;
                koff--;
                if (koff <= 0) {
                    koff += kernelSize;
                }
                float sum = -0.5f;
                for (int i4 = 0; i4 < avals.length; i4++) {
                    sum += avals[i4] * weights[koff + i4];
                }
                dstPixels[dstoff] = sum < 0.0f ? 0 : sum >= 254.0f ? shadowRGBs[255] : shadowRGBs[((int) sum) + 1];
                dstoff += dcolinc;
                srcoff += scolinc;
                c2++;
            }
            dstrow += drowinc;
            srcrow += srowinc;
        }
    }
}
