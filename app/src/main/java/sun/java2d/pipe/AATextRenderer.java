package sun.java2d.pipe;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/AATextRenderer.class */
public class AATextRenderer extends GlyphListLoopPipe implements LoopBasedPipe {
    @Override // sun.java2d.pipe.GlyphListPipe
    protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
        sunGraphics2D.loops.drawGlyphListAALoop.DrawGlyphListAA(sunGraphics2D, sunGraphics2D.surfaceData, glyphList);
    }
}
