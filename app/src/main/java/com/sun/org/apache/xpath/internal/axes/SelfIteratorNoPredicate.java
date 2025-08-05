package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/SelfIteratorNoPredicate.class */
public class SelfIteratorNoPredicate extends LocPathIterator {
    static final long serialVersionUID = -4226887905279814201L;

    SelfIteratorNoPredicate(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis, false);
    }

    public SelfIteratorNoPredicate() throws TransformerException {
        super(null);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        if (this.m_foundLast) {
            return -1;
        }
        DTM dtm = this.m_cdtm;
        int i2 = -1 == this.m_lastFetched ? this.m_context : -1;
        int next = i2;
        this.m_lastFetched = i2;
        if (-1 != next) {
            this.m_pos++;
            return next;
        }
        this.m_foundLast = true;
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.Expression
    public int asNode(XPathContext xctxt) throws TransformerException {
        return xctxt.getCurrentNode();
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getLastPos(XPathContext xctxt) {
        return 1;
    }
}
