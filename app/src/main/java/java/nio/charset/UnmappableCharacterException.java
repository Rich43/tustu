package java.nio.charset;

/* loaded from: rt.jar:java/nio/charset/UnmappableCharacterException.class */
public class UnmappableCharacterException extends CharacterCodingException {
    private static final long serialVersionUID = -7026962371537706123L;
    private int inputLength;

    public UnmappableCharacterException(int i2) {
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
