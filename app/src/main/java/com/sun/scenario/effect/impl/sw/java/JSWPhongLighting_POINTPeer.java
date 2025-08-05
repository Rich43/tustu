package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PhongLighting;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import java.nio.FloatBuffer;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWPhongLighting_POINTPeer.class */
public class JSWPhongLighting_POINTPeer extends JSWEffectPeer {
    private FloatBuffer kvals;

    public JSWPhongLighting_POINTPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final PhongLighting getEffect() {
        return (PhongLighting) super.getEffect();
    }

    private float getSurfaceScale() {
        return getEffect().getSurfaceScale();
    }

    private float getDiffuseConstant() {
        return getEffect().getDiffuseConstant();
    }

    private float getSpecularConstant() {
        return getEffect().getSpecularConstant();
    }

    private float getSpecularExponent() {
        return getEffect().getSpecularExponent();
    }

    private float[] getNormalizedLightPosition() {
        return getEffect().getLight().getNormalizedLightPosition();
    }

    private float[] getLightPosition() {
        PointLight plight = (PointLight) getEffect().getLight();
        return new float[]{plight.getX(), plight.getY(), plight.getZ()};
    }

    private float[] getLightColor() {
        return getEffect().getLight().getColor().getPremultipliedRGBComponents();
    }

    private float getLightSpecularExponent() {
        return ((SpotLight) getEffect().getLight()).getSpecularExponent();
    }

    private float[] getNormalizedLightDirection() {
        return ((SpotLight) getEffect().getLight()).getNormalizedLightDirection();
    }

    private FloatBuffer getKvals() {
        Rectangle bumpImgBounds = getInputNativeBounds(0);
        float xoff = 1.0f / bumpImgBounds.width;
        float yoff = 1.0f / bumpImgBounds.height;
        float[] kx = {-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f};
        float[] ky = {-1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f};
        if (this.kvals == null) {
            this.kvals = BufferUtil.newFloatBuffer(32);
        }
        this.kvals.clear();
        int kidx = 0;
        float factor = (-getSurfaceScale()) * 0.25f;
        for (int i2 = -1; i2 <= 1; i2++) {
            for (int j2 = -1; j2 <= 1; j2++) {
                if (i2 != 0 || j2 != 0) {
                    this.kvals.put(j2 * xoff);
                    this.kvals.put(i2 * yoff);
                    this.kvals.put(kx[kidx] * factor);
                    this.kvals.put(ky[kidx] * factor);
                }
                kidx++;
            }
        }
        this.kvals.rewind();
        return this.kvals;
    }

