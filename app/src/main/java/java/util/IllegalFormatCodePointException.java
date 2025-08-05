package java.util;

/* loaded from: rt.jar:java/util/IllegalFormatCodePointException.class */
public class IllegalFormatCodePointException extends IllegalFormatException {
    private static final long serialVersionUID = 19080630;

    /* renamed from: c, reason: collision with root package name */
    private int f12557c;

    public IllegalFormatCodePointException(int i2) {
        this.f12557c = i2;
    }

    public int getCodePoint() {
        return this.f12557c;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return String.format("Code point = %#x", Integer.valueOf(this.f12557c));
    }
}
