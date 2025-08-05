package jdk.internal.util.xml.impl;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/Pair.class */
public class Pair {
    public String name;
    public String value;
    public int num;
    public char[] chars;
    public int id;
    public Pair list;
    public Pair next;

    public String qname() {
        return new String(this.chars, 1, this.chars.length - 1);
    }

    public String local() {
        if (this.chars[0] != 0) {
            return new String(this.chars, this.chars[0] + 1, (this.chars.length - this.chars[0]) - 1);
        }
        return new String(this.chars, 1, this.chars.length - 1);
    }

    public String pref() {
        if (this.chars[0] != 0) {
            return new String(this.chars, 1, this.chars[0] - 1);
        }
        return "";
    }

    public boolean eqpref(char[] cArr) {
        if (this.chars[0] == cArr[0]) {
            char c2 = this.chars[0];
            char c3 = 1;
            while (true) {
                char c4 = c3;
                if (c4 < c2) {
                    if (this.chars[c4] == cArr[c4]) {
                        c3 = (char) (c4 + 1);
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean eqname(char[] cArr) {
        char length = (char) this.chars.length;
        if (length == cArr.length) {
            char c2 = 0;
            while (true) {
                char c3 = c2;
                if (c3 < length) {
                    if (this.chars[c3] == cArr[c3]) {
                        c2 = (char) (c3 + 1);
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
