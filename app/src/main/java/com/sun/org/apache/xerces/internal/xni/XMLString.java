package com.sun.org.apache.xerces.internal.xni;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XMLString.class */
public class XMLString {
    public static final int DEFAULT_SIZE = 32;
    public char[] ch;
    public int offset;
    public int length;

    public XMLString() {
    }

    public XMLString(char[] ch, int offset, int length) {
        setValues(ch, offset, length);
    }

    public XMLString(XMLString string) {
        setValues(string);
    }

    public void setValues(char[] ch, int offset, int length) {
        this.ch = ch;
        this.offset = offset;
        this.length = length;
    }

    public void setValues(XMLString s2) {
        setValues(s2.ch, s2.offset, s2.length);
    }

    public void clear() {
        this.ch = null;
        this.offset = 0;
        this.length = -1;
    }

    public boolean equals(char[] ch, int offset, int length) {
        if (ch == null || this.length != length) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (this.ch[this.offset + i2] != ch[offset + i2]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(String s2) {
        if (s2 == null || this.length != s2.length()) {
            return false;
        }
        for (int i2 = 0; i2 < this.length; i2++) {
            if (this.ch[this.offset + i2] != s2.charAt(i2)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return this.length > 0 ? new String(this.ch, this.offset, this.length) : "";
    }

    public void append(char c2) {
        if (this.length + 1 > this.ch.length) {
            int newLength = this.ch.length * 2;
            if (newLength < this.ch.length + 32) {
                newLength = this.ch.length + 32;
            }
            char[] tmp = new char[newLength];
            System.arraycopy(this.ch, 0, tmp, 0, this.length);
            this.ch = tmp;
        }
        this.ch[this.length] = c2;
        this.length++;
    }

    public void append(String s2) {
        int length = s2.length();
        if (this.length + length > this.ch.length) {
            int newLength = this.ch.length * 2;
            if (newLength < this.ch.length + length + 32) {
                newLength = this.ch.length + length + 32;
            }
            char[] newch = new char[newLength];
            System.arraycopy(this.ch, 0, newch, 0, this.length);
            this.ch = newch;
        }
        s2.getChars(0, length, this.ch, this.length);
        this.length += length;
    }

    public void append(char[] ch, int offset, int length) {
        if (this.length + length > this.ch.length) {
            int newLength = this.ch.length * 2;
            if (newLength < this.ch.length + length + 32) {
                newLength = this.ch.length + length + 32;
            }
            char[] newch = new char[newLength];
            System.arraycopy(this.ch, 0, newch, 0, this.length);
            this.ch = newch;
        }
        if (ch != null && length > 0) {
            System.arraycopy(ch, offset, this.ch, this.length, length);
            this.length += length;
        }
    }

    public void append(XMLString s2) {
        append(s2.ch, s2.offset, s2.length);
    }
}
