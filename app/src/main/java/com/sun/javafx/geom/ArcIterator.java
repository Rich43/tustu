package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/ArcIterator.class */
class ArcIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f11895x;

    /* renamed from: y, reason: collision with root package name */
    double f11896y;

    /* renamed from: w, reason: collision with root package name */
    double f11897w;

    /* renamed from: h, reason: collision with root package name */
    double f11898h;
    double angStRad;
    double increment;
    double cv;
    BaseTransform transform;
    int index;
    int arcSegs;
    int lineSegs;

    ArcIterator(Arc2D a2, BaseTransform at2) {
        this.f11897w = a2.width / 2.0f;
        this.f11898h = a2.height / 2.0f;
        this.f11895x = a2.f11893x + this.f11897w;
        this.f11896y = a2.f11894y + this.f11898h;
        this.angStRad = -Math.toRadians(a2.start);
        this.transform = at2;
        double ext = -a2.extent;
        if (ext >= 360.0d || ext <= -360.0d) {
            this.arcSegs = 4;
            this.increment = 1.5707963267948966d;
            this.cv = 0.5522847498307933d;
            if (ext < 0.0d) {
                this.increment = -this.increment;
                this.cv = -this.cv;
            }
        } else {
            this.arcSegs = (int) Math.ceil(Math.abs(ext) / 90.0d);
            this.increment = Math.toRadians(ext / this.arcSegs);
            this.cv = btan(this.increment);
            if (this.cv == 0.0d) {
                this.arcSegs = 0;
            }
        }
        switch (a2.getArcType()) {
            case 0:
                this.lineSegs = 0;
                break;
            case 1:
                this.lineSegs = 1;
                break;
            case 2:
                this.lineSegs = 2;
                break;
        }
        if (this.f11897w < 0.0d || this.f11898h < 0.0d) {
            this.lineSegs = -1;
            this.arcSegs = -1;
        }
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public boolean isDone() {
        return this.index > this.arcSegs + this.lineSegs;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public void next() {
        this.index++;
    }

    private static double btan(double increment) {
        double increment2 = increment / 2.0d;
        return (1.3333333333333333d * Math.sin(increment2)) / (1.0d + Math.cos(increment2));
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("arc iterator out of bounds");
        }
        double angle = this.angStRad;
        if (this.index == 0) {
            coords[0] = (float) (this.f11895x + (Math.cos(angle) * this.f11897w));
            coords[1] = (float) (this.f11896y + (Math.sin(angle) * this.f11898h));
            if (this.transform != null) {
                this.transform.transform(coords, 0, coords, 0, 1);
                return 0;
            }
            return 0;
        }
        if (this.index > this.arcSegs) {
            if (this.index == this.arcSegs + this.lineSegs) {
                return 4;
            }
            coords[0] = (float) this.f11895x;
            coords[1] = (float) this.f11896y;
            if (this.transform != null) {
                this.transform.transform(coords, 0, coords, 0, 1);
                return 1;
            }
            return 1;
        }
        double angle2 = angle + (this.increment * (this.index - 1));
        double relx = Math.cos(angle2);
        double rely = Math.sin(angle2);
        coords[0] = (float) (this.f11895x + ((relx - (this.cv * rely)) * this.f11897w));
        coords[1] = (float) (this.f11896y + ((rely + (this.cv * relx)) * this.f11898h));
        double angle3 = angle2 + this.increment;
        double relx2 = Math.cos(angle3);
        double rely2 = Math.sin(angle3);
        coords[2] = (float) (this.f11895x + ((relx2 + (this.cv * rely2)) * this.f11897w));
        coords[3] = (float) (this.f11896y + ((rely2 - (this.cv * relx2)) * this.f11898h));
        coords[4] = (float) (this.f11895x + (relx2 * this.f11897w));
        coords[5] = (float) (this.f11896y + (rely2 * this.f11898h));
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, 3);
            return 3;
        }
        return 3;
    }
}
