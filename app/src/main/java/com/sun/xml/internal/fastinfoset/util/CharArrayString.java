package com.sun.xml.internal.fastinfoset.util;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/CharArrayString.class */
public class CharArrayString extends CharArray {
    protected String _s;

    public CharArrayString(String s2) {
        this(s2, true);
    }

    public CharArrayString(String s2, boolean createArray) {
        this._s = s2;
        if (createArray) {
            this.ch = this._s.toCharArray();
            this.start = 0;
            this.length = this.ch.length;
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.util.CharArray, java.lang.CharSequence
    public String toString() {
        return this._s;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.CharArray
    public int hashCode() {
        return this._s.hashCode();
    }

    @Override // com.sun.xml.internal.fastinfoset.util.CharArray
    public boolean equals(Object obj) {
        int i2;
        int i3;
        if (this == obj) {
            return true;
        }
        if (obj instanceof CharArrayString) {
            CharArrayString chas = (CharArrayString) obj;
            return this._s.equals(chas._s);
        }
        if (obj instanceof CharArray) {
            CharArray cha = (CharArray) obj;
            if (this.length == cha.length) {
                int n2 = this.length;
                int i4 = this.start;
                int j2 = cha.start;
                do {
                    int i5 = n2;
                    n2--;
                    if (i5 == 0) {
                        return true;
                    }
                    i2 = i4;
                    i4++;
                    i3 = j2;
                    j2++;
                } while (this.ch[i2] == cha.ch[i3]);
                return false;
            }
            return false;
        }
        return false;
    }
}
