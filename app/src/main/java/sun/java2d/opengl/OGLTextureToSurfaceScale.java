package sun.java2d.opengl;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.ScaledBlit;
import sun.java2d.pipe.Region;

/* compiled from: OGLBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/opengl/OGLTextureToSurfaceScale.class */
class OGLTextureToSurfaceScale extends ScaledBlit {
    OGLTextureToSurfaceScale() {
        super(OGLSurfaceData.OpenGLTexture, CompositeType.AnyAlpha, OGLSurfaceData.OpenGLSurface);
    }

    @Override // sun.java2d.loops.ScaledBlit
    public void Scale(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5) {
        OGLBlitLoops.IsoBlit(surfaceData, surfaceData2, null, null, composite, region, null, 1, i2, i3, i4, i5, d2, d3, d4, d5, true);
    }
}
