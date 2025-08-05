package sun.java2d.d3d;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.TransformBlit;
import sun.java2d.pipe.Region;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceToSurfaceTransform.class */
class D3DSurfaceToSurfaceTransform extends TransformBlit {
    D3DSurfaceToSurfaceTransform() {
        super(D3DSurfaceData.D3DSurface, CompositeType.AnyAlpha, D3DSurfaceData.D3DSurface);
    }

    @Override // sun.java2d.loops.TransformBlit
    public void Transform(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        D3DBlitLoops.IsoBlit(surfaceData, surfaceData2, null, null, composite, region, affineTransform, i2, i3, i4, i3 + i7, i4 + i8, i5, i6, i5 + i7, i6 + i8, false);
    }
}
