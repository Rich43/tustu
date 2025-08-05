package com.sun.org.apache.xerces.internal.impl.dv.dtd;

import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/dtd/ENTITYDatatypeValidator.class */
public class ENTITYDatatypeValidator implements DatatypeValidator {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator
    public void validate(String content, ValidationContext context) throws InvalidDatatypeValueException {
        if (!context.isEntityUnparsed(content)) {
            throw new InvalidDatatypeValueException("ENTITYNotUnparsed", new Object[]{content});
        }
    }
}
