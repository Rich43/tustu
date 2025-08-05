package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/AnyNodeCounter.class */
public abstract class AnyNodeCounter extends NodeCounter {
    public AnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        super(translet, document, iterator);
    }

    public AnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom) {
        super(translet, document, iterator, hasFrom);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public NodeCounter setStartNode(int node) {
        this._node = node;
        this._nodeType = this._document.getExpandedTypeID(node);
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public String getCounter() {
        if (this._value != -2.147483648E9d) {
            return this._value == 0.0d ? "0" : Double.isNaN(this._value) ? "NaN" : (this._value >= 0.0d || !Double.isInfinite(this._value)) ? Double.isInfinite(this._value) ? Constants.ATTRVAL_INFINITY : formatNumbers((int) this._value) : "-Infinity";
        }
        int root = this._document.getDocument();
        int result = 0;
        for (int next = this._node; next >= root && !matchesFrom(next); next--) {
            if (matchesCount(next)) {
                result++;
            }
        }
        return formatNumbers(result);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        return new DefaultAnyNodeCounter(translet, document, iterator);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/AnyNodeCounter$DefaultAnyNodeCounter.class */
    static class DefaultAnyNodeCounter extends AnyNodeCounter {
        public DefaultAnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
            super(translet, document, iterator);
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.AnyNodeCounter, com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
        public String getCounter() {
            int result;
            if (this._value != -2.147483648E9d) {
                if (this._value == 0.0d) {
                    return "0";
                }
                if (Double.isNaN(this._value)) {
                    return "NaN";
                }
                if (this._value < 0.0d && Double.isInfinite(this._value)) {
                    return "-Infinity";
                }
                if (Double.isInfinite(this._value)) {
                    return Constants.ATTRVAL_INFINITY;
                }
                result = (int) this._value;
            } else {
                result = 0;
                int ntype = this._document.getExpandedTypeID(this._node);
                int root = this._document.getDocument();
                for (int next = this._node; next >= 0; next--) {
                    if (ntype == this._document.getExpandedTypeID(next)) {
                        result++;
                    }
                    if (next == root) {
                        break;
                    }
                }
            }
            return formatNumbers(result);
        }
    }
}
