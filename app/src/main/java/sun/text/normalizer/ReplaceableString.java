package sun.text.normalizer;

/* loaded from: rt.jar:sun/text/normalizer/ReplaceableString.class */
public class ReplaceableString implements Replaceable {
    private StringBuffer buf;

    public ReplaceableString(String str) {
        this.buf = new StringBuffer(str);
    }

    public ReplaceableString(StringBuffer stringBuffer) {
        this.buf = stringBuffer;
    }

    @Override // sun.text.normalizer.Replaceable
    public int length() {
        return this.buf.length();
    }

    @Override // sun.text.normalizer.Replaceable
    public char charAt(int i2) {
        return this.buf.charAt(i2);
    }

    @Override // sun.text.normalizer.Replaceable
    public void getChars(int i2, int i3, char[] cArr, int i4) {
        Utility.getChars(this.buf, i2, i3, cArr, i4);
    }
}
