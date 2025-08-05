package com.sun.org.apache.xml.internal.utils.res;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/res/CharArrayWrapper.class */
public class CharArrayWrapper {
    private char[] m_char;

    public CharArrayWrapper(char[] arg) {
        this.m_char = arg;
    }

    public char getChar(int index) {
        return this.m_char[index];
    }

    public int getLength() {
        return this.m_char.length;
    }
}
