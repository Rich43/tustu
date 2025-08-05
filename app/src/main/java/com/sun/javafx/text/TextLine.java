package com.sun.javafx.text;

import com.sun.javafx.geom.RectBounds;

/* loaded from: jfxrt.jar:com/sun/javafx/text/TextLine.class */
public class TextLine implements com.sun.javafx.scene.text.TextLine {
    TextRun[] runs;
    RectBounds bounds;
    float lsb;
    float rsb;
    float leading;
    int start;
    int length;

    public TextLine(int start, int length, TextRun[] runs, float width, float ascent, float descent, float leading) {
        this.start = start;
        this.length = length;
        this.bounds = new RectBounds(0.0f, ascent, width, descent + leading);
        this.leading = leading;
        this.runs = runs;
    }

    @Override // com.sun.javafx.scene.text.TextLine
    public RectBounds getBounds() {
        return this.bounds;
    }

    public float getLeading() {
        return this.leading;
    }

    @Override // com.sun.javafx.scene.text.TextLine
    public TextRun[] getRuns() {
        return this.runs;
    }

    @Override // com.sun.javafx.scene.text.TextLine
    public int getStart() {
        return this.start;
    }

    @Override // com.sun.javafx.scene.text.TextLine
    public int getLength() {
        return this.length;
    }

    public void setSideBearings(float lsb, float rsb) {
        this.lsb = lsb;
        this.rsb = rsb;
    }

    @Override // com.sun.javafx.scene.text.TextLine
    public float getLeftSideBearing() {
        return this.lsb;
    }

    @Override // com.sun.javafx.scene.text.TextLine
    public float getRightSideBearing() {
        return this.rsb;
    }

    public void setAlignment(float x2) {
        this.bounds.setMinX(x2);
        this.bounds.setMaxX(x2 + this.bounds.getMaxX());
    }

    public void setWidth(float width) {
        this.bounds.setMaxX(this.bounds.getMinX() + width);
    }
}
