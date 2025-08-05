package org.icepdf.core.util.content;

import java.awt.geom.Point2D;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/content/TextMetrics.class */
public class TextMetrics {
    private float shift = 0.0f;
    private float previousAdvance = 0.0f;
    private Point2D.Float advance = new Point2D.Float(0.0f, 0.0f);
    private boolean isYstart = true;
    private float yBTStart = 0.0f;

    public float getShift() {
        return this.shift;
    }

    public void setShift(float shift) {
        this.shift = shift;
    }

    public float getPreviousAdvance() {
        return this.previousAdvance;
    }

    public void setPreviousAdvance(float previousAdvance) {
        this.previousAdvance = previousAdvance;
    }

    public Point2D.Float getAdvance() {
        return this.advance;
    }

    public void setAdvance(Point2D.Float advance) {
        this.advance = advance;
    }

    public boolean isYstart() {
        return this.isYstart;
    }

    public void setYstart(boolean ystart) {
        this.isYstart = ystart;
    }

    public float getyBTStart() {
        return this.yBTStart;
    }

    public void setyBTStart(float yBTStart) {
        this.yBTStart = yBTStart;
    }
}
