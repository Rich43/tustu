package java.nio.file;

/* loaded from: rt.jar:java/nio/file/InvalidPathException.class */
public class InvalidPathException extends IllegalArgumentException {
    static final long serialVersionUID = 4355821422286746137L;
    private String input;
    private int index;

    public InvalidPathException(String str, String str2, int i2) {
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

    public InvalidPathException(String str, String str2) {
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
