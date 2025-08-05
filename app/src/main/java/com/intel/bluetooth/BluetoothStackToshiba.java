package com.intel.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackToshiba.class */
class BluetoothStackToshiba implements BluetoothStack, DeviceInquiryRunnable, SearchServicesRunnable {
    private boolean initialized = false;
    private Vector deviceDiscoveryListeners = new Vector();
    private Hashtable deviceDiscoveryListenerFoundDevices = new Hashtable();
    private Hashtable deviceDiscoveryListenerReportedDevices = new Hashtable();
    private static final int ATTR_RETRIEVABLE_MAX = 65535;
    private static final int RECEIVE_MTU_MAX = 1024;

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isNativeCodeLoaded();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int getLibraryVersion();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int detectBluetoothStack();

    @Override // com.intel.bluetooth.BluetoothStack
    public native void enableNativeDebug(Class cls, boolean z2);

    private native boolean initializeImpl() throws BluetoothStateException;

    private native void destroyImpl();

    @Override // com.intel.bluetooth.BluetoothStack
    public native String getLocalDeviceBluetoothAddress() throws BluetoothStateException;

    private native int runDeviceInquiryImpl(DeviceInquiryThread deviceInquiryThread, int i2, DiscoveryListener discoveryListener) throws BluetoothStateException;

    private native boolean deviceInquiryCancelImpl();

    private native String getRemoteDeviceFriendlyNameImpl(long j2);

    private native short connectSDPImpl(long j2);

    private native void disconnectSDPImpl(short s2);

    private native long[] searchServicesImpl(SearchServicesThread searchServicesThread, short s2, byte[][] bArr);

    private native byte[] populateWorkerImpl(short s2, long j2, int[] iArr);

    private String getBTWVersionInfo() {
        return "";
    }

    private int getDeviceVersion() {
        return 0;
    }

    private int getDeviceManufacturer() {
        return 0;
    }

