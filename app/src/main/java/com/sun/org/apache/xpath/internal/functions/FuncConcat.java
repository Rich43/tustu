package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncConcat.class */
public class FuncConcat extends FunctionMultiArgs {
    static final long serialVersionUID = 1737228885202314413L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        StringBuffer sb = new StringBuffer();
        sb.append(this.m_arg0.execute(xctxt).str());
        sb.append(this.m_arg1.execute(xctxt).str());
        if (null != this.m_arg2) {
            sb.append(this.m_arg2.execute(xctxt).str());
        }
        if (null != this.m_args) {
            for (int i2 = 0; i2 < this.m_args.length; i2++) {
                sb.append(this.m_args[i2].execute(xctxt).str());
            }
        }
        return new XString(sb.toString());
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionMultiArgs, com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
        if (argNum < 2) {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionMultiArgs, com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("gtone", null));
    }
}
