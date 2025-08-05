package com.sun.org.apache.xml.internal.security.c14n.implementations;

import org.w3c.dom.Attr;

/* compiled from: NameSpaceSymbTable.java */
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/NameSpaceSymbEntry.class */
class NameSpaceSymbEntry implements Cloneable {
    String prefix;
    String uri;
    String lastrendered = null;
    boolean rendered;

    /* renamed from: n, reason: collision with root package name */
    Attr f12009n;

    NameSpaceSymbEntry(String str, Attr attr, boolean z2, String str2) {
        this.rendered = false;
        this.uri = str;
        this.rendered = z2;
        this.f12009n = attr;
        this.prefix = str2;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }
}
