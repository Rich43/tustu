package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWColorAdjustPeer.class */
public class JSWColorAdjustPeer extends JSWEffectPeer {
    public JSWColorAdjustPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final ColorAdjust getEffect() {
        return (ColorAdjust) super.getEffect();
    }

    private float getHue() {
        return getEffect().getHue() / 2.0f;
    }

    private float getSaturation() {
        return getEffect().getSaturation() + 1.0f;
    }

    private float getBrightness() {
        return getEffect().getBrightness() + 1.0f;
    }

    private float getContrast() {
        float c2 = getEffect().getContrast();
        if (c2 > 0.0f) {
            c2 *= 3.0f;
        }
        return c2 + 1.0f;
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        int baseImg_tmp;
        float f2;
        float f3;
        float f4;
        float f5;
        float h2;
        float s2;
        float hsb_y;
        float hsb_z;
        float f6;
        float f7;
        float res_x;
        float res_y;
        float res_z;
        float h3;
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
        float saturation = getSaturation();
        float brightness = getBrightness();
        float contrast = getContrast();
        float hue = getHue();
        float inc0_x = (src0Rect[2] - src0Rect[0]) / dstw;
        float inc0_y = (src0Rect[3] - src0Rect[1]) / dsth;
        float pos0_y = src0Rect[1] + (inc0_y * 0.5f);
        for (int dy = 0; dy < 0 + dsth; dy++) {
            float f8 = dy;
            int dyi = dy * dstscan;
            float pos0_x = src0Rect[0] + (inc0_x * 0.5f);
            for (int dx = 0; dx < 0 + dstw; dx++) {
                float f9 = dx;
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
                float src_x = sample_res_x;
                float src_y = sample_res_y;
                float src_z = sample_res_z;
                if (sample_res_w > 0.0f) {
                    src_x /= sample_res_w;
                    src_y /= sample_res_w;
                    src_z /= sample_res_w;
                }
                float src_x2 = ((src_x - 0.5f) * contrast) + 0.5f;
                float src_y2 = ((src_y - 0.5f) * contrast) + 0.5f;
                float src_z2 = ((src_z - 0.5f) * contrast) + 0.5f;
                if (src_x2 > src_y2) {
                    f2 = src_x2;
                } else {
                    f2 = src_y2;
                }
                float max_res = f2;
                if (max_res > src_z2) {
                    f3 = max_res;
                } else {
                    f3 = src_z2;
                }
                float max_res2 = f3;
                if (src_x2 < src_y2) {
                    f4 = src_x2;
                } else {
                    f4 = src_y2;
                }
                float min_res = f4;
                if (min_res < src_z2) {
                    f5 = min_res;
                } else {
                    f5 = src_z2;
                }
                float min_res2 = f5;
                if (max_res2 > min_res2) {
                    float c_x = (max_res2 - src_x2) / (max_res2 - min_res2);
                    float c_y = (max_res2 - src_y2) / (max_res2 - min_res2);
                    float c_z = (max_res2 - src_z2) / (max_res2 - min_res2);
                    if (src_x2 == max_res2) {
                        h3 = c_z - c_y;
                    } else if (src_y2 == max_res2) {
                        h3 = (2.0f + c_x) - c_z;
                    } else {
                        h3 = (4.0f + c_y) - c_x;
                    }
                    h2 = h3 / 6.0f;
                    if (h2 < 0.0f) {
                        h2 += 1.0f;
                    }
                    s2 = (max_res2 - min_res2) / max_res2;
                } else {
                    h2 = 0.0f;
                    s2 = 0.0f;
                }
                float rgb_to_hsb_res_x = h2;
                float rgb_to_hsb_res_y = s2;
                float hsb_x = rgb_to_hsb_res_x + hue;
                if (hsb_x < 0.0f) {
                    hsb_x += 1.0f;
                } else if (hsb_x > 1.0f) {
                    hsb_x -= 1.0f;
                }
                if (saturation > 1.0f) {
                    float sat = saturation - 1.0f;
                    hsb_y = rgb_to_hsb_res_y + ((1.0f - rgb_to_hsb_res_y) * sat);
                } else {
                    hsb_y = rgb_to_hsb_res_y * saturation;
                }
                if (brightness > 1.0f) {
                    float brt = brightness - 1.0f;
                    hsb_y *= 1.0f - brt;
                    hsb_z = max_res2 + ((1.0f - max_res2) * brt);
                } else {
                    hsb_z = max_res2 * brightness;
                }
                float val_tmp_x = hsb_y;
                float val_tmp_y = hsb_z;
                if (val_tmp_x < 0.0f) {
                    f6 = 0.0f;
                } else {
                    f6 = val_tmp_x > 1.0f ? 1.0f : val_tmp_x;
                }
                float clamp_res_x = f6;
                if (val_tmp_y < 0.0f) {
                    f7 = 0.0f;
                } else {
                    f7 = val_tmp_y > 1.0f ? 1.0f : val_tmp_y;
                }
                float clamp_res_y = f7;
                float v_tmp_x = hsb_x;
                float floor_res = (float) Math.floor(v_tmp_x);
                float h4 = (v_tmp_x - floor_res) * 6.0f;
                float floor_res2 = (float) Math.floor(h4);
                float f10 = h4 - floor_res2;
                float p2 = clamp_res_y * (1.0f - clamp_res_x);
                float q2 = clamp_res_y * (1.0f - (clamp_res_x * f10));
                float t2 = clamp_res_y * (1.0f - (clamp_res_x * (1.0f - f10)));
                float floor_res3 = (float) Math.floor(h4);
                if (floor_res3 < 1.0f) {
                    res_x = clamp_res_y;
                    res_y = t2;
                    res_z = p2;
                } else if (floor_res3 < 2.0f) {
                    res_x = q2;
                    res_y = clamp_res_y;
                    res_z = p2;
                } else if (floor_res3 < 3.0f) {
                    res_x = p2;
                    res_y = clamp_res_y;
                    res_z = t2;
                } else if (floor_res3 < 4.0f) {
                    res_x = p2;
                    res_y = q2;
                    res_z = clamp_res_y;
                } else if (floor_res3 < 5.0f) {
                    res_x = t2;
                    res_y = p2;
                    res_z = clamp_res_y;
                } else {
                    res_x = clamp_res_y;
                    res_y = p2;
                    res_z = q2;
                }
                float hsb_to_rgb_res_x = res_x;
                float hsb_to_rgb_res_y = res_y;
                float hsb_to_rgb_res_z = res_z;
                float color_x = sample_res_w * hsb_to_rgb_res_x;
                float color_y = sample_res_w * hsb_to_rgb_res_y;
                float color_z = sample_res_w * hsb_to_rgb_res_z;
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
