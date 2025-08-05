package javax.xml.datatype;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/xml/datatype/Duration.class */
public abstract class Duration {
    private static final boolean DEBUG = true;

    public abstract int getSign();

    public abstract Number getField(DatatypeConstants.Field field);

    public abstract boolean isSet(DatatypeConstants.Field field);

    public abstract Duration add(Duration duration);

    public abstract void addTo(Calendar calendar);

    public abstract Duration multiply(BigDecimal bigDecimal);

    public abstract Duration negate();

    public abstract Duration normalizeWith(Calendar calendar);

    public abstract int compare(Duration duration);

    public abstract int hashCode();

    public QName getXMLSchemaType() {
        boolean yearSet = isSet(DatatypeConstants.YEARS);
        boolean monthSet = isSet(DatatypeConstants.MONTHS);
        boolean daySet = isSet(DatatypeConstants.DAYS);
        boolean hourSet = isSet(DatatypeConstants.HOURS);
        boolean minuteSet = isSet(DatatypeConstants.MINUTES);
        boolean secondSet = isSet(DatatypeConstants.SECONDS);
        if (yearSet && monthSet && daySet && hourSet && minuteSet && secondSet) {
            return DatatypeConstants.DURATION;
        }
        if (!yearSet && !monthSet && daySet && hourSet && minuteSet && secondSet) {
            return DatatypeConstants.DURATION_DAYTIME;
        }
        if (yearSet && monthSet && !daySet && !hourSet && !minuteSet && !secondSet) {
            return DatatypeConstants.DURATION_YEARMONTH;
        }
        throw new IllegalStateException("javax.xml.datatype.Duration#getXMLSchemaType(): this Duration does not match one of the XML Schema date/time datatypes: year set = " + yearSet + " month set = " + monthSet + " day set = " + daySet + " hour set = " + hourSet + " minute set = " + minuteSet + " second set = " + secondSet);
    }

    public int getYears() {
        return getField(DatatypeConstants.YEARS).intValue();
    }

    public int getMonths() {
        return getField(DatatypeConstants.MONTHS).intValue();
    }

    public int getDays() {
        return getField(DatatypeConstants.DAYS).intValue();
    }

    public int getHours() {
        return getField(DatatypeConstants.HOURS).intValue();
    }

    public int getMinutes() {
        return getField(DatatypeConstants.MINUTES).intValue();
    }

    public int getSeconds() {
        return getField(DatatypeConstants.SECONDS).intValue();
    }

    public long getTimeInMillis(Calendar startInstant) {
        Calendar cal = (Calendar) startInstant.clone();
        addTo(cal);
        return getCalendarTimeInMillis(cal) - getCalendarTimeInMillis(startInstant);
    }

    public long getTimeInMillis(Date startInstant) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(startInstant);
        addTo(cal);
        return getCalendarTimeInMillis(cal) - startInstant.getTime();
    }

    public void addTo(Date date) {
        if (date == null) {
            throw new NullPointerException("Cannot call " + getClass().getName() + "#addTo(Date date) with date == null.");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        addTo(cal);
        date.setTime(getCalendarTimeInMillis(cal));
    }

    public Duration subtract(Duration rhs) {
        return add(rhs.negate());
    }

    public Duration multiply(int factor) {
        return multiply(new BigDecimal(String.valueOf(factor)));
    }

    public boolean isLongerThan(Duration duration) {
        return compare(duration) == 1;
    }

    public boolean isShorterThan(Duration duration) {
        return compare(duration) == -1;
    }

    public boolean equals(Object duration) {
        return duration != null && (duration instanceof Duration) && compare((Duration) duration) == 0;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (getSign() < 0) {
            buf.append('-');
        }
        buf.append('P');
        BigInteger years = (BigInteger) getField(DatatypeConstants.YEARS);
        if (years != null) {
            buf.append(((Object) years) + Constants._TAG_Y);
        }
        BigInteger months = (BigInteger) getField(DatatypeConstants.MONTHS);
        if (months != null) {
            buf.append(((Object) months) + PdfOps.M_TOKEN);
        }
        BigInteger days = (BigInteger) getField(DatatypeConstants.DAYS);
        if (days != null) {
            buf.append(((Object) days) + PdfOps.D_TOKEN);
        }
        BigInteger hours = (BigInteger) getField(DatatypeConstants.HOURS);
        BigInteger minutes = (BigInteger) getField(DatatypeConstants.MINUTES);
        BigDecimal seconds = (BigDecimal) getField(DatatypeConstants.SECONDS);
        if (hours != null || minutes != null || seconds != null) {
            buf.append('T');
            if (hours != null) {
                buf.append(((Object) hours) + PdfOps.H_TOKEN);
            }
            if (minutes != null) {
                buf.append(((Object) minutes) + PdfOps.M_TOKEN);
            }
            if (seconds != null) {
                buf.append(toString(seconds) + PdfOps.S_TOKEN);
            }
        }
        return buf.toString();
    }

    private String toString(BigDecimal bd2) {
        StringBuffer buf;
        String intString = bd2.unscaledValue().toString();
        int scale = bd2.scale();
        if (scale == 0) {
            return intString;
        }
        int insertionPoint = intString.length() - scale;
        if (insertionPoint == 0) {
            return "0." + intString;
        }
        if (insertionPoint > 0) {
            buf = new StringBuffer(intString);
            buf.insert(insertionPoint, '.');
        } else {
            buf = new StringBuffer((3 - insertionPoint) + intString.length());
            buf.append("0.");
            for (int i2 = 0; i2 < (-insertionPoint); i2++) {
                buf.append('0');
            }
            buf.append(intString);
        }
        return buf.toString();
    }

    private static long getCalendarTimeInMillis(Calendar cal) {
        return cal.getTime().getTime();
    }
}
