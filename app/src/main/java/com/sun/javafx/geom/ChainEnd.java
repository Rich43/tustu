package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/ChainEnd.class */
final class ChainEnd {
    CurveLink head;
    CurveLink tail;
    ChainEnd partner;
    int etag;

    public ChainEnd(CurveLink first, ChainEnd partner) {
        this.head = first;
        this.tail = first;
        this.partner = partner;
        this.etag = first.getEdgeTag();
    }

    public CurveLink getChain() {
        return this.head;
    }

    public void setOtherEnd(ChainEnd partner) {
        this.partner = partner;
    }

    public ChainEnd getPartner() {
        return this.partner;
    }

    public CurveLink linkTo(ChainEnd that) {
        ChainEnd enter;
        ChainEnd exit;
        if (this.etag == 0 || that.etag == 0) {
            throw new InternalError("ChainEnd linked more than once!");
        }
        if (this.etag == that.etag) {
            throw new InternalError("Linking chains of the same type!");
        }
        if (this.etag == 1) {
            enter = this;
            exit = that;
        } else {
            enter = that;
            exit = this;
        }
        this.etag = 0;
        that.etag = 0;
        enter.tail.setNext(exit.head);
        enter.tail = exit.tail;
        if (this.partner == that) {
            return enter.head;
        }
        ChainEnd otherenter = exit.partner;
        ChainEnd otherexit = enter.partner;
        otherenter.partner = otherexit;
        otherexit.partner = otherenter;
        if (enter.head.getYTop() < otherenter.head.getYTop()) {
            enter.tail.setNext(otherenter.head);
            otherenter.head = enter.head;
            return null;
        }
        otherexit.tail.setNext(enter.head);
        otherexit.tail = enter.tail;
        return null;
    }

    public void addLink(CurveLink newlink) {
        if (this.etag == 1) {
            this.tail.setNext(newlink);
            this.tail = newlink;
        } else {
            newlink.setNext(this.head);
            this.head = newlink;
        }
    }

    public double getX() {
        if (this.etag == 1) {
            return this.tail.getXBot();
        }
        return this.head.getXBot();
    }
}
