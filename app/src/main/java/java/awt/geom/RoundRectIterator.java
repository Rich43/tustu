package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/RoundRectIterator.class */
class RoundRectIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f12406x;

    /* renamed from: y, reason: collision with root package name */
    double f12407y;

    /* renamed from: w, reason: collision with root package name */
    double f12408w;

    /* renamed from: h, reason: collision with root package name */
    double f12409h;

    /* renamed from: aw, reason: collision with root package name */
    double f12410aw;

    /* renamed from: ah, reason: collision with root package name */
    double f12411ah;
    AffineTransform affine;
    int index;
    private static final double angle = 0.7853981633974483d;

    /* renamed from: a, reason: collision with root package name */
    private static final double f12412a = 1.0d - Math.cos(angle);

    /* renamed from: b, reason: collision with root package name */
    private static final double f12413b = Math.tan(angle);

    /* renamed from: c, reason: collision with root package name */
    private static final double f12414c = (Math.sqrt(1.0d + (f12413b * f12413b)) - 1.0d) + f12412a;
    private static final double cv = ((1.3333333333333333d * f12412a) * f12413b) / f12414c;
    private static final double acv = (1.0d - cv) / 2.0d;
    private static double[][] ctrlpts = {new double[]{0.0d, 0.0d, 0.0d, 0.5d}, new double[]{0.0d, 0.0d, 1.0d, -0.5d}, new double[]{0.0d, 0.0d, 1.0d, -acv, 0.0d, acv, 1.0d, 0.0d, 0.0d, 0.5d, 1.0d, 0.0d}, new double[]{1.0d, -0.5d, 1.0d, 0.0d}, new double[]{1.0d, -acv, 1.0d, 0.0d, 1.0d, 0.0d, 1.0d, -acv, 1.0d, 0.0d, 1.0d, -0.5d}, new double[]{1.0d, 0.0d, 0.0d, 0.5d}, new double[]{1.0d, 0.0d, 0.0d, acv, 1.0d, -acv, 0.0d, 0.0d, 1.0d, -0.5d, 0.0d, 0.0d}, new double[]{0.0d, 0.5d, 0.0d, 0.0d}, new double[]{0.0d, acv, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, acv, 0.0d, 0.0d, 0.0d, 0.5d}, new double[0]};
    private static int[] types = {0, 1, 3, 1, 3, 1, 3, 1, 3, 4};

    RoundRectIterator(RoundRectangle2D roundRectangle2D, AffineTransform affineTransform) {
        this.f12406x = roundRectangle2D.getX();
        this.f12407y = roundRectangle2D.getY();
        this.f12408w = roundRectangle2D.getWidth();
        this.f12409h = roundRectangle2D.getHeight();
        this.f12410aw = Math.min(this.f12408w, Math.abs(roundRectangle2D.getArcWidth()));
        this.f12411ah = Math.min(this.f12409h, Math.abs(roundRectangle2D.getArcHeight()));
        this.affine = affineTransform;
        if (this.f12410aw < 0.0d || this.f12411ah < 0.0d) {
            this.index = ctrlpts.length;
        }
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.index >= ctrlpts.length;
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        this.index++;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        if (isDone()) {
            throw new NoSuchElementException("roundrect iterator out of bounds");
        }
        double[] dArr = ctrlpts[this.index];
        int i2 = 0;
        for (int i3 = 0; i3 < dArr.length; i3 += 4) {
            int i4 = i2;
            int i5 = i2 + 1;
            fArr[i4] = (float) (this.f12406x + (dArr[i3 + 0] * this.f12408w) + (dArr[i3 + 1] * this.f12410aw));
            i2 = i5 + 1;
            fArr[i5] = (float) (this.f12407y + (dArr[i3 + 2] * this.f12409h) + (dArr[i3 + 3] * this.f12411ah));
        }
        if (this.affine != null) {
            this.affine.transform(fArr, 0, fArr, 0, i2 / 2);
        }
        return types[this.index];
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        if (isDone()) {
            throw new NoSuchElementException("roundrect iterator out of bounds");
        }
        double[] dArr2 = ctrlpts[this.index];
        int i2 = 0;
        for (int i3 = 0; i3 < dArr2.length; i3 += 4) {
            int i4 = i2;
            int i5 = i2 + 1;
            dArr[i4] = this.f12406x + (dArr2[i3 + 0] * this.f12408w) + (dArr2[i3 + 1] * this.f12410aw);
            i2 = i5 + 1;
            dArr[i5] = this.f12407y + (dArr2[i3 + 2] * this.f12409h) + (dArr2[i3 + 3] * this.f12411ah);
        }
        if (this.affine != null) {
            this.affine.transform(dArr, 0, dArr, 0, i2 / 2);
        }
        return types[this.index];
    }
}
