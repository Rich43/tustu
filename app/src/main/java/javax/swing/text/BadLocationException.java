package javax.swing.text;

/* loaded from: rt.jar:javax/swing/text/BadLocationException.class */
public class BadLocationException extends Exception {
    private int offs;

    public BadLocationException(String str, int i2) {
        super(str);
        this.offs = i2;
    }

    public int offsetRequested() {
        return this.offs;
    }
}
