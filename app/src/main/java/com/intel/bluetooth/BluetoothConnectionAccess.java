package com.intel.bluetooth;

import java.io.IOException;
import javax.bluetooth.RemoteDevice;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConnectionAccess.class */
public interface BluetoothConnectionAccess {
    BluetoothStack getBluetoothStack();

    long getRemoteAddress() throws IOException;

    boolean isClosed();

    void markAuthenticated();

    int getSecurityOpt();

    void shutdown() throws IOException;

    boolean encrypt(long j2, boolean z2) throws IOException;

    RemoteDevice getRemoteDevice();

    void setRemoteDevice(RemoteDevice remoteDevice);
}
