package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/TimeDV.class */
public class TimeDV extends AbstractDateTimeDV {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return parse(content);
        } catch (Exception e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_TIME});
        }
    }

    protected AbstractDateTimeDV.DateTimeData parse(String str) throws SchemaDateTimeException {
        AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
        int len = str.length();
        date.year = 2000;
        date.month = 1;
        date.day = 15;
        getTime(str, 0, len, date);
        validateDateTime(date);
        saveUnnormalized(date);
        if (date.utc != 0 && date.utc != 90) {
            normalize(date);
        }
        date.position = 2;
        return date;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
        StringBuffer message = new StringBuffer(16);
        append(message, date.hour, 2);
        message.append(':');
        append(message, date.minute, 2);
        message.append(':');
        append(message, date.second);
        append(message, (char) date.utc, 0);
        return message.toString();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData date) {
        return datatypeFactory.newXMLGregorianCalendar((BigInteger) null, Integer.MIN_VALUE, Integer.MIN_VALUE, date.unNormHour, date.unNormMinute, (int) date.unNormSecond, date.unNormSecond != 0.0d ? getFractionalSecondsAsBigDecimal(date) : null, date.hasTimeZone() ? (date.timezoneHr * 60) + date.timezoneMin : Integer.MIN_VALUE);
    }
}
