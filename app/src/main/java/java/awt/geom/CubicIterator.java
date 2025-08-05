package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/CubicIterator.class */
class CubicIterator implements PathIterator {
    CubicCurve2D cubic;
    AffineTransform affine;
    int index;

    CubicIterator(CubicCurve2D cubicCurve2D, AffineTransform affineTransform) {
        this.cubic = cubicCurve2D;
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
            throw new NoSuchElementException("cubic iterator iterator out of bounds");
        }
        if (this.index == 0) {
            fArr[0] = (float) this.cubic.getX1();
            fArr[1] = (float) this.cubic.getY1();
            i2 = 0;
        } else {
            fArr[0] = (float) this.cubic.getCtrlX1();
            fArr[1] = (float) this.cubic.getCtrlY1();
            fArr[2] = (float) this.cubic.getCtrlX2();
            fArr[3] = (float) this.cubic.getCtrlY2();
            fArr[4] = (float) this.cubic.getX2();
            fArr[5] = (float) this.cubic.getY2();
            i2 = 3;
        }
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, this.index == 0 ? 1 : 3);
        }
        return i2;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        int i2;
        if (isDone()) {
            throw new NoSuchElementException("cubic iterator iterator out of bounds");
        }
        if (this.index == 0) {
            dArr[0] = this.cubic.getX1();
            dArr[1] = this.cubic.getY1();
            i2 = 0;
        } else {
            dArr[0] = this.cubic.getCtrlX1();
            dArr[1] = this.cubic.getCtrlY1();
            dArr[2] = this.cubic.getCtrlX2();
            dArr[3] = this.cubic.getCtrlY2();
            dArr[4] = this.cubic.getX2();
            dArr[5] = this.cubic.getY2();
            i2 = 3;
        }
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, this.index == 0 ? 1 : 3);
        }
        return i2;
    }
}
