package sun.java2d.pipe;

import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/GlyphListLoopPipe.class */
public abstract class GlyphListLoopPipe extends GlyphListPipe implements LoopBasedPipe {
    @Override // sun.java2d.pipe.GlyphListPipe
    protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList, int i2) {
        switch (i2) {
            case 1:
                sunGraphics2D.loops.drawGlyphListLoop.DrawGlyphList(sunGraphics2D, sunGraphics2D.surfaceData, glyphList);
                break;
            case 2:
                sunGraphics2D.loops.drawGlyphListAALoop.DrawGlyphListAA(sunGraphics2D, sunGraphics2D.surfaceData, glyphList);
                break;
            case 4:
            case 6:
                sunGraphics2D.loops.drawGlyphListLCDLoop.DrawGlyphListLCD(sunGraphics2D, sunGraphics2D.surfaceData, glyphList);
                break;
        }
    }
}
