package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionNode;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.ExtensionsProvider;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.objects.XNull;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.res.XPATHMessages;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncExtFunction.class */
public class FuncExtFunction extends Function {
    static final long serialVersionUID = 5196115554693708718L;
    String m_namespace;
    String m_extensionName;
    Object m_methodKey;
    Vector m_argVec = new Vector();

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        if (null != this.m_argVec) {
            int nArgs = this.m_argVec.size();
            for (int i2 = 0; i2 < nArgs; i2++) {
                Expression arg = (Expression) this.m_argVec.elementAt(i2);
                arg.fixupVariables(vars, globalsSize);
            }
        }
    }

    public String getNamespace() {
        return this.m_namespace;
    }

    public String getFunctionName() {
        return this.m_extensionName;
    }

    public Object getMethodKey() {
        return this.m_methodKey;
    }

    public Expression getArg(int n2) {
        if (n2 >= 0 && n2 < this.m_argVec.size()) {
            return (Expression) this.m_argVec.elementAt(n2);
        }
        return null;
    }

    public int getArgCount() {
        return this.m_argVec.size();
    }

    public FuncExtFunction(String namespace, String extensionName, Object methodKey) {
        this.m_namespace = namespace;
        this.m_extensionName = extensionName;
        this.m_methodKey = methodKey;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        XObject result;
        if (xctxt.isSecureProcessing()) {
            throw new TransformerException(XPATHMessages.createXPATHMessage("ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED", new Object[]{toString()}));
        }
        Vector argVec = new Vector();
        int nArgs = this.m_argVec.size();
        for (int i2 = 0; i2 < nArgs; i2++) {
            Expression arg = (Expression) this.m_argVec.elementAt(i2);
            XObject xobj = arg.execute(xctxt);
            xobj.allowDetachToRelease(false);
            argVec.addElement(xobj);
        }
        ExtensionsProvider extProvider = (ExtensionsProvider) xctxt.getOwnerObject();
        Object val = extProvider.extFunction(this, argVec);
        if (null != val) {
            result = XObject.create(val, xctxt);
        } else {
            result = new XNull();
        }
        return result;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {
        this.m_argVec.addElement(arg);
        arg.exprSetParent(this);
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncExtFunction$ArgExtOwner.class */
    class ArgExtOwner implements ExpressionOwner {
        Expression m_exp;

        ArgExtOwner(Expression exp) {
            this.m_exp = exp;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public Expression getExpression() {
            return this.m_exp;
        }

        @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
        public void setExpression(Expression exp) {
            exp.exprSetParent(FuncExtFunction.this);
            this.m_exp = exp;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void callArgVisitors(XPathVisitor visitor) {
        for (int i2 = 0; i2 < this.m_argVec.size(); i2++) {
            Expression exp = (Expression) this.m_argVec.elementAt(i2);
            exp.callVisitors(new ArgExtOwner(exp), visitor);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.Expression, com.sun.org.apache.xpath.internal.ExpressionNode
    public void exprSetParent(ExpressionNode n2) {
        super.exprSetParent(n2);
        int nArgs = this.m_argVec.size();
        for (int i2 = 0; i2 < nArgs; i2++) {
            Expression arg = (Expression) this.m_argVec.elementAt(i2);
            arg.exprSetParent(n2);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{"Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called."});
        throw new RuntimeException(fMsg);
    }

    public String toString() {
        if (this.m_namespace != null && this.m_namespace.length() > 0) {
            return VectorFormat.DEFAULT_PREFIX + this.m_namespace + "}" + this.m_extensionName;
        }
        return this.m_extensionName;
    }
}
