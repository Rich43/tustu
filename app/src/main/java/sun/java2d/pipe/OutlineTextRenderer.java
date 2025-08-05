package sun.java2d.pipe;

import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/OutlineTextRenderer.class */
public class OutlineTextRenderer implements TextPipe {
    public static final int THRESHHOLD = 100;

    @Override // sun.java2d.pipe.TextPipe
    public void drawChars(SunGraphics2D sunGraphics2D, char[] cArr, int i2, int i3, int i4, int i5) {
        drawString(sunGraphics2D, new String(cArr, i2, i3), i4, i5);
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawString(SunGraphics2D sunGraphics2D, String str, double d2, double d3) {
        if ("".equals(str)) {
            return;
        }
        Shape outline = new TextLayout(str, sunGraphics2D.getFont(), sunGraphics2D.getFontRenderContext()).getOutline(AffineTransform.getTranslateInstance(d2, d3));
        int i2 = sunGraphics2D.getFontInfo().aaHint;
        int i3 = -1;
        if (i2 != 1 && sunGraphics2D.antialiasHint != 2) {
            i3 = sunGraphics2D.antialiasHint;
            sunGraphics2D.antialiasHint = 2;
            sunGraphics2D.validatePipe();
        } else if (i2 == 1 && sunGraphics2D.antialiasHint != 1) {
            i3 = sunGraphics2D.antialiasHint;
            sunGraphics2D.antialiasHint = 1;
            sunGraphics2D.validatePipe();
        }
        sunGraphics2D.fill(outline);
        if (i3 != -1) {
            sunGraphics2D.antialiasHint = i3;
            sunGraphics2D.validatePipe();
        }
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawGlyphVector(SunGraphics2D sunGraphics2D, GlyphVector glyphVector, float f2, float f3) {
        Shape outline = glyphVector.getOutline(f2, f3);
        int i2 = -1;
        FontRenderContext fontRenderContext = glyphVector.getFontRenderContext();
        boolean zIsAntiAliased = fontRenderContext.isAntiAliased();
        if (zIsAntiAliased && sunGraphics2D.getGVFontInfo(glyphVector.getFont(), fontRenderContext).aaHint == 1) {
            zIsAntiAliased = false;
        }
        if (zIsAntiAliased && sunGraphics2D.antialiasHint != 2) {
            i2 = sunGraphics2D.antialiasHint;
            sunGraphics2D.antialiasHint = 2;
            sunGraphics2D.validatePipe();
        } else if (!zIsAntiAliased && sunGraphics2D.antialiasHint != 1) {
            i2 = sunGraphics2D.antialiasHint;
            sunGraphics2D.antialiasHint = 1;
            sunGraphics2D.validatePipe();
        }
        sunGraphics2D.fill(outline);
        if (i2 != -1) {
            sunGraphics2D.antialiasHint = i2;
            sunGraphics2D.validatePipe();
        }
    }
}
