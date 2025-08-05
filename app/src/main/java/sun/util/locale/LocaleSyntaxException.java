package sun.util.locale;

/* loaded from: rt.jar:sun/util/locale/LocaleSyntaxException.class */
public class LocaleSyntaxException extends Exception {
    private static final long serialVersionUID = 1;
    private int index;

    public LocaleSyntaxException(String str) {
        this(str, 0);
    }

    public LocaleSyntaxException(String str, int i2) {
        super(str);
        this.index = -1;
        this.index = i2;
    }

    public int getErrorIndex() {
        return this.index;
    }
}
