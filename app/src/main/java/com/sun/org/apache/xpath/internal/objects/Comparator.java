package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.utils.XMLString;

/* compiled from: XNodeSet.java */
/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/Comparator.class */
abstract class Comparator {
    abstract boolean compareStrings(XMLString xMLString, XMLString xMLString2);

    abstract boolean compareNumbers(double d2, double d3);

    Comparator() {
    }
}
