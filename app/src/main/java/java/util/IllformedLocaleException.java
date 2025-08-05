package java.util;

/* loaded from: rt.jar:java/util/IllformedLocaleException.class */
public class IllformedLocaleException extends RuntimeException {
    private static final long serialVersionUID = -5245986824925681401L;
    private int _errIdx;

    public IllformedLocaleException() {
        this._errIdx = -1;
    }

    public IllformedLocaleException(String str) {
        super(str);
        this._errIdx = -1;
    }

    public IllformedLocaleException(String str, int i2) {
        super(str + (i2 < 0 ? "" : " [at index " + i2 + "]"));
        this._errIdx = -1;
        this._errIdx = i2;
    }

    public int getErrorIndex() {
        return this._errIdx;
    }
}
