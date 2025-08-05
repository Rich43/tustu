package sun.font;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/* loaded from: rt.jar:sun/font/StrikeMetrics.class */
public final class StrikeMetrics {
    public float ascentX;
    public float ascentY;
    public float descentX;
    public float descentY;
    public float baselineX;
    public float baselineY;
    public float leadingX;
    public float leadingY;
    public float maxAdvanceX;
    public float maxAdvanceY;

    StrikeMetrics() {
        this.ascentY = 2.1474836E9f;
        this.ascentX = 2.1474836E9f;
        this.leadingY = -2.1474836E9f;
        this.leadingX = -2.1474836E9f;
        this.descentY = -2.1474836E9f;
        this.descentX = -2.1474836E9f;
        this.maxAdvanceY = -2.1474836E9f;
        this.maxAdvanceX = -2.1474836E9f;
        this.baselineX = -2.1474836E9f;
        this.baselineX = -2.1474836E9f;
    }

    StrikeMetrics(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        this.ascentX = f2;
        this.ascentY = f3;
        this.descentX = f4;
        this.descentY = f5;
        this.baselineX = f6;
        this.baselineY = f7;
        this.leadingX = f8;
        this.leadingY = f9;
        this.maxAdvanceX = f10;
        this.maxAdvanceY = f11;
    }

    public float getAscent() {
        return -this.ascentY;
    }

    public float getDescent() {
        return this.descentY;
    }

    public float getLeading() {
        return this.leadingY;
    }

    public float getMaxAdvance() {
        return this.maxAdvanceX;
    }

    void merge(StrikeMetrics strikeMetrics) {
        if (strikeMetrics == null) {
            return;
        }
        if (strikeMetrics.ascentX < this.ascentX) {
            this.ascentX = strikeMetrics.ascentX;
        }
        if (strikeMetrics.ascentY < this.ascentY) {
            this.ascentY = strikeMetrics.ascentY;
        }
        if (strikeMetrics.descentX > this.descentX) {
            this.descentX = strikeMetrics.descentX;
        }
        if (strikeMetrics.descentY > this.descentY) {
            this.descentY = strikeMetrics.descentY;
        }
        if (strikeMetrics.baselineX > this.baselineX) {
            this.baselineX = strikeMetrics.baselineX;
        }
        if (strikeMetrics.baselineY > this.baselineY) {
            this.baselineY = strikeMetrics.baselineY;
        }
        if (strikeMetrics.leadingX > this.leadingX) {
            this.leadingX = strikeMetrics.leadingX;
        }
        if (strikeMetrics.leadingY > this.leadingY) {
            this.leadingY = strikeMetrics.leadingY;
        }
        if (strikeMetrics.maxAdvanceX > this.maxAdvanceX) {
            this.maxAdvanceX = strikeMetrics.maxAdvanceX;
        }
        if (strikeMetrics.maxAdvanceY > this.maxAdvanceY) {
            this.maxAdvanceY = strikeMetrics.maxAdvanceY;
        }
    }

    void convertToUserSpace(AffineTransform affineTransform) {
        Point2D.Float r0 = new Point2D.Float();
        r0.f12396x = this.ascentX;
        r0.f12397y = this.ascentY;
        affineTransform.deltaTransform(r0, r0);
        this.ascentX = r0.f12396x;
        this.ascentY = r0.f12397y;
        r0.f12396x = this.descentX;
        r0.f12397y = this.descentY;
        affineTransform.deltaTransform(r0, r0);
        this.descentX = r0.f12396x;
        this.descentY = r0.f12397y;
        r0.f12396x = this.baselineX;
        r0.f12397y = this.baselineY;
        affineTransform.deltaTransform(r0, r0);
        this.baselineX = r0.f12396x;
        this.baselineY = r0.f12397y;
        r0.f12396x = this.leadingX;
        r0.f12397y = this.leadingY;
        affineTransform.deltaTransform(r0, r0);
        this.leadingX = r0.f12396x;
        this.leadingY = r0.f12397y;
        r0.f12396x = this.maxAdvanceX;
        r0.f12397y = this.maxAdvanceY;
        affineTransform.deltaTransform(r0, r0);
        this.maxAdvanceX = r0.f12396x;
        this.maxAdvanceY = r0.f12397y;
    }

    public String toString() {
        return "ascent:x=" + this.ascentX + " y=" + this.ascentY + " descent:x=" + this.descentX + " y=" + this.descentY + " baseline:x=" + this.baselineX + " y=" + this.baselineY + " leading:x=" + this.leadingX + " y=" + this.leadingY + " maxAdvance:x=" + this.maxAdvanceX + " y=" + this.maxAdvanceY;
    }
}
