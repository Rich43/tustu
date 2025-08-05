package com.sun.org.apache.xerces.internal.impl.xs.models;

import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/models/XSCMUniOp.class */
public class XSCMUniOp extends CMNode {
    private CMNode fChild;

    public XSCMUniOp(int type, CMNode childNode) {
        super(type);
        if (type() != 5 && type() != 4 && type() != 6) {
            throw new RuntimeException("ImplementationMessages.VAL_UST");
        }
        this.fChild = childNode;
    }

    final CMNode getChild() {
        return this.fChild;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public boolean isNullable() {
        if (type() == 6) {
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

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public void setUserData(Object userData) {
        super.setUserData(userData);
        this.fChild.setUserData(userData);
    }
}
