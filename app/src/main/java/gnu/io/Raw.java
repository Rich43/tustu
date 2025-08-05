package gnu.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/* loaded from: RXTXcomm.jar:gnu/io/Raw.class */
final class Raw extends RawPort {
    private int ciAddress;
    static boolean dsrFlag;
    private RawPortEventListener SPEventListener;
    private MonitorThread monThread;
    private final RawOutputStream out = new RawOutputStream(this);
    private final RawInputStream in = new RawInputStream(this);
    private int speed = jssc.SerialPort.BAUDRATE_9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int parity = 0;
    private int flowmode = 0;
    private int timeout = 0;
    private int threshold = 0;
    private int InputBuffer = 0;
    private int OutputBuffer = 0;
    private int dataAvailable = 0;

    private static native void Initialize();

    private native int open(int i2) throws PortInUseException;

    private native void nativeSetRawPortParams(int i2, int i3, int i4, int i5) throws UnsupportedCommOperationException;

    native void setflowcontrol(int i2) throws IOException;

    public native int NativegetReceiveTimeout();

    public native boolean NativeisReceiveTimeoutEnabled();

    public native void NativeEnableReceiveTimeoutThreshold(int i2, int i3, int i4);

    public native boolean isDTR();

    public native void setDTR(boolean z2);

    public native void setRTS(boolean z2);

    private native void setDSR(boolean z2);

    public native boolean isCTS();

    public native boolean isDSR();

    public native boolean isCD();

    public native boolean isRI();

    public native boolean isRTS();

