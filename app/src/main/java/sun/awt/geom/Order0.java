package sun.awt.geom;

import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/awt/geom/Order0.class */
final class Order0 extends Curve {

    /* renamed from: x, reason: collision with root package name */
    private double f13543x;

    /* renamed from: y, reason: collision with root package name */
    private double f13544y;

    public Order0(double d2, double d3) {
        super(1);
        this.f13543x = d2;
        this.f13544y = d3;
    }

    @Override // sun.awt.geom.Curve
    public int getOrder() {
        return 0;
    }

    @Override // sun.awt.geom.Curve
    public double getXTop() {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double getYTop() {
        return this.f13544y;
    }

    @Override // sun.awt.geom.Curve
    public double getXBot() {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double getYBot() {
        return this.f13544y;
    }

    @Override // sun.awt.geom.Curve
    public double getXMin() {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double getXMax() {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double getX0() {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double getY0() {
        return this.f13544y;
    }

    @Override // sun.awt.geom.Curve
    public double getX1() {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double getY1() {
        return this.f13544y;
    }

    @Override // sun.awt.geom.Curve
    public double XforY(double d2) {
        return d2;
    }

    @Override // sun.awt.geom.Curve
    public double TforY(double d2) {
        return 0.0d;
    }

    @Override // sun.awt.geom.Curve
    public double XforT(double d2) {
        return this.f13543x;
    }

    @Override // sun.awt.geom.Curve
    public double YforT(double d2) {
        return this.f13544y;
    }

    @Override // sun.awt.geom.Curve
    public double dXforT(double d2, int i2) {
        return 0.0d;
    }

    @Override // sun.awt.geom.Curve
    public double dYforT(double d2, int i2) {
        return 0.0d;
    }

    @Override // sun.awt.geom.Curve
    public double nextVertical(double d2, double d3) {
        return d3;
    }

    @Override // sun.awt.geom.Curve
    public int crossingsFor(double d2, double d3) {
        return 0;
    }

    @Override // sun.awt.geom.Curve
    public boolean accumulateCrossings(Crossings crossings) {
        return this.f13543x > crossings.getXLo() && this.f13543x < crossings.getXHi() && this.f13544y > crossings.getYLo() && this.f13544y < crossings.getYHi();
    }

    @Override // sun.awt.geom.Curve
    public void enlarge(Rectangle2D rectangle2D) {
        rectangle2D.add(this.f13543x, this.f13544y);
    }

    @Override // sun.awt.geom.Curve
    public Curve getSubCurve(double d2, double d3, int i2) {
        return this;
    }

    @Override // sun.awt.geom.Curve
    public Curve getReversedCurve() {
        return this;
    }

    @Override // sun.awt.geom.Curve
    public int getSegment(double[] dArr) {
        dArr[0] = this.f13543x;
        dArr[1] = this.f13544y;
        return 0;
    }
}
