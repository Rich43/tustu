package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Or.class */
public class Or extends Operation {
    static final long serialVersionUID = -644107191353853079L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        XObject expr1 = this.m_left.execute(xctxt);
        if (!expr1.bool()) {
            XObject expr2 = this.m_right.execute(xctxt);
            return expr2.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
        }
        return XBoolean.S_TRUE;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean bool(XPathContext xctxt) throws TransformerException {
        return this.m_left.bool(xctxt) || this.m_right.bool(xctxt);
    }
}
