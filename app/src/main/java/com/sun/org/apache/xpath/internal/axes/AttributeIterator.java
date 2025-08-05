package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xpath.internal.compiler.Compiler;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/AttributeIterator.class */
public class AttributeIterator extends ChildTestIterator {
    static final long serialVersionUID = -8417986700712229686L;

    AttributeIterator(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.BasicTestIterator
    protected int getNextNode() {
        int nextAttribute;
        if (-1 == this.m_lastFetched) {
            nextAttribute = this.m_cdtm.getFirstAttribute(this.m_context);
        } else {
            nextAttribute = this.m_cdtm.getNextAttribute(this.m_lastFetched);
        }
        this.m_lastFetched = nextAttribute;
        return this.m_lastFetched;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return 2;
    }
}
