package java.nio.charset;

/* loaded from: rt.jar:java/nio/charset/MalformedInputException.class */
public class MalformedInputException extends CharacterCodingException {
    private static final long serialVersionUID = -3438823399834806194L;
    private int inputLength;

    public MalformedInputException(int i2) {
        this.inputLength = i2;
    }

    public int getInputLength() {
        return this.inputLength;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Input length = " + this.inputLength;
    }
}