    BluetoothStackToshiba() {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getStackID() {
        return BlueCoveImpl.STACK_TOSHIBA;
    }

    public String toString() {
        return getStackID();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getFeatureSet() {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public BluetoothStack.LibraryInformation[] requireNativeLibraries() {
        return BluetoothStack.LibraryInformation.library("bluecove");
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void initialize() throws BluetoothStateException {
        if (!initializeImpl()) {
            throw new BluetoothStateException("TOSHIBA BluetoothStack not found");
        }
        this.initialized = true;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void destroy() {
        if (this.initialized) {
            destroyImpl();
            this.initialized = false;
            DebugLog.debug("TOSHIBA destroyed");
        }
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
    public void removeAuthenticationWithRemoteDevice(long address) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public DeviceClass getLocalDeviceClass() {
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void setLocalDeviceServiceClasses(int classOfDevice) {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceName() {
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isLocalDevicePowerOn() {
        return true;
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
            return String.valueOf(65535);
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
    public int getLocalDeviceDiscoverable() {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address, String passkey) throws IOException {
        return false;
    }

    public boolean isRemoteDeviceConnected(long address) {
        return true;
    }

    public String getRemoteDeviceLinkMode(long address) {
        return "";
    }

    public String getRemoteDeviceVersionInfo(long address) {
        return "";
    }

    public boolean setSniffMode(long address) {
        return false;
    }

    public boolean cancelSniffMode(long address) {
        return false;
    }

    public int getRemoteDeviceRSSI(long address) {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        this.deviceDiscoveryListeners.addElement(listener);
        if (BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_INQUIRY_REPORT_ASAP, false)) {
            this.deviceDiscoveryListenerFoundDevices.put(listener, new Hashtable());
        }
        this.deviceDiscoveryListenerReportedDevices.put(listener, new Vector());
        return DeviceInquiryThread.startInquiry(this, this, accessCode, listener);
    }

    @Override // com.intel.bluetooth.DeviceInquiryRunnable
    public int runDeviceInquiry(DeviceInquiryThread startedNotify, int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        Hashtable previouslyFound;
        try {
            int discType = runDeviceInquiryImpl(startedNotify, accessCode, listener);
            if (discType == 0 && (previouslyFound = (Hashtable) this.deviceDiscoveryListenerFoundDevices.get(listener)) != null) {
                Vector reported = (Vector) this.deviceDiscoveryListenerReportedDevices.get(listener);
                Enumeration en = previouslyFound.keys();
                while (en.hasMoreElements()) {
                    RemoteDevice remoteDevice = (RemoteDevice) en.nextElement2();
                    if (!reported.contains(remoteDevice)) {
                        reported.addElement(remoteDevice);
                        Integer deviceClassInt = (Integer) previouslyFound.get(remoteDevice);
                        DeviceClass deviceClass = new DeviceClass(deviceClassInt.intValue());
                        listener.deviceDiscovered(remoteDevice, deviceClass);
                        if (!this.deviceDiscoveryListeners.contains(listener)) {
                            return 5;
                        }
                    }
                }
            }
            this.deviceDiscoveryListeners.removeElement(listener);
            this.deviceDiscoveryListenerFoundDevices.remove(listener);
            this.deviceDiscoveryListenerReportedDevices.remove(listener);
            return discType;
        } finally {
            this.deviceDiscoveryListeners.removeElement(listener);
            this.deviceDiscoveryListenerFoundDevices.remove(listener);
            this.deviceDiscoveryListenerReportedDevices.remove(listener);
        }
    }

    @Override // com.intel.bluetooth.DeviceInquiryRunnable
    public void deviceDiscoveredCallback(DiscoveryListener listener, long deviceAddr, int deviceClass, String deviceName, boolean paired) {
        DebugLog.debug("deviceDiscoveredCallback deviceName", deviceName);
        if (!this.deviceDiscoveryListeners.contains(listener)) {
            return;
        }
        RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this, deviceAddr, deviceName, paired);
        Vector reported = (Vector) this.deviceDiscoveryListenerReportedDevices.get(listener);
        if (reported == null || reported.contains(remoteDevice)) {
            return;
        }
        Hashtable previouslyFound = (Hashtable) this.deviceDiscoveryListenerFoundDevices.get(listener);
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
        listener.deviceDiscovered(remoteDevice, cod);
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
        return getRemoteDeviceFriendlyNameImpl(address);
    }

    private boolean setAttributes(ServiceRecordImpl serviceRecord, int[] attrIDs, byte[] bytes) {
        boolean anyRetrived = false;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BluetoothStackWIDCOMMSDPInputStream btis = null;
        try {
            btis = new BluetoothStackWIDCOMMSDPInputStream(bais);
        } catch (Exception e2) {
        }
        for (int id : attrIDs) {
            try {
                DataElement element = btis.readElement();
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
            } catch (Throwable th) {
            }
        }
        return anyRetrived;
    }

    @Override // com.intel.bluetooth.SearchServicesRunnable
    public int runSearchServices(SearchServicesThread startedNotify, int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        try {
            short cid = connectSDPImpl(RemoteDeviceHelper.getAddress(device.getBluetoothAddress()));
            byte[][] uuidBytes = new byte[uuidSet.length][16];
            for (int i2 = 0; i2 < uuidSet.length; i2++) {
                uuidBytes[i2] = new byte[16];
                String full = uuidSet[i2].toString();
                for (int j2 = 0; j2 < 16; j2++) {
                    String sub = full.substring(j2 * 2, (j2 * 2) + 2).toUpperCase();
                    uuidBytes[i2][j2] = (byte) Integer.parseInt(sub, 16);
                }
            }
            try {
                long[] handles = searchServicesImpl(startedNotify, cid, uuidBytes);
                if (handles.length <= 0) {
                    disconnectSDPImpl(cid);
                    return 4;
                }
                ServiceRecordImpl[] records = new ServiceRecordImpl[handles.length];
                for (int i3 = 0; i3 < handles.length; i3++) {
                    records[i3] = new ServiceRecordImpl(this, device, handles[i3]);
                    try {
                        byte[] bytes = populateWorkerImpl(cid, handles[i3], attrSet);
                        if (bytes != null) {
                            setAttributes(records[i3], attrSet, bytes);
                        }
                    } catch (Exception e2) {
                        disconnectSDPImpl(cid);
                        return 3;
                    }
                }
                listener.servicesDiscovered(startedNotify.getTransID(), records);
                disconnectSDPImpl(cid);
                return 1;
            } catch (Exception e3) {
                disconnectSDPImpl(cid);
                return 3;
            }
        } catch (Exception e4) {
            return 6;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        return SearchServicesThread.startSearchServices(this, this, attrSet, uuidSet, device, listener);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelServiceSearch(int transID) {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecord, int[] attrIDs) throws IOException {
        if (attrIDs.length > 65535) {
            throw new IllegalArgumentException();
        }
        try {
            short cid = connectSDPImpl(RemoteDeviceHelper.getAddress(serviceRecord.getHostDevice().getBluetoothAddress()));
            try {
                byte[] bytes = populateWorkerImpl(cid, serviceRecord.getHandle(), attrIDs);
                if (bytes == null) {
                    return false;
                }
                boolean ret = setAttributes(serviceRecord, attrIDs, bytes);
                disconnectSDPImpl(cid);
                return ret;
            } catch (Exception e2) {
                disconnectSDPImpl(cid);
                return false;
            }
        } catch (Exception e3) {
            return false;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long connectionRfOpenClientConnection(BluetoothConnectionParams params) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseClientConnection(long handle) throws IOException {
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
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerAcceptAndOpenRfServerConnection(long handle) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseServerConnection(long handle) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfFlush(long handle) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfRead(long handle) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfRead(long handle, byte[] b2, int off, int len) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfReadAvailable(long handle) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, int b2) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, byte[] b2, int off, int len) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long getConnectionRfRemoteAddress(long handle) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2OpenClientConnection(BluetoothConnectionParams params, int receiveMTU, int transmitMTU) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2CloseClientConnection(long handle) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerOpen(BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU, ServiceRecordImpl serviceRecord) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerAcceptAndOpenServerConnection(long handle) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2CloseServerConnection(long handle) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetSecurityOpt(long handle, int expected) throws IOException {
        return expected;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Ready(long handle) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2Receive(long handle, byte[] inBuf) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2Send(long handle, byte[] data, int transmitMTU) throws IOException {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetReceiveMTU(long handle) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int l2GetTransmitMTU(long handle) throws IOException {
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2RemoteAddress(long handle) throws IOException {
        return 0L;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Encrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }
}
