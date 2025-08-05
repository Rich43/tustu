package com.sun.javafx.tk;

import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/FontMetrics.class */
public class FontMetrics {
    private float maxAscent;
    private float ascent;
    private float xheight;
    private int baseline;
    private float descent;
    private float maxDescent;
    private float leading;
    private float lineHeight;
    private Font font;

    public static FontMetrics impl_createFontMetrics(float maxAscent, float ascent, float xheight, float descent, float maxDescent, float leading, Font font) {
        return new FontMetrics(maxAscent, ascent, xheight, descent, maxDescent, leading, font);
    }

    public final float getMaxAscent() {
        return this.maxAscent;
    }

    public final float getAscent() {
        return this.ascent;
    }

    public final float getXheight() {
        return this.xheight;
    }

    public final int getBaseline() {
        return this.baseline;
    }

    public final float getDescent() {
        return this.descent;
    }

    public final float getMaxDescent() {
        return this.maxDescent;
    }

    public final float getLeading() {
        return this.leading;
    }

    public final float getLineHeight() {
        return this.lineHeight;
    }

    public final Font getFont() {
        if (this.font == null) {
            this.font = Font.getDefault();
        }
        return this.font;
    }

    public FontMetrics(float maxAscent, float ascent, float xheight, float descent, float maxDescent, float leading, Font font) {
        this.maxAscent = maxAscent;
        this.ascent = ascent;
        this.xheight = xheight;
        this.descent = descent;
        this.maxDescent = maxDescent;
        this.leading = leading;
        this.font = font;
        this.lineHeight = maxAscent + maxDescent + leading;
    }

    public float computeStringWidth(String string) {
        return Toolkit.getToolkit().getFontLoader().computeStringWidth(string, getFont());
    }

    public String toString() {
        return "FontMetrics: [maxAscent=" + getMaxAscent() + ", ascent=" + getAscent() + ", xheight=" + getXheight() + ", baseline=" + getBaseline() + ", descent=" + getDescent() + ", maxDescent=" + getMaxDescent() + ", leading=" + getLeading() + ", lineHeight=" + getLineHeight() + ", font=" + ((Object) getFont()) + "]";
    }
}
