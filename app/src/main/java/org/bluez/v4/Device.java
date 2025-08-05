package org.bluez.v4;

import java.util.Map;
import org.bluez.dbus.DBusProperties;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.bluez.Device")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Device.class */
public interface Device extends DBusInterface, DBusProperties.PropertiesAccess {

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Device$Properties.class */
    public enum Properties implements DBusProperties.PropertyEnum {
        Address,
        Name,
        Icon,
        Class,
        UUIDs,
        Paired,
        Connected,
        Trusted,
        Alias,
        Nodes,
        Adapter,
        LegacyPairing
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$InProgress] */
    Map<UInt32, String> DiscoverServices(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotReady, org.bluez.Error$Failed, org.bluez.Error$NotAuthorized] */
    void CancelDiscovery();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotConnected] */
    void Disconnect();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed, org.bluez.Error$OutOfMemory] */
    Object[] ListNodes();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NotSupported] */
    Object CreateNode(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$DoesNotExist] */
    void RemoveNode(Object obj);

    @DBusInterfaceName("org.bluez.Device.PropertyChanged")
    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Device$PropertyChanged.class */
    public static class PropertyChanged extends DBusSignal {
        public PropertyChanged(String path, String name, Variant<Object> value) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Device$DisconnectRequested.class */
    public static class DisconnectRequested extends DBusSignal {
        public DisconnectRequested(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Device$NodeCreated.class */
    public static class NodeCreated extends DBusSignal {
        public NodeCreated(String path, Path node) throws DBusException {
            super(path, new Object[]{node});
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Device$NodeRemoved.class */
    public static class NodeRemoved extends DBusSignal {
        public NodeRemoved(String path, Path node) throws DBusException {
            super(path, new Object[]{node});
        }
    }
}
