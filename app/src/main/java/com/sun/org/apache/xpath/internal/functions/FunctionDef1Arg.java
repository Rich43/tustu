package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XString;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FunctionDef1Arg.class */
public class FunctionDef1Arg extends FunctionOneArg {
    static final long serialVersionUID = 2325189412814149264L;

    protected int getArg0AsNode(XPathContext xctxt) throws TransformerException {
        return null == this.m_arg0 ? xctxt.getCurrentNode() : this.m_arg0.asNode(xctxt);
    }

    public boolean Arg0IsNodesetExpr() {
        if (null == this.m_arg0) {
            return true;
        }
        return this.m_arg0.isNodesetExpr();
    }

    protected XMLString getArg0AsString(XPathContext xctxt) throws TransformerException {
        if (null == this.m_arg0) {
            int currentNode = xctxt.getCurrentNode();
            if (-1 == currentNode) {
                return XString.EMPTYSTRING;
            }
            DTM dtm = xctxt.getDTM(currentNode);
            return dtm.getStringValue(currentNode);
        }
        return this.m_arg0.execute(xctxt).xstr();
    }

    protected double getArg0AsNumber(XPathContext xctxt) throws TransformerException {
        if (null == this.m_arg0) {
            int currentNode = xctxt.getCurrentNode();
            if (-1 == currentNode) {
                return 0.0d;
            }
            DTM dtm = xctxt.getDTM(currentNode);
            XMLString str = dtm.getStringValue(currentNode);
            return str.toDouble();
        }
        return this.m_arg0.execute(xctxt).num();
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
        if (argNum > 1) {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_ZERO_OR_ONE", null));
    }

    @Override // com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.Expression
    public boolean canTraverseOutsideSubtree() {
        if (null == this.m_arg0) {
            return false;
        }
        return super.canTraverseOutsideSubtree();
    }
}
