package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/CurveLink.class */
final class CurveLink {
    Curve curve;
    double ytop;
    double ybot;
    int etag;
    CurveLink next;

    public CurveLink(Curve curve, double ystart, double yend, int etag) {
        this.curve = curve;
        this.ytop = ystart;
        this.ybot = yend;
        this.etag = etag;
        if (this.ytop < curve.getYTop() || this.ybot > curve.getYBot()) {
            throw new InternalError("bad curvelink [" + this.ytop + "=>" + this.ybot + "] for " + ((Object) curve));
        }
    }

    public boolean absorb(CurveLink link) {
        return absorb(link.curve, link.ytop, link.ybot, link.etag);
    }

    public boolean absorb(Curve curve, double ystart, double yend, int etag) {
        if (this.curve != curve || this.etag != etag || this.ybot < ystart || this.ytop > yend) {
            return false;
        }
        if (ystart < curve.getYTop() || yend > curve.getYBot()) {
            throw new InternalError("bad curvelink [" + ystart + "=>" + yend + "] for " + ((Object) curve));
        }
        this.ytop = Math.min(this.ytop, ystart);
        this.ybot = Math.max(this.ybot, yend);
        return true;
    }

    public boolean isEmpty() {
        return this.ytop == this.ybot;
    }

    public Curve getCurve() {
        return this.curve;
    }

    public Curve getSubCurve() {
        if (this.ytop == this.curve.getYTop() && this.ybot == this.curve.getYBot()) {
            return this.curve.getWithDirection(this.etag);
        }
        return this.curve.getSubCurve(this.ytop, this.ybot, this.etag);
    }

    public Curve getMoveto() {
        return new Order0(getXTop(), getYTop());
    }

    public double getXTop() {
        return this.curve.XforY(this.ytop);
    }

    public double getYTop() {
        return this.ytop;
    }

    public double getXBot() {
        return this.curve.XforY(this.ybot);
    }

    public double getYBot() {
        return this.ybot;
    }

    public double getX() {
        return this.curve.XforY(this.ytop);
    }

    public int getEdgeTag() {
        return this.etag;
    }

    public void setNext(CurveLink link) {
        this.next = link;
    }

    public CurveLink getNext() {
        return this.next;
    }
}
