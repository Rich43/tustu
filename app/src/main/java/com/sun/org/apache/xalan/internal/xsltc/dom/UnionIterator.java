package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/UnionIterator.class */
public final class UnionIterator extends MultiValuedNodeHeapIterator {
    private final DOM _dom;

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/UnionIterator$LookAheadIterator.class */
    private final class LookAheadIterator extends MultiValuedNodeHeapIterator.HeapNode {
        public DTMAxisIterator iterator;

        public LookAheadIterator(DTMAxisIterator iterator) {
            super();
            this.iterator = iterator;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public int step() {
            this._node = this.iterator.next();
            return this._node;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public MultiValuedNodeHeapIterator.HeapNode cloneHeapNode() {
            LookAheadIterator clone = (LookAheadIterator) super.cloneHeapNode();
            clone.iterator = this.iterator.cloneIterator();
            return clone;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public void setMark() {
            super.setMark();
            this.iterator.setMark();
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public void gotoMark() {
            super.gotoMark();
            this.iterator.gotoMark();
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public boolean isLessThan(MultiValuedNodeHeapIterator.HeapNode heapNode) {
            return UnionIterator.this._dom.lessThan(this._node, heapNode._node);
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public MultiValuedNodeHeapIterator.HeapNode setStartNode(int node) {
            this.iterator.setStartNode(node);
            return this;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode
        public MultiValuedNodeHeapIterator.HeapNode reset() {
            this.iterator.reset();
            return this;
        }
    }

    public UnionIterator(DOM dom) {
        this._dom = dom;
    }

    public UnionIterator addIterator(DTMAxisIterator iterator) {
        addHeapNode(new LookAheadIterator(iterator));
        return this;
    }
}
