package sun.java2d.pipe;

import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import sun.font.GlyphList;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.FontInfo;

/* loaded from: rt.jar:sun/java2d/pipe/GlyphListPipe.class */
public abstract class GlyphListPipe implements TextPipe {
    protected abstract void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList);

    @Override // sun.java2d.pipe.TextPipe
    public void drawString(SunGraphics2D sunGraphics2D, String str, double d2, double d3) {
        float f2;
        float f3;
        FontInfo fontInfo = sunGraphics2D.getFontInfo();
        if (fontInfo.pixelHeight > 100) {
            SurfaceData.outlineTextRenderer.drawString(sunGraphics2D, str, d2, d3);
            return;
        }
        if (sunGraphics2D.transformState >= 3) {
            double[] dArr = {d2 + fontInfo.originX, d3 + fontInfo.originY};
            sunGraphics2D.transform.transform(dArr, 0, dArr, 0, 1);
            f2 = (float) dArr[0];
            f3 = (float) dArr[1];
        } else {
            f2 = (float) (d2 + fontInfo.originX + sunGraphics2D.transX);
            f3 = (float) (d3 + fontInfo.originY + sunGraphics2D.transY);
        }
        GlyphList glyphList = GlyphList.getInstance();
        if (glyphList.setFromString(fontInfo, str, f2, f3)) {
            drawGlyphList(sunGraphics2D, glyphList);
            glyphList.dispose();
        } else {
            glyphList.dispose();
            new TextLayout(str, sunGraphics2D.getFont(), sunGraphics2D.getFontRenderContext()).draw(sunGraphics2D, (float) d2, (float) d3);
        }
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawChars(SunGraphics2D sunGraphics2D, char[] cArr, int i2, int i3, int i4, int i5) {
        float f2;
        float f3;
        FontInfo fontInfo = sunGraphics2D.getFontInfo();
        if (fontInfo.pixelHeight > 100) {
            SurfaceData.outlineTextRenderer.drawChars(sunGraphics2D, cArr, i2, i3, i4, i5);
            return;
        }
        if (sunGraphics2D.transformState >= 3) {
            double[] dArr = {i4 + fontInfo.originX, i5 + fontInfo.originY};
            sunGraphics2D.transform.transform(dArr, 0, dArr, 0, 1);
            f2 = (float) dArr[0];
            f3 = (float) dArr[1];
        } else {
            f2 = i4 + fontInfo.originX + sunGraphics2D.transX;
            f3 = i5 + fontInfo.originY + sunGraphics2D.transY;
        }
        GlyphList glyphList = GlyphList.getInstance();
        if (glyphList.setFromChars(fontInfo, cArr, i2, i3, f2, f3)) {
            drawGlyphList(sunGraphics2D, glyphList);
            glyphList.dispose();
        } else {
            glyphList.dispose();
            new TextLayout(new String(cArr, i2, i3), sunGraphics2D.getFont(), sunGraphics2D.getFontRenderContext()).draw(sunGraphics2D, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawGlyphVector(SunGraphics2D sunGraphics2D, GlyphVector glyphVector, float f2, float f3) {
        float f4;
        float f5;
        FontInfo gVFontInfo = sunGraphics2D.getGVFontInfo(glyphVector.getFont(), glyphVector.getFontRenderContext());
        if (gVFontInfo.pixelHeight > 100) {
            SurfaceData.outlineTextRenderer.drawGlyphVector(sunGraphics2D, glyphVector, f2, f3);
            return;
        }
        if (sunGraphics2D.transformState >= 3) {
            double[] dArr = {f2, f3};
            sunGraphics2D.transform.transform(dArr, 0, dArr, 0, 1);
            f4 = (float) dArr[0];
            f5 = (float) dArr[1];
        } else {
            f4 = f2 + sunGraphics2D.transX;
            f5 = f3 + sunGraphics2D.transY;
        }
        GlyphList glyphList = GlyphList.getInstance();
        glyphList.setFromGlyphVector(gVFontInfo, glyphVector, f4, f5);
        drawGlyphList(sunGraphics2D, glyphList, gVFontInfo.aaHint);
        glyphList.dispose();
    }

    protected void drawGlyphList(SunGraphics2D sunGraphics2D, GlyphList glyphList, int i2) {
        drawGlyphList(sunGraphics2D, glyphList);
    }
}
