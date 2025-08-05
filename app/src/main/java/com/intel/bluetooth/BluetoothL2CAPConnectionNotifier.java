package com.intel.bluetooth;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothL2CAPConnectionNotifier.class */
class BluetoothL2CAPConnectionNotifier extends BluetoothConnectionNotifierBase implements L2CAPConnectionNotifier {
    private int transmitMTU;
    private int psm;

    public BluetoothL2CAPConnectionNotifier(BluetoothStack bluetoothStack, BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU) throws IOException {
        super(bluetoothStack, params);
        this.psm = -1;
        this.handle = bluetoothStack.l2ServerOpen(params, receiveMTU, transmitMTU, this.serviceRecord);
        this.psm = this.serviceRecord.getChannel(BluetoothConsts.L2CAP_PROTOCOL_UUID);
        this.transmitMTU = transmitMTU;
        this.serviceRecord.attributeUpdated = false;
        this.securityOpt = Utils.securityOpt(params.authenticate, params.encrypt);
        connectionCreated();
    }

    @Override // javax.bluetooth.L2CAPConnectionNotifier
    public L2CAPConnection acceptAndOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Notifier is closed");
        }
        updateServiceRecord(true);
        try {
            long clientHandle = this.bluetoothStack.l2ServerAcceptAndOpenServerConnection(this.handle);
            int clientSecurityOpt = this.bluetoothStack.l2GetSecurityOpt(clientHandle, this.securityOpt);
            return new BluetoothL2CAPServerConnection(this.bluetoothStack, clientHandle, this.transmitMTU, clientSecurityOpt);
        } catch (InterruptedIOException e2) {
            throw e2;
        } catch (IOException e3) {
            if (this.closed) {
                throw new InterruptedIOException(new StringBuffer().append("Notifier has been closed; ").append(e3.getMessage()).toString());
            }
            throw e3;
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierBase
    protected void stackServerClose(long handle) throws IOException {
        this.bluetoothStack.l2ServerClose(handle, this.serviceRecord);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierBase
    protected void validateServiceRecord(ServiceRecord srvRecord) {
        if (this.psm != this.serviceRecord.getChannel(BluetoothConsts.L2CAP_PROTOCOL_UUID)) {
            throw new IllegalArgumentException("Must not change the PSM");
        }
        super.validateServiceRecord(srvRecord);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierBase
    protected void updateStackServiceRecord(ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        this.bluetoothStack.l2ServerUpdateServiceRecord(this.handle, serviceRecord, acceptAndOpen);
    }
}
