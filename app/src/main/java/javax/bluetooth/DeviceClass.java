package javax.bluetooth;

import com.intel.bluetooth.BluetoothConsts;
import com.intel.bluetooth.DebugLog;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/DeviceClass.class */
public class DeviceClass {
    private static final int SERVICE_MASK = 16769024;
    private static final int MAJOR_MASK = 7936;
    private static final int MINOR_MASK = 252;
    private int record;

    public DeviceClass(int record) {
        DebugLog.debug("new DeviceClass", record);
        this.record = record;
        if ((record & (-16777216)) != 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getServiceClasses() {
        return this.record & 16769024;
    }

    public int getMajorDeviceClass() {
        return this.record & 7936;
    }

    public int getMinorDeviceClass() {
        return this.record & 252;
    }

    public String toString() {
        return BluetoothConsts.toString(this);
    }
}
