package com.sun.org.apache.xerces.internal.impl.dv;

import com.sun.org.apache.xerces.internal.xs.ShortList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/ValidatedInfo.class */
public class ValidatedInfo {
    public String normalizedValue;
    public Object actualValue;
    public short actualValueType;
    public XSSimpleType memberType;
    public XSSimpleType[] memberTypes;
    public ShortList itemValueTypes;

    public void reset() {
        this.normalizedValue = null;
        this.actualValue = null;
        this.memberType = null;
        this.memberTypes = null;
    }

    public String stringValue() {
        if (this.actualValue == null) {
            return this.normalizedValue;
        }
        return this.actualValue.toString();
    }
}
