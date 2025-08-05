package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontStrikeDesc.class */
public class FontStrikeDesc {
    float[] matrix = new float[4];
    float size;
    int aaMode;
    private int hash;

    public FontStrikeDesc(float fontSize, BaseTransform transform, int aaMode) {
        this.size = fontSize;
        this.aaMode = aaMode;
        this.matrix[0] = (float) transform.getMxx();
        this.matrix[1] = (float) transform.getMxy();
        this.matrix[2] = (float) transform.getMyx();
        this.matrix[3] = (float) transform.getMyy();
    }

    public int hashCode() {
        if (this.hash == 0) {
            this.hash = this.aaMode + Float.floatToIntBits(this.size) + Float.floatToIntBits(this.matrix[0]) + Float.floatToIntBits(this.matrix[1]) + Float.floatToIntBits(this.matrix[2]) + Float.floatToIntBits(this.matrix[3]);
        }
        return this.hash;
    }

    public boolean equals(Object o2) {
        FontStrikeDesc other = (FontStrikeDesc) o2;
        return this.aaMode == other.aaMode && this.matrix[0] == other.matrix[0] && this.matrix[1] == other.matrix[1] && this.matrix[2] == other.matrix[2] && this.matrix[3] == other.matrix[3] && this.size == other.size;
    }
}
