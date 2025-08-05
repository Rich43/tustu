package java.nio.charset;

/* loaded from: rt.jar:java/nio/charset/IllegalCharsetNameException.class */
public class IllegalCharsetNameException extends IllegalArgumentException {
    private static final long serialVersionUID = 1457525358470002989L;
    private String charsetName;

    public IllegalCharsetNameException(String str) {
        super(String.valueOf(str));
        this.charsetName = str;
    }

    public String getCharsetName() {
        return this.charsetName;
    }
}
