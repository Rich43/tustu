package com.intel.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackBlueSoleil.class */
class BluetoothStackBlueSoleil implements BluetoothStack, DeviceInquiryRunnable, SearchServicesRunnable {
    private static BluetoothStackBlueSoleil singleInstance = null;
    private boolean initialized = false;
    private DiscoveryListener currentDeviceDiscoveryListener;

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
    public native String getLocalDeviceBluetoothAddress();

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getLocalDeviceName();

    public native int getDeviceClassImpl();

    native boolean isBlueSoleilStarted(int i2);

    private native boolean isBluetoothReady(int i2);

    native int getStackVersionInfo();

    native int getDeviceVersion();

    native int getDeviceManufacturer();

    public native int runDeviceInquiryImpl(DeviceInquiryThread deviceInquiryThread, int i2, DiscoveryListener discoveryListener) throws BluetoothStateException;

    public native boolean cancelInquirympl();

    private native int runSearchServicesImpl(SearchServicesThread searchServicesThread, DiscoveryListener discoveryListener, byte[] bArr, long j2, RemoteDevice remoteDevice) throws BluetoothStateException;

    private native long connectionRfOpenImpl(long j2, byte[] bArr) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfCloseClientConnection(long j2) throws IOException;

    private native long rfServerOpenImpl(byte[] bArr, String str, boolean z2, boolean z3) throws IOException;

