package com.intel.bluetooth;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConnectionNotifierBase.class */
abstract class BluetoothConnectionNotifierBase implements Connection, BluetoothConnectionNotifierServiceRecordAccess {
    private static Hashtable stackConnections = new Hashtable();
    protected BluetoothStack bluetoothStack;
    protected volatile long handle;
    protected ServiceRecordImpl serviceRecord;
    protected boolean closed = false;
    protected int securityOpt;

    protected abstract void stackServerClose(long j2) throws IOException;

    protected abstract void updateStackServiceRecord(ServiceRecordImpl serviceRecordImpl, boolean z2) throws ServiceRegistrationException;

    static void shutdownConnections(BluetoothStack bluetoothStack) {
        Vector connections;
        synchronized (stackConnections) {
            connections = (Vector) stackConnections.get(bluetoothStack);
        }
        if (connections == null) {
            return;
        }
        new Vector();
        Vector c2shutdown = Utils.clone(connections.elements());
        Enumeration en = c2shutdown.elements();
        while (en.hasMoreElements()) {
            BluetoothConnectionNotifierBase c2 = (BluetoothConnectionNotifierBase) en.nextElement2();
            try {
                c2.shutdown();
            } catch (IOException e2) {
                DebugLog.debug("connection shutdown", (Throwable) e2);
            }
        }
    }

    protected BluetoothConnectionNotifierBase(BluetoothStack bluetoothStack, BluetoothConnectionNotifierParams params) throws BluetoothStateException, Error {
        this.bluetoothStack = bluetoothStack;
        if (params.name == null) {
            throw new NullPointerException("Service name is null");
        }
        this.serviceRecord = new ServiceRecordImpl(this.bluetoothStack, null, 0L);
    }

    protected void connectionCreated() {
        Vector connections;
        synchronized (stackConnections) {
            connections = (Vector) stackConnections.get(this.bluetoothStack);
            if (connections == null) {
                connections = new Vector();
                stackConnections.put(this.bluetoothStack, connections);
            }
        }
        connections.addElement(this);
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        if (!this.closed) {
            shutdown();
        }
    }

    public void shutdown() throws IOException {
        Vector connections;
        long synchronizedHandle;
        this.closed = true;
        if (this.handle != 0) {
            DebugLog.debug("closing ConnectionNotifier", this.handle);
            synchronized (stackConnections) {
                connections = (Vector) stackConnections.get(this.bluetoothStack);
            }
            connections.removeElement(this);
            synchronized (this) {
                synchronizedHandle = this.handle;
                this.handle = 0L;
            }
            if (synchronizedHandle != 0) {
                ServiceRecordsRegistry.unregister(this.serviceRecord);
                if (this.serviceRecord.deviceServiceClasses != 0 && (this.bluetoothStack.getFeatureSet() & 4) != 0) {
                    this.bluetoothStack.setLocalDeviceServiceClasses(ServiceRecordsRegistry.getDeviceServiceClasses());
                }
                stackServerClose(synchronizedHandle);
            }
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess
    public ServiceRecord getServiceRecord() {
        if (this.closed) {
            throw new IllegalArgumentException("ConnectionNotifier is closed");
        }
        ServiceRecordsRegistry.register(this, this.serviceRecord);
        return this.serviceRecord;
    }

    protected void validateServiceRecord(ServiceRecord srvRecord) {
        DataElement protocolDescriptor = srvRecord.getAttributeValue(4);
        if (protocolDescriptor == null || protocolDescriptor.getDataType() != 48) {
            throw new IllegalArgumentException("ProtocolDescriptorList is mandatory");
        }
        DataElement serviceClassIDList = srvRecord.getAttributeValue(1);
        if (serviceClassIDList == null || serviceClassIDList.getDataType() != 48 || serviceClassIDList.getSize() == 0) {
            throw new IllegalArgumentException("ServiceClassIDList is mandatory");
        }
        boolean isL2CAPpresent = false;
        Enumeration protocolsSeqEnum = (Enumeration) protocolDescriptor.getValue();
        while (true) {
            if (!protocolsSeqEnum.hasMoreElements()) {
                break;
            }
            DataElement elementSeq = (DataElement) protocolsSeqEnum.nextElement2();
            if (elementSeq.getDataType() == 48) {
                Enumeration elementSeqEnum = (Enumeration) elementSeq.getValue();
                if (elementSeqEnum.hasMoreElements()) {
                    DataElement protocolElement = (DataElement) elementSeqEnum.nextElement2();
                    if (protocolElement.getDataType() == 24 && BluetoothConsts.L2CAP_PROTOCOL_UUID.equals(protocolElement.getValue())) {
                        isL2CAPpresent = true;
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        if (!isL2CAPpresent) {
            throw new IllegalArgumentException("L2CAP UUID is mandatory in ProtocolDescriptorList");
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionNotifierServiceRecordAccess
    public void updateServiceRecord(boolean acceptAndOpen) throws ServiceRegistrationException {
        if (this.serviceRecord.attributeUpdated || !acceptAndOpen) {
            try {
                validateServiceRecord(this.serviceRecord);
                try {
                    updateStackServiceRecord(this.serviceRecord, acceptAndOpen);
                    this.serviceRecord.attributeUpdated = false;
                } catch (Throwable th) {
                    this.serviceRecord.attributeUpdated = false;
                    throw th;
                }
            } catch (IllegalArgumentException e2) {
                if (acceptAndOpen) {
                    throw new ServiceRegistrationException(e2.getMessage());
                }
                throw e2;
            }
        }
        if (this.serviceRecord.deviceServiceClasses != this.serviceRecord.deviceServiceClassesRegistered && (this.bluetoothStack.getFeatureSet() & 4) != 0) {
            this.bluetoothStack.setLocalDeviceServiceClasses(ServiceRecordsRegistry.getDeviceServiceClasses());
            this.serviceRecord.deviceServiceClassesRegistered = this.serviceRecord.deviceServiceClasses;
        }
    }
}
