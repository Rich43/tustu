package sun.java2d.d3d;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import sun.java2d.ScreenUpdateManager;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;

/* loaded from: rt.jar:sun/java2d/d3d/D3DBlitLoops.class */
final class D3DBlitLoops {
    private static final int OFFSET_SRCTYPE = 16;
    private static final int OFFSET_HINT = 8;
    private static final int OFFSET_TEXTURE = 3;
    private static final int OFFSET_RTT = 2;
    private static final int OFFSET_XFORM = 1;
    private static final int OFFSET_ISOBLIT = 0;

    D3DBlitLoops() {
    }

    static void register() {
        D3DSwToSurfaceBlit d3DSwToSurfaceBlit = new D3DSwToSurfaceBlit(SurfaceType.IntArgbPre, 1);
        D3DSwToTextureBlit d3DSwToTextureBlit = new D3DSwToTextureBlit(SurfaceType.IntArgbPre, 1);
        D3DSwToSurfaceTransform d3DSwToSurfaceTransform = new D3DSwToSurfaceTransform(SurfaceType.IntArgbPre, 1);
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new D3DSurfaceToGDIWindowSurfaceBlit(), new D3DSurfaceToGDIWindowSurfaceScale(), new D3DSurfaceToGDIWindowSurfaceTransform(), new D3DSurfaceToSurfaceBlit(), new D3DSurfaceToSurfaceScale(), new D3DSurfaceToSurfaceTransform(), new D3DRTTSurfaceToSurfaceBlit(), new D3DRTTSurfaceToSurfaceScale(), new D3DRTTSurfaceToSurfaceTransform(), new D3DSurfaceToSwBlit(SurfaceType.IntArgb, 0), d3DSwToSurfaceBlit, new D3DSwToSurfaceBlit(SurfaceType.IntArgb, 0), new D3DSwToSurfaceBlit(SurfaceType.IntRgb, 3), new D3DSwToSurfaceBlit(SurfaceType.IntBgr, 4), new D3DSwToSurfaceBlit(SurfaceType.ThreeByteBgr, 9), new D3DSwToSurfaceBlit(SurfaceType.Ushort565Rgb, 5), new D3DSwToSurfaceBlit(SurfaceType.Ushort555Rgb, 6), new D3DSwToSurfaceBlit(SurfaceType.ByteIndexed, 7), new D3DGeneralBlit(D3DSurfaceData.D3DSurface, CompositeType.AnyAlpha, d3DSwToSurfaceBlit), new D3DSwToSurfaceScale(SurfaceType.IntArgb, 0), new D3DSwToSurfaceScale(SurfaceType.IntArgbPre, 1), new D3DSwToSurfaceScale(SurfaceType.IntRgb, 3), new D3DSwToSurfaceScale(SurfaceType.IntBgr, 4), new D3DSwToSurfaceScale(SurfaceType.ThreeByteBgr, 9), new D3DSwToSurfaceScale(SurfaceType.Ushort565Rgb, 5), new D3DSwToSurfaceScale(SurfaceType.Ushort555Rgb, 6), new D3DSwToSurfaceScale(SurfaceType.ByteIndexed, 7), new D3DSwToSurfaceTransform(SurfaceType.IntArgb, 0), new D3DSwToSurfaceTransform(SurfaceType.IntRgb, 3), new D3DSwToSurfaceTransform(SurfaceType.IntBgr, 4), new D3DSwToSurfaceTransform(SurfaceType.ThreeByteBgr, 9), new D3DSwToSurfaceTransform(SurfaceType.Ushort565Rgb, 5), new D3DSwToSurfaceTransform(SurfaceType.Ushort555Rgb, 6), new D3DSwToSurfaceTransform(SurfaceType.ByteIndexed, 7), d3DSwToSurfaceTransform, new D3DGeneralTransformedBlit(d3DSwToSurfaceTransform), new D3DTextureToSurfaceBlit(), new D3DTextureToSurfaceScale(), new D3DTextureToSurfaceTransform(), d3DSwToTextureBlit, new D3DSwToTextureBlit(SurfaceType.IntRgb, 3), new D3DSwToTextureBlit(SurfaceType.IntArgb, 0), new D3DSwToTextureBlit(SurfaceType.IntBgr, 4), new D3DSwToTextureBlit(SurfaceType.ThreeByteBgr, 9), new D3DSwToTextureBlit(SurfaceType.Ushort565Rgb, 5), new D3DSwToTextureBlit(SurfaceType.Ushort555Rgb, 6), new D3DSwToTextureBlit(SurfaceType.ByteIndexed, 7), new D3DGeneralBlit(D3DSurfaceData.D3DTexture, CompositeType.SrcNoEa, d3DSwToTextureBlit)});
    }

    private static int createPackedParams(boolean z2, boolean z3, boolean z4, boolean z5, int i2, int i3) {
        return (i3 << 16) | (i2 << 8) | ((z3 ? 1 : 0) << 3) | ((z4 ? 1 : 0) << 2) | ((z5 ? 1 : 0) << 1) | ((z2 ? 1 : 0) << 0);
    }

    private static void enqueueBlit(RenderQueue renderQueue, SurfaceData surfaceData, SurfaceData surfaceData2, int i2, int i3, int i4, int i5, int i6, double d2, double d3, double d4, double d5) {
        RenderBuffer buffer = renderQueue.getBuffer();
        renderQueue.ensureCapacityAndAlignment(72, 24);
        buffer.putInt(31);
        buffer.putInt(i2);
        buffer.putInt(i3).putInt(i4);
        buffer.putInt(i5).putInt(i6);
        buffer.putDouble(d2).putDouble(d3);
        buffer.putDouble(d4).putDouble(d5);
        buffer.putLong(surfaceData.getNativeOps());
        buffer.putLong(surfaceData2.getNativeOps());
    }

    static void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, double d2, double d3, double d4, double d5, int i7, boolean z2) {
        int i8 = 0;
        if (surfaceData.getTransparency() == 1) {
            i8 = 0 | 1;
        }
        D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) surfaceData2;
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.addReference(surfaceData);
            if (z2) {
                D3DContext.setScratchSurface(d3DSurfaceData.getContext());
            } else {
                D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, region, composite, affineTransform, null, null, i8);
            }
            enqueueBlit(d3DRenderQueue, surfaceData, surfaceData2, createPackedParams(false, z2, false, affineTransform != null, i2, i7), i3, i4, i5, i6, d2, d3, d4, d5);
            d3DRenderQueue.flushNow();
            d3DRenderQueue.unlock();
            if (d3DSurfaceData.getType() == 1) {
                ((D3DScreenUpdateManager) ScreenUpdateManager.getInstance()).runUpdateNow();
            }
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    static void IsoBlit(SurfaceData surfaceData, SurfaceData surfaceData2, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, double d2, double d3, double d4, double d5, boolean z2) {
        boolean z3;
        int i7 = 0;
        if (surfaceData.getTransparency() == 1) {
            i7 = 0 | 1;
        }
        D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) surfaceData2;
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            D3DSurfaceData d3DSurfaceData2 = (D3DSurfaceData) surfaceData;
            if (d3DSurfaceData2.getType() == 3) {
                z3 = false;
            } else {
                z3 = true;
            }
            D3DContext.validateContext(d3DSurfaceData2, d3DSurfaceData, region, composite, affineTransform, null, null, i7);
            if (bufferedImageOp != null) {
                D3DBufImgOps.enableBufImgOp(d3DRenderQueue, d3DSurfaceData2, bufferedImage, bufferedImageOp);
            }
            enqueueBlit(d3DRenderQueue, surfaceData, surfaceData2, createPackedParams(true, z2, z3, affineTransform != null, i2, 0), i3, i4, i5, i6, d2, d3, d4, d5);
            if (bufferedImageOp != null) {
                D3DBufImgOps.disableBufImgOp(d3DRenderQueue, bufferedImageOp);
            }
            if (z3 && d3DSurfaceData.getType() == 1) {
                ((D3DScreenUpdateManager) ScreenUpdateManager.getInstance()).runUpdateNow();
            }
        } finally {
            d3DRenderQueue.unlock();
        }
    }
}
