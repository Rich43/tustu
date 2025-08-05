package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SingletonIterator.class */
public class SingletonIterator extends DTMAxisIteratorBase {
    private int _node;
    private final boolean _isConstant;

    public SingletonIterator() {
        this(Integer.MIN_VALUE, false);
    }

    public SingletonIterator(int node) {
        this(node, false);
    }

    public SingletonIterator(int node, boolean constant) {
        this._startNode = node;
        this._node = node;
        this._isConstant = constant;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        if (this._isConstant) {
            this._node = this._startNode;
            return resetPosition();
        }
        if (this._isRestartable) {
            if (this._node <= 0) {
                this._startNode = node;
                this._node = node;
            }
            return resetPosition();
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        if (this._isConstant) {
            this._node = this._startNode;
            return resetPosition();
        }
        boolean temp = this._isRestartable;
        this._isRestartable = true;
        setStartNode(this._startNode);
        this._isRestartable = temp;
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        int result = this._node;
        this._node = -1;
        return returnNode(result);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._markedNode = this._node;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._node = this._markedNode;
    }
}
