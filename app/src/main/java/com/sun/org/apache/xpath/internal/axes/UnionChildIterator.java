package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.patterns.NodeTest;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/UnionChildIterator.class */
public class UnionChildIterator extends ChildTestIterator {
    static final long serialVersionUID = 3500298482193003495L;
    private PredicatedNodeTest[] m_nodeTests;

    public UnionChildIterator() {
        super(null);
        this.m_nodeTests = null;
    }

    public void addNodeTest(PredicatedNodeTest test) {
        if (null == this.m_nodeTests) {
            this.m_nodeTests = new PredicatedNodeTest[1];
            this.m_nodeTests[0] = test;
        } else {
            PredicatedNodeTest[] tests = this.m_nodeTests;
            int len = this.m_nodeTests.length;
            this.m_nodeTests = new PredicatedNodeTest[len + 1];
            System.arraycopy(tests, 0, this.m_nodeTests, 0, len);
            this.m_nodeTests[len] = test;
        }
        test.exprSetParent(this);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        if (this.m_nodeTests != null) {
            for (int i2 = 0; i2 < this.m_nodeTests.length; i2++) {
                this.m_nodeTests[i2].fixupVariables(vars, globalsSize);
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public short acceptNode(int n2) {
        XPathContext xctxt = getXPathContext();
        try {
            try {
                xctxt.pushCurrentNode(n2);
                for (int i2 = 0; i2 < this.m_nodeTests.length; i2++) {
                    PredicatedNodeTest pnt = this.m_nodeTests[i2];
                    XObject score = pnt.execute(xctxt, n2);
                    if (score != NodeTest.SCORE_NONE) {
                        if (pnt.getPredicateCount() <= 0) {
                            xctxt.popCurrentNode();
                            return (short) 1;
                        }
                        if (pnt.executePredicates(n2, xctxt)) {
                            return (short) 1;
                        }
                    }
                }
                xctxt.popCurrentNode();
                return (short) 3;
            } catch (TransformerException se) {
                throw new RuntimeException(se.getMessage());
            }
        } finally {
            xctxt.popCurrentNode();
        }
    }
}
