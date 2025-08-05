package com.intel.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackWIDCOMM.class */
class BluetoothStackWIDCOMM implements BluetoothStack, BluetoothStackExtension {
    private static BluetoothStackWIDCOMM singleInstance = null;
    private boolean initialized = false;
    private Vector deviceDiscoveryListeners = new Vector();
    private Hashtable deviceDiscoveryListenerFoundDevices = new Hashtable();
    private Hashtable deviceDiscoveryListenerReportedDevices = new Hashtable();
    private static final int ATTR_RETRIEVABLE_MAX = 256;
    private static final int RECEIVE_MTU_MAX = 1024;
    static final short NULL_DESC_TYPE = 0;
    static final short UINT_DESC_TYPE = 1;
    static final short TWO_COMP_INT_DESC_TYPE = 2;
    static final short UUID_DESC_TYPE = 3;
    static final short TEXT_STR_DESC_TYPE = 4;
    static final short BOOLEAN_DESC_TYPE = 5;
    static final short DATA_ELE_SEQ_DESC_TYPE = 6;
    static final short DATA_ELE_ALT_DESC_TYPE = 7;
    static final short URL_DESC_TYPE = 8;
    static Class class$com$intel$bluetooth$BluetoothStackWIDCOMM;

    private native int nativeBuildFeatures();

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isNativeCodeLoaded();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int getLibraryVersion();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int detectBluetoothStack();

    @Override // com.intel.bluetooth.BluetoothStack
    public native void enableNativeDebug(Class cls, boolean z2);

    public native boolean initializeImpl();

    private native void uninitialize();

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getLocalDeviceBluetoothAddress() throws BluetoothStateException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getLocalDeviceName();

    private native int getDeviceClassImpl();

    private native boolean isStackServerUp();

    public native boolean isLocalDeviceDiscoverable();

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isLocalDevicePowerOn();

    private native String getBTWVersionInfo();

    private native int getDeviceVersion();

    private native int getDeviceManufacturer();

    private native boolean authenticateRemoteDeviceImpl(long j2, String str) throws IOException;

    private native void removeAuthenticationWithRemoteDeviceImpl(long j2) throws IOException;

    private native boolean isRemoteDeviceConnected(long j2);

    private native String getRemoteDeviceLinkMode(long j2);

    private native String getRemoteDeviceVersionInfo(long j2);

    private native boolean setSniffMode(long j2);

    private native boolean cancelSniffMode(long j2);

