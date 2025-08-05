package sun.java2d.loops;

import sun.java2d.SurfaceData;
import sun.java2d.loops.ProcessPath;
import sun.java2d.pipe.Region;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/PixelWriterDrawHandler.class */
class PixelWriterDrawHandler extends ProcessPath.DrawHandler {
    PixelWriter pw;
    SurfaceData sData;
    Region clip;

    public PixelWriterDrawHandler(SurfaceData surfaceData, PixelWriter pixelWriter, Region region, int i2) {
        super(region.getLoX(), region.getLoY(), region.getHiX(), region.getHiY(), i2);
        this.sData = surfaceData;
        this.pw = pixelWriter;
        this.clip = region;
    }

    @Override // sun.java2d.loops.ProcessPath.DrawHandler
    public void drawLine(int i2, int i3, int i4, int i5) {
        GeneralRenderer.doDrawLine(this.sData, this.pw, null, this.clip, i2, i3, i4, i5);
    }

    @Override // sun.java2d.loops.ProcessPath.DrawHandler
    public void drawPixel(int i2, int i3) {
        GeneralRenderer.doSetRect(this.sData, this.pw, i2, i3, i2 + 1, i3 + 1);
    }

    @Override // sun.java2d.loops.ProcessPath.DrawHandler
    public void drawScanline(int i2, int i3, int i4) {
        GeneralRenderer.doSetRect(this.sData, this.pw, i2, i4, i3 + 1, i4 + 1);
    }
}
