package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/DupFilterIterator.class */
public final class DupFilterIterator extends DTMAxisIteratorBase {
    private DTMAxisIterator _source;
    private IntegerArray _nodes = new IntegerArray();
    private int _current = 0;
    private int _nodesSize = 0;
    private int _lastNext = -1;
    private int _markedLastNext = -1;

    public DupFilterIterator(DTMAxisIterator source) {
        this._source = source;
        if (source instanceof KeyIndex) {
            setStartNode(0);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        if (this._isRestartable) {
            boolean sourceIsKeyIndex = this._source instanceof KeyIndex;
            if (sourceIsKeyIndex && this._startNode == 0) {
                return this;
            }
            if (node != this._startNode) {
                DTMAxisIterator dTMAxisIterator = this._source;
                this._startNode = node;
                dTMAxisIterator.setStartNode(node);
                this._nodes.clear();
                while (true) {
                    int node2 = this._source.next();
                    if (node2 == -1) {
                        break;
                    }
                    this._nodes.add(node2);
                }
                if (!sourceIsKeyIndex) {
                    this._nodes.sort();
                }
                this._nodesSize = this._nodes.cardinality();
                this._current = 0;
                this._lastNext = -1;
                resetPosition();
            }
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        while (this._current < this._nodesSize) {
            IntegerArray integerArray = this._nodes;
            int i2 = this._current;
            this._current = i2 + 1;
            int next = integerArray.at(i2);
            if (next != this._lastNext) {
                this._lastNext = next;
                return returnNode(next);
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        try {
            DupFilterIterator clone = (DupFilterIterator) super.clone();
            clone._nodes = (IntegerArray) this._nodes.clone();
            clone._source = this._source.cloneIterator();
            clone._isRestartable = false;
            return clone.reset();
        } catch (CloneNotSupportedException e2) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setRestartable(boolean isRestartable) {
        this._isRestartable = isRestartable;
        this._source.setRestartable(isRestartable);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._markedNode = this._current;
        this._markedLastNext = this._lastNext;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._current = this._markedNode;
        this._lastNext = this._markedLastNext;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        this._current = 0;
        this._lastNext = -1;
        return resetPosition();
    }
}
