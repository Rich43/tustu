package sun.awt.image;

import java.awt.image.DataBuffer;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/DataBufferNative.class */
public class DataBufferNative extends DataBuffer {
    protected SurfaceData surfaceData;
    protected int width;

    protected native int getElem(int i2, int i3, SurfaceData surfaceData);

    protected native void setElem(int i2, int i3, int i4, SurfaceData surfaceData);

    public DataBufferNative(SurfaceData surfaceData, int i2, int i3, int i4) {
        super(i2, i3 * i4);
        this.width = i3;
        this.surfaceData = surfaceData;
    }

    @Override // java.awt.image.DataBuffer
    public int getElem(int i2, int i3) {
        return getElem(i3 % this.width, i3 / this.width, this.surfaceData);
    }

    @Override // java.awt.image.DataBuffer
    public void setElem(int i2, int i3, int i4) {
        setElem(i3 % this.width, i3 / this.width, i4, this.surfaceData);
    }
}
