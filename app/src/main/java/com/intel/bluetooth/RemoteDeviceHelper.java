package com.intel.bluetooth;

import com.intel.bluetooth.WeakVectorFactory;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/RemoteDeviceHelper.class */
public abstract class RemoteDeviceHelper {
    private static Hashtable stackDevicesCashed = new Hashtable();

    /* renamed from: com.intel.bluetooth.RemoteDeviceHelper$1, reason: invalid class name */
    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/RemoteDeviceHelper$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/RemoteDeviceHelper$RemoteDeviceWithExtendedInfo.class */
    private static class RemoteDeviceWithExtendedInfo extends RemoteDevice {
        String name;
        long addressLong;
        BluetoothStack bluetoothStack;
        private Hashtable stackAttributes;
        private boolean paired;
        private WeakVectorFactory.WeakVector connections;

        RemoteDeviceWithExtendedInfo(BluetoothStack x0, long x1, String x2, AnonymousClass1 x3) {
            this(x0, x1, x2);
        }

        private RemoteDeviceWithExtendedInfo(BluetoothStack bluetoothStack, long address, String name) {
            super(RemoteDeviceHelper.getBluetoothAddress(address));
            this.bluetoothStack = bluetoothStack;
            this.name = name;
            this.addressLong = address;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addConnection(Object connection) {
            synchronized (this) {
                if (this.connections == null) {
                    this.connections = WeakVectorFactory.createWeakVector();
                }
            }
            synchronized (this.connections) {
                this.connections.addElement(connection);
                DebugLog.debug("connection open, open now", this.connections.size());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeConnection(Object connection) {
            if (this.connections == null) {
                return;
            }
            synchronized (this.connections) {
                this.connections.removeElement(connection);
                DebugLog.debug("connection closed, open now", this.connections.size());
            }
        }

        void shutdownConnections() {
            Vector c2shutdown;
            if (!hasConnections()) {
                return;
            }
            new Vector();
            synchronized (this.connections) {
                c2shutdown = Utils.clone(this.connections.elements());
            }
            Enumeration en = c2shutdown.elements();
            while (en.hasMoreElements()) {
                BluetoothConnectionAccess c2 = (BluetoothConnectionAccess) en.nextElement2();
                try {
                    c2.shutdown();
                } catch (IOException e2) {
                    DebugLog.debug("connection shutdown", (Throwable) e2);
                }
            }
            synchronized (this.connections) {
                this.connections.removeAllElements();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setStackAttributes(Object key, Object value) {
            if (this.stackAttributes == null) {
                this.stackAttributes = new Hashtable();
            }
            if (value == null) {
                this.stackAttributes.remove(key);
            } else {
                this.stackAttributes.put(key, value);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Object getStackAttributes(Object key) {
            if (this.stackAttributes == null) {
                return null;
            }
            return this.stackAttributes.get(key);
        }

        public String toString() {
            return super.getBluetoothAddress();
        }

        int connectionsCount() {
            if (this.connections == null) {
                return 0;
            }
            return this.connections.size();
        }

        boolean hasConnections() {
            return connectionsCount() != 0;
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean authenticate() throws IOException {
            if (!hasConnections()) {
                throw new IOException("No open connections to this RemoteDevice");
            }
            if (isAuthenticated()) {
                return true;
            }
            boolean authenticated = this.bluetoothStack.authenticateRemoteDevice(this.addressLong);
            this.paired = authenticated;
            if (authenticated) {
                updateConnectionMarkAuthenticated();
            }
            return authenticated;
        }

        boolean authenticate(String passkey) throws IOException {
            boolean authenticated = this.bluetoothStack.authenticateRemoteDevice(this.addressLong, passkey);
            this.paired = authenticated;
            if (authenticated) {
                updateConnectionMarkAuthenticated();
            }
            return authenticated;
        }

        void removeAuthentication() throws IOException {
            this.bluetoothStack.removeAuthenticationWithRemoteDevice(this.addressLong);
            this.paired = false;
        }

        private void updateConnectionMarkAuthenticated() {
            if (this.connections == null) {
                return;
            }
            synchronized (this.connections) {
                Enumeration en = this.connections.elements();
                while (en.hasMoreElements()) {
                    BluetoothConnectionAccess c2 = (BluetoothConnectionAccess) en.nextElement2();
                    c2.markAuthenticated();
                }
            }
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean authorize(Connection conn) throws IOException {
            if (!(conn instanceof BluetoothConnectionAccess)) {
                throw new IllegalArgumentException("Connection is not a Bluetooth connection");
            }
            if (((BluetoothConnectionAccess) conn).isClosed()) {
                throw new IOException("Connection is already closed");
            }
            if (conn instanceof BluetoothServerConnection) {
                return isTrustedDevice() || isAuthenticated();
            }
            throw new IllegalArgumentException("Connection is not an incomming Bluetooth connection");
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean isAuthorized(Connection conn) throws IOException {
            if (!(conn instanceof BluetoothConnectionAccess)) {
                throw new IllegalArgumentException("Connection is not a Bluetooth connection");
            }
            if (((BluetoothConnectionAccess) conn).isClosed()) {
                throw new IOException("Connection is already closed");
            }
            if (!(conn instanceof BluetoothServerConnection)) {
                throw new IllegalArgumentException("Connection is not an incomming Bluetooth connection");
            }
            return isTrustedDevice();
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean encrypt(Connection conn, boolean on) throws IOException {
            if (!(conn instanceof BluetoothConnectionAccess)) {
                throw new IllegalArgumentException("Connection is not a Bluetooth connection");
            }
            if (((BluetoothConnectionAccess) conn).getRemoteAddress() != this.addressLong) {
                throw new IllegalArgumentException("Connection is not to this device");
            }
            if ((((BluetoothConnectionAccess) conn).getSecurityOpt() == 2) == on) {
                return true;
            }
            return ((BluetoothConnectionAccess) conn).encrypt(this.addressLong, on);
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean isAuthenticated() {
            if (!hasConnections()) {
                DebugLog.debug("no connections, Authenticated = false");
                return false;
            }
            Boolean authenticated = this.bluetoothStack.isRemoteDeviceAuthenticated(this.addressLong);
            if (authenticated != null) {
                return authenticated.booleanValue();
            }
            synchronized (this.connections) {
                Enumeration en = this.connections.elements();
                while (en.hasMoreElements()) {
                    BluetoothConnectionAccess c2 = (BluetoothConnectionAccess) en.nextElement2();
                    if (c2.getSecurityOpt() != 0) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean isEncrypted() {
            if (!hasConnections()) {
                return false;
            }
            synchronized (this.connections) {
                Enumeration en = this.connections.elements();
                while (en.hasMoreElements()) {
                    BluetoothConnectionAccess c2 = (BluetoothConnectionAccess) en.nextElement2();
                    if (c2.getSecurityOpt() == 2) {
                        return true;
                    }
                }
                return false;
            }
        }

        @Override // javax.bluetooth.RemoteDevice
        public boolean isTrustedDevice() {
            Boolean trusted = this.bluetoothStack.isRemoteDeviceTrusted(this.addressLong);
            if (trusted == null) {
                return this.paired;
            }
            return trusted.booleanValue();
        }
    }

    private RemoteDeviceHelper() {
    }

    private static synchronized Hashtable devicesCashed(BluetoothStack bluetoothStack) {
        Hashtable devicesCashed = (Hashtable) stackDevicesCashed.get(bluetoothStack);
        if (devicesCashed == null) {
            devicesCashed = new Hashtable();
            stackDevicesCashed.put(bluetoothStack, devicesCashed);
        }
        return devicesCashed;
    }

    private static RemoteDeviceWithExtendedInfo getCashedDeviceWithExtendedInfo(BluetoothStack bluetoothStack, long address) {
        Object key = new Long(address);
        return (RemoteDeviceWithExtendedInfo) devicesCashed(bluetoothStack).get(key);
    }

    static RemoteDevice getCashedDevice(BluetoothStack bluetoothStack, long address) {
        return getCashedDeviceWithExtendedInfo(bluetoothStack, address);
    }

    static RemoteDevice getStackBoundDevice(BluetoothStack bluetoothStack, RemoteDevice device) {
        if ((device instanceof RemoteDeviceWithExtendedInfo) && ((RemoteDeviceWithExtendedInfo) device).bluetoothStack == bluetoothStack) {
            return device;
        }
        return createRemoteDevice(bluetoothStack, getAddress(device), null, false);
    }

    static RemoteDevice createRemoteDevice(BluetoothStack bluetoothStack, long address, String name, boolean paired) {
        RemoteDeviceWithExtendedInfo dev = getCashedDeviceWithExtendedInfo(bluetoothStack, address);
        if (dev == null) {
            Object saveID = BlueCoveImpl.getCurrentThreadBluetoothStackID();
            try {
                BlueCoveImpl.setThreadBluetoothStack(bluetoothStack);
                dev = new RemoteDeviceWithExtendedInfo(bluetoothStack, address, name, null);
                if (saveID != null) {
                    BlueCoveImpl.setThreadBluetoothStackID(saveID);
                }
                devicesCashed(bluetoothStack).put(new Long(address), dev);
                DebugLog.debug0x("new devicesCashed", address);
            } catch (Throwable th) {
                if (saveID != null) {
                    BlueCoveImpl.setThreadBluetoothStackID(saveID);
                }
                throw th;
            }
        } else if (!Utils.isStringSet(dev.name) || Utils.isStringSet(name)) {
            dev.name = name;
        }
        if (paired) {
            dev.paired = paired;
        }
        return dev;
    }

    private static BluetoothStack getBluetoothStack() throws RuntimeException {
        try {
            return BlueCoveImpl.instance().getBluetoothStack();
        } catch (BluetoothStateException e2) {
            throw ((RuntimeException) UtilsJavaSE.initCause(new RuntimeException("Can't initialize bluetooth support"), e2));
        }
    }

    private static RemoteDeviceWithExtendedInfo remoteDeviceImpl(RemoteDevice device) {
        return (RemoteDeviceWithExtendedInfo) createRemoteDevice(null, device);
    }

    static RemoteDevice createRemoteDevice(BluetoothStack bluetoothStack, RemoteDevice device) throws RuntimeException {
        if (device instanceof RemoteDeviceWithExtendedInfo) {
            return device;
        }
        if (bluetoothStack == null) {
            bluetoothStack = getBluetoothStack();
        }
        return createRemoteDevice(bluetoothStack, getAddress(device), null, false);
    }

    static RemoteDevice[] remoteDeviceListToArray(Vector devices) {
        RemoteDevice[] devicesArray = new RemoteDevice[devices.size()];
        int i2 = 0;
        Enumeration en = devices.elements();
        while (en.hasMoreElements()) {
            int i3 = i2;
            i2++;
            devicesArray[i3] = (RemoteDevice) en.nextElement2();
        }
        return devicesArray;
    }

    public static int openConnections() {
        int c2 = 0;
        Hashtable devicesCashed = devicesCashed(getBluetoothStack());
        synchronized (devicesCashed) {
            Enumeration en = devicesCashed.elements();
            while (en.hasMoreElements()) {
                c2 += ((RemoteDeviceWithExtendedInfo) en.nextElement2()).connectionsCount();
            }
        }
        return c2;
    }

    public static int openConnections(long address) {
        RemoteDeviceWithExtendedInfo dev = getCashedDeviceWithExtendedInfo(getBluetoothStack(), address);
        if (dev == null) {
            return 0;
        }
        return dev.connectionsCount();
    }

    public static int connectedDevices() {
        int c2 = 0;
        Hashtable devicesCashed = devicesCashed(getBluetoothStack());
        synchronized (devicesCashed) {
            Enumeration en = devicesCashed.elements();
            while (en.hasMoreElements()) {
                if (((RemoteDeviceWithExtendedInfo) en.nextElement2()).hasConnections()) {
                    c2++;
                }
            }
        }
        return c2;
    }

    static void shutdownConnections(BluetoothStack bluetoothStack) {
        Hashtable devicesCashed = devicesCashed(bluetoothStack);
        synchronized (devicesCashed) {
            Enumeration en = devicesCashed.elements();
            while (en.hasMoreElements()) {
                ((RemoteDeviceWithExtendedInfo) en.nextElement2()).shutdownConnections();
            }
        }
    }

    public static String formatBluetoothAddress(String address) {
        String s2 = address.toUpperCase();
        return new StringBuffer().append("000000000000".substring(s2.length())).append(s2).toString();
    }

    public static String getBluetoothAddress(long address) {
        return formatBluetoothAddress(Utils.toHexString(address));
    }

    public static long getAddress(String bluetoothAddress) {
        if (bluetoothAddress.indexOf(45) != -1) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal bluetoothAddress {").append(bluetoothAddress).append("}").toString());
        }
        try {
            return Long.parseLong(bluetoothAddress, 16);
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("Illegal bluetoothAddress {").append(bluetoothAddress).append("}; should be hex number").toString());
        }
    }

    static long getAddress(RemoteDevice device) {
        if (device instanceof RemoteDeviceWithExtendedInfo) {
            return ((RemoteDeviceWithExtendedInfo) device).addressLong;
        }
        return getAddress(device.getBluetoothAddress());
    }

    static void setStackAttributes(BluetoothStack bluetoothStack, RemoteDevice device, Object key, Object value) {
        RemoteDeviceWithExtendedInfo devInfo = (RemoteDeviceWithExtendedInfo) createRemoteDevice(bluetoothStack, device);
        devInfo.setStackAttributes(key, value);
    }

    static Object getStackAttributes(BluetoothStack bluetoothStack, RemoteDevice device, Object key) {
        RemoteDeviceWithExtendedInfo devInfo;
        if (device instanceof RemoteDeviceWithExtendedInfo) {
            devInfo = (RemoteDeviceWithExtendedInfo) device;
        } else {
            devInfo = getCashedDeviceWithExtendedInfo(bluetoothStack, getAddress(device));
        }
        if (devInfo != null) {
            return devInfo.getStackAttributes(key);
        }
        return null;
    }

    static void connected(BluetoothConnectionAccess connection) throws IOException {
        RemoteDeviceWithExtendedInfo device = (RemoteDeviceWithExtendedInfo) implGetRemoteDevice((Connection) connection);
        connection.setRemoteDevice(device);
        device.addConnection(connection);
    }

    static void disconnected(BluetoothConnectionAccess connection) {
        RemoteDevice d2 = connection.getRemoteDevice();
        if (d2 != null) {
            ((RemoteDeviceWithExtendedInfo) d2).removeConnection(connection);
            connection.setRemoteDevice(null);
        }
    }

    public static int readRSSI(RemoteDevice device) throws IOException {
        RemoteDeviceWithExtendedInfo deviceImpl = remoteDeviceImpl(device);
        if (deviceImpl.bluetoothStack instanceof BluetoothStackExtension) {
            return ((BluetoothStackExtension) deviceImpl.bluetoothStack).readRemoteDeviceRSSI(deviceImpl.addressLong);
        }
        throw new NotSupportedIOException(deviceImpl.bluetoothStack.getStackID());
    }

    public static boolean authenticate(RemoteDevice device) throws IOException {
        return remoteDeviceImpl(device).authenticate();
    }

    public static boolean authenticate(RemoteDevice device, String passkey) throws IOException {
        return remoteDeviceImpl(device).authenticate(passkey);
    }

    public static void removeAuthentication(RemoteDevice device) throws IOException {
        remoteDeviceImpl(device).removeAuthentication();
    }

    public static String implGetFriendlyName(RemoteDevice device, long address, boolean alwaysAsk) throws IOException, RuntimeException {
        if (!(device instanceof RemoteDeviceWithExtendedInfo)) {
            device = createRemoteDevice(null, device);
        }
        String name = ((RemoteDeviceWithExtendedInfo) device).name;
        if (alwaysAsk || name == null) {
            name = ((RemoteDeviceWithExtendedInfo) device).bluetoothStack.getRemoteDeviceFriendlyName(address);
            if (name != null) {
                ((RemoteDeviceWithExtendedInfo) device).name = name;
            }
        }
        return name;
    }

    public static RemoteDevice implGetRemoteDevice(Connection conn) throws IOException {
        if (!(conn instanceof BluetoothConnectionAccess)) {
            throw new IllegalArgumentException(new StringBuffer().append("Not a Bluetooth connection ").append(conn.getClass().getName()).toString());
        }
        return createRemoteDevice(((BluetoothConnectionAccess) conn).getBluetoothStack(), ((BluetoothConnectionAccess) conn).getRemoteAddress(), null, false);
    }

    public static RemoteDevice[] implRetrieveDevices(BluetoothStack bluetoothStack, int option) {
        if (option != 1 && option != 0) {
            throw new IllegalArgumentException("invalid option");
        }
        RemoteDevice[] impl = bluetoothStack.retrieveDevices(option);
        if (impl != null) {
            if (impl.length == 0) {
                return null;
            }
            return impl;
        }
        Hashtable devicesCashed = devicesCashed(bluetoothStack);
        switch (option) {
            case 0:
                if (devicesCashed.size() == 0) {
                    return null;
                }
                RemoteDevice[] devices = new RemoteDevice[devicesCashed.size()];
                int k2 = 0;
                Enumeration en = devicesCashed.elements();
                while (en.hasMoreElements()) {
                    int i2 = k2;
                    k2++;
                    devices[i2] = (RemoteDevice) en.nextElement2();
                }
                return devices;
            case 1:
                if (devicesCashed.size() == 0) {
                    return null;
                }
                Vector devicesPaired = new Vector();
                Enumeration en2 = devicesCashed.elements();
                while (en2.hasMoreElements()) {
                    RemoteDeviceWithExtendedInfo d2 = (RemoteDeviceWithExtendedInfo) en2.nextElement2();
                    if (d2.isTrustedDevice()) {
                        devicesPaired.addElement(d2);
                    }
                }
                if (devicesPaired.size() == 0) {
                    return null;
                }
                return remoteDeviceListToArray(devicesPaired);
            default:
                throw new IllegalArgumentException("invalid option");
        }
    }

    public static boolean implAuthorize(RemoteDevice device, Connection conn) throws IOException {
        return remoteDeviceImpl(device).authorize(conn);
    }

    public static boolean implEncrypt(RemoteDevice device, Connection conn, boolean on) throws IOException {
        return remoteDeviceImpl(device).encrypt(conn, on);
    }

    public static boolean implIsAuthenticated(RemoteDevice device) {
        return remoteDeviceImpl(device).isAuthenticated();
    }

    public static boolean implIsAuthorized(RemoteDevice device, Connection conn) throws IOException {
        return remoteDeviceImpl(device).isAuthorized(conn);
    }

    public static boolean implIsEncrypted(RemoteDevice device) {
        return remoteDeviceImpl(device).isEncrypted();
    }

    public static boolean implIsTrustedDevice(RemoteDevice device) {
        return remoteDeviceImpl(device).isTrustedDevice();
    }
}
