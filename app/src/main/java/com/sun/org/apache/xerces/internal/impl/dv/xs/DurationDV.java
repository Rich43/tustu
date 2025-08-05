package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.Duration;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/DurationDV.class */
public class DurationDV extends AbstractDateTimeDV {
    public static final int DURATION_TYPE = 0;
    public static final int YEARMONTHDURATION_TYPE = 1;
    public static final int DAYTIMEDURATION_TYPE = 2;
    private static final AbstractDateTimeDV.DateTimeData[] DATETIMES = {new AbstractDateTimeDV.DateTimeData(1696, 9, 1, 0, 0, 0.0d, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1697, 2, 1, 0, 0, 0.0d, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 3, 1, 0, 0, 0.0d, 90, null, true, null), new AbstractDateTimeDV.DateTimeData(1903, 7, 1, 0, 0, 0.0d, 90, null, true, null)};

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return parse(content, 0);
        } catch (Exception e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "duration"});
        }
    }

    protected AbstractDateTimeDV.DateTimeData parse(String str, int durationType) throws SchemaDateTimeException {
        int len = str.length();
        AbstractDateTimeDV.DateTimeData date = new AbstractDateTimeDV.DateTimeData(str, this);
        int start = 0 + 1;
        char c2 = str.charAt(0);
        if (c2 != 'P' && c2 != '-') {
            throw new SchemaDateTimeException();
        }
        date.utc = c2 == '-' ? 45 : 0;
        if (c2 == '-') {
            start++;
            if (str.charAt(start) != 'P') {
                throw new SchemaDateTimeException();
            }
        }
        int negate = 1;
        if (date.utc == 45) {
            negate = -1;
        }
        boolean designator = false;
        int endDate = indexOf(str, start, len, 'T');
        if (endDate == -1) {
            endDate = len;
        } else if (durationType == 1) {
            throw new SchemaDateTimeException();
        }
        int end = indexOf(str, start, endDate, 'Y');
        if (end != -1) {
            if (durationType == 2) {
                throw new SchemaDateTimeException();
            }
            date.year = negate * parseInt(str, start, end);
            start = end + 1;
            designator = true;
        }
        int end2 = indexOf(str, start, endDate, 'M');
        if (end2 != -1) {
            if (durationType == 2) {
                throw new SchemaDateTimeException();
            }
            date.month = negate * parseInt(str, start, end2);
            start = end2 + 1;
            designator = true;
        }
        int end3 = indexOf(str, start, endDate, 'D');
        if (end3 != -1) {
            if (durationType == 1) {
                throw new SchemaDateTimeException();
            }
            date.day = negate * parseInt(str, start, end3);
            start = end3 + 1;
            designator = true;
        }
        if (len == endDate && start != len) {
            throw new SchemaDateTimeException();
        }
        if (len != endDate) {
            int start2 = start + 1;
            int end4 = indexOf(str, start2, len, 'H');
            if (end4 != -1) {
                date.hour = negate * parseInt(str, start2, end4);
                start2 = end4 + 1;
                designator = true;
            }
            int end5 = indexOf(str, start2, len, 'M');
            if (end5 != -1) {
                date.minute = negate * parseInt(str, start2, end5);
                start2 = end5 + 1;
                designator = true;
            }
            int end6 = indexOf(str, start2, len, 'S');
            if (end6 != -1) {
                date.second = negate * parseSecond(str, start2, end6);
                start2 = end6 + 1;
                designator = true;
            }
            if (start2 != len || str.charAt(start2 - 1) == 'T') {
                throw new SchemaDateTimeException();
            }
        }
        if (!designator) {
            throw new SchemaDateTimeException();
        }
        return date;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected short compareDates(AbstractDateTimeDV.DateTimeData date1, AbstractDateTimeDV.DateTimeData date2, boolean strict) {
        if (compareOrder(date1, date2) == 0) {
            return (short) 0;
        }
        AbstractDateTimeDV.DateTimeData[] result = {new AbstractDateTimeDV.DateTimeData(null, this), new AbstractDateTimeDV.DateTimeData(null, this)};
        AbstractDateTimeDV.DateTimeData tempA = addDuration(date1, DATETIMES[0], result[0]);
        AbstractDateTimeDV.DateTimeData tempB = addDuration(date2, DATETIMES[0], result[1]);
        short resultA = compareOrder(tempA, tempB);
        if (resultA == 2) {
            return (short) 2;
        }
        AbstractDateTimeDV.DateTimeData tempA2 = addDuration(date1, DATETIMES[1], result[0]);
        AbstractDateTimeDV.DateTimeData tempB2 = addDuration(date2, DATETIMES[1], result[1]);
        short resultB = compareOrder(tempA2, tempB2);
        short resultA2 = compareResults(resultA, resultB, strict);
        if (resultA2 == 2) {
            return (short) 2;
        }
        AbstractDateTimeDV.DateTimeData tempA3 = addDuration(date1, DATETIMES[2], result[0]);
        AbstractDateTimeDV.DateTimeData tempB3 = addDuration(date2, DATETIMES[2], result[1]);
        short resultB2 = compareOrder(tempA3, tempB3);
        short resultA3 = compareResults(resultA2, resultB2, strict);
        if (resultA3 == 2) {
            return (short) 2;
        }
        AbstractDateTimeDV.DateTimeData tempA4 = addDuration(date1, DATETIMES[3], result[0]);
        AbstractDateTimeDV.DateTimeData tempB4 = addDuration(date2, DATETIMES[3], result[1]);
        short resultB3 = compareOrder(tempA4, tempB4);
        return compareResults(resultA3, resultB3, strict);
    }

    private short compareResults(short resultA, short resultB, boolean strict) {
        if (resultB == 2) {
            return (short) 2;
        }
        if (resultA != resultB && strict) {
            return (short) 2;
        }
        if (resultA != resultB && !strict) {
            if (resultA == 0 || resultB == 0) {
                return resultA != 0 ? resultA : resultB;
            }
            return (short) 2;
        }
        return resultA;
    }

    private AbstractDateTimeDV.DateTimeData addDuration(AbstractDateTimeDV.DateTimeData date, AbstractDateTimeDV.DateTimeData addto, AbstractDateTimeDV.DateTimeData duration) {
        int carry;
        resetDateObj(duration);
        int temp = addto.month + date.month;
        duration.month = modulo(temp, 1, 13);
        duration.year = addto.year + date.year + fQuotient(temp, 1, 13);
        double dtemp = addto.second + date.second;
        int carry2 = (int) Math.floor(dtemp / 60.0d);
        duration.second = dtemp - (carry2 * 60);
        int temp2 = addto.minute + date.minute + carry2;
        int carry3 = fQuotient(temp2, 60);
        duration.minute = mod(temp2, 60, carry3);
        int temp3 = addto.hour + date.hour + carry3;
        int carry4 = fQuotient(temp3, 24);
        duration.hour = mod(temp3, 24, carry4);
        duration.day = addto.day + date.day + carry4;
        while (true) {
            int temp4 = maxDayInMonthFor(duration.year, duration.month);
            if (duration.day < 1) {
                duration.day += maxDayInMonthFor(duration.year, duration.month - 1);
                carry = -1;
            } else if (duration.day > temp4) {
                duration.day -= temp4;
                carry = 1;
            } else {
                duration.utc = 90;
                return duration;
            }
            int temp5 = duration.month + carry;
            duration.month = modulo(temp5, 1, 13);
            duration.year += fQuotient(temp5, 1, 13);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected double parseSecond(String buffer, int start, int end) throws NumberFormatException {
        int dot = -1;
        for (int i2 = start; i2 < end; i2++) {
            char ch = buffer.charAt(i2);
            if (ch == '.') {
                dot = i2;
            } else if (ch > '9' || ch < '0') {
                throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
            }
        }
        if (dot + 1 == end) {
            throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
        }
        double value = Double.parseDouble(buffer.substring(start, end));
        if (value == Double.POSITIVE_INFINITY) {
            throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
        }
        return value;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected String dateToString(AbstractDateTimeDV.DateTimeData date) {
        StringBuffer message = new StringBuffer(30);
        if (date.year < 0 || date.month < 0 || date.day < 0 || date.hour < 0 || date.minute < 0 || date.second < 0.0d) {
            message.append('-');
        }
        message.append('P');
        message.append((date.year < 0 ? -1 : 1) * date.year);
        message.append('Y');
        message.append((date.month < 0 ? -1 : 1) * date.month);
        message.append('M');
        message.append((date.day < 0 ? -1 : 1) * date.day);
        message.append('D');
        message.append('T');
        message.append((date.hour < 0 ? -1 : 1) * date.hour);
        message.append('H');
        message.append((date.minute < 0 ? -1 : 1) * date.minute);
        message.append('M');
        append2(message, (date.second < 0.0d ? -1 : 1) * date.second);
        message.append('S');
        return message.toString();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.AbstractDateTimeDV
    protected Duration getDuration(AbstractDateTimeDV.DateTimeData date) {
        int sign = 1;
        if (date.year < 0 || date.month < 0 || date.day < 0 || date.hour < 0 || date.minute < 0 || date.second < 0.0d) {
            sign = -1;
        }
        return datatypeFactory.newDuration(sign == 1, date.year != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.year) : null, date.month != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.month) : null, date.day != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.day) : null, date.hour != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.hour) : null, date.minute != Integer.MIN_VALUE ? BigInteger.valueOf(sign * date.minute) : null, date.second != -2.147483648E9d ? new BigDecimal(String.valueOf(sign * date.second)) : null);
    }
}
