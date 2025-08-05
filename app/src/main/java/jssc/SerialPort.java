package jssc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import sun.java2d.marlin.MarlinConst;

/* JADX WARN: Classes with same name are omitted:
  jssc.jar:jssc/SerialPort.class
 */
/* loaded from: jssc2.8.jar:jssc/SerialPort.class */
public class SerialPort {
    private SerialPortEventListener eventListener;
    private long portHandle;
    private String portName;
    public static final int BAUDRATE_110 = 110;
    public static final int BAUDRATE_300 = 300;
    public static final int BAUDRATE_600 = 600;
    public static final int BAUDRATE_1200 = 1200;
    public static final int BAUDRATE_4800 = 4800;
    public static final int BAUDRATE_9600 = 9600;
    public static final int BAUDRATE_14400 = 14400;
    public static final int BAUDRATE_19200 = 19200;
    public static final int BAUDRATE_38400 = 38400;
    public static final int BAUDRATE_57600 = 57600;
    public static final int BAUDRATE_115200 = 115200;
    public static final int BAUDRATE_128000 = 128000;
    public static final int BAUDRATE_256000 = 256000;
    public static final int DATABITS_5 = 5;
    public static final int DATABITS_6 = 6;
    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;
    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_2 = 2;
    public static final int STOPBITS_1_5 = 3;
    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;
    public static final int PARITY_MARK = 3;
    public static final int PARITY_SPACE = 4;
    public static final int PURGE_RXABORT = 2;
    public static final int PURGE_RXCLEAR = 8;
    public static final int PURGE_TXABORT = 1;
    public static final int PURGE_TXCLEAR = 4;
    public static final int MASK_RXCHAR = 1;
    public static final int MASK_RXFLAG = 2;
    public static final int MASK_TXEMPTY = 4;
    public static final int MASK_CTS = 8;
    public static final int MASK_DSR = 16;
    public static final int MASK_RLSD = 32;
    public static final int MASK_BREAK = 64;
    public static final int MASK_ERR = 128;
    public static final int MASK_RING = 256;
    public static final int FLOWCONTROL_NONE = 0;
    public static final int FLOWCONTROL_RTSCTS_IN = 1;
    public static final int FLOWCONTROL_RTSCTS_OUT = 2;
    public static final int FLOWCONTROL_XONXOFF_IN = 4;
    public static final int FLOWCONTROL_XONXOFF_OUT = 8;
    public static final int ERROR_FRAME = 8;
    public static final int ERROR_OVERRUN = 2;
    public static final int ERROR_PARITY = 4;
    private static final int PARAMS_FLAG_IGNPAR = 1;
    private static final int PARAMS_FLAG_PARMRK = 2;
    private int linuxMask;
    private EventThread eventThread;
    private boolean portOpened = false;
    private boolean maskAssigned = false;
    private boolean eventListenerAdded = false;
    private Method methodErrorOccurred = null;
    private SerialNativeInterface serialInterface = new SerialNativeInterface();

    public SerialPort(String portName) {
        this.portName = portName;
    }

    public String getPortName() {
        return this.portName;
    }

    public boolean isOpened() {
        return this.portOpened;
    }

    public boolean openPort() throws SerialPortException {
        if (this.portOpened) {
            throw new SerialPortException(this.portName, "openPort()", SerialPortException.TYPE_PORT_ALREADY_OPENED);
        }
        if (this.portName != null) {
            boolean useTIOCEXCL = System.getProperty(SerialNativeInterface.PROPERTY_JSSC_NO_TIOCEXCL) == null && System.getProperty(SerialNativeInterface.PROPERTY_JSSC_NO_TIOCEXCL.toLowerCase()) == null;
            this.portHandle = this.serialInterface.openPort(this.portName, useTIOCEXCL);
            if (this.portHandle == -1) {
                throw new SerialPortException(this.portName, "openPort()", SerialPortException.TYPE_PORT_BUSY);
            }
            if (this.portHandle == -2) {
                throw new SerialPortException(this.portName, "openPort()", SerialPortException.TYPE_PORT_NOT_FOUND);
            }
            if (this.portHandle == -3) {
                throw new SerialPortException(this.portName, "openPort()", SerialPortException.TYPE_PERMISSION_DENIED);
            }
            if (this.portHandle == -4) {
                throw new SerialPortException(this.portName, "openPort()", SerialPortException.TYPE_INCORRECT_SERIAL_PORT);
            }
            this.portOpened = true;
            return true;
        }
        throw new SerialPortException(this.portName, "openPort()", SerialPortException.TYPE_NULL_NOT_PERMITTED);
    }

