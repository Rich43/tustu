package sun.java2d.d3d;

import java.awt.Composite;
import java.awt.geom.AffineTransform;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.loops.TransformBlit;
import sun.java2d.pipe.Region;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DGeneralTransformedBlit.class */
final class D3DGeneralTransformedBlit extends TransformBlit {
    private final TransformBlit performop;
    private WeakReference<SurfaceData> srcTmp;

    D3DGeneralTransformedBlit(TransformBlit transformBlit) {
        super(SurfaceType.Any, CompositeType.AnyAlpha, D3DSurfaceData.D3DSurface);
        this.performop = transformBlit;
    }

    @Override // sun.java2d.loops.TransformBlit
    public synchronized void Transform(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Blit fromCache = Blit.getFromCache(surfaceData.getSurfaceType(), CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
        SurfaceData surfaceData3 = this.srcTmp != null ? this.srcTmp.get() : null;
        SurfaceData surfaceDataConvertFrom = convertFrom(fromCache, surfaceData, i3, i4, i7, i8, surfaceData3, 3);
        this.performop.Transform(surfaceDataConvertFrom, surfaceData2, composite, region, affineTransform, i2, 0, 0, i5, i6, i7, i8);
        if (surfaceDataConvertFrom != surfaceData3) {
            this.srcTmp = new WeakReference<>(surfaceDataConvertFrom);
        }
    }
}
