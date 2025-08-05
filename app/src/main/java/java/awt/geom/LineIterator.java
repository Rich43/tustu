package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/LineIterator.class */
class LineIterator implements PathIterator {
    Line2D line;
    AffineTransform affine;
    int index;

    LineIterator(Line2D line2D, AffineTransform affineTransform) {
        this.line = line2D;
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
            throw new NoSuchElementException("line iterator out of bounds");
        }
        if (this.index == 0) {
            fArr[0] = (float) this.line.getX1();
            fArr[1] = (float) this.line.getY1();
            i2 = 0;
        } else {
            fArr[0] = (float) this.line.getX2();
            fArr[1] = (float) this.line.getY2();
            i2 = 1;
        }
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, 1);
        }
        return i2;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        int i2;
        if (isDone()) {
            throw new NoSuchElementException("line iterator out of bounds");
        }
        if (this.index == 0) {
            dArr[0] = this.line.getX1();
            dArr[1] = this.line.getY1();
            i2 = 0;
        } else {
            dArr[0] = this.line.getX2();
            dArr[1] = this.line.getY2();
            i2 = 1;
        }
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, 1);
        }
        return i2;
    }
}
