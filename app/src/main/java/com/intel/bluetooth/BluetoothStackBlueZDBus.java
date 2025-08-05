package com.intel.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import cx.ath.matthew.unix.UnixSocket;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.bluez.BlueZAPI;
import org.bluez.BlueZAPIFactory;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.exceptions.DBusExecutionException;
import org.icepdf.core.util.PdfOps;

/* loaded from: bluecove-bluez-2.1.1.jar:com/intel/bluetooth/BluetoothStackBlueZDBus.class */
class BluetoothStackBlueZDBus implements BluetoothStack, DeviceInquiryRunnable, SearchServicesRunnable {
    public static final String NATIVE_BLUECOVE_LIB_BLUEZ = "bluecovez";
    private static final String BLUEZ_DEVICEID_PREFIX = "hci";
    private static final int LISTEN_BACKLOG_RFCOMM = 4;
    private static final int LISTEN_BACKLOG_L2CAP = 4;
    private static final Vector<String> devicesUsed = new Vector<>();
    private String deviceID;
    private BlueZAPI blueZ;
    static final int BLUECOVE_DBUS_VERSION = 2010100;
    private Map<String, String> propertiesMap;
    private DiscoveryListener discoveryListener;
    private final int l2cap_receiveMTU_max = 65535;
    private DBusConnection dbusConn = null;
    private long localDeviceBTAddress = -1;
    private boolean deviceInquiryCanceled = false;

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isNativeCodeLoaded();

    private native int getLibraryVersionNative();

    @Override // com.intel.bluetooth.BluetoothStack
    public native void enableNativeDebug(Class cls, boolean z2);

    private native long connectionRfOpenClientConnectionImpl(long j2, long j3, int i2, boolean z2, boolean z3, int i3) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void connectionRfCloseClientConnection(long j2) throws IOException;

    public native int rfGetSecurityOptImpl(long j2) throws IOException;

    private native long rfServerOpenImpl(long j2, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i2) throws IOException;

    private native int rfServerGetChannelIDImpl(long j2) throws IOException;

