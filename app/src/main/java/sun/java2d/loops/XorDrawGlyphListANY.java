package sun.java2d.loops;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/XorDrawGlyphListANY.class */
class XorDrawGlyphListANY extends DrawGlyphList {
    XorDrawGlyphListANY() {
        super(SurfaceType.AnyColor, CompositeType.Xor, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.DrawGlyphList
    public void DrawGlyphList(SunGraphics2D sunGraphics2D, SurfaceData surfaceData, GlyphList glyphList) {
        GeneralRenderer.doDrawGlyphList(surfaceData, GeneralRenderer.createXorPixelWriter(sunGraphics2D, surfaceData), glyphList, sunGraphics2D.getCompClip());
    }
}
