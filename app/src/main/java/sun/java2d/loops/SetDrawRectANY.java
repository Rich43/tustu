package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/SetDrawRectANY.class */
class SetDrawRectANY extends DrawRect {
    SetDrawRectANY() {
        super(SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.DrawRect
    public void DrawRect(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int i2, int i3, int i4, int i5) {
        GeneralRenderer.doDrawRect(GeneralRenderer.createSolidPixelWriter(sunGraphics2D, surfaceData), sunGraphics2D, surfaceData, i2, i3, i4, i5);
    }
}
