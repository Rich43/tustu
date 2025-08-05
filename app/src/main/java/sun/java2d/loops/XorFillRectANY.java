package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/XorFillRectANY.class */
class XorFillRectANY extends FillRect {
    XorFillRectANY() {
        super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.FillRect
    public void FillRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
        PixelWriter pixelWriterCreateXorPixelWriter = GeneralRenderer.createXorPixelWriter(sunGraphics2D, surfaceData);
        Region boundsIntersectionXYWH = sunGraphics2D.getCompClip().getBoundsIntersectionXYWH(i2, i3, i4, i5);
        GeneralRenderer.doSetRect(surfaceData, pixelWriterCreateXorPixelWriter, boundsIntersectionXYWH.getLoX(), boundsIntersectionXYWH.getLoY(), boundsIntersectionXYWH.getHiX(), boundsIntersectionXYWH.getHiY());
    }
}
