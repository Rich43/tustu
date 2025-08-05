package sun.java2d.d3d;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.loops.TransformBlit;
import sun.java2d.pipe.Region;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DSwToSurfaceTransform.class */
class D3DSwToSurfaceTransform extends TransformBlit {
    private int typeval;

    D3DSwToSurfaceTransform(SurfaceType surfaceType, int i2) {
        super(surfaceType, CompositeType.AnyAlpha, D3DSurfaceData.D3DSurface);
        this.typeval = i2;
    }

    @Override // sun.java2d.loops.TransformBlit
    public void Transform(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        D3DBlitLoops.Blit(surfaceData, surfaceData2, composite, region, affineTransform, i2, i3, i4, i3 + i7, i4 + i8, i5, i6, i5 + i7, i6 + i8, this.typeval, false);
    }
}
