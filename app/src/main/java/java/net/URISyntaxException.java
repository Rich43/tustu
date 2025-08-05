package java.net;

/* loaded from: rt.jar:java/net/URISyntaxException.class */
public class URISyntaxException extends Exception {
    private static final long serialVersionUID = 2137979680897488891L;
    private String input;
    private int index;

    public URISyntaxException(String str, String str2, int i2) {
        super(str2);
        if (str == null || str2 == null) {
            throw new NullPointerException();
        }
        if (i2 < -1) {
            throw new IllegalArgumentException();
        }
        this.input = str;
        this.index = i2;
    }

    public URISyntaxException(String str, String str2) {
        this(str, str2, -1);
    }

    public String getInput() {
        return this.input;
    }

    public String getReason() {
        return super.getMessage();
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getReason());
        if (this.index > -1) {
            stringBuffer.append(" at index ");
            stringBuffer.append(this.index);
        }
        stringBuffer.append(": ");
        stringBuffer.append(this.input);
        return stringBuffer.toString();
    }
}
