package sun.java2d.opengl;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.TransformBlit;
import sun.java2d.pipe.Region;

/* compiled from: OGLBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/opengl/OGLSurfaceToSurfaceTransform.class */
class OGLSurfaceToSurfaceTransform extends TransformBlit {
    OGLSurfaceToSurfaceTransform() {
        super(OGLSurfaceData.OpenGLSurface, CompositeType.AnyAlpha, OGLSurfaceData.OpenGLSurface);
    }

    @Override // sun.java2d.loops.TransformBlit
    public void Transform(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        OGLBlitLoops.IsoBlit(surfaceData, surfaceData2, null, null, composite, region, affineTransform, i2, i3, i4, i3 + i7, i4 + i8, i5, i6, i5 + i7, i6 + i8, false);
    }
}
