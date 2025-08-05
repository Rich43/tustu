package sun.awt;

/* loaded from: rt.jar:sun/awt/CharsetString.class */
public class CharsetString {
    public char[] charsetChars;
    public int offset;
    public int length;
    public FontDescriptor fontDescriptor;

    public CharsetString(char[] cArr, int i2, int i3, FontDescriptor fontDescriptor) {
        this.charsetChars = cArr;
        this.offset = i2;
        this.length = i3;
        this.fontDescriptor = fontDescriptor;
    }
}
