package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FunctionMultiArgs.class */
public class FunctionMultiArgs extends Function3Args {
    static final long serialVersionUID = 7117257746138417181L;
    Expression[] m_args;

    public Expression[] getArgs() {
        return this.m_args;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {
        if (argNum < 3) {
            super.setArg(arg, argNum);
            return;
        }
        if (null == this.m_args) {
            this.m_args = new Expression[1];
            this.m_args[0] = arg;
        } else {
            Expression[] args = new Expression[this.m_args.length + 1];
            System.arraycopy(this.m_args, 0, args, 0, this.m_args.length);
            args[this.m_args.length] = arg;
            this.m_args = args;
        }
        arg.exprSetParent(this);
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        super.fixupVariables(vars, globalsSize);
        if (null != this.m_args) {
            for (int i2 = 0; i2 < this.m_args.length; i2++) {
                this.m_args[i2].fixupVariables(vars, globalsSize);
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{"Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called."});
        throw new RuntimeException(fMsg);
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (super.canTraverseOutsideSubtree()) {
            return true;
        }
        int n2 = this.m_args.length;
        for (int i2 = 0; i2 < n2; i2++) {
            if (this.m_args[i2].canTraverseOutsideSubtree()) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FunctionMultiArgs$ArgMultiOwner.class */
    class ArgMultiOwner implements ExpressionOwner {
        int m_argIndex;

        ArgMultiOwner(int index) {
            this.m_argIndex = index;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return FunctionMultiArgs.this.m_args[this.m_argIndex];
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(FunctionMultiArgs.this);
            FunctionMultiArgs.this.m_args[this.m_argIndex] = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void callArgVisitors(XPathVisitor visitor) {
        super.callArgVisitors(visitor);
        if (null != this.m_args) {
            int n2 = this.m_args.length;
            for (int i2 = 0; i2 < n2; i2++) {
                this.m_args[i2].callVisitors(new ArgMultiOwner(i2), visitor);
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!super.deepEquals(expr)) {
            return false;
        }
        FunctionMultiArgs fma = (FunctionMultiArgs) expr;
        if (null != this.m_args) {
            int n2 = this.m_args.length;
            if (null == fma || fma.m_args.length != n2) {
                return false;
            }
            for (int i2 = 0; i2 < n2; i2++) {
                if (!this.m_args[i2].deepEquals(fma.m_args[i2])) {
                    return false;
                }
            }
            return true;
        }
        if (null != fma.m_args) {
            return false;
        }
        return true;
    }
}
