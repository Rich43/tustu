package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import java.nio.FloatBuffer;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWLinearConvolvePeer.class */
public class JSWLinearConvolvePeer extends JSWEffectPeer<LinearConvolveRenderState> {
    private static final float cmin = 1.0f;
    private static final float cmax = 254.9375f;

    public JSWLinearConvolvePeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    private Rectangle getResultBounds(LinearConvolveRenderState lcrstate, Rectangle outputClip, ImageData... inputDatas) {
        Rectangle r2 = inputDatas[0].getTransformedBounds(null);
        return lcrstate.getPassResultBounds(r2, outputClip);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, LinearConvolveRenderState lcrstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        float dxcol;
        float dycol;
        float dxrow;
        float dyrow;
        setRenderState(lcrstate);
        Rectangle dstRawBounds = getResultBounds(lcrstate, (Rectangle) null, inputs);
        Rectangle dstBounds = new Rectangle(dstRawBounds);
        dstBounds.intersectWith(outputClip);
        setDestBounds(dstBounds);
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage src = (HeapImage) inputs[0].getUntransformedImage();
        int srcw = src.getPhysicalWidth();
        int srch = src.getPhysicalHeight();
        int srcscan = src.getScanlineStride();
        int[] srcPixels = src.getPixelArray();
        Rectangle src0Bounds = inputs[0].getUntransformedBounds();
        BaseTransform src0Transform = inputs[0].getTransform();
        Rectangle src0NativeBounds = new Rectangle(0, 0, srcw, srch);
        setInputBounds(0, src0Bounds);
        setInputTransform(0, src0Transform);
        setInputNativeBounds(0, src0NativeBounds);
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        int count = lcrstate.getPassKernelSize();
        FloatBuffer weights_buf = lcrstate.getPassWeights();
        LinearConvolveRenderState.PassType type = lcrstate.getPassType();
        if (!src0Transform.isIdentity() || !dstBounds.contains(dstRawBounds.f11913x, dstRawBounds.f11914y)) {
            type = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
        }
        if (count >= 0) {
            type = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
        }
        if (type == LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED) {
            float[] weights_arr = new float[count * 2];
            weights_buf.get(weights_arr, 0, count);
            weights_buf.rewind();
            weights_buf.get(weights_arr, count, count);
            filterHV(dstPixels, dstw, dsth, 1, dstscan, srcPixels, srcw, srch, 1, srcscan, weights_arr);
        } else if (type == LinearConvolveRenderState.PassType.VERTICAL_CENTERED) {
            float[] weights_arr2 = new float[count * 2];
            weights_buf.get(weights_arr2, 0, count);
            weights_buf.rewind();
            weights_buf.get(weights_arr2, count, count);
            filterHV(dstPixels, dsth, dstw, dstscan, 1, srcPixels, srch, srcw, srcscan, 1, weights_arr2);
        } else {
            float[] weights_arr3 = new float[count];
            weights_buf.get(weights_arr3, 0, count);
            float[] srcRect = new float[8];
            int nCoords = getTextureCoordinates(0, srcRect, src0Bounds.f11913x, src0Bounds.f11914y, src0NativeBounds.width, src0NativeBounds.height, dstBounds, src0Transform);
            float srcx0 = srcRect[0] * srcw;
            float srcy0 = srcRect[1] * srch;
            if (nCoords < 8) {
                dxcol = ((srcRect[2] - srcRect[0]) * srcw) / dstBounds.width;
                dycol = 0.0f;
                dxrow = 0.0f;
                dyrow = ((srcRect[3] - srcRect[1]) * srch) / dstBounds.height;
            } else {
                dxcol = ((srcRect[4] - srcRect[0]) * srcw) / dstBounds.width;
                dycol = ((srcRect[5] - srcRect[1]) * srch) / dstBounds.height;
                dxrow = ((srcRect[6] - srcRect[0]) * srcw) / dstBounds.width;
                dyrow = ((srcRect[7] - srcRect[1]) * srch) / dstBounds.height;
            }
            float[] offset_arr = lcrstate.getPassVector();
            float deltax = offset_arr[0] * srcw;
            float deltay = offset_arr[1] * srch;
            float offsetx = offset_arr[2] * srcw;
            float offsety = offset_arr[3] * srch;
            filterVector(dstPixels, dstw, dsth, dstscan, srcPixels, srcw, srch, srcscan, weights_arr3, count, srcx0, srcy0, offsetx, offsety, deltax, deltay, dxcol, dycol, dxrow, dyrow);
        }
        return new ImageData(getFilterContext(), dst, dstBounds);
    }