    private native void rfServerCloseImpl(long j2, boolean z2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long rfServerAcceptAndOpenRfServerConnection(long j2) throws IOException;

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

    @Override // com.intel.bluetooth.BluetoothStack
    public native long getConnectionRfRemoteAddress(long j2) throws IOException;

    private native long l2OpenClientConnectionImpl(long j2, long j3, int i2, boolean z2, boolean z3, int i3, int i4, int i5) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native void l2CloseClientConnection(long j2) throws IOException;

    private native long l2ServerOpenImpl(long j2, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, int i2, int i3, int i4, int i5) throws IOException;

    public native int l2ServerGetPSMImpl(long j2) throws IOException;

    @Override // com.intel.bluetooth.BluetoothStack
    public native long l2ServerAcceptAndOpenServerConnection(long j2) throws IOException;

    private native void l2ServerCloseImpl(long j2, boolean z2) throws IOException;

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

    @Override // com.intel.bluetooth.BluetoothStack
    public native int l2GetSecurityOpt(long j2, int i2) throws IOException;

    /* loaded from: bluecove-bluez-2.1.1.jar:com/intel/bluetooth/BluetoothStackBlueZDBus$DiscoveryData.class */
    private class DiscoveryData {
        public int deviceClass;
        public String name;
        boolean paired;

        private DiscoveryData() {
        }
    }

    BluetoothStackBlueZDBus() {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getStackID() {
        return BlueCoveImpl.STACK_BLUEZ_DBUS;
    }

    public String toString() {
        if (this.deviceID != null) {
            return getStackID() + CallSiteDescriptor.TOKEN_DELIMITER + this.deviceID;
        }
        return getStackID();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public BluetoothStack.LibraryInformation[] requireNativeLibraries() {
        try {
            BluetoothStack.LibraryInformation unixSocketLib = new BluetoothStack.LibraryInformation("unix-java", false);
            unixSocketLib.stackClass = UnixSocket.class;
            return new BluetoothStack.LibraryInformation[]{new BluetoothStack.LibraryInformation(NATIVE_BLUECOVE_LIB_BLUEZ), unixSocketLib};
        } catch (NoClassDefFoundError e2) {
            return new BluetoothStack.LibraryInformation[]{new BluetoothStack.LibraryInformation(NATIVE_BLUECOVE_LIB_BLUEZ)};
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getLibraryVersion() throws BluetoothStateException {
        int version = getLibraryVersionNative();
        if (version != 2010100) {
            DebugLog.fatal("BlueCove native library version mismatch " + version + " expected 2010100");
            throw new BluetoothStateException("BlueCove native library version mismatch");
        }
        return version;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int detectBluetoothStack() {
        return 32;
    }

    private String toHexString(long l2) {
        StringBuffer buf = new StringBuffer();
        String lo = Integer.toHexString((int) l2);
        if (l2 > 4294967295L) {
            String hi = Integer.toHexString((int) (l2 >> 32));
            buf.append(hi);
        }
        buf.append(lo);
        StringBuffer result = new StringBuffer();
        int prependZeros = 12 - buf.length();
        for (int i2 = 0; i2 < prependZeros; i2++) {
            result.append("0");
        }
        result.append(buf.toString());
        StringBuffer hex = new StringBuffer();
        for (int i3 = 0; i3 < 12; i3 += 2) {
            hex.append(result.substring(i3, i3 + 2));
            if (i3 < 10) {
                hex.append(CallSiteDescriptor.TOKEN_DELIMITER);
            }
        }
        return hex.toString().toUpperCase(Locale.ENGLISH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long convertBTAddress(String anAddress) throws NumberFormatException {
        long btAddress = Long.parseLong(anAddress.replaceAll(CallSiteDescriptor.TOKEN_DELIMITER, ""), 16);
        return btAddress;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void initialize() throws BluetoothStateException {
        Path adapterPath;
        try {
            try {
                this.dbusConn = DBusConnection.getConnection(0);
                try {
                    this.blueZ = BlueZAPIFactory.getBlueZAPI(this.dbusConn);
                    String findID = BlueCoveImpl.getConfigProperty("bluecove.deviceID");
                    String deviceAddressStr = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_LOCAL_DEVICE_ADDRESS);
                    if (findID != null) {
                        if (findID.startsWith(BLUEZ_DEVICEID_PREFIX)) {
                            adapterPath = this.blueZ.findAdapter(findID);
                            if (adapterPath == null) {
                                throw new BluetoothStateException("Can't find '" + findID + "' adapter");
                            }
                        } else {
                            int findNumber = Integer.parseInt(findID);
                            adapterPath = this.blueZ.getAdapter(findNumber);
                            if (adapterPath == null) {
                                throw new BluetoothStateException("Can't find adapter #" + findID);
                            }
                        }
                    } else if (deviceAddressStr != null) {
                        String pattern = toHexString(Long.parseLong(deviceAddressStr, 16));
                        adapterPath = this.blueZ.findAdapter(pattern);
                        if (adapterPath == null) {
                            throw new BluetoothStateException("Can't find adapter with address '" + deviceAddressStr + PdfOps.SINGLE_QUOTE_TOKEN);
                        }
                    } else {
                        adapterPath = this.blueZ.defaultAdapter();
                        if (adapterPath == null) {
                            throw new BluetoothStateException("Can't find default adapter");
                        }
                    }
                    try {
                        this.blueZ.selectAdapter(adapterPath);
                        this.localDeviceBTAddress = convertBTAddress(this.blueZ.getAdapterAddress());
                        this.deviceID = this.blueZ.getAdapterID();
                        if (devicesUsed.contains(this.deviceID)) {
                            throw new BluetoothStateException("LocalDevice " + this.deviceID + " alredy in use");
                        }
                        this.propertiesMap = new TreeMap();
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_DEVICES_MAX, "7");
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_SD_TRANS_MAX, "7");
                        this.propertiesMap.put("bluecove.deviceID", this.deviceID);
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY_SCAN, "true");
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE_SCAN, "true");
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY, "true");
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE, "true");
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_SD_ATTR_RETRIEVABLE_MAX, String.valueOf(256));
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_MASTER_SWITCH, "false");
                        this.propertiesMap.put(BluetoothConsts.PROPERTY_BLUETOOTH_L2CAP_RECEIVEMTU_MAX, String.valueOf(65535));
                        if (1 == 0) {
                            if (this.dbusConn != null) {
                                this.dbusConn.disconnect();
                            }
                            this.dbusConn = null;
                        }
                    } catch (DBusException e2) {
                        throw new BluetoothStateException(((Object) adapterPath) + " " + e2.getMessage());
                    }
                } catch (DBusException e3) {
                    DebugLog.error("Failed to get bluez dbus manager", (Throwable) e3);
                    throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException("Can't access BlueZ D-Bus"), e3));
                }
            } catch (DBusException e4) {
                DebugLog.error("Failed to get the dbus connection", (Throwable) e4);
                throw new BluetoothStateException(e4.getMessage());
            }
        } catch (Throwable th) {
            if (0 == 0) {
                if (this.dbusConn != null) {
                    this.dbusConn.disconnect();
                }
                this.dbusConn = null;
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void destroy() {
        DebugLog.debug("destroy()");
        if (this.deviceID != null) {
            devicesUsed.removeElement(this.deviceID);
            this.deviceID = null;
        }
        if (this.dbusConn != null) {
            this.dbusConn.disconnect();
            this.dbusConn = null;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isCurrentThreadInterruptedCallback() {
        return Thread.interrupted();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getFeatureSet() {
        return 3;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceBluetoothAddress() throws BluetoothStateException {
        return RemoteDeviceHelper.getBluetoothAddress(this.localDeviceBTAddress);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public DeviceClass getLocalDeviceClass() {
        try {
            int record = this.blueZ.getAdapterDeviceClass();
            if (10390272 == getLocalDeviceDiscoverable()) {
                record |= 8192;
            }
            return new DeviceClass(record);
        } catch (DBusExecutionException e2) {
            DebugLog.error("getLocalDeviceClass", (Throwable) e2);
            return null;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceName() {
        return this.blueZ.getAdapterName();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isLocalDevicePowerOn() {
        return this.blueZ.isAdapterPowerOn();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceProperty(String property) {
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_DEVICES_LIST.equals(property)) {
            StringBuffer b2 = new StringBuffer();
            for (String adapterId : this.blueZ.listAdapters()) {
                if (b2.length() > 0) {
                    b2.append(',');
                }
                b2.append(adapterId);
            }
            return b2.toString();
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_VERSION.equals(property)) {
            return this.blueZ.getAdapterVersion() + "; HCI " + this.blueZ.getAdapterRevision();
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_MANUFACTURER.equals(property)) {
            return this.blueZ.getAdapterManufacturer();
        }
        return this.propertiesMap.get(property);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getLocalDeviceDiscoverable() {
        if (this.blueZ.isAdapterDiscoverable()) {
            int timeout = this.blueZ.getAdapterDiscoverableTimeout();
            if (timeout == 0) {
                return 10390323;
            }
            return 10390272;
        }
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
        if (getLocalDeviceDiscoverable() == mode) {
            return true;
        }
        try {
            return this.blueZ.setAdapterDiscoverable(mode);
        } catch (DBusException e2) {
            throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException(e2.getMessage()), e2));
        } catch (DBusExecutionException e3) {
            throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException(e3.getMessage()), e3));
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void setLocalDeviceServiceClasses(int classOfDevice) {
        DebugLog.debug("setLocalDeviceServiceClasses()");
        throw new NotSupportedRuntimeException(getStackID());
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address) throws IOException {
        try {
            this.blueZ.authenticateRemoteDevice(toHexString(address));
            return true;
        } catch (Throwable e2) {
            DebugLog.error("Error creating bonding", e2);
            return false;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address, String passkey) throws IOException {
        try {
            return this.blueZ.authenticateRemoteDevice(toHexString(address), passkey);
        } catch (Throwable e2) {
            throw ((IOException) UtilsJavaSE.initCause(new IOException(e2.getMessage()), e2));
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void removeAuthenticationWithRemoteDevice(long address) throws IOException {
        try {
            this.blueZ.removeAuthenticationWithRemoteDevice(toHexString(address));
        } catch (Throwable e2) {
            throw ((IOException) UtilsJavaSE.initCause(new IOException(e2.getMessage()), e2));
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        DebugLog.debug("startInquiry()");
        if (this.discoveryListener != null) {
            throw new BluetoothStateException("Another inquiry already running");
        }
        this.discoveryListener = listener;
        this.deviceInquiryCanceled = false;
        return DeviceInquiryThread.startInquiry(this, this, accessCode, listener);
    }

    @Override // com.intel.bluetooth.DeviceInquiryRunnable
    public int runDeviceInquiry(final DeviceInquiryThread startedNotify, int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        DebugLog.debug("runDeviceInquiry()");
        try {
            final Map<Long, DiscoveryData> address2DiscoveryData = new HashMap<>();
            BlueZAPI.DeviceInquiryListener bluezDiscoveryListener = new BlueZAPI.DeviceInquiryListener() { // from class: com.intel.bluetooth.BluetoothStackBlueZDBus.1
                @Override // org.bluez.BlueZAPI.DeviceInquiryListener
                public void deviceInquiryStarted() {
                    startedNotify.deviceInquiryStartedCallback();
                }

                @Override // org.bluez.BlueZAPI.DeviceInquiryListener
                public void deviceDiscovered(String deviceAddr, String deviceName, int deviceClass, boolean paired) throws NumberFormatException {
                    long longAddress = BluetoothStackBlueZDBus.this.convertBTAddress(deviceAddr);
                    DiscoveryData discoveryData = (DiscoveryData) address2DiscoveryData.get(Long.valueOf(longAddress));
                    if (discoveryData == null) {
                        discoveryData = new DiscoveryData();
                        address2DiscoveryData.put(Long.valueOf(longAddress), discoveryData);
                    }
                    if (deviceName != null) {
                        discoveryData.name = deviceName;
                    }
                    if (deviceClass >= 0) {
                        discoveryData.deviceClass = deviceClass;
                    }
                }
            };
            try {
                this.blueZ.deviceInquiry(bluezDiscoveryListener);
                for (Long address : address2DiscoveryData.keySet()) {
                    DiscoveryData discoveryData = address2DiscoveryData.get(address);
                    if (discoveryData.name == null) {
                        try {
                            discoveryData.name = this.blueZ.getRemoteDeviceFriendlyName(toHexString(address.longValue()));
                        } catch (Throwable e2) {
                            DebugLog.error("can't get device name", e2);
                        }
                        if (discoveryData.name == null) {
                            discoveryData.name = "";
                        }
                    }
                    RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this, address.longValue(), discoveryData.name, discoveryData.paired);
                    listener.deviceDiscovered(remoteDevice, new DeviceClass(discoveryData.deviceClass));
                    if (this.deviceInquiryCanceled) {
                        break;
                    }
                }
                if (this.deviceInquiryCanceled) {
                    this.discoveryListener = null;
                    return 5;
                }
                this.discoveryListener = null;
                return 0;
            } catch (Throwable e3) {
                if (!this.deviceInquiryCanceled) {
                    DebugLog.error("deviceInquiry error", e3);
                    throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException(e3.getMessage()), e3));
                }
                this.discoveryListener = null;
                return 5;
            }
        } catch (Throwable th) {
            this.discoveryListener = null;
            throw th;
        }
    }

    @Override // com.intel.bluetooth.DeviceInquiryRunnable
    public void deviceDiscoveredCallback(DiscoveryListener listener, long deviceAddr, int deviceClass, String deviceName, boolean paired) {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelInquiry(DiscoveryListener listener) {
        DebugLog.debug("cancelInquiry()");
        if (this.discoveryListener != null && this.discoveryListener == listener) {
            this.deviceInquiryCanceled = true;
            try {
                this.blueZ.deviceInquiryCancel();
                return true;
            } catch (Throwable th) {
                return false;
            }
        }
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getRemoteDeviceFriendlyName(long deviceAddress) throws IOException {
        if (this.discoveryListener != null) {
            throw new IOException("DeviceInquiry alredy running");
        }
        try {
            return this.blueZ.getRemoteDeviceFriendlyName(toHexString(deviceAddress));
        } catch (DBusExecutionException e2) {
            throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException(e2.getMessage()), e2));
        } catch (DBusException e3) {
            throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException(e3.getMessage()), e3));
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public RemoteDevice[] retrieveDevices(int option) {
        List<String> preKnownDevices = this.blueZ.retrieveDevices(1 == option);
        if (preKnownDevices == null) {
            return null;
        }
        Vector<RemoteDevice> devices = new Vector<>();
        for (String addres : preKnownDevices) {
            devices.add(RemoteDeviceHelper.createRemoteDevice(this, convertBTAddress(addres), null, true));
        }
        return RemoteDeviceHelper.remoteDeviceListToArray(devices);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceTrusted(long address) {
        try {
            return this.blueZ.isRemoteDeviceTrusted(toHexString(address));
        } catch (DBusExecutionException e2) {
            DebugLog.error("isRemoteDeviceTrusted", (Throwable) e2);
            return Boolean.FALSE;
        } catch (DBusException e3) {
            DebugLog.error("isRemoteDeviceTrusted", (Throwable) e3);
            return Boolean.FALSE;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceAuthenticated(long address) {
        try {
            return Boolean.valueOf(this.blueZ.isRemoteDeviceConnected(toHexString(address)) && this.blueZ.isRemoteDeviceTrusted(toHexString(address)).booleanValue());
        } catch (DBusExecutionException e2) {
            DebugLog.error("isRemoteDeviceAuthenticated", (Throwable) e2);
            return Boolean.FALSE;
        } catch (DBusException e3) {
            DebugLog.error("isRemoteDeviceAuthenticated", (Throwable) e3);
            return false;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        try {
            DebugLog.debug("searchServices() device", device.getBluetoothAddress());
            return SearchServicesThread.startSearchServices(this, this, attrSet, uuidSet, device, listener);
        } catch (Exception ex) {
            DebugLog.debug("searchServices() failed", (Throwable) ex);
            throw new BluetoothStateException("searchServices() failed: " + ex.getMessage());
        }
    }

    private int getRemoteServices(SearchServicesThread sst, UUID[] uuidSet, RemoteDevice remoteDevice) {
        try {
            Map<Integer, String> xmlRecords = this.blueZ.getRemoteDeviceServices(toHexString(RemoteDeviceHelper.getAddress(remoteDevice)));
            if (xmlRecords == null) {
                return 6;
            }
            for (Map.Entry<Integer, String> record : xmlRecords.entrySet()) {
                DebugLog.debug("pars service record", record.getValue());
                ServiceRecordImpl sr = new ServiceRecordImpl(this, remoteDevice, record.getKey().intValue());
                try {
                    Map<Integer, DataElement> elements = BlueZServiceRecordXML.parsXMLRecord(record.getValue());
                    for (Map.Entry<Integer, DataElement> element : elements.entrySet()) {
                        sr.populateAttributeValue(element.getKey().intValue(), element.getValue());
                    }
                    int u2 = 0;
                    while (true) {
                        if (u2 < uuidSet.length) {
                            if (sr.hasServiceClassUUID(uuidSet[u2]) || sr.hasProtocolClassUUID(uuidSet[u2])) {
                                u2++;
                            } else {
                                DebugLog.debug("ignoring service", sr);
                                break;
                            }
                        } else {
                            DebugLog.debug("found service");
                            sst.addServicesRecords(sr);
                            break;
                        }
                    }
                } catch (IOException e2) {
                    DebugLog.error("Error parsing service record", e2);
                }
            }
            return 1;
        } catch (DBusException e3) {
            DebugLog.error("get Service records failed", (Throwable) e3);
            return 3;
        } catch (DBusExecutionException e4) {
            DebugLog.error("get Service records failed", (Throwable) e4);
            return 3;
        }
    }

    @Override // com.intel.bluetooth.SearchServicesRunnable
    public int runSearchServices(SearchServicesThread sst, int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        DebugLog.debug("runSearchServices()");
        sst.searchServicesStartedCallback();
        int respCode = getRemoteServices(sst, uuidSet, device);
        DebugLog.debug("SearchServices finished", sst.getTransID());
        Vector<ServiceRecord> records = sst.getServicesRecords();
        if (records.size() != 0) {
            ServiceRecord[] servRecordArray = (ServiceRecord[]) Utils.vector2toArray(records, new ServiceRecord[records.size()]);
            listener.servicesDiscovered(sst.getTransID(), servRecordArray);
        }
        if (respCode != 3 && sst.isTerminated()) {
            return 2;
        }
        if (respCode == 1) {
            if (records.size() != 0) {
                return 1;
            }
            return 4;
        }
        return respCode;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelServiceSearch(int transID) {
        DebugLog.debug("cancelServiceSearch()");
        SearchServicesThread sst = SearchServicesThread.getServiceSearchThread(transID);
        if (sst != null) {
            return sst.setTerminated();
        }
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecord, int[] attrIDs) throws IOException {
        DebugLog.debug("populateServicesRecordAttributeValues()");
        RemoteDeviceHelper.getAddress(serviceRecord.getHostDevice());
        throw new UnsupportedOperationException("populateServicesRecordAttributeValues Not supported yet.");
    }

    private synchronized void registerSDPRecord(ServiceRecordImpl serviceRecord) throws ServiceRegistrationException {
        try {
            long handle = this.blueZ.registerSDPRecord(BlueZServiceRecordXML.exportXMLRecord(serviceRecord));
            serviceRecord.setHandle(handle);
            serviceRecord.populateAttributeValue(0, new DataElement(10, handle));
        } catch (Throwable e2) {
            throw ((ServiceRegistrationException) UtilsJavaSE.initCause(new ServiceRegistrationException(e2.getMessage()), e2));
        }
    }

    private synchronized void updateSDPRecord(ServiceRecordImpl serviceRecord) throws ServiceRegistrationException {
        try {
            this.blueZ.updateSDPRecord(serviceRecord.getHandle(), BlueZServiceRecordXML.exportXMLRecord(serviceRecord));
        } catch (Throwable e2) {
            throw ((ServiceRegistrationException) UtilsJavaSE.initCause(new ServiceRegistrationException(e2.getMessage()), e2));
        }
    }

    private synchronized void unregisterSDPRecord(ServiceRecordImpl serviceRecord) throws ServiceRegistrationException {
        try {
            this.blueZ.unregisterSDPRecord(serviceRecord.getHandle());
        } catch (Throwable e2) {
            throw ((ServiceRegistrationException) UtilsJavaSE.initCause(new ServiceRegistrationException(e2.getMessage()), e2));
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long connectionRfOpenClientConnection(BluetoothConnectionParams params) throws IOException {
        return connectionRfOpenClientConnectionImpl(this.localDeviceBTAddress, params.address, params.channel, params.authenticate, params.encrypt, params.timeout);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int rfGetSecurityOpt(long handle, int expected) throws IOException {
        return rfGetSecurityOptImpl(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean rfEncrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord) throws IOException {
        long socket = rfServerOpenImpl(this.localDeviceBTAddress, params.authorize, params.authenticate, params.encrypt, params.master, params.timeouts, 4);
        boolean success = false;
        try {
            int channel = rfServerGetChannelIDImpl(socket);
            serviceRecord.populateRFCOMMAttributes(0L, channel, params.uuid, params.name, params.obex);
            registerSDPRecord(serviceRecord);
            success = true;
            if (1 == 0) {
                rfServerCloseImpl(socket, true);
            }
            return socket;
        } catch (Throwable th) {
            if (!success) {
                rfServerCloseImpl(socket, true);
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        try {
            unregisterSDPRecord(serviceRecord);
            rfServerCloseImpl(handle, false);
        } catch (Throwable th) {
            rfServerCloseImpl(handle, false);
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        updateSDPRecord(serviceRecord);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseServerConnection(long clientHandle) throws IOException {
        connectionRfCloseClientConnection(clientHandle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfRead(long handle) throws IOException {
        byte[] data = new byte[1];
        int size = connectionRfRead(handle, data, 0, 1);
        if (size == -1) {
            return -1;
        }
        return 255 & data[0];
    }

    private void validateMTU(int receiveMTU, int transmitMTU) {
        if (receiveMTU > 65535) {
            throw new IllegalArgumentException("invalid ReceiveMTU value " + receiveMTU);
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2OpenClientConnection(BluetoothConnectionParams params, int receiveMTU, int transmitMTU) throws IOException {
        validateMTU(receiveMTU, transmitMTU);
        return l2OpenClientConnectionImpl(this.localDeviceBTAddress, params.address, params.channel, params.authenticate, params.encrypt, receiveMTU, transmitMTU, params.timeout);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long l2ServerOpen(BluetoothConnectionNotifierParams params, int receiveMTU, int transmitMTU, ServiceRecordImpl serviceRecord) throws IOException {
        validateMTU(receiveMTU, transmitMTU);
        long socket = l2ServerOpenImpl(this.localDeviceBTAddress, params.authorize, params.authenticate, params.encrypt, params.master, params.timeouts, 4, receiveMTU, transmitMTU, params.bluecove_ext_psm);
        boolean success = false;
        try {
            int channel = l2ServerGetPSMImpl(socket);
            serviceRecord.populateL2CAPAttributes(0, channel, params.uuid, params.name);
            registerSDPRecord(serviceRecord);
            success = true;
            if (1 == 0) {
                l2ServerCloseImpl(socket, true);
            }
            return socket;
        } catch (Throwable th) {
            if (!success) {
                l2ServerCloseImpl(socket, true);
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        updateSDPRecord(serviceRecord);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2CloseServerConnection(long handle) throws IOException {
        l2CloseClientConnection(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void l2ServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        try {
            unregisterSDPRecord(serviceRecord);
            l2ServerCloseImpl(handle, false);
        } catch (Throwable th) {
            l2ServerCloseImpl(handle, false);
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean l2Encrypt(long address, long handle, boolean on) throws IOException {
        return false;
    }
}
