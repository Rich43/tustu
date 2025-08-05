package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.OpMap;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/OneStepIteratorForward.class */
public class OneStepIteratorForward extends ChildTestIterator {
    static final long serialVersionUID = -1576936606178190566L;
    protected int m_axis;

    OneStepIteratorForward(Compiler compiler, int opPos, int analysis) throws TransformerException {
        super(compiler, opPos, analysis);
        this.m_axis = -1;
        int firstStepPos = OpMap.getFirstChildPos(opPos);
        this.m_axis = WalkerFactory.getAxisFromStep(compiler, firstStepPos);
    }

    public OneStepIteratorForward(int axis) {
        super(null);
        this.m_axis = -1;
        this.m_axis = axis;
        initNodeTest(-1);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        this.m_traverser = this.m_cdtm.getAxisTraverser(this.m_axis);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.BasicTestIterator
    protected int getNextNode() {
        int next;
        if (-1 == this.m_lastFetched) {
            next = this.m_traverser.first(this.m_context);
        } else {
            next = this.m_traverser.next(this.m_context, this.m_lastFetched);
        }
        this.m_lastFetched = next;
        return this.m_lastFetched;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.ChildTestIterator, com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        return this.m_axis;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr) || this.m_axis != ((OneStepIteratorForward) expr).m_axis) {
            return false;
        }
        return true;
    }
}
