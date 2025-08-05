package sun.java2d.loops;

import java.awt.image.WritableRaster;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/PixelWriter.class */
abstract class PixelWriter {
    protected WritableRaster dstRast;

    public abstract void writePixel(int i2, int i3);

    PixelWriter() {
    }

    public void setRaster(WritableRaster writableRaster) {
        this.dstRast = writableRaster;
    }
}
