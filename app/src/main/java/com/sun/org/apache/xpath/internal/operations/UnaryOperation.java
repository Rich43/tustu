package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/UnaryOperation.class */
public abstract class UnaryOperation extends Expression implements ExpressionOwner {
    static final long serialVersionUID = 6536083808424286166L;
    protected Expression m_right;

    public abstract XObject operate(XObject xObject) throws TransformerException;

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        this.m_right.fixupVariables(vars, globalsSize);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (null != this.m_right && this.m_right.canTraverseOutsideSubtree()) {
            return true;
        }
        return false;
    }

    public void setRight(Expression r2) {
        this.m_right = r2;
        r2.exprSetParent(this);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        return operate(this.m_right.execute(xctxt));
    }

    public Expression getOperand() {
        return this.m_right;
    }

    @Override // com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        if (visitor.visitUnaryOperation(owner, this)) {
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
        if (!isSameClass(expr) || !this.m_right.deepEquals(((UnaryOperation) expr).m_right)) {
            return false;
        }
        return true;
    }
}
