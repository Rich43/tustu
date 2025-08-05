package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/MonthDayDV.class */
public class MonthDayDV extends AbstractDateTimeDV {
    private static final int MONTHDAY_SIZE = 7;

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return parse(content);
        } catch (Exception e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_MONTHDAY});
        }
    }

    protected AbstractDateTimeDV.DateTimeData parse(String str) throws SchemaDateTimeException {
        AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
        int len = str.length();
        date.year = 2000;
        if (str.charAt(0) != '-' || str.charAt(1) != '-') {
            throw new SchemaDateTimeException("Invalid format for gMonthDay: " + str);
        }
        date.month = parseInt(str, 2, 4);
        int start = 4 + 1;
        if (str.charAt(4) != '-') {
            throw new SchemaDateTimeException("Invalid format for gMonthDay: " + str);
        }
        date.day = parseInt(str, start, start + 2);
        if (7 < len) {
            if (!isNextCharUTCSign(str, 7, len)) {
                throw new SchemaDateTimeException("Error in month parsing:" + str);
            }
            getTimeZone(str, date, 7, len);
        }
        validateDateTime(date);
        saveUnnormalized(date);
        if (date.utc != 0 && date.utc != 90) {
            normalize(date);
        }
        date.position = 1;
        return date;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
        StringBuffer message = new StringBuffer(8);
        message.append('-');
        message.append('-');
        append(message, date.month, 2);
        message.append('-');
        append(message, date.day, 2);
        append(message, (char) date.utc, 0);
        return message.toString();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
        return datatypeFactory.newXMLGregorianCalendar(Integer.MIN_VALUE, date.unNormMonth, date.unNormDay, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, date.hasTimeZone() ? (date.timezoneHr * 60) + date.timezoneMin : Integer.MIN_VALUE);
    }
}