    private int getKvalsArrayLength() {
        return 8;
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        int origImg_tmp;
        int bumpImg_tmp;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        int i2;
        setEffect(effect);
        Rectangle dstBounds = getResultBounds(transform, outputClip, inputs);
        setDestBounds(dstBounds);
        HeapImage src0 = (HeapImage) inputs[0].getTransformedImage(dstBounds);
        int src0w = src0.getPhysicalWidth();
        int src0h = src0.getPhysicalHeight();
        int src0scan = src0.getScanlineStride();
        int[] bumpImg = src0.getPixelArray();
        Rectangle src0Bounds = new Rectangle(0, 0, src0w, src0h);
        Rectangle src0InputBounds = inputs[0].getTransformedBounds(dstBounds);
        BaseTransform src0Transform = BaseTransform.IDENTITY_TRANSFORM;
        setInputBounds(0, src0InputBounds);
        setInputNativeBounds(0, src0Bounds);
        HeapImage src1 = (HeapImage) inputs[1].getTransformedImage(dstBounds);
        int src1w = src1.getPhysicalWidth();
        int src1h = src1.getPhysicalHeight();
        int src1scan = src1.getScanlineStride();
        int[] origImg = src1.getPixelArray();
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
        float[] lightPosition_arr = getLightPosition();
        float lightPosition_x = lightPosition_arr[0];
        float lightPosition_y = lightPosition_arr[1];
        float lightPosition_z = lightPosition_arr[2];
        float specularExponent = getSpecularExponent();
        FloatBuffer kvals_buf = getKvals();
        float[] kvals_arr = new float[kvals_buf.capacity()];
        kvals_buf.get(kvals_arr);
        float diffuseConstant = getDiffuseConstant();
        float[] lightColor_arr = getLightColor();
        float lightColor_x = lightColor_arr[0];
        float lightColor_y = lightColor_arr[1];
        float lightColor_z = lightColor_arr[2];
        float specularConstant = getSpecularConstant();
        float surfaceScale = getSurfaceScale();
        float inc0_x = (src0Rect[2] - src0Rect[0]) / dstw;
        float inc0_y = (src0Rect[3] - src0Rect[1]) / dsth;
        float inc1_x = (src1Rect[2] - src1Rect[0]) / dstw;
        float inc1_y = (src1Rect[3] - src1Rect[1]) / dsth;
        float pos0_y = src0Rect[1] + (inc0_y * 0.5f);
        float pos1_y = src1Rect[1] + (inc1_y * 0.5f);
        for (int dy = 0; dy < 0 + dsth; dy++) {
            float pixcoord_y = dy;
            int dyi = dy * dstscan;
            float pos0_x = src0Rect[0] + (inc0_x * 0.5f);
            float pos1_x = src1Rect[0] + (inc1_x * 0.5f);
            for (int dx = 0; dx < 0 + dstw; dx++) {
                float pixcoord_x = dx;
                float loc_tmp_x = pos1_x;
                float loc_tmp_y = pos1_y;
                if (loc_tmp_x >= 0.0f && loc_tmp_y >= 0.0f) {
                    int iloc_tmp_x = (int) (loc_tmp_x * src1w);
                    int iloc_tmp_y = (int) (loc_tmp_y * src1h);
                    boolean out = iloc_tmp_x >= src1w || iloc_tmp_y >= src1h;
                    origImg_tmp = out ? 0 : origImg[(iloc_tmp_y * src1scan) + iloc_tmp_x];
                } else {
                    origImg_tmp = 0;
                }
                float sample_res_x = ((origImg_tmp >> 16) & 255) / 255.0f;
                float sample_res_y = ((origImg_tmp >> 8) & 255) / 255.0f;
                float sample_res_z = (origImg_tmp & 255) / 255.0f;
                float sample_res_w = (origImg_tmp >>> 24) / 255.0f;
                float sum_x = 0.0f;
                float sum_y = 0.0f;
                for (int i3 = 0; i3 < 8; i3++) {
                    float loc_tmp_x2 = pos0_x + kvals_arr[(i3 * 4) + 0];
                    float loc_tmp_y2 = pos0_y + kvals_arr[(i3 * 4) + 1];
                    if (loc_tmp_x2 >= 0.0f && loc_tmp_y2 >= 0.0f) {
                        int iloc_tmp_x2 = (int) (loc_tmp_x2 * src0w);
                        int iloc_tmp_y2 = (int) (loc_tmp_y2 * src0h);
                        boolean out2 = iloc_tmp_x2 >= src0w || iloc_tmp_y2 >= src0h;
                        i2 = out2 ? 0 : bumpImg[(iloc_tmp_y2 * src0scan) + iloc_tmp_x2];
                    } else {
                        i2 = 0;
                    }
                    int bumpImg_tmp2 = i2;
                    float sample_res_w2 = (bumpImg_tmp2 >>> 24) / 255.0f;
                    sum_x += kvals_arr[(i3 * 4) + 2] * sample_res_w2;
                    sum_y += kvals_arr[(i3 * 4) + 3] * sample_res_w2;
                }
                float x_tmp_x = sum_x;
                float x_tmp_y = sum_y;
                float denom = (float) Math.sqrt((x_tmp_x * x_tmp_x) + (x_tmp_y * x_tmp_y) + (1.0f * 1.0f));
                float normalize_res_x = x_tmp_x / denom;
                float normalize_res_y = x_tmp_y / denom;
                float normalize_res_z = 1.0f / denom;
                float loc_tmp_x3 = pos0_x;
                float loc_tmp_y3 = pos0_y;
                if (loc_tmp_x3 >= 0.0f && loc_tmp_y3 >= 0.0f) {
                    int iloc_tmp_x3 = (int) (loc_tmp_x3 * src0w);
                    int iloc_tmp_y3 = (int) (loc_tmp_y3 * src0h);
                    boolean out3 = iloc_tmp_x3 >= src0w || iloc_tmp_y3 >= src0h;
                    bumpImg_tmp = out3 ? 0 : bumpImg[(iloc_tmp_y3 * src0scan) + iloc_tmp_x3];
                } else {
                    bumpImg_tmp = 0;
                }
                float sample_res_w3 = (bumpImg_tmp >>> 24) / 255.0f;
                float tmp_z = surfaceScale * sample_res_w3;
                float x_tmp_x2 = lightPosition_x - pixcoord_x;
                float x_tmp_y2 = lightPosition_y - pixcoord_y;
                float x_tmp_z = lightPosition_z - tmp_z;
                float denom2 = (float) Math.sqrt((x_tmp_x2 * x_tmp_x2) + (x_tmp_y2 * x_tmp_y2) + (x_tmp_z * x_tmp_z));
                float normalize_res_x2 = x_tmp_x2 / denom2;
                float normalize_res_y2 = x_tmp_y2 / denom2;
                float normalize_res_z2 = x_tmp_z / denom2;
                float x_tmp_x3 = normalize_res_x2 + 0.0f;
                float x_tmp_y3 = normalize_res_y2 + 0.0f;
                float x_tmp_z2 = normalize_res_z2 + 1.0f;
                float denom3 = (float) Math.sqrt((x_tmp_x3 * x_tmp_x3) + (x_tmp_y3 * x_tmp_y3) + (x_tmp_z2 * x_tmp_z2));
                float normalize_res_x3 = x_tmp_x3 / denom3;
                float normalize_res_y3 = x_tmp_y3 / denom3;
                float normalize_res_z3 = x_tmp_z2 / denom3;
                float dot_res = (normalize_res_x * normalize_res_x2) + (normalize_res_y * normalize_res_y2) + (normalize_res_z * normalize_res_z2);
                float D_x = diffuseConstant * dot_res * lightColor_x;
                float D_y = diffuseConstant * dot_res * lightColor_y;
                float D_z = diffuseConstant * dot_res * lightColor_z;
                if (D_x < 0.0f) {
                    f2 = 0.0f;
                } else if (D_x <= 1.0f) {
                    f2 = D_x;
                } else {
                    f2 = 1.0f;
                }
                float clamp_res_x = f2;
                if (D_y < 0.0f) {
                    f3 = 0.0f;
                } else if (D_y <= 1.0f) {
                    f3 = D_y;
                } else {
                    f3 = 1.0f;
                }
                float clamp_res_y = f3;
                if (D_z < 0.0f) {
                    f4 = 0.0f;
                } else if (D_z <= 1.0f) {
                    f4 = D_z;
                } else {
                    f4 = 1.0f;
                }
                float clamp_res_z = f4;
                float pow_res = (float) Math.pow((normalize_res_x * normalize_res_x3) + (normalize_res_y * normalize_res_y3) + (normalize_res_z * normalize_res_z3), specularExponent);
                float S_x = specularConstant * pow_res * lightColor_x;
                float S_y = specularConstant * pow_res * lightColor_y;
                float S_z = specularConstant * pow_res * lightColor_z;
                if (S_x > S_y) {
                    f5 = S_x;
                } else {
                    f5 = S_y;
                }
                float max_res = f5;
                if (max_res > S_z) {
                    f6 = max_res;
                } else {
                    f6 = S_z;
                }
                float max_res2 = f6;
                float orig_x = sample_res_x * clamp_res_x;
                float orig_y = sample_res_y * clamp_res_y;
                float orig_z = sample_res_z * clamp_res_z;
                float orig_w = sample_res_w * 1.0f;
                float S_x2 = S_x * orig_w;
                float S_y2 = S_y * orig_w;
                float S_z2 = S_z * orig_w;
                float S_w = max_res2 * orig_w;
                float color_x = S_x2 + (orig_x * (1.0f - S_w));
                float color_y = S_y2 + (orig_y * (1.0f - S_w));
                float color_z = S_z2 + (orig_z * (1.0f - S_w));
                float color_w = S_w + (orig_w * (1.0f - S_w));
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
