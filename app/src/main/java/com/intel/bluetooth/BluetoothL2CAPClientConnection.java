package com.intel.bluetooth;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothL2CAPClientConnection.class */
class BluetoothL2CAPClientConnection extends BluetoothL2CAPConnection {
    public BluetoothL2CAPClientConnection(BluetoothStack bluetoothStack, BluetoothConnectionParams params, int receiveMTU, int transmitMTU) throws IOException {
        super(bluetoothStack, bluetoothStack.l2OpenClientConnection(params, receiveMTU, transmitMTU));
        boolean initOK = false;
        try {
            this.securityOpt = bluetoothStack.l2GetSecurityOpt(this.handle, Utils.securityOpt(params.authenticate, params.encrypt));
            this.transmitMTU = getTransmitMTU();
            if (transmitMTU > 0 && transmitMTU < this.transmitMTU) {
                this.transmitMTU = transmitMTU;
            }
            RemoteDeviceHelper.connected(this);
            initOK = true;
            if (1 == 0) {
                try {
                    bluetoothStack.l2CloseClientConnection(this.handle);
                } catch (IOException e2) {
                    DebugLog.error("close error", e2);
                }
            }
        } catch (Throwable th) {
            if (!initOK) {
                try {
                    bluetoothStack.l2CloseClientConnection(this.handle);
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
        this.bluetoothStack.l2CloseClientConnection(handle);
    }
}
