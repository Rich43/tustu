package com.intel.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/ServiceRecordImpl.class */
class ServiceRecordImpl implements ServiceRecord {
    private BluetoothStack bluetoothStack;
    private RemoteDevice device;
    private long handle;
    protected boolean attributeUpdated;
    int deviceServiceClasses;
    int deviceServiceClassesRegistered = 0;
    Hashtable attributes = new Hashtable();

    ServiceRecordImpl(BluetoothStack bluetoothStack, RemoteDevice device, long handle) {
        this.bluetoothStack = bluetoothStack;
        this.device = device;
        this.handle = handle;
    }

    byte[] toByteArray() throws IOException {
        DataElement rootSeq = new DataElement(48);
        int[] sortIDs = new int[this.attributes.size()];
        int k2 = 0;
        Enumeration e2 = this.attributes.keys();
        while (e2.hasMoreElements()) {
            Integer key = (Integer) e2.nextElement2();
            sortIDs[k2] = key.intValue();
            k2++;
        }
        for (int i2 = 0; i2 < sortIDs.length; i2++) {
            for (int j2 = 0; j2 < (sortIDs.length - i2) - 1; j2++) {
                if (sortIDs[j2] > sortIDs[j2 + 1]) {
                    int temp = sortIDs[j2];
                    sortIDs[j2] = sortIDs[j2 + 1];
                    sortIDs[j2 + 1] = temp;
                }
            }
        }
        for (int attrID : sortIDs) {
            rootSeq.addElement(new DataElement(9, attrID));
            rootSeq.addElement(getAttributeValue(attrID));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SDPOutputStream sdpOut = new SDPOutputStream(out);
        sdpOut.writeElement(rootSeq);
        return out.toByteArray();
    }

    void loadByteArray(byte[] data) throws IOException {
        DataElement element = new SDPInputStream(new ByteArrayInputStream(data)).readElement();
        if (element.getDataType() != 48) {
            throw new IOException(new StringBuffer().append("DATSEQ expected instead of ").append(element.getDataType()).toString());
        }
        Enumeration en = (Enumeration) element.getValue();
        while (en.hasMoreElements()) {
            DataElement id = (DataElement) en.nextElement2();
            if (id.getDataType() != 9) {
                throw new IOException(new StringBuffer().append("U_INT_2 expected instead of ").append(id.getDataType()).toString());
            }
            DataElement value = (DataElement) en.nextElement2();
            populateAttributeValue((int) id.getLong(), value);
        }
    }

    @Override // javax.bluetooth.ServiceRecord
    public DataElement getAttributeValue(int attrID) {
        if (attrID < 0 || attrID > 65535) {
            throw new IllegalArgumentException();
        }
        return (DataElement) this.attributes.get(new Integer(attrID));
    }

    @Override // javax.bluetooth.ServiceRecord
    public RemoteDevice getHostDevice() {
        return this.device;
    }

    @Override // javax.bluetooth.ServiceRecord
    public int[] getAttributeIDs() {
        int[] attrIDs = new int[this.attributes.size()];
        int i2 = 0;
        Enumeration e2 = this.attributes.keys();
        while (e2.hasMoreElements()) {
            int i3 = i2;
            i2++;
            attrIDs[i3] = ((Integer) e2.nextElement2()).intValue();
        }
        return attrIDs;
    }

    @Override // javax.bluetooth.ServiceRecord
    public boolean populateRecord(int[] attrIDs) throws IOException {
        if (this.device == null) {
            throw new RuntimeException("This is local device service record");
        }
        if (attrIDs == null) {
            throw new NullPointerException("attrIDs is null");
        }
        if (attrIDs.length == 0) {
            throw new IllegalArgumentException();
        }
        for (int i2 = 0; i2 < attrIDs.length; i2++) {
            if (attrIDs[i2] < 0 || attrIDs[i2] > 65535) {
                throw new IllegalArgumentException();
            }
        }
        int[] sortIDs = new int[attrIDs.length];
        System.arraycopy(attrIDs, 0, sortIDs, 0, attrIDs.length);
        for (int i3 = 0; i3 < sortIDs.length; i3++) {
            for (int j2 = 0; j2 < (sortIDs.length - i3) - 1; j2++) {
                if (sortIDs[j2] > sortIDs[j2 + 1]) {
                    int temp = sortIDs[j2];
                    sortIDs[j2] = sortIDs[j2 + 1];
                    sortIDs[j2 + 1] = temp;
                }
            }
        }
        for (int i4 = 0; i4 < sortIDs.length - 1; i4++) {
            if (sortIDs[i4] == sortIDs[i4 + 1]) {
                throw new IllegalArgumentException();
            }
            DebugLog.debug0x("srvRec query for attr", sortIDs[i4]);
        }
        DebugLog.debug0x("srvRec query for attr", sortIDs[sortIDs.length - 1]);
        return this.bluetoothStack.populateServicesRecordAttributeValues(this, sortIDs);
    }

    @Override // javax.bluetooth.ServiceRecord
    public String getConnectionURL(int requiredSecurity, boolean mustBeMaster) {
        int commChannel = -1;
        DataElement protocolDescriptor = getAttributeValue(4);
        if (protocolDescriptor == null || protocolDescriptor.getDataType() != 48) {
            return null;
        }
        boolean isL2CAP = false;
        boolean isRFCOMM = false;
        boolean isOBEX = false;
        Enumeration protocolsSeqEnum = (Enumeration) protocolDescriptor.getValue();
        while (protocolsSeqEnum.hasMoreElements()) {
            DataElement elementSeq = (DataElement) protocolsSeqEnum.nextElement2();
            if (elementSeq.getDataType() == 48) {
                Enumeration elementSeqEnum = (Enumeration) elementSeq.getValue();
                if (elementSeqEnum.hasMoreElements()) {
                    DataElement protocolElement = (DataElement) elementSeqEnum.nextElement2();
                    if (protocolElement.getDataType() == 24) {
                        Object uuid = protocolElement.getValue();
                        if (BluetoothConsts.OBEX_PROTOCOL_UUID.equals(uuid)) {
                            isOBEX = true;
                            isRFCOMM = false;
                            isL2CAP = false;
                        } else if (elementSeqEnum.hasMoreElements() && BluetoothConsts.RFCOMM_PROTOCOL_UUID.equals(uuid)) {
                            DataElement protocolPSMElement = (DataElement) elementSeqEnum.nextElement2();
                            switch (protocolPSMElement.getDataType()) {
                                case 8:
                                case 9:
                                case 10:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                    long val = protocolPSMElement.getLong();
                                    if (val < 1 || val > 30) {
                                        break;
                                    } else {
                                        commChannel = (int) val;
                                        isRFCOMM = true;
                                        isL2CAP = false;
                                        break;
                                    }
                            }
                        } else if (elementSeqEnum.hasMoreElements() && BluetoothConsts.L2CAP_PROTOCOL_UUID.equals(uuid)) {
                            DataElement protocolPSMElement2 = (DataElement) elementSeqEnum.nextElement2();
                            switch (protocolPSMElement2.getDataType()) {
                                case 8:
                                case 9:
                                case 10:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                    long pcm = protocolPSMElement2.getLong();
                                    if (pcm < 5 || pcm > 65535) {
                                        break;
                                    } else {
                                        commChannel = (int) pcm;
                                        isL2CAP = true;
                                        break;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
        if (commChannel == -1) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        if (isOBEX) {
            buf.append(BluetoothConsts.PROTOCOL_SCHEME_BT_OBEX);
        } else if (isRFCOMM) {
            buf.append(BluetoothConsts.PROTOCOL_SCHEME_RFCOMM);
        } else if (isL2CAP) {
            buf.append(BluetoothConsts.PROTOCOL_SCHEME_L2CAP);
        } else {
            return null;
        }
        buf.append("://");
        if (this.device == null) {
            try {
                Object saveID = BlueCoveImpl.getCurrentThreadBluetoothStackID();
                try {
                    BlueCoveImpl.setThreadBluetoothStack(this.bluetoothStack);
                    buf.append(LocalDevice.getLocalDevice().getBluetoothAddress());
                    if (saveID != null) {
                        BlueCoveImpl.setThreadBluetoothStackID(saveID);
                    }
                } catch (Throwable th) {
                    if (saveID != null) {
                        BlueCoveImpl.setThreadBluetoothStackID(saveID);
                    }
                    throw th;
                }
            } catch (BluetoothStateException bse) {
                DebugLog.error("can't read LocalAddress", bse);
                buf.append("localhost");
            }
        } else {
            buf.append(getHostDevice().getBluetoothAddress());
        }
        buf.append(CallSiteDescriptor.TOKEN_DELIMITER);
        if (isL2CAP) {
            String hex = Integer.toHexString(commChannel);
            for (int i2 = hex.length(); i2 < 4; i2++) {
                buf.append('0');
            }
            buf.append(hex);
        } else {
            buf.append(commChannel);
        }
        switch (requiredSecurity) {
            case 0:
                buf.append(";authenticate=false;encrypt=false");
                break;
            case 1:
                buf.append(";authenticate=true;encrypt=false");
                break;
            case 2:
                buf.append(";authenticate=true;encrypt=true");
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (mustBeMaster) {
            buf.append(";master=true");
        } else {
            buf.append(";master=false");
        }
        return buf.toString();
    }

    int getChannel(UUID protocolUUID) {
        int channel = -1;
        DataElement protocolDescriptor = getAttributeValue(4);
        if (protocolDescriptor == null || protocolDescriptor.getDataType() != 48) {
            return -1;
        }
        Enumeration protocolsSeqEnum = (Enumeration) protocolDescriptor.getValue();
        while (protocolsSeqEnum.hasMoreElements()) {
            DataElement elementSeq = (DataElement) protocolsSeqEnum.nextElement2();
            if (elementSeq.getDataType() == 48) {
                Enumeration elementSeqEnum = (Enumeration) elementSeq.getValue();
                if (elementSeqEnum.hasMoreElements()) {
                    DataElement protocolElement = (DataElement) elementSeqEnum.nextElement2();
                    if (protocolElement.getDataType() == 24) {
                        Object uuid = protocolElement.getValue();
                        if (elementSeqEnum.hasMoreElements() && protocolUUID.equals(uuid)) {
                            DataElement protocolPSMElement = (DataElement) elementSeqEnum.nextElement2();
                            switch (protocolPSMElement.getDataType()) {
                                case 8:
                                case 9:
                                case 10:
                                case 16:
                                case 17:
                                case 18:
                                case 19:
                                    channel = (int) protocolPSMElement.getLong();
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return channel;
    }

    @Override // javax.bluetooth.ServiceRecord
    public void setDeviceServiceClasses(int classes) {
        if (this.device != null) {
            throw new RuntimeException("Service record obtained from a remote device");
        }
        if ((classes & (-16769021)) != 0) {
            throw new IllegalArgumentException();
        }
        if ((classes & 8188) != 0) {
            throw new IllegalArgumentException();
        }
        if ((this.bluetoothStack.getFeatureSet() & 4) == 0) {
            throw new NotSupportedRuntimeException(this.bluetoothStack.getStackID());
        }
        this.deviceServiceClasses = classes;
    }

    @Override // javax.bluetooth.ServiceRecord
    public boolean setAttributeValue(int attrID, DataElement attrValue) {
        if (this.device != null) {
            throw new IllegalArgumentException();
        }
        if (attrID < 0 || attrID > 65535) {
            throw new IllegalArgumentException();
        }
        if (attrID == 0) {
            throw new IllegalArgumentException();
        }
        this.attributeUpdated = true;
        if (attrValue == null) {
            return this.attributes.remove(new Integer(attrID)) != null;
        }
        this.attributes.put(new Integer(attrID), attrValue);
        return true;
    }

    void populateAttributeValue(int attrID, DataElement attrValue) {
        if (attrID < 0 || attrID > 65535) {
            throw new IllegalArgumentException();
        }
        if (attrValue == null) {
            this.attributes.remove(new Integer(attrID));
        } else {
            this.attributes.put(new Integer(attrID), attrValue);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("{\n");
        Enumeration e2 = this.attributes.keys();
        while (e2.hasMoreElements()) {
            Integer i2 = (Integer) e2.nextElement2();
            buf.append("0x");
            buf.append(Integer.toHexString(i2.intValue()));
            buf.append(":\n\t");
            DataElement d2 = (DataElement) this.attributes.get(i2);
            buf.append((Object) d2);
            buf.append("\n");
        }
        buf.append("}");
        return buf.toString();
    }

    long getHandle() {
        return this.handle;
    }

    void setHandle(long handle) {
        this.handle = handle;
    }

    boolean hasServiceClassUUID(UUID uuid) {
        DataElement attrDataElement = getAttributeValue(1);
        if (attrDataElement == null || attrDataElement.getDataType() != 48 || attrDataElement.getSize() == 0) {
            return false;
        }
        Object value = attrDataElement.getValue();
        if (value == null || !(value instanceof Enumeration)) {
            DebugLog.debug("Bogus Value in DATSEQ");
            if (value != null) {
                DebugLog.error(new StringBuffer().append("DATSEQ class ").append(value.getClass().getName()).toString());
                return false;
            }
            return false;
        }
        Enumeration e2 = (Enumeration) value;
        while (e2.hasMoreElements()) {
            Object element = e2.nextElement2();
            if (!(element instanceof DataElement)) {
                DebugLog.debug(new StringBuffer().append("Bogus element in DATSEQ, ").append(value.getClass().getName()).toString());
            } else {
                DataElement dataElement = (DataElement) element;
                if (dataElement.getDataType() == 24 && uuid.equals(dataElement.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean hasProtocolClassUUID(UUID uuid) {
        DataElement protocolDescriptor = getAttributeValue(4);
        if (protocolDescriptor == null || protocolDescriptor.getDataType() != 48) {
            return false;
        }
        Enumeration protocolsSeqEnum = (Enumeration) protocolDescriptor.getValue();
        while (protocolsSeqEnum.hasMoreElements()) {
            DataElement elementSeq = (DataElement) protocolsSeqEnum.nextElement2();
            if (elementSeq.getDataType() == 48) {
                Enumeration elementSeqEnum = (Enumeration) elementSeq.getValue();
                if (elementSeqEnum.hasMoreElements()) {
                    DataElement protocolElement = (DataElement) elementSeqEnum.nextElement2();
                    if (protocolElement.getDataType() == 24 && uuid.equals(protocolElement.getValue())) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    DataElement clone(DataElement de2) {
        DataElement c2 = null;
        switch (de2.getDataType()) {
            case 0:
                c2 = new DataElement(de2.getDataType());
                break;
            case 8:
            case 9:
            case 10:
            case 16:
            case 17:
            case 18:
                c2 = new DataElement(de2.getDataType(), de2.getLong());
                break;
            case 12:
            case 19:
            case 20:
            case 24:
            case 32:
            case 64:
                c2 = new DataElement(de2.getDataType(), de2.getValue());
                break;
            case 40:
                c2 = new DataElement(de2.getBoolean());
                break;
            case 48:
            case 56:
                c2 = new DataElement(de2.getDataType());
                Enumeration en = (Enumeration) de2.getValue();
                while (en.hasMoreElements()) {
                    DataElement dataElement = (DataElement) en.nextElement2();
                    c2.addElement(clone(dataElement));
                }
                break;
        }
        return c2;
    }

    void populateRFCOMMAttributes(long handle, int channel, UUID uuid, String name, boolean obex) {
        populateAttributeValue(0, new DataElement(10, handle));
        DataElement serviceClassIDList = new DataElement(48);
        serviceClassIDList.addElement(new DataElement(24, uuid));
        if (!obex) {
            serviceClassIDList.addElement(new DataElement(24, BluetoothConsts.SERIAL_PORT_UUID));
        }
        populateAttributeValue(1, serviceClassIDList);
        DataElement protocolDescriptorList = new DataElement(48);
        DataElement L2CAPDescriptor = new DataElement(48);
        L2CAPDescriptor.addElement(new DataElement(24, BluetoothConsts.L2CAP_PROTOCOL_UUID));
        protocolDescriptorList.addElement(L2CAPDescriptor);
        DataElement RFCOMMDescriptor = new DataElement(48);
        RFCOMMDescriptor.addElement(new DataElement(24, BluetoothConsts.RFCOMM_PROTOCOL_UUID));
        RFCOMMDescriptor.addElement(new DataElement(8, channel));
        protocolDescriptorList.addElement(RFCOMMDescriptor);
        if (obex) {
            DataElement OBEXDescriptor = new DataElement(48);
            OBEXDescriptor.addElement(new DataElement(24, BluetoothConsts.OBEX_PROTOCOL_UUID));
            protocolDescriptorList.addElement(OBEXDescriptor);
        }
        populateAttributeValue(4, protocolDescriptorList);
        if (name != null) {
            populateAttributeValue(256, new DataElement(32, name));
        }
    }

    void populateL2CAPAttributes(int handle, int channel, UUID uuid, String name) {
        populateAttributeValue(0, new DataElement(10, handle));
        DataElement serviceClassIDList = new DataElement(48);
        serviceClassIDList.addElement(new DataElement(24, uuid));
        populateAttributeValue(1, serviceClassIDList);
        DataElement protocolDescriptorList = new DataElement(48);
        DataElement L2CAPDescriptor = new DataElement(48);
        L2CAPDescriptor.addElement(new DataElement(24, BluetoothConsts.L2CAP_PROTOCOL_UUID));
        L2CAPDescriptor.addElement(new DataElement(9, channel));
        protocolDescriptorList.addElement(L2CAPDescriptor);
        populateAttributeValue(4, protocolDescriptorList);
        if (name != null) {
            populateAttributeValue(256, new DataElement(32, name));
        }
    }
}
