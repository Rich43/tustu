package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSCMLeaf.class */
public class XSCMLeaf extends CMNode {
    private Object fLeaf;
    private int fParticleId;
    private int fPosition;

    public XSCMLeaf(int type, Object leaf, int id, int position) {
        super(type);
        this.fLeaf = null;
        this.fParticleId = -1;
        this.fPosition = -1;
        this.fLeaf = leaf;
        this.fParticleId = id;
        this.fPosition = position;
    }

    final Object getLeaf() {
        return this.fLeaf;
    }

    final int getParticleId() {
        return this.fParticleId;
    }

    final int getPosition() {
        return this.fPosition;
    }

    final void setPosition(int newPosition) {
        this.fPosition = newPosition;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public boolean isNullable() {
        return this.fPosition == -1;
    }

    public String toString() {
        StringBuffer strRet = new StringBuffer(this.fLeaf.toString());
        if (this.fPosition >= 0) {
            strRet.append(" (Pos:" + Integer.toString(this.fPosition) + ")");
        }
        return strRet.toString();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    protected void calcFirstPos(CMStateSet toSet) {
        if (this.fPosition == -1) {
            toSet.zeroBits();
        } else {
            toSet.setBit(this.fPosition);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    protected void calcLastPos(CMStateSet toSet) {
        if (this.fPosition == -1) {
            toSet.zeroBits();
        } else {
            toSet.setBit(this.fPosition);
        }
    }
}
