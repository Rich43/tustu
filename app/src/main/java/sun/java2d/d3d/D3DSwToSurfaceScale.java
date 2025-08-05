package sun.java2d.d3d;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.ScaledBlit;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DSwToSurfaceScale.class */
class D3DSwToSurfaceScale extends ScaledBlit {
    private int typeval;

    D3DSwToSurfaceScale(SurfaceType surfaceType, int i2) {
        super(surfaceType, CompositeType.AnyAlpha, D3DSurfaceData.D3DSurface);
        this.typeval = i2;
    }

    @Override // sun.java2d.loops.ScaledBlit
    public void Scale(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5) {
        D3DBlitLoops.Blit(surfaceData, surfaceData2, composite, region, null, 1, i2, i3, i4, i5, d2, d3, d4, d5, this.typeval, false);
    }
}
