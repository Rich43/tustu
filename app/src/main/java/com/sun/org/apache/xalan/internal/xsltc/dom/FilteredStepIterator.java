package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/FilteredStepIterator.class */
public final class FilteredStepIterator extends StepIterator {
    private Filter _filter;

    public FilteredStepIterator(DTMAxisIterator source, DTMAxisIterator iterator, Filter filter) {
        super(source, iterator);
        this._filter = filter;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.dom.StepIterator, com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        int node;
        do {
            node = super.next();
            if (node == -1) {
                return node;
            }
        } while (!this._filter.test(node));
        return returnNode(node);
    }
}
