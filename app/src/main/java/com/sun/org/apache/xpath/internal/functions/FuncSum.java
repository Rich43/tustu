package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncSum.class */
public class FuncSum extends FunctionOneArg {
    static final long serialVersionUID = -2719049259574677519L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        DTMIterator nodes = this.m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
        double sum = 0.0d;
        while (true) {
            int pos = nodes.nextNode();
            if (-1 != pos) {
                DTM dtm = nodes.getDTM(pos);
                XMLString s2 = dtm.getStringValue(pos);
                if (null != s2) {
                    sum += s2.toDouble();
                }
            } else {
                nodes.detach();
                return new XNumber(sum);
            }
        }
    }
}
