package sun.java2d.loops;

import java.awt.geom.Path2D;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/XorDrawPathANY.class */
class XorDrawPathANY extends DrawPath {
    XorDrawPathANY() {
        super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.DrawPath
    public void DrawPath(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, Path2D.Float r12) {
        ProcessPath.drawPath(new PixelWriterDrawHandler(surfaceData, GeneralRenderer.createXorPixelWriter(sunGraphics2D, surfaceData), sunGraphics2D.getCompClip(), sunGraphics2D.strokeHint), r12, i2, i3);
    }
}
