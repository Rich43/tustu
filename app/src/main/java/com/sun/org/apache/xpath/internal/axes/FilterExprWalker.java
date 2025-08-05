package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.operations.Variable;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/FilterExprWalker.class */
public class FilterExprWalker extends AxesWalker {
    static final long serialVersionUID = 5457182471424488375L;
    private Expression m_expr;
    private transient XNodeSet m_exprObj;
    private boolean m_mustHardReset;
    private boolean m_canDetachNodeset;

    public FilterExprWalker(WalkingIterator locPathIterator) {
        super(locPathIterator, 20);
        this.m_mustHardReset = false;
        this.m_canDetachNodeset = true;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public void init(Compiler compiler, int opPos, int stepType) throws TransformerException {
        super.init(compiler, opPos, stepType);
        switch (stepType) {
            case 22:
            case 23:
                break;
            case 24:
            case 25:
                this.m_mustHardReset = true;
                break;
            default:
                this.m_expr = compiler.compileExpression(opPos + 2);
                this.m_expr.exprSetParent(this);
                return;
        }
        this.m_expr = compiler.compileExpression(opPos);
        this.m_expr.exprSetParent(this);
        if (this.m_expr instanceof Variable) {
            this.m_canDetachNodeset = false;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public void detach() {
        super.detach();
        if (this.m_canDetachNodeset) {
            this.m_exprObj.detach();
        }
        this.m_exprObj = null;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public void setRoot(int root) {
        super.setRoot(root);
        this.m_exprObj = FilterExprIteratorSimple.executeFilterExpr(root, this.m_lpi.getXPathContext(), this.m_lpi.getPrefixResolver(), this.m_lpi.getIsTopLevel(), this.m_lpi.m_stackFrame, this.m_expr);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker, com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public Object clone() throws CloneNotSupportedException {
        FilterExprWalker clone = (FilterExprWalker) super.clone();
        if (null != this.m_exprObj) {
            clone.m_exprObj = (XNodeSet) this.m_exprObj.clone();
        }
        return clone;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public short acceptNode(int n2) {
        try {
            if (getPredicateCount() > 0) {
                countProximityPosition(0);
                if (!executePredicates(n2, this.m_lpi.getXPathContext())) {
                    return (short) 3;
                }
                return (short) 1;
            }
            return (short) 1;
        } catch (TransformerException se) {
            throw new RuntimeException(se.getMessage());
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public int getNextNode() {
        if (null != this.m_exprObj) {
            int next = this.m_exprObj.nextNode();
            return next;
        }
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker, com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.axes.SubContextList
    public int getLastPos(XPathContext xctxt) {
        return this.m_exprObj.getLength();
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

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker, com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        if (null != this.m_expr && (this.m_expr instanceof PathComponent)) {
            return ((PathComponent) this.m_expr).getAnalysisBits();
        }
        return 67108864;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public boolean isDocOrdered() {
        return this.m_exprObj.isDocOrdered();
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker
    public int getAxis() {
        return this.m_exprObj.getAxis();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/FilterExprWalker$filterExprOwner.class */
    class filterExprOwner implements ExpressionOwner {
        filterExprOwner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return FilterExprWalker.this.m_expr;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(FilterExprWalker.this);
            FilterExprWalker.this.m_expr = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest
    public void callPredicateVisitors(XPathVisitor visitor) {
        this.m_expr.callVisitors(new filterExprOwner(), visitor);
        super.callPredicateVisitors(visitor);
    }

    @Override // com.sun.org.apache.xpath.internal.axes.AxesWalker, com.sun.org.apache.xpath.internal.axes.PredicatedNodeTest, com.sun.org.apache.xpath.internal.patterns.NodeTest, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        FilterExprWalker walker = (FilterExprWalker) expr;
        if (!this.m_expr.deepEquals(walker.m_expr)) {
            return false;
        }
        return true;
    }
}
