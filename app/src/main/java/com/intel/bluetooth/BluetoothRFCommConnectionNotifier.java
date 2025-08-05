package com.intel.bluetooth;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothRFCommConnectionNotifier.class */
class BluetoothRFCommConnectionNotifier extends BluetoothConnectionNotifierBase implements StreamConnectionNotifier {
    private int rfcommChannel;

    public BluetoothRFCommConnectionNotifier(BluetoothStack bluetoothStack, BluetoothConnectionNotifierParams params) throws IOException {
        super(bluetoothStack, params);
        this.rfcommChannel = -1;
        this.handle = bluetoothStack.rfServerOpen(params, this.serviceRecord);
        this.rfcommChannel = this.serviceRecord.getChannel(BluetoothConsts.RFCOMM_PROTOCOL_UUID);
        this.serviceRecord.attributeUpdated = false;
        this.securityOpt = Utils.securityOpt(params.authenticate, params.encrypt);
        connectionCreated();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierBase
    protected void stackServerClose(long handle) throws IOException {
        this.bluetoothStack.rfServerClose(handle, this.serviceRecord);
    }

    @Override // javax.microedition.io.StreamConnectionNotifier
    public StreamConnection acceptAndOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Notifier is closed");
        }
        updateServiceRecord(true);
        try {
            long clientHandle = this.bluetoothStack.rfServerAcceptAndOpenRfServerConnection(this.handle);
            int clientSecurityOpt = this.bluetoothStack.rfGetSecurityOpt(clientHandle, this.securityOpt);
            return new BluetoothRFCommServerConnection(this.bluetoothStack, clientHandle, clientSecurityOpt);
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
    protected void validateServiceRecord(ServiceRecord srvRecord) {
        if (this.rfcommChannel != this.serviceRecord.getChannel(BluetoothConsts.RFCOMM_PROTOCOL_UUID)) {
            throw new IllegalArgumentException("Must not change the RFCOMM server channel number");
        }
        super.validateServiceRecord(srvRecord);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierBase
    protected void updateStackServiceRecord(ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        this.bluetoothStack.rfServerUpdateServiceRecord(this.handle, serviceRecord, acceptAndOpen);
    }
}