    public boolean setParams(int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException {
        return setParams(baudRate, dataBits, stopBits, parity, true, true);
    }

    public boolean setParams(int baudRate, int dataBits, int stopBits, int parity, boolean setRTS, boolean setDTR) throws SerialPortException {
        checkPortOpened("setParams()");
        if (stopBits == 1) {
            stopBits = 0;
        } else if (stopBits == 3) {
            stopBits = 1;
        }
        int flags = 0;
        if (System.getProperty(SerialNativeInterface.PROPERTY_JSSC_IGNPAR) != null || System.getProperty(SerialNativeInterface.PROPERTY_JSSC_IGNPAR.toLowerCase()) != null) {
            flags = 0 | 1;
        }
        if (System.getProperty(SerialNativeInterface.PROPERTY_JSSC_PARMRK) != null || System.getProperty(SerialNativeInterface.PROPERTY_JSSC_PARMRK.toLowerCase()) != null) {
            flags |= 2;
        }
        return this.serialInterface.setParams(this.portHandle, baudRate, dataBits, stopBits, parity, setRTS, setDTR, flags);
    }

    public boolean purgePort(int flags) throws SerialPortException {
        checkPortOpened("purgePort()");
        return this.serialInterface.purgePort(this.portHandle, flags);
    }

    public boolean setEventsMask(int mask) throws SerialPortException {
        checkPortOpened("setEventsMask()");
        if (SerialNativeInterface.getOsType() == 0 || SerialNativeInterface.getOsType() == 2 || SerialNativeInterface.getOsType() == 3) {
            this.linuxMask = mask;
            if (mask > 0) {
                this.maskAssigned = true;
                return true;
            }
            this.maskAssigned = false;
            return true;
        }
        boolean returnValue = this.serialInterface.setEventsMask(this.portHandle, mask);
        if (!returnValue) {
            throw new SerialPortException(this.portName, "setEventsMask()", SerialPortException.TYPE_CANT_SET_MASK);
        }
        if (mask > 0) {
            this.maskAssigned = true;
        } else {
            this.maskAssigned = false;
        }
        return returnValue;
    }

    public int getEventsMask() throws SerialPortException {
        checkPortOpened("getEventsMask()");
        if (SerialNativeInterface.getOsType() == 0 || SerialNativeInterface.getOsType() == 2 || SerialNativeInterface.getOsType() == 3) {
            return this.linuxMask;
        }
        return this.serialInterface.getEventsMask(this.portHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLinuxMask() {
        return this.linuxMask;
    }

    public boolean setRTS(boolean enabled) throws SerialPortException {
        checkPortOpened("setRTS()");
        return this.serialInterface.setRTS(this.portHandle, enabled);
    }

    public boolean setDTR(boolean enabled) throws SerialPortException {
        checkPortOpened("setDTR()");
        return this.serialInterface.setDTR(this.portHandle, enabled);
    }

    public boolean writeBytes(byte[] buffer) throws SerialPortException {
        checkPortOpened("writeBytes()");
        return this.serialInterface.writeBytes(this.portHandle, buffer);
    }

    public boolean writeByte(byte singleByte) throws SerialPortException {
        checkPortOpened("writeByte()");
        return writeBytes(new byte[]{singleByte});
    }

    public boolean writeString(String string) throws SerialPortException {
        checkPortOpened("writeString()");
        return writeBytes(string.getBytes());
    }

    public boolean writeString(String string, String charsetName) throws SerialPortException, UnsupportedEncodingException {
        checkPortOpened("writeString()");
        return writeBytes(string.getBytes(charsetName));
    }

    public boolean writeInt(int singleInt) throws SerialPortException {
        checkPortOpened("writeInt()");
        return writeBytes(new byte[]{(byte) singleInt});
    }

    public boolean writeIntArray(int[] buffer) throws SerialPortException {
        checkPortOpened("writeIntArray()");
        byte[] byteArray = new byte[buffer.length];
        for (int i2 = 0; i2 < buffer.length; i2++) {
            byteArray[i2] = (byte) buffer[i2];
        }
        return writeBytes(byteArray);
    }

    public byte[] readBytes(int byteCount) throws SerialPortException {
        checkPortOpened("readBytes()");
        return this.serialInterface.readBytes(this.portHandle, byteCount);
    }

    public String readString(int byteCount) throws SerialPortException {
        checkPortOpened("readString()");
        return new String(readBytes(byteCount));
    }

    public String readHexString(int byteCount) throws SerialPortException {
        checkPortOpened("readHexString()");
        return readHexString(byteCount, " ");
    }

    public String readHexString(int byteCount, String separator) throws SerialPortException {
        checkPortOpened("readHexString()");
        String[] strBuffer = readHexStringArray(byteCount);
        String returnString = "";
        boolean insertSeparator = false;
        for (String value : strBuffer) {
            if (insertSeparator) {
                returnString = returnString + separator;
            }
            returnString = returnString + value;
            insertSeparator = true;
        }
        return returnString;
    }

    public String[] readHexStringArray(int byteCount) throws SerialPortException {
        checkPortOpened("readHexStringArray()");
        int[] intBuffer = readIntArray(byteCount);
        String[] strBuffer = new String[intBuffer.length];
        for (int i2 = 0; i2 < intBuffer.length; i2++) {
            String value = Integer.toHexString(intBuffer[i2]).toUpperCase();
            if (value.length() == 1) {
                value = "0" + value;
            }
            strBuffer[i2] = value;
        }
        return strBuffer;
    }

    public int[] readIntArray(int byteCount) throws SerialPortException {
        checkPortOpened("readIntArray()");
        byte[] buffer = readBytes(byteCount);
        int[] intBuffer = new int[buffer.length];
        for (int i2 = 0; i2 < buffer.length; i2++) {
            if (buffer[i2] < 0) {
                intBuffer[i2] = 256 + buffer[i2];
            } else {
                intBuffer[i2] = buffer[i2];
            }
        }
        return intBuffer;
    }

    private void waitBytesWithTimeout(String methodName, int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("waitBytesWithTimeout()");
        boolean timeIsOut = true;
        long startTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - startTime >= timeout) {
                break;
            }
            if (getInputBufferBytesCount() >= byteCount) {
                timeIsOut = false;
                break;
            }
            try {
                Thread.sleep(0L, 100);
            } catch (InterruptedException e2) {
            }
        }
        if (timeIsOut) {
            throw new SerialPortTimeoutException(this.portName, methodName, timeout);
        }
    }

    public byte[] readBytes(int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("readBytes()");
        waitBytesWithTimeout("readBytes()", byteCount, timeout);
        return readBytes(byteCount);
    }

    public String readString(int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("readString()");
        waitBytesWithTimeout("readString()", byteCount, timeout);
        return readString(byteCount);
    }

    public String readHexString(int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("readHexString()");
        waitBytesWithTimeout("readHexString()", byteCount, timeout);
        return readHexString(byteCount);
    }

    public String readHexString(int byteCount, String separator, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("readHexString()");
        waitBytesWithTimeout("readHexString()", byteCount, timeout);
        return readHexString(byteCount, separator);
    }

    public String[] readHexStringArray(int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("readHexStringArray()");
        waitBytesWithTimeout("readHexStringArray()", byteCount, timeout);
        return readHexStringArray(byteCount);
    }

    public int[] readIntArray(int byteCount, int timeout) throws SerialPortException, SerialPortTimeoutException {
        checkPortOpened("readIntArray()");
        waitBytesWithTimeout("readIntArray()", byteCount, timeout);
        return readIntArray(byteCount);
    }

    public byte[] readBytes() throws SerialPortException {
        checkPortOpened("readBytes()");
        int byteCount = getInputBufferBytesCount();
        if (byteCount <= 0) {
            return null;
        }
        return readBytes(byteCount);
    }

    public String readString() throws SerialPortException {
        checkPortOpened("readString()");
        int byteCount = getInputBufferBytesCount();
        if (byteCount <= 0) {
            return null;
        }
        return readString(byteCount);
    }

    public String readHexString() throws SerialPortException {
        checkPortOpened("readHexString()");
        int byteCount = getInputBufferBytesCount();
        if (byteCount <= 0) {
            return null;
        }
        return readHexString(byteCount);
    }

    public String readHexString(String separator) throws SerialPortException {
        checkPortOpened("readHexString()");
        int byteCount = getInputBufferBytesCount();
        if (byteCount <= 0) {
            return null;
        }
        return readHexString(byteCount, separator);
    }

    public String[] readHexStringArray() throws SerialPortException {
        checkPortOpened("readHexStringArray()");
        int byteCount = getInputBufferBytesCount();
        if (byteCount <= 0) {
            return null;
        }
        return readHexStringArray(byteCount);
    }

    public int[] readIntArray() throws SerialPortException {
        checkPortOpened("readIntArray()");
        int byteCount = getInputBufferBytesCount();
        if (byteCount <= 0) {
            return null;
        }
        return readIntArray(byteCount);
    }

    public int getInputBufferBytesCount() throws SerialPortException {
        checkPortOpened("getInputBufferBytesCount()");
        return this.serialInterface.getBuffersBytesCount(this.portHandle)[0];
    }

    public int getOutputBufferBytesCount() throws SerialPortException {
        checkPortOpened("getOutputBufferBytesCount()");
        return this.serialInterface.getBuffersBytesCount(this.portHandle)[1];
    }

    public boolean setFlowControlMode(int mask) throws SerialPortException {
        checkPortOpened("setFlowControlMode()");
        return this.serialInterface.setFlowControlMode(this.portHandle, mask);
    }

    public int getFlowControlMode() throws SerialPortException {
        checkPortOpened("getFlowControlMode()");
        return this.serialInterface.getFlowControlMode(this.portHandle);
    }

    public boolean sendBreak(int duration) throws SerialPortException {
        checkPortOpened("sendBreak()");
        return this.serialInterface.sendBreak(this.portHandle, duration);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[][] waitEvents() {
        return this.serialInterface.waitEvents(this.portHandle);
    }

    private void checkPortOpened(String methodName) throws SerialPortException {
        if (!this.portOpened) {
            throw new SerialPortException(this.portName, methodName, SerialPortException.TYPE_PORT_NOT_OPENED);
        }
    }

    public int[] getLinesStatus() throws SerialPortException {
        checkPortOpened("getLinesStatus()");
        return this.serialInterface.getLinesStatus(this.portHandle);
    }

    public boolean isCTS() throws SerialPortException {
        checkPortOpened("isCTS()");
        if (this.serialInterface.getLinesStatus(this.portHandle)[0] == 1) {
            return true;
        }
        return false;
    }

    public boolean isDSR() throws SerialPortException {
        checkPortOpened("isDSR()");
        if (this.serialInterface.getLinesStatus(this.portHandle)[1] == 1) {
            return true;
        }
        return false;
    }

    public boolean isRING() throws SerialPortException {
        checkPortOpened("isRING()");
        if (this.serialInterface.getLinesStatus(this.portHandle)[2] == 1) {
            return true;
        }
        return false;
    }

    public boolean isRLSD() throws SerialPortException {
        checkPortOpened("isRLSD()");
        if (this.serialInterface.getLinesStatus(this.portHandle)[3] == 1) {
            return true;
        }
        return false;
    }

    public void addEventListener(SerialPortEventListener listener) throws SerialPortException {
        addEventListener(listener, 1, false);
    }

    public void addEventListener(SerialPortEventListener listener, int mask) throws SerialPortException {
        addEventListener(listener, mask, true);
    }

    private void addEventListener(SerialPortEventListener listener, int mask, boolean overwriteMask) throws SerialPortException {
        checkPortOpened("addEventListener()");
        if (!this.eventListenerAdded) {
            if ((this.maskAssigned && overwriteMask) || !this.maskAssigned) {
                setEventsMask(mask);
            }
            this.eventListener = listener;
            this.eventThread = getNewEventThread();
            this.eventThread.setName("EventThread " + this.portName);
            try {
                Method method = this.eventListener.getClass().getMethod("errorOccurred", SerialPortException.class);
                method.setAccessible(true);
                this.methodErrorOccurred = method;
            } catch (NoSuchMethodException e2) {
            } catch (SecurityException e3) {
            }
            this.eventThread.start();
            this.eventListenerAdded = true;
            return;
        }
        throw new SerialPortException(this.portName, "addEventListener()", SerialPortException.TYPE_LISTENER_ALREADY_ADDED);
    }

    private EventThread getNewEventThread() {
        if (SerialNativeInterface.getOsType() == 0 || SerialNativeInterface.getOsType() == 2 || SerialNativeInterface.getOsType() == 3) {
            return new LinuxEventThread();
        }
        return new EventThread();
    }

    public boolean removeEventListener() throws SerialPortException {
        checkPortOpened("removeEventListener()");
        if (!this.eventListenerAdded) {
            throw new SerialPortException(this.portName, "removeEventListener()", SerialPortException.TYPE_CANT_REMOVE_LISTENER);
        }
        this.eventThread.terminateThread();
        setEventsMask(0);
        if (Thread.currentThread().getId() != this.eventThread.getId() && this.eventThread.isAlive()) {
            try {
                this.eventThread.join(MarlinConst.statDump);
            } catch (InterruptedException e2) {
                throw new SerialPortException(this.portName, "removeEventListener()", SerialPortException.TYPE_LISTENER_THREAD_INTERRUPTED);
            }
        }
        this.methodErrorOccurred = null;
        this.eventListenerAdded = false;
        return true;
    }

    public boolean closePort() throws SerialPortException {
        checkPortOpened("closePort()");
        if (this.eventListenerAdded) {
            removeEventListener();
        }
        boolean returnValue = this.serialInterface.closePort(this.portHandle);
        if (returnValue) {
            this.maskAssigned = false;
            this.portOpened = false;
        }
        return returnValue;
    }

    /* JADX WARN: Classes with same name are omitted:
  jssc.jar:jssc/SerialPort$EventThread.class
 */
    /* loaded from: jssc2.8.jar:jssc/SerialPort$EventThread.class */
    private class EventThread extends Thread {
        private boolean threadTerminated;

        private EventThread() {
            this.threadTerminated = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.threadTerminated) {
                int[][] eventArray = SerialPort.this.waitEvents();
                for (int i2 = 0; i2 < eventArray.length; i2++) {
                    if (eventArray[i2][0] > 0 && !this.threadTerminated) {
                        SerialPort.this.eventListener.serialEvent(new SerialPortEvent(SerialPort.this.portName, eventArray[i2][0], eventArray[i2][1]));
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void terminateThread() {
            this.threadTerminated = true;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  jssc.jar:jssc/SerialPort$LinuxEventThread.class
 */
    /* loaded from: jssc2.8.jar:jssc/SerialPort$LinuxEventThread.class */
    private class LinuxEventThread extends EventThread {
        private final int INTERRUPT_BREAK = 512;
        private final int INTERRUPT_TX = 1024;
        private final int INTERRUPT_FRAME = 2048;
        private final int INTERRUPT_OVERRUN = 4096;
        private final int INTERRUPT_PARITY = 8192;
        private int interruptBreak;
        private int interruptTX;
        private int interruptFrame;
        private int interruptOverrun;
        private int interruptParity;
        private int preCTS;
        private int preDSR;
        private int preRLSD;
        private int preRING;

        public LinuxEventThread() {
            super();
            this.INTERRUPT_BREAK = 512;
            this.INTERRUPT_TX = 1024;
            this.INTERRUPT_FRAME = 2048;
            this.INTERRUPT_OVERRUN = 4096;
            this.INTERRUPT_PARITY = 8192;
            int[][] eventArray = SerialPort.this.waitEvents();
            for (int i2 = 0; i2 < eventArray.length; i2++) {
                int eventType = eventArray[i2][0];
                int eventValue = eventArray[i2][1];
                switch (eventType) {
                    case 8:
                        this.preCTS = eventValue;
                        break;
                    case 16:
                        this.preDSR = eventValue;
                        break;
                    case 32:
                        this.preRLSD = eventValue;
                        break;
                    case 256:
                        this.preRING = eventValue;
                        break;
                    case 512:
                        this.interruptBreak = eventValue;
                        break;
                    case 1024:
                        this.interruptTX = eventValue;
                        break;
                    case 2048:
                        this.interruptFrame = eventValue;
                        break;
                    case 4096:
                        this.interruptOverrun = eventValue;
                        break;
                    case 8192:
                        this.interruptParity = eventValue;
                        break;
                }
            }
        }

        @Override // jssc.SerialPort.EventThread, java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!((EventThread) this).threadTerminated) {
                int[][] eventArray = SerialPort.this.waitEvents();
                int mask = SerialPort.this.getLinuxMask();
                boolean interruptTxChanged = false;
                int errorMask = 0;
                for (int i2 = 0; i2 < eventArray.length; i2++) {
                    boolean sendEvent = false;
                    int eventType = eventArray[i2][0];
                    int eventValue = eventArray[i2][1];
                    if (eventType > 0 && !((EventThread) this).threadTerminated) {
                        switch (eventType) {
                            case 1:
                                if ((mask & 1) == 1 && eventValue > 0) {
                                    sendEvent = true;
                                    break;
                                }
                                break;
                            case 4:
                                if ((mask & 4) == 4 && eventValue == 0 && interruptTxChanged) {
                                    sendEvent = true;
                                    break;
                                }
                                break;
                            case 8:
                                if (eventValue != this.preCTS) {
                                    this.preCTS = eventValue;
                                    if ((mask & 8) == 8) {
                                        sendEvent = true;
                                        break;
                                    }
                                }
                                break;
                            case 16:
                                if (eventValue != this.preDSR) {
                                    this.preDSR = eventValue;
                                    if ((mask & 16) == 16) {
                                        sendEvent = true;
                                        break;
                                    }
                                }
                                break;
                            case 32:
                                if (eventValue != this.preRLSD) {
                                    this.preRLSD = eventValue;
                                    if ((mask & 32) == 32) {
                                        sendEvent = true;
                                        break;
                                    }
                                }
                                break;
                            case 256:
                                if (eventValue != this.preRING) {
                                    this.preRING = eventValue;
                                    if ((mask & 256) == 256) {
                                        sendEvent = true;
                                        break;
                                    }
                                }
                                break;
                            case 512:
                                if (eventValue != this.interruptBreak) {
                                    this.interruptBreak = eventValue;
                                    if ((mask & 64) == 64) {
                                        eventType = 64;
                                        eventValue = 0;
                                        sendEvent = true;
                                        break;
                                    }
                                }
                                break;
                            case 1024:
                                if (eventValue != this.interruptTX) {
                                    this.interruptTX = eventValue;
                                    interruptTxChanged = true;
                                    break;
                                }
                                break;
                            case 2048:
                                if (eventValue != this.interruptFrame) {
                                    this.interruptFrame = eventValue;
                                    errorMask |= 8;
                                    break;
                                }
                                break;
                            case 4096:
                                if (eventValue != this.interruptOverrun) {
                                    this.interruptOverrun = eventValue;
                                    errorMask |= 2;
                                    break;
                                }
                                break;
                            case 8192:
                                if (eventValue != this.interruptParity) {
                                    this.interruptParity = eventValue;
                                    errorMask |= 4;
                                }
                                if ((mask & 128) == 128 && errorMask != 0) {
                                    eventType = 128;
                                    eventValue = errorMask;
                                    sendEvent = true;
                                    break;
                                }
                                break;
                        }
                        if (sendEvent) {
                            SerialPort.this.eventListener.serialEvent(new SerialPortEvent(SerialPort.this.portName, eventType, eventValue));
                        }
                    }
                }
                try {
                    Thread.sleep(0L, 100);
                } catch (Exception e2) {
                }
            }
        }
    }
}
