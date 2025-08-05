package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncSubstring.class */
public class FuncSubstring extends Function3Args {
    static final long serialVersionUID = -5996676095024715502L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        double start;
        int startIndex;
        XMLString substr;
        XMLString s1 = this.m_arg0.execute(xctxt).xstr();
        double start2 = this.m_arg1.execute(xctxt).num();
        int lenOfS1 = s1.length();
        if (lenOfS1 <= 0) {
            return XString.EMPTYSTRING;
        }
        if (Double.isNaN(start2)) {
            start = -1000000.0d;
            startIndex = 0;
        } else {
            start = Math.round(start2);
            startIndex = start > 0.0d ? ((int) start) - 1 : 0;
        }
        if (null != this.m_arg2) {
            double len = this.m_arg2.num(xctxt);
            int end = ((int) (Math.round(len) + start)) - 1;
            if (end < 0) {
                end = 0;
            } else if (end > lenOfS1) {
                end = lenOfS1;
            }
            if (startIndex > lenOfS1) {
                startIndex = lenOfS1;
            }
            substr = s1.substring(startIndex, end);
        } else {
            if (startIndex > lenOfS1) {
                startIndex = lenOfS1;
            }
            substr = s1.substring(startIndex);
        }
        return (XString) substr;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
        if (argNum < 2) {
            reportWrongNumberArgs();
        }
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function3Args, com.sun.org.apache.xpath.internal.functions.Function2Args, com.sun.org.apache.xpath.internal.functions.FunctionOneArg, com.sun.org.apache.xpath.internal.functions.Function
    protected void reportWrongNumberArgs() throws WrongNumberArgsException {
        throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("ER_TWO_OR_THREE", null));
    }
}
