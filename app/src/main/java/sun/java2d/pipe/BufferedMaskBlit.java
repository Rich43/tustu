package sun.java2d.pipe;

import java.awt.AlphaComposite;
import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.MaskBlit;
import sun.java2d.loops.SurfaceType;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedMaskBlit.class */
public abstract class BufferedMaskBlit extends MaskBlit {
    private static final int ST_INT_ARGB = 0;
    private static final int ST_INT_ARGB_PRE = 1;
    private static final int ST_INT_RGB = 2;
    private static final int ST_INT_BGR = 3;
    private final RenderQueue rq;
    private final int srcTypeVal;
    private Blit blitop;

    private native int enqueueTile(long j2, int i2, SurfaceData surfaceData, long j3, int i3, byte[] bArr, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12);

    protected abstract void validateContext(SurfaceData surfaceData, Composite composite, Region region);

    protected BufferedMaskBlit(RenderQueue renderQueue, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(surfaceType, compositeType, surfaceType2);
        this.rq = renderQueue;
        if (surfaceType == SurfaceType.IntArgb) {
            this.srcTypeVal = 0;
            return;
        }
        if (surfaceType == SurfaceType.IntArgbPre) {
            this.srcTypeVal = 1;
        } else if (surfaceType == SurfaceType.IntRgb) {
            this.srcTypeVal = 2;
        } else {
            if (surfaceType == SurfaceType.IntBgr) {
                this.srcTypeVal = 3;
                return;
            }
            throw new InternalError("unrecognized source surface type");
        }
    }

    @Override // sun.java2d.loops.MaskBlit
    public void MaskBlit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, byte[] bArr, int i8, int i9) {
        if (i6 <= 0 || i7 <= 0) {
            return;
        }
        if (bArr == null) {
            if (this.blitop == null) {
                this.blitop = Blit.getFromCache(surfaceData.getSurfaceType(), CompositeType.AnyAlpha, getDestType());
            }
            this.blitop.Blit(surfaceData, surfaceData2, composite, region, i2, i3, i4, i5, i6, i7);
            return;
        }
        if (((AlphaComposite) composite).getRule() != 3) {
            composite = AlphaComposite.SrcOver;
        }
        this.rq.lock();
        try {
            validateContext(surfaceData2, composite, region);
            RenderBuffer buffer = this.rq.getBuffer();
            this.rq.ensureCapacity(20 + (i6 * i7 * 4));
            buffer.position(enqueueTile(buffer.getAddress(), buffer.position(), surfaceData, surfaceData.getNativeOps(), this.srcTypeVal, bArr, bArr.length, i8, i9, i2, i3, i4, i5, i6, i7));
            this.rq.unlock();
        } catch (Throwable th) {
            this.rq.unlock();
            throw th;
        }
    }
}
