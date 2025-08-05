package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SortingIterator.class */
public final class SortingIterator extends DTMAxisIteratorBase {
    private static final int INIT_DATA_SIZE = 16;
    private DTMAxisIterator _source;
    private NodeSortRecordFactory _factory;
    private NodeSortRecord[] _data;
    private int _free = 0;
    private int _current;

    public SortingIterator(DTMAxisIterator source, NodeSortRecordFactory factory) {
        this._source = source;
        this._factory = factory;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        if (this._current >= this._free) {
            return -1;
        }
        NodeSortRecord[] nodeSortRecordArr = this._data;
        int i2 = this._current;
        this._current = i2 + 1;
        return nodeSortRecordArr[i2].getNode();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        try {
            DTMAxisIterator dTMAxisIterator = this._source;
            this._startNode = node;
            dTMAxisIterator.setStartNode(node);
            this._data = new NodeSortRecord[16];
            this._free = 0;
            while (true) {
                int node2 = this._source.next();
                if (node2 != -1) {
                    addRecord(this._factory.makeNodeSortRecord(node2, this._free));
                } else {
                    quicksort(0, this._free - 1);
                    this._current = 0;
                    return this;
                }
            }
        } catch (Exception e2) {
            return this;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getPosition() {
        if (this._current == 0) {
            return 1;
        }
        return this._current;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getLast() {
        return this._free;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._source.setMark();
        this._markedNode = this._current;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._source.gotoMark();
        this._current = this._markedNode;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        try {
            SortingIterator clone = (SortingIterator) super.clone();
            clone._source = this._source.cloneIterator();
            clone._factory = this._factory;
            clone._data = this._data;
            clone._free = this._free;
            clone._current = this._current;
            clone.setRestartable(false);
            return clone.reset();
        } catch (CloneNotSupportedException e2) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e2.toString());
            return null;
        }
    }

    private void addRecord(NodeSortRecord record) {
        if (this._free == this._data.length) {
            NodeSortRecord[] newArray = new NodeSortRecord[this._data.length * 2];
            System.arraycopy(this._data, 0, newArray, 0, this._free);
            this._data = newArray;
        }
        NodeSortRecord[] nodeSortRecordArr = this._data;
        int i2 = this._free;
        this._free = i2 + 1;
        nodeSortRecordArr[i2] = record;
    }

    private void quicksort(int p2, int r2) {
        while (p2 < r2) {
            int q2 = partition(p2, r2);
            quicksort(p2, q2);
            p2 = q2 + 1;
        }
    }

    private int partition(int p2, int r2) {
        NodeSortRecord x2 = this._data[(p2 + r2) >>> 1];
        int i2 = p2 - 1;
        int j2 = r2 + 1;
        while (true) {
            j2--;
            if (x2.compareTo(this._data[j2]) >= 0) {
                do {
                    i2++;
                } while (x2.compareTo(this._data[i2]) > 0);
                if (i2 < j2) {
                    NodeSortRecord t2 = this._data[i2];
                    this._data[i2] = this._data[j2];
                    this._data[j2] = t2;
                } else {
                    return j2;
                }
            }
        }
    }
}
