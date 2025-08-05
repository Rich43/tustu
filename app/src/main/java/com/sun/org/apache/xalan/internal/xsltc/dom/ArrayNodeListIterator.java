package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/ArrayNodeListIterator.class */
public class ArrayNodeListIterator implements DTMAxisIterator {
    private int _pos = 0;
    private int _mark = 0;
    private int[] _nodes;
    private static final int[] EMPTY = new int[0];

    public ArrayNodeListIterator(int[] nodes) {
        this._nodes = nodes;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        if (this._pos >= this._nodes.length) {
            return -1;
        }
        int[] iArr = this._nodes;
        int i2 = this._pos;
        this._pos = i2 + 1;
        return iArr[i2];
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        this._pos = 0;
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getLast() {
        return this._nodes.length;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getPosition() {
        return this._pos;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        this._mark = this._pos;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        this._pos = this._mark;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int node) {
        if (node == -1) {
            this._nodes = EMPTY;
        }
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getStartNode() {
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public boolean isReverse() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        return new ArrayNodeListIterator(this._nodes);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setRestartable(boolean isRestartable) {
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getNodeByPosition(int position) {
        return this._nodes[position - 1];
    }
}
