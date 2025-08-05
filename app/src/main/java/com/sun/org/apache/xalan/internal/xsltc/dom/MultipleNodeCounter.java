package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultipleNodeCounter.class */
public abstract class MultipleNodeCounter extends NodeCounter {
    private DTMAxisIterator _precSiblings;

    public MultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        super(translet, document, iterator);
        this._precSiblings = null;
    }

    public MultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom) {
        super(translet, document, iterator, hasFrom);
        this._precSiblings = null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public NodeCounter setStartNode(int node) {
        this._node = node;
        this._nodeType = this._document.getExpandedTypeID(node);
        this._precSiblings = this._document.getAxisIterator(12);
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public String getCounter() {
        if (this._value != -2.147483648E9d) {
            return this._value == 0.0d ? "0" : Double.isNaN(this._value) ? "NaN" : (this._value >= 0.0d || !Double.isInfinite(this._value)) ? Double.isInfinite(this._value) ? Constants.ATTRVAL_INFINITY : formatNumbers((int) this._value) : "-Infinity";
        }
        IntegerArray ancestors = new IntegerArray();
        int next = this._node;
        ancestors.add(next);
        while (true) {
            int parent = this._document.getParent(next);
            next = parent;
            if (parent <= -1 || matchesFrom(next)) {
                break;
            }
            ancestors.add(next);
        }
        int nAncestors = ancestors.cardinality();
        int[] counters = new int[nAncestors];
        for (int i2 = 0; i2 < nAncestors; i2++) {
            counters[i2] = Integer.MIN_VALUE;
        }
        int j2 = 0;
        int i3 = nAncestors - 1;
        while (i3 >= 0) {
            int i4 = counters[j2];
            int ancestor = ancestors.at(i3);
            if (matchesCount(ancestor)) {
                this._precSiblings.setStartNode(ancestor);
                while (true) {
                    int next2 = this._precSiblings.next();
                    if (next2 == -1) {
                        break;
                    }
                    if (matchesCount(next2)) {
                        counters[j2] = counters[j2] == Integer.MIN_VALUE ? 1 : counters[j2] + 1;
                    }
                }
                counters[j2] = counters[j2] == Integer.MIN_VALUE ? 1 : counters[j2] + 1;
            }
            i3--;
            j2++;
        }
        return formatNumbers(counters);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        return new DefaultMultipleNodeCounter(translet, document, iterator);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/MultipleNodeCounter$DefaultMultipleNodeCounter.class */
    static class DefaultMultipleNodeCounter extends MultipleNodeCounter {
        public DefaultMultipleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
            super(translet, document, iterator);
        }
    }
}
