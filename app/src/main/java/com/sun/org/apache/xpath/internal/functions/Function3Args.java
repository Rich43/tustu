package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/Function3Args.class */
public class Function3Args extends Function2Args {
    static final long serialVersionUID = 7915240747161506646L;
    Expression m_arg2;

    public Expression getArg2() {
        return this.m_arg2;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        if (null != this.m_arg2) {
            this.m_arg2.fixupVariables(vars, globalsSize);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {
        if (argNum < 2) {
            super.setArg(arg, argNum);
        } else if (2 == argNum) {
            this.m_arg2 = arg;
            arg.exprSetParent(this);
        } else {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
        if (argNum != 3) {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("three", null));
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (super.canTraverseOutsideSubtree()) {
            return true;
        }
        return this.m_arg2.canTraverseOutsideSubtree();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/Function3Args$Arg2Owner.class */
    class Arg2Owner implements ExpressionOwner {
        Arg2Owner() {
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return Function3Args.this.m_arg2;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(Function3Args.this);
            Function3Args.this.m_arg2 = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void callArgVisitors(XPathVisitor visitor) {
        super.callArgVisitors(visitor);
        if (null != this.m_arg2) {
            this.m_arg2.callVisitors(new Arg2Owner(), visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        if (null != this.m_arg2) {
            if (null == ((Function3Args) expr).m_arg2 || !this.m_arg2.deepEquals(((Function3Args) expr).m_arg2)) {
                return false;
            }
            return true;
        }
        if (null != ((Function3Args) expr).m_arg2) {
            return false;
        }
        return true;
    }
}
