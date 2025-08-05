package com.sun.org.apache.xerces.internal.jaxp.datatype;

import com.sun.org.apache.xerces.internal.util.DatatypeMessageFormatter;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/datatype/XMLGregorianCalendarImpl.class */
public class XMLGregorianCalendarImpl extends XMLGregorianCalendar implements Serializable, Cloneable {
    private transient BigInteger orig_eon;
    private transient BigDecimal orig_fracSeconds;
    private static final int BILLION_I = 1000000000;
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int HOUR = 3;
    private static final int MINUTE = 4;
    private static final int SECOND = 5;
    private static final int MILLISECOND = 6;
    private static final int TIMEZONE = 7;
    private static final long serialVersionUID = 1;
    private static final BigInteger BILLION_B = new BigInteger("1000000000");
    private static final Date PURE_GREGORIAN_CHANGE = new Date(Long.MIN_VALUE);
    private static final String[] FIELD_NAME = {"Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond", "Timezone"};
    public static final XMLGregorianCalendar LEAP_YEAR_DEFAULT = createDateTime(400, 1, 1, 0, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final BigInteger FOUR = BigInteger.valueOf(4);
    private static final BigInteger HUNDRED = BigInteger.valueOf(100);
    private static final BigInteger FOUR_HUNDRED = BigInteger.valueOf(400);
    private static final BigInteger SIXTY = BigInteger.valueOf(60);
    private static final BigInteger TWENTY_FOUR = BigInteger.valueOf(24);
    private static final BigInteger TWELVE = BigInteger.valueOf(12);
    private static final BigDecimal DECIMAL_ZERO = BigDecimal.valueOf(0L);
    private static final BigDecimal DECIMAL_ONE = BigDecimal.valueOf(1L);
    private static final BigDecimal DECIMAL_SIXTY = BigDecimal.valueOf(60L);
    private transient int orig_year = Integer.MIN_VALUE;
    private transient int orig_month = Integer.MIN_VALUE;
    private transient int orig_day = Integer.MIN_VALUE;
    private transient int orig_hour = Integer.MIN_VALUE;
    private transient int orig_minute = Integer.MIN_VALUE;
    private transient int orig_second = Integer.MIN_VALUE;
    private transient int orig_timezone = Integer.MIN_VALUE;
    private BigInteger eon = null;
    private int year = Integer.MIN_VALUE;
    private int month = Integer.MIN_VALUE;
    private int day = Integer.MIN_VALUE;
    private int timezone = Integer.MIN_VALUE;
    private int hour = Integer.MIN_VALUE;
    private int minute = Integer.MIN_VALUE;
    private int second = Integer.MIN_VALUE;
    private BigDecimal fractionalSecond = null;

    protected XMLGregorianCalendarImpl(String lexicalRepresentation) throws IllegalArgumentException {
        String format;
        int lexRepLength = lexicalRepresentation.length();
        if (lexicalRepresentation.indexOf(84) != -1) {
            format = "%Y-%M-%DT%h:%m:%s%z";
        } else if (lexRepLength >= 3 && lexicalRepresentation.charAt(2) == ':') {
            format = "%h:%m:%s%z";
        } else if (lexicalRepresentation.startsWith("--")) {
            if (lexRepLength >= 3 && lexicalRepresentation.charAt(2) == '-') {
                format = "---%D%z";
            } else if (lexRepLength == 4 || lexRepLength == 5 || lexRepLength == 10) {
                format = "--%M%z";
            } else {
                format = "--%M-%D%z";
            }
        } else {
            int countSeparator = 0;
            int timezoneOffset = lexicalRepresentation.indexOf(58);
            lexRepLength = timezoneOffset != -1 ? lexRepLength - 6 : lexRepLength;
            for (int i2 = 1; i2 < lexRepLength; i2++) {
                if (lexicalRepresentation.charAt(i2) == '-') {
                    countSeparator++;
                }
            }
            if (countSeparator == 0) {
                format = "%Y%z";
            } else if (countSeparator == 1) {
                format = "%Y-%M%z";
            } else {
                format = "%Y-%M-%D%z";
            }
        }
        Parser p2 = new Parser(format, lexicalRepresentation);
        p2.parse();
        if (!isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCRepresentation", new Object[]{lexicalRepresentation}));
        }
        save();
    }

    private void save() {
        this.orig_eon = this.eon;
        this.orig_year = this.year;
        this.orig_month = this.month;
        this.orig_day = this.day;
        this.orig_hour = this.hour;
        this.orig_minute = this.minute;
        this.orig_second = this.second;
        this.orig_fracSeconds = this.fractionalSecond;
        this.orig_timezone = this.timezone;
    }

    public XMLGregorianCalendarImpl() {
    }

    protected XMLGregorianCalendarImpl(BigInteger year, int month, int day, int hour, int minute, int second, BigDecimal fractionalSecond, int timezone) {
        setYear(year);
        setMonth(month);
        setDay(day);
        setTime(hour, minute, second, fractionalSecond);
        setTimezone(timezone);
        if (!isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-fractional", new Object[]{year, new Integer(month), new Integer(day), new Integer(hour), new Integer(minute), new Integer(second), fractionalSecond, new Integer(timezone)}));
        }
        save();
    }

    private XMLGregorianCalendarImpl(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone) {
        setYear(year);
        setMonth(month);
        setDay(day);
        setTime(hour, minute, second);
        setTimezone(timezone);
        BigDecimal realMilliseconds = null;
        setFractionalSecond(millisecond != Integer.MIN_VALUE ? BigDecimal.valueOf(millisecond, 3) : realMilliseconds);
        if (!isValid()) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidXGCValue-milli", new Object[]{new Integer(year), new Integer(month), new Integer(day), new Integer(hour), new Integer(minute), new Integer(second), new Integer(millisecond), new Integer(timezone)}));
        }
        save();
    }

