package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.utils.XMLString;

/* compiled from: XNodeSet.java */
/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/GreaterThanOrEqualComparator.class */
class GreaterThanOrEqualComparator extends Comparator {
    GreaterThanOrEqualComparator() {
    }

    @Override // com.sun.org.apache.xpath.internal.objects.Comparator
    boolean compareStrings(XMLString s1, XMLString s2) {
        return s1.toDouble() >= s2.toDouble();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.Comparator
    boolean compareNumbers(double n1, double n2) {
        return n1 >= n2;
    }
}
