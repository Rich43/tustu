package org.bluez.v3;

import java.util.Map;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.bluez.Adapter")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter.class */
public interface Adapter extends org.bluez.Adapter {
    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady] */
    Map GetInfo();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady] */
    String GetAddress();

    String GetVersion();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Failed] */
    String GetRevision();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Failed] */
    String GetManufacturer();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Failed] */
    String GetCompany();

    String[] ListAvailableModes();

    String GetMode();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Failed, org.bluez.Error$NoSuchAdapter] */
    void SetMode(String str);

    UInt32 GetDiscoverableTimeout();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$InvalidArguments] */
    void SetDiscoverableTimeout(UInt32 uInt32);

    boolean IsConnectable();

    boolean IsDiscoverable();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    boolean IsConnected(String str);

    String[] ListConnections();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$UnsupportedMajorClass] */
    String GetMajorClass();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$UnsupportedMajorClass] */
    String[] ListAvailableMinorClasses();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$UnsupportedMajorClass] */
    String GetMinorClass();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchAdapter, org.bluez.Error$Failed, org.bluez.Error$UnsupportedMajorClass] */
    void SetMinorClass(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$NoSuchAdapter, org.bluez.Error$Failed] */
    String[] GetServiceClasses();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed] */
    String GetName();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    void SetName(String str);

    Map GetRemoteInfo(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteVersion(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteRevision(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteManufacturer(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteCompany(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteMajorClass(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteMinorClass(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String[] GetRemoteServiceClasses(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    UInt32 GetRemoteClass(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    byte[] GetRemoteFeatures(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable, org.bluez.Error$NotReady, org.bluez.Error$RequestDeferred] */
    String GetRemoteName(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String GetRemoteAlias(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Failed, org.bluez.Error$InvalidArguments] */
    void SetRemoteAlias(String str, String str2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Failed, org.bluez.Error$InvalidArguments] */
    void ClearRemoteAlias(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String LastSeen(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotAvailable] */
    String LastUsed(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$NoSuchAdapter, org.bluez.Error$InvalidArguments, org.bluez.Error$NotConnected, org.bluez.Error$InProgress] */
    void DisconnectRemoteDevice(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InvalidArguments, org.bluez.Error$AlreadyExists, org.bluez.Error$InProgress, org.bluez.Error$NoSuchAdapter, org.bluez.Error$ConnectionAttemptFailed, org.bluez.Error$AuthenticationFailed, org.bluez.Error$AuthenticationTimeout, org.bluez.Error$AuthenticationRejected, org.bluez.Error$AuthenticationCanceled] */
    void CreateBonding(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InvalidArguments, org.bluez.Error$NotInProgress, org.bluez.Error$NotAuthorized] */
    void CancelBondingProcess(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchAdapter, org.bluez.Error$DoesNotExist] */
    void RemoveBonding(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    boolean HasBonding(String str);

    String[] ListBondings();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$DoesNotExist] */
    byte GetPinCodeLength(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    byte GetEncryptionKeySize(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$AlreadyExists] */
    void SetTrusted(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    boolean IsTrusted(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$DoesNotExist] */
    void RemoveTrust(String str);

    String[] ListTrusts();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InProgress, org.bluez.Error$NoSuchAdapter] */
    void DiscoverDevices();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InProgress, org.bluez.Error$NoSuchAdapter] */
    void DiscoverDevicesWithoutNameResolving();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$NotAuthorized, org.bluez.Error$NoSuchAdapter] */
    void CancelDiscovery();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InProgress, org.bluez.Error$NoSuchAdapter] */
    void StartPeriodicDiscovery();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$NotAuthorized, org.bluez.Error$NoSuchAdapter] */
    void StopPeriodicDiscovery();

    boolean IsPeriodicDiscovery();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    void SetPeriodicDiscoveryNameResolving(boolean z2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    boolean GetPeriodicDiscoveryNameResolving();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$InProgress, org.bluez.Error$ConnectionAttemptFailed, org.bluez.Error$Failed] */
    UInt32[] GetRemoteServiceHandles(String str, String str2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$InProgress, org.bluez.Error$Failed] */
    byte[] GetRemoteServiceRecord(String str, UInt32 uInt32);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$InProgress, org.bluez.Error$Failed] */
    String GetRemoteServiceRecordAsXML(String str, UInt32 uInt32);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InProgress, org.bluez.Error$Failed] */
    String[] GetRemoteServiceIdentifiers(String str);

    void FinishRemoteServiceTransaction(String str);

    String[] ListRemoteDevices();

    String[] ListRecentRemoteDevices(String str);

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$ModeChanged.class */
    public static class ModeChanged extends DBusSignal {
        private final String mode;

        public ModeChanged(String path, String mode) throws DBusException {
            super(path, new Object[]{mode});
            this.mode = mode;
        }

        public String getMode() {
            return this.mode;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$DiscoverableTimeoutChanged.class */
    public static class DiscoverableTimeoutChanged extends DBusSignal {
        private final UInt32 timeout;

        public DiscoverableTimeoutChanged(String path, UInt32 timeout) throws DBusException {
            super(path, new Object[]{timeout});
            this.timeout = timeout;
        }

        public UInt32 getTimeout() {
            return this.timeout;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$MinorClassChanged.class */
    public static class MinorClassChanged extends DBusSignal {
        private final String minorClass;

        public MinorClassChanged(String path, String minorClass) throws DBusException {
            super(path, new Object[]{minorClass});
            this.minorClass = minorClass;
        }

        public String getMinorClass() {
            return this.minorClass;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$NameChanged.class */
    public static class NameChanged extends DBusSignal {
        private final String name;

        public NameChanged(String path, String name) throws DBusException {
            super(path, new Object[]{name});
            this.name = name;
        }

        public String getMinorClass() {
            return this.name;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$DiscoveryStarted.class */
    public static class DiscoveryStarted extends DBusSignal {
        public DiscoveryStarted(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$DiscoveryCompleted.class */
    public static class DiscoveryCompleted extends DBusSignal {
        public DiscoveryCompleted(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$PeriodicDiscoveryStarted.class */
    public static class PeriodicDiscoveryStarted extends DBusSignal {
        public PeriodicDiscoveryStarted(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$PeriodicDiscoveryStopped.class */
    public static class PeriodicDiscoveryStopped extends DBusSignal {
        public PeriodicDiscoveryStopped(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteDeviceFound.class */
    public static class RemoteDeviceFound extends DBusSignal {
        private final String address;
        private final UInt32 deviceClass;
        private final int rssi;

        public RemoteDeviceFound(String path, String address, UInt32 deviceClass, int rssi) throws DBusException {
            super(path, new Object[]{address, deviceClass, Integer.valueOf(rssi)});
            this.address = address;
            this.deviceClass = deviceClass;
            this.rssi = rssi;
        }

        public String getDeviceAddress() {
            return this.address;
        }

        public UInt32 getDeviceClass() {
            return this.deviceClass;
        }

        public int getDeviceRssi() {
            return this.rssi;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteDeviceDisappeared.class */
    public static class RemoteDeviceDisappeared extends DBusSignal {
        private final String address;

        public RemoteDeviceDisappeared(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteClassUpdated.class */
    public static class RemoteClassUpdated extends DBusSignal {
        private final String address;
        private final UInt32 deviceClass;

        public RemoteClassUpdated(String path, String address, UInt32 deviceClass) throws DBusException {
            super(path, new Object[]{address, deviceClass});
            this.address = address;
            this.deviceClass = deviceClass;
        }

        public String getDeviceAddress() {
            return this.address;
        }

        public UInt32 getDeviceClass() {
            return this.deviceClass;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteNameUpdated.class */
    public static class RemoteNameUpdated extends DBusSignal {
        private final String address;
        private final String name;

        public RemoteNameUpdated(String path, String address, String name) throws DBusException {
            super(path, new Object[]{address, name});
            this.address = address;
            this.name = name;
        }

        public String getDeviceAddress() {
            return this.address;
        }

        public String getDeviceName() {
            return this.name;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteIdentifiersUpdated.class */
    public static class RemoteIdentifiersUpdated extends DBusSignal {
        private final String address;
        private final String[] identifiers;

        public RemoteIdentifiersUpdated(String path, String address, String[] identifiers) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
            this.identifiers = identifiers;
        }

        public String getDeviceAddress() {
            return this.address;
        }

        public String[] getIdentifiers() {
            return this.identifiers;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteNameFailed.class */
    public static class RemoteNameFailed extends DBusSignal {
        private final String address;

        public RemoteNameFailed(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteNameRequested.class */
    public static class RemoteNameRequested extends DBusSignal {
        private final String address;

        public RemoteNameRequested(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteAliasChanged.class */
    public static class RemoteAliasChanged extends DBusSignal {
        private final String address;
        private final String alias;

        public RemoteAliasChanged(String path, String address, String alias) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
            this.alias = alias;
        }

        public String getDeviceAddress() {
            return this.address;
        }

        public String getAlias() {
            return this.alias;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteAliasCleared.class */
    public static class RemoteAliasCleared extends DBusSignal {
        private final String address;

        public RemoteAliasCleared(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteDeviceConnected.class */
    public static class RemoteDeviceConnected extends DBusSignal {
        private final String address;

        public RemoteDeviceConnected(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteDeviceDisconnectRequested.class */
    public static class RemoteDeviceDisconnectRequested extends DBusSignal {
        private final String address;

        public RemoteDeviceDisconnectRequested(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$RemoteDeviceDisconnected.class */
    public static class RemoteDeviceDisconnected extends DBusSignal {
        private final String address;

        public RemoteDeviceDisconnected(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$BondingCreated.class */
    public static class BondingCreated extends DBusSignal {
        private final String address;

        public BondingCreated(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$BondingRemoved.class */
    public static class BondingRemoved extends DBusSignal {
        private final String address;

        public BondingRemoved(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$TrustAdded.class */
    public static class TrustAdded extends DBusSignal {
        private final String address;

        public TrustAdded(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Adapter$TrustRemoved.class */
    public static class TrustRemoved extends DBusSignal {
        private final String address;

        public TrustRemoved(String path, String address) throws DBusException {
            super(path, new Object[]{address});
            this.address = address;
        }

        public String getDeviceAddress() {
            return this.address;
        }
    }
}
