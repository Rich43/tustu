package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Order0.class */
final class Order0 extends Curve {

    /* renamed from: x, reason: collision with root package name */
    private double f11905x;

    /* renamed from: y, reason: collision with root package name */
    private double f11906y;

    public Order0(double x2, double y2) {
        super(1);
        this.f11905x = x2;
        this.f11906y = y2;
    }

    @Override // com.sun.javafx.geom.Curve
    public int getOrder() {
        return 0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXTop() {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getYTop() {
        return this.f11906y;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXBot() {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getYBot() {
        return this.f11906y;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXMin() {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXMax() {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getX0() {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getY0() {
        return this.f11906y;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getX1() {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getY1() {
        return this.f11906y;
    }

    @Override // com.sun.javafx.geom.Curve
    public double XforY(double y2) {
        return y2;
    }

    @Override // com.sun.javafx.geom.Curve
    public double TforY(double y2) {
        return 0.0d;
    }

    @Override // com.sun.javafx.geom.Curve
    public double XforT(double t2) {
        return this.f11905x;
    }

    @Override // com.sun.javafx.geom.Curve
    public double YforT(double t2) {
        return this.f11906y;
    }

    @Override // com.sun.javafx.geom.Curve
    public double dXforT(double t2, int deriv) {
        return 0.0d;
    }

    @Override // com.sun.javafx.geom.Curve
    public double dYforT(double t2, int deriv) {
        return 0.0d;
    }

    @Override // com.sun.javafx.geom.Curve
    public double nextVertical(double t0, double t1) {
        return t1;
    }

    @Override // com.sun.javafx.geom.Curve
    public int crossingsFor(double x2, double y2) {
        return 0;
    }

    @Override // com.sun.javafx.geom.Curve
    public boolean accumulateCrossings(Crossings c2) {
        return this.f11905x > c2.getXLo() && this.f11905x < c2.getXHi() && this.f11906y > c2.getYLo() && this.f11906y < c2.getYHi();
    }

    @Override // com.sun.javafx.geom.Curve
    public void enlarge(RectBounds r2) {
        r2.add((float) this.f11905x, (float) this.f11906y);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getSubCurve(double ystart, double yend, int dir) {
        return this;
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getReversedCurve() {
        return this;
    }

    @Override // com.sun.javafx.geom.Curve
    public int getSegment(float[] coords) {
        coords[0] = (float) this.f11905x;
        coords[1] = (float) this.f11906y;
        return 0;
    }
}
