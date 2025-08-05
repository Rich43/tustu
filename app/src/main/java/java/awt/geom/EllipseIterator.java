package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/EllipseIterator.class */
class EllipseIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f12390x;

    /* renamed from: y, reason: collision with root package name */
    double f12391y;

    /* renamed from: w, reason: collision with root package name */
    double f12392w;

    /* renamed from: h, reason: collision with root package name */
    double f12393h;
    AffineTransform affine;
    int index;
    public static final double CtrlVal = 0.5522847498307933d;
    private static final double pcv = 0.7761423749153966d;
    private static final double ncv = 0.22385762508460333d;
    private static double[][] ctrlpts = {new double[]{1.0d, pcv, pcv, 1.0d, 0.5d, 1.0d}, new double[]{ncv, 1.0d, 0.0d, pcv, 0.0d, 0.5d}, new double[]{0.0d, ncv, ncv, 0.0d, 0.5d, 0.0d}, new double[]{pcv, 0.0d, 1.0d, ncv, 1.0d, 0.5d}};

    EllipseIterator(Ellipse2D ellipse2D, AffineTransform affineTransform) {
        this.f12390x = ellipse2D.getX();
        this.f12391y = ellipse2D.getY();
        this.f12392w = ellipse2D.getWidth();
        this.f12393h = ellipse2D.getHeight();
        this.affine = affineTransform;
        if (this.f12392w < 0.0d || this.f12393h < 0.0d) {
            this.index = 6;
        }
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.index > 5;
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        this.index++;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        if (isDone()) {
            throw new NoSuchElementException("ellipse iterator out of bounds");
        }
        if (this.index == 5) {
            return 4;
        }
        if (this.index == 0) {
            double[] dArr = ctrlpts[3];
            fArr[0] = (float) (this.f12390x + (dArr[4] * this.f12392w));
            fArr[1] = (float) (this.f12391y + (dArr[5] * this.f12393h));
            if (this.affine != null) {
                this.affine.transform(fArr, 0, fArr, 0, 1);
                return 0;
            }
            return 0;
        }
        double[] dArr2 = ctrlpts[this.index - 1];
        fArr[0] = (float) (this.f12390x + (dArr2[0] * this.f12392w));
        fArr[1] = (float) (this.f12391y + (dArr2[1] * this.f12393h));
        fArr[2] = (float) (this.f12390x + (dArr2[2] * this.f12392w));
        fArr[3] = (float) (this.f12391y + (dArr2[3] * this.f12393h));
        fArr[4] = (float) (this.f12390x + (dArr2[4] * this.f12392w));
        fArr[5] = (float) (this.f12391y + (dArr2[5] * this.f12393h));
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, 3);
            return 3;
        }
        return 3;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        if (isDone()) {
            throw new NoSuchElementException("ellipse iterator out of bounds");
        }
        if (this.index == 5) {
            return 4;
        }
        if (this.index == 0) {
            double[] dArr2 = ctrlpts[3];
            dArr[0] = this.f12390x + (dArr2[4] * this.f12392w);
            dArr[1] = this.f12391y + (dArr2[5] * this.f12393h);
            if (this.affine != null) {
                this.affine.transform(dArr, 0, dArr, 0, 1);
                return 0;
            }
            return 0;
        }
        double[] dArr3 = ctrlpts[this.index - 1];
        dArr[0] = this.f12390x + (dArr3[0] * this.f12392w);
        dArr[1] = this.f12391y + (dArr3[1] * this.f12393h);
        dArr[2] = this.f12390x + (dArr3[2] * this.f12392w);
        dArr[3] = this.f12391y + (dArr3[3] * this.f12393h);
        dArr[4] = this.f12390x + (dArr3[4] * this.f12392w);
        dArr[5] = this.f12391y + (dArr3[5] * this.f12393h);
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, 3);
            return 3;
        }
        return 3;
    }
}
