package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/datatype/XMLGregorianCalendar.class */
public abstract class XMLGregorianCalendar implements Cloneable {
    public abstract void clear();

    public abstract void reset();

    public abstract void setYear(BigInteger bigInteger);

    public abstract void setYear(int i2);

    public abstract void setMonth(int i2);

    public abstract void setDay(int i2);

    public abstract void setTimezone(int i2);

    public abstract void setHour(int i2);

    public abstract void setMinute(int i2);

    public abstract void setSecond(int i2);

    public abstract void setMillisecond(int i2);

    public abstract void setFractionalSecond(BigDecimal bigDecimal);

    public abstract BigInteger getEon();

    public abstract int getYear();

    public abstract BigInteger getEonAndYear();

    public abstract int getMonth();

    public abstract int getDay();

    public abstract int getTimezone();

    public abstract int getHour();

    public abstract int getMinute();

    public abstract int getSecond();

    public abstract BigDecimal getFractionalSecond();

    public abstract int compare(XMLGregorianCalendar xMLGregorianCalendar);

    public abstract XMLGregorianCalendar normalize();

    public abstract String toXMLFormat();

    public abstract QName getXMLSchemaType();

    public abstract boolean isValid();

    public abstract void add(Duration duration);

    public abstract GregorianCalendar toGregorianCalendar();

    public abstract GregorianCalendar toGregorianCalendar(TimeZone timeZone, Locale locale, XMLGregorianCalendar xMLGregorianCalendar);

    public abstract TimeZone getTimeZone(int i2);

    public abstract Object clone();

    public void setTime(int hour, int minute, int second) {
        setTime(hour, minute, second, (BigDecimal) null);
    }

    public void setTime(int hour, int minute, int second, BigDecimal fractional) {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
        setFractionalSecond(fractional);
    }

    public void setTime(int hour, int minute, int second, int millisecond) {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
        setMillisecond(millisecond);
    }

    public int getMillisecond() {
        BigDecimal fractionalSeconds = getFractionalSecond();
        if (fractionalSeconds == null) {
            return Integer.MIN_VALUE;
        }
        return getFractionalSecond().movePointRight(3).intValue();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof XMLGregorianCalendar) && compare((XMLGregorianCalendar) obj) == 0;
    }

    public int hashCode() {
        int timezone = getTimezone();
        if (timezone == Integer.MIN_VALUE) {
            timezone = 0;
        }
        XMLGregorianCalendar gc = this;
        if (timezone != 0) {
            gc = normalize();
        }
        return gc.getYear() + gc.getMonth() + gc.getDay() + gc.getHour() + gc.getMinute() + gc.getSecond();
    }

    public String toString() {
        return toXMLFormat();
    }
}
