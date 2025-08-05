package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/EllipseIterator.class */
class EllipseIterator implements PathIterator {

    /* renamed from: x, reason: collision with root package name */
    double f11901x;

    /* renamed from: y, reason: collision with root package name */
    double f11902y;

    /* renamed from: w, reason: collision with root package name */
    double f11903w;

    /* renamed from: h, reason: collision with root package name */
    double f11904h;
    BaseTransform transform;
    int index;
    public static final double CtrlVal = 0.5522847498307933d;
    private static final double pcv = 0.7761423749153966d;
    private static final double ncv = 0.22385762508460333d;
    private static final double[][] ctrlpts = {new double[]{1.0d, pcv, pcv, 1.0d, 0.5d, 1.0d}, new double[]{ncv, 1.0d, 0.0d, pcv, 0.0d, 0.5d}, new double[]{0.0d, ncv, ncv, 0.0d, 0.5d, 0.0d}, new double[]{pcv, 0.0d, 1.0d, ncv, 1.0d, 0.5d}};

    EllipseIterator(Ellipse2D e2, BaseTransform tx) {
        this.f11901x = e2.f11899x;
        this.f11902y = e2.f11900y;
        this.f11903w = e2.width;
        this.f11904h = e2.height;
        this.transform = tx;
        if (this.f11903w < 0.0d || this.f11904h < 0.0d) {
            this.index = 6;
        }
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int getWindingRule() {
        return 1;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public boolean isDone() {
        return this.index > 5;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public void next() {
        this.index++;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("ellipse iterator out of bounds");
        }
        if (this.index == 5) {
            return 4;
        }
        if (this.index == 0) {
            double[] ctrls = ctrlpts[3];
            coords[0] = (float) (this.f11901x + (ctrls[4] * this.f11903w));
            coords[1] = (float) (this.f11902y + (ctrls[5] * this.f11904h));
            if (this.transform != null) {
                this.transform.transform(coords, 0, coords, 0, 1);
                return 0;
            }
            return 0;
        }
        double[] ctrls2 = ctrlpts[this.index - 1];
        coords[0] = (float) (this.f11901x + (ctrls2[0] * this.f11903w));
        coords[1] = (float) (this.f11902y + (ctrls2[1] * this.f11904h));
        coords[2] = (float) (this.f11901x + (ctrls2[2] * this.f11903w));
        coords[3] = (float) (this.f11902y + (ctrls2[3] * this.f11904h));
        coords[4] = (float) (this.f11901x + (ctrls2[4] * this.f11903w));
        coords[5] = (float) (this.f11902y + (ctrls2[5] * this.f11904h));
        if (this.transform != null) {
            this.transform.transform(coords, 0, coords, 0, 3);
            return 3;
        }
        return 3;
    }
}
