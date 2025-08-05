package gnu.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/* loaded from: RXTXcomm.jar:gnu/io/LPRPort.class */
final class LPRPort extends ParallelPort {
    private static final boolean debug = false;
    private int fd;
    private final ParallelOutputStream out = new ParallelOutputStream(this);
    private final ParallelInputStream in = new ParallelInputStream(this);
    private int lprmode = 0;
    private int timeout = 0;
    private int threshold = 1;
    private ParallelPortEventListener PPEventListener;
    private MonitorThread monThread;

    private static native void Initialize();

    private native synchronized int open(String str) throws PortInUseException;

    public native boolean setLPRMode(int i2) throws UnsupportedCommOperationException;

    @Override // gnu.io.ParallelPort
    public native boolean isPaperOut();

    @Override // gnu.io.ParallelPort
    public native boolean isPrinterBusy();

    @Override // gnu.io.ParallelPort
    public native boolean isPrinterError();

    @Override // gnu.io.ParallelPort
    public native boolean isPrinterSelected();

    @Override // gnu.io.ParallelPort
    public native boolean isPrinterTimedOut();

    private native void nativeClose();

    @Override // gnu.io.CommPort
    public native void setInputBufferSize(int i2);

    @Override // gnu.io.CommPort
    public native int getInputBufferSize();

    @Override // gnu.io.CommPort
    public native void setOutputBufferSize(int i2);

    @Override // gnu.io.CommPort
    public native int getOutputBufferSize();

    @Override // gnu.io.ParallelPort
    public native int getOutputBufferFree();

    protected native void writeByte(int i2) throws IOException;

    protected native void writeArray(byte[] bArr, int i2, int i3) throws IOException;

    protected native void drain() throws IOException;

    protected native int nativeavailable() throws IOException;

    protected native int readByte() throws IOException;

    protected native int readArray(byte[] bArr, int i2, int i3) throws IOException;

    native void eventLoop();

    static {
        System.loadLibrary("rxtxParallel");
        Initialize();
    }

    public LPRPort(String str) throws PortInUseException {
        this.fd = open(str);
        this.name = str;
    }

    @Override // gnu.io.CommPort
    public OutputStream getOutputStream() {
        return this.out;
    }

    @Override // gnu.io.CommPort
    public InputStream getInputStream() {
        return this.in;
    }

    @Override // gnu.io.ParallelPort
    public int getMode() {
        return this.lprmode;
    }

