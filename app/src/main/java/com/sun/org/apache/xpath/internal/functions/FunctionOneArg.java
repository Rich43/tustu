package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FunctionOneArg.class */
public class FunctionOneArg extends Function implements ExpressionOwner {
    static final long serialVersionUID = -5180174180765609758L;
    Expression m_arg0;

    public Expression getArg0() {
        return this.m_arg0;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {
        if (0 == argNum) {
            this.m_arg0 = arg;
            arg.exprSetParent(this);
        } else {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
        if (argNum != 1) {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("one", null));
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        return this.m_arg0.canTraverseOutsideSubtree();
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        if (null != this.m_arg0) {
            this.m_arg0.fixupVariables(vars, globalsSize);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void callArgVisitors(XPathVisitor visitor) {
        if (null != this.m_arg0) {
            this.m_arg0.callVisitors(this, visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public Expression getExpression() {
        return this.m_arg0;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public void setExpression(Expression exp) {
        exp.exprSetParent(this);
        this.m_arg0 = exp;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        if (null != this.m_arg0) {
            if (null == ((FunctionOneArg) expr).m_arg0 || !this.m_arg0.deepEquals(((FunctionOneArg) expr).m_arg0)) {
                return false;
            }
            return true;
        }
        if (null != ((FunctionOneArg) expr).m_arg0) {
            return false;
        }
        return true;
    }
}
