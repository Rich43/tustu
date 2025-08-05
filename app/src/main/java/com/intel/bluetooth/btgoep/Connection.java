package com.intel.bluetooth.btgoep;

import com.ibm.oti.connection.CreateConnection;
import com.intel.bluetooth.BluetoothConnectionAccess;
import com.intel.bluetooth.BluetoothConnectionAccessAdapter;
import com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess;
import com.intel.bluetooth.MicroeditionConnector;
import java.io.IOException;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.obex.Authenticator;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ServerRequestHandler;
import javax.obex.SessionNotifier;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/btgoep/Connection.class */
public class Connection extends BluetoothConnectionAccessAdapter implements CreateConnection, ClientSession, SessionNotifier, BluetoothConnectionNotifierServiceRecordAccess {
    private javax.microedition.io.Connection impl = null;

    public void setParameters(String spec, int access, boolean timeout) throws IOException {
        this.impl = MicroeditionConnector.open(new StringBuffer().append("btgoep:").append(spec).toString(), access, timeout);
    }

    public javax.microedition.io.Connection setParameters2(String spec, int access, boolean timeout) throws IOException {
        setParameters(spec, access, timeout);
        return this;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccessAdapter
    protected BluetoothConnectionAccess getImpl() {
        return (BluetoothConnectionAccess) this.impl;
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        this.impl.close();
    }

    @Override // javax.obex.ClientSession
    public HeaderSet connect(HeaderSet headers) throws IOException {
        return ((ClientSession) this.impl).connect(headers);
    }

    @Override // javax.obex.ClientSession
    public HeaderSet createHeaderSet() {
        return ((ClientSession) this.impl).createHeaderSet();
    }

    @Override // javax.obex.ClientSession
    public HeaderSet delete(HeaderSet headers) throws IOException {
        return ((ClientSession) this.impl).delete(headers);
    }

    @Override // javax.obex.ClientSession
    public HeaderSet disconnect(HeaderSet headers) throws IOException {
        return ((ClientSession) this.impl).disconnect(headers);
    }

    @Override // javax.obex.ClientSession
    public Operation get(HeaderSet headers) throws IOException {
        return ((ClientSession) this.impl).get(headers);
    }

    @Override // javax.obex.ClientSession
    public long getConnectionID() {
        return ((ClientSession) this.impl).getConnectionID();
    }

    @Override // javax.obex.ClientSession
    public Operation put(HeaderSet headers) throws IOException {
        return ((ClientSession) this.impl).put(headers);
    }

    @Override // javax.obex.ClientSession
    public void setAuthenticator(Authenticator auth) {
        ((ClientSession) this.impl).setAuthenticator(auth);
    }

    @Override // javax.obex.ClientSession
    public void setConnectionID(long id) {
        ((ClientSession) this.impl).setConnectionID(id);
    }

    @Override // javax.obex.ClientSession
    public HeaderSet setPath(HeaderSet headers, boolean backup, boolean create) throws IOException {
        return ((ClientSession) this.impl).setPath(headers, backup, create);
    }

    @Override // javax.obex.SessionNotifier
    public javax.microedition.io.Connection acceptAndOpen(ServerRequestHandler handler) throws IOException {
        return ((SessionNotifier) this.impl).acceptAndOpen(handler);
    }

    @Override // javax.obex.SessionNotifier
    public javax.microedition.io.Connection acceptAndOpen(ServerRequestHandler handler, Authenticator auth) throws IOException {
        return ((SessionNotifier) this.impl).acceptAndOpen(handler, auth);
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