    public XMLGregorianCalendarImpl(GregorianCalendar cal) {
        int year = cal.get(1);
        setYear(cal.get(0) == 0 ? -year : year);
        setMonth(cal.get(2) + 1);
        setDay(cal.get(5));
        setTime(cal.get(11), cal.get(12), cal.get(13), cal.get(14));
        int offsetInMinutes = (cal.get(15) + cal.get(16)) / 60000;
        setTimezone(offsetInMinutes);
        save();
    }

    public static XMLGregorianCalendar createDateTime(BigInteger year, int month, int day, int hours, int minutes, int seconds, BigDecimal fractionalSecond, int timezone) {
        return new XMLGregorianCalendarImpl(year, month, day, hours, minutes, seconds, fractionalSecond, timezone);
    }

    public static XMLGregorianCalendar createDateTime(int year, int month, int day, int hour, int minute, int second) {
        return new XMLGregorianCalendarImpl(year, month, day, hour, minute, second, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static XMLGregorianCalendar createDateTime(int year, int month, int day, int hours, int minutes, int seconds, int milliseconds, int timezone) {
        return new XMLGregorianCalendarImpl(year, month, day, hours, minutes, seconds, milliseconds, timezone);
    }

    public static XMLGregorianCalendar createDate(int year, int month, int day, int timezone) {
        return new XMLGregorianCalendarImpl(year, month, day, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, timezone);
    }

    public static XMLGregorianCalendar createTime(int hours, int minutes, int seconds, int timezone) {
        return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, hours, minutes, seconds, Integer.MIN_VALUE, timezone);
    }

    public static XMLGregorianCalendar createTime(int hours, int minutes, int seconds, BigDecimal fractionalSecond, int timezone) {
        return new XMLGregorianCalendarImpl((BigInteger) null, Integer.MIN_VALUE, Integer.MIN_VALUE, hours, minutes, seconds, fractionalSecond, timezone);
    }

    public static XMLGregorianCalendar createTime(int hours, int minutes, int seconds, int milliseconds, int timezone) {
        return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, hours, minutes, seconds, milliseconds, timezone);
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public BigInteger getEon() {
        return this.eon;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getYear() {
        return this.year;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public BigInteger getEonAndYear() {
        if (this.year != Integer.MIN_VALUE && this.eon != null) {
            return this.eon.add(BigInteger.valueOf(this.year));
        }
        if (this.year != Integer.MIN_VALUE && this.eon == null) {
            return BigInteger.valueOf(this.year);
        }
        return null;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getMonth() {
        return this.month;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getDay() {
        return this.day;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getTimezone() {
        return this.timezone;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getHour() {
        return this.hour;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getMinute() {
        return this.minute;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getSecond() {
        return this.second;
    }

    private BigDecimal getSeconds() {
        if (this.second == Integer.MIN_VALUE) {
            return DECIMAL_ZERO;
        }
        BigDecimal result = BigDecimal.valueOf(this.second);
        if (this.fractionalSecond != null) {
            return result.add(this.fractionalSecond);
        }
        return result;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int getMillisecond() {
        if (this.fractionalSecond == null) {
            return Integer.MIN_VALUE;
        }
        return this.fractionalSecond.movePointRight(3).intValue();
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public BigDecimal getFractionalSecond() {
        return this.fractionalSecond;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setYear(BigInteger year) {
        if (year == null) {
            this.eon = null;
            this.year = Integer.MIN_VALUE;
        } else {
            BigInteger temp = year.remainder(BILLION_B);
            this.year = temp.intValue();
            setEon(year.subtract(temp));
        }
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setYear(int year) {
        if (year == Integer.MIN_VALUE) {
            this.year = Integer.MIN_VALUE;
            this.eon = null;
        } else if (Math.abs(year) < BILLION_I) {
            this.year = year;
            this.eon = null;
        } else {
            BigInteger theYear = BigInteger.valueOf(year);
            BigInteger remainder = theYear.remainder(BILLION_B);
            this.year = remainder.intValue();
            setEon(theYear.subtract(remainder));
        }
    }

    private void setEon(BigInteger eon) {
        if (eon != null && eon.compareTo(BigInteger.ZERO) == 0) {
            this.eon = null;
        } else {
            this.eon = eon;
        }
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setMonth(int month) {
        if ((month < 1 || 12 < month) && month != Integer.MIN_VALUE) {
            invalidFieldValue(1, month);
        }
        this.month = month;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setDay(int day) {
        if ((day < 1 || 31 < day) && day != Integer.MIN_VALUE) {
            invalidFieldValue(2, day);
        }
        this.day = day;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setTimezone(int offset) {
        if ((offset < -840 || 840 < offset) && offset != Integer.MIN_VALUE) {
            invalidFieldValue(7, offset);
        }
        this.timezone = offset;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setTime(int hour, int minute, int second) {
        setTime(hour, minute, second, (BigDecimal) null);
    }

    private void invalidFieldValue(int field, int value) {
        throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFieldValue", new Object[]{new Integer(value), FIELD_NAME[field]}));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void testHour() {
        if (getHour() == 24) {
            if (getMinute() != 0 || getSecond() != 0) {
                invalidFieldValue(3, getHour());
            }
            setHour(0, false);
            add(new DurationImpl(true, 0, 0, 1, 0, 0, 0));
        }
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setHour(int hour) {
        setHour(hour, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHour(int hour, boolean validate) {
        if ((hour < 0 || hour > 24) && hour != Integer.MIN_VALUE) {
            invalidFieldValue(3, hour);
        }
        this.hour = hour;
        if (validate) {
            testHour();
        }
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setMinute(int minute) {
        if ((minute < 0 || 59 < minute) && minute != Integer.MIN_VALUE) {
            invalidFieldValue(4, minute);
        }
        this.minute = minute;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setSecond(int second) {
        if ((second < 0 || 60 < second) && second != Integer.MIN_VALUE) {
            invalidFieldValue(5, second);
        }
        this.second = second;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setTime(int hour, int minute, int second, BigDecimal fractional) {
        setHour(hour, false);
        setMinute(minute);
        if (second != 60) {
            setSecond(second);
        } else if ((hour == 23 && minute == 59) || (hour == 0 && minute == 0)) {
            setSecond(second);
        } else {
            invalidFieldValue(5, second);
        }
        setFractionalSecond(fractional);
        testHour();
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setTime(int hour, int minute, int second, int millisecond) {
        setHour(hour, false);
        setMinute(minute);
        if (second != 60) {
            setSecond(second);
        } else if ((hour == 23 && minute == 59) || (hour == 0 && minute == 0)) {
            setSecond(second);
        } else {
            invalidFieldValue(5, second);
        }
        setMillisecond(millisecond);
        testHour();
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int compare(XMLGregorianCalendar rhs) {
        XMLGregorianCalendarImpl P2 = this;
        XMLGregorianCalendarImpl Q2 = (XMLGregorianCalendarImpl) rhs;
        if (P2.getTimezone() == Q2.getTimezone()) {
            return internalCompare(P2, Q2);
        }
        if (P2.getTimezone() != Integer.MIN_VALUE && Q2.getTimezone() != Integer.MIN_VALUE) {
            return internalCompare((XMLGregorianCalendarImpl) P2.normalize(), (XMLGregorianCalendarImpl) Q2.normalize());
        }
        if (P2.getTimezone() != Integer.MIN_VALUE) {
            if (P2.getTimezone() != 0) {
                P2 = (XMLGregorianCalendarImpl) P2.normalize();
            }
            XMLGregorianCalendar MinQ = Q2.normalizeToTimezone(DatatypeConstants.MIN_TIMEZONE_OFFSET);
            int result = internalCompare(P2, MinQ);
            if (result == -1) {
                return result;
            }
            XMLGregorianCalendar MaxQ = Q2.normalizeToTimezone(DatatypeConstants.MAX_TIMEZONE_OFFSET);
            int result2 = internalCompare(P2, MaxQ);
            if (result2 == 1) {
                return result2;
            }
            return 2;
        }
        if (Q2.getTimezone() != 0) {
            Q2 = (XMLGregorianCalendarImpl) Q2.normalizeToTimezone(Q2.getTimezone());
        }
        XMLGregorianCalendar MaxP = P2.normalizeToTimezone(DatatypeConstants.MAX_TIMEZONE_OFFSET);
        int result3 = internalCompare(MaxP, Q2);
        if (result3 == -1) {
            return result3;
        }
        XMLGregorianCalendar MinP = P2.normalizeToTimezone(DatatypeConstants.MIN_TIMEZONE_OFFSET);
        int result4 = internalCompare(MinP, Q2);
        if (result4 == 1) {
            return result4;
        }
        return 2;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public XMLGregorianCalendar normalize() {
        XMLGregorianCalendar normalized = normalizeToTimezone(this.timezone);
        if (getTimezone() == Integer.MIN_VALUE) {
            normalized.setTimezone(Integer.MIN_VALUE);
        }
        if (getMillisecond() == Integer.MIN_VALUE) {
            normalized.setMillisecond(Integer.MIN_VALUE);
        }
        return normalized;
    }

    private XMLGregorianCalendar normalizeToTimezone(int timezone) {
        XMLGregorianCalendar result = (XMLGregorianCalendar) clone();
        int minutes = -timezone;
        Duration d2 = new DurationImpl(minutes >= 0, 0, 0, 0, 0, minutes < 0 ? -minutes : minutes, 0);
        result.add(d2);
        result.setTimezone(0);
        return result;
    }

    private static int internalCompare(XMLGregorianCalendar P2, XMLGregorianCalendar Q2) {
        if (P2.getEon() == Q2.getEon()) {
            int result = compareField(P2.getYear(), Q2.getYear());
            if (result != 0) {
                return result;
            }
        } else {
            int result2 = compareField(P2.getEonAndYear(), Q2.getEonAndYear());
            if (result2 != 0) {
                return result2;
            }
        }
        int result3 = compareField(P2.getMonth(), Q2.getMonth());
        if (result3 != 0) {
            return result3;
        }
        int result4 = compareField(P2.getDay(), Q2.getDay());
        if (result4 != 0) {
            return result4;
        }
        int result5 = compareField(P2.getHour(), Q2.getHour());
        if (result5 != 0) {
            return result5;
        }
        int result6 = compareField(P2.getMinute(), Q2.getMinute());
        if (result6 != 0) {
            return result6;
        }
        int result7 = compareField(P2.getSecond(), Q2.getSecond());
        if (result7 != 0) {
            return result7;
        }
        return compareField(P2.getFractionalSecond(), Q2.getFractionalSecond());
    }

    private static int compareField(int Pfield, int Qfield) {
        if (Pfield == Qfield) {
            return 0;
        }
        if (Pfield == Integer.MIN_VALUE || Qfield == Integer.MIN_VALUE) {
            return 2;
        }
        return Pfield < Qfield ? -1 : 1;
    }

    private static int compareField(BigInteger Pfield, BigInteger Qfield) {
        if (Pfield == null) {
            return Qfield == null ? 0 : 2;
        }
        if (Qfield == null) {
            return 2;
        }
        return Pfield.compareTo(Qfield);
    }

    private static int compareField(BigDecimal Pfield, BigDecimal Qfield) {
        if (Pfield == Qfield) {
            return 0;
        }
        if (Pfield == null) {
            Pfield = DECIMAL_ZERO;
        }
        if (Qfield == null) {
            Qfield = DECIMAL_ZERO;
        }
        return Pfield.compareTo(Qfield);
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof XMLGregorianCalendar)) {
            return false;
        }
        return obj == this || compare((XMLGregorianCalendar) obj) == 0;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public int hashCode() {
        int timezone = getTimezone();
        if (timezone == Integer.MIN_VALUE) {
            timezone = 0;
        }
        XMLGregorianCalendar gc = this;
        if (timezone != 0) {
            gc = normalizeToTimezone(getTimezone());
        }
        return gc.getYear() + gc.getMonth() + gc.getDay() + gc.getHour() + gc.getMinute() + gc.getSecond();
    }

    public static XMLGregorianCalendar parse(String lexicalRepresentation) {
        return new XMLGregorianCalendarImpl(lexicalRepresentation);
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public String toXMLFormat() {
        QName typekind = getXMLSchemaType();
        String formatString = null;
        if (typekind == DatatypeConstants.DATETIME) {
            formatString = "%Y-%M-%DT%h:%m:%s%z";
        } else if (typekind == DatatypeConstants.DATE) {
            formatString = "%Y-%M-%D%z";
        } else if (typekind == DatatypeConstants.TIME) {
            formatString = "%h:%m:%s%z";
        } else if (typekind == DatatypeConstants.GMONTH) {
            formatString = "--%M%z";
        } else if (typekind == DatatypeConstants.GDAY) {
            formatString = "---%D%z";
        } else if (typekind == DatatypeConstants.GYEAR) {
            formatString = "%Y%z";
        } else if (typekind == DatatypeConstants.GYEARMONTH) {
            formatString = "%Y-%M%z";
        } else if (typekind == DatatypeConstants.GMONTHDAY) {
            formatString = "--%M-%D%z";
        }
        return format(formatString);
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public QName getXMLSchemaType() {
        int mask = (this.year != Integer.MIN_VALUE ? 32 : 0) | (this.month != Integer.MIN_VALUE ? 16 : 0) | (this.day != Integer.MIN_VALUE ? 8 : 0) | (this.hour != Integer.MIN_VALUE ? 4 : 0) | (this.minute != Integer.MIN_VALUE ? 2 : 0) | (this.second != Integer.MIN_VALUE ? 1 : 0);
        switch (mask) {
            case 7:
                return DatatypeConstants.TIME;
            case 8:
                return DatatypeConstants.GDAY;
            case 16:
                return DatatypeConstants.GMONTH;
            case 24:
                return DatatypeConstants.GMONTHDAY;
            case 32:
                return DatatypeConstants.GYEAR;
            case 48:
                return DatatypeConstants.GYEARMONTH;
            case 56:
                return DatatypeConstants.DATE;
            case 63:
                return DatatypeConstants.DATETIME;
            default:
                throw new IllegalStateException(getClass().getName() + "#getXMLSchemaType() :" + DatatypeMessageFormatter.formatMessage(null, "InvalidXGCFields", null));
        }
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public boolean isValid() {
        if (this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE) {
            if (this.year != Integer.MIN_VALUE) {
                if (this.eon == null) {
                    if (this.day > maximumDayInMonthFor(this.year, this.month)) {
                        return false;
                    }
                } else if (this.day > maximumDayInMonthFor(getEonAndYear(), this.month)) {
                    return false;
                }
            } else if (this.day > maximumDayInMonthFor(2000, this.month)) {
                return false;
            }
        }
        if (this.hour == 24) {
            if (this.minute == 0 && this.second == 0) {
                if (this.fractionalSecond != null && this.fractionalSecond.compareTo(DECIMAL_ZERO) != 0) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (this.eon == null && this.year == 0) {
            return false;
        }
        return true;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void add(Duration duration) {
        BigDecimal startSeconds;
        BigInteger tempDays;
        int monthCarry;
        int quotient;
        int endMonth;
        BigInteger mdimf;
        boolean[] fieldUndefined = {false, false, false, false, false, false};
        int signum = duration.getSign();
        int startMonth = getMonth();
        if (startMonth == Integer.MIN_VALUE) {
            startMonth = 1;
            fieldUndefined[1] = true;
        }
        BigInteger dMonths = sanitize(duration.getField(DatatypeConstants.MONTHS), signum);
        BigInteger temp = BigInteger.valueOf(startMonth).add(dMonths);
        setMonth(temp.subtract(BigInteger.ONE).mod(TWELVE).intValue() + 1);
        BigInteger carry = new BigDecimal(temp.subtract(BigInteger.ONE)).divide(new BigDecimal(TWELVE), 3).toBigInteger();
        BigInteger startYear = getEonAndYear();
        if (startYear == null) {
            fieldUndefined[0] = true;
            startYear = BigInteger.ZERO;
        }
        BigInteger dYears = sanitize(duration.getField(DatatypeConstants.YEARS), signum);
        BigInteger endYear = startYear.add(dYears).add(carry);
        setYear(endYear);
        if (getSecond() == Integer.MIN_VALUE) {
            fieldUndefined[5] = true;
            startSeconds = DECIMAL_ZERO;
        } else {
            startSeconds = getSeconds();
        }
        BigDecimal dSeconds = DurationImpl.sanitize((BigDecimal) duration.getField(DatatypeConstants.SECONDS), signum);
        BigDecimal tempBD = startSeconds.add(dSeconds);
        BigDecimal fQuotient = new BigDecimal(new BigDecimal(tempBD.toBigInteger()).divide(DECIMAL_SIXTY, 3).toBigInteger());
        BigDecimal endSeconds = tempBD.subtract(fQuotient.multiply(DECIMAL_SIXTY));
        BigInteger carry2 = fQuotient.toBigInteger();
        setSecond(endSeconds.intValue());
        BigDecimal tempFracSeconds = endSeconds.subtract(new BigDecimal(BigInteger.valueOf(getSecond())));
        if (tempFracSeconds.compareTo(DECIMAL_ZERO) < 0) {
            setFractionalSecond(DECIMAL_ONE.add(tempFracSeconds));
            if (getSecond() == 0) {
                setSecond(59);
                carry2 = carry2.subtract(BigInteger.ONE);
            } else {
                setSecond(getSecond() - 1);
            }
        } else {
            setFractionalSecond(tempFracSeconds);
        }
        int startMinutes = getMinute();
        if (startMinutes == Integer.MIN_VALUE) {
            fieldUndefined[4] = true;
            startMinutes = 0;
        }
        BigInteger dMinutes = sanitize(duration.getField(DatatypeConstants.MINUTES), signum);
        BigInteger temp2 = BigInteger.valueOf(startMinutes).add(dMinutes).add(carry2);
        setMinute(temp2.mod(SIXTY).intValue());
        BigInteger carry3 = new BigDecimal(temp2).divide(DECIMAL_SIXTY, 3).toBigInteger();
        int startHours = getHour();
        if (startHours == Integer.MIN_VALUE) {
            fieldUndefined[3] = true;
            startHours = 0;
        }
        BigInteger dHours = sanitize(duration.getField(DatatypeConstants.HOURS), signum);
        BigInteger temp3 = BigInteger.valueOf(startHours).add(dHours).add(carry3);
        setHour(temp3.mod(TWENTY_FOUR).intValue(), false);
        BigInteger carry4 = new BigDecimal(temp3).divide(new BigDecimal(TWENTY_FOUR), 3).toBigInteger();
        int startDay = getDay();
        if (startDay == Integer.MIN_VALUE) {
            fieldUndefined[2] = true;
            startDay = 1;
        }
        BigInteger dDays = sanitize(duration.getField(DatatypeConstants.DAYS), signum);
        int maxDayInMonth = maximumDayInMonthFor(getEonAndYear(), getMonth());
        if (startDay > maxDayInMonth) {
            tempDays = BigInteger.valueOf(maxDayInMonth);
        } else if (startDay < 1) {
            tempDays = BigInteger.ONE;
        } else {
            tempDays = BigInteger.valueOf(startDay);
        }
        BigInteger endDays = tempDays.add(dDays).add(carry4);
        while (true) {
            if (endDays.compareTo(BigInteger.ONE) < 0) {
                if (this.month >= 2) {
                    mdimf = BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear(), getMonth() - 1));
                } else {
                    mdimf = BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear().subtract(BigInteger.valueOf(1L)), 12));
                }
                endDays = endDays.add(mdimf);
                monthCarry = -1;
            } else {
                if (endDays.compareTo(BigInteger.valueOf(maximumDayInMonthFor(getEonAndYear(), getMonth()))) <= 0) {
                    break;
                }
                endDays = endDays.add(BigInteger.valueOf(-maximumDayInMonthFor(getEonAndYear(), getMonth())));
                monthCarry = 1;
            }
            int intTemp = getMonth() + monthCarry;
            int endMonth2 = (intTemp - 1) % 12;
            if (endMonth2 < 0) {
                endMonth = 12 + endMonth2 + 1;
                quotient = BigDecimal.valueOf(intTemp - 1).divide(new BigDecimal(TWELVE), 0).intValue();
            } else {
                quotient = (intTemp - 1) / 12;
                endMonth = endMonth2 + 1;
            }
            setMonth(endMonth);
            if (quotient != 0) {
                setYear(getEonAndYear().add(BigInteger.valueOf(quotient)));
            }
        }
        setDay(endDays.intValue());
        for (int i2 = 0; i2 <= 5; i2++) {
            if (fieldUndefined[i2]) {
                switch (i2) {
                    case 0:
                        setYear(Integer.MIN_VALUE);
                        break;
                    case 1:
                        setMonth(Integer.MIN_VALUE);
                        break;
                    case 2:
                        setDay(Integer.MIN_VALUE);
                        break;
                    case 3:
                        setHour(Integer.MIN_VALUE, false);
                        break;
                    case 4:
                        setMinute(Integer.MIN_VALUE);
                        break;
                    case 5:
                        setSecond(Integer.MIN_VALUE);
                        setFractionalSecond(null);
                        break;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/datatype/XMLGregorianCalendarImpl$DaysInMonth.class */
    private static class DaysInMonth {
        private static final int[] table = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        private DaysInMonth() {
        }
    }

    private static int maximumDayInMonthFor(BigInteger year, int month) {
        if (month != 2) {
            return DaysInMonth.table[month];
        }
        if (!year.mod(FOUR_HUNDRED).equals(BigInteger.ZERO)) {
            if (year.mod(HUNDRED).equals(BigInteger.ZERO) || !year.mod(FOUR).equals(BigInteger.ZERO)) {
                return DaysInMonth.table[month];
            }
            return 29;
        }
        return 29;
    }

    private static int maximumDayInMonthFor(int year, int month) {
        if (month != 2) {
            return DaysInMonth.table[month];
        }
        if (year % 400 == 0) {
            return 29;
        }
        if (year % 100 == 0 || year % 4 != 0) {
            return DaysInMonth.table[2];
        }
        return 29;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public GregorianCalendar toGregorianCalendar() {
        TimeZone tz = getTimeZone(Integer.MIN_VALUE);
        Locale locale = getDefaultLocale();
        GregorianCalendar result = new GregorianCalendar(tz, locale);
        result.clear();
        result.setGregorianChange(PURE_GREGORIAN_CHANGE);
        if (this.year != Integer.MIN_VALUE) {
            if (this.eon == null) {
                result.set(0, this.year < 0 ? 0 : 1);
                result.set(1, Math.abs(this.year));
            } else {
                BigInteger eonAndYear = getEonAndYear();
                result.set(0, eonAndYear.signum() == -1 ? 0 : 1);
                result.set(1, eonAndYear.abs().intValue());
            }
        }
        if (this.month != Integer.MIN_VALUE) {
            result.set(2, this.month - 1);
        }
        if (this.day != Integer.MIN_VALUE) {
            result.set(5, this.day);
        }
        if (this.hour != Integer.MIN_VALUE) {
            result.set(11, this.hour);
        }
        if (this.minute != Integer.MIN_VALUE) {
            result.set(12, this.minute);
        }
        if (this.second != Integer.MIN_VALUE) {
            result.set(13, this.second);
        }
        if (this.fractionalSecond != null) {
            result.set(14, getMillisecond());
        }
        return result;
    }

    private Locale getDefaultLocale() {
        String lang = SecuritySupport.getSystemProperty("user.language.format");
        String country = SecuritySupport.getSystemProperty("user.country.format");
        String variant = SecuritySupport.getSystemProperty("user.variant.format");
        Locale locale = null;
        if (lang != null) {
            if (country != null) {
                if (variant != null) {
                    locale = new Locale(lang, country, variant);
                } else {
                    locale = new Locale(lang, country);
                }
            } else {
                locale = new Locale(lang);
            }
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public GregorianCalendar toGregorianCalendar(TimeZone timezone, Locale aLocale, XMLGregorianCalendar defaults) {
        int defaultYear;
        TimeZone tz = timezone;
        if (tz == null) {
            int defaultZoneoffset = Integer.MIN_VALUE;
            if (defaults != null) {
                defaultZoneoffset = defaults.getTimezone();
            }
            tz = getTimeZone(defaultZoneoffset);
        }
        if (aLocale == null) {
            aLocale = Locale.getDefault();
        }
        GregorianCalendar result = new GregorianCalendar(tz, aLocale);
        result.clear();
        result.setGregorianChange(PURE_GREGORIAN_CHANGE);
        if (this.year != Integer.MIN_VALUE) {
            if (this.eon == null) {
                result.set(0, this.year < 0 ? 0 : 1);
                result.set(1, Math.abs(this.year));
            } else {
                BigInteger eonAndYear = getEonAndYear();
                result.set(0, eonAndYear.signum() == -1 ? 0 : 1);
                result.set(1, eonAndYear.abs().intValue());
            }
        } else if (defaults != null && (defaultYear = defaults.getYear()) != Integer.MIN_VALUE) {
            if (defaults.getEon() == null) {
                result.set(0, defaultYear < 0 ? 0 : 1);
                result.set(1, Math.abs(defaultYear));
            } else {
                BigInteger defaultEonAndYear = defaults.getEonAndYear();
                result.set(0, defaultEonAndYear.signum() == -1 ? 0 : 1);
                result.set(1, defaultEonAndYear.abs().intValue());
            }
        }
        if (this.month != Integer.MIN_VALUE) {
            result.set(2, this.month - 1);
        } else {
            int defaultMonth = defaults != null ? defaults.getMonth() : Integer.MIN_VALUE;
            if (defaultMonth != Integer.MIN_VALUE) {
                result.set(2, defaultMonth - 1);
            }
        }
        if (this.day != Integer.MIN_VALUE) {
            result.set(5, this.day);
        } else {
            int defaultDay = defaults != null ? defaults.getDay() : Integer.MIN_VALUE;
            if (defaultDay != Integer.MIN_VALUE) {
                result.set(5, defaultDay);
            }
        }
        if (this.hour != Integer.MIN_VALUE) {
            result.set(11, this.hour);
        } else {
            int defaultHour = defaults != null ? defaults.getHour() : Integer.MIN_VALUE;
            if (defaultHour != Integer.MIN_VALUE) {
                result.set(11, defaultHour);
            }
        }
        if (this.minute != Integer.MIN_VALUE) {
            result.set(12, this.minute);
        } else {
            int defaultMinute = defaults != null ? defaults.getMinute() : Integer.MIN_VALUE;
            if (defaultMinute != Integer.MIN_VALUE) {
                result.set(12, defaultMinute);
            }
        }
        if (this.second != Integer.MIN_VALUE) {
            result.set(13, this.second);
        } else {
            int defaultSecond = defaults != null ? defaults.getSecond() : Integer.MIN_VALUE;
            if (defaultSecond != Integer.MIN_VALUE) {
                result.set(13, defaultSecond);
            }
        }
        if (this.fractionalSecond != null) {
            result.set(14, getMillisecond());
        } else {
            BigDecimal defaultFractionalSecond = defaults != null ? defaults.getFractionalSecond() : null;
            if (defaultFractionalSecond != null) {
                result.set(14, defaults.getMillisecond());
            }
        }
        return result;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public TimeZone getTimeZone(int defaultZoneoffset) {
        TimeZone result;
        int zoneoffset = getTimezone();
        if (zoneoffset == Integer.MIN_VALUE) {
            zoneoffset = defaultZoneoffset;
        }
        if (zoneoffset == Integer.MIN_VALUE) {
            result = TimeZone.getDefault();
        } else {
            char sign = zoneoffset < 0 ? '-' : '+';
            if (sign == '-') {
                zoneoffset = -zoneoffset;
            }
            int hour = zoneoffset / 60;
            int minutes = zoneoffset - (hour * 60);
            StringBuffer customTimezoneId = new StringBuffer(8);
            customTimezoneId.append("GMT");
            customTimezoneId.append(sign);
            customTimezoneId.append(hour);
            if (minutes != 0) {
                if (minutes < 10) {
                    customTimezoneId.append('0');
                }
                customTimezoneId.append(minutes);
            }
            result = TimeZone.getTimeZone(customTimezoneId.toString());
        }
        return result;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public Object clone() {
        return new XMLGregorianCalendarImpl(getEonAndYear(), this.month, this.day, this.hour, this.minute, this.second, this.fractionalSecond, this.timezone);
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void clear() {
        this.eon = null;
        this.year = Integer.MIN_VALUE;
        this.month = Integer.MIN_VALUE;
        this.day = Integer.MIN_VALUE;
        this.timezone = Integer.MIN_VALUE;
        this.hour = Integer.MIN_VALUE;
        this.minute = Integer.MIN_VALUE;
        this.second = Integer.MIN_VALUE;
        this.fractionalSecond = null;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setMillisecond(int millisecond) {
        if (millisecond == Integer.MIN_VALUE) {
            this.fractionalSecond = null;
            return;
        }
        if ((millisecond < 0 || 999 < millisecond) && millisecond != Integer.MIN_VALUE) {
            invalidFieldValue(6, millisecond);
        }
        this.fractionalSecond = BigDecimal.valueOf(millisecond, 3);
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void setFractionalSecond(BigDecimal fractional) {
        if (fractional != null && (fractional.compareTo(DECIMAL_ZERO) < 0 || fractional.compareTo(DECIMAL_ONE) > 0)) {
            throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage(null, "InvalidFractional", new Object[]{fractional.toString()}));
        }
        this.fractionalSecond = fractional;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/datatype/XMLGregorianCalendarImpl$Parser.class */
    private final class Parser {
        private final String format;
        private final String value;
        private final int flen;
        private final int vlen;
        private int fidx;
        private int vidx;

        private Parser(String format, String value) {
            this.format = format;
            this.value = value;
            this.flen = format.length();
            this.vlen = value.length();
        }

        public void parse() throws IllegalArgumentException {
            while (this.fidx < this.flen) {
                String str = this.format;
                int i2 = this.fidx;
                this.fidx = i2 + 1;
                char fch = str.charAt(i2);
                if (fch != '%') {
                    skip(fch);
                } else {
                    String str2 = this.format;
                    int i3 = this.fidx;
                    this.fidx = i3 + 1;
                    switch (str2.charAt(i3)) {
                        case 'D':
                            XMLGregorianCalendarImpl.this.setDay(parseInt(2, 2));
                            break;
                        case 'M':
                            XMLGregorianCalendarImpl.this.setMonth(parseInt(2, 2));
                            break;
                        case 'Y':
                            parseYear();
                            break;
                        case 'h':
                            XMLGregorianCalendarImpl.this.setHour(parseInt(2, 2), false);
                            break;
                        case 'm':
                            XMLGregorianCalendarImpl.this.setMinute(parseInt(2, 2));
                            break;
                        case 's':
                            XMLGregorianCalendarImpl.this.setSecond(parseInt(2, 2));
                            if (peek() != '.') {
                                break;
                            } else {
                                XMLGregorianCalendarImpl.this.setFractionalSecond(parseBigDecimal());
                                break;
                            }
                        case 'z':
                            char vch = peek();
                            if (vch == 'Z') {
                                this.vidx++;
                                XMLGregorianCalendarImpl.this.setTimezone(0);
                                break;
                            } else if (vch != '+' && vch != '-') {
                                break;
                            } else {
                                this.vidx++;
                                int h2 = parseInt(2, 2);
                                skip(':');
                                int m2 = parseInt(2, 2);
                                XMLGregorianCalendarImpl.this.setTimezone(((h2 * 60) + m2) * (vch == '+' ? 1 : -1));
                                break;
                            }
                            break;
                        default:
                            throw new InternalError();
                    }
                }
            }
            if (this.vidx == this.vlen) {
                XMLGregorianCalendarImpl.this.testHour();
                return;
            }
            throw new IllegalArgumentException(this.value);
        }

        private char peek() throws IllegalArgumentException {
            if (this.vidx == this.vlen) {
                return (char) 65535;
            }
            return this.value.charAt(this.vidx);
        }

        private char read() throws IllegalArgumentException {
            if (this.vidx == this.vlen) {
                throw new IllegalArgumentException(this.value);
            }
            String str = this.value;
            int i2 = this.vidx;
            this.vidx = i2 + 1;
            return str.charAt(i2);
        }

        private void skip(char ch) throws IllegalArgumentException {
            if (read() != ch) {
                throw new IllegalArgumentException(this.value);
            }
        }

        private int parseInt(int minDigits, int maxDigits) throws IllegalArgumentException {
            int n2 = 0;
            int vstart = this.vidx;
            while (true) {
                char ch = peek();
                if (!XMLGregorianCalendarImpl.isDigit(ch) || this.vidx - vstart >= maxDigits) {
                    break;
                }
                this.vidx++;
                n2 = ((n2 * 10) + ch) - 48;
            }
            if (this.vidx - vstart < minDigits) {
                throw new IllegalArgumentException(this.value);
            }
            return n2;
        }

        private void parseYear() throws IllegalArgumentException {
            int vstart = this.vidx;
            int sign = 0;
            if (peek() == '-') {
                this.vidx++;
                sign = 1;
            }
            while (XMLGregorianCalendarImpl.isDigit(peek())) {
                this.vidx++;
            }
            int digits = (this.vidx - vstart) - sign;
            if (digits < 4) {
                throw new IllegalArgumentException(this.value);
            }
            String yearString = this.value.substring(vstart, this.vidx);
            if (digits < 10) {
                XMLGregorianCalendarImpl.this.setYear(Integer.parseInt(yearString));
            } else {
                XMLGregorianCalendarImpl.this.setYear(new BigInteger(yearString));
            }
        }

        private BigDecimal parseBigDecimal() throws IllegalArgumentException {
            int vstart = this.vidx;
            if (peek() == '.') {
                this.vidx++;
                while (XMLGregorianCalendarImpl.isDigit(peek())) {
                    this.vidx++;
                }
                return new BigDecimal(this.value.substring(vstart, this.vidx));
            }
            throw new IllegalArgumentException(this.value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private String format(String format) {
        StringBuilder buf = new StringBuilder();
        int fidx = 0;
        int flen = format.length();
        while (fidx < flen) {
            int i2 = fidx;
            fidx++;
            char fch = format.charAt(i2);
            if (fch != '%') {
                buf.append(fch);
            } else {
                fidx++;
                switch (format.charAt(fidx)) {
                    case 'D':
                        printNumber(buf, getDay(), 2);
                        break;
                    case 'M':
                        printNumber(buf, getMonth(), 2);
                        break;
                    case 'Y':
                        if (this.eon == null) {
                            int absYear = this.year;
                            if (absYear < 0) {
                                buf.append('-');
                                absYear = -this.year;
                            }
                            printNumber(buf, absYear, 4);
                            break;
                        } else {
                            printNumber(buf, getEonAndYear(), 4);
                            break;
                        }
                    case 'h':
                        printNumber(buf, getHour(), 2);
                        break;
                    case 'm':
                        printNumber(buf, getMinute(), 2);
                        break;
                    case 's':
                        printNumber(buf, getSecond(), 2);
                        if (getFractionalSecond() == null) {
                            break;
                        } else {
                            String frac = getFractionalSecond().toPlainString();
                            buf.append(frac.substring(1, frac.length()));
                            break;
                        }
                    case 'z':
                        int offset = getTimezone();
                        if (offset == 0) {
                            buf.append('Z');
                            break;
                        } else if (offset != Integer.MIN_VALUE) {
                            if (offset < 0) {
                                buf.append('-');
                                offset *= -1;
                            } else {
                                buf.append('+');
                            }
                            printNumber(buf, offset / 60, 2);
                            buf.append(':');
                            printNumber(buf, offset % 60, 2);
                            break;
                        } else {
                            break;
                        }
                    default:
                        throw new InternalError();
                }
            }
        }
        return buf.toString();
    }

    private void printNumber(StringBuilder out, int number, int nDigits) {
        String s2 = String.valueOf(number);
        for (int i2 = s2.length(); i2 < nDigits; i2++) {
            out.append('0');
        }
        out.append(s2);
    }

    private void printNumber(StringBuilder out, BigInteger number, int nDigits) {
        String s2 = number.toString();
        for (int i2 = s2.length(); i2 < nDigits; i2++) {
            out.append('0');
        }
        out.append(s2);
    }

    static BigInteger sanitize(Number value, int signum) {
        if (signum == 0 || value == null) {
            return BigInteger.ZERO;
        }
        return signum < 0 ? ((BigInteger) value).negate() : (BigInteger) value;
    }

    @Override // javax.xml.datatype.XMLGregorianCalendar
    public void reset() {
        this.eon = this.orig_eon;
        this.year = this.orig_year;
        this.month = this.orig_month;
        this.day = this.orig_day;
        this.hour = this.orig_hour;
        this.minute = this.orig_minute;
        this.second = this.orig_second;
        this.fractionalSecond = this.orig_fracSeconds;
        this.timezone = this.orig_timezone;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        save();
    }
}
