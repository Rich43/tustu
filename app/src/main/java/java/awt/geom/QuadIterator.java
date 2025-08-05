package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/QuadIterator.class */
class QuadIterator implements PathIterator {
    QuadCurve2D quad;
    AffineTransform affine;
    int index;

    QuadIterator(QuadCurve2D quadCurve2D, AffineTransform affineTransform) {
        this.quad = quadCurve2D;
        this.affine = affineTransform;
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.index > 1;
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        this.index++;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        int i2;
        if (isDone()) {
            throw new NoSuchElementException("quad iterator iterator out of bounds");
        }
        if (this.index == 0) {
            fArr[0] = (float) this.quad.getX1();
            fArr[1] = (float) this.quad.getY1();
            i2 = 0;
        } else {
            fArr[0] = (float) this.quad.getCtrlX();
            fArr[1] = (float) this.quad.getCtrlY();
            fArr[2] = (float) this.quad.getX2();
            fArr[3] = (float) this.quad.getY2();
            i2 = 2;
        }
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, this.index == 0 ? 1 : 2);
        }
        return i2;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        int i2;
        if (isDone()) {
            throw new NoSuchElementException("quad iterator iterator out of bounds");
        }
        if (this.index == 0) {
            dArr[0] = this.quad.getX1();
            dArr[1] = this.quad.getY1();
            i2 = 0;
        } else {
            dArr[0] = this.quad.getCtrlX();
            dArr[1] = this.quad.getCtrlY();
            dArr[2] = this.quad.getX2();
            dArr[3] = this.quad.getY2();
            i2 = 2;
        }
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, this.index == 0 ? 1 : 2);
        }
        return i2;
    }
}
