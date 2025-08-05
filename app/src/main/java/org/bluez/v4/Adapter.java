package org.bluez.v4;

import java.util.Map;
import org.bluez.dbus.DBusProperties;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.bluez.Adapter")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter.class */
public interface Adapter extends org.bluez.Adapter, DBusProperties.PropertiesAccess {

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter$Properties.class */
    public enum Properties implements DBusProperties.PropertyEnum {
        Address,
        Name,
        Class,
        Powered,
        Discoverable,
        Pairable,
        PaireableTimeout,
        DiscoverableTimeout,
        Discovering,
        Devices
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected] */
    void RequestSession();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist] */
    void ReleaseSession();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed] */
    void StartDiscovery();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$NotAuthorized] */
    void StopDiscovery();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist, org.bluez.Error$InvalidArguments] */
    Path FindDevice(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed, org.bluez.Error$OutOfMemory] */
    Path[] ListDevices();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    Path CreateDevice(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    Path CreatePairedDevice(String str, Path path, String str2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotInProgress] */
    void CancelDeviceCreation(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed] */
    void RemoveDevice(Path path);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$AlreadyExists] */
    void RegisterAgent(Path path, String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist] */
    void UnregisterAgent(Path path);

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter$PropertyChanged.class */
    public static class PropertyChanged extends DBusSignal {
        public PropertyChanged(String path, String name, Variant<Object> value) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter$DeviceFound.class */
    public static class DeviceFound extends DBusSignal {
        private final String address;
        private final Map<String, Variant<?>> devicePoperties;

        public DeviceFound(String path, String address, Map<String, Variant<?>> devicePoperties) throws DBusException {
            super(path, new Object[]{address, devicePoperties});
            this.address = address;
            this.devicePoperties = devicePoperties;
        }

        public String getDeviceAddress() {
            return this.address;
        }

        public Map<String, Variant<?>> getDevicePoperties() {
            return this.devicePoperties;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter$DeviceDisappeared.class */
    public static class DeviceDisappeared extends DBusSignal {
        public DeviceDisappeared(String path, String address) throws DBusException {
            super(path, new Object[]{address});
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter$DeviceCreated.class */
    public static class DeviceCreated extends DBusSignal {
        public DeviceCreated(String path, Path device) throws DBusException {
            super(path, new Object[]{device});
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Adapter$DeviceRemoved.class */
    public static class DeviceRemoved extends DBusSignal {
        public DeviceRemoved(String path, Path device) throws DBusException {
            super(path, new Object[]{device});
        }
    }
}
