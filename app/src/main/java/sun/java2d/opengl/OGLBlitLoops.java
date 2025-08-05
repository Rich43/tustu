package sun.java2d.opengl;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.RenderQueue;

/* loaded from: rt.jar:sun/java2d/opengl/OGLBlitLoops.class */
final class OGLBlitLoops {
    private static final int OFFSET_SRCTYPE = 16;
    private static final int OFFSET_HINT = 8;
    private static final int OFFSET_TEXTURE = 3;
    private static final int OFFSET_RTT = 2;
    private static final int OFFSET_XFORM = 1;
    private static final int OFFSET_ISOBLIT = 0;

    OGLBlitLoops() {
    }

    static void register() {
        OGLSwToSurfaceBlit oGLSwToSurfaceBlit = new OGLSwToSurfaceBlit(SurfaceType.IntArgbPre, 1);
        OGLSwToTextureBlit oGLSwToTextureBlit = new OGLSwToTextureBlit(SurfaceType.IntArgbPre, 1);
        OGLSwToSurfaceTransform oGLSwToSurfaceTransform = new OGLSwToSurfaceTransform(SurfaceType.IntArgbPre, 1);
        OGLSurfaceToSwBlit oGLSurfaceToSwBlit = new OGLSurfaceToSwBlit(SurfaceType.IntArgbPre, 1);
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new OGLSurfaceToSurfaceBlit(), new OGLSurfaceToSurfaceScale(), new OGLSurfaceToSurfaceTransform(), new OGLRTTSurfaceToSurfaceBlit(), new OGLRTTSurfaceToSurfaceScale(), new OGLRTTSurfaceToSurfaceTransform(), new OGLSurfaceToSwBlit(SurfaceType.IntArgb, 0), oGLSurfaceToSwBlit, oGLSwToSurfaceBlit, new OGLSwToSurfaceBlit(SurfaceType.IntRgb, 2), new OGLSwToSurfaceBlit(SurfaceType.IntRgbx, 3), new OGLSwToSurfaceBlit(SurfaceType.IntBgr, 4), new OGLSwToSurfaceBlit(SurfaceType.IntBgrx, 5), new OGLSwToSurfaceBlit(SurfaceType.ThreeByteBgr, 11), new OGLSwToSurfaceBlit(SurfaceType.Ushort565Rgb, 6), new OGLSwToSurfaceBlit(SurfaceType.Ushort555Rgb, 7), new OGLSwToSurfaceBlit(SurfaceType.Ushort555Rgbx, 8), new OGLSwToSurfaceBlit(SurfaceType.ByteGray, 9), new OGLSwToSurfaceBlit(SurfaceType.UshortGray, 10), new OGLGeneralBlit(OGLSurfaceData.OpenGLSurface, CompositeType.AnyAlpha, oGLSwToSurfaceBlit), new OGLAnyCompositeBlit(OGLSurfaceData.OpenGLSurface, oGLSurfaceToSwBlit, oGLSurfaceToSwBlit, oGLSwToSurfaceBlit), new OGLAnyCompositeBlit(SurfaceType.Any, null, oGLSurfaceToSwBlit, oGLSwToSurfaceBlit), new OGLSwToSurfaceScale(SurfaceType.IntRgb, 2), new OGLSwToSurfaceScale(SurfaceType.IntRgbx, 3), new OGLSwToSurfaceScale(SurfaceType.IntBgr, 4), new OGLSwToSurfaceScale(SurfaceType.IntBgrx, 5), new OGLSwToSurfaceScale(SurfaceType.ThreeByteBgr, 11), new OGLSwToSurfaceScale(SurfaceType.Ushort565Rgb, 6), new OGLSwToSurfaceScale(SurfaceType.Ushort555Rgb, 7), new OGLSwToSurfaceScale(SurfaceType.Ushort555Rgbx, 8), new OGLSwToSurfaceScale(SurfaceType.ByteGray, 9), new OGLSwToSurfaceScale(SurfaceType.UshortGray, 10), new OGLSwToSurfaceScale(SurfaceType.IntArgbPre, 1), new OGLSwToSurfaceTransform(SurfaceType.IntRgb, 2), new OGLSwToSurfaceTransform(SurfaceType.IntRgbx, 3), new OGLSwToSurfaceTransform(SurfaceType.IntBgr, 4), new OGLSwToSurfaceTransform(SurfaceType.IntBgrx, 5), new OGLSwToSurfaceTransform(SurfaceType.ThreeByteBgr, 11), new OGLSwToSurfaceTransform(SurfaceType.Ushort565Rgb, 6), new OGLSwToSurfaceTransform(SurfaceType.Ushort555Rgb, 7), new OGLSwToSurfaceTransform(SurfaceType.Ushort555Rgbx, 8), new OGLSwToSurfaceTransform(SurfaceType.ByteGray, 9), new OGLSwToSurfaceTransform(SurfaceType.UshortGray, 10), oGLSwToSurfaceTransform, new OGLGeneralTransformedBlit(oGLSwToSurfaceTransform), new OGLTextureToSurfaceBlit(), new OGLTextureToSurfaceScale(), new OGLTextureToSurfaceTransform(), oGLSwToTextureBlit, new OGLSwToTextureBlit(SurfaceType.IntRgb, 2), new OGLSwToTextureBlit(SurfaceType.IntRgbx, 3), new OGLSwToTextureBlit(SurfaceType.IntBgr, 4), new OGLSwToTextureBlit(SurfaceType.IntBgrx, 5), new OGLSwToTextureBlit(SurfaceType.ThreeByteBgr, 11), new OGLSwToTextureBlit(SurfaceType.Ushort565Rgb, 6), new OGLSwToTextureBlit(SurfaceType.Ushort555Rgb, 7), new OGLSwToTextureBlit(SurfaceType.Ushort555Rgbx, 8), new OGLSwToTextureBlit(SurfaceType.ByteGray, 9), new OGLSwToTextureBlit(SurfaceType.UshortGray, 10), new OGLGeneralBlit(OGLSurfaceData.OpenGLTexture, CompositeType.SrcNoEa, oGLSwToTextureBlit)});
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
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            oGLRenderQueue.addReference(surfaceData);
            OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) surfaceData2;
            if (z2) {
                OGLContext.setScratchSurface(oGLSurfaceData.getOGLGraphicsConfig());
            } else {
                OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, region, composite, affineTransform, null, null, i8);
            }
            enqueueBlit(oGLRenderQueue, surfaceData, surfaceData2, createPackedParams(false, z2, false, affineTransform != null, i2, i7), i3, i4, i5, i6, d2, d3, d4, d5);
            oGLRenderQueue.flushNow();
            oGLRenderQueue.unlock();
        } catch (Throwable th) {
            oGLRenderQueue.unlock();
            throw th;
        }
    }

    static void IsoBlit(SurfaceData surfaceData, SurfaceData surfaceData2, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, double d2, double d3, double d4, double d5, boolean z2) {
        boolean z3;
        OGLSurfaceData oGLSurfaceData;
        int i7 = 0;
        if (surfaceData.getTransparency() == 1) {
            i7 = 0 | 1;
        }
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            OGLSurfaceData oGLSurfaceData2 = (OGLSurfaceData) surfaceData;
            OGLSurfaceData oGLSurfaceData3 = (OGLSurfaceData) surfaceData2;
            int type = oGLSurfaceData2.getType();
            if (type == 3) {
                z3 = false;
                oGLSurfaceData = oGLSurfaceData3;
            } else {
                z3 = true;
                if (type == 5) {
                    oGLSurfaceData = oGLSurfaceData3;
                } else {
                    oGLSurfaceData = oGLSurfaceData2;
                }
            }
            OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData3, region, composite, affineTransform, null, null, i7);
            if (bufferedImageOp != null) {
                OGLBufImgOps.enableBufImgOp(oGLRenderQueue, oGLSurfaceData2, bufferedImage, bufferedImageOp);
            }
            enqueueBlit(oGLRenderQueue, surfaceData, surfaceData2, createPackedParams(true, z2, z3, affineTransform != null, i2, 0), i3, i4, i5, i6, d2, d3, d4, d5);
            if (bufferedImageOp != null) {
                OGLBufImgOps.disableBufImgOp(oGLRenderQueue, bufferedImageOp);
            }
            if (z3 && oGLSurfaceData3.isOnScreen()) {
                oGLRenderQueue.flushNow();
            }
        } finally {
            oGLRenderQueue.unlock();
        }
    }
}
