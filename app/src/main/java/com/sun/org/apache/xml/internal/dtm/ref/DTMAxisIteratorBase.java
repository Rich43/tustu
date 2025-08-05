package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMAxisIteratorBase.class */
public abstract class DTMAxisIteratorBase implements DTMAxisIterator {
    protected int _markedNode;
    protected int _last = -1;
    protected int _position = 0;
    protected int _startNode = -1;
    protected boolean _includeSelf = false;
    protected boolean _isRestartable = true;

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getStartNode() {
        return this._startNode;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        boolean temp = this._isRestartable;
        this._isRestartable = true;
        setStartNode(this._startNode);
        this._isRestartable = temp;
        return this;
    }

    public DTMAxisIterator includeSelf() {
        this._includeSelf = true;
        return this;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
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

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getPosition() {
        if (this._position == 0) {
            return 1;
        }
        return this._position;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public boolean isReverse() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        try {
            DTMAxisIteratorBase clone = (DTMAxisIteratorBase) super.clone();
            clone._isRestartable = false;
            return clone;
        } catch (CloneNotSupportedException e2) {
            throw new WrappedRuntimeException(e2);
        }
    }

    protected final int returnNode(int node) {
        this._position++;
        return node;
    }

    protected final DTMAxisIterator resetPosition() {
        this._position = 0;
        return this;
    }

    public boolean isDocOrdered() {
        return true;
    }

    public int getAxis() {
        return -1;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setRestartable(boolean isRestartable) {
        this._isRestartable = isRestartable;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int getNodeByPosition(int position) {
        int node;
        if (position > 0) {
            int pos = isReverse() ? (getLast() - position) + 1 : position;
            do {
                node = next();
                if (node == -1) {
                    return -1;
                }
            } while (pos != getPosition());
            return node;
        }
        return -1;
    }
}
