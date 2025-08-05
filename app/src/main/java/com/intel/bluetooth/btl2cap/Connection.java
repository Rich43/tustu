package com.intel.bluetooth.btl2cap;

import com.ibm.oti.connection.CreateConnection;
import com.intel.bluetooth.BluetoothConnectionAccess;
import com.intel.bluetooth.BluetoothConnectionAccessAdapter;
import com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess;
import com.intel.bluetooth.MicroeditionConnector;
import java.io.IOException;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.L2CAPConnectionNotifier;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/btl2cap/Connection.class */
public class Connection extends BluetoothConnectionAccessAdapter implements CreateConnection, L2CAPConnection, L2CAPConnectionNotifier, BluetoothConnectionNotifierServiceRecordAccess {
    javax.microedition.io.Connection impl = null;

    @Override // com.intel.bluetooth.BluetoothConnectionAccessAdapter
    protected BluetoothConnectionAccess getImpl() {
        return (BluetoothConnectionAccess) this.impl;
    }

    public void setParameters(String spec, int access, boolean timeout) throws IOException {
        this.impl = MicroeditionConnector.open(new StringBuffer().append("btl2cap:").append(spec).toString(), access, timeout);
    }

    public javax.microedition.io.Connection setParameters2(String spec, int access, boolean timeout) throws IOException {
        setParameters(spec, access, timeout);
        return this;
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        this.impl.close();
    }

    @Override // javax.bluetooth.L2CAPConnection
    public int getReceiveMTU() throws IOException {
        return ((L2CAPConnection) this.impl).getReceiveMTU();
    }

    @Override // javax.bluetooth.L2CAPConnection
    public int getTransmitMTU() throws IOException {
        return ((L2CAPConnection) this.impl).getTransmitMTU();
    }

    @Override // javax.bluetooth.L2CAPConnection
    public boolean ready() throws IOException {
        return ((L2CAPConnection) this.impl).ready();
    }

    @Override // javax.bluetooth.L2CAPConnection
    public int receive(byte[] inBuf) throws IOException {
        return ((L2CAPConnection) this.impl).receive(inBuf);
    }

    @Override // javax.bluetooth.L2CAPConnection
    public void send(byte[] data) throws IOException {
        ((L2CAPConnection) this.impl).send(data);
    }

    @Override // javax.bluetooth.L2CAPConnectionNotifier
    public L2CAPConnection acceptAndOpen() throws IOException {
        return ((L2CAPConnectionNotifier) this.impl).acceptAndOpen();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess
    public ServiceRecord getServiceRecord() {
        return ((BluetoothConnectionNotifierServiceRecordAccess) this.impl).getServiceRecord();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess
    public void updateServiceRecord(boolean acceptAndOpen) throws ServiceRegistrationException {
        ((BluetoothConnectionNotifierServiceRecordAccess) this.impl).updateServiceRecord(acceptAndOpen);
    }
}
