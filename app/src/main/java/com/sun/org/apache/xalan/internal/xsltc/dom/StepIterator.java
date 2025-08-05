package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/StepIterator.class */
public class StepIterator extends DTMAxisIteratorBase {
    protected DTMAxisIterator _source;
    protected DTMAxisIterator _iterator;
    private int _pos = -1;

    public StepIterator(DTMAxisIterator source, DTMAxisIterator iterator) {
        this._source = source;
        this._iterator = iterator;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setRestartable(boolean isRestartable) {
        this._isRestartable = isRestartable;
        this._source.setRestartable(isRestartable);
        this._iterator.setRestartable(true);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        this._isRestartable = false;
        try {
            StepIterator clone = (StepIterator) super.clone();
            clone._source = this._source.cloneIterator();
            clone._iterator = this._iterator.cloneIterator();
            clone._iterator.setRestartable(true);
            clone._isRestartable = false;
            return clone.reset();
        } catch (CloneNotSupportedException e2) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
            return null;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        if (this._isRestartable) {
            DTMAxisIterator dTMAxisIterator = this._source;
            this._startNode = node;
            dTMAxisIterator.setStartNode(node);
            this._iterator.setStartNode(this._includeSelf ? this._startNode : this._source.next());
            return resetPosition();
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        this._source.reset();
        this._iterator.setStartNode(this._includeSelf ? this._startNode : this._source.next());
        return resetPosition();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        while (true) {
            int node = this._iterator.next();
            if (node != -1) {
                return returnNode(node);
            }
            int node2 = this._source.next();
            if (node2 == -1) {
                return -1;
            }
            this._iterator.setStartNode(node2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._source.setMark();
        this._iterator.setMark();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._source.gotoMark();
        this._iterator.gotoMark();
    }
}
