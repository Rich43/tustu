package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.VariableStack;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/FilterExprIteratorSimple.class */
public class FilterExprIteratorSimple extends LocPathIterator {
    static final long serialVersionUID = -6978977187025375579L;
    private Expression m_expr;
    private transient XNodeSet m_exprObj;
    private boolean m_mustHardReset;
    private boolean m_canDetachNodeset;

    public FilterExprIteratorSimple() {
        super(null);
        this.m_mustHardReset = false;
        this.m_canDetachNodeset = true;
    }

    public FilterExprIteratorSimple(Expression expr) {
        super(null);
        this.m_mustHardReset = false;
        this.m_canDetachNodeset = true;
        this.m_expr = expr;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void setRoot(int context, Object environment) {
        super.setRoot(context, environment);
        this.m_exprObj = executeFilterExpr(context, this.m_execContext, getPrefixResolver(), getIsTopLevel(), this.m_stackFrame, this.m_expr);
    }

    public static XNodeSet executeFilterExpr(int context, XPathContext xctxt, PrefixResolver prefixResolver, boolean isTopLevel, int stackFrame, Expression expr) throws WrappedRuntimeException {
        XNodeSet result;
        PrefixResolver savedResolver = xctxt.getNamespaceContext();
        try {
            try {
                xctxt.pushCurrentNode(context);
                xctxt.setNamespaceContext(prefixResolver);
                if (isTopLevel) {
                    VariableStack vars = xctxt.getVarStack();
                    int savedStart = vars.getStackFrame();
                    vars.setStackFrame(stackFrame);
                    result = (XNodeSet) expr.execute(xctxt);
                    result.setShouldCacheNodes(true);
                    vars.setStackFrame(savedStart);
                } else {
                    result = (XNodeSet) expr.execute(xctxt);
                }
                return result;
            } catch (TransformerException se) {
                throw new WrappedRuntimeException(se);
            }
        } finally {
            xctxt.popCurrentNode();
            xctxt.setNamespaceContext(savedResolver);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int nextNode() {
        int next;
        if (this.m_foundLast) {
            return -1;
        }
        if (null != this.m_exprObj) {
            int iNextNode = this.m_exprObj.nextNode();
            next = iNextNode;
            this.m_lastFetched = iNextNode;
        } else {
            next = -1;
            this.m_lastFetched = -1;
        }
        if (-1 != next) {
            this.m_pos++;
            return next;
        }
        this.m_foundLast = true;
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public void detach() {
        if (this.m_allowDetach) {
            super.detach();
            this.m_exprObj.detach();
            this.m_exprObj = null;
        }
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

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/FilterExprIteratorSimple$filterExprOwner.class */
    class filterExprOwner implements ExpressionOwner {
        filterExprOwner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return FilterExprIteratorSimple.this.m_expr;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(FilterExprIteratorSimple.this);
            FilterExprIteratorSimple.this.m_expr = exp;
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
        FilterExprIteratorSimple fet = (FilterExprIteratorSimple) expr;
        if (!this.m_expr.deepEquals(fet.m_expr)) {
            return false;
        }
        return true;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.LocPathIterator, com.sun.org.apache.xml.internal.dtm.DTMIterator
    public int getAxis() {
        if (null != this.m_exprObj) {
            return this.m_exprObj.getAxis();
        }
        return 20;
    }
}
