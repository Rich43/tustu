package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/CharKey.class */
public class CharKey {
    private char m_char;

    public CharKey(char key) {
        this.m_char = key;
    }

    public CharKey() {
    }

    public final void setChar(char c2) {
        this.m_char = c2;
    }

    public final int hashCode() {
        return this.m_char;
    }

    public final boolean equals(Object obj) {
        return ((CharKey) obj).m_char == this.m_char;
    }
}
