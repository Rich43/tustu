package sun.java2d.pipe;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/LCDTextRenderer.class */
public class LCDTextRenderer extends GlyphListLoopPipe {
    @Override // sun.java2d.pipe.GlyphListPipe
    protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
        sunGraphics2D.loops.drawGlyphListLCDLoop.DrawGlyphListLCD(sunGraphics2D, sunGraphics2D.surfaceData, glyphList);
    }
}
