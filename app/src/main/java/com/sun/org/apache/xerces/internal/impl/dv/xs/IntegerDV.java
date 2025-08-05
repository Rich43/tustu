package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.DecimalDV;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/IntegerDV.class */
public class IntegerDV extends DecimalDV {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.DecimalDV, com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return new DecimalDV.XDecimal(content, true);
        } catch (NumberFormatException e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_INTEGER});
        }
    }
}
