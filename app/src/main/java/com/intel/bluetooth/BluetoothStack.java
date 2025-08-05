package com.intel.bluetooth;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStack.class */
public interface BluetoothStack {
    public static final int FEATURE_L2CAP = 1;
    public static final int FEATURE_SERVICE_ATTRIBUTES = 2;
    public static final int FEATURE_SET_DEVICE_SERVICE_CLASSES = 4;
    public static final int FEATURE_RSSI = 8;

    boolean isNativeCodeLoaded();

    LibraryInformation[] requireNativeLibraries();

    int getLibraryVersion() throws BluetoothStateException;

    int detectBluetoothStack();

    void enableNativeDebug(Class cls, boolean z2);

    void initialize() throws BluetoothStateException;

    void destroy();

    String getStackID();

    boolean isCurrentThreadInterruptedCallback();

    int getFeatureSet();

    String getLocalDeviceBluetoothAddress() throws BluetoothStateException;

    String getLocalDeviceName();

    DeviceClass getLocalDeviceClass();

    void setLocalDeviceServiceClasses(int i2);

    boolean setLocalDeviceDiscoverable(int i2) throws BluetoothStateException;

    int getLocalDeviceDiscoverable();

    boolean isLocalDevicePowerOn();

    String getLocalDeviceProperty(String str);

    boolean authenticateRemoteDevice(long j2) throws IOException;

    boolean authenticateRemoteDevice(long j2, String str) throws IOException;

    void removeAuthenticationWithRemoteDevice(long j2) throws IOException;

    boolean startInquiry(int i2, DiscoveryListener discoveryListener) throws BluetoothStateException;

    boolean cancelInquiry(DiscoveryListener discoveryListener);

    String getRemoteDeviceFriendlyName(long j2) throws IOException;

    RemoteDevice[] retrieveDevices(int i2);

    Boolean isRemoteDeviceTrusted(long j2);

    Boolean isRemoteDeviceAuthenticated(long j2);

    int searchServices(int[] iArr, UUID[] uuidArr, RemoteDevice remoteDevice, DiscoveryListener discoveryListener) throws BluetoothStateException;

    boolean cancelServiceSearch(int i2);

    boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecordImpl, int[] iArr) throws IOException;

    long connectionRfOpenClientConnection(BluetoothConnectionParams bluetoothConnectionParams) throws IOException;

    int rfGetSecurityOpt(long j2, int i2) throws IOException;

    void connectionRfCloseClientConnection(long j2) throws IOException;

    void connectionRfCloseServerConnection(long j2) throws IOException;

    long rfServerOpen(BluetoothConnectionNotifierParams bluetoothConnectionNotifierParams, ServiceRecordImpl serviceRecordImpl) throws IOException;

    void rfServerUpdateServiceRecord(long j2, ServiceRecordImpl serviceRecordImpl, boolean z2) throws ServiceRegistrationException;

    long rfServerAcceptAndOpenRfServerConnection(long j2) throws IOException;

    void rfServerClose(long j2, ServiceRecordImpl serviceRecordImpl) throws IOException;

    long getConnectionRfRemoteAddress(long j2) throws IOException;

    boolean rfEncrypt(long j2, long j3, boolean z2) throws IOException;

    int connectionRfRead(long j2) throws IOException;

    int connectionRfRead(long j2, byte[] bArr, int i2, int i3) throws IOException;

    int connectionRfReadAvailable(long j2) throws IOException;

    void connectionRfWrite(long j2, int i2) throws IOException;

    void connectionRfWrite(long j2, byte[] bArr, int i2, int i3) throws IOException;

    void connectionRfFlush(long j2) throws IOException;

    long l2OpenClientConnection(BluetoothConnectionParams bluetoothConnectionParams, int i2, int i3) throws IOException;

    void l2CloseClientConnection(long j2) throws IOException;

    long l2ServerOpen(BluetoothConnectionNotifierParams bluetoothConnectionNotifierParams, int i2, int i3, ServiceRecordImpl serviceRecordImpl) throws IOException;

    void l2ServerUpdateServiceRecord(long j2, ServiceRecordImpl serviceRecordImpl, boolean z2) throws ServiceRegistrationException;

    long l2ServerAcceptAndOpenServerConnection(long j2) throws IOException;

    void l2CloseServerConnection(long j2) throws IOException;

    void l2ServerClose(long j2, ServiceRecordImpl serviceRecordImpl) throws IOException;

    int l2GetSecurityOpt(long j2, int i2) throws IOException;

    int l2GetTransmitMTU(long j2) throws IOException;

    int l2GetReceiveMTU(long j2) throws IOException;

    boolean l2Ready(long j2) throws IOException;

    int l2Receive(long j2, byte[] bArr) throws IOException;

    void l2Send(long j2, byte[] bArr, int i2) throws IOException;

    long l2RemoteAddress(long j2) throws IOException;

    boolean l2Encrypt(long j2, long j3, boolean z2) throws IOException;

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStack$LibraryInformation.class */
    public static class LibraryInformation {
        public final String libraryName;
        public Class stackClass;
        public final boolean required;

        public LibraryInformation(String libraryName) {
            this(libraryName, true);
        }

        public LibraryInformation(String libraryName, boolean required) {
            this.libraryName = libraryName;
            this.required = required;
        }

        public static LibraryInformation[] library(String libraryName) {
            return new LibraryInformation[]{new LibraryInformation(libraryName)};
        }
    }
}
