package sun.misc;

/* loaded from: rt.jar:sun/misc/Regexp.class */
public class Regexp {
    public boolean ignoreCase;
    public String exp;
    public String prefix;
    public String suffix;
    public boolean exact;
    public int prefixLen;
    public int suffixLen;
    public int totalLen;
    public String[] mids;

    public Regexp(String str) {
        this.exp = str;
        int iIndexOf = str.indexOf(42);
        int iLastIndexOf = str.lastIndexOf(42);
        if (iIndexOf < 0) {
            this.totalLen = str.length();
            this.exact = true;
            return;
        }
        this.prefixLen = iIndexOf;
        if (iIndexOf == 0) {
            this.prefix = null;
        } else {
            this.prefix = str.substring(0, iIndexOf);
        }
        this.suffixLen = (str.length() - iLastIndexOf) - 1;
        if (this.suffixLen == 0) {
            this.suffix = null;
        } else {
            this.suffix = str.substring(iLastIndexOf + 1);
        }
        int i2 = 0;
        int iIndexOf2 = iIndexOf;
        while (true) {
            int i3 = iIndexOf2;
            if (i3 >= iLastIndexOf || i3 < 0) {
                break;
            }
            i2++;
            iIndexOf2 = str.indexOf(42, i3 + 1);
        }
        this.totalLen = this.prefixLen + this.suffixLen;
        if (i2 > 0) {
            this.mids = new String[i2];
            int i4 = iIndexOf;
            for (int i5 = 0; i5 < i2; i5++) {
                int i6 = i4 + 1;
                int iIndexOf3 = str.indexOf(42, i6);
                if (i6 < iIndexOf3) {
                    this.mids[i5] = str.substring(i6, iIndexOf3);
                    this.totalLen += this.mids[i5].length();
                }
                i4 = iIndexOf3;
            }
        }
    }

    final boolean matches(String str) {
        return matches(str, 0, str.length());
    }

    boolean matches(String str, int i2, int i3) {
        if (this.exact) {
            return i3 == this.totalLen && this.exp.regionMatches(this.ignoreCase, 0, str, i2, i3);
        }
        if (i3 < this.totalLen) {
            return false;
        }
        if (this.prefixLen <= 0 || this.prefix.regionMatches(this.ignoreCase, 0, str, i2, this.prefixLen)) {
            if (this.suffixLen > 0 && !this.suffix.regionMatches(this.ignoreCase, 0, str, (i2 + i3) - this.suffixLen, this.suffixLen)) {
                return false;
            }
            if (this.mids == null) {
                return true;
            }
            int length = this.mids.length;
            int i4 = i2 + this.prefixLen;
            int i5 = (i2 + i3) - this.suffixLen;
            for (int i6 = 0; i6 < length; i6++) {
                String str2 = this.mids[i6];
                int length2 = str2.length();
                while (i4 + length2 <= i5 && !str2.regionMatches(this.ignoreCase, 0, str, i4, length2)) {
                    i4++;
                }
                if (i4 + length2 > i5) {
                    return false;
                }
                i4 += length2;
            }
            return true;
        }
        return false;
    }
}
