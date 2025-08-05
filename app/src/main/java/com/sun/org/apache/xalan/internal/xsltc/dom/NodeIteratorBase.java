package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.NodeIterator;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/NodeIteratorBase.class */
public abstract class NodeIteratorBase implements NodeIterator {
    protected int _markedNode;
    protected int _last = -1;
    protected int _position = 0;
    protected int _startNode = -1;
    protected boolean _includeSelf = false;
    protected boolean _isRestartable = true;

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public abstract NodeIterator setStartNode(int i2);

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public void setRestartable(boolean isRestartable) {
        this._isRestartable = isRestartable;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public NodeIterator reset() {
        boolean temp = this._isRestartable;
        this._isRestartable = true;
        setStartNode(this._includeSelf ? this._startNode + 1 : this._startNode);
        this._isRestartable = temp;
        return this;
    }

    public NodeIterator includeSelf() {
        this._includeSelf = true;
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public int getLast() {
        if (this._last == -1) {
            int temp = this._position;
            setMark();
            reset();
            do {
                this._last++;
            } while (next() != -1);
            gotoMark();
            this._position = temp;
        }
        return this._last;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public int getPosition() {
        if (this._position == 0) {
            return 1;
        }
        return this._position;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public boolean isReverse() {
        return false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.NodeIterator
    public NodeIterator cloneIterator() {
        try {
            NodeIteratorBase clone = (NodeIteratorBase) super.clone();
            clone._isRestartable = false;
            return clone.reset();
        } catch (CloneNotSupportedException e2) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
            return null;
        }
    }

    protected final int returnNode(int node) {
        this._position++;
        return node;
    }

    protected final NodeIterator resetPosition() {
        this._position = 0;
        return this;
    }
}
