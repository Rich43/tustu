package com.sun.scenario.effect.impl.sw.sse;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSELinearConvolveShadowPeer.class */
public class SSELinearConvolveShadowPeer extends SSELinearConvolvePeer {
    private static native void filterVector(int[] iArr, int i2, int i3, int i4, int[] iArr2, int i5, int i6, int i7, float[] fArr, int i8, float f2, float f3, float f4, float f5, float f6, float f7, float[] fArr2, float f8, float f9, float f10, float f11);

    private static native void filterHV(int[] iArr, int i2, int i3, int i4, int i5, int[] iArr2, int i6, int i7, int i8, int i9, float[] fArr, float[] fArr2);

    public SSELinearConvolveShadowPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private float[] getShadowColor() {
        return ((LinearConvolveRenderState) getRenderState()).getPassShadowColorComponents();
    }

    @Override // com.sun.scenario.effect.impl.sw.sse.SSELinearConvolvePeer
    void filterVector(int[] dstPixels, int dstw, int dsth, int dstscan, int[] srcPixels, int srcw, int srch, int srcscan, float[] weights, int count, float srcx0, float srcy0, float offsetx, float offsety, float deltax, float deltay, float dxcol, float dycol, float dxrow, float dyrow) {
        filterVector(dstPixels, dstw, dsth, dstscan, srcPixels, srcw, srch, srcscan, weights, count, srcx0, srcy0, offsetx, offsety, deltax, deltay, getShadowColor(), dxcol, dycol, dxrow, dyrow);
    }

    @Override // com.sun.scenario.effect.impl.sw.sse.SSELinearConvolvePeer
    void filterHV(int[] dstPixels, int dstcols, int dstrows, int dcolinc, int drowinc, int[] srcPixels, int srccols, int srcrows, int scolinc, int srowinc, float[] weights) {
        filterHV(dstPixels, dstcols, dstrows, dcolinc, drowinc, srcPixels, srccols, srcrows, scolinc, srowinc, weights, getShadowColor());
    }
}
