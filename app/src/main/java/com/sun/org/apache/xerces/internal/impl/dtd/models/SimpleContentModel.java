package com.sun.org.apache.xerces.internal.impl.dtd.models;

import com.sun.org.apache.xerces.internal.xni.QName;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/SimpleContentModel.class */
public class SimpleContentModel implements ContentModelValidator {
    public static final short CHOICE = -1;
    public static final short SEQUENCE = -1;
    private QName fFirstChild = new QName();
    private QName fSecondChild = new QName();
    private int fOperator;

    public SimpleContentModel(short operator, QName firstChild, QName secondChild) {
        this.fFirstChild.setValues(firstChild);
        if (secondChild != null) {
            this.fSecondChild.setValues(secondChild);
        } else {
            this.fSecondChild.clear();
        }
        this.fOperator = operator;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator
    public int validate(QName[] children, int offset, int length) {
        switch (this.fOperator) {
            case 0:
                if (length == 0 || children[offset].rawname != this.fFirstChild.rawname) {
                    return 0;
                }
                if (length > 1) {
                    return 1;
                }
                return -1;
            case 1:
                if (length == 1 && children[offset].rawname != this.fFirstChild.rawname) {
                    return 0;
                }
                if (length > 1) {
                    return 1;
                }
                return -1;
            case 2:
                if (length > 0) {
                    for (int index = 0; index < length; index++) {
                        if (children[offset + index].rawname != this.fFirstChild.rawname) {
                            return index;
                        }
                    }
                    return -1;
                }
                return -1;
            case 3:
                if (length == 0) {
                    return 0;
                }
                for (int index2 = 0; index2 < length; index2++) {
                    if (children[offset + index2].rawname != this.fFirstChild.rawname) {
                        return index2;
                    }
                }
                return -1;
            case 4:
                if (length == 0) {
                    return 0;
                }
                if (children[offset].rawname != this.fFirstChild.rawname && children[offset].rawname != this.fSecondChild.rawname) {
                    return 0;
                }
                if (length > 1) {
                    return 1;
                }
                return -1;
            case 5:
                if (length == 2) {
                    if (children[offset].rawname != this.fFirstChild.rawname) {
                        return 0;
                    }
                    if (children[offset + 1].rawname != this.fSecondChild.rawname) {
                        return 1;
                    }
                    return -1;
                }
                if (length > 2) {
                    return 2;
                }
                return length;
            default:
                throw new RuntimeException("ImplementationMessages.VAL_CST");
        }
    }
}
