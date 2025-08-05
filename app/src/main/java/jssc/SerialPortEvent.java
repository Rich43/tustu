package jssc;

/* JADX WARN: Classes with same name are omitted:
  jssc.jar:jssc/SerialPortEvent.class
 */
/* loaded from: jssc2.8.jar:jssc/SerialPortEvent.class */
public class SerialPortEvent {
    private String portName;
    private int eventType;
    private int eventValue;
    public static final int RXCHAR = 1;
    public static final int RXFLAG = 2;
    public static final int TXEMPTY = 4;
    public static final int CTS = 8;
    public static final int DSR = 16;
    public static final int RLSD = 32;
    public static final int BREAK = 64;
    public static final int ERR = 128;
    public static final int RING = 256;

    public SerialPortEvent(String portName, int eventType, int eventValue) {
        this.portName = portName;
        this.eventType = eventType;
        this.eventValue = eventValue;
    }

    public String getPortName() {
        return this.portName;
    }

    public int getEventType() {
        return this.eventType;
    }

    public int getEventValue() {
        return this.eventValue;
    }

    public boolean isRXCHAR() {
        if (this.eventType == 1) {
            return true;
        }
        return false;
    }

    public boolean isRXFLAG() {
        if (this.eventType == 2) {
            return true;
        }
        return false;
    }

    public boolean isTXEMPTY() {
        if (this.eventType == 4) {
            return true;
        }
        return false;
    }

    public boolean isCTS() {
        if (this.eventType == 8) {
            return true;
        }
        return false;
    }

    public boolean isDSR() {
        if (this.eventType == 16) {
            return true;
        }
        return false;
    }

    public boolean isRLSD() {
        if (this.eventType == 32) {
            return true;
        }
        return false;
    }

    public boolean isBREAK() {
        if (this.eventType == 64) {
            return true;
        }
        return false;
    }

    public boolean isERR() {
        if (this.eventType == 128) {
            return true;
        }
        return false;
    }

    public boolean isRING() {
        if (this.eventType == 256) {
            return true;
        }
        return false;
    }
}