    private native int rfServerSCN(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long rfServerAcceptAndOpenRfServerConnection(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void rfServerClose(long j2, ServiceRecordImpl serviceRecordImpl) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long getConnectionRfRemoteAddress(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfRead(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfRead(long j2, byte[] bArr, int i2, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native int connectionRfReadAvailable(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfWrite(long j2, int i2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfWrite(long j2, byte[] bArr, int i2, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfFlush(long j2) throws IOException;

    BluetoothStackBlueSoleil() {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getStackID() {
        return BlueCoveImpl.STACK_BLUESOLEIL;
    }

    public String toString() {
        return getStackID();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public BluetoothStack.LibraryInformation[] requireNativeLibraries() {
        return BluetoothStack.LibraryInformation.library("intelbth");
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void initialize() throws BluetoothStateException {
        if (singleInstance != null) {
            throw new BluetoothStateException(new StringBuffer().append("Only one instance of ").append(getStackID()).append(" stack supported").toString());
        }
        if (!initializeImpl()) {
            DebugLog.fatal("Can't initialize BlueSoleil");
            throw new BluetoothStateException("BlueSoleil BluetoothStack not found");
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
            DebugLog.debug("BlueSoleil destroyed");
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
        return true;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getLocalDeviceDiscoverable() {
        if (isBluetoothReady(2)) {
            return 10390323;
        }
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isLocalDevicePowerOn() {
        return isBluetoothReady(15);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getFeatureSet() {
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
        if (BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY_SCAN.equals(property) || BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE_SCAN.equals(property) || BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY.equals(property)) {
            return "true";
        }
        if (BluetoothConsts.PROPERTY_BLUETOOTH_SD_ATTR_RETRIEVABLE_MAX.equals(property)) {
            return "0";
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_PROPERTY_STACK_VERSION.equals(property)) {
            return String.valueOf(getStackVersionInfo());
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

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address) throws IOException {
        return false;
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
        if (this.currentDeviceDiscoveryListener != null) {
            throw new BluetoothStateException("Another inquiry already running");
        }
        this.currentDeviceDiscoveryListener = listener;
        return DeviceInquiryThread.startInquiry(this, this, accessCode, listener);
    }

    @Override // com.intel.bluetooth.DeviceInquiryRunnable
    public int runDeviceInquiry(DeviceInquiryThread startedNotify, int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        try {
            startedNotify.deviceInquiryStartedCallback();
            int iRunDeviceInquiryImpl = runDeviceInquiryImpl(startedNotify, accessCode, listener);
            this.currentDeviceDiscoveryListener = null;
            return iRunDeviceInquiryImpl;
        } catch (Throwable th) {
            this.currentDeviceDiscoveryListener = null;
            throw th;
        }
    }

    @Override // com.intel.bluetooth.DeviceInquiryRunnable
    public void deviceDiscoveredCallback(DiscoveryListener listener, long deviceAddr, int deviceClass, String deviceName, boolean paired) {
        DebugLog.debug("deviceDiscoveredCallback", deviceName);
        RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this, deviceAddr, deviceName, paired);
        if (this.currentDeviceDiscoveryListener == null || this.currentDeviceDiscoveryListener != listener) {
            return;
        }
        listener.deviceDiscovered(remoteDevice, new DeviceClass(deviceClass));
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelInquiry(DiscoveryListener listener) {
        if (this.currentDeviceDiscoveryListener != listener) {
            return false;
        }
        this.currentDeviceDiscoveryListener = null;
        return cancelInquirympl();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getRemoteDeviceFriendlyName(long address) throws IOException {
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        return SearchServicesThread.startSearchServices(this, this, attrSet, uuidSet, device, listener);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelServiceSearch(int transID) {
        return false;
    }

    @Override // com.intel.bluetooth.SearchServicesRunnable
    public int runSearchServices(SearchServicesThread startedNotify, int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        startedNotify.searchServicesStartedCallback();
        UUID uuid = null;
        if (uuidSet != null && uuidSet.length > 0) {
            uuid = uuidSet[uuidSet.length - 1];
        }
        return runSearchServicesImpl(startedNotify, listener, Utils.UUIDToByteArray(uuid), RemoteDeviceHelper.getAddress(device), device);
    }

    public void servicesFoundCallback(SearchServicesThread startedNotify, DiscoveryListener listener, RemoteDevice device, String serviceName, byte[] uuidValue, int channel, long recordHanlde) throws RuntimeException {
        ServiceRecordImpl record = new ServiceRecordImpl(this, device, 0L);
        UUID uuid = new UUID(Utils.UUIDByteArrayToString(uuidValue), false);
        record.populateRFCOMMAttributes(recordHanlde, channel, uuid, serviceName, BluetoothConsts.obexUUIDs.contains(uuid));
        DebugLog.debug("servicesFoundCallback", record);
        RemoteDevice listedDevice = RemoteDeviceHelper.createRemoteDevice(this, device);
        RemoteDeviceHelper.setStackAttributes(this, listedDevice, new StringBuffer().append("RFCOMM_channel").append(channel).toString(), uuid);
        ServiceRecord[] records = {record};
        listener.servicesDiscovered(startedNotify.getTransID(), records);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecord, int[] attrIDs) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long connectionRfOpenClientConnection(BluetoothConnectionParams params) throws IOException {
        if (params.authenticate || params.encrypt) {
            throw new IOException("authenticate not supported on BlueSoleil");
        }
        RemoteDevice listedDevice = RemoteDeviceHelper.getCashedDevice(this, params.address);
        if (listedDevice == null) {
            throw new IOException("Device not discovered");
        }
        UUID uuid = (UUID) RemoteDeviceHelper.getStackAttributes(this, listedDevice, new StringBuffer().append("RFCOMM_channel").append(params.channel).toString());
        if (uuid == null) {
            throw new IOException("Device service not discovered");
        }
        DebugLog.debug("Connect to service UUID", uuid);
        return connectionRfOpenImpl(params.address, Utils.UUIDToByteArray(uuid));
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord) throws IOException {
        if (params.authenticate || params.encrypt) {
            throw new IOException("authenticate not supported on BlueSoleil");
        }
        byte[] uuidValue = Utils.UUIDToByteArray(params.uuid);
        long handle = rfServerOpenImpl(uuidValue, params.name, params.authenticate, params.encrypt);
        int channel = rfServerSCN(handle);
        DebugLog.debug("serverSCN", channel);
        int serviceRecordHandle = (int) handle;
        serviceRecord.populateRFCOMMAttributes(serviceRecordHandle, channel, params.uuid, params.name, false);
        return handle;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        if (!acceptAndOpen) {
            throw new ServiceRegistrationException(new StringBuffer().append("Not Supported on ").append(getStackID()).toString());
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseServerConnection(long handle) throws IOException {
        connectionRfCloseClientConnection(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int rfGetSecurityOpt(long handle, int expected) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean rfEncrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2OpenClientConnection(BluetoothConnectionParams params, int receiveMTU, int transmitMTU) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2CloseClientConnection(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerOpen(BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU, ServiceRecordImpl serviceRecord) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        throw new ServiceRegistrationException(new StringBuffer().append("Not Supported on").append(getStackID()).toString());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerAcceptAndOpenServerConnection(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2CloseServerConnection(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetSecurityOpt(long handle, int expected) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Ready(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2Receive(long handle, byte[] inBuf) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2Send(long handle, byte[] data, int transmitMTU) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetReceiveMTU(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetTransmitMTU(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2RemoteAddress(long handle) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Encrypt(long address, long handle, boolean on) throws IOException {
        throw new NotSupportedIOException(getStackID());
    }
}
