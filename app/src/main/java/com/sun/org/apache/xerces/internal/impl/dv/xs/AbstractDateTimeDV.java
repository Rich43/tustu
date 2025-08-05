package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;
import com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime;
import java.math.BigDecimal;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV.class */
public abstract class AbstractDateTimeDV extends TypeValidator {
    private static final boolean DEBUG = false;
    protected static final int YEAR = 2000;
    protected static final int MONTH = 1;
    protected static final int DAY = 1;
    protected static final DatatypeFactory datatypeFactory = new DatatypeFactoryImpl();

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 2552;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public boolean isIdentical(Object value1, Object value2) {
        if (!(value1 instanceof DateTimeData) || !(value2 instanceof DateTimeData)) {
            return false;
        }
        DateTimeData v1 = (DateTimeData) value1;
        DateTimeData v2 = (DateTimeData) value2;
        if (v1.timezoneHr == v2.timezoneHr && v1.timezoneMin == v2.timezoneMin) {
            return v1.equals(v2);
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int compare(Object value1, Object value2) {
        return compareDates((DateTimeData) value1, (DateTimeData) value2, true);
    }

    protected short compareDates(DateTimeData date1, DateTimeData date2, boolean strict) {
        if (date1.utc == date2.utc) {
            return compareOrder(date1, date2);
        }
        DateTimeData tempDate = new DateTimeData(null, this);
        if (date1.utc == 90) {
            cloneDate(date2, tempDate);
            tempDate.timezoneHr = 14;
            tempDate.timezoneMin = 0;
            tempDate.utc = 43;
            normalize(tempDate);
            short c1 = compareOrder(date1, tempDate);
            if (c1 == -1) {
                return c1;
            }
            cloneDate(date2, tempDate);
            tempDate.timezoneHr = -14;
            tempDate.timezoneMin = 0;
            tempDate.utc = 45;
            normalize(tempDate);
            short c2 = compareOrder(date1, tempDate);
            if (c2 == 1) {
                return c2;
            }
            return (short) 2;
        }
        if (date2.utc == 90) {
            cloneDate(date1, tempDate);
            tempDate.timezoneHr = -14;
            tempDate.timezoneMin = 0;
            tempDate.utc = 45;
            normalize(tempDate);
            short c12 = compareOrder(tempDate, date2);
            if (c12 == -1) {
                return c12;
            }
            cloneDate(date1, tempDate);
            tempDate.timezoneHr = 14;
            tempDate.timezoneMin = 0;
            tempDate.utc = 43;
            normalize(tempDate);
            short c22 = compareOrder(tempDate, date2);
            if (c22 == 1) {
                return c22;
            }
            return (short) 2;
        }
        return (short) 2;
    }

    protected short compareOrder(DateTimeData date1, DateTimeData date2) {
        if (date1.position < 1) {
            if (date1.year < date2.year) {
                return (short) -1;
            }
            if (date1.year > date2.year) {
                return (short) 1;
            }
        }
        if (date1.position < 2) {
            if (date1.month < date2.month) {
                return (short) -1;
            }
            if (date1.month > date2.month) {
                return (short) 1;
            }
        }
        if (date1.day < date2.day) {
            return (short) -1;
        }
        if (date1.day > date2.day) {
            return (short) 1;
        }
        if (date1.hour < date2.hour) {
            return (short) -1;
        }
        if (date1.hour > date2.hour) {
            return (short) 1;
        }
        if (date1.minute < date2.minute) {
            return (short) -1;
        }
        if (date1.minute > date2.minute) {
            return (short) 1;
        }
        if (date1.second < date2.second) {
            return (short) -1;
        }
        if (date1.second > date2.second) {
            return (short) 1;
        }
        if (date1.utc < date2.utc) {
            return (short) -1;
        }
        if (date1.utc > date2.utc) {
            return (short) 1;
        }
        return (short) 0;
    }

    protected void getTime(String buffer, int start, int end, DateTimeData data) throws RuntimeException {
        int stop = start + 2;
        data.hour = parseInt(buffer, start, stop);
        int stop2 = stop + 1;
        if (buffer.charAt(stop) != ':') {
            throw new RuntimeException("Error in parsing time zone");
        }
        int stop3 = stop2 + 2;
        data.minute = parseInt(buffer, stop2, stop3);
        int stop4 = stop3 + 1;
        if (buffer.charAt(stop3) != ':') {
            throw new RuntimeException("Error in parsing time zone");
        }
        int sign = findUTCSign(buffer, stop2, end);
        data.second = parseSecond(buffer, stop4, sign < 0 ? end : sign);
        if (sign > 0) {
            getTimeZone(buffer, data, sign, end);
        }
    }

    protected int getDate(String buffer, int start, int end, DateTimeData date) throws RuntimeException {
        int start2 = getYearMonth(buffer, start, end, date);
        int start3 = start2 + 1;
        if (buffer.charAt(start2) != '-') {
            throw new RuntimeException("CCYY-MM must be followed by '-' sign");
        }
        int stop = start3 + 2;
        date.day = parseInt(buffer, start3, stop);
        return stop;
    }

    protected int getYearMonth(String buffer, int start, int end, DateTimeData date) throws RuntimeException {
        if (buffer.charAt(0) == '-') {
            start++;
        }
        int i2 = indexOf(buffer, start, end, '-');
        if (i2 == -1) {
            throw new RuntimeException("Year separator is missing or misplaced");
        }
        int length = i2 - start;
        if (length < 4) {
            throw new RuntimeException("Year must have 'CCYY' format");
        }
        if (length > 4 && buffer.charAt(start) == '0') {
            throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
        }
        date.year = parseIntYear(buffer, i2);
        if (buffer.charAt(i2) != '-') {
            throw new RuntimeException("CCYY must be followed by '-' sign");
        }
        int i3 = i2 + 1;
        int i4 = i3 + 2;
        date.month = parseInt(buffer, i3, i4);
        return i4;
    }

    protected void parseTimeZone(String buffer, int start, int end, DateTimeData date) throws RuntimeException {
        if (start < end) {
            if (!isNextCharUTCSign(buffer, start, end)) {
                throw new RuntimeException("Error in month parsing");
            }
            getTimeZone(buffer, date, start, end);
        }
    }

    protected void getTimeZone(String buffer, DateTimeData data, int sign, int end) throws RuntimeException {
        data.utc = buffer.charAt(sign);
        if (buffer.charAt(sign) == 'Z') {
            if (end > sign + 1) {
                throw new RuntimeException("Error in parsing time zone");
            }
            return;
        }
        if (sign <= end - 6) {
            int negate = buffer.charAt(sign) == '-' ? -1 : 1;
            int sign2 = sign + 1;
            int stop = sign2 + 2;
            data.timezoneHr = negate * parseInt(buffer, sign2, stop);
            int stop2 = stop + 1;
            if (buffer.charAt(stop) != ':') {
                throw new RuntimeException("Error in parsing time zone");
            }
            data.timezoneMin = negate * parseInt(buffer, stop2, stop2 + 2);
            if (stop2 + 2 != end) {
                throw new RuntimeException("Error in parsing time zone");
            }
            if (data.timezoneHr != 0 || data.timezoneMin != 0) {
                data.normalized = false;
                return;
            }
            return;
        }
        throw new RuntimeException("Error in parsing time zone");
    }

    protected int indexOf(String buffer, int start, int end, char ch) {
        for (int i2 = start; i2 < end; i2++) {
            if (buffer.charAt(i2) == ch) {
                return i2;
            }
        }
        return -1;
    }

    protected void validateDateTime(DateTimeData data) {
        if (data.year == 0) {
            throw new RuntimeException("The year \"0000\" is an illegal year value");
        }
        if (data.month < 1 || data.month > 12) {
            throw new RuntimeException("The month must have values 1 to 12");
        }
        if (data.day > maxDayInMonthFor(data.year, data.month) || data.day < 1) {
            throw new RuntimeException("The day must have values 1 to 31");
        }
        if (data.hour > 23 || data.hour < 0) {
            if (data.hour == 24 && data.minute == 0 && data.second == 0.0d) {
                data.hour = 0;
                int i2 = data.day + 1;
                data.day = i2;
                if (i2 > maxDayInMonthFor(data.year, data.month)) {
                    data.day = 1;
                    int i3 = data.month + 1;
                    data.month = i3;
                    if (i3 > 12) {
                        data.month = 1;
                        int i4 = data.year + 1;
                        data.year = i4;
                        if (i4 == 0) {
                            data.year = 1;
                        }
                    }
                }
            } else {
                throw new RuntimeException("Hour must have values 0-23, unless 24:00:00");
            }
        }
        if (data.minute > 59 || data.minute < 0) {
            throw new RuntimeException("Minute must have values 0-59");
        }
        if (data.second >= 60.0d || data.second < 0.0d) {
            throw new RuntimeException("Second must have values 0-59");
        }
        if (data.timezoneHr > 14 || data.timezoneHr < -14) {
            throw new RuntimeException("Time zone should have range -14:00 to +14:00");
        }
        if ((data.timezoneHr == 14 || data.timezoneHr == -14) && data.timezoneMin != 0) {
            throw new RuntimeException("Time zone should have range -14:00 to +14:00");
        }
        if (data.timezoneMin > 59 || data.timezoneMin < -59) {
            throw new RuntimeException("Minute must have values 0-59");
        }
    }

    protected int findUTCSign(String buffer, int start, int end) {
        for (int i2 = start; i2 < end; i2++) {
            int c2 = buffer.charAt(i2);
            if (c2 == 90 || c2 == 43 || c2 == 45) {
                return i2;
            }
        }
        return -1;
    }

    protected final boolean isNextCharUTCSign(String buffer, int start, int end) {
        if (start < end) {
            char c2 = buffer.charAt(start);
            return c2 == 'Z' || c2 == '+' || c2 == '-';
        }
        return false;
    }

    protected int parseInt(String buffer, int start, int end) throws NumberFormatException {
        int result = 0;
        int multmin = (-2147483647) / 10;
        int i2 = start;
        do {
            int digit = getDigit(buffer.charAt(i2));
            if (digit < 0) {
                throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
            }
            if (result >= multmin) {
                int result2 = result * 10;
                if (result2 < (-2147483647) + digit) {
                    throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
                }
                result = result2 - digit;
                i2++;
            } else {
                throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
            }
        } while (i2 < end);
        return -result;
    }

    protected int parseIntYear(String buffer, int end) {
        int limit;
        int result = 0;
        boolean negative = false;
        int i2 = 0;
        if (buffer.charAt(0) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i2 = 0 + 1;
        } else {
            limit = -2147483647;
        }
        int multmin = limit / 10;
        while (i2 < end) {
            int i3 = i2;
            i2++;
            int digit = getDigit(buffer.charAt(i3));
            if (digit < 0) {
                throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
            }
            if (result >= multmin) {
                int result2 = result * 10;
                if (result2 < limit + digit) {
                    throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
                }
                result = result2 - digit;
            } else {
                throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
            }
        }
        if (negative) {
            if (i2 > 1) {
                return result;
            }
            throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
        }
        return -result;
    }

    protected void normalize(DateTimeData date) {
        int carry;
        int temp = date.minute + ((-1) * date.timezoneMin);
        int carry2 = fQuotient(temp, 60);
        date.minute = mod(temp, 60, carry2);
        int temp2 = date.hour + ((-1) * date.timezoneHr) + carry2;
        int carry3 = fQuotient(temp2, 24);
        date.hour = mod(temp2, 24, carry3);
        date.day += carry3;
        while (true) {
            int temp3 = maxDayInMonthFor(date.year, date.month);
            if (date.day < 1) {
                date.day += maxDayInMonthFor(date.year, date.month - 1);
                carry = -1;
            } else if (date.day > temp3) {
                date.day -= temp3;
                carry = 1;
            } else {
                date.utc = 90;
                return;
            }
            int temp4 = date.month + carry;
            date.month = modulo(temp4, 1, 13);
            date.year += fQuotient(temp4, 1, 13);
            if (date.year == 0) {
                date.year = (date.timezoneHr < 0 || date.timezoneMin < 0) ? 1 : -1;
            }
        }
    }

    protected void saveUnnormalized(DateTimeData date) {
        date.unNormYear = date.year;
        date.unNormMonth = date.month;
        date.unNormDay = date.day;
        date.unNormHour = date.hour;
        date.unNormMinute = date.minute;
        date.unNormSecond = date.second;
    }

    protected void resetDateObj(DateTimeData data) {
        data.year = 0;
        data.month = 0;
        data.day = 0;
        data.hour = 0;
        data.minute = 0;
        data.second = 0.0d;
        data.utc = 0;
        data.timezoneHr = 0;
        data.timezoneMin = 0;
    }

    protected int maxDayInMonthFor(int year, int month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            }
            return 28;
        }
        return 31;
    }

