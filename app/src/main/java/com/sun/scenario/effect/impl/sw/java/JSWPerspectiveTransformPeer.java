package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWPerspectiveTransformPeer.class */
public class JSWPerspectiveTransformPeer extends JSWEffectPeer {
    public JSWPerspectiveTransformPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final PerspectiveTransform getEffect() {
        return (PerspectiveTransform) super.getEffect();
    }

    private float[][] getITX() {
        PerspectiveTransformState state = (PerspectiveTransformState) AccessHelper.getState(getEffect());
        return state.getITX();
    }

    private float[] getTx0() {
        Rectangle ib = getInputBounds(0);
        Rectangle nb = getInputNativeBounds(0);
        float scalex = ib.width / nb.width;
        float[] itx0 = getITX()[0];
        return new float[]{itx0[0] * scalex, itx0[1] * scalex, itx0[2] * scalex};
    }

    private float[] getTx1() {
        Rectangle ib = getInputBounds(0);
        Rectangle nb = getInputNativeBounds(0);
        float scaley = ib.height / nb.height;
        float[] itx1 = getITX()[1];
        return new float[]{itx1[0] * scaley, itx1[1] * scaley, itx1[2] * scaley};
    }

    private float[] getTx2() {
        return getITX()[2];
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public int getTextureCoordinates(int inputIndex, float[] coords, float srcX, float srcY, float srcNativeWidth, float srcNativeHeight, Rectangle dstBounds, BaseTransform transform) {
        coords[0] = dstBounds.f11913x;
        coords[1] = dstBounds.f11914y;
        coords[2] = dstBounds.f11913x + dstBounds.width;
        coords[3] = dstBounds.f11914y + dstBounds.height;
        return 4;
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        setEffect(effect);
        Rectangle dstBounds = getResultBounds(transform, outputClip, inputs);
        setDestBounds(dstBounds);
        HeapImage src0 = (HeapImage) inputs[0].getUntransformedImage();
        int src0w = src0.getPhysicalWidth();
        int src0h = src0.getPhysicalHeight();
        int src0scan = src0.getScanlineStride();
        int[] baseImg = src0.getPixelArray();
        Rectangle src0Bounds = new Rectangle(0, 0, src0w, src0h);
        Rectangle src0InputBounds = inputs[0].getUntransformedBounds();
        BaseTransform src0Transform = inputs[0].getTransform();
        setInputBounds(0, src0InputBounds);
        setInputNativeBounds(0, src0Bounds);
        float[] baseImg_vals = new float[4];
        float[] src0Rect = new float[4];
        getTextureCoordinates(0, src0Rect, src0InputBounds.f11913x, src0InputBounds.f11914y, src0w, src0h, dstBounds, src0Transform);
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        float[] tx1_arr = getTx1();
        float tx1_x = tx1_arr[0];
        float tx1_y = tx1_arr[1];
        float tx1_z = tx1_arr[2];
        float[] tx0_arr = getTx0();
        float tx0_x = tx0_arr[0];
        float tx0_y = tx0_arr[1];
        float tx0_z = tx0_arr[2];
        float[] tx2_arr = getTx2();
        float tx2_x = tx2_arr[0];
        float tx2_y = tx2_arr[1];
        float tx2_z = tx2_arr[2];
        float inc0_x = (src0Rect[2] - src0Rect[0]) / dstw;
        float inc0_y = (src0Rect[3] - src0Rect[1]) / dsth;
        float pos0_y = src0Rect[1] + (inc0_y * 0.5f);
        for (int dy = 0; dy < 0 + dsth; dy++) {
            float f2 = dy;
            int dyi = dy * dstscan;
            float pos0_x = src0Rect[0] + (inc0_x * 0.5f);
            for (int dx = 0; dx < 0 + dstw; dx++) {
                float f3 = dx;
                float p1_x = pos0_x;
                float p1_y = pos0_y;
                float dot_res = (p1_x * tx2_x) + (p1_y * tx2_y) + (1.0f * tx2_z);
                float p2_x = (((p1_x * tx0_x) + (p1_y * tx0_y)) + (1.0f * tx0_z)) / dot_res;
                float p2_y = (((p1_x * tx1_x) + (p1_y * tx1_y)) + (1.0f * tx1_z)) / dot_res;
                lsample(baseImg, p2_x, p2_y, src0w, src0h, src0scan, baseImg_vals);
                float sample_res_x = baseImg_vals[0];
                float sample_res_y = baseImg_vals[1];
                float sample_res_z = baseImg_vals[2];
                float sample_res_w = baseImg_vals[3];
                float color_x = sample_res_x;
                float color_y = sample_res_y;
                float color_z = sample_res_z;
                float color_w = sample_res_w;
                if (color_w < 0.0f) {
                    color_w = 0.0f;
                } else if (color_w > 1.0f) {
                    color_w = 1.0f;
                }
                if (color_x < 0.0f) {
                    color_x = 0.0f;
                } else if (color_x > color_w) {
                    color_x = color_w;
                }
                if (color_y < 0.0f) {
                    color_y = 0.0f;
                } else if (color_y > color_w) {
                    color_y = color_w;
                }
                if (color_z < 0.0f) {
                    color_z = 0.0f;
                } else if (color_z > color_w) {
                    color_z = color_w;
                }
                dstPixels[dyi + dx] = (((int) (color_x * 255.0f)) << 16) | (((int) (color_y * 255.0f)) << 8) | (((int) (color_z * 255.0f)) << 0) | (((int) (color_w * 255.0f)) << 24);
                pos0_x += inc0_x;
            }
            pos0_y += inc0_y;
        }
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
