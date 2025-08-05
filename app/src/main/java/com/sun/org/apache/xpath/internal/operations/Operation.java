package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Operation.class */
public class Operation extends Expression implements ExpressionOwner {
    static final long serialVersionUID = -3037139537171050430L;
    protected Expression m_left;
    protected Expression m_right;

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        this.m_left.fixupVariables(vars, globalsSize);
        this.m_right.fixupVariables(vars, globalsSize);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_left && this.m_left.canTraverseOutsideSubtree()) {
            return true;
        }
        if (null != this.m_right && this.m_right.canTraverseOutsideSubtree()) {
            return true;
        }
        return false;
    }

    public void setLeftRight(Expression l2, Expression r2) {
        this.m_left = l2;
        this.m_right = r2;
        l2.exprSetParent(this);
        r2.exprSetParent(this);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        XObject left = this.m_left.execute(xctxt, true);
        XObject right = this.m_right.execute(xctxt, true);
        XObject result = operate(left, right);
        left.detach();
        right.detach();
        return result;
    }

    public XObject operate(XObject left, XObject right) throws TransformerException {
        return null;
    }

    public Expression getLeftOperand() {
        return this.m_left;
    }

    public Expression getRightOperand() {
        return this.m_right;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Operation$LeftExprOwner.class */
    class LeftExprOwner implements ExpressionOwner {
        LeftExprOwner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return Operation.this.m_left;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(Operation.this);
            Operation.this.m_left = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        if (visitor.visitBinaryOperation(owner, this)) {
            this.m_left.callVisitors(new LeftExprOwner(), visitor);
            this.m_right.callVisitors(this, visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public Expression getExpression() {
        return this.m_right;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public void setExpression(Expression exp) {
        exp.exprSetParent(this);
        this.m_right = exp;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!isSameClass(expr) || !this.m_left.deepEquals(((Operation) expr).m_left) || !this.m_right.deepEquals(((Operation) expr).m_right)) {
            return false;
        }
        return true;
    }
}