    private boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    protected int mod(int a2, int b2, int quotient) {
        return a2 - (quotient * b2);
    }

    protected int fQuotient(int a2, int b2) {
        return (int) Math.floor(a2 / b2);
    }

    protected int modulo(int temp, int low, int high) {
        int a2 = temp - low;
        int b2 = high - low;
        return mod(a2, b2, fQuotient(a2, b2)) + low;
    }

    protected int fQuotient(int temp, int low, int high) {
        return fQuotient(temp - low, high - low);
    }

    protected String dateToString(DateTimeData date) {
        StringBuffer message = new StringBuffer(25);
        append(message, date.year, 4);
        message.append('-');
        append(message, date.month, 2);
        message.append('-');
        append(message, date.day, 2);
        message.append('T');
        append(message, date.hour, 2);
        message.append(':');
        append(message, date.minute, 2);
        message.append(':');
        append(message, date.second);
        append(message, (char) date.utc, 0);
        return message.toString();
    }

    protected final void append(StringBuffer message, int value, int nch) {
        if (value == Integer.MIN_VALUE) {
            message.append(value);
            return;
        }
        if (value < 0) {
            message.append('-');
            value = -value;
        }
        if (nch == 4) {
            if (value < 10) {
                message.append("000");
            } else if (value < 100) {
                message.append("00");
            } else if (value < 1000) {
                message.append('0');
            }
            message.append(value);
            return;
        }
        if (nch == 2) {
            if (value < 10) {
                message.append('0');
            }
            message.append(value);
        } else if (value != 0) {
            message.append((char) value);
        }
    }

