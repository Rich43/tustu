package sun.font;

/* loaded from: rt.jar:sun/font/TextRecord.class */
public final class TextRecord {
    public char[] text;
    public int start;
    public int limit;
    public int min;
    public int max;

    public void init(char[] cArr, int i2, int i3, int i4, int i5) {
        this.text = cArr;
        this.start = i2;
        this.limit = i3;
        this.min = i4;
        this.max = i5;
    }
}
