package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Equals.class */
public class Equals extends Operation {
    static final long serialVersionUID = -2658315633903426134L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation
    public XObject operate(XObject left, XObject right) throws TransformerException {
        return left.equals(right) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean bool(XPathContext xctxt) throws TransformerException {
        XObject left = this.m_left.execute(xctxt, true);
        XObject right = this.m_right.execute(xctxt, true);
        boolean result = left.equals(right);
        left.detach();
        right.detach();
        return result;
    }
}
