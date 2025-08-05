package gnu.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/* loaded from: RXTXcomm.jar:gnu/io/RXTXPort.class */
public final class RXTXPort extends SerialPort {
    protected static final boolean debug = false;
    protected static final boolean debug_read = false;
    protected static final boolean debug_read_results = false;
    protected static final boolean debug_write = false;
    protected static final boolean debug_events = false;
    protected static final boolean debug_verbose = false;

    /* renamed from: z, reason: collision with root package name */
    private static Zystem f12219z;
    boolean MonitorThreadAlive;
    private int fd;
    static boolean dsrFlag;
    private int timeout;
    private SerialPortEventListener SPEventListener;
    private MonitorThread monThread;
    boolean MonitorThreadLock;
    int IOLocked = 0;
    Object IOLockedMutex = new Object();
    long eis = 0;
    int pid = 0;
    private final SerialOutputStream out = new SerialOutputStream(this);
    private final SerialInputStream in = new SerialInputStream(this);
    private int speed = jssc.SerialPort.BAUDRATE_9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int parity = 0;
    private int flowmode = 0;
    private int threshold = 0;
    private int InputBuffer = 0;
    private int OutputBuffer = 0;
    boolean monThreadisInterrupted = true;
    boolean closeLock = false;

    private static native void Initialize();

    private native synchronized int open(String str) throws PortInUseException;

    private native int nativeGetParity(int i2);

    private native int nativeGetFlowControlMode(int i2);

    private native boolean nativeSetSerialPortParams(int i2, int i3, int i4, int i5) throws UnsupportedCommOperationException;

    native void setflowcontrol(int i2) throws IOException;

    public native int NativegetReceiveTimeout();

    private native boolean NativeisReceiveTimeoutEnabled();

    private native void NativeEnableReceiveTimeoutThreshold(int i2, int i3, int i4);

    @Override // gnu.io.SerialPort
    public native boolean isDTR();

    @Override // gnu.io.SerialPort
    public native void setDTR(boolean z2);

    @Override // gnu.io.SerialPort
    public native void setRTS(boolean z2);

    private native void setDSR(boolean z2);

    @Override // gnu.io.SerialPort
    public native boolean isCTS();

    @Override // gnu.io.SerialPort
    public native boolean isDSR();

    @Override // gnu.io.SerialPort
    public native boolean isCD();

    @Override // gnu.io.SerialPort
    public native boolean isRI();

    @Override // gnu.io.SerialPort
    public native boolean isRTS();

    @Override // gnu.io.SerialPort
    public native void sendBreak(int i2);

    protected native void writeByte(int i2, boolean z2) throws IOException;

    protected native void writeArray(byte[] bArr, int i2, int i3, boolean z2) throws IOException;

    protected native boolean nativeDrain(boolean z2) throws IOException;

    protected native int nativeavailable() throws IOException;

    protected native int readByte() throws IOException;

    protected native int readArray(byte[] bArr, int i2, int i3) throws IOException;

    protected native int readTerminatedArray(byte[] bArr, int i2, int i3, byte[] bArr2) throws IOException;

    native void eventLoop();

    private native void interruptEventLoop();

    private native void nativeSetEventFlag(int i2, int i3, boolean z2);

    private native void nativeClose(String str);

    private static native void nativeStaticSetSerialPortParams(String str, int i2, int i3, int i4, int i5) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticSetDSR(String str, boolean z2) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticSetDTR(String str, boolean z2) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticSetRTS(String str, boolean z2) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticIsDSR(String str) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticIsDTR(String str) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticIsRTS(String str) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticIsCTS(String str) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticIsCD(String str) throws UnsupportedCommOperationException;

    private static native boolean nativeStaticIsRI(String str) throws UnsupportedCommOperationException;

    private static native int nativeStaticGetBaudRate(String str) throws UnsupportedCommOperationException;

    private static native int nativeStaticGetDataBits(String str) throws UnsupportedCommOperationException;

