package java.util;

/* loaded from: rt.jar:java/util/MissingFormatWidthException.class */
public class MissingFormatWidthException extends IllegalFormatException {
    private static final long serialVersionUID = 15560123;

    /* renamed from: s, reason: collision with root package name */
    private String f12562s;

    public MissingFormatWidthException(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.f12562s = str;
    }

    public String getFormatSpecifier() {
        return this.f12562s;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.f12562s;
    }
}
