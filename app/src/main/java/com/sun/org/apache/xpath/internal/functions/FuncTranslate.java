package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncTranslate.class */
public class FuncTranslate extends Function3Args {
    static final long serialVersionUID = -1672834340026116482L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        String theFirstString = this.m_arg0.execute(xctxt).str();
        String theSecondString = this.m_arg1.execute(xctxt).str();
        String theThirdString = this.m_arg2.execute(xctxt).str();
        int theFirstStringLength = theFirstString.length();
        int theThirdStringLength = theThirdString.length();
        StringBuffer sbuffer = new StringBuffer();
        for (int i2 = 0; i2 < theFirstStringLength; i2++) {
            char theCurrentChar = theFirstString.charAt(i2);
            int theIndex = theSecondString.indexOf(theCurrentChar);
            if (theIndex < 0) {
                sbuffer.append(theCurrentChar);
            } else if (theIndex < theThirdStringLength) {
                sbuffer.append(theThirdString.charAt(theIndex));
            }
        }
        return new XString(sbuffer.toString());
    }
}
