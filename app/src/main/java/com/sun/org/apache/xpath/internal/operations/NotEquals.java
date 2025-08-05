package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/NotEquals.class */
public class NotEquals extends Operation {
    static final long serialVersionUID = -7869072863070586900L;

    @Override // com.sun.org.apache.xpath.internal.operations.Operation
    public XObject operate(XObject left, XObject right) throws TransformerException {
        return left.notEquals(right) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
}
