package sun.awt.geom;

/* loaded from: rt.jar:sun/awt/geom/ChainEnd.class */
final class ChainEnd {
    CurveLink head;
    CurveLink tail;
    ChainEnd partner;
    int etag;

    public ChainEnd(CurveLink curveLink, ChainEnd chainEnd) {
        this.head = curveLink;
        this.tail = curveLink;
        this.partner = chainEnd;
        this.etag = curveLink.getEdgeTag();
    }

    public CurveLink getChain() {
        return this.head;
    }

    public void setOtherEnd(ChainEnd chainEnd) {
        this.partner = chainEnd;
    }

    public ChainEnd getPartner() {
        return this.partner;
    }

    public CurveLink linkTo(ChainEnd chainEnd) {
        ChainEnd chainEnd2;
        ChainEnd chainEnd3;
        if (this.etag == 0 || chainEnd.etag == 0) {
            throw new InternalError("ChainEnd linked more than once!");
        }
        if (this.etag == chainEnd.etag) {
            throw new InternalError("Linking chains of the same type!");
        }
        if (this.etag == 1) {
            chainEnd2 = this;
            chainEnd3 = chainEnd;
        } else {
            chainEnd2 = chainEnd;
            chainEnd3 = this;
        }
        this.etag = 0;
        chainEnd.etag = 0;
        chainEnd2.tail.setNext(chainEnd3.head);
        chainEnd2.tail = chainEnd3.tail;
        if (this.partner == chainEnd) {
            return chainEnd2.head;
        }
        ChainEnd chainEnd4 = chainEnd3.partner;
        ChainEnd chainEnd5 = chainEnd2.partner;
        chainEnd4.partner = chainEnd5;
        chainEnd5.partner = chainEnd4;
        if (chainEnd2.head.getYTop() < chainEnd4.head.getYTop()) {
            chainEnd2.tail.setNext(chainEnd4.head);
            chainEnd4.head = chainEnd2.head;
            return null;
        }
        chainEnd5.tail.setNext(chainEnd2.head);
        chainEnd5.tail = chainEnd2.tail;
        return null;
    }

    public void addLink(CurveLink curveLink) {
        if (this.etag == 1) {
            this.tail.setNext(curveLink);
            this.tail = curveLink;
        } else {
            curveLink.setNext(this.head);
            this.head = curveLink;
        }
    }

    public double getX() {
        if (this.etag == 1) {
            return this.tail.getXBot();
        }
        return this.head.getXBot();
    }
}
