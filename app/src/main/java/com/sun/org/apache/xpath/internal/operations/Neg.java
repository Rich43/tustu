package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Neg.class */
public class Neg extends UnaryOperation {
    static final long serialVersionUID = -6280607702375702291L;

    @Override // com.sun.org.apache.xpath.internal.operations.UnaryOperation
    public XObject operate(XObject right) throws TransformerException {
        return new XNumber(-right.num());
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public double num(XPathContext xctxt) throws TransformerException {
        return -this.m_right.num(xctxt);
    }
}
