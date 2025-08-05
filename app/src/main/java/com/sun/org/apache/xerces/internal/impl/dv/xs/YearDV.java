package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/YearDV.class */
public class YearDV extends AbstractDateTimeDV {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return parse(content);
        } catch (Exception e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_YEAR});
        }
    }

    protected AbstractDateTimeDV.DateTimeData parse(String str) throws SchemaDateTimeException {
        AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
        int len = str.length();
        int start = 0;
        if (str.charAt(0) == '-') {
            start = 1;
        }
        int sign = findUTCSign(str, start, len);
        int length = (sign == -1 ? len : sign) - start;
        if (length < 4) {
            throw new RuntimeException("Year must have 'CCYY' format");
        }
        if (length > 4 && str.charAt(start) == '0') {
            throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
        }
        if (sign == -1) {
            date.year = parseIntYear(str, len);
        } else {
            date.year = parseIntYear(str, sign);
            getTimeZone(str, date, sign, len);
        }
        date.month = 1;
        date.day = 1;
        validateDateTime(date);
        saveUnnormalized(date);
        if (date.utc != 0 && date.utc != 90) {
            normalize(date);
        }
        date.position = 0;
        return date;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
        StringBuffer message = new StringBuffer(5);
        append(message, date.year, 4);
        append(message, (char) date.utc, 0);
        return message.toString();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
        return datatypeFactory.newXMLGregorianCalendar(date.unNormYear, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, date.hasTimeZone() ? (date.timezoneHr * 60) + date.timezoneMin : Integer.MIN_VALUE);
    }
}