    private static native int nativeStaticGetParity(String str) throws UnsupportedCommOperationException;

    private static native int nativeStaticGetStopBits(String str) throws UnsupportedCommOperationException;

    private native byte nativeGetParityErrorChar() throws UnsupportedCommOperationException;

    private native boolean nativeSetParityErrorChar(byte b2) throws UnsupportedCommOperationException;

    private native byte nativeGetEndOfInputChar() throws UnsupportedCommOperationException;

    private native boolean nativeSetEndOfInputChar(byte b2) throws UnsupportedCommOperationException;

    private native boolean nativeSetUartType(String str, boolean z2) throws UnsupportedCommOperationException;

    native String nativeGetUartType() throws UnsupportedCommOperationException;

    private native boolean nativeSetBaudBase(int i2) throws UnsupportedCommOperationException;

    private native int nativeGetBaudBase() throws UnsupportedCommOperationException;

    private native boolean nativeSetDivisor(int i2) throws UnsupportedCommOperationException;

    private native int nativeGetDivisor() throws UnsupportedCommOperationException;

    private native boolean nativeSetLowLatency() throws UnsupportedCommOperationException;

    private native boolean nativeGetLowLatency() throws UnsupportedCommOperationException;

    private native boolean nativeSetCallOutHangup(boolean z2) throws UnsupportedCommOperationException;

    private native boolean nativeGetCallOutHangup() throws UnsupportedCommOperationException;

    private native boolean nativeClearCommInput() throws UnsupportedCommOperationException;

    static {
        try {
            f12219z = new Zystem();
        } catch (Exception e2) {
        }
        System.loadLibrary("rxtxSerial");
        Initialize();
        dsrFlag = false;
    }

    public RXTXPort(String str) throws PortInUseException {
        this.MonitorThreadAlive = false;
        this.fd = 0;
        this.MonitorThreadLock = true;
        this.fd = open(str);
        this.name = str;
        this.MonitorThreadLock = true;
        this.monThread = new MonitorThread(this);
        this.monThread.start();
        waitForTheNativeCodeSilly();
        this.MonitorThreadAlive = true;
        this.timeout = -1;
    }

    @Override // gnu.io.CommPort
    public OutputStream getOutputStream() {
        return this.out;
    }

    @Override // gnu.io.CommPort
    public InputStream getInputStream() {
        return this.in;
    }

    @Override // gnu.io.SerialPort
    public synchronized void setSerialPortParams(int i2, int i3, int i4, int i5) throws UnsupportedCommOperationException {
        if (nativeSetSerialPortParams(i2, i3, i4, i5)) {
            throw new UnsupportedCommOperationException("Invalid Parameter");
        }
        this.speed = i2;
        if (i4 == 3) {
            this.dataBits = 5;
        } else {
            this.dataBits = i3;
        }
        this.stopBits = i4;
        this.parity = i5;
        f12219z.reportln(new StringBuffer().append("RXTXPort:setSerialPortParams(").append(i2).append(" ").append(i3).append(" ").append(i4).append(" ").append(i5).append(") returning").toString());
    }

    @Override // gnu.io.SerialPort
    public int getBaudRate() {
        return this.speed;
    }

    @Override // gnu.io.SerialPort
    public int getDataBits() {
        return this.dataBits;
    }

    @Override // gnu.io.SerialPort
    public int getStopBits() {
        return this.stopBits;
    }

    @Override // gnu.io.SerialPort
    public int getParity() {
        return this.parity;
    }

