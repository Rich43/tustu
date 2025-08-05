package sun.java2d.opengl;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;

/* compiled from: OGLBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/opengl/OGLAnyCompositeBlit.class */
final class OGLAnyCompositeBlit extends Blit {
    private WeakReference<SurfaceData> dstTmp;
    private WeakReference<SurfaceData> srcTmp;
    private final Blit convertsrc;
    private final Blit convertdst;
    private final Blit convertresult;

    OGLAnyCompositeBlit(SurfaceType surfaceType, Blit blit, Blit blit2, Blit blit3) {
        super(surfaceType, CompositeType.Any, OGLSurfaceData.OpenGLSurface);
        this.convertsrc = blit;
        this.convertdst = blit2;
        this.convertresult = blit3;
    }

    @Override // sun.java2d.loops.Blit
    public synchronized void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (this.convertsrc != null) {
            SurfaceData surfaceData3 = null;
            if (this.srcTmp != null) {
                surfaceData3 = this.srcTmp.get();
            }
            surfaceData = convertFrom(this.convertsrc, surfaceData, i2, i3, i6, i7, surfaceData3, 3);
            if (surfaceData != surfaceData3) {
                this.srcTmp = new WeakReference<>(surfaceData);
            }
        }
        SurfaceData surfaceData4 = null;
        if (this.dstTmp != null) {
            surfaceData4 = this.dstTmp.get();
        }
        SurfaceData surfaceDataConvertFrom = convertFrom(this.convertdst, surfaceData2, i4, i5, i6, i7, surfaceData4, 3);
        Blit.getFromCache(surfaceData.getSurfaceType(), CompositeType.Any, surfaceDataConvertFrom.getSurfaceType()).Blit(surfaceData, surfaceDataConvertFrom, composite, region == null ? null : region.getTranslatedRegion(-i4, -i5), i2, i3, 0, 0, i6, i7);
        if (surfaceDataConvertFrom != surfaceData4) {
            this.dstTmp = new WeakReference<>(surfaceDataConvertFrom);
        }
        this.convertresult.Blit(surfaceDataConvertFrom, surfaceData2, AlphaComposite.Src, region, 0, 0, i4, i5, i6, i7);
    }
}
