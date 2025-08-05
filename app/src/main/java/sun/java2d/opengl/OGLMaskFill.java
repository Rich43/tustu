package sun.java2d.opengl;

import java.awt.Composite;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.BufferedMaskFill;

/* loaded from: rt.jar:sun/java2d/opengl/OGLMaskFill.class */
class OGLMaskFill extends BufferedMaskFill {
    @Override // sun.java2d.pipe.BufferedMaskFill
    protected native void maskFill(int i2, int i3, int i4, int i5, int i6, int i7, int i8, byte[] bArr);

    static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new OGLMaskFill(SurfaceType.AnyColor, CompositeType.SrcOver), new OGLMaskFill(SurfaceType.OpaqueColor, CompositeType.SrcNoEa), new OGLMaskFill(SurfaceType.GradientPaint, CompositeType.SrcOver), new OGLMaskFill(SurfaceType.OpaqueGradientPaint, CompositeType.SrcNoEa), new OGLMaskFill(SurfaceType.LinearGradientPaint, CompositeType.SrcOver), new OGLMaskFill(SurfaceType.OpaqueLinearGradientPaint, CompositeType.SrcNoEa), new OGLMaskFill(SurfaceType.RadialGradientPaint, CompositeType.SrcOver), new OGLMaskFill(SurfaceType.OpaqueRadialGradientPaint, CompositeType.SrcNoEa), new OGLMaskFill(SurfaceType.TexturePaint, CompositeType.SrcOver), new OGLMaskFill(SurfaceType.OpaqueTexturePaint, CompositeType.SrcNoEa)});
    }

    protected OGLMaskFill(SurfaceType surfaceType, CompositeType compositeType) {
        super(OGLRenderQueue.getInstance(), surfaceType, compositeType, OGLSurfaceData.OpenGLSurface);
    }

    @Override // sun.java2d.pipe.BufferedMaskFill
    protected void validateContext(SunGraphics2D sunGraphics2D, Composite composite, int i2) {
        try {
            OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sunGraphics2D.surfaceData;
            OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, sunGraphics2D.getCompClip(), composite, null, sunGraphics2D.paint, sunGraphics2D, i2);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }
}
