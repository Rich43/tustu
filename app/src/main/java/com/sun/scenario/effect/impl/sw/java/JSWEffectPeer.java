package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWEffectPeer.class */
public abstract class JSWEffectPeer<T extends RenderState> extends EffectPeer<T> {
    protected static final int FVALS_A = 3;
    protected static final int FVALS_R = 0;
    protected static final int FVALS_G = 1;
    protected static final int FVALS_B = 2;

    protected JSWEffectPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    protected final void laccum(int pixel, float mul, float[] fvals) {
        float mul2 = mul / 255.0f;
        fvals[0] = fvals[0] + (((pixel >> 16) & 255) * mul2);
        fvals[1] = fvals[1] + (((pixel >> 8) & 255) * mul2);
        fvals[2] = fvals[2] + ((pixel & 255) * mul2);
        fvals[3] = fvals[3] + ((pixel >>> 24) * mul2);
    }

    protected final void lsample(int[] img, float floc_x, float floc_y, int w2, int h2, int scan, float[] fvals) {
        fvals[0] = 0.0f;
        fvals[1] = 0.0f;
        fvals[2] = 0.0f;
        fvals[3] = 0.0f;
        float floc_x2 = (floc_x * w2) + 0.5f;
        float floc_y2 = (floc_y * h2) + 0.5f;
        int iloc_x = (int) floc_x2;
        int iloc_y = (int) floc_y2;
        if (floc_x2 > 0.0f && floc_y2 > 0.0f && iloc_x <= w2 && iloc_y <= h2) {
            float floc_x3 = floc_x2 - iloc_x;
            float floc_y3 = floc_y2 - iloc_y;
            int offset = (iloc_y * scan) + iloc_x;
            float fract = floc_x3 * floc_y3;
            if (iloc_y < h2) {
                if (iloc_x < w2) {
                    laccum(img[offset], fract, fvals);
                }
                if (iloc_x > 0) {
                    laccum(img[offset - 1], floc_y3 - fract, fvals);
                }
            }
            if (iloc_y > 0) {
                if (iloc_x < w2) {
                    laccum(img[offset - scan], floc_x3 - fract, fvals);
                }
                if (iloc_x > 0) {
                    laccum(img[(offset - scan) - 1], ((1.0f - floc_x3) - floc_y3) + fract, fvals);
                }
            }
        }
    }

    protected final void laccumsample(int[] img, float fpix_x, float fpix_y, int w2, int h2, int scan, float factor, float[] fvals) {
        float factor2 = factor * 255.0f;
        float fpix_x2 = fpix_x + 0.5f;
        float fpix_y2 = fpix_y + 0.5f;
        int ipix_x = (int) fpix_x2;
        int ipix_y = (int) fpix_y2;
        if (fpix_x2 > 0.0f && fpix_y2 > 0.0f && ipix_x <= w2 && ipix_y <= h2) {
            float fpix_x3 = fpix_x2 - ipix_x;
            float fpix_y3 = fpix_y2 - ipix_y;
            int offset = (ipix_y * scan) + ipix_x;
            float fract = fpix_x3 * fpix_y3;
            if (ipix_y < h2) {
                if (ipix_x < w2) {
                    laccum(img[offset], fract * factor2, fvals);
                }
                if (ipix_x > 0) {
                    laccum(img[offset - 1], (fpix_y3 - fract) * factor2, fvals);
                }
            }
            if (ipix_y > 0) {
                if (ipix_x < w2) {
                    laccum(img[offset - scan], (fpix_x3 - fract) * factor2, fvals);
                }
                if (ipix_x > 0) {
                    laccum(img[(offset - scan) - 1], (((1.0f - fpix_x3) - fpix_y3) + fract) * factor2, fvals);
                }
            }
        }
    }

    protected final void faccum(float[] map, int offset, float mul, float[] fvals) {
        fvals[0] = fvals[0] + (map[offset] * mul);
        fvals[1] = fvals[1] + (map[offset + 1] * mul);
        fvals[2] = fvals[2] + (map[offset + 2] * mul);
        fvals[3] = fvals[3] + (map[offset + 3] * mul);
    }

    protected final void fsample(float[] map, float floc_x, float floc_y, int w2, int h2, int scan, float[] fvals) {
        fvals[0] = 0.0f;
        fvals[1] = 0.0f;
        fvals[2] = 0.0f;
        fvals[3] = 0.0f;
        float floc_x2 = (floc_x * w2) + 0.5f;
        float floc_y2 = (floc_y * h2) + 0.5f;
        int iloc_x = (int) floc_x2;
        int iloc_y = (int) floc_y2;
        if (floc_x2 > 0.0f && floc_y2 > 0.0f && iloc_x <= w2 && iloc_y <= h2) {
            float floc_x3 = floc_x2 - iloc_x;
            float floc_y3 = floc_y2 - iloc_y;
            int offset = 4 * ((iloc_y * scan) + iloc_x);
            float fract = floc_x3 * floc_y3;
            if (iloc_y < h2) {
                if (iloc_x < w2) {
                    faccum(map, offset, fract, fvals);
                }
                if (iloc_x > 0) {
                    faccum(map, offset - 4, floc_y3 - fract, fvals);
                }
            }
            if (iloc_y > 0) {
                if (iloc_x < w2) {
                    faccum(map, offset - (scan * 4), floc_x3 - fract, fvals);
                }
                if (iloc_x > 0) {
                    faccum(map, (offset - (scan * 4)) - 4, ((1.0f - floc_x3) - floc_y3) + fract, fvals);
                }
            }
        }
    }
}
