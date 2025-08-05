package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/FilterExprIterator.class */
public class FilterExprIterator extends BasicTestIterator {
    static final long serialVersionUID = 2552176105165737614L;
    private Expression m_expr;
    private transient XNodeSet m_exprObj;
    private boolean m_mustHardReset;
    private boolean m_canDetachNodeset;

    public FilterExprIterator() {
        super(null);
        this.m_mustHardReset = false;
        this.m_canDetachNodeset = true;
    }

    public FilterExprIterator(Expression expr) {
        super(null);
        this.m_mustHardReset = false;
        this.m_canDetachNodeset = true;
        this.m_expr = expr;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        this.m_exprObj = FilterExprIteratorSimple.executeFilterExpr(context, this.m_execContext, getPrefixResolver(), getIsTopLevel(), this.m_stackFrame, this.m_expr);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.BasicTestIterator
    protected int getNextNode() {
        if (null != this.m_exprObj) {
            this.m_lastFetched = this.m_exprObj.nextNode();
        } else {
            this.m_lastFetched = -1;
        }
        return this.m_lastFetched;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        super.detach();
        this.m_exprObj.detach();
        this.m_exprObj = null;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        this.m_expr.fixupVariables(vars, globalsSize);
    }

    public Expression getInnerExpression() {
        return this.m_expr;
    }

    public void setInnerExpression(Expression expr) {
        expr.exprSetParent(this);
        this.m_expr = expr;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        if (null != this.m_expr && (this.m_expr instanceof PathComponent)) {
            return ((PathComponent) this.m_expr).getAnalysisBits();
        }
        return 67108864;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public boolean isDocOrdered() {
        return this.m_exprObj.isDocOrdered();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/FilterExprIterator$filterExprOwner.class */
    class filterExprOwner implements ExpressionOwner {
        filterExprOwner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return FilterExprIterator.this.m_expr;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(FilterExprIterator.this);
            FilterExprIterator.this.m_expr = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public void callPredicateVisitors(XPathVisitor visitor) {
        this.m_expr.callVisitors(new filterExprOwner(), visitor);
        super.callPredicateVisitors(visitor);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        FilterExprIterator fet = (FilterExprIterator) expr;
        if (!this.m_expr.deepEquals(fet.m_expr)) {
            return false;
        }
        return true;
    }
}
