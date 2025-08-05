package java.util;

/* loaded from: rt.jar:java/util/IllegalFormatPrecisionException.class */
public class IllegalFormatPrecisionException extends IllegalFormatException {
    private static final long serialVersionUID = 18711008;

    /* renamed from: p, reason: collision with root package name */
    private int f12559p;

    public IllegalFormatPrecisionException(int i2) {
        this.f12559p = i2;
    }

    public int getPrecision() {
        return this.f12559p;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return Integer.toString(this.f12559p);
    }
}