    @Override // gnu.io.ParallelPort
    public int setMode(int i2) throws UnsupportedCommOperationException {
        try {
            setLPRMode(i2);
            this.lprmode = i2;
            return 0;
        } catch (UnsupportedCommOperationException e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    @Override // gnu.io.ParallelPort
    public void restart() {
        System.out.println("restart() is not implemented");
    }

    @Override // gnu.io.ParallelPort
    public void suspend() {
        System.out.println("suspend() is not implemented");
    }

    @Override // gnu.io.CommPort
    public synchronized void close() {
        if (this.fd < 0) {
            return;
        }
        nativeClose();
        super.close();
        removeEventListener();
        this.fd = 0;
        Runtime.getRuntime().gc();
    }

    @Override // gnu.io.CommPort
    public void enableReceiveFraming(int i2) throws UnsupportedCommOperationException {
        throw new UnsupportedCommOperationException("Not supported");
    }

    @Override // gnu.io.CommPort
    public void disableReceiveFraming() {
    }

    @Override // gnu.io.CommPort
    public boolean isReceiveFramingEnabled() {
        return false;
    }

    @Override // gnu.io.CommPort
    public int getReceiveFramingByte() {
        return 0;
    }

    @Override // gnu.io.CommPort
    public void enableReceiveTimeout(int i2) {
        if (i2 <= 0) {
            this.timeout = 0;
        } else {
            this.timeout = i2;
        }
    }

    @Override // gnu.io.CommPort
    public void disableReceiveTimeout() {
        this.timeout = 0;
    }

    @Override // gnu.io.CommPort
    public boolean isReceiveTimeoutEnabled() {
        return this.timeout > 0;
    }

    @Override // gnu.io.CommPort
    public int getReceiveTimeout() {
        return this.timeout;
    }

    @Override // gnu.io.CommPort
    public void enableReceiveThreshold(int i2) {
        if (i2 <= 1) {
            this.threshold = 1;
        } else {
            this.threshold = i2;
        }
    }

    @Override // gnu.io.CommPort
    public void disableReceiveThreshold() {
        this.threshold = 1;
    }

    @Override // gnu.io.CommPort
    public int getReceiveThreshold() {
        return this.threshold;
    }

    @Override // gnu.io.CommPort
    public boolean isReceiveThresholdEnabled() {
        return this.threshold > 1;
    }

    public boolean checkMonitorThread() {
        if (this.monThread != null) {
            return this.monThread.isInterrupted();
        }
        return true;
    }

    public synchronized boolean sendEvent(int i2, boolean z2) {
        if (this.fd == 0 || this.PPEventListener == null || this.monThread == null) {
            return true;
        }
        switch (i2) {
            case 1:
                if (!this.monThread.monError) {
                    return false;
                }
                break;
            case 2:
                if (!this.monThread.monBuffer) {
                    return false;
                }
                break;
            default:
                System.err.println(new StringBuffer().append("unknown event:").append(i2).toString());
                return false;
        }
        ParallelPortEvent parallelPortEvent = new ParallelPortEvent(this, i2, !z2, z2);
        if (this.PPEventListener != null) {
            this.PPEventListener.parallelEvent(parallelPortEvent);
        }
        if (this.fd == 0 || this.PPEventListener == null || this.monThread == null) {
            return true;
        }
        try {
            Thread.sleep(50L);
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // gnu.io.ParallelPort
    public synchronized void addEventListener(ParallelPortEventListener parallelPortEventListener) throws TooManyListenersException {
        if (this.PPEventListener != null) {
            throw new TooManyListenersException();
        }
        this.PPEventListener = parallelPortEventListener;
        this.monThread = new MonitorThread(this);
        this.monThread.start();
    }

    @Override // gnu.io.ParallelPort
    public synchronized void removeEventListener() {
        this.PPEventListener = null;
        if (this.monThread != null) {
            this.monThread.interrupt();
            this.monThread = null;
        }
    }

    @Override // gnu.io.ParallelPort
    public synchronized void notifyOnError(boolean z2) {
        System.out.println("notifyOnError is not implemented yet");
        this.monThread.monError = z2;
    }

    @Override // gnu.io.ParallelPort
    public synchronized void notifyOnBuffer(boolean z2) {
        System.out.println("notifyOnBuffer is not implemented yet");
        this.monThread.monBuffer = z2;
    }

    protected void finalize() {
        if (this.fd > 0) {
            close();
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/LPRPort$ParallelOutputStream.class */
    class ParallelOutputStream extends OutputStream {
        private final LPRPort this$0;

        ParallelOutputStream(LPRPort lPRPort) {
            this.this$0 = lPRPort;
        }

        @Override // java.io.OutputStream
        public synchronized void write(int i2) throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            this.this$0.writeByte(i2);
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] bArr) throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            this.this$0.writeArray(bArr, 0, bArr.length);
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            this.this$0.writeArray(bArr, i2, i3);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public synchronized void flush() throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/LPRPort$ParallelInputStream.class */
    class ParallelInputStream extends InputStream {
        private final LPRPort this$0;

        ParallelInputStream(LPRPort lPRPort) {
            this.this$0 = lPRPort;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            return this.this$0.readByte();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            return this.this$0.readArray(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            return this.this$0.readArray(bArr, i2, i3);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            return this.this$0.nativeavailable();
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/LPRPort$MonitorThread.class */
    class MonitorThread extends Thread {
        private boolean monError = false;
        private boolean monBuffer = false;
        private final LPRPort this$0;

        MonitorThread(LPRPort lPRPort) {
            this.this$0 = lPRPort;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.this$0.eventLoop();
            yield();
        }
    }
}
