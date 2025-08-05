package com.intel.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackOSX.class */
class BluetoothStackOSX implements BluetoothStack, BluetoothStackExtension {
    public static final boolean debug = false;
    private static BluetoothStackOSX singleInstance = null;
    private static final int ATTR_RETRIEVABLE_MAX = 256;
    private int localDeviceSupportedSoftwareVersion;
    private static final int BLUETOOTH_SOFTWARE_VERSION_2_0_0 = 20000;
    private final Vector deviceDiscoveryListeners = new Vector();
    private final Hashtable deviceDiscoveryListenerReportedDevices = new Hashtable();
    private int receive_mtu_max = -1;
    private long lastDeviceDiscoveryTime = 0;
    private int localDeviceServiceClasses = 0;
    private Thread localDeviceServiceClassMaintainer = null;

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isNativeCodeLoaded();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int getLibraryVersion();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int detectBluetoothStack();

    private native boolean initializeImpl();

    @Override // com.intel.bluetooth.BluetoothStack
    public native void enableNativeDebug(Class cls, boolean z2);

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getLocalDeviceBluetoothAddress() throws BluetoothStateException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getLocalDeviceName();

    private native int getDeviceClassImpl();

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean setLocalDeviceServiceClassesImpl(int i2);

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isLocalDevicePowerOn();

    private native boolean isLocalDeviceFeatureSwitchRoles();

    private native boolean isLocalDeviceFeatureParkMode();

    private native boolean isLocalDeviceFeatureRSSI();

    private native int getLocalDeviceL2CAPMTUMaximum();

    private native int getLocalDeviceSupportedSoftwareVersion();

    private native String getLocalDeviceSoftwareVersionInfo();

    private native int getLocalDeviceManufacturer();

    private native String getLocalDeviceVersion();

    private native boolean getLocalDeviceDiscoverableImpl();

    private native boolean retrieveDevicesImpl(int i2, RetrieveDevicesCallback retrieveDevicesCallback);

    private native boolean isRemoteDeviceTrustedImpl(long j2);

    private native boolean isRemoteDeviceAuthenticatedImpl(long j2);

    private native int readRemoteDeviceRSSIImpl(long j2) throws IOException;

