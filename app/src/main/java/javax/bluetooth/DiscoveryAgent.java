package javax.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import com.intel.bluetooth.DebugLog;
import com.intel.bluetooth.RemoteDeviceHelper;
import com.intel.bluetooth.SelectServiceHandler;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/DiscoveryAgent.class */
public class DiscoveryAgent {
    public static final int NOT_DISCOVERABLE = 0;
    public static final int GIAC = 10390323;
    public static final int LIAC = 10390272;
    public static final int CACHED = 0;
    public static final int PREKNOWN = 1;
    private BluetoothStack bluetoothStack;

    private DiscoveryAgent() {
    }

    DiscoveryAgent(BluetoothStack bluetoothStack) {
        this();
        this.bluetoothStack = bluetoothStack;
    }

    public RemoteDevice[] retrieveDevices(int option) {
        return RemoteDeviceHelper.implRetrieveDevices(this.bluetoothStack, option);
    }

    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        if (listener == null) {
            throw new NullPointerException("DiscoveryListener is null");
        }
        if (accessCode != 10390272 && accessCode != 10390323 && (accessCode < 10390272 || accessCode > 10390335)) {
            throw new IllegalArgumentException(new StringBuffer().append("Invalid accessCode ").append(accessCode).toString());
        }
        return this.bluetoothStack.startInquiry(accessCode, listener);
    }

    public boolean cancelInquiry(DiscoveryListener listener) {
        if (listener == null) {
            throw new NullPointerException("DiscoveryListener is null");
        }
        DebugLog.debug("cancelInquiry");
        return this.bluetoothStack.cancelInquiry(listener);
    }

    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice btDev, DiscoveryListener discListener) throws BluetoothStateException {
        if (uuidSet == null) {
            throw new NullPointerException("uuidSet is null");
        }
        if (uuidSet.length == 0) {
            throw new IllegalArgumentException("uuidSet is empty");
        }
        for (int u1 = 0; u1 < uuidSet.length; u1++) {
            if (uuidSet[u1] == null) {
                throw new NullPointerException(new StringBuffer().append("uuidSet[").append(u1).append("] is null").toString());
            }
            for (int u2 = u1 + 1; u2 < uuidSet.length; u2++) {
                if (uuidSet[u1].equals(uuidSet[u2])) {
                    throw new IllegalArgumentException(new StringBuffer().append("uuidSet has duplicate values ").append(uuidSet[u1].toString()).toString());
                }
            }
        }
        if (btDev == null) {
            throw new NullPointerException("RemoteDevice is null");
        }
        if (discListener == null) {
            throw new NullPointerException("DiscoveryListener is null");
        }
        for (int i2 = 0; attrSet != null && i2 < attrSet.length; i2++) {
            if (attrSet[i2] < 0 || attrSet[i2] > 65535) {
                throw new IllegalArgumentException(new StringBuffer().append("attrSet[").append(i2).append("] not in range").toString());
            }
        }
        return this.bluetoothStack.searchServices(attrSet, uuidSet, btDev, discListener);
    }

    public boolean cancelServiceSearch(int transID) {
        DebugLog.debug("cancelServiceSearch", transID);
        return this.bluetoothStack.cancelServiceSearch(transID);
    }

    public String selectService(UUID uuid, int security, boolean master) throws BluetoothStateException {
        return new SelectServiceHandler(this).selectService(uuid, security, master);
    }
}
