package sun.java2d.d3d;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.TransformBlit;
import sun.java2d.pipe.Region;
import sun.java2d.windows.GDIWindowSurfaceData;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceToGDIWindowSurfaceTransform.class */
class D3DSurfaceToGDIWindowSurfaceTransform extends TransformBlit {
    D3DSurfaceToGDIWindowSurfaceTransform() {
        super(D3DSurfaceData.D3DSurface, CompositeType.AnyAlpha, GDIWindowSurfaceData.AnyGdi);
    }

    @Override // sun.java2d.loops.TransformBlit
    public void Transform(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        D3DVolatileSurfaceManager.handleVItoScreenOp(surfaceData, surfaceData2);
    }
}
