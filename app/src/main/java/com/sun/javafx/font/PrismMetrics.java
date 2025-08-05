package com.sun.javafx.font;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismMetrics.class */
public class PrismMetrics implements Metrics {
    PrismFontFile fontResource;
    float ascent;
    float descent;
    float linegap;
    private float[] styleMetrics;
    float size;
    static final int XHEIGHT = 0;
    static final int CAPHEIGHT = 1;
    static final int TYPO_ASCENT = 2;
    static final int TYPO_DESCENT = 3;
    static final int TYPO_LINEGAP = 4;
    static final int STRIKETHROUGH_THICKNESS = 5;
    static final int STRIKETHROUGH_OFFSET = 6;
    static final int UNDERLINE_THICKESS = 7;
    static final int UNDERLINE_OFFSET = 8;
    static final int METRICS_TOTAL = 9;

    PrismMetrics(float ascent, float descent, float linegap, PrismFontFile fontResource, float size) {
        this.ascent = ascent;
        this.descent = descent;
        this.linegap = linegap;
        this.fontResource = fontResource;
        this.size = size;
    }

    @Override // com.sun.javafx.font.Metrics
    public float getAscent() {
        return this.ascent;
    }

    @Override // com.sun.javafx.font.Metrics
    public float getDescent() {
        return this.descent;
    }

    @Override // com.sun.javafx.font.Metrics
    public float getLineGap() {
        return this.linegap;
    }

    @Override // com.sun.javafx.font.Metrics
    public float getLineHeight() {
        return (-this.ascent) + this.descent + this.linegap;
    }

    private void checkStyleMetrics() {
        if (this.styleMetrics == null) {
            this.styleMetrics = this.fontResource.getStyleMetrics(this.size);
        }
    }

    @Override // com.sun.javafx.font.Metrics
    public float getTypoAscent() {
        checkStyleMetrics();
        return this.styleMetrics[2];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getTypoDescent() {
        checkStyleMetrics();
        return this.styleMetrics[3];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getTypoLineGap() {
        checkStyleMetrics();
        return this.styleMetrics[4];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getCapHeight() {
        checkStyleMetrics();
        return this.styleMetrics[1];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getXHeight() {
        checkStyleMetrics();
        return this.styleMetrics[0];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getStrikethroughOffset() {
        checkStyleMetrics();
        return this.styleMetrics[6];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getStrikethroughThickness() {
        checkStyleMetrics();
        return this.styleMetrics[5];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getUnderLineOffset() {
        checkStyleMetrics();
        return this.styleMetrics[8];
    }

    @Override // com.sun.javafx.font.Metrics
    public float getUnderLineThickness() {
        checkStyleMetrics();
        return this.styleMetrics[7];
    }

    public String toString() {
        return "ascent = " + getAscent() + " descent = " + getDescent() + " linegap = " + getLineGap() + " lineheight = " + getLineHeight();
    }
}
