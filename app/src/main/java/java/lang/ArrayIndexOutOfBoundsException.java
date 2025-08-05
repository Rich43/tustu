package java.lang;

/* loaded from: rt.jar:java/lang/ArrayIndexOutOfBoundsException.class */
public class ArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private static final long serialVersionUID = -5116101128118950844L;

    public ArrayIndexOutOfBoundsException() {
    }

    public ArrayIndexOutOfBoundsException(int i2) {
        super("Array index out of range: " + i2);
    }

    public ArrayIndexOutOfBoundsException(String str) {
        super(str);
    }
}
