package java.util;

/* loaded from: rt.jar:java/util/UnknownFormatConversionException.class */
public class UnknownFormatConversionException extends IllegalFormatException {
    private static final long serialVersionUID = 19060418;

    /* renamed from: s, reason: collision with root package name */
    private String f12570s;

    public UnknownFormatConversionException(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.f12570s = str;
    }

    public String getConversion() {
        return this.f12570s;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return String.format("Conversion = '%s'", this.f12570s);
    }
}
