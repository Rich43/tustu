package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncGenerateId.class */
public class FuncGenerateId extends FunctionDef1Arg {
    static final long serialVersionUID = 973544842091724273L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        int which = getArg0AsNode(xctxt);
        if (-1 != which) {
            return new XString("N" + Integer.toHexString(which).toUpperCase());
        }
        return XString.EMPTYSTRING;
    }
}
