package java.text;

/* loaded from: rt.jar:java/text/ParseException.class */
public class ParseException extends Exception {
    private static final long serialVersionUID = 2703218443322787634L;
    private int errorOffset;

    public ParseException(String str, int i2) {
        super(str);
        this.errorOffset = i2;
    }

    public int getErrorOffset() {
        return this.errorOffset;
    }
}
