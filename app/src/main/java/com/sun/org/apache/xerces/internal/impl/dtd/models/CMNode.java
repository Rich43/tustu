package com.sun.org.apache.xerces.internal.impl.dtd.models;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/CMNode.class */
public abstract class CMNode {
    private int fType;
    private CMStateSet fFirstPos = null;
    private CMStateSet fFollowPos = null;
    private CMStateSet fLastPos = null;
    private int fMaxStates = -1;
    private Object fUserData = null;

    public abstract boolean isNullable();

    protected abstract void calcFirstPos(CMStateSet cMStateSet);

    protected abstract void calcLastPos(CMStateSet cMStateSet);

    public CMNode(int type) {
        this.fType = type;
    }

    public final int type() {
        return this.fType;
    }

    public final CMStateSet firstPos() {
        if (this.fFirstPos == null) {
            this.fFirstPos = new CMStateSet(this.fMaxStates);
            calcFirstPos(this.fFirstPos);
        }
        return this.fFirstPos;
    }

    public final CMStateSet lastPos() {
        if (this.fLastPos == null) {
            this.fLastPos = new CMStateSet(this.fMaxStates);
            calcLastPos(this.fLastPos);
        }
        return this.fLastPos;
    }

    final void setFollowPos(CMStateSet setToAdopt) {
        this.fFollowPos = setToAdopt;
    }

    public final void setMaxStates(int maxStates) {
        this.fMaxStates = maxStates;
    }

    public void setUserData(Object userData) {
        this.fUserData = userData;
    }

    public Object getUserData() {
        return this.fUserData;
    }
}
