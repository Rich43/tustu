package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncCount.class */
public class FuncCount extends FunctionOneArg {
    static final long serialVersionUID = -7116225100474153751L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        DTMIterator nl = this.m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
        int i2 = nl.getLength();
        nl.detach();
        return new XNumber(i2);
    }
}
