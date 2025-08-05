package sun.java2d.pipe;

import java.awt.font.GlyphVector;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/TextPipe.class */
public interface TextPipe {
    void drawString(SunGraphics2D sunGraphics2D, String str, double d2, double d3);

    void drawGlyphVector(SunGraphics2D sunGraphics2D, GlyphVector glyphVector, float f2, float f3);

    void drawChars(SunGraphics2D sunGraphics2D, char[] cArr, int i2, int i3, int i4, int i5);
}
