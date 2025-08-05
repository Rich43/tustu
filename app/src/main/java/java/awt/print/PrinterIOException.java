package java.awt.print;

import java.io.IOException;

/* loaded from: rt.jar:java/awt/print/PrinterIOException.class */
public class PrinterIOException extends PrinterException {
    static final long serialVersionUID = 5850870712125932846L;
    private IOException mException;

    public PrinterIOException(IOException iOException) {
        initCause(null);
        this.mException = iOException;
    }

    public IOException getIOException() {
        return this.mException;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.mException;
    }
}
