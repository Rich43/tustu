package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWBlend_DARKENPeer.class */
public class JSWBlend_DARKENPeer extends JSWEffectPeer {
    public JSWBlend_DARKENPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final Blend getEffect() {
        return (Blend) super.getEffect();
    }

    private float getOpacity() {
        return getEffect().getOpacity();
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        int botImg_tmp;
        int topImg_tmp;
        setEffect(effect);
        Rectangle dstBounds = getResultBounds(transform, outputClip, inputs);
        setDestBounds(dstBounds);
        HeapImage src0 = (HeapImage) inputs[0].getTransformedImage(dstBounds);
        int src0w = src0.getPhysicalWidth();
        int src0h = src0.getPhysicalHeight();
        int src0scan = src0.getScanlineStride();
        int[] botImg = src0.getPixelArray();
        Rectangle src0Bounds = new Rectangle(0, 0, src0w, src0h);
        Rectangle src0InputBounds = inputs[0].getTransformedBounds(dstBounds);
        BaseTransform src0Transform = BaseTransform.IDENTITY_TRANSFORM;
        setInputBounds(0, src0InputBounds);
        setInputNativeBounds(0, src0Bounds);
        HeapImage src1 = (HeapImage) inputs[1].getTransformedImage(dstBounds);
        int src1w = src1.getPhysicalWidth();
        int src1h = src1.getPhysicalHeight();
        int src1scan = src1.getScanlineStride();
        int[] topImg = src1.getPixelArray();
        Rectangle src1Bounds = new Rectangle(0, 0, src1w, src1h);
        Rectangle src1InputBounds = inputs[1].getTransformedBounds(dstBounds);
        BaseTransform src1Transform = BaseTransform.IDENTITY_TRANSFORM;
        setInputBounds(1, src1InputBounds);
        setInputNativeBounds(1, src1Bounds);
        float[] src0Rect = new float[4];
        getTextureCoordinates(0, src0Rect, src0InputBounds.f11913x, src0InputBounds.f11914y, src0w, src0h, dstBounds, src0Transform);
        float[] src1Rect = new float[4];
        getTextureCoordinates(1, src1Rect, src1InputBounds.f11913x, src1InputBounds.f11914y, src1w, src1h, dstBounds, src1Transform);
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        float opacity = getOpacity();
        float inc0_x = (src0Rect[2] - src0Rect[0]) / dstw;
        float inc0_y = (src0Rect[3] - src0Rect[1]) / dsth;
        float inc1_x = (src1Rect[2] - src1Rect[0]) / dstw;
        float inc1_y = (src1Rect[3] - src1Rect[1]) / dsth;
        float pos0_y = src0Rect[1] + (inc0_y * 0.5f);
        float pos1_y = src1Rect[1] + (inc1_y * 0.5f);
        for (int dy = 0; dy < 0 + dsth; dy++) {
            float f2 = dy;
            int dyi = dy * dstscan;
            float pos0_x = src0Rect[0] + (inc0_x * 0.5f);
            float pos1_x = src1Rect[0] + (inc1_x * 0.5f);
            for (int dx = 0; dx < 0 + dstw; dx++) {
                float f3 = dx;
                float loc_tmp_x = pos0_x;
                float loc_tmp_y = pos0_y;
                if (loc_tmp_x >= 0.0f && loc_tmp_y >= 0.0f) {
                    int iloc_tmp_x = (int) (loc_tmp_x * src0w);
                    int iloc_tmp_y = (int) (loc_tmp_y * src0h);
                    boolean out = iloc_tmp_x >= src0w || iloc_tmp_y >= src0h;
                    botImg_tmp = out ? 0 : botImg[(iloc_tmp_y * src0scan) + iloc_tmp_x];
                } else {
                    botImg_tmp = 0;
                }
                float sample_res_x = ((botImg_tmp >> 16) & 255) / 255.0f;
                float sample_res_y = ((botImg_tmp >> 8) & 255) / 255.0f;
                float sample_res_z = (botImg_tmp & 255) / 255.0f;
                float sample_res_w = (botImg_tmp >>> 24) / 255.0f;
                float loc_tmp_x2 = pos1_x;
                float loc_tmp_y2 = pos1_y;
                if (loc_tmp_x2 >= 0.0f && loc_tmp_y2 >= 0.0f) {
                    int iloc_tmp_x2 = (int) (loc_tmp_x2 * src1w);
                    int iloc_tmp_y2 = (int) (loc_tmp_y2 * src1h);
                    boolean out2 = iloc_tmp_x2 >= src1w || iloc_tmp_y2 >= src1h;
                    topImg_tmp = out2 ? 0 : topImg[(iloc_tmp_y2 * src1scan) + iloc_tmp_x2];
                } else {
                    topImg_tmp = 0;
                }
                float top_x = (((topImg_tmp >> 16) & 255) / 255.0f) * opacity;
                float top_y = (((topImg_tmp >> 8) & 255) / 255.0f) * opacity;
                float top_z = ((topImg_tmp & 255) / 255.0f) * opacity;
                float top_w = ((topImg_tmp >>> 24) / 255.0f) * opacity;
                float res_w = (sample_res_w + top_w) - (sample_res_w * top_w);
                float x_tmp_x = top_w * sample_res_x;
                float x_tmp_y = top_w * sample_res_y;
                float x_tmp_z = top_w * sample_res_z;
                float y_tmp_x = sample_res_w * top_x;
                float y_tmp_y = sample_res_w * top_y;
                float y_tmp_z = sample_res_w * top_z;
                float max_res_x = x_tmp_x > y_tmp_x ? x_tmp_x : y_tmp_x;
                float max_res_y = x_tmp_y > y_tmp_y ? x_tmp_y : y_tmp_y;
                float max_res_z = x_tmp_z > y_tmp_z ? x_tmp_z : y_tmp_z;
                float res_x = (sample_res_x + top_x) - max_res_x;
                float res_y = (sample_res_y + top_y) - max_res_y;
                float res_z = (sample_res_z + top_z) - max_res_z;
                float color_x = res_x;
                float color_y = res_y;
                float color_z = res_z;
                float color_w = res_w;
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
                pos1_x += inc1_x;
            }
            pos0_y += inc0_y;
            pos1_y += inc1_y;
        }
        inputs[0].releaseTransformedImage(src0);
        inputs[1].releaseTransformedImage(src1);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
