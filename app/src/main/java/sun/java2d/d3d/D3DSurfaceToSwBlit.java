package sun.java2d.d3d;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderBuffer;

/* compiled from: D3DBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceToSwBlit.class */
class D3DSurfaceToSwBlit extends Blit {
    private int typeval;

    D3DSurfaceToSwBlit(SurfaceType surfaceType, int i2) {
        super(D3DSurfaceData.D3DSurface, CompositeType.SrcNoEa, surfaceType);
        this.typeval = i2;
    }

    @Override // sun.java2d.loops.Blit
    public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.addReference(surfaceData2);
            RenderBuffer buffer = d3DRenderQueue.getBuffer();
            D3DContext.setScratchSurface(((D3DSurfaceData) surfaceData).getContext());
            d3DRenderQueue.ensureCapacityAndAlignment(48, 32);
            buffer.putInt(34);
            buffer.putInt(i2).putInt(i3);
            buffer.putInt(i4).putInt(i5);
            buffer.putInt(i6).putInt(i7);
            buffer.putInt(this.typeval);
            buffer.putLong(surfaceData.getNativeOps());
            buffer.putLong(surfaceData2.getNativeOps());
            d3DRenderQueue.flushNow();
            d3DRenderQueue.unlock();
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }
}
