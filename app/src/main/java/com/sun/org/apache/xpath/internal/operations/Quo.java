package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Quo.class */
public class Quo extends Operation {
    static final long serialVersionUID = 693765299196169905L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation
    public XObject operate(XObject left, XObject right) throws TransformerException {
        return new XNumber((int) (left.num() / right.num()));
    }
}
