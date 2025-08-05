package sun.java2d.opengl;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.BufferedMaskBlit;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/opengl/OGLMaskBlit.class */
class OGLMaskBlit extends BufferedMaskBlit {
    static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new OGLMaskBlit(SurfaceType.IntArgb, CompositeType.SrcOver), new OGLMaskBlit(SurfaceType.IntArgbPre, CompositeType.SrcOver), new OGLMaskBlit(SurfaceType.IntRgb, CompositeType.SrcOver), new OGLMaskBlit(SurfaceType.IntRgb, CompositeType.SrcNoEa), new OGLMaskBlit(SurfaceType.IntBgr, CompositeType.SrcOver), new OGLMaskBlit(SurfaceType.IntBgr, CompositeType.SrcNoEa)});
    }

    private OGLMaskBlit(SurfaceType surfaceType, CompositeType compositeType) {
        super(OGLRenderQueue.getInstance(), surfaceType, compositeType, OGLSurfaceData.OpenGLSurface);
    }

    @Override // sun.java2d.pipe.BufferedMaskBlit
    protected void validateContext(SurfaceData surfaceData, Composite composite, Region region) {
        OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) surfaceData;
        OGLContext.validateContext(oGLSurfaceData, oGLSurfaceData, region, composite, null, null, null, 0);
    }
}
