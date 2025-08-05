package java.io;

/* loaded from: rt.jar:java/io/OptionalDataException.class */
public class OptionalDataException extends ObjectStreamException {
    private static final long serialVersionUID = -8011121865681257820L;
    public int length;
    public boolean eof;

    OptionalDataException(int i2) {
        this.eof = false;
        this.length = i2;
    }

    OptionalDataException(boolean z2) {
        this.length = 0;
        this.eof = z2;
    }
}
