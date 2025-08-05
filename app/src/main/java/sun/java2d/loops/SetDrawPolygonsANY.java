package sun.java2d.loops;

import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/SetDrawPolygonsANY.class */
class SetDrawPolygonsANY extends DrawPolygons {
    SetDrawPolygonsANY() {
        super(SurfaceType.AnyColor, CompositeType.SrcNoEa, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.DrawPolygons
    public void DrawPolygons(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, int[] iArr, int[] iArr2, int[] iArr3, int i2, int i3, int i4, boolean z2) {
        PixelWriter pixelWriterCreateSolidPixelWriter = GeneralRenderer.createSolidPixelWriter(sunGraphics2D, surfaceData);
        int i5 = 0;
        Region compClip = sunGraphics2D.getCompClip();
        for (int i6 = 0; i6 < i2; i6++) {
            int i7 = iArr3[i6];
            GeneralRenderer.doDrawPoly(surfaceData, pixelWriterCreateSolidPixelWriter, iArr, iArr2, i5, i7, compClip, i3, i4, z2);
            i5 += i7;
        }
    }
}
