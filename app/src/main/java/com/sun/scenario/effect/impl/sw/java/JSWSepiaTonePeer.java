package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWSepiaTonePeer.class */
public class JSWSepiaTonePeer extends JSWEffectPeer {
    public JSWSepiaTonePeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final SepiaTone getEffect() {
        return (SepiaTone) super.getEffect();
    }

    private float getLevel() {
        return getEffect().getLevel();
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        int baseImg_tmp;
        setEffect(effect);
        Rectangle dstBounds = getResultBounds(transform, outputClip, inputs);
        setDestBounds(dstBounds);
        HeapImage src0 = (HeapImage) inputs[0].getTransformedImage(dstBounds);
        int src0w = src0.getPhysicalWidth();
        int src0h = src0.getPhysicalHeight();
        int src0scan = src0.getScanlineStride();
        int[] baseImg = src0.getPixelArray();
        Rectangle src0Bounds = new Rectangle(0, 0, src0w, src0h);
        Rectangle src0InputBounds = inputs[0].getTransformedBounds(dstBounds);
        BaseTransform src0Transform = BaseTransform.IDENTITY_TRANSFORM;
        setInputBounds(0, src0InputBounds);
        setInputNativeBounds(0, src0Bounds);
        float[] src0Rect = new float[4];
        getTextureCoordinates(0, src0Rect, src0InputBounds.f11913x, src0InputBounds.f11914y, src0w, src0h, dstBounds, src0Transform);
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        float level = getLevel();
        float inc0_x = (src0Rect[2] - src0Rect[0]) / dstw;
        float inc0_y = (src0Rect[3] - src0Rect[1]) / dsth;
        float pos0_y = src0Rect[1] + (inc0_y * 0.5f);
        for (int dy = 0; dy < 0 + dsth; dy++) {
            float f2 = dy;
            int dyi = dy * dstscan;
            float pos0_x = src0Rect[0] + (inc0_x * 0.5f);
            for (int dx = 0; dx < 0 + dstw; dx++) {
                float f3 = dx;
                float loc_tmp_x = pos0_x;
                float loc_tmp_y = pos0_y;
                if (loc_tmp_x >= 0.0f && loc_tmp_y >= 0.0f) {
                    int iloc_tmp_x = (int) (loc_tmp_x * src0w);
                    int iloc_tmp_y = (int) (loc_tmp_y * src0h);
                    boolean out = iloc_tmp_x >= src0w || iloc_tmp_y >= src0h;
                    baseImg_tmp = out ? 0 : baseImg[(iloc_tmp_y * src0scan) + iloc_tmp_x];
                } else {
                    baseImg_tmp = 0;
                }
                float sample_res_x = ((baseImg_tmp >> 16) & 255) / 255.0f;
                float sample_res_y = ((baseImg_tmp >> 8) & 255) / 255.0f;
                float sample_res_z = (baseImg_tmp & 255) / 255.0f;
                float sample_res_w = (baseImg_tmp >>> 24) / 255.0f;
                float dot_res = (sample_res_x * 0.3f) + (sample_res_y * 0.59f) + (sample_res_z * 0.11f);
                float sep_x = dot_res * 1.6f;
                float sep_y = dot_res * 1.2f;
                float sep_z = dot_res * 0.9f;
                float a_tmp = 1.0f - level;
                float mix_res_x = (sep_x * (1.0f - a_tmp)) + (sample_res_x * a_tmp);
                float mix_res_y = (sep_y * (1.0f - a_tmp)) + (sample_res_y * a_tmp);
                float mix_res_z = (sep_z * (1.0f - a_tmp)) + (sample_res_z * a_tmp);
                float color_x = mix_res_x;
                float color_y = mix_res_y;
                float color_z = mix_res_z;
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
        inputs[0].releaseTransformedImage(src0);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
