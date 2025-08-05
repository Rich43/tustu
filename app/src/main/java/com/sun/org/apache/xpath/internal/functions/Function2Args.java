package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/Function2Args.class */
public class Function2Args extends FunctionOneArg {
    static final long serialVersionUID = 5574294996842710641L;
    Expression m_arg1;

    public Expression getArg1() {
        return this.m_arg1;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        if (null != this.m_arg1) {
            this.m_arg1.fixupVariables(vars, globalsSize);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {
        if (argNum == 0) {
            super.setArg(arg, argNum);
        } else if (1 == argNum) {
            this.m_arg1 = arg;
            arg.exprSetParent(this);
        } else {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
        if (argNum != 2) {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("two", null));
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (super.canTraverseOutsideSubtree()) {
            return true;
        }
        return this.m_arg1.canTraverseOutsideSubtree();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/Function2Args$Arg1Owner.class */
    class Arg1Owner implements ExpressionOwner {
        Arg1Owner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return Function2Args.this.m_arg1;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(Function2Args.this);
            Function2Args.this.m_arg1 = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void callArgVisitors(XPathVisitor visitor) {
        super.callArgVisitors(visitor);
        if (null != this.m_arg1) {
            this.m_arg1.callVisitors(new Arg1Owner(), visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        if (null != this.m_arg1) {
            if (null == ((Function2Args) expr).m_arg1 || !this.m_arg1.deepEquals(((Function2Args) expr).m_arg1)) {
                return false;
            }
            return true;
        }
        if (null != ((Function2Args) expr).m_arg1) {
            return false;
        }
        return true;
    }
}
