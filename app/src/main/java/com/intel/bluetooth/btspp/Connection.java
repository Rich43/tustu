package com.intel.bluetooth.btspp;

import com.ibm.oti.connection.CreateConnection;
import com.intel.bluetooth.BluetoothConnectionAccess;
import com.intel.bluetooth.BluetoothConnectionAccessAdapter;
import com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess;
import com.intel.bluetooth.MicroeditionConnector;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/btspp/Connection.class */
public class Connection extends BluetoothConnectionAccessAdapter implements CreateConnection, StreamConnection, StreamConnectionNotifier, BluetoothConnectionNotifierServiceRecordAccess {
    javax.microedition.io.Connection impl = null;

    @Override // com.intel.bluetooth.BluetoothConnectionAccessAdapter
    protected BluetoothConnectionAccess getImpl() {
        return (BluetoothConnectionAccess) this.impl;
    }

    public void setParameters(String spec, int access, boolean timeout) throws IOException {
        this.impl = MicroeditionConnector.open(new StringBuffer().append("btspp:").append(spec).toString(), access, timeout);
    }

    public javax.microedition.io.Connection setParameters2(String spec, int access, boolean timeout) throws IOException {
        setParameters(spec, access, timeout);
        return this;
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        this.impl.close();
    }

    @Override // javax.microedition.io.InputConnection
    public DataInputStream openDataInputStream() throws IOException {
        return ((InputConnection) this.impl).openDataInputStream();
    }

    @Override // javax.microedition.io.InputConnection
    public InputStream openInputStream() throws IOException {
        return ((InputConnection) this.impl).openInputStream();
    }

    @Override // javax.microedition.io.OutputConnection
    public DataOutputStream openDataOutputStream() throws IOException {
        return ((OutputConnection) this.impl).openDataOutputStream();
    }

    @Override // javax.microedition.io.OutputConnection
    public OutputStream openOutputStream() throws IOException {
        return ((OutputConnection) this.impl).openOutputStream();
    }

    @Override // javax.microedition.io.StreamConnectionNotifier
    public StreamConnection acceptAndOpen() throws IOException {
        return ((StreamConnectionNotifier) this.impl).acceptAndOpen();
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
