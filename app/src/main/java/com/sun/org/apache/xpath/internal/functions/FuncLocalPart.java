package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncLocalPart.class */
public class FuncLocalPart extends FunctionDef1Arg {
    static final long serialVersionUID = 7591798770325814746L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        int context = getArg0AsNode(xctxt);
        if (-1 == context) {
            return XString.EMPTYSTRING;
        }
        DTM dtm = xctxt.getDTM(context);
        String s2 = context != -1 ? dtm.getLocalName(context) : "";
        if (s2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) || s2.equals("xmlns")) {
            return XString.EMPTYSTRING;
        }
        return new XString(s2);
    }
}
