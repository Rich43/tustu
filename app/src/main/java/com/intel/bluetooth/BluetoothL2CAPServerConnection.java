package com.intel.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothL2CAPServerConnection.class */
class BluetoothL2CAPServerConnection extends BluetoothL2CAPConnection implements BluetoothServerConnection {
    protected BluetoothL2CAPServerConnection(BluetoothStack bluetoothStack, long handle, int transmitMTU, int securityOpt) throws IOException {
        super(bluetoothStack, handle);
        boolean initOK = false;
        try {
            this.securityOpt = securityOpt;
            this.transmitMTU = getTransmitMTU();
            if (transmitMTU > 0 && transmitMTU < this.transmitMTU) {
                this.transmitMTU = transmitMTU;
            }
            RemoteDeviceHelper.connected(this);
            initOK = true;
            if (1 == 0) {
                try {
                    bluetoothStack.l2CloseServerConnection(this.handle);
                } catch (IOException e2) {
                    DebugLog.error("close error", e2);
                }
            }
        } catch (Throwable th) {
            if (!initOK) {
                try {
                    bluetoothStack.l2CloseServerConnection(this.handle);
                } catch (IOException e3) {
                    DebugLog.error("close error", e3);
                }
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothL2CAPConnection
    void closeConnectionHandle(long handle) throws IOException {
        RemoteDeviceHelper.disconnected(this);
        this.bluetoothStack.l2CloseServerConnection(handle);
    }
}
