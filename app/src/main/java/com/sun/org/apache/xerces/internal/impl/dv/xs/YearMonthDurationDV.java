package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.Duration;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/YearMonthDurationDV.class */
class YearMonthDurationDV extends DurationDV {
    YearMonthDurationDV() {
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.DurationDV, com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return parse(content, 1);
        } catch (Exception e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "yearMonthDuration"});
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.DurationDV, com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected Duration getDuration(AbstractDateTimeDV.DateTimeData date) {
        int sign = 1;
        if (date.year < 0 || date.month < 0) {
            sign = -1;
        }
        return datatypeFactory.newDuration(sign == 1, date.year != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.year) : null, date.month != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.month) : null, (BigInteger) null, (BigInteger) null, (BigInteger) null, (BigDecimal) null);
    }
}
