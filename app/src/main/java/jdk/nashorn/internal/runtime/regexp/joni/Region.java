package jdk.nashorn.internal.runtime.regexp.joni;

import sun.util.locale.LanguageTag;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/Region.class */
public final class Region {
    static final int REGION_NOTPOS = -1;
    public final int numRegs;
    public final int[] beg;
    public final int[] end;

    public Region(int num) {
        this.numRegs = num;
        this.beg = new int[num];
        this.end = new int[num];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Region: \n");
        for (int i2 = 0; i2 < this.beg.length; i2++) {
            sb.append(" " + i2 + ": (" + this.beg[i2] + LanguageTag.SEP + this.end[i2] + ")");
        }
        return sb.toString();
    }

    void clear() {
        for (int i2 = 0; i2 < this.beg.length; i2++) {
            this.end[i2] = -1;
            this.beg[i2] = -1;
        }
    }
}
