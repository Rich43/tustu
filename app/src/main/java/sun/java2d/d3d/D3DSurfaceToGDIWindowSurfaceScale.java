package sun.java2d.d3d;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.ScaledBlit;
import sun.java2d.pipe.Region;
import sun.java2d.windows.GDIWindowSurfaceData;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceToGDIWindowSurfaceScale.class */
class D3DSurfaceToGDIWindowSurfaceScale extends ScaledBlit {
    D3DSurfaceToGDIWindowSurfaceScale() {
        super(D3DSurfaceData.D3DSurface, CompositeType.AnyAlpha, GDIWindowSurfaceData.AnyGdi);
    }

    @Override // sun.java2d.loops.ScaledBlit
    public void Scale(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5) {
        D3DVolatileSurfaceManager.handleVItoScreenOp(surfaceData, surfaceData2);
    }
}
