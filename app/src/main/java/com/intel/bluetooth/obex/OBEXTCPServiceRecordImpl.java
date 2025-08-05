package com.intel.bluetooth.obex;

import java.io.IOException;
import javax.bluetooth.DataElement;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXTCPServiceRecordImpl.class */
class OBEXTCPServiceRecordImpl implements ServiceRecord {
    private String host;
    private String port;

    OBEXTCPServiceRecordImpl(ServerSocketConnection notifier) {
        try {
            this.port = String.valueOf(notifier.getLocalPort());
            this.host = notifier.getLocalAddress();
        } catch (IOException e2) {
            this.host = null;
        }
    }

    OBEXTCPServiceRecordImpl(SocketConnection connection) {
        try {
            this.port = String.valueOf(connection.getPort());
            this.host = connection.getAddress();
        } catch (IOException e2) {
            this.host = null;
        }
    }

    @Override // javax.bluetooth.ServiceRecord
    public String getConnectionURL(int requiredSecurity, boolean mustBeMaster) {
        if (this.host == null) {
            return null;
        }
        return new StringBuffer().append("tcpobex://").append(this.host).append(CallSiteDescriptor.TOKEN_DELIMITER).append(this.port).toString();
    }

    @Override // javax.bluetooth.ServiceRecord
    public int[] getAttributeIDs() {
        throw new IllegalArgumentException("Not a Bluetooth ServiceRecord");
    }

    @Override // javax.bluetooth.ServiceRecord
    public DataElement getAttributeValue(int attrID) {
        throw new IllegalArgumentException("Not a Bluetooth ServiceRecord");
    }

    @Override // javax.bluetooth.ServiceRecord
    public RemoteDevice getHostDevice() {
        throw new IllegalArgumentException("Not a Bluetooth ServiceRecord");
    }

    @Override // javax.bluetooth.ServiceRecord
    public boolean populateRecord(int[] attrIDs) throws IOException {
        throw new IllegalArgumentException("Not a Bluetooth ServiceRecord");
    }

    @Override // javax.bluetooth.ServiceRecord
    public boolean setAttributeValue(int attrID, DataElement attrValue) {
        throw new IllegalArgumentException("Not a Bluetooth ServiceRecord");
    }

    @Override // javax.bluetooth.ServiceRecord
    public void setDeviceServiceClasses(int classes) {
        throw new IllegalArgumentException("Not a Bluetooth ServiceRecord");
    }
}
