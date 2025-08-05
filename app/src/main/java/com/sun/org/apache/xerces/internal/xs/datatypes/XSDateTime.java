package com.sun.org.apache.xerces.internal.xs.datatypes;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/datatypes/XSDateTime.class */
public interface XSDateTime {
    int getYears();

    int getMonths();

    int getDays();

    int getHours();

    int getMinutes();

    double getSeconds();

    boolean hasTimeZone();

    int getTimeZoneHours();

    int getTimeZoneMinutes();

    String getLexicalValue();

    XSDateTime normalize();

    boolean isNormalized();

    XMLGregorianCalendar getXMLGregorianCalendar();

    Duration getDuration();
}
