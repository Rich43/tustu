package sun.font;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/CompositeStrike.class */
public final class CompositeStrike extends FontStrike {
    static final int SLOTMASK = 16777215;
    private CompositeFont compFont;
    private PhysicalStrike[] strikes;
    int numGlyphs = 0;

    CompositeStrike(CompositeFont compositeFont, FontStrikeDesc fontStrikeDesc) {
        this.compFont = compositeFont;
        this.desc = fontStrikeDesc;
        this.disposer = new FontStrikeDisposer(this.compFont, fontStrikeDesc);
        if (fontStrikeDesc.style != this.compFont.style) {
            this.algoStyle = true;
            if ((fontStrikeDesc.style & 1) == 1 && (this.compFont.style & 1) == 0) {
                this.boldness = 1.33f;
            }
            if ((fontStrikeDesc.style & 2) == 2 && (this.compFont.style & 2) == 0) {
                this.italic = 0.7f;
            }
        }
        this.strikes = new PhysicalStrike[this.compFont.numSlots];
    }

    PhysicalStrike getStrikeForGlyph(int i2) {
        return getStrikeForSlot(i2 >>> 24);
    }

    PhysicalStrike getStrikeForSlot(int i2) {
        if (i2 >= this.strikes.length) {
            i2 = 0;
        }
        PhysicalStrike physicalStrike = this.strikes[i2];
        if (physicalStrike == null) {
            physicalStrike = (PhysicalStrike) this.compFont.getSlotFont(i2).getStrike(this.desc);
            this.strikes[i2] = physicalStrike;
        }
        return physicalStrike;
    }

    @Override // sun.font.FontStrike
    public int getNumGlyphs() {
        return this.compFont.getNumGlyphs();
    }

    @Override // sun.font.FontStrike
    StrikeMetrics getFontMetrics() {
        if (this.strikeMetrics == null) {
            StrikeMetrics strikeMetrics = new StrikeMetrics();
            for (int i2 = 0; i2 < this.compFont.numMetricsSlots; i2++) {
                strikeMetrics.merge(getStrikeForSlot(i2).getFontMetrics());
            }
            this.strikeMetrics = strikeMetrics;
        }
        return this.strikeMetrics;
    }

    @Override // sun.font.FontStrike
    void getGlyphImagePtrs(int[] iArr, long[] jArr, int i2) {
        int slot0GlyphImagePtrs = getStrikeForSlot(0).getSlot0GlyphImagePtrs(iArr, jArr, i2);
        if (slot0GlyphImagePtrs == i2) {
            return;
        }
        for (int i3 = slot0GlyphImagePtrs; i3 < i2; i3++) {
            jArr[i3] = getStrikeForGlyph(iArr[i3]).getGlyphImagePtr(iArr[i3] & 16777215);
        }
    }

    @Override // sun.font.FontStrike
    long getGlyphImagePtr(int i2) {
        return getStrikeForGlyph(i2).getGlyphImagePtr(i2 & 16777215);
    }

    @Override // sun.font.FontStrike
    void getGlyphImageBounds(int i2, Point2D.Float r7, Rectangle rectangle) {
        getStrikeForGlyph(i2).getGlyphImageBounds(i2 & 16777215, r7, rectangle);
    }

    @Override // sun.font.FontStrike
    Point2D.Float getGlyphMetrics(int i2) {
        return getStrikeForGlyph(i2).getGlyphMetrics(i2 & 16777215);
    }

    @Override // sun.font.FontStrike
    Point2D.Float getCharMetrics(char c2) {
        return getGlyphMetrics(this.compFont.getMapper().charToGlyph(c2));
    }

    @Override // sun.font.FontStrike
    float getGlyphAdvance(int i2) {
        return getStrikeForGlyph(i2).getGlyphAdvance(i2 & 16777215);
    }

    @Override // sun.font.FontStrike
    float getCodePointAdvance(int i2) {
        return getGlyphAdvance(this.compFont.getMapper().charToGlyph(i2));
    }

    @Override // sun.font.FontStrike
    Rectangle2D.Float getGlyphOutlineBounds(int i2) {
        return getStrikeForGlyph(i2).getGlyphOutlineBounds(i2 & 16777215);
    }

    @Override // sun.font.FontStrike
    GeneralPath getGlyphOutline(int i2, float f2, float f3) {
        GeneralPath glyphOutline = getStrikeForGlyph(i2).getGlyphOutline(i2 & 16777215, f2, f3);
        if (glyphOutline == null) {
            return new GeneralPath();
        }
        return glyphOutline;
    }

    @Override // sun.font.FontStrike
    GeneralPath getGlyphVectorOutline(int[] iArr, float f2, float f3) {
        GeneralPath generalPath = null;
        int i2 = 0;
        while (i2 < iArr.length) {
            int i3 = i2;
            int i4 = iArr[i2] >>> 24;
            while (i2 < iArr.length && (iArr[i2 + 1] >>> 24) == i4) {
                i2++;
            }
            int i5 = (i2 - i3) + 1;
            int[] iArr2 = new int[i5];
            for (int i6 = 0; i6 < i5; i6++) {
                iArr2[i6] = iArr[i6] & 16777215;
            }
            GeneralPath glyphVectorOutline = getStrikeForSlot(i4).getGlyphVectorOutline(iArr2, f2, f3);
            if (generalPath == null) {
                generalPath = glyphVectorOutline;
            } else if (glyphVectorOutline != null) {
                generalPath.append((Shape) glyphVectorOutline, false);
            }
        }
        if (generalPath == null) {
            return new GeneralPath();
        }
        return generalPath;
    }
}
