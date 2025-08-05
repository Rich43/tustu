package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncLang.class */
public class FuncLang extends FunctionOneArg {
    static final long serialVersionUID = -7868705139354872185L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        int langAttr;
        int valLen;
        String lang = this.m_arg0.execute(xctxt).str();
        int parent = xctxt.getCurrentNode();
        boolean isLang = false;
        DTM dtm = xctxt.getDTM(parent);
        while (true) {
            if (-1 == parent) {
                break;
            }
            if (1 == dtm.getNodeType(parent) && -1 != (langAttr = dtm.getAttributeNode(parent, "http://www.w3.org/XML/1998/namespace", "lang"))) {
                String langVal = dtm.getNodeValue(langAttr);
                if (langVal.toLowerCase().startsWith(lang.toLowerCase()) && (langVal.length() == (valLen = lang.length()) || langVal.charAt(valLen) == '-')) {
                    isLang = true;
                }
            } else {
                parent = dtm.getParent(parent);
            }
        }
        return isLang ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}
