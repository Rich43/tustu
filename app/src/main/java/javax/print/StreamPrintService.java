package javax.print;

import java.io.OutputStream;

/* loaded from: rt.jar:javax/print/StreamPrintService.class */
public abstract class StreamPrintService implements PrintService {
    private OutputStream outStream;
    private boolean disposed = false;

    public abstract String getOutputFormat();

    private StreamPrintService() {
    }

    protected StreamPrintService(OutputStream outputStream) {
        this.outStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return this.outStream;
    }

    public void dispose() {
        this.disposed = true;
    }

    public boolean isDisposed() {
        return this.disposed;
    }
}
