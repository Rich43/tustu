package com.sun.org.apache.xerces.internal.jaxp.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/datatype/DatatypeFactoryImpl.class */
public class DatatypeFactoryImpl extends DatatypeFactory {
    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDuration(String lexicalRepresentation) {
        return new DurationImpl(lexicalRepresentation);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDuration(long durationInMilliseconds) {
        return new DurationImpl(durationInMilliseconds);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDuration(boolean isPositive, BigInteger years, BigInteger months, BigInteger days, BigInteger hours, BigInteger minutes, BigDecimal seconds) {
        return new DurationImpl(isPositive, years, months, days, hours, minutes, seconds);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationYearMonth(boolean isPositive, BigInteger year, BigInteger month) {
        return new DurationYearMonthImpl(isPositive, year, month);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationYearMonth(boolean isPositive, int year, int month) {
        return new DurationYearMonthImpl(isPositive, year, month);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationYearMonth(String lexicalRepresentation) {
        return new DurationYearMonthImpl(lexicalRepresentation);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationYearMonth(long durationInMilliseconds) {
        return new DurationYearMonthImpl(durationInMilliseconds);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationDayTime(String lexicalRepresentation) {
        if (lexicalRepresentation == null) {
            throw new NullPointerException("Trying to create an xdt:dayTimeDuration with an invalid lexical representation of \"null\"");
        }
        return new DurationDayTimeImpl(lexicalRepresentation);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationDayTime(long durationInMilliseconds) {
        return new DurationDayTimeImpl(durationInMilliseconds);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationDayTime(boolean isPositive, BigInteger day, BigInteger hour, BigInteger minute, BigInteger second) {
        return new DurationDayTimeImpl(isPositive, day, hour, minute, second != null ? new BigDecimal(second) : null);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public Duration newDurationDayTime(boolean isPositive, int day, int hour, int minute, int second) {
        return new DurationDayTimeImpl(isPositive, day, hour, minute, second);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public XMLGregorianCalendar newXMLGregorianCalendar() {
        return new XMLGregorianCalendarImpl();
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
        return new XMLGregorianCalendarImpl(lexicalRepresentation);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal) {
        return new XMLGregorianCalendarImpl(cal);
    }

    @Override // javax.xml.datatype.DatatypeFactory
    public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month, int day, int hour, int minute, int second, BigDecimal fractionalSecond, int timezone) {
        return new XMLGregorianCalendarImpl(year, month, day, hour, minute, second, fractionalSecond, timezone);
    }
}
