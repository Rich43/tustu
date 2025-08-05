package javax.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/BluetoothConnectionException.class */
public class BluetoothConnectionException extends IOException {
    private static final long serialVersionUID = 1;
    public static final int UNKNOWN_PSM = 1;
    public static final int SECURITY_BLOCK = 2;
    public static final int NO_RESOURCES = 3;
    public static final int FAILED_NOINFO = 4;
    public static final int TIMEOUT = 5;
    public static final int UNACCEPTABLE_PARAMS = 6;
    private int errorCode;

    public BluetoothConnectionException(int error) {
        if (error < 1 || error > 6) {
            throw new IllegalArgumentException();
        }
        this.errorCode = error;
    }

    public BluetoothConnectionException(int error, String msg) {
        super(msg);
        if (error < 1 || error > 6) {
            throw new IllegalArgumentException();
        }
        this.errorCode = error;
    }

    public int getStatus() {
        return this.errorCode;
    }
}
