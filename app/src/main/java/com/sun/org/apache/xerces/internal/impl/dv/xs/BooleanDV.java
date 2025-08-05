package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/BooleanDV.class */
public class BooleanDV extends TypeValidator {
    private static final String[] fValueSpace = {"false", "true", "0", "1"};

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 24;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        Boolean ret;
        if (content.equals(fValueSpace[0]) || content.equals(fValueSpace[2])) {
            ret = Boolean.FALSE;
        } else if (content.equals(fValueSpace[1]) || content.equals(fValueSpace[3])) {
            ret = Boolean.TRUE;
        } else {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "boolean"});
        }
        return ret;
    }
}
