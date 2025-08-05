package com.sun.scenario.effect.impl.sw.sse;

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

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEPhongLighting_SPOTPeer.class */
public class SSEPhongLighting_SPOTPeer extends SSEEffectPeer {
    private FloatBuffer kvals;

    private static native void filter(int[] iArr, int i2, int i3, int i4, int i5, int i6, int[] iArr2, float f2, float f3, float f4, float f5, int i7, int i8, int i9, float f6, float[] fArr, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, int[] iArr3, float f17, float f18, float f19, float f20, int i10, int i11, int i12, float f21, float f22, float f23);

    public SSEPhongLighting_SPOTPeer(FilterContext fctx, Renderer r2, String uniqueName) {
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
        float diffuseConstant = getDiffuseConstant();
        FloatBuffer kvals_buf = getKvals();
        float[] kvals_arr = new float[kvals_buf.capacity()];
        kvals_buf.get(kvals_arr);
        float[] lightColor_arr = getLightColor();
        float[] lightPosition_arr = getLightPosition();
        float lightSpecularExponent = getLightSpecularExponent();
        float[] normalizedLightDirection_arr = getNormalizedLightDirection();
        float specularConstant = getSpecularConstant();
        float specularExponent = getSpecularExponent();
        float surfaceScale = getSurfaceScale();
        filter(dstPixels, 0, 0, dstw, dsth, dstscan, bumpImg, src0Rect[0], src0Rect[1], src0Rect[2], src0Rect[3], src0w, src0h, src0scan, diffuseConstant, kvals_arr, lightColor_arr[0], lightColor_arr[1], lightColor_arr[2], lightPosition_arr[0], lightPosition_arr[1], lightPosition_arr[2], lightSpecularExponent, normalizedLightDirection_arr[0], normalizedLightDirection_arr[1], normalizedLightDirection_arr[2], origImg, src1Rect[0], src1Rect[1], src1Rect[2], src1Rect[3], src1w, src1h, src1scan, specularConstant, specularExponent, surfaceScale);
        inputs[0].releaseTransformedImage(src0);
        inputs[1].releaseTransformedImage(src1);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
