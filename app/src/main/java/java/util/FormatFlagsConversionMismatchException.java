package java.util;

/* loaded from: rt.jar:java/util/FormatFlagsConversionMismatchException.class */
public class FormatFlagsConversionMismatchException extends IllegalFormatException {
    private static final long serialVersionUID = 19120414;

    /* renamed from: f, reason: collision with root package name */
    private String f12550f;

    /* renamed from: c, reason: collision with root package name */
    private char f12551c;

    public FormatFlagsConversionMismatchException(String str, char c2) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.f12550f = str;
        this.f12551c = c2;
    }

    public String getFlags() {
        return this.f12550f;
    }

    public char getConversion() {
        return this.f12551c;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Conversion = " + this.f12551c + ", Flags = " + this.f12550f;
    }
}
