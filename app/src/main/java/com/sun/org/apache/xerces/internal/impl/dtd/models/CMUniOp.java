package com.sun.org.apache.xerces.internal.impl.dtd.models;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/CMUniOp.class */
public class CMUniOp extends CMNode {
    private CMNode fChild;

    public CMUniOp(int type, CMNode childNode) {
        super(type);
        if (type() != 1 && type() != 2 && type() != 3) {
            throw new RuntimeException("ImplementationMessages.VAL_UST");
        }
        this.fChild = childNode;
    }

    final CMNode getChild() {
        return this.fChild;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public boolean isNullable() {
        if (type() == 3) {
            return this.fChild.isNullable();
        }
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    protected void calcFirstPos(CMStateSet toSet) {
        toSet.setTo(this.fChild.firstPos());
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    protected void calcLastPos(CMStateSet toSet) {
        toSet.setTo(this.fChild.lastPos());
    }
}
