package sun.java2d.opengl;

import java.awt.Composite;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderBuffer;

/* compiled from: OGLBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/opengl/OGLSurfaceToSwBlit.class */
final class OGLSurfaceToSwBlit extends Blit {
    private final int typeval;
    private WeakReference<SurfaceData> srcTmp;

    OGLSurfaceToSwBlit(SurfaceType surfaceType, int i2) {
        super(OGLSurfaceData.OpenGLSurface, CompositeType.SrcNoEa, surfaceType);
        this.typeval = i2;
    }

    private synchronized void complexClipBlit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        SurfaceData surfaceData3 = null;
        if (this.srcTmp != null) {
            surfaceData3 = this.srcTmp.get();
        }
        SurfaceData surfaceDataConvertFrom = convertFrom(this, surfaceData, i2, i3, i6, i7, surfaceData3, this.typeval == 1 ? 3 : 2);
        Blit.getFromCache(surfaceDataConvertFrom.getSurfaceType(), CompositeType.SrcNoEa, surfaceData2.getSurfaceType()).Blit(surfaceDataConvertFrom, surfaceData2, composite, region, 0, 0, i4, i5, i6, i7);
        if (surfaceDataConvertFrom != surfaceData3) {
            this.srcTmp = new WeakReference<>(surfaceDataConvertFrom);
        }
    }

    @Override // sun.java2d.loops.Blit
    public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (region != null) {
            Region intersectionXYWH = region.getIntersectionXYWH(i4, i5, i6, i7);
            if (intersectionXYWH.isEmpty()) {
                return;
            }
            i2 += intersectionXYWH.getLoX() - i4;
            i3 += intersectionXYWH.getLoY() - i5;
            i4 = intersectionXYWH.getLoX();
            i5 = intersectionXYWH.getLoY();
            i6 = intersectionXYWH.getWidth();
            i7 = intersectionXYWH.getHeight();
            if (!intersectionXYWH.isRectangular()) {
                complexClipBlit(surfaceData, surfaceData2, composite, intersectionXYWH, i2, i3, i4, i5, i6, i7);
                return;
            }
        }
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            oGLRenderQueue.addReference(surfaceData2);
            RenderBuffer buffer = oGLRenderQueue.getBuffer();
            OGLContext.validateContext((OGLSurfaceData) surfaceData);
            oGLRenderQueue.ensureCapacityAndAlignment(48, 32);
            buffer.putInt(34);
            buffer.putInt(i2).putInt(i3);
            buffer.putInt(i4).putInt(i5);
            buffer.putInt(i6).putInt(i7);
            buffer.putInt(this.typeval);
            buffer.putLong(surfaceData.getNativeOps());
            buffer.putLong(surfaceData2.getNativeOps());
            oGLRenderQueue.flushNow();
            oGLRenderQueue.unlock();
        } catch (Throwable th) {
            oGLRenderQueue.unlock();
            throw th;
        }
    }
}
