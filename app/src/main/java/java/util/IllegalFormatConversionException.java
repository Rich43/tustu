package java.util;

/* loaded from: rt.jar:java/util/IllegalFormatConversionException.class */
public class IllegalFormatConversionException extends IllegalFormatException {
    private static final long serialVersionUID = 17000126;

    /* renamed from: c, reason: collision with root package name */
    private char f12558c;
    private Class<?> arg;

    public IllegalFormatConversionException(char c2, Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }
        this.f12558c = c2;
        this.arg = cls;
    }

    public char getConversion() {
        return this.f12558c;
    }

    public Class<?> getArgumentClass() {
        return this.arg;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return String.format("%c != %s", Character.valueOf(this.f12558c), this.arg.getName());
    }
}
