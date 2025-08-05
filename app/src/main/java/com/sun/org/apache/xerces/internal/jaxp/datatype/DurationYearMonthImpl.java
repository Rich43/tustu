package com.sun.org.apache.xerces.internal.jaxp.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/datatype/DurationYearMonthImpl.class */
class DurationYearMonthImpl extends DurationImpl {
    public DurationYearMonthImpl(boolean isPositive, BigInteger years, BigInteger months) {
        super(isPositive, years, months, (BigInteger) null, (BigInteger) null, (BigInteger) null, (BigDecimal) null);
        convertToCanonicalYearMonth();
    }

    protected DurationYearMonthImpl(boolean isPositive, int years, int months) {
        this(isPositive, wrap(years), wrap(months));
    }

    protected DurationYearMonthImpl(long durationInMilliseconds) {
        super(durationInMilliseconds);
        convertToCanonicalYearMonth();
        this.days = null;
        this.hours = null;
        this.minutes = null;
        this.seconds = null;
        this.signum = calcSignum(this.signum >= 0);
    }

    protected DurationYearMonthImpl(String lexicalRepresentation) {
        super(lexicalRepresentation);
        if (getDays() > 0 || getHours() > 0 || getMinutes() > 0 || getSeconds() > 0) {
            throw new IllegalArgumentException("Trying to create an xdt:yearMonthDuration with an invalid lexical representation of \"" + lexicalRepresentation + "\", data model requires PnYnM.");
        }
        convertToCanonicalYearMonth();
    }

    public int getValue() {
        return (getYears() * 12) + getMonths();
    }

    private void convertToCanonicalYearMonth() {
        while (getMonths() >= 12) {
            this.months = this.months.subtract(BigInteger.valueOf(12L));
            this.years = BigInteger.valueOf(getYears()).add(BigInteger.ONE);
        }
    }
}
