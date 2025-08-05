package sun.font;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

/* loaded from: rt.jar:sun/font/FontLineMetrics.class */
public final class FontLineMetrics extends LineMetrics implements Cloneable {
    public int numchars;
    public final CoreMetrics cm;
    public final FontRenderContext frc;

    public FontLineMetrics(int i2, CoreMetrics coreMetrics, FontRenderContext fontRenderContext) {
        this.numchars = i2;
        this.cm = coreMetrics;
        this.frc = fontRenderContext;
    }

    @Override // java.awt.font.LineMetrics
    public final int getNumChars() {
        return this.numchars;
    }

    @Override // java.awt.font.LineMetrics
    public final float getAscent() {
        return this.cm.ascent;
    }

    @Override // java.awt.font.LineMetrics
    public final float getDescent() {
        return this.cm.descent;
    }

    @Override // java.awt.font.LineMetrics
    public final float getLeading() {
        return this.cm.leading;
    }

    @Override // java.awt.font.LineMetrics
    public final float getHeight() {
        return this.cm.height;
    }

    @Override // java.awt.font.LineMetrics
    public final int getBaselineIndex() {
        return this.cm.baselineIndex;
    }

    @Override // java.awt.font.LineMetrics
    public final float[] getBaselineOffsets() {
        return (float[]) this.cm.baselineOffsets.clone();
    }

    @Override // java.awt.font.LineMetrics
    public final float getStrikethroughOffset() {
        return this.cm.strikethroughOffset;
    }

    @Override // java.awt.font.LineMetrics
    public final float getStrikethroughThickness() {
        return this.cm.strikethroughThickness;
    }

    @Override // java.awt.font.LineMetrics
    public final float getUnderlineOffset() {
        return this.cm.underlineOffset;
    }

    @Override // java.awt.font.LineMetrics
    public final float getUnderlineThickness() {
        return this.cm.underlineThickness;
    }

    public final int hashCode() {
        return this.cm.hashCode();
    }

    public final boolean equals(Object obj) {
        try {
            return this.cm.equals(((FontLineMetrics) obj).cm);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
