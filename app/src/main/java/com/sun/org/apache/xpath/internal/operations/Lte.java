package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Lte.class */
public class Lte extends Operation {
    static final long serialVersionUID = 6945650810527140228L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation
    public XObject operate(XObject left, XObject right) throws TransformerException {
        return left.lessThanOrEqual(right) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}
