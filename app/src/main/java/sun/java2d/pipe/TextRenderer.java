package sun.java2d.pipe;

import java.awt.Rectangle;
import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/TextRenderer.class */
public class TextRenderer extends GlyphListPipe {
    CompositePipe outpipe;

    public TextRenderer(CompositePipe compositePipe) {
        this.outpipe = compositePipe;
    }

    @Override // sun.java2d.pipe.GlyphListPipe
    protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList) {
        int numGlyphs = glyphList.getNumGlyphs();
        Region compClip = sunGraphics2D.getCompClip();
        int loX = compClip.getLoX();
        int loY = compClip.getLoY();
        int hiX = compClip.getHiX();
        int hiY = compClip.getHiY();
        Object objStartSequence = null;
        try {
            int[] bounds = glyphList.getBounds();
            Rectangle rectangle = new Rectangle(bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1]);
            objStartSequence = this.outpipe.startSequence(sunGraphics2D, sunGraphics2D.untransformShape(rectangle), rectangle, bounds);
            for (int i2 = 0; i2 < numGlyphs; i2++) {
                glyphList.setGlyphIndex(i2);
                int[] metrics = glyphList.getMetrics();
                int i3 = metrics[0];
                int i4 = metrics[1];
                int i5 = metrics[2];
                int i6 = i3 + i5;
                int i7 = i4 + metrics[3];
                int i8 = 0;
                if (i3 < loX) {
                    i8 = loX - i3;
                    i3 = loX;
                }
                if (i4 < loY) {
                    i8 += (loY - i4) * i5;
                    i4 = loY;
                }
                if (i6 > hiX) {
                    i6 = hiX;
                }
                if (i7 > hiY) {
                    i7 = hiY;
                }
                if (i6 > i3 && i7 > i4 && this.outpipe.needTile(objStartSequence, i3, i4, i6 - i3, i7 - i4)) {
                    this.outpipe.renderPathTile(objStartSequence, glyphList.getGrayBits(), i8, i5, i3, i4, i6 - i3, i7 - i4);
                } else {
                    this.outpipe.skipTile(objStartSequence, i3, i4);
                }
            }
            if (objStartSequence != null) {
                this.outpipe.endSequence(objStartSequence);
            }
        } catch (Throwable th) {
            if (objStartSequence != null) {
                this.outpipe.endSequence(objStartSequence);
            }
            throw th;
        }
    }
}
