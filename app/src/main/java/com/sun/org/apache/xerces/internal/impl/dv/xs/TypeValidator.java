package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/TypeValidator.class */
public abstract class TypeValidator {
    public static final short LESS_THAN = -1;
    public static final short EQUAL = 0;
    public static final short GREATER_THAN = 1;
    public static final short INDETERMINATE = 2;

    public abstract short getAllowedFacets();

    public abstract Object getActualValue(String str, ValidationContext validationContext) throws InvalidDatatypeValueException;

    public void checkExtraRules(Object value, ValidationContext context) throws InvalidDatatypeValueException {
    }

    public boolean isIdentical(Object value1, Object value2) {
        return value1.equals(value2);
    }

    public int compare(Object value1, Object value2) {
        return -1;
    }

    public int getDataLength(Object value) {
        if (value instanceof String) {
            return ((String) value).length();
        }
        return -1;
    }

    public int getTotalDigits(Object value) {
        return -1;
    }

    public int getFractionDigits(Object value) {
        return -1;
    }

    public static final boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static final int getDigit(char ch) {
        if (isDigit(ch)) {
            return ch - '0';
        }
        return -1;
    }
}
