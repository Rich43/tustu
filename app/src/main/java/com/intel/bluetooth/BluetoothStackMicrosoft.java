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
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackMicrosoft.class */
class BluetoothStackMicrosoft implements BluetoothStack {
    private static final int BTH_MODE_POWER_OFF = 1;
    private static final int BTH_MODE_CONNECTABLE = 2;
    private static final int BTH_MODE_DISCOVERABLE = 3;
    private static BluetoothStackMicrosoft singleInstance = null;
    private boolean windowsCE;
    private DiscoveryListener currentDeviceDiscoveryListener;
    private Thread limitedDiscoverableTimer;
    private static final int ATTR_RETRIEVABLE_MAX = 256;
    private Hashtable deviceDiscoveryDevices;
    private static int connectThreadNumber;
    private boolean peerInitialized = false;
    private long localBluetoothAddress = 0;

    @Override // com.intel.bluetooth.BluetoothStack
    public native boolean isNativeCodeLoaded();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int getLibraryVersion();

    @Override // com.intel.bluetooth.BluetoothStack
    public native int detectBluetoothStack();

    @Override // com.intel.bluetooth.BluetoothStack
    public native void enableNativeDebug(Class cls, boolean z2);

    private static native int initializationStatus() throws IOException;

    private native void uninitialize();

    private native boolean isWindowsCE();

