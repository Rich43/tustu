package sun.java2d.d3d;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.ScaledBlit;
import sun.java2d.pipe.Region;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DRTTSurfaceToSurfaceScale.class */
class D3DRTTSurfaceToSurfaceScale extends ScaledBlit {
    D3DRTTSurfaceToSurfaceScale() {
        super(D3DSurfaceData.D3DSurfaceRTT, CompositeType.AnyAlpha, D3DSurfaceData.D3DSurface);
    }

    @Override // sun.java2d.loops.ScaledBlit
    public void Scale(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5) {
        D3DBlitLoops.IsoBlit(surfaceData, surfaceData2, null, null, composite, region, null, 1, i2, i3, i4, i5, d2, d3, d4, d5, true);
    }
}
