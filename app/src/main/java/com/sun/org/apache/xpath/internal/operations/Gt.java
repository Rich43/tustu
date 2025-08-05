package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Gt.class */
public class Gt extends Operation {
    static final long serialVersionUID = 8927078751014375950L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation
    public XObject operate(XObject left, XObject right) throws TransformerException {
        return left.greaterThan(right) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}
