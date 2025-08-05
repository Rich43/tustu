package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.ColorAdjust;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEColorAdjustPeer.class */
public class SSEColorAdjustPeer extends SSEEffectPeer {
    private static native void filter(int[] iArr, int i2, int i3, int i4, int i5, int i6, int[] iArr2, float f2, float f3, float f4, float f5, int i7, int i8, int i9, float f6, float f7, float f8, float f9);

    public SSEColorAdjustPeer(FilterContext fctx, Renderer r2, String uniqueName) {
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
        float brightness = getBrightness();
        float contrast = getContrast();
        float hue = getHue();
        float saturation = getSaturation();
        filter(dstPixels, 0, 0, dstw, dsth, dstscan, baseImg, src0Rect[0], src0Rect[1], src0Rect[2], src0Rect[3], src0w, src0h, src0scan, brightness, contrast, hue, saturation);
        inputs[0].releaseTransformedImage(src0);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
