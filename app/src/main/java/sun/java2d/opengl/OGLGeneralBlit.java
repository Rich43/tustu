package sun.java2d.opengl;

import java.awt.Composite;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;

/* compiled from: OGLBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/opengl/OGLGeneralBlit.class */
class OGLGeneralBlit extends Blit {
    private final Blit performop;
    private WeakReference srcTmp;

    OGLGeneralBlit(SurfaceType surfaceType, CompositeType compositeType, Blit blit) {
        super(SurfaceType.Any, compositeType, surfaceType);
        this.performop = blit;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.java2d.loops.Blit
    public synchronized void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        Blit fromCache = Blit.getFromCache(surfaceData.getSurfaceType(), CompositeType.SrcNoEa, SurfaceType.IntArgbPre);
        SurfaceData surfaceData3 = null;
        if (this.srcTmp != null) {
            surfaceData3 = (SurfaceData) this.srcTmp.get();
        }
        SurfaceData surfaceDataConvertFrom = convertFrom(fromCache, surfaceData, i2, i3, i6, i7, surfaceData3, 3);
        this.performop.Blit(surfaceDataConvertFrom, surfaceData2, composite, region, 0, 0, i4, i5, i6, i7);
        if (surfaceDataConvertFrom != surfaceData3) {
            this.srcTmp = new WeakReference(surfaceDataConvertFrom);
        }
    }
}
