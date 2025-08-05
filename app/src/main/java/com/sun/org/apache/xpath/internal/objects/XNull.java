package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xpath.internal.XPathContext;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XNull.class */
public class XNull extends XNodeSet {
    static final long serialVersionUID = -6841683711458983005L;

    @Override // com.sun.org.apache.xpath.internal.objects.XNodeSet, com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XNodeSet, com.sun.org.apache.xpath.internal.objects.XObject
    public String getTypeString() {
        return "#CLASS_NULL";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XNodeSet, com.sun.org.apache.xpath.internal.objects.XObject
    public double num() {
        return 0.0d;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XNodeSet, com.sun.org.apache.xpath.internal.objects.XObject
    public boolean bool() {
        return false;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XNodeSet, com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        return "";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int rtf(XPathContext support) {
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XNodeSet, com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        return obj2.getType() == -1;
    }
}
