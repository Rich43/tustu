package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/ChildIterator.class */
public class ChildIterator extends LocPathIterator {
    static final long serialVersionUID = -6935428015142993583L;

    ChildIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis, false);
        initNodeTest(-1);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.Expression
    public int asNode(XPathContext xctxt) throws TransformerException {
        int current = xctxt.getCurrentNode();
        DTM dtm = xctxt.getDTM(current);
        return dtm.getFirstChild(current);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        int nextSibling;
        if (this.m_foundLast) {
            return -1;
        }
        if (-1 == this.m_lastFetched) {
            nextSibling = this.m_cdtm.getFirstChild(this.m_context);
        } else {
            nextSibling = this.m_cdtm.getNextSibling(this.m_lastFetched);
        }
        int next = nextSibling;
        this.m_lastFetched = nextSibling;
        if (-1 != next) {
            this.m_pos++;
            return next;
        }
        this.m_foundLast = true;
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return 3;
    }
}
