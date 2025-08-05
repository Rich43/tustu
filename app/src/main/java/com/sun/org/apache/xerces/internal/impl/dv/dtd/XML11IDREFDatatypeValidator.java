package com.sun.org.apache.xerces.internal.impl.dv.dtd;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.util.XML11Char;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/dtd/XML11IDREFDatatypeValidator.class */
public class XML11IDREFDatatypeValidator extends IDREFDatatypeValidator {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.dtd.IDREFDatatypeValidator, com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator
    public void validate(String content, ValidationContext context) throws InvalidDatatypeValueException {
        if (context.useNamespaces()) {
            if (!XML11Char.isXML11ValidNCName(content)) {
                throw new InvalidDatatypeValueException("IDREFInvalidWithNamespaces", new Object[]{content});
            }
        } else if (!XML11Char.isXML11ValidName(content)) {
            throw new InvalidDatatypeValueException("IDREFInvalid", new Object[]{content});
        }
        context.addIdRef(content);
    }
}
