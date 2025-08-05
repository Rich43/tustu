package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/RoundRectIterator.class */
class RoundRectIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f11915x;

    /* renamed from: y, reason: collision with root package name */
    double f11916y;

    /* renamed from: w, reason: collision with root package name */
    double f11917w;

    /* renamed from: h, reason: collision with root package name */
    double f11918h;

    /* renamed from: aw, reason: collision with root package name */
    double f11919aw;

    /* renamed from: ah, reason: collision with root package name */
    double f11920ah;
    BaseTransform transform;
    int index;
    private static final double angle = 0.7853981633974483d;

    /* renamed from: a, reason: collision with root package name */
    private static final double f11921a = 1.0d - Math.cos(angle);

    /* renamed from: b, reason: collision with root package name */
    private static final double f11922b = Math.tan(angle);

    /* renamed from: c, reason: collision with root package name */
    private static final double f11923c = (Math.sqrt(1.0d + (f11922b * f11922b)) - 1.0d) + f11921a;
    private static final double cv = ((1.3333333333333333d * f11921a) * f11922b) / f11923c;
    private static final double acv = (1.0d - cv) / 2.0d;
    private static final double[][] ctrlpts = {new double[]{0.0d, 0.0d, 0.0d, 0.5d}, new double[]{0.0d, 0.0d, 1.0d, -0.5d}, new double[]{0.0d, 0.0d, 1.0d, -acv, 0.0d, acv, 1.0d, 0.0d, 0.0d, 0.5d, 1.0d, 0.0d}, new double[]{1.0d, -0.5d, 1.0d, 0.0d}, new double[]{1.0d, -acv, 1.0d, 0.0d, 1.0d, 0.0d, 1.0d, -acv, 1.0d, 0.0d, 1.0d, -0.5d}, new double[]{1.0d, 0.0d, 0.0d, 0.5d}, new double[]{1.0d, 0.0d, 0.0d, acv, 1.0d, -acv, 0.0d, 0.0d, 1.0d, -0.5d, 0.0d, 0.0d}, new double[]{0.0d, 0.5d, 0.0d, 0.0d}, new double[]{0.0d, acv, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, acv, 0.0d, 0.0d, 0.0d, 0.5d}, new double[0]};
    private static final int[] types = {0, 1, 3, 1, 3, 1, 3, 1, 3, 4};

    RoundRectIterator(RoundRectangle2D rr, BaseTransform tx) {
        this.f11915x = rr.f11924x;
        this.f11916y = rr.f11925y;
        this.f11917w = rr.width;
        this.f11918h = rr.height;
        this.f11919aw = Math.min(this.f11917w, Math.abs(rr.arcWidth));
        this.f11920ah = Math.min(this.f11918h, Math.abs(rr.arcHeight));
        this.transform = tx;
        if (this.f11919aw < 0.0d || this.f11920ah < 0.0d) {
            this.index = ctrlpts.length;
        }
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public boolean isDone() {
        return this.index >= ctrlpts.length;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public void next() {
        this.index++;
        if (this.index < ctrlpts.length && this.f11919aw == 0.0d && this.f11920ah == 0.0d && types[this.index] == 3) {
            this.index++;
        }
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("roundrect iterator out of bounds");
        }
        double[] ctrls = ctrlpts[this.index];
        int nc = 0;
        for (int i2 = 0; i2 < ctrls.length; i2 += 4) {
            int i3 = nc;
            int nc2 = nc + 1;
            coords[i3] = (float) (this.f11915x + (ctrls[i2 + 0] * this.f11917w) + (ctrls[i2 + 1] * this.f11919aw));
            nc = nc2 + 1;
            coords[nc2] = (float) (this.f11916y + (ctrls[i2 + 2] * this.f11918h) + (ctrls[i2 + 3] * this.f11920ah));
        }
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, nc / 2);
        }
        return types[this.index];
    }
}
