package java.util;

/* loaded from: rt.jar:java/util/IllegalFormatWidthException.class */
public class IllegalFormatWidthException extends IllegalFormatException {
    private static final long serialVersionUID = 16660902;

    /* renamed from: w, reason: collision with root package name */
    private int f12560w;

    public IllegalFormatWidthException(int i2) {
        this.f12560w = i2;
    }

    public int getWidth() {
        return this.f12560w;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return Integer.toString(this.f12560w);
    }
}