    private native int getRemoteDeviceRSSI(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native int runDeviceInquiryImpl(DeviceInquiryRunnable deviceInquiryRunnable, DeviceInquiryThread deviceInquiryThread, int i2, DiscoveryListener discoveryListener) throws BluetoothStateException;

    private native boolean deviceInquiryCancelImpl();

    native String getRemoteDeviceFriendlyName(long j2, int i2, int i3) throws IOException;

    native String peekRemoteDeviceFriendlyName(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native long[] runSearchServicesImpl(SearchServicesThread searchServicesThread, byte[] bArr, long j2) throws BluetoothStateException, SearchServicesTerminatedException;

    private native void cancelServiceSearchImpl();

    private native byte[] getServiceAttribute(int i2, long j2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean isServiceRecordDiscoverable(long j2, long j3) throws IOException;

    private native long connectionRfOpenClientConnectionImpl(long j2, int i2, boolean z2, boolean z3, int i3) throws IOException;

    private native void closeRfCommPortImpl(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long getConnectionRfRemoteAddress(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfRead(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfRead(long j2, byte[] bArr, int i2, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfReadAvailable(long j2) throws IOException;

    private native void connectionRfWriteImpl(long j2, byte[] bArr, int i2, int i3) throws IOException;

    private native synchronized long rfServerOpenImpl(byte[] bArr, byte[] bArr2, boolean z2, String str, boolean z3, boolean z4) throws IOException;

    private native int rfServerSCN(long j2) throws IOException;

    private native void sdpServiceAddAttribute(long j2, char c2, int i2, short s2, byte[] bArr) throws ServiceRegistrationException;

    private native void sdpServiceAddServiceClassIdList(long j2, char c2, byte[][] bArr) throws ServiceRegistrationException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long rfServerAcceptAndOpenRfServerConnection(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfCloseServerConnection(long j2) throws IOException;

    private native void rfServerCloseImpl(long j2) throws IOException;

    private native long l2OpenClientConnectionImpl(long j2, int i2, boolean z2, boolean z3, int i3, int i4, int i5) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void l2CloseClientConnection(long j2) throws IOException;

    private native synchronized long l2ServerOpenImpl(byte[] bArr, boolean z2, boolean z3, String str, int i2, int i3, int i4) throws IOException;

    public native int l2ServerPSM(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long l2ServerAcceptAndOpenServerConnection(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void l2CloseServerConnection(long j2) throws IOException;

    private native void l2ServerCloseImpl(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2GetReceiveMTU(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2GetTransmitMTU(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean l2Ready(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2Receive(long j2, byte[] bArr) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void l2Send(long j2, byte[] bArr, int i2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long l2RemoteAddress(long j2) throws IOException;

    BluetoothStackWIDCOMM() {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getStackID() {
        return BlueCoveImpl.STACK_WIDCOMM;
    }

    public String toString() {
        return getStackID();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getFeatureSet() {
        int nativeBuildFeaturs = nativeBuildFeatures();
        return 3 | (nativeBuildFeaturs > 0 ? 8 : 0);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public BluetoothStack.LibraryInformation[] requireNativeLibraries() {
        return BluetoothStack.LibraryInformation.library("bluecove");
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void initialize() throws BluetoothStateException {
        if (singleInstance != null) {
            throw new BluetoothStateException(new StringBuffer().append("Only one instance of ").append(getStackID()).append(" stack supported").toString());
        }
        if (!initializeImpl()) {
            throw new RuntimeException("WIDCOMM BluetoothStack not found");
        }
        this.initialized = true;
        singleInstance = this;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void destroy() {
        if (singleInstance != this) {
            throw new RuntimeException("Destroy invalid instance");
        }
        if (this.initialized) {
            uninitialize();
            this.initialized = false;
            DebugLog.debug("WIDCOMM destroyed");
        }
        singleInstance = null;
    }

    protected void finalize() {
        destroy();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public DeviceClass getLocalDeviceClass() {
        return new DeviceClass(getDeviceClassImpl());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void setLocalDeviceServiceClasses(int classOfDevice) {
        throw new NotSupportedRuntimeException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
        int curentMode = getLocalDeviceDiscoverable();
        if (curentMode == mode) {
            return true;
        }
        return false;
    }

    private synchronized void verifyDeviceReady() throws BluetoothStateException {
        if (!isLocalDevicePowerOn()) {
            throw new BluetoothStateException("Bluetooth Device is not ready");
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getLocalDeviceDiscoverable() {
        if (isStackServerUp() && isLocalDeviceDiscoverable()) {
            return 10390323;
        }
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceProperty(String property) {
        if (BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_DEVICES_MAX.equals(property)) {
            return "7";
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_SD_TRANS_MAX.equals(property)) {
            return "1";
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY_SCAN.equals(property) || BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE_SCAN.equals(property) || BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY.equals(property) || BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE.equals(property)) {
            return "true";
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_SD_ATTR_RETRIEVABLE_MAX.equals(property)) {
            return String.valueOf(256);
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_MASTER_SWITCH.equals(property)) {
            return "false";
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_L2CAP_RECEIVEMTU_MAX.equals(property)) {
            return String.valueOf(1024);
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_VERSION.equals(property)) {
            return String.valueOf(getDeviceVersion());
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_MANUFACTURER.equals(property)) {
            return String.valueOf(getDeviceManufacturer());
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_STACK_VERSION.equals(property)) {
            return getBTWVersionInfo();
        }
        if (property.startsWith("bluecove.nativeFunction:")) {
            String functionDescr = property.substring(property.indexOf(58) + 1, property.length());
            int paramIdx = functionDescr.indexOf(58);
            if (paramIdx == -1) {
                throw new RuntimeException(new StringBuffer().append("Invalid native function ").append(functionDescr).append("; arguments expected").toString());
            }
            String function = functionDescr.substring(0, paramIdx);
            long address = RemoteDeviceHelper.getAddress(functionDescr.substring(function.length() + 1, functionDescr.length()));
            if ("getRemoteDeviceVersionInfo".equals(function)) {
                return getRemoteDeviceVersionInfo(address);
            }
            if ("cancelSniffMode".equals(function)) {
                return String.valueOf(cancelSniffMode(address));
            }
            if ("setSniffMode".equals(function)) {
                return String.valueOf(setSniffMode(address));
            }
            if ("getRemoteDeviceRSSI".equals(function)) {
                return String.valueOf(getRemoteDeviceRSSI(address));
            }
            if ("getRemoteDeviceLinkMode".equals(function)) {
                if (isRemoteDeviceConnected(address)) {
                    return getRemoteDeviceLinkMode(address);
                }
                return "disconnected";
            }
            return null;
        }
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isCurrentThreadInterruptedCallback() {
        return UtilsJavaSE.isCurrentThreadInterrupted();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public RemoteDevice[] retrieveDevices(int option) {
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceTrusted(long address) {
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceAuthenticated(long address) {
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStackExtension
    public int readRemoteDeviceRSSI(long address) throws IOException {
        return getRemoteDeviceRSSI(address);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address, String passkey) throws IOException {
        return authenticateRemoteDeviceImpl(address, passkey);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void removeAuthenticationWithRemoteDevice(long address) throws IOException {
        removeAuthenticationWithRemoteDeviceImpl(address);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        this.deviceDiscoveryListeners.addElement(listener);
        if (BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_INQUIRY_REPORT_ASAP, false)) {
            this.deviceDiscoveryListenerFoundDevices.put(listener, new Hashtable());
        }
        this.deviceDiscoveryListenerReportedDevices.put(listener, new Vector());
        DeviceInquiryRunnable inquiryRunnable = new DeviceInquiryRunnable(this) { // from class: com.intel.bluetooth.BluetoothStackWIDCOMM.1
            private final BluetoothStackWIDCOMM this$0;

            {
                this.this$0 = this;
            }

            @Override // com.intel.bluetooth.DeviceInquiryRunnable
            public int runDeviceInquiry(DeviceInquiryThread startedNotify, int accessCode2, DiscoveryListener listener2) throws BluetoothStateException {
                Hashtable previouslyFound;
                try {
                    int discType = this.this$0.runDeviceInquiryImpl(this, startedNotify, accessCode2, listener2);
                    if (discType == 0 && (previouslyFound = (Hashtable) this.this$0.deviceDiscoveryListenerFoundDevices.get(listener2)) != null) {
                        Vector reported = (Vector) this.this$0.deviceDiscoveryListenerReportedDevices.get(listener2);
                        Enumeration en = previouslyFound.keys();
                        while (en.hasMoreElements()) {
                            RemoteDevice remoteDevice = (RemoteDevice) en.nextElement2();
                            if (!reported.contains(remoteDevice)) {
                                reported.addElement(remoteDevice);
                                Integer deviceClassInt = (Integer) previouslyFound.get(remoteDevice);
                                DeviceClass deviceClass = new DeviceClass(deviceClassInt.intValue());
                                listener2.deviceDiscovered(remoteDevice, deviceClass);
                                if (!this.this$0.deviceDiscoveryListeners.contains(listener2)) {
                                    return 5;
                                }
                            }
                        }
                    }
                    this.this$0.deviceDiscoveryListeners.removeElement(listener2);
                    this.this$0.deviceDiscoveryListenerFoundDevices.remove(listener2);
                    this.this$0.deviceDiscoveryListenerReportedDevices.remove(listener2);
                    return discType;
                } finally {
                    this.this$0.deviceDiscoveryListeners.removeElement(listener2);
                    this.this$0.deviceDiscoveryListenerFoundDevices.remove(listener2);
                    this.this$0.deviceDiscoveryListenerReportedDevices.remove(listener2);
                }
            }

            @Override // com.intel.bluetooth.DeviceInquiryRunnable
            public void deviceDiscoveredCallback(DiscoveryListener listener2, long deviceAddr, int deviceClass, String deviceName, boolean paired) {
                DebugLog.debug("deviceDiscoveredCallback deviceName", deviceName);
                if (!this.this$0.deviceDiscoveryListeners.contains(listener2)) {
                    return;
                }
                RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this.this$0, deviceAddr, deviceName, paired);
                Vector reported = (Vector) this.this$0.deviceDiscoveryListenerReportedDevices.get(listener2);
                if (reported != null && !reported.contains(remoteDevice)) {
                    Hashtable previouslyFound = (Hashtable) this.this$0.deviceDiscoveryListenerFoundDevices.get(listener2);
                    if (previouslyFound != null) {
                        Integer deviceClassInt = (Integer) previouslyFound.get(remoteDevice);
                        if (deviceClassInt == null) {
                            previouslyFound.put(remoteDevice, new Integer(deviceClass));
                            return;
                        } else {
                            if (deviceClass != 0) {
                                previouslyFound.put(remoteDevice, new Integer(deviceClass));
                                return;
                            }
                            return;
                        }
                    }
                    DeviceClass cod = new DeviceClass(deviceClass);
                    reported.addElement(remoteDevice);
                    DebugLog.debug("deviceDiscoveredCallback address", remoteDevice.getBluetoothAddress());
                    DebugLog.debug("deviceDiscoveredCallback deviceClass", cod);
                    listener2.deviceDiscovered(remoteDevice, cod);
                }
            }
        };
        return DeviceInquiryThread.startInquiry(this, inquiryRunnable, accessCode, listener);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelInquiry(DiscoveryListener listener) {
        if (!this.deviceDiscoveryListeners.removeElement(listener)) {
            return false;
        }
        return deviceInquiryCancelImpl();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getRemoteDeviceFriendlyName(long address) throws IOException {
        if (this.deviceDiscoveryListeners.size() != 0) {
            return peekRemoteDeviceFriendlyName(address);
        }
        DiscoveryListener listener = new DiscoveryListenerAdapter();
        if (startInquiry(10390323, listener)) {
            String name = peekRemoteDeviceFriendlyName(address);
            cancelInquiry(listener);
            return name;
        }
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        SearchServicesRunnable searchRunnable = new SearchServicesRunnable(this) { // from class: com.intel.bluetooth.BluetoothStackWIDCOMM.2
            private final BluetoothStackWIDCOMM this$0;

            {
                this.this$0 = this;
            }

            @Override // com.intel.bluetooth.SearchServicesRunnable
            public int runSearchServices(SearchServicesThread sst, int[] attrSet2, UUID[] uuidSet2, RemoteDevice device2, DiscoveryListener listener2) throws BluetoothStateException {
                Class clsClass$;
                int i2;
                if (BluetoothStackWIDCOMM.class$com$intel$bluetooth$BluetoothStackWIDCOMM == null) {
                    clsClass$ = BluetoothStackWIDCOMM.class$("com.intel.bluetooth.BluetoothStackWIDCOMM");
                    BluetoothStackWIDCOMM.class$com$intel$bluetooth$BluetoothStackWIDCOMM = clsClass$;
                } else {
                    clsClass$ = BluetoothStackWIDCOMM.class$com$intel$bluetooth$BluetoothStackWIDCOMM;
                }
                Class cls = clsClass$;
                synchronized (clsClass$) {
                    byte[] uuidValue = Utils.UUIDToByteArray(BluetoothConsts.L2CAP_PROTOCOL_UUID);
                    int u2 = 0;
                    while (true) {
                        if (u2 < uuidSet2.length) {
                            if (!uuidSet2[u2].equals(BluetoothConsts.L2CAP_PROTOCOL_UUID)) {
                                if (uuidSet2[u2].equals(BluetoothConsts.RFCOMM_PROTOCOL_UUID)) {
                                    uuidValue = Utils.UUIDToByteArray(uuidSet2[u2]);
                                } else {
                                    uuidValue = Utils.UUIDToByteArray(uuidSet2[u2]);
                                    break;
                                }
                            }
                            u2++;
                        }
                    }
                    try {
                        long[] handles = this.this$0.runSearchServicesImpl(sst, uuidValue, RemoteDeviceHelper.getAddress(device2));
                        if (handles == null) {
                            DebugLog.debug("SERVICE_SEARCH_ERROR");
                            return 3;
                        }
                        if (handles.length > 0) {
                            Vector records = new Vector();
                            int[] uuidFilerAttrIDs = {1, 4};
                            int[] requiredAttrIDs = {0, 2, 3};
                            for (i2 = 0; i2 < handles.length; i2++) {
                                ServiceRecordImpl sr = new ServiceRecordImpl(this.this$0, device2, handles[i2]);
                                try {
                                    sr.populateRecord(uuidFilerAttrIDs);
                                    int u3 = 0;
                                    while (true) {
                                        if (u3 >= uuidSet2.length) {
                                            if (this.this$0.isServiceRecordDiscoverable(RemoteDeviceHelper.getAddress(device2), sr.getHandle())) {
                                                records.addElement(sr);
                                                sr.populateRecord(requiredAttrIDs);
                                                if (attrSet2 != null) {
                                                    sr.populateRecord(attrSet2);
                                                }
                                                DebugLog.debug(new StringBuffer().append("ServiceRecord (").append(i2).append(") sr.handle").toString(), handles[i2]);
                                                DebugLog.debug(new StringBuffer().append("ServiceRecord (").append(i2).append(")").toString(), sr);
                                            }
                                        } else if (sr.hasServiceClassUUID(uuidSet2[u3]) || sr.hasProtocolClassUUID(uuidSet2[u3])) {
                                            u3++;
                                        }
                                    }
                                } catch (Exception e2) {
                                    DebugLog.debug("populateRecord error", (Throwable) e2);
                                }
                            }
                            if (records.size() != 0) {
                                DebugLog.debug("SERVICE_SEARCH_COMPLETED");
                                ServiceRecord[] fileteredRecords = (ServiceRecord[]) Utils.vector2toArray(records, new ServiceRecord[records.size()]);
                                listener2.servicesDiscovered(sst.getTransID(), fileteredRecords);
                                return 1;
                            }
                        }
                        DebugLog.debug("SERVICE_SEARCH_NO_RECORDS");
                        return 4;
                    } catch (SearchServicesTerminatedException e3) {
                        DebugLog.debug("SERVICE_SEARCH_TERMINATED");
                        return 2;
                    }
                }
                if (sst.isTerminated()) {
                    DebugLog.debug("SERVICE_SEARCH_TERMINATED");
                    return 2;
                }
            }
        };
        return SearchServicesThread.startSearchServices(this, searchRunnable, attrSet, uuidSet, device, listener);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelServiceSearch(int transID) {
        SearchServicesThread sst = SearchServicesThread.getServiceSearchThread(transID);
        if (sst != null) {
            synchronized (this) {
                if (!sst.isTerminated()) {
                    sst.setTerminated();
                    cancelServiceSearchImpl();
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecord, int[] attrIDs) throws IOException {
        if (attrIDs.length > 256) {
            throw new IllegalArgumentException();
        }
        boolean anyRetrived = false;
        for (int id : attrIDs) {
            try {
                byte[] sdpStruct = getServiceAttribute(id, serviceRecord.getHandle());
                if (sdpStruct != null) {
                    DataElement element = new BluetoothStackWIDCOMMSDPInputStream(new ByteArrayInputStream(sdpStruct)).readElement();
                    if (id == 4) {
                        Enumeration protocolsSeqEnum = (Enumeration) element.getValue();
                        if (protocolsSeqEnum.hasMoreElements()) {
                            DataElement protocolElement = (DataElement) protocolsSeqEnum.nextElement2();
                            if (protocolElement.getDataType() != 48) {
                                DataElement newMainSeq = new DataElement(48);
                                newMainSeq.addElement(element);
                                element = newMainSeq;
                            }
                        }
                    }
                    serviceRecord.populateAttributeValue(id, element);
                    anyRetrived = true;
                }
            } catch (Throwable th) {
            }
        }
        return anyRetrived;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long connectionRfOpenClientConnection(BluetoothConnectionParams params) throws IOException {
        verifyDeviceReady();
        return connectionRfOpenClientConnectionImpl(params.address, params.channel, params.authenticate, params.encrypt, params.timeout);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseClientConnection(long handle) throws IOException {
        closeRfCommPortImpl(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, int b2) throws IOException {
        byte[] buf = {(byte) (b2 & 255)};
        connectionRfWriteImpl(handle, buf, 0, 1);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, byte[] b2, int off, int len) throws IOException {
        if (len < 65535) {
            connectionRfWriteImpl(handle, b2, off, len);
            return;
        }
        int i2 = 0;
        while (true) {
            int done = i2;
            if (done < len) {
                int l2 = len - done;
                if (l2 > 65535) {
                    l2 = 65535;
                }
                connectionRfWriteImpl(handle, b2, off + done, l2);
                i2 = done + 65535;
            } else {
                return;
            }
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfFlush(long handle) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int rfGetSecurityOpt(long handle, int expected) throws IOException {
        return expected;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean rfEncrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord) throws IOException {
        verifyDeviceReady();
        byte[] uuidValue = Utils.UUIDToByteArray(params.uuid);
        byte[] uuidValue2 = params.obex ? null : Utils.UUIDToByteArray(BluetoothConsts.SERIAL_PORT_UUID);
        long handle = rfServerOpenImpl(uuidValue, uuidValue2, params.obex, params.name, params.authenticate, params.encrypt);
        int channel = rfServerSCN(handle);
        DebugLog.debug("serverSCN", channel);
        serviceRecord.populateRFCOMMAttributes(handle, channel, params.uuid, params.name, params.obex);
        return handle;
    }

    private byte[] long2byte(long value, int len) {
        byte[] cvalue = new byte[len];
        long l2 = value;
        for (int i2 = len - 1; i2 >= 0; i2--) {
            cvalue[i2] = (byte) (l2 & 255);
            l2 >>= 8;
        }
        return cvalue;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        sdpServiceUpdateServiceRecord(handle, 'r', serviceRecord);
    }

    private byte[] sdpServiceSequenceAttribute(Enumeration en) throws ServiceRegistrationException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SDPOutputStream sdpOut = new SDPOutputStream(out);
        while (en.hasMoreElements()) {
            try {
                sdpOut.writeElement((DataElement) en.nextElement2());
            } catch (IOException e2) {
                throw new ServiceRegistrationException(e2.getMessage());
            }
        }
        return out.toByteArray();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v45, types: [byte[], byte[][]] */
    private void sdpServiceUpdateServiceRecord(long handle, char handleType, ServiceRecordImpl serviceRecord) throws ServiceRegistrationException {
        int[] ids = serviceRecord.getAttributeIDs();
        if (ids == null || ids.length == 0) {
            return;
        }
        DataElement serviceClassIDList = serviceRecord.getAttributeValue(1);
        if (serviceClassIDList.getDataType() != 48) {
            throw new ServiceRegistrationException("Invalid serviceClassIDList");
        }
        Enumeration en = (Enumeration) serviceClassIDList.getValue();
        Vector uuids = new Vector();
        while (en.hasMoreElements()) {
            DataElement u2 = (DataElement) en.nextElement2();
            if (u2.getDataType() != 24) {
                throw new ServiceRegistrationException(new StringBuffer().append("Invalid serviceClassIDList element ").append((Object) u2).toString());
            }
            uuids.add(u2.getValue());
        }
        if (uuids.size() > 0) {
            ?? r0 = new byte[uuids.size()];
            for (int u3 = 0; u3 < r0.length; u3++) {
                r0[u3] = Utils.UUIDToByteArray((UUID) uuids.elementAt(u3));
            }
            sdpServiceAddServiceClassIdList(handle, handleType, r0);
        }
        for (int id : ids) {
            switch (id) {
                case 0:
                case 1:
                case 4:
                case 256:
                    break;
                default:
                    DataElement d2 = serviceRecord.getAttributeValue(id);
                    switch (d2.getDataType()) {
                        case 0:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 0, null);
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 13:
                        case 14:
                        case 15:
                        case 21:
                        case 22:
                        case 23:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                        default:
                            throw new ServiceRegistrationException(new StringBuffer().append("Invalid ").append(d2.getDataType()).toString());
                        case 8:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 1, long2byte(d2.getLong(), 1));
                            break;
                        case 9:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 1, long2byte(d2.getLong(), 2));
                            break;
                        case 10:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 1, long2byte(d2.getLong(), 4));
                            break;
                        case 11:
                        case 12:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 1, (byte[]) d2.getValue());
                            break;
                        case 16:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 2, long2byte(d2.getLong(), 1));
                            break;
                        case 17:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 2, long2byte(d2.getLong(), 2));
                            break;
                        case 18:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 2, long2byte(d2.getLong(), 4));
                            break;
                        case 19:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 2, long2byte(d2.getLong(), 8));
                            break;
                        case 20:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 2, (byte[]) d2.getValue());
                            break;
                        case 24:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 3, BluetoothStackWIDCOMMSDPInputStream.getUUIDHexBytes((UUID) d2.getValue()));
                            break;
                        case 32:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 4, Utils.getUTF8Bytes(d2.getValue().toString()));
                            break;
                        case 40:
                            byte[] bArr = new byte[1];
                            bArr[0] = (byte) (d2.getBoolean() ? 1 : 0);
                            sdpServiceAddAttribute(handle, handleType, id, (short) 5, bArr);
                            break;
                        case 48:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 6, sdpServiceSequenceAttribute((Enumeration) d2.getValue()));
                            break;
                        case 56:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 7, sdpServiceSequenceAttribute((Enumeration) d2.getValue()));
                            break;
                        case 64:
                            sdpServiceAddAttribute(handle, handleType, id, (short) 8, Utils.getASCIIBytes(d2.getValue().toString()));
                            break;
                    }
            }
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        rfServerCloseImpl(handle);
    }

    private void validateMTU(int receiveMTU, int transmitMTU) {
        if (receiveMTU > 1024) {
            throw new IllegalArgumentException(new StringBuffer().append("invalid ReceiveMTU value ").append(receiveMTU).toString());
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2OpenClientConnection(BluetoothConnectionParams params, int receiveMTU, int transmitMTU) throws IOException {
        verifyDeviceReady();
        validateMTU(receiveMTU, transmitMTU);
        return l2OpenClientConnectionImpl(params.address, params.channel, params.authenticate, params.encrypt, receiveMTU, transmitMTU, params.timeout);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerOpen(BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU, ServiceRecordImpl serviceRecord) throws IOException {
        verifyDeviceReady();
        validateMTU(receiveMTU, transmitMTU);
        byte[] uuidValue = Utils.UUIDToByteArray(params.uuid);
        long handle = l2ServerOpenImpl(uuidValue, params.authenticate, params.encrypt, params.name, receiveMTU, transmitMTU, params.bluecove_ext_psm);
        int channel = l2ServerPSM(handle);
        int serviceRecordHandle = (int) handle;
        serviceRecord.populateL2CAPAttributes(serviceRecordHandle, channel, params.uuid, params.name);
        return handle;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        sdpServiceUpdateServiceRecord(handle, 'l', serviceRecord);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        l2ServerCloseImpl(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetSecurityOpt(long handle, int expected) throws IOException {
        return expected;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Encrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }
}
