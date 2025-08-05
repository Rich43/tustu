package java.lang;

/* loaded from: rt.jar:java/lang/StringIndexOutOfBoundsException.class */
public class StringIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private static final long serialVersionUID = -6762910422159637258L;

    public StringIndexOutOfBoundsException() {
    }

    public StringIndexOutOfBoundsException(String str) {
        super(str);
    }

    public StringIndexOutOfBoundsException(int i2) {
        super("String index out of range: " + i2);
    }
}