    public native void sendBreak(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void writeByte(int i2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void writeArray(byte[] bArr, int i2, int i3) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void drain() throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeavailable() throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native int readByte() throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native int readArray(byte[] bArr, int i2, int i3) throws IOException;

    native void eventLoop();

    private native int nativeClose();

    static {
        System.loadLibrary("rxtxRaw");
        Initialize();
        dsrFlag = false;
    }

    public Raw(String str) throws PortInUseException {
        this.ciAddress = Integer.parseInt(str);
        open(this.ciAddress);
    }

    @Override // gnu.io.CommPort
    public OutputStream getOutputStream() {
        return this.out;
    }

    @Override // gnu.io.CommPort
    public InputStream getInputStream() {
        return this.in;
    }

    @Override // gnu.io.RawPort
    public void setRawPortParams(int i2, int i3, int i4, int i5) throws UnsupportedCommOperationException {
        nativeSetRawPortParams(i2, i3, i4, i5);
        this.speed = i2;
        this.dataBits = i3;
        this.stopBits = i4;
        this.parity = i5;
    }

    public int getBaudRate() {
        return this.speed;
    }

    public int getDataBits() {
        return this.dataBits;
    }

    public int getStopBits() {
        return this.stopBits;
    }

    public int getParity() {
        return this.parity;
    }

    public void setFlowControlMode(int i2) {
        try {
            setflowcontrol(i2);
            this.flowmode = i2;
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

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
        enableReceiveTimeout(0);
    }

    @Override // gnu.io.CommPort
    public void enableReceiveTimeout(int i2) {
        if (i2 >= 0) {
            this.timeout = i2;
            NativeEnableReceiveTimeoutThreshold(i2, this.threshold, this.InputBuffer);
        } else {
            System.out.println("Invalid timeout");
        }
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
        } else {
            System.out.println("Invalid Threshold");
        }
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
        this.InputBuffer = i2;
    }

    @Override // gnu.io.CommPort
    public int getInputBufferSize() {
        return this.InputBuffer;
    }

    @Override // gnu.io.CommPort
    public void setOutputBufferSize(int i2) {
        this.OutputBuffer = i2;
    }

    @Override // gnu.io.CommPort
    public int getOutputBufferSize() {
        return this.OutputBuffer;
    }

    public void sendEvent(int i2, boolean z2) {
        switch (i2) {
            case 1:
                this.dataAvailable = 1;
                if (!this.monThread.Data) {
                    return;
                }
                break;
            case 2:
                if (!this.monThread.Output) {
                    return;
                }
                break;
            case 3:
                if (!this.monThread.CTS) {
                    return;
                }
                break;
            case 4:
                if (!this.monThread.DSR) {
                    return;
                }
                break;
            case 5:
                if (!this.monThread.RI) {
                    return;
                }
                break;
            case 6:
                if (!this.monThread.CD) {
                    return;
                }
                break;
            case 7:
                if (!this.monThread.OE) {
                    return;
                }
                break;
            case 8:
                if (!this.monThread.PE) {
                    return;
                }
                break;
            case 9:
                if (!this.monThread.FE) {
                    return;
                }
                break;
            case 10:
                if (!this.monThread.BI) {
                    return;
                }
                break;
            default:
                System.err.println(new StringBuffer().append("unknown event:").append(i2).toString());
                return;
        }
        RawPortEvent rawPortEvent = new RawPortEvent(this, i2, !z2, z2);
        if (this.SPEventListener != null) {
            this.SPEventListener.RawEvent(rawPortEvent);
        }
    }

    @Override // gnu.io.RawPort
    public void addEventListener(RawPortEventListener rawPortEventListener) throws TooManyListenersException {
        if (this.SPEventListener != null) {
            throw new TooManyListenersException();
        }
        this.SPEventListener = rawPortEventListener;
        this.monThread = new MonitorThread(this);
        this.monThread.start();
    }

    @Override // gnu.io.RawPort
    public void removeEventListener() {
        this.SPEventListener = null;
        if (this.monThread != null) {
            this.monThread.interrupt();
            this.monThread = null;
        }
    }

    public void notifyOnDataAvailable(boolean z2) {
        this.monThread.Data = z2;
    }

    public void notifyOnOutputEmpty(boolean z2) {
        this.monThread.Output = z2;
    }

    public void notifyOnCTS(boolean z2) {
        this.monThread.CTS = z2;
    }

    public void notifyOnDSR(boolean z2) {
        this.monThread.DSR = z2;
    }

    public void notifyOnRingIndicator(boolean z2) {
        this.monThread.RI = z2;
    }

    public void notifyOnCarrierDetect(boolean z2) {
        this.monThread.CD = z2;
    }

    public void notifyOnOverrunError(boolean z2) {
        this.monThread.OE = z2;
    }

    public void notifyOnParityError(boolean z2) {
        this.monThread.PE = z2;
    }

    public void notifyOnFramingError(boolean z2) {
        this.monThread.FE = z2;
    }

    public void notifyOnBreakInterrupt(boolean z2) {
        this.monThread.BI = z2;
    }

    @Override // gnu.io.CommPort
    public void close() {
        setDTR(false);
        setDSR(false);
        nativeClose();
        super.close();
        this.ciAddress = 0;
    }

    protected void finalize() {
        close();
    }

    /* loaded from: RXTXcomm.jar:gnu/io/Raw$RawOutputStream.class */
    class RawOutputStream extends OutputStream {
        private final Raw this$0;

        RawOutputStream(Raw raw) {
            this.this$0 = raw;
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.this$0.writeByte(i2);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            this.this$0.writeArray(bArr, 0, bArr.length);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.this$0.writeArray(bArr, i2, i3);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.this$0.drain();
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/Raw$RawInputStream.class */
    class RawInputStream extends InputStream {
        private final Raw this$0;

        RawInputStream(Raw raw) {
            this.this$0 = raw;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            this.this$0.dataAvailable = 0;
            return this.this$0.readByte();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            this.this$0.dataAvailable = 0;
            int i4 = 0;
            int[] iArr = {bArr.length, this.this$0.InputBuffer, i3};
            while (iArr[i4] == 0 && i4 < iArr.length) {
                i4++;
            }
            int iMin = iArr[i4];
            while (i4 < iArr.length) {
                if (iArr[i4] > 0) {
                    iMin = Math.min(iMin, iArr[i4]);
                }
                i4++;
            }
            int iMin2 = Math.min(iMin, this.this$0.threshold);
            if (iMin2 == 0) {
                iMin2 = 1;
            }
            available();
            return this.this$0.readArray(bArr, i2, iMin2);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.this$0.nativeavailable();
        }
    }

    /* loaded from: RXTXcomm.jar:gnu/io/Raw$MonitorThread.class */
    class MonitorThread extends Thread {
        private boolean CTS = false;
        private boolean DSR = false;
        private boolean RI = false;
        private boolean CD = false;
        private boolean OE = false;
        private boolean PE = false;
        private boolean FE = false;
        private boolean BI = false;
        private boolean Data = false;
        private boolean Output = false;
        private final Raw this$0;

        MonitorThread(Raw raw) {
            this.this$0 = raw;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.this$0.eventLoop();
        }
    }

    public String getVersion() {
        return "$Id: Raw.java,v 1.1.2.17 2008-09-14 22:29:30 jarvi Exp $";
    }
}
