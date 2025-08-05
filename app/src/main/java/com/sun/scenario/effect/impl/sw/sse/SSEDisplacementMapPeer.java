package com.sun.scenario.effect.impl.sw.sse;

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

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEDisplacementMapPeer.class */
public class SSEDisplacementMapPeer extends SSEEffectPeer {
    private static native void filter(int[] iArr, int i2, int i3, int i4, int i5, int i6, float f2, float f3, float f4, float f5, float[] fArr, float f6, float f7, float f8, float f9, int i7, int i8, int i9, int[] iArr2, float f10, float f11, float f12, float f13, int i10, int i11, int i12, float f14, float f15, float f16, float f17, float f18);

    public SSEDisplacementMapPeer(FilterContext fctx, Renderer r2, String uniqueName) {
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
        FloatMap src1 = (FloatMap) getSamplerData(1);
        int src1w = src1.getWidth();
        int src1h = src1.getHeight();
        int src1scan = src1.getWidth();
        float[] mapImg = src1.getData();
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
        float[] src1Rect = {0.0f, 0.0f, 1.0f, 1.0f};
        float[] src0Rect = new float[4];
        getTextureCoordinates(0, src0Rect, src0InputBounds.f11913x, src0InputBounds.f11914y, src0w, src0h, dstBounds, src0Transform);
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        float[] imagetx_arr = getImagetx();
        float[] sampletx_arr = getSampletx();
        float wrap = getWrap();
        filter(dstPixels, 0, 0, dstw, dsth, dstscan, imagetx_arr[0], imagetx_arr[1], imagetx_arr[2], imagetx_arr[3], mapImg, src1Rect[0], src1Rect[1], src1Rect[2], src1Rect[3], src1w, src1h, src1scan, origImg, src0Rect[0], src0Rect[1], src0Rect[2], src0Rect[3], src0w, src0h, src0scan, sampletx_arr[0], sampletx_arr[1], sampletx_arr[2], sampletx_arr[3], wrap);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
