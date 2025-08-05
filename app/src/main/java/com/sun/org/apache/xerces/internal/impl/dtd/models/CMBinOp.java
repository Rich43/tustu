package com.sun.org.apache.xerces.internal.impl.dtd.models;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/models/CMBinOp.class */
public class CMBinOp extends CMNode {
    private CMNode fLeftChild;
    private CMNode fRightChild;

    public CMBinOp(int type, CMNode leftNode, CMNode rightNode) {
        super(type);
        if (type() != 4 && type() != 5) {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
        this.fLeftChild = leftNode;
        this.fRightChild = rightNode;
    }

    final CMNode getLeft() {
        return this.fLeftChild;
    }

    final CMNode getRight() {
        return this.fRightChild;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    public boolean isNullable() {
        if (type() == 4) {
            return this.fLeftChild.isNullable() || this.fRightChild.isNullable();
        }
        if (type() == 5) {
            return this.fLeftChild.isNullable() && this.fRightChild.isNullable();
        }
        throw new RuntimeException("ImplementationMessages.VAL_BST");
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    protected void calcFirstPos(CMStateSet toSet) {
        if (type() == 4) {
            toSet.setTo(this.fLeftChild.firstPos());
            toSet.union(this.fRightChild.firstPos());
        } else {
            if (type() == 5) {
                toSet.setTo(this.fLeftChild.firstPos());
                if (this.fLeftChild.isNullable()) {
                    toSet.union(this.fRightChild.firstPos());
                    return;
                }
                return;
            }
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
    protected void calcLastPos(CMStateSet toSet) {
        if (type() == 4) {
            toSet.setTo(this.fLeftChild.lastPos());
            toSet.union(this.fRightChild.lastPos());
        } else {
            if (type() == 5) {
                toSet.setTo(this.fRightChild.lastPos());
                if (this.fRightChild.isNullable()) {
                    toSet.union(this.fLeftChild.lastPos());
                    return;
                }
                return;
            }
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }
}
