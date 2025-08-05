package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SingleNodeCounter.class */
public abstract class SingleNodeCounter extends NodeCounter {
    private static final int[] EmptyArray = new int[0];
    DTMAxisIterator _countSiblings;

    public SingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        super(translet, document, iterator);
        this._countSiblings = null;
    }

    public SingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom) {
        super(translet, document, iterator, hasFrom);
        this._countSiblings = null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public NodeCounter setStartNode(int node) {
        this._node = node;
        this._nodeType = this._document.getExpandedTypeID(node);
        this._countSiblings = this._document.getAxisIterator(12);
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public String getCounter() {
        int next;
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
            int result = (int) this._value;
            return formatNumbers(result);
        }
        int next2 = this._node;
        int result2 = 0;
        boolean matchesCount = matchesCount(next2);
        if (!matchesCount) {
            while (true) {
                int parent = this._document.getParent(next2);
                next2 = parent;
                if (parent <= -1 || matchesCount(next2)) {
                    break;
                }
                if (matchesFrom(next2)) {
                    next2 = -1;
                    break;
                }
            }
        }
        if (next2 != -1) {
            int from = next2;
            if (!matchesCount && this._hasFrom) {
                do {
                    int parent2 = this._document.getParent(from);
                    from = parent2;
                    if (parent2 <= -1) {
                        break;
                    }
                } while (!matchesFrom(from));
            }
            if (from != -1) {
                this._countSiblings.setStartNode(next2);
                do {
                    if (matchesCount(next2)) {
                        result2++;
                    }
                    next = this._countSiblings.next();
                    next2 = next;
                } while (next != -1);
                return formatNumbers(result2);
            }
        }
        return formatNumbers(EmptyArray);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        return new DefaultSingleNodeCounter(translet, document, iterator);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/SingleNodeCounter$DefaultSingleNodeCounter.class */
    static class DefaultSingleNodeCounter extends SingleNodeCounter {
        public DefaultSingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
            super(translet, document, iterator);
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SingleNodeCounter, com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
        public NodeCounter setStartNode(int node) {
            this._node = node;
            this._nodeType = this._document.getExpandedTypeID(node);
            this._countSiblings = this._document.getTypedAxisIterator(12, this._document.getExpandedTypeID(node));
            return this;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.dom.SingleNodeCounter, com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
        public String getCounter() {
            int result;
            if (this._value == -2.147483648E9d) {
                result = 1;
                this._countSiblings.setStartNode(this._node);
                while (this._countSiblings.next() != -1) {
                    result++;
                }
            } else {
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
            }
            return formatNumbers(result);
        }
    }
}