    @Override // gnu.io.SerialPort
    public void setFlowControlMode(int i2) {
        if (this.monThreadisInterrupted) {
            return;
        }
        try {
            setflowcontrol(i2);
            this.flowmode = i2;
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override // gnu.io.SerialPort
    public int getFlowControlMode() {
        return this.flowmode;
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
    public void disableReceiveTimeout() {
        this.timeout = -1;
        NativeEnableReceiveTimeoutThreshold(this.timeout, this.threshold, this.InputBuffer);
    }

    @Override // gnu.io.CommPort
    public void enableReceiveTimeout(int i2) {
        if (i2 >= 0) {
            this.timeout = i2;
            NativeEnableReceiveTimeoutThreshold(i2, this.threshold, this.InputBuffer);
            return;
        }
        throw new IllegalArgumentException("Unexpected negative timeout value");
    }

    @Override // gnu.io.CommPort
    public boolean isReceiveTimeoutEnabled() {
        return NativeisReceiveTimeoutEnabled();
    }

    @Override // gnu.io.CommPort
    public int getReceiveTimeout() {
        return NativegetReceiveTimeout();
    }

    @Override // gnu.io.CommPort
    public void enableReceiveThreshold(int i2) {
        if (i2 >= 0) {
            this.threshold = i2;
            NativeEnableReceiveTimeoutThreshold(this.timeout, this.threshold, this.InputBuffer);
            return;
        }
        throw new IllegalArgumentException("Unexpected negative threshold value");
    }

    @Override // gnu.io.CommPort
    public void disableReceiveThreshold() {
        enableReceiveThreshold(0);
    }

    @Override // gnu.io.CommPort
    public int getReceiveThreshold() {
        return this.threshold;
    }

    @Override // gnu.io.CommPort
    public boolean isReceiveThresholdEnabled() {
        return this.threshold > 0;
    }

    @Override // gnu.io.CommPort
    public void setInputBufferSize(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Unexpected negative buffer size value");
        }
        this.InputBuffer = i2;
    }

    @Override // gnu.io.CommPort
    public int getInputBufferSize() {
        return this.InputBuffer;
    }

    @Override // gnu.io.CommPort
    public void setOutputBufferSize(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Unexpected negative buffer size value");
        }
        this.OutputBuffer = i2;
    }

    @Override // gnu.io.CommPort
    public int getOutputBufferSize() {
        return this.OutputBuffer;
    }

    public boolean checkMonitorThread() {
        if (this.monThread != null) {
            return this.monThreadisInterrupted;
        }
        return true;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x0018. Please report as an issue. */
    public boolean sendEvent(int i2, boolean z2) {
        if (this.fd == 0 || this.SPEventListener == null || this.monThread == null) {
            return true;
        }
        switch (i2) {
        }
        switch (i2) {
            case 1:
                if (!this.monThread.Data) {
                    return false;
                }
                break;
            case 2:
                if (!this.monThread.Output) {
                    return false;
                }
                break;
            case 3:
                if (!this.monThread.CTS) {
                    return false;
                }
                break;
            case 4:
                if (!this.monThread.DSR) {
                    return false;
                }
                break;
            case 5:
                if (!this.monThread.RI) {
                    return false;
                }
                break;
            case 6:
                if (!this.monThread.CD) {
                    return false;
                }
                break;
            case 7:
                if (!this.monThread.OE) {
                    return false;
                }
                break;
            case 8:
                if (!this.monThread.PE) {
                    return false;
                }
                break;
            case 9:
                if (!this.monThread.FE) {
                    return false;
                }
                break;
            case 10:
                if (!this.monThread.BI) {
                    return false;
                }
                break;
            default:
                System.err.println(new StringBuffer().append("unknown event: ").append(i2).toString());
                return false;
        }
        SerialPortEvent serialPortEvent = new SerialPortEvent(this, i2, !z2, z2);
        if (this.monThreadisInterrupted) {
            return true;
        }
        if (this.SPEventListener != null) {
            this.SPEventListener.serialEvent(serialPortEvent);
        }
        if (this.fd == 0 || this.SPEventListener == null || this.monThread == null) {
            return true;
        }
        return false;
    }

    @Override // gnu.io.SerialPort
    public void addEventListener(SerialPortEventListener serialPortEventListener) throws TooManyListenersException {
        if (this.SPEventListener != null) {
            throw new TooManyListenersException();
        }
        this.SPEventListener = serialPortEventListener;
        if (!this.MonitorThreadAlive) {
            this.MonitorThreadLock = true;
            this.monThread = new MonitorThread(this);
            this.monThread.start();
            waitForTheNativeCodeSilly();
            this.MonitorThreadAlive = true;
        }
    }

    @Override // gnu.io.SerialPort
    public void removeEventListener() {
        waitForTheNativeCodeSilly();
        if (this.monThreadisInterrupted) {
            f12219z.reportln("\tRXTXPort:removeEventListener() already interrupted");
            this.monThread = null;
            this.SPEventListener = null;
            return;
        }
        if (this.monThread != null && this.monThread.isAlive()) {
            this.monThreadisInterrupted = true;
            interruptEventLoop();
            try {
                this.monThread.join(3000L);
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.monThread = null;
        this.SPEventListener = null;
        this.MonitorThreadLock = false;
        this.MonitorThreadAlive = false;
        this.monThreadisInterrupted = true;
        f12219z.reportln("RXTXPort:removeEventListener() returning");
    }

    protected void waitForTheNativeCodeSilly() {
        while (this.MonitorThreadLock) {
            try {
                Thread.sleep(5L);
            } catch (Exception e2) {
            }
        }
    }

    @Override // gnu.io.SerialPort
    public void notifyOnDataAvailable(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 1, z2);
        this.monThread.Data = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnOutputEmpty(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 2, z2);
        this.monThread.Output = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnCTS(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 3, z2);
        this.monThread.CTS = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnDSR(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 4, z2);
        this.monThread.DSR = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnRingIndicator(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 5, z2);
        this.monThread.RI = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnCarrierDetect(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 6, z2);
        this.monThread.CD = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnOverrunError(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 7, z2);
        this.monThread.OE = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnParityError(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 8, z2);
        this.monThread.PE = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnFramingError(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 9, z2);
        this.monThread.FE = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.SerialPort
    public void notifyOnBreakInterrupt(boolean z2) {
        waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        nativeSetEventFlag(this.fd, 10, z2);
        this.monThread.BI = z2;
        this.MonitorThreadLock = false;
    }

    @Override // gnu.io.CommPort
    public void close() {
        synchronized (this) {
            while (this.IOLocked > 0) {
                try {
                    wait(500L);
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            if (this.closeLock) {
                return;
            }
            this.closeLock = true;
            if (this.fd <= 0) {
                f12219z.reportln("RXTXPort:close detected bad File Descriptor");
                return;
            }
            setDTR(false);
            setDSR(false);
            if (!this.monThreadisInterrupted) {
                removeEventListener();
            }
            nativeClose(this.name);
            super.close();
            this.fd = 0;
            this.closeLock = false;
        }
    }

    protected void finalize() {
        if (this.fd > 0) {
            close();
        }
        f12219z.finalize();
    }

    /* loaded from: RXTXcomm.jar:gnu/io/RXTXPort$SerialOutputStream.class */
    class SerialOutputStream extends OutputStream {
        private final RXTXPort this$0;

        SerialOutputStream(RXTXPort rXTXPort) {
            this.this$0 = rXTXPort;
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            if (this.this$0.speed == 0 || this.this$0.monThreadisInterrupted) {
                return;
            }
            synchronized (this.this$0.IOLockedMutex) {
                this.this$0.IOLocked++;
            }
            try {
                this.this$0.waitForTheNativeCodeSilly();
                if (this.this$0.fd == 0) {
                    throw new IOException();
                }
                this.this$0.writeByte(i2, this.this$0.monThreadisInterrupted);
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                }
            } catch (Throwable th) {
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                    throw th;
                }
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            if (this.this$0.speed != 0 && !this.this$0.monThreadisInterrupted) {
                if (this.this$0.fd == 0) {
                    throw new IOException();
                }
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked++;
                }
                try {
                    this.this$0.waitForTheNativeCodeSilly();
                    this.this$0.writeArray(bArr, 0, bArr.length, this.this$0.monThreadisInterrupted);
                    synchronized (this.this$0.IOLockedMutex) {
                        this.this$0.IOLocked--;
                    }
                } catch (Throwable th) {
                    synchronized (this.this$0.IOLockedMutex) {
                        this.this$0.IOLocked--;
                        throw th;
                    }
                }
            }
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            if (this.this$0.speed == 0) {
                return;
            }
            if (i2 + i3 > bArr.length) {
                throw new IndexOutOfBoundsException("Invalid offset/length passed to read");
            }
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            if (this.this$0.monThreadisInterrupted) {
                return;
            }
            synchronized (this.this$0.IOLockedMutex) {
                this.this$0.IOLocked++;
            }
            try {
                this.this$0.waitForTheNativeCodeSilly();
                this.this$0.writeArray(bArr2, 0, i3, this.this$0.monThreadisInterrupted);
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                }
            } catch (Throwable th) {
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                    throw th;
                }
            }
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            if (this.this$0.speed == 0) {
                return;
            }
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            if (this.this$0.monThreadisInterrupted) {
                return;
            }
            synchronized (this.this$0.IOLockedMutex) {
                this.this$0.IOLocked++;
            }
            try {
                this.this$0.waitForTheNativeCodeSilly();
                if (this.this$0.nativeDrain(this.this$0.monThreadisInterrupted)) {
                    this.this$0.sendEvent(2, true);
                }
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                }
            } catch (Throwable th) {
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                    throw th;
                }
            }
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/RXTXPort$SerialInputStream.class */
    class SerialInputStream extends InputStream {
        private final RXTXPort this$0;

        SerialInputStream(RXTXPort rXTXPort) {
            this.this$0 = rXTXPort;
        }

        @Override // java.io.InputStream
        public synchronized int read() throws IOException {
            if (this.this$0.fd == 0) {
                throw new IOException();
            }
            if (this.this$0.monThreadisInterrupted) {
                RXTXPort.f12219z.reportln("+++++++++ read() monThreadisInterrupted");
            }
            synchronized (this.this$0.IOLockedMutex) {
                this.this$0.IOLocked++;
            }
            try {
                this.this$0.waitForTheNativeCodeSilly();
                int i2 = this.this$0.readByte();
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                }
                return i2;
            } catch (Throwable th) {
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                    throw th;
                }
            }
        }

        @Override // java.io.InputStream
        public synchronized int read(byte[] bArr) throws IOException {
            if (this.this$0.monThreadisInterrupted) {
                return 0;
            }
            synchronized (this.this$0.IOLockedMutex) {
                this.this$0.IOLocked++;
            }
            try {
                this.this$0.waitForTheNativeCodeSilly();
                int i2 = read(bArr, 0, bArr.length);
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                }
                return i2;
            } catch (Throwable th) {
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                    throw th;
                }
            }
        }

        @Override // java.io.InputStream
        public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
            int iMin;
            if (this.this$0.fd == 0) {
                RXTXPort.f12219z.reportln("+++++++ IOException()\n");
                throw new IOException();
            }
            if (bArr == null) {
                RXTXPort.f12219z.reportln("+++++++ NullPointerException()\n");
                throw new NullPointerException();
            }
            if (i2 < 0 || i3 < 0 || i2 + i3 > bArr.length) {
                RXTXPort.f12219z.reportln("+++++++ IndexOutOfBoundsException()\n");
                throw new IndexOutOfBoundsException();
            }
            if (i3 != 0) {
                if (this.this$0.threshold == 0) {
                    int iNativeavailable = this.this$0.nativeavailable();
                    if (iNativeavailable == 0) {
                        iMin = 1;
                    } else {
                        iMin = Math.min(i3, iNativeavailable);
                    }
                } else {
                    iMin = Math.min(i3, this.this$0.threshold);
                }
                if (this.this$0.monThreadisInterrupted) {
                    return 0;
                }
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked++;
                }
                try {
                    this.this$0.waitForTheNativeCodeSilly();
                    int array = this.this$0.readArray(bArr, i2, iMin);
                    synchronized (this.this$0.IOLockedMutex) {
                        this.this$0.IOLocked--;
                    }
                    return array;
                } catch (Throwable th) {
                    synchronized (this.this$0.IOLockedMutex) {
                        this.this$0.IOLocked--;
                        throw th;
                    }
                }
            }
            return 0;
        }

        public synchronized int read(byte[] bArr, int i2, int i3, byte[] bArr2) throws IOException {
            int iMin;
            if (this.this$0.fd == 0) {
                RXTXPort.f12219z.reportln("+++++++ IOException()\n");
                throw new IOException();
            }
            if (bArr == null) {
                RXTXPort.f12219z.reportln("+++++++ NullPointerException()\n");
                throw new NullPointerException();
            }
            if (i2 < 0 || i3 < 0 || i2 + i3 > bArr.length) {
                RXTXPort.f12219z.reportln("+++++++ IndexOutOfBoundsException()\n");
                throw new IndexOutOfBoundsException();
            }
            if (i3 != 0) {
                if (this.this$0.threshold == 0) {
                    int iNativeavailable = this.this$0.nativeavailable();
                    if (iNativeavailable == 0) {
                        iMin = 1;
                    } else {
                        iMin = Math.min(i3, iNativeavailable);
                    }
                } else {
                    iMin = Math.min(i3, this.this$0.threshold);
                }
                if (this.this$0.monThreadisInterrupted) {
                    return 0;
                }
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked++;
                }
                try {
                    this.this$0.waitForTheNativeCodeSilly();
                    int terminatedArray = this.this$0.readTerminatedArray(bArr, i2, iMin, bArr2);
                    synchronized (this.this$0.IOLockedMutex) {
                        this.this$0.IOLocked--;
                    }
                    return terminatedArray;
                } catch (Throwable th) {
                    synchronized (this.this$0.IOLockedMutex) {
                        this.this$0.IOLocked--;
                        throw th;
                    }
                }
            }
            return 0;
        }

        @Override // java.io.InputStream
        public synchronized int available() throws IOException {
            if (this.this$0.monThreadisInterrupted) {
                return 0;
            }
            synchronized (this.this$0.IOLockedMutex) {
                this.this$0.IOLocked++;
            }
            try {
                int iNativeavailable = this.this$0.nativeavailable();
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                }
                return iNativeavailable;
            } catch (Throwable th) {
                synchronized (this.this$0.IOLockedMutex) {
                    this.this$0.IOLocked--;
                    throw th;
                }
            }
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/RXTXPort$MonitorThread.class */
    class MonitorThread extends Thread {
        private volatile boolean CTS = false;
        private volatile boolean DSR = false;
        private volatile boolean RI = false;
        private volatile boolean CD = false;
        private volatile boolean OE = false;
        private volatile boolean PE = false;
        private volatile boolean FE = false;
        private volatile boolean BI = false;
        private volatile boolean Data = false;
        private volatile boolean Output = false;
        private final RXTXPort this$0;

        MonitorThread(RXTXPort rXTXPort) {
            this.this$0 = rXTXPort;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.this$0.monThreadisInterrupted = false;
            this.this$0.eventLoop();
        }

        protected void finalize() throws Throwable {
        }
    }

    public void setRcvFifoTrigger(int i2) {
    }

    public static int staticGetBaudRate(String str) throws UnsupportedCommOperationException {
        return nativeStaticGetBaudRate(str);
    }

    public static int staticGetDataBits(String str) throws UnsupportedCommOperationException {
        return nativeStaticGetDataBits(str);
    }

    public static int staticGetParity(String str) throws UnsupportedCommOperationException {
        return nativeStaticGetParity(str);
    }

    public static int staticGetStopBits(String str) throws UnsupportedCommOperationException {
        return nativeStaticGetStopBits(str);
    }

    public static void staticSetSerialPortParams(String str, int i2, int i3, int i4, int i5) throws UnsupportedCommOperationException {
        nativeStaticSetSerialPortParams(str, i2, i3, i4, i5);
    }

    public static boolean staticSetDSR(String str, boolean z2) throws UnsupportedCommOperationException {
        return nativeStaticSetDSR(str, z2);
    }

    public static boolean staticSetDTR(String str, boolean z2) throws UnsupportedCommOperationException {
        return nativeStaticSetDTR(str, z2);
    }

    public static boolean staticSetRTS(String str, boolean z2) throws UnsupportedCommOperationException {
        return nativeStaticSetRTS(str, z2);
    }

    public static boolean staticIsRTS(String str) throws UnsupportedCommOperationException {
        return nativeStaticIsRTS(str);
    }

    public static boolean staticIsCD(String str) throws UnsupportedCommOperationException {
        return nativeStaticIsCD(str);
    }

    public static boolean staticIsCTS(String str) throws UnsupportedCommOperationException {
        return nativeStaticIsCTS(str);
    }

    public static boolean staticIsDSR(String str) throws UnsupportedCommOperationException {
        return nativeStaticIsDSR(str);
    }

    public static boolean staticIsDTR(String str) throws UnsupportedCommOperationException {
        return nativeStaticIsDTR(str);
    }

    public static boolean staticIsRI(String str) throws UnsupportedCommOperationException {
        return nativeStaticIsRI(str);
    }

    @Override // gnu.io.SerialPort
    public byte getParityErrorChar() throws UnsupportedCommOperationException {
        return nativeGetParityErrorChar();
    }

    @Override // gnu.io.SerialPort
    public boolean setParityErrorChar(byte b2) throws UnsupportedCommOperationException {
        return nativeSetParityErrorChar(b2);
    }

    @Override // gnu.io.SerialPort
    public byte getEndOfInputChar() throws UnsupportedCommOperationException {
        return nativeGetEndOfInputChar();
    }

    @Override // gnu.io.SerialPort
    public boolean setEndOfInputChar(byte b2) throws UnsupportedCommOperationException {
        return nativeSetEndOfInputChar(b2);
    }

    @Override // gnu.io.SerialPort
    public boolean setUARTType(String str, boolean z2) throws UnsupportedCommOperationException {
        return nativeSetUartType(str, z2);
    }

    @Override // gnu.io.SerialPort
    public String getUARTType() throws UnsupportedCommOperationException {
        return nativeGetUartType();
    }

    @Override // gnu.io.SerialPort
    public boolean setBaudBase(int i2) throws UnsupportedCommOperationException, IOException {
        return nativeSetBaudBase(i2);
    }

    @Override // gnu.io.SerialPort
    public int getBaudBase() throws UnsupportedCommOperationException, IOException {
        return nativeGetBaudBase();
    }

    @Override // gnu.io.SerialPort
    public boolean setDivisor(int i2) throws UnsupportedCommOperationException, IOException {
        return nativeSetDivisor(i2);
    }

    @Override // gnu.io.SerialPort
    public int getDivisor() throws UnsupportedCommOperationException, IOException {
        return nativeGetDivisor();
    }

    @Override // gnu.io.SerialPort
    public boolean setLowLatency() throws UnsupportedCommOperationException {
        return nativeSetLowLatency();
    }

    @Override // gnu.io.SerialPort
    public boolean getLowLatency() throws UnsupportedCommOperationException {
        return nativeGetLowLatency();
    }

    @Override // gnu.io.SerialPort
    public boolean setCallOutHangup(boolean z2) throws UnsupportedCommOperationException {
        return nativeSetCallOutHangup(z2);
    }

    @Override // gnu.io.SerialPort
    public boolean getCallOutHangup() throws UnsupportedCommOperationException {
        return nativeGetCallOutHangup();
    }

    public boolean clearCommInput() throws UnsupportedCommOperationException {
        return nativeClearCommInput();
    }
}
