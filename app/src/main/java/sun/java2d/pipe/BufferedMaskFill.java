package sun.java2d.pipe;

import java.awt.AlphaComposite;
import java.awt.Composite;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.MaskFill;
import sun.java2d.loops.SurfaceType;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedMaskFill.class */
public abstract class BufferedMaskFill extends MaskFill {
    protected final RenderQueue rq;

    protected abstract void maskFill(int i2, int i3, int i4, int i5, int i6, int i7, int i8, byte[] bArr);

    protected abstract void validateContext(SunGraphics2D sunGraphics2D, Composite composite, int i2);

    protected BufferedMaskFill(RenderQueue renderQueue, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(surfaceType, compositeType, surfaceType2);
        this.rq = renderQueue;
    }

    @Override // sun.java2d.loops.MaskFill
    public void MaskFill(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, Composite composite, final int i2, final int i3, final int i4, final int i5, final byte[] bArr, final int i6, final int i7) {
        int length;
        if (((AlphaComposite) composite).getRule() != 3) {
            composite = AlphaComposite.SrcOver;
        }
        this.rq.lock();
        try {
            validateContext(sunGraphics2D, composite, 2);
            if (bArr != null) {
                length = (bArr.length + 3) & (-4);
            } else {
                length = 0;
            }
            int i8 = 32 + length;
            RenderBuffer buffer = this.rq.getBuffer();
            if (i8 <= buffer.capacity()) {
                if (i8 > buffer.remaining()) {
                    this.rq.flushNow();
                }
                buffer.putInt(32);
                buffer.putInt(i2).putInt(i3).putInt(i4).putInt(i5);
                buffer.putInt(i6);
                buffer.putInt(i7);
                buffer.putInt(length);
                if (bArr != null) {
                    int length2 = length - bArr.length;
                    buffer.put(bArr);
                    if (length2 != 0) {
                        buffer.position(buffer.position() + length2);
                    }
                }
            } else {
                this.rq.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.pipe.BufferedMaskFill.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BufferedMaskFill.this.maskFill(i2, i3, i4, i5, i6, i7, bArr.length, bArr);
                    }
                });
            }
        } finally {
            this.rq.unlock();
        }
    }
}
