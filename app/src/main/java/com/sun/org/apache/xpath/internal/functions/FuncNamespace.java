package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncNamespace.class */
public class FuncNamespace extends FunctionDef1Arg {
    static final long serialVersionUID = -4695674566722321237L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        String s2;
        int context = getArg0AsNode(xctxt);
        if (context != -1) {
            DTM dtm = xctxt.getDTM(context);
            int t2 = dtm.getNodeType(context);
            if (t2 == 1) {
                s2 = dtm.getNamespaceURI(context);
            } else if (t2 == 2) {
                String s3 = dtm.getNodeName(context);
                if (s3.startsWith("xmlns:") || s3.equals("xmlns")) {
                    return XString.EMPTYSTRING;
                }
                s2 = dtm.getNamespaceURI(context);
            } else {
                return XString.EMPTYSTRING;
            }
            return null == s2 ? XString.EMPTYSTRING : new XString(s2);
        }
        return XString.EMPTYSTRING;
    }
}
