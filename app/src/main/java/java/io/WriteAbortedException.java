package java.io;

import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:java/io/WriteAbortedException.class */
public class WriteAbortedException extends ObjectStreamException {
    private static final long serialVersionUID = -3326426625597282442L;
    public Exception detail;

    public WriteAbortedException(String str, Exception exc) {
        super(str);
        initCause(null);
        this.detail = exc;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.detail == null) {
            return super.getMessage();
        }
        return super.getMessage() + VectorFormat.DEFAULT_SEPARATOR + this.detail.toString();
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.detail;
    }
}
