package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Mod.class */
public class Mod extends Operation {
    static final long serialVersionUID = 5009471154238918201L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation
    public XObject operate(XObject left, XObject right) throws TransformerException {
        return new XNumber(left.num() % right.num());
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public double num(XPathContext xctxt) throws TransformerException {
        return this.m_left.num(xctxt) % this.m_right.num(xctxt);
    }
}