    protected void filterVector(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan, float[] weights, int count, float srcx0, float srcy0, float offsetx, float offsety, float deltax, float deltay, float dxcol, float dycol, float dxrow, float dyrow) {
        int dstrow = 0;
        float[] fvals = new float[4];
        float srcx02 = srcx0 + ((dxrow + dxcol) * 0.5f);
        float srcy02 = srcy0 + ((dyrow + dycol) * 0.5f);
        for (int dy = 0; dy < dsth; dy++) {
            float srcx = srcx02;
            float srcy = srcy02;
            for (int dx = 0; dx < dstw; dx++) {
                fvals[3] = 0.0f;
                fvals[2] = 0.0f;
                fvals[1] = 0.0f;
                fvals[0] = 0.0f;
                float sampx = srcx + offsetx;
                float sampy = srcy + offsety;
                for (int i2 = 0; i2 < count; i2++) {
                    laccumsample(srcPixels, sampx, sampy, srcw, srch, srcscan, weights[i2], fvals);
                    sampx += deltax;
                    sampy += deltay;
                }
                dstPixels[dstrow + dx] = ((fvals[3] < 1.0f ? 0 : fvals[3] > cmax ? 255 : (int) fvals[3]) << 24) + ((fvals[0] < 1.0f ? 0 : fvals[0] > cmax ? 255 : (int) fvals[0]) << 16) + ((fvals[1] < 1.0f ? 0 : fvals[1] > cmax ? 255 : (int) fvals[1]) << 8) + (fvals[2] < 1.0f ? 0 : fvals[2] > cmax ? 255 : (int) fvals[2]);
                srcx += dxcol;
                srcy += dycol;
            }
            srcx02 += dxrow;
            srcy02 += dyrow;
            dstrow += dstscan;
        }
    }

    protected void filterHV(int[] dstPixels, int dstcols, int dstrows, int dcolinc, int drowinc, int[] srcPixels, int srccols, int srcrows, int scolinc, int srowinc, float[] weights) {
        int kernelSize = weights.length / 2;
        float[] cvals = new float[kernelSize * 4];
        int dstrow = 0;
        int srcrow = 0;
        for (int r2 = 0; r2 < dstrows; r2++) {
            int dstoff = dstrow;
            int srcoff = srcrow;
            for (int i2 = 0; i2 < cvals.length; i2++) {
                cvals[i2] = 0.0f;
            }
            int koff = kernelSize;
            int c2 = 0;
            while (c2 < dstcols) {
                int i3 = (kernelSize - koff) * 4;
                int rgb = c2 < srccols ? srcPixels[srcoff] : 0;
                cvals[i3 + 0] = rgb >>> 24;
                cvals[i3 + 1] = (rgb >> 16) & 255;
                cvals[i3 + 2] = (rgb >> 8) & 255;
                cvals[i3 + 3] = rgb & 255;
                koff--;
                if (koff <= 0) {
                    koff += kernelSize;
                }
                float suma = 0.0f;
                float sumr = 0.0f;
                float sumg = 0.0f;
                float sumb = 0.0f;
                for (int i4 = 0; i4 < cvals.length; i4 += 4) {
                    float factor = weights[koff + (i4 >> 2)];
                    suma += cvals[i4 + 0] * factor;
                    sumr += cvals[i4 + 1] * factor;
                    sumg += cvals[i4 + 2] * factor;
                    sumb += cvals[i4 + 3] * factor;
                }
                dstPixels[dstoff] = ((suma < 1.0f ? 0 : suma > cmax ? 255 : (int) suma) << 24) + ((sumr < 1.0f ? 0 : sumr > cmax ? 255 : (int) sumr) << 16) + ((sumg < 1.0f ? 0 : sumg > cmax ? 255 : (int) sumg) << 8) + (sumb < 1.0f ? 0 : sumb > cmax ? 255 : (int) sumb);
                dstoff += dcolinc;
                srcoff += scolinc;
                c2++;
            }
            dstrow += drowinc;
            srcrow += srowinc;
        }
    }
}
