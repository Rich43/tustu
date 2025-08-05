package com.intel.bluetooth.obex;

import com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess;
import com.intel.bluetooth.Utils;
import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.microedition.io.Connection;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.obex.Authenticator;
import javax.obex.ServerRequestHandler;
import javax.obex.SessionNotifier;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXSessionNotifierImpl.class */
public class OBEXSessionNotifierImpl implements SessionNotifier, BluetoothConnectionNotifierServiceRecordAccess {
    private StreamConnectionNotifier notifier;
    private OBEXConnectionParams obexConnectionParams;
    private static final String FQCN;
    private static final Vector fqcnSet;
    static Class class$com$intel$bluetooth$obex$OBEXSessionNotifierImpl;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$com$intel$bluetooth$obex$OBEXSessionNotifierImpl == null) {
            clsClass$ = class$("com.intel.bluetooth.obex.OBEXSessionNotifierImpl");
            class$com$intel$bluetooth$obex$OBEXSessionNotifierImpl = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$obex$OBEXSessionNotifierImpl;
        }
        FQCN = clsClass$.getName();
        fqcnSet = new Vector();
        fqcnSet.addElement(FQCN);
    }

    public OBEXSessionNotifierImpl(StreamConnectionNotifier notifier, OBEXConnectionParams obexConnectionParams) throws Error, IOException {
        Utils.isLegalAPICall(fqcnSet);
        this.notifier = notifier;
        this.obexConnectionParams = obexConnectionParams;
    }

    @Override // javax.obex.SessionNotifier
    public Connection acceptAndOpen(ServerRequestHandler handler) throws IOException {
        return acceptAndOpen(handler, null);
    }

    @Override // javax.obex.SessionNotifier
    public synchronized Connection acceptAndOpen(ServerRequestHandler handler, Authenticator auth) throws IOException {
        if (this.notifier == null) {
            throw new IOException("Session closed");
        }
        if (handler == null) {
            throw new NullPointerException("handler is null");
        }
        OBEXServerSessionImpl sessionImpl = new OBEXServerSessionImpl(this.notifier.acceptAndOpen(), handler, auth, this.obexConnectionParams);
        sessionImpl.startSessionHandlerThread();
        return sessionImpl;
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        StreamConnectionNotifier n2 = this.notifier;
        this.notifier = null;
        if (n2 != null) {
            n2.close();
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess
    public ServiceRecord getServiceRecord() {
        if (this.notifier instanceof ServerSocketConnection) {
            return new OBEXTCPServiceRecordImpl((ServerSocketConnection) this.notifier);
        }
        if (!(this.notifier instanceof BluetoothConnectionNotifierServiceRecordAccess)) {
            throw new IllegalArgumentException("connection is not a Bluetooth notifier");
        }
        return ((BluetoothConnectionNotifierServiceRecordAccess) this.notifier).getServiceRecord();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess
    public void updateServiceRecord(boolean acceptAndOpen) throws ServiceRegistrationException {
        if (!(this.notifier instanceof BluetoothConnectionNotifierServiceRecordAccess)) {
            throw new IllegalArgumentException("connection is not a Bluetooth notifier");
        }
        ((BluetoothConnectionNotifierServiceRecordAccess) this.notifier).updateServiceRecord(acceptAndOpen);
    }
}
