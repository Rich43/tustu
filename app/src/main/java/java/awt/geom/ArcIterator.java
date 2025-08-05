package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/ArcIterator.class */
class ArcIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f12382x;

    /* renamed from: y, reason: collision with root package name */
    double f12383y;

    /* renamed from: w, reason: collision with root package name */
    double f12384w;

    /* renamed from: h, reason: collision with root package name */
    double f12385h;
    double angStRad;
    double increment;
    double cv;
    AffineTransform affine;
    int index;
    int arcSegs;
    int lineSegs;

    ArcIterator(Arc2D arc2D, AffineTransform affineTransform) {
        this.f12384w = arc2D.getWidth() / 2.0d;
        this.f12385h = arc2D.getHeight() / 2.0d;
        this.f12382x = arc2D.getX() + this.f12384w;
        this.f12383y = arc2D.getY() + this.f12385h;
        this.angStRad = -Math.toRadians(arc2D.getAngleStart());
        this.affine = affineTransform;
        double d2 = -arc2D.getAngleExtent();
        if (d2 >= 360.0d || d2 <= -360.0d) {
            this.arcSegs = 4;
            this.increment = 1.5707963267948966d;
            this.cv = 0.5522847498307933d;
            if (d2 < 0.0d) {
                this.increment = -this.increment;
                this.cv = -this.cv;
            }
        } else {
            this.arcSegs = (int) Math.ceil(Math.abs(d2) / 90.0d);
            this.increment = Math.toRadians(d2 / this.arcSegs);
            this.cv = btan(this.increment);
            if (this.cv == 0.0d) {
                this.arcSegs = 0;
            }
        }
        switch (arc2D.getArcType()) {
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
        if (this.f12384w < 0.0d || this.f12385h < 0.0d) {
            this.lineSegs = -1;
            this.arcSegs = -1;
        }
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.index > this.arcSegs + this.lineSegs;
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        this.index++;
    }

    private static double btan(double d2) {
        double d3 = d2 / 2.0d;
        return (1.3333333333333333d * Math.sin(d3)) / (1.0d + Math.cos(d3));
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        if (isDone()) {
            throw new NoSuchElementException("arc iterator out of bounds");
        }
        double d2 = this.angStRad;
        if (this.index == 0) {
            fArr[0] = (float) (this.f12382x + (Math.cos(d2) * this.f12384w));
            fArr[1] = (float) (this.f12383y + (Math.sin(d2) * this.f12385h));
            if (this.affine != null) {
                this.affine.transform(fArr, 0, fArr, 0, 1);
                return 0;
            }
            return 0;
        }
        if (this.index > this.arcSegs) {
            if (this.index == this.arcSegs + this.lineSegs) {
                return 4;
            }
            fArr[0] = (float) this.f12382x;
            fArr[1] = (float) this.f12383y;
            if (this.affine != null) {
                this.affine.transform(fArr, 0, fArr, 0, 1);
                return 1;
            }
            return 1;
        }
        double d3 = d2 + (this.increment * (this.index - 1));
        double dCos = Math.cos(d3);
        double dSin = Math.sin(d3);
        fArr[0] = (float) (this.f12382x + ((dCos - (this.cv * dSin)) * this.f12384w));
        fArr[1] = (float) (this.f12383y + ((dSin + (this.cv * dCos)) * this.f12385h));
        double d4 = d3 + this.increment;
        double dCos2 = Math.cos(d4);
        double dSin2 = Math.sin(d4);
        fArr[2] = (float) (this.f12382x + ((dCos2 + (this.cv * dSin2)) * this.f12384w));
        fArr[3] = (float) (this.f12383y + ((dSin2 - (this.cv * dCos2)) * this.f12385h));
        fArr[4] = (float) (this.f12382x + (dCos2 * this.f12384w));
        fArr[5] = (float) (this.f12383y + (dSin2 * this.f12385h));
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, 3);
            return 3;
        }
        return 3;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        if (isDone()) {
            throw new NoSuchElementException("arc iterator out of bounds");
        }
        double d2 = this.angStRad;
        if (this.index == 0) {
            dArr[0] = this.f12382x + (Math.cos(d2) * this.f12384w);
            dArr[1] = this.f12383y + (Math.sin(d2) * this.f12385h);
            if (this.affine != null) {
                this.affine.transform(dArr, 0, dArr, 0, 1);
                return 0;
            }
            return 0;
        }
        if (this.index > this.arcSegs) {
            if (this.index == this.arcSegs + this.lineSegs) {
                return 4;
            }
            dArr[0] = this.f12382x;
            dArr[1] = this.f12383y;
            if (this.affine != null) {
                this.affine.transform(dArr, 0, dArr, 0, 1);
                return 1;
            }
            return 1;
        }
        double d3 = d2 + (this.increment * (this.index - 1));
        double dCos = Math.cos(d3);
        double dSin = Math.sin(d3);
        dArr[0] = this.f12382x + ((dCos - (this.cv * dSin)) * this.f12384w);
        dArr[1] = this.f12383y + ((dSin + (this.cv * dCos)) * this.f12385h);
        double d4 = d3 + this.increment;
        double dCos2 = Math.cos(d4);
        double dSin2 = Math.sin(d4);
        dArr[2] = this.f12382x + ((dCos2 + (this.cv * dSin2)) * this.f12384w);
        dArr[3] = this.f12383y + ((dSin2 - (this.cv * dCos2)) * this.f12385h);
        dArr[4] = this.f12382x + (dCos2 * this.f12384w);
        dArr[5] = this.f12383y + (dSin2 * this.f12385h);
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, 3);
            return 3;
        }
        return 3;
    }
}
