package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/RectIterator.class */
class RectIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f12398x;

    /* renamed from: y, reason: collision with root package name */
    double f12399y;

    /* renamed from: w, reason: collision with root package name */
    double f12400w;

    /* renamed from: h, reason: collision with root package name */
    double f12401h;
    AffineTransform affine;
    int index;

    RectIterator(Rectangle2D rectangle2D, AffineTransform affineTransform) {
        this.f12398x = rectangle2D.getX();
        this.f12399y = rectangle2D.getY();
        this.f12400w = rectangle2D.getWidth();
        this.f12401h = rectangle2D.getHeight();
        this.affine = affineTransform;
        if (this.f12400w < 0.0d || this.f12401h < 0.0d) {
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
            throw new NoSuchElementException("rect iterator out of bounds");
        }
        if (this.index == 5) {
            return 4;
        }
        fArr[0] = (float) this.f12398x;
        fArr[1] = (float) this.f12399y;
        if (this.index == 1 || this.index == 2) {
            fArr[0] = fArr[0] + ((float) this.f12400w);
        }
        if (this.index == 2 || this.index == 3) {
            fArr[1] = fArr[1] + ((float) this.f12401h);
        }
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, 1);
        }
        return this.index == 0 ? 0 : 1;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        if (isDone()) {
            throw new NoSuchElementException("rect iterator out of bounds");
        }
        if (this.index == 5) {
            return 4;
        }
        dArr[0] = this.f12398x;
        dArr[1] = this.f12399y;
        if (this.index == 1 || this.index == 2) {
            dArr[0] = dArr[0] + this.f12400w;
        }
        if (this.index == 2 || this.index == 3) {
            dArr[1] = dArr[1] + this.f12401h;
        }
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, 1);
        }
        return this.index == 0 ? 0 : 1;
    }
}
