package sun.java2d.loops;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/XorDrawGlyphListAAANY.class */
class XorDrawGlyphListAAANY extends DrawGlyphListAA {
    XorDrawGlyphListAAANY() {
        super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.DrawGlyphListAA
    public void DrawGlyphListAA(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
        GeneralRenderer.doDrawGlyphList(surfaceData, GeneralRenderer.createXorPixelWriter(sunGraphics2D, surfaceData), glyphList, sunGraphics2D.getCompClip());
    }
}
