package com.sun.scenario.effect.impl.sw.sse;

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

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEPerspectiveTransformPeer.class */
public class SSEPerspectiveTransformPeer extends SSEEffectPeer {
    private static native void filter(int[] iArr, int i2, int i3, int i4, int i5, int i6, int[] iArr2, float f2, float f3, float f4, float f5, int i7, int i8, int i9, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14);

    public SSEPerspectiveTransformPeer(FilterContext fctx, Renderer r2, String uniqueName) {
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
        float[] src0Rect = new float[4];
        getTextureCoordinates(0, src0Rect, src0InputBounds.f11913x, src0InputBounds.f11914y, src0w, src0h, dstBounds, src0Transform);
        int dstw = dstBounds.width;
        int dsth = dstBounds.height;
        HeapImage dst = (HeapImage) getRenderer().getCompatibleImage(dstw, dsth);
        setDestNativeBounds(dst.getPhysicalWidth(), dst.getPhysicalHeight());
        int dstscan = dst.getScanlineStride();
        int[] dstPixels = dst.getPixelArray();
        float[] tx0_arr = getTx0();
        float[] tx1_arr = getTx1();
        float[] tx2_arr = getTx2();
        filter(dstPixels, 0, 0, dstw, dsth, dstscan, baseImg, src0Rect[0], src0Rect[1], src0Rect[2], src0Rect[3], src0w, src0h, src0scan, tx0_arr[0], tx0_arr[1], tx0_arr[2], tx1_arr[0], tx1_arr[1], tx1_arr[2], tx2_arr[0], tx2_arr[1], tx2_arr[2]);
        return new ImageData(getFilterContext(), dst, dstBounds);
    }
}
