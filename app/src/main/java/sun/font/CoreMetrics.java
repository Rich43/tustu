package sun.font;

import java.awt.font.LineMetrics;

/* loaded from: rt.jar:sun/font/CoreMetrics.class */
public final class CoreMetrics {
    public final float ascent;
    public final float descent;
    public final float leading;
    public final float height;
    public final int baselineIndex;
    public final float[] baselineOffsets;
    public final float strikethroughOffset;
    public final float strikethroughThickness;
    public final float underlineOffset;
    public final float underlineThickness;
    public final float ssOffset;
    public final float italicAngle;

    public CoreMetrics(float f2, float f3, float f4, float f5, int i2, float[] fArr, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.ascent = f2;
        this.descent = f3;
        this.leading = f4;
        this.height = f5;
        this.baselineIndex = i2;
        this.baselineOffsets = fArr;
        this.strikethroughOffset = f6;
        this.strikethroughThickness = f7;
        this.underlineOffset = f8;
        this.underlineThickness = f9;
        this.ssOffset = f10;
        this.italicAngle = f11;
    }

    public static CoreMetrics get(LineMetrics lineMetrics) {
        return ((FontLineMetrics) lineMetrics).cm;
    }

    public final int hashCode() {
        return Float.floatToIntBits(this.ascent + this.ssOffset);
    }

    public final boolean equals(Object obj) {
        try {
            return equals((CoreMetrics) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public final boolean equals(CoreMetrics coreMetrics) {
        if (coreMetrics != null) {
            if (this == coreMetrics) {
                return true;
            }
            return this.ascent == coreMetrics.ascent && this.descent == coreMetrics.descent && this.leading == coreMetrics.leading && this.baselineIndex == coreMetrics.baselineIndex && this.baselineOffsets[0] == coreMetrics.baselineOffsets[0] && this.baselineOffsets[1] == coreMetrics.baselineOffsets[1] && this.baselineOffsets[2] == coreMetrics.baselineOffsets[2] && this.strikethroughOffset == coreMetrics.strikethroughOffset && this.strikethroughThickness == coreMetrics.strikethroughThickness && this.underlineOffset == coreMetrics.underlineOffset && this.underlineThickness == coreMetrics.underlineThickness && this.ssOffset == coreMetrics.ssOffset && this.italicAngle == coreMetrics.italicAngle;
        }
        return false;
    }

    public final float effectiveBaselineOffset(float[] fArr) {
        switch (this.baselineIndex) {
            case -2:
                return fArr[3] - this.descent;
            case -1:
                return fArr[4] + this.ascent;
            default:
                return fArr[this.baselineIndex];
        }
    }
}