    private native int getDeviceClass(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void setDiscoverable(boolean z2) throws BluetoothStateException;

    private native int getBluetoothRadioMode();

    private native String getradioname(long j2);

    private native int getDeviceVersion(long j2);

    private native int getDeviceManufacturer(long j2);

    private native boolean retrieveDevicesImpl(int i2, RetrieveDevicesCallback retrieveDevicesCallback);

    private native boolean isRemoteDeviceTrustedImpl(long j2);

    private native boolean isRemoteDeviceAuthenticatedImpl(long j2);

    private native boolean authenticateRemoteDeviceImpl(long j2, String str) throws IOException;

    private native void removeAuthenticationWithRemoteDeviceImpl(long j2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native int runDeviceInquiryImpl(DeviceInquiryRunnable deviceInquiryRunnable, DeviceInquiryThread deviceInquiryThread, int i2, int i3, DiscoveryListener discoveryListener) throws BluetoothStateException;

    private native boolean cancelInquiry();

    /* JADX INFO: Access modifiers changed from: private */
    public native int[] runSearchServicesImpl(UUID[] uuidArr, long j2) throws SearchServicesException;

    public native byte[] getServiceAttributes(int[] iArr, long j2, int i2) throws IOException;

    private native long socket(boolean z2, boolean z3) throws IOException;

    private native long getsockaddress(long j2) throws IOException;

    private native void storesockopt(long j2);

    private native int getsockchannel(long j2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void connect(long j2, long j3, int i2, int i3) throws IOException;

    private native void bind(long j2) throws IOException;

    private native void listen(long j2) throws IOException;

    private native long accept(long j2) throws IOException;

    private native int recvAvailable(long j2) throws IOException;

    private native int recv(long j2) throws IOException;

    private native int recv(long j2, byte[] bArr, int i2, int i3) throws IOException;

    private native void send(long j2, int i2) throws IOException;

    private native void send(long j2, byte[] bArr, int i2, int i3) throws IOException;

    private native void close(long j2) throws IOException;

    private native String getpeername(long j2) throws IOException;

    private native long getpeeraddress(long j2) throws IOException;

    private native long registerService(byte[] bArr, int i2) throws ServiceRegistrationException;

    private native void unregisterService(long j2) throws ServiceRegistrationException;

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int nextConnectThreadNum() {
        int i2 = connectThreadNumber;
        connectThreadNumber = i2 + 1;
        return i2;
    }

    BluetoothStackMicrosoft() {
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getStackID() {
        return BlueCoveImpl.STACK_WINSOCK;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public BluetoothStack.LibraryInformation[] requireNativeLibraries() {
        return BluetoothStack.LibraryInformation.library("intelbth");
    }

    public String toString() {
        return getStackID();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void initialize() throws BluetoothStateException {
        if (singleInstance != null) {
            throw new BluetoothStateException(new StringBuffer().append("Only one instance of ").append(getStackID()).append(" stack supported").toString());
        }
        try {
            int status = initializationStatus();
            DebugLog.debug("initializationStatus", status);
            if (status == 1) {
                this.peerInitialized = true;
            }
            this.windowsCE = isWindowsCE();
            singleInstance = this;
        } catch (BluetoothStateException e2) {
            throw e2;
        } catch (IOException e3) {
            DebugLog.fatal("initialization", e3);
            throw new BluetoothStateException(e3.getMessage());
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void destroy() {
        if (singleInstance != this) {
            throw new RuntimeException("Destroy invalid instance");
        }
        if (this.peerInitialized) {
            this.peerInitialized = false;
            uninitialize();
        }
        cancelLimitedDiscoverableTimer();
        singleInstance = null;
    }

    private void initialized() throws BluetoothStateException {
        if (!this.peerInitialized) {
            throw new BluetoothStateException("Bluetooth system is unavailable");
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getFeatureSet() {
        return 2 | (this.windowsCE ? 0 : 4);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceBluetoothAddress() {
        try {
            long socket = socket(false, false);
            bind(socket);
            this.localBluetoothAddress = getsockaddress(socket);
            String address = RemoteDeviceHelper.getBluetoothAddress(this.localBluetoothAddress);
            storesockopt(socket);
            close(socket);
            return address;
        } catch (IOException e2) {
            DebugLog.error("get local bluetoothAddress", e2);
            return "000000000000";
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceName() {
        if (this.localBluetoothAddress == 0) {
            getLocalDeviceBluetoothAddress();
        }
        return getradioname(this.localBluetoothAddress);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getRemoteDeviceFriendlyName(long address) throws IOException {
        return getpeername(address);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public DeviceClass getLocalDeviceClass() {
        return new DeviceClass(getDeviceClass(this.localBluetoothAddress));
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void setLocalDeviceServiceClasses(int classOfDevice) {
    }

    private void cancelLimitedDiscoverableTimer() {
        if (this.limitedDiscoverableTimer != null) {
            this.limitedDiscoverableTimer.interrupt();
            this.limitedDiscoverableTimer = null;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean setLocalDeviceDiscoverable(int mode) throws BluetoothStateException {
        switch (mode) {
            case 0:
                cancelLimitedDiscoverableTimer();
                DebugLog.debug("setDiscoverable(false)");
                setDiscoverable(false);
                return 0 == getLocalDeviceDiscoverable();
            case 10390272:
                cancelLimitedDiscoverableTimer();
                DebugLog.debug("setDiscoverable(LIAC)");
                setDiscoverable(true);
                if (10390323 != getLocalDeviceDiscoverable()) {
                    return false;
                }
                this.limitedDiscoverableTimer = Utils.schedule(60000L, new Runnable(this) { // from class: com.intel.bluetooth.BluetoothStackMicrosoft.1
                    private final BluetoothStackMicrosoft this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            try {
                                this.this$0.setDiscoverable(false);
                                this.this$0.limitedDiscoverableTimer = null;
                            } catch (BluetoothStateException e2) {
                                DebugLog.debug("error setDiscoverable", (Throwable) e2);
                                this.this$0.limitedDiscoverableTimer = null;
                            }
                        } catch (Throwable th) {
                            this.this$0.limitedDiscoverableTimer = null;
                            throw th;
                        }
                    }
                });
                return true;
            case 10390323:
                cancelLimitedDiscoverableTimer();
                DebugLog.debug("setDiscoverable(true)");
                setDiscoverable(true);
                return 10390323 == getLocalDeviceDiscoverable();
            default:
                return false;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isLocalDevicePowerOn() {
        int mode = getBluetoothRadioMode();
        if (mode == 1) {
            return false;
        }
        return mode == 2 || mode == 3;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int getLocalDeviceDiscoverable() {
        int mode = getBluetoothRadioMode();
        if (mode == 3) {
            if (this.limitedDiscoverableTimer != null) {
                DebugLog.debug("Discoverable = LIAC");
                return 10390272;
            }
            DebugLog.debug("Discoverable = GIAC");
            return 10390323;
        }
        DebugLog.debug("Discoverable = NOT_DISCOVERABLE");
        return 0;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public String getLocalDeviceProperty(String property) {
        if (BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_DEVICES_MAX.equals(property) || BluetoothConsts.PROPERTY_BLUETOOTH_SD_TRANS_MAX.equals(property)) {
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
            return "0";
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_VERSION.equals(property)) {
            return String.valueOf(getDeviceVersion(this.localBluetoothAddress));
        }
        if (BlueCoveLocalDeviceProperties.LOCAL_DEVICE_RADIO_MANUFACTURER.equals(property)) {
            return String.valueOf(getDeviceManufacturer(this.localBluetoothAddress));
        }
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean isCurrentThreadInterruptedCallback() {
        return UtilsJavaSE.isCurrentThreadInterrupted();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public RemoteDevice[] retrieveDevices(int option) {
        if (this.windowsCE) {
            return null;
        }
        Vector devices = new Vector();
        RetrieveDevicesCallback retrieveDevicesCallback = new RetrieveDevicesCallback(this, devices) { // from class: com.intel.bluetooth.BluetoothStackMicrosoft.2
            private final Vector val$devices;
            private final BluetoothStackMicrosoft this$0;

            {
                this.this$0 = this;
                this.val$devices = devices;
            }

            @Override // com.intel.bluetooth.RetrieveDevicesCallback
            public void deviceFoundCallback(long deviceAddr, int deviceClass, String deviceName, boolean paired) {
                DebugLog.debug("device found", deviceAddr);
                RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this.this$0, deviceAddr, deviceName, paired);
                this.val$devices.add(remoteDevice);
            }
        };
        if (retrieveDevicesImpl(option, retrieveDevicesCallback)) {
            return RemoteDeviceHelper.remoteDeviceListToArray(devices);
        }
        return null;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceTrusted(long address) {
        if (this.windowsCE) {
            return null;
        }
        return new Boolean(isRemoteDeviceTrustedImpl(address));
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public Boolean isRemoteDeviceAuthenticated(long address) {
        if (this.windowsCE) {
            return null;
        }
        return new Boolean(isRemoteDeviceAuthenticatedImpl(address));
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean authenticateRemoteDevice(long address) throws IOException {
        return authenticateRemoteDeviceImpl(address, null);
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
        initialized();
        if (this.currentDeviceDiscoveryListener != null) {
            throw new BluetoothStateException("Another inquiry already running");
        }
        this.currentDeviceDiscoveryListener = listener;
        DeviceInquiryRunnable inquiryRunnable = new DeviceInquiryRunnable(this) { // from class: com.intel.bluetooth.BluetoothStackMicrosoft.3
            private final BluetoothStackMicrosoft this$0;

            {
                this.this$0 = this;
            }

            @Override // com.intel.bluetooth.DeviceInquiryRunnable
            public int runDeviceInquiry(DeviceInquiryThread inquiryThread, int accessCode2, DiscoveryListener listener2) throws BluetoothStateException {
                try {
                    this.this$0.deviceDiscoveryDevices = new Hashtable();
                    int discType = this.this$0.runDeviceInquiryImpl(this, inquiryThread, accessCode2, DeviceInquiryThread.getConfigDeviceInquiryDuration(), listener2);
                    if (discType == 0) {
                        Enumeration en = this.this$0.deviceDiscoveryDevices.keys();
                        while (en.hasMoreElements()) {
                            RemoteDevice remoteDevice = (RemoteDevice) en.nextElement2();
                            DeviceClass deviceClass = (DeviceClass) this.this$0.deviceDiscoveryDevices.get(remoteDevice);
                            listener2.deviceDiscovered(remoteDevice, deviceClass);
                            if (this.this$0.currentDeviceDiscoveryListener == null) {
                                return 5;
                            }
                        }
                    }
                    this.this$0.deviceDiscoveryDevices = null;
                    this.this$0.currentDeviceDiscoveryListener = null;
                    return discType;
                } finally {
                    this.this$0.deviceDiscoveryDevices = null;
                    this.this$0.currentDeviceDiscoveryListener = null;
                }
            }

            @Override // com.intel.bluetooth.DeviceInquiryRunnable
            public void deviceDiscoveredCallback(DiscoveryListener listener2, long deviceAddr, int deviceClass, String deviceName, boolean paired) {
                RemoteDevice remoteDevice = RemoteDeviceHelper.createRemoteDevice(this.this$0, deviceAddr, deviceName, paired);
                if (this.this$0.currentDeviceDiscoveryListener == null || this.this$0.deviceDiscoveryDevices == null || this.this$0.currentDeviceDiscoveryListener != listener2) {
                    return;
                }
                DeviceClass cod = new DeviceClass(deviceClass);
                DebugLog.debug("deviceDiscoveredCallback address", remoteDevice.getBluetoothAddress());
                DebugLog.debug("deviceDiscoveredCallback deviceClass", cod);
                this.this$0.deviceDiscoveryDevices.put(remoteDevice, cod);
            }
        };
        return DeviceInquiryThread.startInquiry(this, inquiryRunnable, accessCode, listener);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean cancelInquiry(DiscoveryListener listener) {
        if (this.currentDeviceDiscoveryListener != listener) {
            return false;
        }
        this.currentDeviceDiscoveryListener = null;
        return cancelInquiry();
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        SearchServicesRunnable searchRunnable = new SearchServicesRunnable(this) { // from class: com.intel.bluetooth.BluetoothStackMicrosoft.4
            private final BluetoothStackMicrosoft this$0;

            {
                this.this$0 = this;
            }

            @Override // com.intel.bluetooth.SearchServicesRunnable
            public int runSearchServices(SearchServicesThread sst, int[] attrSet2, UUID[] uuidSet2, RemoteDevice device2, DiscoveryListener listener2) throws BluetoothStateException {
                sst.searchServicesStartedCallback();
                try {
                    int[] handles = this.this$0.runSearchServicesImpl(uuidSet2, RemoteDeviceHelper.getAddress(device2));
                    if (handles == null) {
                        return 3;
                    }
                    if (handles.length > 0) {
                        ServiceRecord[] records = new ServiceRecordImpl[handles.length];
                        int[] requiredAttrIDs = {0, 1, 2, 3, 4};
                        boolean hasError = false;
                        for (int i2 = 0; i2 < handles.length; i2++) {
                            records[i2] = new ServiceRecordImpl(this.this$0, device2, handles[i2]);
                            try {
                                records[i2].populateRecord(requiredAttrIDs);
                                if (attrSet2 != null) {
                                    records[i2].populateRecord(attrSet2);
                                }
                            } catch (Exception e2) {
                                DebugLog.debug("populateRecord error", (Throwable) e2);
                                hasError = true;
                            }
                            if (sst.isTerminated()) {
                                return 2;
                            }
                        }
                        listener2.servicesDiscovered(sst.getTransID(), records);
                        if (hasError) {
                            return 3;
                        }
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
            return sst.setTerminated();
        }
        return false;
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public boolean populateServicesRecordAttributeValues(ServiceRecordImpl serviceRecord, int[] attrIDs) throws IOException {
        if (attrIDs.length > 256) {
            throw new IllegalArgumentException();
        }
        byte[] blob = getServiceAttributes(attrIDs, RemoteDeviceHelper.getAddress(serviceRecord.getHostDevice()), (int) serviceRecord.getHandle());
        if (blob.length > 0) {
            try {
                boolean anyRetrived = false;
                DataElement element = new SDPInputStream(new ByteArrayInputStream(blob)).readElement();
                Enumeration e2 = (Enumeration) element.getValue();
                while (e2.hasMoreElements()) {
                    int attrID = (int) ((DataElement) e2.nextElement2()).getLong();
                    serviceRecord.populateAttributeValue(attrID, (DataElement) e2.nextElement2());
                    if (!anyRetrived) {
                        int i2 = 0;
                        while (true) {
                            if (i2 >= attrIDs.length) {
                                break;
                            }
                            if (attrIDs[i2] != attrID) {
                                i2++;
                            } else {
                                anyRetrived = true;
                                break;
                            }
                        }
                    }
                }
                return anyRetrived;
            } catch (IOException e3) {
                throw e3;
            } catch (Throwable th) {
                throw new IOException();
            }
        }
        return false;
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothStackMicrosoft$ConnectThread.class */
    private class ConnectThread extends Thread {
        final Object event;
        final long socket;
        final BluetoothConnectionParams params;
        final int retryUnreachable;
        volatile IOException error;
        volatile boolean success;
        volatile boolean connecting;
        private final BluetoothStackMicrosoft this$0;

        ConnectThread(BluetoothStackMicrosoft bluetoothStackMicrosoft, Object event, long socket, BluetoothConnectionParams params) {
            super(new StringBuffer().append("ConnectThread-").append(BluetoothStackMicrosoft.nextConnectThreadNum()).toString());
            this.this$0 = bluetoothStackMicrosoft;
            this.success = false;
            this.connecting = true;
            this.event = event;
            this.socket = socket;
            this.params = params;
            this.retryUnreachable = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_CONNECT_UNREACHABLE_RETRY, 2);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                try {
                    this.this$0.connect(this.socket, this.params.address, this.params.channel, this.retryUnreachable);
                    this.success = true;
                    this.connecting = false;
                    synchronized (this.event) {
                        this.event.notifyAll();
                    }
                } catch (IOException e2) {
                    this.error = e2;
                    this.connecting = false;
                    synchronized (this.event) {
                        this.event.notifyAll();
                    }
                }
            } catch (Throwable th) {
                this.connecting = false;
                synchronized (this.event) {
                    this.event.notifyAll();
                    throw th;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0046, code lost:
    
        r0.wait(r9.timeout);
        r14 = r0.connecting;
        r0.interrupt();
     */
    @Override // com.intel.bluetooth.BluetoothStack
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long connectionRfOpenClientConnection(com.intel.bluetooth.BluetoothConnectionParams r9) throws java.io.IOException {
        /*
            r8 = this;
            r0 = r8
            r1 = r9
            boolean r1 = r1.authenticate
            r2 = r9
            boolean r2 = r2.encrypt
            long r0 = r0.socket(r1, r2)
            r10 = r0
            java.lang.Object r0 = new java.lang.Object
            r1 = r0
            r1.<init>()
            r12 = r0
            com.intel.bluetooth.BluetoothStackMicrosoft$ConnectThread r0 = new com.intel.bluetooth.BluetoothStackMicrosoft$ConnectThread
            r1 = r0
            r2 = r8
            r3 = r12
            r4 = r10
            r5 = r9
            r1.<init>(r2, r3, r4, r5)
            r13 = r0
            r0 = r13
            com.intel.bluetooth.UtilsJavaSE.threadSetDaemon(r0)
            r0 = 0
            r14 = r0
            r0 = r12
            r1 = r0
            r15 = r1
            monitor-enter(r0)
            r0 = r13
            r0.start()     // Catch: java.lang.Throwable -> L81
        L37:
            r0 = r13
            boolean r0 = r0.connecting     // Catch: java.lang.Throwable -> L81
            if (r0 == 0) goto L7b
            r0 = r9
            boolean r0 = r0.timeouts     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            if (r0 == 0) goto L5f
            r0 = r12
            r1 = r9
            int r1 = r1.timeout     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            long r1 = (long) r1     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            r0.wait(r1)     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            r0 = r13
            boolean r0 = r0.connecting     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            r14 = r0
            r0 = r13
            r0.interrupt()     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            goto L7b
        L5f:
            r0 = r12
            r0.wait()     // Catch: java.lang.InterruptedException -> L67 java.lang.Throwable -> L81
            goto L37
        L67:
            r16 = move-exception
            r0 = r8
            r1 = r10
            r0.close(r1)     // Catch: java.lang.Exception -> L71 java.lang.Throwable -> L81
            goto L73
        L71:
            r17 = move-exception
        L73:
            java.io.InterruptedIOException r0 = new java.io.InterruptedIOException     // Catch: java.lang.Throwable -> L81
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> L81
            throw r0     // Catch: java.lang.Throwable -> L81
        L7b:
            r0 = r15
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L81
            goto L89
        L81:
            r18 = move-exception
            r0 = r15
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L81
            r0 = r18
            throw r0
        L89:
            r0 = r13
            boolean r0 = r0.success
            if (r0 != 0) goto L9b
            r0 = r8
            r1 = r10
            r0.close(r1)     // Catch: java.lang.Exception -> L99
            goto L9b
        L99:
            r15 = move-exception
        L9b:
            r0 = r13
            java.io.IOException r0 = r0.error
            if (r0 == 0) goto La9
            r0 = r13
            java.io.IOException r0 = r0.error
            throw r0
        La9:
            r0 = r13
            boolean r0 = r0.success
            if (r0 != 0) goto Lc8
            r0 = r14
            if (r0 == 0) goto Lbf
            javax.bluetooth.BluetoothConnectionException r0 = new javax.bluetooth.BluetoothConnectionException
            r1 = r0
            r2 = 5
            r1.<init>(r2)
            throw r0
        Lbf:
            javax.bluetooth.BluetoothConnectionException r0 = new javax.bluetooth.BluetoothConnectionException
            r1 = r0
            r2 = 4
            r1.<init>(r2)
            throw r0
        Lc8:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.intel.bluetooth.BluetoothStackMicrosoft.connectionRfOpenClientConnection(com.intel.bluetooth.BluetoothConnectionParams):long");
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseClientConnection(long handle) throws IOException {
        close(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerOpen(BluetoothConnectionNotifierParams params, ServiceRecordImpl serviceRecord) throws IOException {
        long socket = socket(params.authenticate, params.encrypt);
        try {
            synchronized (this) {
                bind(socket);
            }
            listen(socket);
            int channel = getsockchannel(socket);
            DebugLog.debug("service channel ", channel);
            serviceRecord.populateRFCOMMAttributes(socket, channel, params.uuid, params.name, params.obex);
            serviceRecord.setHandle(registerService(serviceRecord.toByteArray(), serviceRecord.deviceServiceClasses));
            if (1 == 0) {
                try {
                    close(socket);
                } catch (IOException e2) {
                    DebugLog.debug("close on failure", (Throwable) e2);
                }
            }
            return socket;
        } catch (Throwable th) {
            if (0 == 0) {
                try {
                    close(socket);
                } catch (IOException e3) {
                    DebugLog.debug("close on failure", (Throwable) e3);
                }
            }
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerClose(long handle, ServiceRecordImpl serviceRecord) throws IOException {
        try {
            close(handle);
            unregisterService(serviceRecord.getHandle());
        } catch (Throwable th) {
            unregisterService(serviceRecord.getHandle());
            throw th;
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long rfServerAcceptAndOpenRfServerConnection(long handle) throws IOException {
        return accept(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void rfServerUpdateServiceRecord(long handle, ServiceRecordImpl serviceRecord, boolean acceptAndOpen) throws ServiceRegistrationException {
        unregisterService(serviceRecord.getHandle());
        try {
            byte[] blob = serviceRecord.toByteArray();
            serviceRecord.setHandle(registerService(blob, serviceRecord.deviceServiceClasses));
            DebugLog.debug("new serviceRecord", serviceRecord);
        } catch (IOException e2) {
            throw new ServiceRegistrationException(e2.toString());
        }
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfCloseServerConnection(long handle) throws IOException {
        connectionRfCloseClientConnection(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public long getConnectionRfRemoteAddress(long handle) throws IOException {
        return getpeeraddress(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfRead(long handle) throws IOException {
        return recv(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfRead(long handle, byte[] b2, int off, int len) throws IOException {
        return recv(handle, b2, off, len);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public int connectionRfReadAvailable(long handle) throws IOException {
        return recvAvailable(handle);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, int b2) throws IOException {
        send(handle, b2);
    }

    @Override // com.intel.bluetooth.BluetoothStack
    public void connectionRfWrite(long handle, byte[] b2, int off, int len) throws IOException {
        send(handle, b2, off, len);
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
