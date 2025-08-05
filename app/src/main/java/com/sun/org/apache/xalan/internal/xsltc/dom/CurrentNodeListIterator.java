package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListIterator.class */
public final class CurrentNodeListIterator extends DTMAxisIteratorBase {
    private boolean _docOrder;
    private DTMAxisIterator _source;
    private final CurrentNodeListFilter _filter;
    private IntegerArray _nodes;
    private int _currentIndex;
    private final int _currentNode;
    private AbstractTranslet _translet;

    public CurrentNodeListIterator(DTMAxisIterator source, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet) {
        this(source, !source.isReverse(), filter, currentNode, translet);
    }

    public CurrentNodeListIterator(DTMAxisIterator source, boolean docOrder, CurrentNodeListFilter filter, int currentNode, AbstractTranslet translet) {
        this._nodes = new IntegerArray();
        this._source = source;
        this._filter = filter;
        this._translet = translet;
        this._docOrder = docOrder;
        this._currentNode = currentNode;
    }

    public DTMAxisIterator forceNaturalOrder() {
        this._docOrder = true;
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setRestartable(boolean isRestartable) {
        this._isRestartable = isRestartable;
        this._source.setRestartable(isRestartable);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public boolean isReverse() {
        return !this._docOrder;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        try {
            CurrentNodeListIterator clone = (CurrentNodeListIterator) super.clone();
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
    public DTMAxisIterator reset() {
        this._currentIndex = 0;
        return resetPosition();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        int last = this._nodes.cardinality();
        int currentNode = this._currentNode;
        AbstractTranslet translet = this._translet;
        int index = this._currentIndex;
        while (index < last) {
            int position = this._docOrder ? index + 1 : last - index;
            int i2 = index;
            index++;
            int node = this._nodes.at(i2);
            if (this._filter.test(node, position, last, currentNode, translet, this)) {
                this._currentIndex = index;
                return returnNode(node);
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        if (this._isRestartable) {
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
            this._currentIndex = 0;
            resetPosition();
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getLast() {
        if (this._last == -1) {
            this._last = computePositionOfLast();
        }
        return this._last;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._markedNode = this._currentIndex;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._currentIndex = this._markedNode;
    }

    private int computePositionOfLast() {
        int last = this._nodes.cardinality();
        int currNode = this._currentNode;
        AbstractTranslet translet = this._translet;
        int lastPosition = this._position;
        int index = this._currentIndex;
        while (index < last) {
            int position = this._docOrder ? index + 1 : last - index;
            int i2 = index;
            index++;
            int nodeIndex = this._nodes.at(i2);
            if (this._filter.test(nodeIndex, position, last, currNode, translet, this)) {
                lastPosition++;
            }
        }
        return lastPosition;
    }
}