    protected final void append(StringBuffer message, double value) {
        if (value < 0.0d) {
            message.append('-');
            value = -value;
        }
        if (value < 10.0d) {
            message.append('0');
        }
        append2(message, value);
    }

    protected final void append2(StringBuffer message, double value) {
        int intValue = (int) value;
        if (value == intValue) {
            message.append(intValue);
        } else {
            append3(message, value);
        }
    }

    private void append3(StringBuffer message, double value) {
        String d2 = String.valueOf(value);
        int eIndex = d2.indexOf(69);
        if (eIndex == -1) {
            message.append(d2);
            return;
        }
        if (value < 1.0d) {
            try {
                int exp = parseInt(d2, eIndex + 2, d2.length());
                message.append("0.");
                for (int i2 = 1; i2 < exp; i2++) {
                    message.append('0');
                }
                int end = eIndex - 1;
                while (end > 0 && d2.charAt(end) == '0') {
                    end--;
                }
                for (int i3 = 0; i3 <= end; i3++) {
                    char c2 = d2.charAt(i3);
                    if (c2 != '.') {
                        message.append(c2);
                    }
                }
                return;
            } catch (Exception e2) {
                message.append(d2);
                return;
            }
        }
        try {
            int exp2 = parseInt(d2, eIndex + 1, d2.length());
            int integerEnd = exp2 + 2;
            for (int i4 = 0; i4 < eIndex; i4++) {
                char c3 = d2.charAt(i4);
                if (c3 != '.') {
                    if (i4 == integerEnd) {
                        message.append('.');
                    }
                    message.append(c3);
                }
            }
            for (int i5 = integerEnd - eIndex; i5 > 0; i5--) {
                message.append('0');
            }
        } catch (Exception e3) {
            message.append(d2);
        }
    }

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
        if (dot == -1) {
            if (start + 2 != end) {
                throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
            }
        } else if (start + 2 != dot || dot + 1 == end) {
            throw new NumberFormatException(PdfOps.SINGLE_QUOTE_TOKEN + buffer + "' has wrong format");
        }
        return Double.parseDouble(buffer.substring(start, end));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cloneDate(DateTimeData finalValue, DateTimeData tempDate) {
        tempDate.year = finalValue.year;
        tempDate.month = finalValue.month;
        tempDate.day = finalValue.day;
        tempDate.hour = finalValue.hour;
        tempDate.minute = finalValue.minute;
        tempDate.second = finalValue.second;
        tempDate.utc = finalValue.utc;
        tempDate.timezoneHr = finalValue.timezoneHr;
        tempDate.timezoneMin = finalValue.timezoneMin;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/AbstractDateTimeDV$DateTimeData.class */
    static final class DateTimeData implements XSDateTime {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int utc;
        double second;
        int timezoneHr;
        int timezoneMin;
        private String originalValue;
        boolean normalized = true;
        int unNormYear;
        int unNormMonth;
        int unNormDay;
        int unNormHour;
        int unNormMinute;
        double unNormSecond;
        int position;
        final AbstractDateTimeDV type;
        private volatile String canonical;

        public DateTimeData(String originalValue, AbstractDateTimeDV type) {
            this.originalValue = originalValue;
            this.type = type;
        }

        public DateTimeData(int year, int month, int day, int hour, int minute, double second, int utc, String originalValue, boolean normalized, AbstractDateTimeDV type) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.utc = utc;
            this.type = type;
            this.originalValue = originalValue;
        }

        public boolean equals(Object obj) {
            return (obj instanceof DateTimeData) && this.type.compareDates(this, (DateTimeData) obj, true) == 0;
        }

        public int hashCode() {
            DateTimeData tempDate = new DateTimeData(null, this.type);
            this.type.cloneDate(this, tempDate);
            this.type.normalize(tempDate);
            return this.type.dateToString(tempDate).hashCode();
        }

        public String toString() {
            if (this.canonical == null) {
                this.canonical = this.type.dateToString(this);
            }
            return this.canonical;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getYears() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.year : this.unNormYear;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getMonths() {
            if (this.type instanceof DurationDV) {
                return (this.year * 12) + this.month;
            }
            return this.normalized ? this.month : this.unNormMonth;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getDays() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.day : this.unNormDay;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getHours() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.hour : this.unNormHour;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getMinutes() {
            if (this.type instanceof DurationDV) {
                return 0;
            }
            return this.normalized ? this.minute : this.unNormMinute;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public double getSeconds() {
            if (this.type instanceof DurationDV) {
                return (this.day * 24 * 60 * 60) + (this.hour * 60 * 60) + (this.minute * 60) + this.second;
            }
            return this.normalized ? this.second : this.unNormSecond;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public boolean hasTimeZone() {
            return this.utc != 0;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getTimeZoneHours() {
            return this.timezoneHr;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public int getTimeZoneMinutes() {
            return this.timezoneMin;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public String getLexicalValue() {
            return this.originalValue;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public XSDateTime normalize() {
            if (!this.normalized) {
                DateTimeData dt = (DateTimeData) clone();
                dt.normalized = true;
                return dt;
            }
            return this;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public boolean isNormalized() {
            return this.normalized;
        }

        public Object clone() {
            DateTimeData dt = new DateTimeData(this.year, this.month, this.day, this.hour, this.minute, this.second, this.utc, this.originalValue, this.normalized, this.type);
            dt.canonical = this.canonical;
            dt.position = this.position;
            dt.timezoneHr = this.timezoneHr;
            dt.timezoneMin = this.timezoneMin;
            dt.unNormYear = this.unNormYear;
            dt.unNormMonth = this.unNormMonth;
            dt.unNormDay = this.unNormDay;
            dt.unNormHour = this.unNormHour;
            dt.unNormMinute = this.unNormMinute;
            dt.unNormSecond = this.unNormSecond;
            return dt;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public XMLGregorianCalendar getXMLGregorianCalendar() {
            return this.type.getXMLGregorianCalendar(this);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDateTime
        public Duration getDuration() {
            return this.type.getDuration(this);
        }
    }

    protected XMLGregorianCalendar getXMLGregorianCalendar(DateTimeData data) {
        return null;
    }

    protected Duration getDuration(DateTimeData data) {
        return null;
    }

    protected final BigDecimal getFractionalSecondsAsBigDecimal(DateTimeData data) {
        StringBuffer buf = new StringBuffer();
        append3(buf, data.unNormSecond);
        String value = buf.toString();
        int index = value.indexOf(46);
        if (index == -1) {
            return null;
        }
        BigDecimal _val = new BigDecimal(value.substring(index));
        if (_val.compareTo(BigDecimal.valueOf(0L)) == 0) {
            return null;
        }
        return _val;
    }
}
