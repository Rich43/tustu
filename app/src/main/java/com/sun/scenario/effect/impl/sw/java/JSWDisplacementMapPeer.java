package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.DisplacementMap;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWDisplacementMapPeer.class */
public class JSWDisplacementMapPeer extends JSWEffectPeer {
    public JSWDisplacementMapPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.scenario.effect.impl.EffectPeer
    public final DisplacementMap getEffect() {
        return (DisplacementMap) super.getEffect();
    }

    private float[] getSampletx() {
        return new float[]{getEffect().getOffsetX(), getEffect().getOffsetY(), getEffect().getScaleX(), getEffect().getScaleY()};
    }

    private float[] getImagetx() {
        float inset = getEffect().getWrap() ? 0.5f : 0.0f;
        return new float[]{inset / getInputNativeBounds(0).width, inset / getInputNativeBounds(0).height, (getInputBounds(0).width - (2.0f * inset)) / getInputNativeBounds(0).width, (getInputBounds(0).height - (2.0f * inset)) / getInputNativeBounds(0).height};
    }

    private float getWrap() {
        return getEffect().getWrap() ? 1.0f : 0.0f;
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    protected Object getSamplerData(int i2) {
        return getEffect().getMapData();
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public int getTextureCoordinates(int inputIndex, float[] coords, float srcX, float srcY, float srcNativeWidth, float srcNativeHeight, Rectangle dstBounds, BaseTransform transform) {
        coords[1] = 0.0f;
        coords[0] = 0.0f;
        coords[3] = 1.0f;
        coords[2] = 1.0f;
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
        int[] origImg = src0.getPixelArray();
        Rectangle src0Bounds = new Rectangle(0, 0, src0w, src0h);
        Rectangle src0InputBounds = inputs[0].getUntransformedBounds();
        BaseTransform src0Transform = inputs[0].getTransform();
        setInputBounds(0, src0InputBounds);
        setInputNativeBounds(0, src0Bounds);
        float[] origImg_vals = new float[4];
        FloatMap src1 = (FloatMap) getSamplerData(1);
        int src1w = src1.getWidth();
        int src1h = src1.getHeight();
        int src1scan = src1.getWidth();
        float[] mapImg = src1.getData();
        float[] mapImg_vals = new float[4];
        float[] src0Rect = new float[4];
        getTextureCoordinates(0, src0Rect, src0InputBounds.f11913x, src0InputBounds.f11914y, src0w, src0h, dstBounds, src0Transform);
        float[] src1Rect = {0.0f, 0.0f, 1.0f, 1.0f};
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        float[] imagetx_arr = getImagetx();
        float imagetx_x = imagetx_arr[0];
        float imagetx_y = imagetx_arr[1];
        float imagetx_z = imagetx_arr[2];
        float imagetx_w = imagetx_arr[3];
        float wrap = getWrap();
        float[] sampletx_arr = getSampletx();
        float sampletx_x = sampletx_arr[0];
        float sampletx_y = sampletx_arr[1];
        float sampletx_z = sampletx_arr[2];
        float sampletx_w = sampletx_arr[3];
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
                float loc_tmp_x = pos1_x;
                float loc_tmp_y = pos1_y;
                fsample(mapImg, loc_tmp_x, loc_tmp_y, src1w, src1h, src1scan, mapImg_vals);
                float sample_res_x = mapImg_vals[0];
                float sample_res_y = mapImg_vals[1];
                float f4 = mapImg_vals[2];
                float f5 = mapImg_vals[3];
                float loc_x = pos0_x + (sampletx_z * (sample_res_x + sampletx_x));
                float loc_y = pos0_y + (sampletx_w * (sample_res_y + sampletx_y));
                float floor_res_x = (float) Math.floor(loc_x);
                float floor_res_y = (float) Math.floor(loc_y);
                lsample(origImg, imagetx_x + ((loc_x - (wrap * floor_res_x)) * imagetx_z), imagetx_y + ((loc_y - (wrap * floor_res_y)) * imagetx_w), src0w, src0h, src0scan, origImg_vals);
                float sample_res_x2 = origImg_vals[0];
                float sample_res_y2 = origImg_vals[1];
                float sample_res_z = origImg_vals[2];
                float sample_res_w = origImg_vals[3];
                float color_x = sample_res_x2;
                float color_y = sample_res_y2;
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
                pos1_x += inc1_x;
            }
            pos0_y += inc0_y;
            pos1_y += inc1_y;
        }
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
