package javax.bluetooth;

import com.intel.bluetooth.DebugLog;
import com.intel.bluetooth.RemoteDeviceHelper;
import com.intel.bluetooth.UtilsJavaSE;
import java.io.IOException;
import javax.microedition.io.Connection;
import sun.util.locale.LanguageTag;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/RemoteDevice.class */
public class RemoteDevice {
    private String addressStr;
    private long addressLong;

    protected RemoteDevice(String address) {
        if (address == null) {
            throw new NullPointerException("address is null");
        }
        if (address.length() != 12) {
            throw new IllegalArgumentException(new StringBuffer().append("Malformed address: ").append(address).append("; should be 12 characters").toString());
        }
        if (address.startsWith(LanguageTag.SEP)) {
            throw new IllegalArgumentException(new StringBuffer().append("Malformed address: ").append(address).append("; can't be negative").toString());
        }
        DebugLog.debug("new RemoteDevice", address);
        this.addressStr = RemoteDeviceHelper.formatBluetoothAddress(address);
        try {
            if (this.addressStr.equals(LocalDevice.getLocalDevice().getBluetoothAddress())) {
                throw new IllegalArgumentException("can't use the LocalDevice address.");
            }
            this.addressLong = RemoteDeviceHelper.getAddress(address);
        } catch (BluetoothStateException e2) {
            throw ((RuntimeException) UtilsJavaSE.initCause(new RuntimeException("Can't initialize bluetooth support"), e2));
        }
    }

    public boolean isTrustedDevice() {
        return RemoteDeviceHelper.implIsTrustedDevice(this);
    }

    public String getFriendlyName(boolean alwaysAsk) throws IOException {
        return RemoteDeviceHelper.implGetFriendlyName(this, this.addressLong, alwaysAsk);
    }

    public final String getBluetoothAddress() {
        return this.addressStr;
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof RemoteDevice) && ((RemoteDevice) obj).addressLong == this.addressLong;
    }

    public int hashCode() {
        return new Long(this.addressLong).hashCode();
    }

    public static RemoteDevice getRemoteDevice(Connection conn) throws IOException {
        return RemoteDeviceHelper.implGetRemoteDevice(conn);
    }

    public boolean authenticate() throws IOException {
        return RemoteDeviceHelper.authenticate(this);
    }

    public boolean authorize(Connection conn) throws IOException {
        return RemoteDeviceHelper.implAuthorize(this, conn);
    }

    public boolean encrypt(Connection conn, boolean on) throws IOException {
        return RemoteDeviceHelper.implEncrypt(this, conn, on);
    }

    public boolean isAuthenticated() {
        return RemoteDeviceHelper.implIsAuthenticated(this);
    }

    public boolean isAuthorized(Connection conn) throws IOException {
        return RemoteDeviceHelper.implIsAuthorized(this, conn);
    }

    public boolean isEncrypted() {
        return RemoteDeviceHelper.implIsEncrypted(this);
    }
}
