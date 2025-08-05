package com.intel.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothRFCommServerConnection.class */
class BluetoothRFCommServerConnection extends BluetoothRFCommConnection implements BluetoothServerConnection {
    protected BluetoothRFCommServerConnection(BluetoothStack bluetoothStack, long handle, int securityOpt) throws IOException {
        super(bluetoothStack, handle);
        boolean initOK = false;
        try {
            this.securityOpt = securityOpt;
            RemoteDeviceHelper.connected(this);
            initOK = true;
            if (1 == 0) {
                try {
                    bluetoothStack.connectionRfCloseServerConnection(this.handle);
                } catch (IOException e2) {
                    DebugLog.error("close error", e2);
                }
            }
        } catch (Throwable th) {
            if (!initOK) {
                try {
                    bluetoothStack.connectionRfCloseServerConnection(this.handle);
                } catch (IOException e3) {
                    DebugLog.error("close error", e3);
                }
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothRFCommConnection
    void closeConnectionHandle(long handle) throws IOException {
        RemoteDeviceHelper.disconnected(this);
        this.bluetoothStack.connectionRfCloseServerConnection(handle);
    }
}
