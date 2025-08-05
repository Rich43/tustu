package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLString;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLStringBuffer.class */
public class XMLStringBuffer extends XMLString {
    public XMLStringBuffer() {
        this(32);
    }

    public XMLStringBuffer(int size) {
        this.ch = new char[size];
    }

    public XMLStringBuffer(char c2) {
        this(1);
        append(c2);
    }

    public XMLStringBuffer(String s2) {
        this(s2.length());
        append(s2);
    }

    public XMLStringBuffer(char[] ch, int offset, int length) {
        this(length);
        append(ch, offset, length);
    }

    public XMLStringBuffer(XMLString s2) {
        this(s2.length);
        append(s2);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLString
    public void clear() {
        this.offset = 0;
        this.length = 0;
    }
}