    private native boolean authenticateRemoteDeviceImpl(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getRemoteDeviceFriendlyName(long j2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native int runDeviceInquiryImpl(DeviceInquiryRunnable deviceInquiryRunnable, DeviceInquiryThread deviceInquiryThread, int i2, int i3, DiscoveryListener discoveryListener) throws BluetoothStateException;

    private native boolean deviceInquiryCancelImpl();

    /* JADX INFO: Access modifiers changed from: private */
    public native int runSearchServicesImpl(long j2, int i2) throws BluetoothStateException, SearchServicesException;

    private native void cancelServiceSearchImpl(int i2);

    private native byte[] getServiceAttributeImpl(long j2, long j3, int i2);

    private native long connectionRfOpenClientConnectionImpl(long j2, int i2, boolean z2, boolean z3, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfCloseClientConnection(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int rfGetSecurityOpt(long j2, int i2) throws IOException;

    private native long rfServerCreateImpl(byte[] bArr, boolean z2, String str, boolean z3, boolean z4) throws IOException;

    private native int rfServerGetChannelID(long j2) throws IOException;

    private native void rfServerCloseImpl(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long rfServerAcceptAndOpenRfServerConnection(long j2) throws IOException;

    private native void sdpServiceUpdateServiceRecordPublish(long j2, char c2) throws ServiceRegistrationException;

    private native void sdpServiceAddAttribute(long j2, char c2, int i2, int i3, long j3, byte[] bArr) throws ServiceRegistrationException;

    private native void sdpServiceSequenceAttributeStart(long j2, char c2, int i2, int i3) throws ServiceRegistrationException;

    private native void sdpServiceSequenceAttributeEnd(long j2, char c2, int i2) throws ServiceRegistrationException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfRead(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfRead(long j2, byte[] bArr, int i2, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfReadAvailable(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfWrite(long j2, byte[] bArr, int i2, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long getConnectionRfRemoteAddress(long j2) throws IOException;

    private native long l2OpenClientConnectionImpl(long j2, int i2, boolean z2, boolean z3, int i3, int i4, int i5) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void l2CloseClientConnection(long j2) throws IOException;

    private native long l2ServerOpenImpl(byte[] bArr, boolean z2, boolean z3, String str, int i2, int i3, int i4) throws IOException;

    public native int l2ServerPSM(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long l2ServerAcceptAndOpenServerConnection(long j2) throws IOException;

    private native void l2ServerCloseImpl(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2GetSecurityOpt(long j2, int i2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean l2Ready(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2Receive(long j2, byte[] bArr) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void l2Send(long j2, byte[] bArr, int i2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2GetReceiveMTU(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2GetTransmitMTU(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long l2RemoteAddress(long j2) throws IOException;

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static long access$302(com.intel.bluetooth.BluetoothStackOSX r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.lastDeviceDiscoveryTime = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.intel.bluetooth.BluetoothStackOSX.access$302(com.intel.bluetooth.BluetoothStackOSX, long):long");
    }

    BluetoothStackOSX() {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public BluetoothStack.LibraryInformation[] requireNativeLibraries() {
        return BluetoothStack.LibraryInformation.library("bluecove");
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getStackID() {
        return BlueCoveImpl.STACK_OSX;
    }

    public String toString() {
        return getStackID();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getFeatureSet() {
        if (this.localDeviceSupportedSoftwareVersion >= BLUETOOTH_SOFTWARE_VERSION_2_0_0) {
            return 7 | (isLocalDeviceFeatureRSSI() ? 8 : 0);
        }
        return 3;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void initialize() throws BluetoothStateException {
        if (singleInstance != null) {
            throw new BluetoothStateException(new StringBuffer().append("Only one instance of ").append(getStackID()).append(" stack supported").toString());
        }
        String sysVersion = System.getProperty("os.version");
        String jreDataModel = System.getProperty("sun.arch.data.model");
        boolean osIsLeopard = sysVersion != null && sysVersion.startsWith("10.5");
        boolean jreIs64Bit = "64".equals(jreDataModel);
        if (osIsLeopard && jreIs64Bit) {
            throw new BluetoothStateException("Mac OS X 10.5 not supported with a 64 bit JRE");
        }
        this.localDeviceSupportedSoftwareVersion = getLocalDeviceSupportedSoftwareVersion();
        DebugLog.debug("localDeviceSupportedSoftwareVersion", this.localDeviceSupportedSoftwareVersion);
        if (!initializeImpl()) {
            throw new BluetoothStateException("OS X BluetoothStack not found");
        }
        singleInstance = this;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void destroy() {
        if (this.localDeviceSupportedSoftwareVersion >= BLUETOOTH_SOFTWARE_VERSION_2_0_0) {
            setLocalDeviceServiceClassesImpl(0);
        }
        singleInstance = null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isCurrentThreadInterruptedCallback() {
        return UtilsJavaSE.isCurrentThreadInterrupted();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public DeviceClass getLocalDeviceClass() {
        return new DeviceClass(getDeviceClassImpl());
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackOSX$MaintainDeviceServiceClassesThread.class */
    private class MaintainDeviceServiceClassesThread extends Thread {
        private final BluetoothStackOSX this$0;

        MaintainDeviceServiceClassesThread(BluetoothStackOSX bluetoothStackOSX) {
            super("MaintainDeviceServiceClassesThread");
            this.this$0 = bluetoothStackOSX;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            boolean updated = true;
            while (true) {
                int delay = 120000;
                if (!updated) {
                    delay = 1000;
                }
                try {
                    Thread.sleep(delay);
                    if (this.this$0.localDeviceServiceClasses != 0) {
                        updated = this.this$0.setLocalDeviceServiceClassesImpl(this.this$0.localDeviceServiceClasses);
                    } else if (!updated) {
                        updated = true;
                    }
                } catch (InterruptedException e2) {
                    return;
                }
            }
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public synchronized void setLocalDeviceServiceClasses(int classOfDevice) {
        if (this.localDeviceSupportedSoftwareVersion < BLUETOOTH_SOFTWARE_VERSION_2_0_0) {
            return;
        }
        if (classOfDevice != this.localDeviceServiceClasses) {
            setLocalDeviceServiceClassesImpl(classOfDevice);
        }
        this.localDeviceServiceClasses = classOfDevice;
        if (classOfDevice != 0 && this.localDeviceServiceClassMaintainer == null) {
            this.localDeviceServiceClassMaintainer = new MaintainDeviceServiceClassesThread(this);
            UtilsJavaSE.threadSetDaemon(this.localDeviceServiceClassMaintainer);
            this.localDeviceServiceClassMaintainer.start();
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceProperty(String property) {
        if (BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_DEVICES_MAX.equals(property)) {
            return isLocalDeviceFeatureParkMode() ? "255" : "7";
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_SD_TRANS_MAX.equals(property)) {
            return "7";
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
            return String.valueOf(receiveMTUMAX());
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_VERSION.equals(property)) {
            return getLocalDeviceVersion();
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_MANUFACTURER.equals(property)) {
            return String.valueOf(getLocalDeviceManufacturer());
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_STACK_VERSION.equals(property)) {
            return getLocalDeviceSoftwareVersionInfo();
        }
        return null;
    }

    private int receiveMTUMAX() {
        if (this.receive_mtu_max < 0) {
            this.receive_mtu_max = getLocalDeviceL2CAPMTUMaximum();
        }
        return this.receive_mtu_max;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getLocalDeviceDiscoverable() {
        if (getLocalDeviceDiscoverableImpl()) {
            return 10390323;
        }
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
        if (getLocalDeviceDiscoverable() == mode) {
            return true;
        }
        return false;
    }

    private void verifyDeviceReady() throws BluetoothStateException {
        if (!isLocalDevicePowerOn()) {
            throw new BluetoothStateException("Bluetooth Device is not ready");
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public RemoteDevice[] retrieveDevices(int option) {
        Vector devices = new Vector();
        RetrieveDevicesCallback retrieveDevicesCallback = new RetrieveDevicesCallback(this, devices) { // from class: com.intel.bluetooth.BluetoothStackOSX.1
            private final Vector val$devices;
            private final BluetoothStackOSX this$0;

            {
                this.this$0 = this;
                this.val$devices = devices;
            }

            @Override // com.intel.bluetooth.RetrieveDevicesCallback
            public void deviceFoundCallback(long deviceAddr, int deviceClass, String deviceName, boolean paired) {
                DebugLog.debug("device found", deviceAddr);
                RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this.this$0, deviceAddr, deviceName, paired);
                if (!this.val$devices.contains(remoteDevice)) {
                    this.val$devices.add(remoteDevice);
                }
            }
        };
        if (retrieveDevicesImpl(option, retrieveDevicesCallback)) {
            return RemoteDeviceHelper.remoteDeviceListToArray(devices);
        }
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceTrusted(long address) {
        return new Boolean(isRemoteDeviceTrustedImpl(address));
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceAuthenticated(long address) {
        return new Boolean(isRemoteDeviceAuthenticatedImpl(address));
    }

    @Override // com.intel.bluetooth.BluetoothStackExtension
    public int readRemoteDeviceRSSI(long address) throws IOException {
        return readRemoteDeviceRSSIImpl(address);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address) throws IOException {
        return authenticateRemoteDeviceImpl(address);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address, String passkey) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void removeAuthenticationWithRemoteDevice(long address) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        long sinceDiscoveryLast = System.currentTimeMillis() - this.lastDeviceDiscoveryTime;
        if (sinceDiscoveryLast < 7000) {
            try {
                Thread.sleep(7000 - sinceDiscoveryLast);
            } catch (InterruptedException e2) {
                throw new BluetoothStateException();
            }
        }
        this.deviceDiscoveryListeners.addElement(listener);
        this.deviceDiscoveryListenerReportedDevices.put(listener, new Vector());
        DeviceInquiryRunnable inquiryRunnable = new DeviceInquiryRunnable(this) { // from class: com.intel.bluetooth.BluetoothStackOSX.2
            private final BluetoothStackOSX this$0;

            {
                this.this$0 = this;
            }

            /* JADX WARN: Failed to check method for inline after forced processcom.intel.bluetooth.BluetoothStackOSX.access$302(com.intel.bluetooth.BluetoothStackOSX, long):long */
            @Override // com.intel.bluetooth.DeviceInquiryRunnable
            public int runDeviceInquiry(DeviceInquiryThread startedNotify, int accessCode2, DiscoveryListener listener2) throws BluetoothStateException {
                try {
                    int iRunDeviceInquiryImpl = this.this$0.runDeviceInquiryImpl(this, startedNotify, accessCode2, DeviceInquiryThread.getConfigDeviceInquiryDuration(), listener2);
                    BluetoothStackOSX.access$302(this.this$0, System.currentTimeMillis());
                    this.this$0.deviceDiscoveryListeners.removeElement(listener2);
                    this.this$0.deviceDiscoveryListenerReportedDevices.remove(listener2);
                    return iRunDeviceInquiryImpl;
                } catch (Throwable th) {
                    BluetoothStackOSX.access$302(this.this$0, System.currentTimeMillis());
                    this.this$0.deviceDiscoveryListeners.removeElement(listener2);
                    this.this$0.deviceDiscoveryListenerReportedDevices.remove(listener2);
                    throw th;
                }
            }

            @Override // com.intel.bluetooth.DeviceInquiryRunnable
            public void deviceDiscoveredCallback(DiscoveryListener listener2, long deviceAddr, int deviceClass, String deviceName, boolean paired) {
                if (!this.this$0.deviceDiscoveryListeners.contains(listener2)) {
                    return;
                }
                RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this.this$0, deviceAddr, deviceName, paired);
                Vector reported = (Vector) this.this$0.deviceDiscoveryListenerReportedDevices.get(listener2);
                if (reported == null || reported.contains(remoteDevice)) {
                    return;
                }
                reported.addElement(remoteDevice);
                DeviceClass cod = new DeviceClass(deviceClass);
                DebugLog.debug("deviceDiscoveredCallback address", remoteDevice.getBluetoothAddress());
                DebugLog.debug("deviceDiscoveredCallback deviceClass", cod);
                listener2.deviceDiscovered(remoteDevice, cod);
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
    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        SearchServicesRunnable searchRunnable = new SearchServicesRunnable(this) { // from class: com.intel.bluetooth.BluetoothStackOSX.3
            private final BluetoothStackOSX this$0;

            {
                this.this$0 = this;
            }

            @Override // com.intel.bluetooth.SearchServicesRunnable
            public int runSearchServices(SearchServicesThread sst, int[] attrSet2, UUID[] uuidSet2, RemoteDevice device2, DiscoveryListener listener2) throws BluetoothStateException {
                int u2;
                sst.searchServicesStartedCallback();
                try {
                    int recordsSize = this.this$0.runSearchServicesImpl(RemoteDeviceHelper.getAddress(device2), sst.getTransID());
                    if (sst.isTerminated()) {
                        return 2;
                    }
                    if (recordsSize == 0) {
                        return 4;
                    }
                    Vector records = new Vector();
                    int[] uuidFilerAttrIDs = {1, 4};
                    int[] requiredAttrIDs = {0, 2, 3};
                    for (int i2 = 0; i2 < recordsSize; i2++) {
                        ServiceRecordImpl sr = new ServiceRecordImpl(this.this$0, device2, i2);
                        try {
                            sr.populateRecord(uuidFilerAttrIDs);
                            for (0; u2 < uuidSet2.length; u2 + 1) {
                                u2 = (sr.hasServiceClassUUID(uuidSet2[u2]) || sr.hasProtocolClassUUID(uuidSet2[u2])) ? u2 + 1 : 0;
                            }
                            records.addElement(sr);
                            sr.populateRecord(requiredAttrIDs);
                            if (attrSet2 != null) {
                                sr.populateRecord(attrSet2);
                            }
                            DebugLog.debug(new StringBuffer().append("ServiceRecord (").append(i2).append(")").toString(), sr);
                        } catch (Exception e2) {
                            DebugLog.debug("populateRecord error", (Throwable) e2);
                        }
                        if (sst.isTerminated()) {
                            DebugLog.debug(new StringBuffer().append("SERVICE_SEARCH_TERMINATED ").append(sst.getTransID()).toString());
                            return 2;
                        }
                    }
                    if (records.size() != 0) {
                        DebugLog.debug(new StringBuffer().append("SERVICE_SEARCH_COMPLETED ").append(sst.getTransID()).toString());
                        ServiceRecord[] fileteredRecords = (ServiceRecord[]) Utils.vector2toArray(records, new ServiceRecord[records.size()]);
                        listener2.servicesDiscovered(sst.getTransID(), fileteredRecords);
                        return 1;
                    }
                    return 4;
                } catch (SearchServicesDeviceNotReachableException e3) {
                    return 6;
                } catch (SearchServicesTerminatedException e4) {
                    return 2;
                } catch (SearchServicesException e5) {
                    return 3;
                }
            }
        };
        return SearchServicesThread.startSearchServices(this, searchRunnable, attrSet, uuidSet, device, listener);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelServiceSearch(int transID) {
        SearchServicesThread sst = SearchServicesThread.getServiceSearchThread(transID);
        if (sst != null) {
            synchronized (this) {
                if (!sst.isTerminated()) {
                    sst.setTerminated();
                    cancelServiceSearchImpl(transID);
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
        long address = RemoteDeviceHelper.getAddress(serviceRecord.getHostDevice());
        for (int id : attrIDs) {
            try {
                byte[] blob = getServiceAttributeImpl(address, serviceRecord.getHandle(), id);
                if (blob != null) {
                    DataElement element = new SDPInputStream(new ByteArrayInputStream(blob)).readElement();
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
        long jConnectionRfOpenClientConnectionImpl;
        if (params.encrypt) {
            throw new BluetoothConnectionException(2, "encrypt mode not supported");
        }
        Object lock = RemoteDeviceHelper.createRemoteDevice(this, params.address, null, false);
        synchronized (lock) {
            jConnectionRfOpenClientConnectionImpl = connectionRfOpenClientConnectionImpl(params.address, params.channel, params.authenticate, params.encrypt, params.timeout);
        }
        return jConnectionRfOpenClientConnectionImpl;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean rfEncrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord) throws IOException {
        verifyDeviceReady();
        if (params.encrypt) {
            throw new BluetoothConnectionException(2, "encrypt mode not supported");
        }
        byte[] uuidValue = Utils.UUIDToByteArray(params.uuid);
        long handle = rfServerCreateImpl(uuidValue, params.obex, params.name, params.authenticate, params.encrypt);
        boolean success = false;
        try {
            int channel = rfServerGetChannelID(handle);
            serviceRecord.populateRFCOMMAttributes(handle, channel, params.uuid, params.name, params.obex);
            success = true;
            if (1 == 0) {
                rfServerCloseImpl(handle);
            }
            return handle;
        } catch (Throwable th) {
            if (!success) {
                rfServerCloseImpl(handle);
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        rfServerCloseImpl(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        sdpServiceUpdateServiceRecord(handle, 'R', serviceRecord);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseServerConnection(long handle) throws IOException {
        connectionRfCloseClientConnection(handle);
    }

    private void sdpServiceAddAttribute(long handle, char handleType, int attrID, DataElement element) throws ServiceRegistrationException {
        int type = element.getDataType();
        switch (type) {
            case 0:
                sdpServiceAddAttribute(handle, handleType, attrID, type, 0L, null);
                return;
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
                throw new IllegalArgumentException();
            case 8:
            case 9:
            case 10:
            case 16:
            case 17:
            case 18:
            case 19:
                sdpServiceAddAttribute(handle, handleType, attrID, type, element.getLong(), null);
                return;
            case 11:
            case 12:
            case 20:
                sdpServiceAddAttribute(handle, handleType, attrID, type, 0L, (byte[]) element.getValue());
                return;
            case 24:
                sdpServiceAddAttribute(handle, handleType, attrID, type, 0L, Utils.UUIDToByteArray((UUID) element.getValue()));
                return;
            case 32:
                byte[] bs2 = Utils.getUTF8Bytes((String) element.getValue());
                sdpServiceAddAttribute(handle, handleType, attrID, type, 0L, bs2);
                return;
            case 40:
                sdpServiceAddAttribute(handle, handleType, attrID, type, element.getBoolean() ? 1L : 0L, null);
                return;
            case 48:
            case 56:
                sdpServiceSequenceAttributeStart(handle, handleType, attrID, type);
                Enumeration e2 = (Enumeration) element.getValue();
                while (e2.hasMoreElements()) {
                    DataElement child = (DataElement) e2.nextElement2();
                    sdpServiceAddAttribute(handle, handleType, -1, child);
                }
                sdpServiceSequenceAttributeEnd(handle, handleType, attrID);
                return;
            case 64:
                byte[] bu2 = Utils.getASCIIBytes((String) element.getValue());
                sdpServiceAddAttribute(handle, handleType, attrID, type, 0L, bu2);
                return;
        }
    }

    private void sdpServiceUpdateServiceRecord(long handle, char handleType, ServiceRecordImpl serviceRecord) throws ServiceRegistrationException {
        int[] ids = serviceRecord.getAttributeIDs();
        if (ids == null || ids.length == 0) {
            return;
        }
        for (int attrID : ids) {
            switch (attrID) {
                case 0:
                case 4:
                case 256:
                    break;
                default:
                    sdpServiceAddAttribute(handle, handleType, attrID, serviceRecord.getAttributeValue(attrID));
                    break;
            }
        }
        sdpServiceUpdateServiceRecordPublish(handle, handleType);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfFlush(long handle) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, int b2) throws IOException {
        byte[] buf = {(byte) (b2 & 255)};
        connectionRfWrite(handle, buf, 0, 1);
    }

    private void validateMTU(int receiveMTU, int transmitMTU) {
        if (receiveMTU > receiveMTUMAX()) {
            throw new IllegalArgumentException(new StringBuffer().append("invalid ReceiveMTU value ").append(receiveMTU).toString());
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2OpenClientConnection(BluetoothConnectionParams params, int receiveMTU, int transmitMTU) throws IOException {
        long jL2OpenClientConnectionImpl;
        validateMTU(receiveMTU, transmitMTU);
        if (params.encrypt) {
            throw new BluetoothConnectionException(2, "encrypt mode not supported");
        }
        Object lock = RemoteDeviceHelper.createRemoteDevice(this, params.address, null, false);
        synchronized (lock) {
            jL2OpenClientConnectionImpl = l2OpenClientConnectionImpl(params.address, params.channel, params.authenticate, params.encrypt, receiveMTU, transmitMTU, params.timeout);
        }
        return jL2OpenClientConnectionImpl;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerOpen(BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU, ServiceRecordImpl serviceRecord) throws IOException {
        verifyDeviceReady();
        validateMTU(receiveMTU, transmitMTU);
        if (params.encrypt) {
            throw new BluetoothConnectionException(2, "encrypt mode not supported");
        }
        byte[] uuidValue = Utils.UUIDToByteArray(params.uuid);
        long handle = l2ServerOpenImpl(uuidValue, params.authenticate, params.encrypt, params.name, receiveMTU, transmitMTU, params.bluecove_ext_psm);
        int channel = l2ServerPSM(handle);
        int serviceRecordHandle = (int) handle;
        serviceRecord.populateL2CAPAttributes(serviceRecordHandle, channel, params.uuid, params.name);
        return handle;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        sdpServiceUpdateServiceRecord(handle, 'L', serviceRecord);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2CloseServerConnection(long handle) throws IOException {
        l2CloseClientConnection(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        l2ServerCloseImpl(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Encrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }
}
