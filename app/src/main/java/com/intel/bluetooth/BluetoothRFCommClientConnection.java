package com.intel.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothRFCommClientConnection.class */
class BluetoothRFCommClientConnection extends BluetoothRFCommConnection {
    public BluetoothRFCommClientConnection(BluetoothStack bluetoothStack, BluetoothConnectionParams params) throws IOException {
        super(bluetoothStack, bluetoothStack.connectionRfOpenClientConnection(params));
        boolean initOK = false;
        try {
            this.securityOpt = bluetoothStack.rfGetSecurityOpt(this.handle, Utils.securityOpt(params.authenticate, params.encrypt));
            RemoteDeviceHelper.connected(this);
            initOK = true;
            if (1 == 0) {
                try {
                    bluetoothStack.connectionRfCloseClientConnection(this.handle);
                } catch (IOException e2) {
                    DebugLog.error("close error", e2);
                }
            }
        } catch (Throwable th) {
            if (!initOK) {
                try {
                    bluetoothStack.connectionRfCloseClientConnection(this.handle);
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
        this.bluetoothStack.connectionRfCloseClientConnection(handle);
    }
}
