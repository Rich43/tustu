package sun.text;

import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/text/ComposedCharIter.class */
public final class ComposedCharIter {
    public static final int DONE = -1;
    private static int[] chars = new int[2000];
    private static String[] decomps = new String[2000];
    private static int decompNum = NormalizerImpl.getDecompose(chars, decomps);
    private int curChar = -1;

    public int next() {
        if (this.curChar == decompNum - 1) {
            return -1;
        }
        int[] iArr = chars;
        int i2 = this.curChar + 1;
        this.curChar = i2;
        return iArr[i2];
    }

    public String decomposition() {
        return decomps[this.curChar];
    }
}
