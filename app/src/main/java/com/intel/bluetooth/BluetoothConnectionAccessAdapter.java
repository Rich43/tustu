package com.intel.bluetooth;

import java.io.IOException;
import javax.bluetooth.RemoteDevice;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConnectionAccessAdapter.class */
public abstract class BluetoothConnectionAccessAdapter implements BluetoothConnectionAccess {
    protected abstract BluetoothConnectionAccess getImpl();

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public long getRemoteAddress() throws IOException {
        return getImpl().getRemoteAddress();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public RemoteDevice getRemoteDevice() {
        return getImpl().getRemoteDevice();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean isClosed() {
        return getImpl().isClosed();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void shutdown() throws IOException {
        getImpl().shutdown();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void markAuthenticated() {
        getImpl().markAuthenticated();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public int getSecurityOpt() {
        return getImpl().getSecurityOpt();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean encrypt(long address, boolean on) throws IOException {
        return getImpl().encrypt(address, on);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void setRemoteDevice(RemoteDevice remoteDevice) {
        getImpl().setRemoteDevice(remoteDevice);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public BluetoothStack getBluetoothStack() {
        return getImpl().getBluetoothStack();
    }
}
