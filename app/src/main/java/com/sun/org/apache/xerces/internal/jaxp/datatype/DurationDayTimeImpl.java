package com.sun.org.apache.xerces.internal.jaxp.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/datatype/DurationDayTimeImpl.class */
class DurationDayTimeImpl extends DurationImpl {
    public DurationDayTimeImpl(boolean isPositive, BigInteger days, BigInteger hours, BigInteger minutes, BigDecimal seconds) {
        super(isPositive, (BigInteger) null, (BigInteger) null, days, hours, minutes, seconds);
        convertToCanonicalDayTime();
    }

    public DurationDayTimeImpl(boolean isPositive, int days, int hours, int minutes, int seconds) {
        this(isPositive, wrap(days), wrap(hours), wrap(minutes), seconds != Integer.MIN_VALUE ? new BigDecimal(String.valueOf(seconds)) : null);
    }

    protected DurationDayTimeImpl(String lexicalRepresentation) {
        super(lexicalRepresentation);
        if (getYears() > 0 || getMonths() > 0) {
            throw new IllegalArgumentException("Trying to create an xdt:dayTimeDuration with an invalid lexical representation of \"" + lexicalRepresentation + "\", data model requires a format PnDTnHnMnS.");
        }
        convertToCanonicalDayTime();
    }

    protected DurationDayTimeImpl(long durationInMilliseconds) {
        super(durationInMilliseconds);
        convertToCanonicalDayTime();
        this.years = null;
        this.months = null;
    }

    public float getValue() {
        float sec = this.seconds == null ? 0.0f : this.seconds.floatValue();
        return (((((getDays() * 24) + getHours()) * 60) + getMinutes()) * 60) + sec;
    }

    private void convertToCanonicalDayTime() {
        while (getSeconds() >= 60) {
            this.seconds = this.seconds.subtract(BigDecimal.valueOf(60L));
            this.minutes = BigInteger.valueOf(getMinutes()).add(BigInteger.ONE);
        }
        while (getMinutes() >= 60) {
            this.minutes = this.minutes.subtract(BigInteger.valueOf(60L));
            this.hours = BigInteger.valueOf(getHours()).add(BigInteger.ONE);
        }
        while (getHours() >= 24) {
            this.hours = this.hours.subtract(BigInteger.valueOf(24L));
            this.days = BigInteger.valueOf(getDays()).add(BigInteger.ONE);
        }
    }
}
