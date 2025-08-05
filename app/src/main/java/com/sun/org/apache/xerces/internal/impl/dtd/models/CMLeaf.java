package com.sun.org.apache.xerces.internal.impl.dtd.models;

import com.sun.org.apache.xerces.internal.xni.QName;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/CMLeaf.class */
public class CMLeaf extends CMNode {
    private QName fElement;
    private int fPosition;

    public CMLeaf(QName element, int position) {
        super(0);
        this.fElement = new QName();
        this.fPosition = -1;
        this.fElement.setValues(element);
        this.fPosition = position;
    }

    public CMLeaf(QName element) {
        super(0);
        this.fElement = new QName();
        this.fPosition = -1;
        this.fElement.setValues(element);
    }

    final QName getElement() {
        return this.fElement;
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
        StringBuffer strRet = new StringBuffer(this.fElement.toString());
        strRet.append(" (");
        strRet.append(this.fElement.uri);
        strRet.append(',');
        strRet.append(this.fElement.localpart);
        strRet.append(')');
        if (this.fPosition >= 0) {
            strRet.append(" (Pos:" + new Integer(this.fPosition).toString() + ")");
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
