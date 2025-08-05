package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultiValuedNodeHeapIterator.class */
public abstract class MultiValuedNodeHeapIterator extends DTMAxisIteratorBase {
    private static final int InitSize = 8;
    private int _returnedLast;
    private int _cachedHeapSize;
    private int _heapSize = 0;
    private int _size = 8;
    private HeapNode[] _heap = new HeapNode[8];
    private int _free = 0;
    private int _cachedReturnedLast = -1;

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultiValuedNodeHeapIterator$HeapNode.class */
    public abstract class HeapNode implements Cloneable {
        protected int _node;
        protected int _markedNode;
        protected boolean _isStartSet = false;

        public abstract int step();

        public abstract boolean isLessThan(HeapNode heapNode);

        public abstract HeapNode setStartNode(int i2);

        public abstract HeapNode reset();

        public HeapNode() {
        }

        public HeapNode cloneHeapNode() {
            try {
                HeapNode clone = (HeapNode) super.clone();
                clone._node = this._node;
                clone._markedNode = this._node;
                return clone;
            } catch (CloneNotSupportedException e2) {
                BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
                return null;
            }
        }

        public void setMark() {
            this._markedNode = this._node;
        }

        public void gotoMark() {
            this._node = this._markedNode;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        this._isRestartable = false;
        HeapNode[] heapCopy = new HeapNode[this._heap.length];
        try {
            MultiValuedNodeHeapIterator clone = (MultiValuedNodeHeapIterator) super.clone();
            for (int i2 = 0; i2 < this._free; i2++) {
                heapCopy[i2] = this._heap[i2].cloneHeapNode();
            }
            clone.setRestartable(false);
            clone._heap = heapCopy;
            return clone.reset();
        } catch (CloneNotSupportedException e2) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
            return null;
        }
    }

    protected void addHeapNode(HeapNode node) {
        if (this._free == this._size) {
            int i2 = this._size * 2;
            this._size = i2;
            HeapNode[] newArray = new HeapNode[i2];
            System.arraycopy(this._heap, 0, newArray, 0, this._free);
            this._heap = newArray;
        }
        this._heapSize++;
        HeapNode[] heapNodeArr = this._heap;
        int i3 = this._free;
        this._free = i3 + 1;
        heapNodeArr[i3] = node;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        while (this._heapSize > 0) {
            int smallest = this._heap[0]._node;
            if (smallest == -1) {
                if (this._heapSize > 1) {
                    HeapNode temp = this._heap[0];
                    HeapNode[] heapNodeArr = this._heap;
                    HeapNode[] heapNodeArr2 = this._heap;
                    int i2 = this._heapSize - 1;
                    this._heapSize = i2;
                    heapNodeArr[0] = heapNodeArr2[i2];
                    this._heap[this._heapSize] = temp;
                } else {
                    return -1;
                }
            } else if (smallest == this._returnedLast) {
                this._heap[0].step();
            } else {
                this._heap[0].step();
                heapify(0);
                this._returnedLast = smallest;
                return returnNode(smallest);
            }
            heapify(0);
        }
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        if (this._isRestartable) {
            this._startNode = node;
            for (int i2 = 0; i2 < this._free; i2++) {
                if (!this._heap[i2]._isStartSet) {
                    this._heap[i2].setStartNode(node);
                    this._heap[i2].step();
                    this._heap[i2]._isStartSet = true;
                }
            }
            int i3 = this._free;
            this._heapSize = i3;
            for (int i4 = i3 / 2; i4 >= 0; i4--) {
                heapify(i4);
            }
            this._returnedLast = -1;
            return resetPosition();
        }
        return this;
    }

    protected void init() {
        for (int i2 = 0; i2 < this._free; i2++) {
            this._heap[i2] = null;
        }
        this._heapSize = 0;
        this._free = 0;
    }

    private void heapify(int i2) {
        while (true) {
            int r2 = (i2 + 1) << 1;
            int l2 = r2 - 1;
            int smallest = (l2 >= this._heapSize || !this._heap[l2].isLessThan(this._heap[i2])) ? i2 : l2;
            if (r2 < this._heapSize && this._heap[r2].isLessThan(this._heap[smallest])) {
                smallest = r2;
            }
            if (smallest != i2) {
                HeapNode temp = this._heap[smallest];
                this._heap[smallest] = this._heap[i2];
                this._heap[i2] = temp;
                i2 = smallest;
            } else {
                return;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        for (int i2 = 0; i2 < this._free; i2++) {
            this._heap[i2].setMark();
        }
        this._cachedReturnedLast = this._returnedLast;
        this._cachedHeapSize = this._heapSize;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        for (int i2 = 0; i2 < this._free; i2++) {
            this._heap[i2].gotoMark();
        }
        int i3 = this._cachedHeapSize;
        this._heapSize = i3;
        for (int i4 = i3 / 2; i4 >= 0; i4--) {
            heapify(i4);
        }
        this._returnedLast = this._cachedReturnedLast;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        for (int i2 = 0; i2 < this._free; i2++) {
            this._heap[i2].reset();
            this._heap[i2].step();
        }
        int i3 = this._free;
        this._heapSize = i3;
        for (int i4 = i3 / 2; i4 >= 0; i4--) {
            heapify(i4);
        }
        this._returnedLast = -1;
        return resetPosition();
    }
}
