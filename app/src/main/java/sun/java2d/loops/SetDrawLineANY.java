package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/SetDrawLineANY.class */
class SetDrawLineANY extends DrawLine {
    SetDrawLineANY() {
        super(SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.DrawLine
    public void DrawLine(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
        PixelWriter pixelWriterCreateSolidPixelWriter = GeneralRenderer.createSolidPixelWriter(sunGraphics2D, surfaceData);
        if (i3 >= i5) {
            GeneralRenderer.doDrawLine(surfaceData, pixelWriterCreateSolidPixelWriter, null, sunGraphics2D.getCompClip(), i4, i5, i2, i3);
        } else {
            GeneralRenderer.doDrawLine(surfaceData, pixelWriterCreateSolidPixelWriter, null, sunGraphics2D.getCompClip(), i2, i3, i4, i5);
        }
    }
}
