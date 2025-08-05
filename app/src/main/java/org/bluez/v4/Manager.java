package org.bluez.v4;

import org.bluez.dbus.DBusProperties;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.bluez.Manager")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Manager.class */
public interface Manager extends org.bluez.Manager, DBusProperties.PropertiesAccess {

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Manager$Properties.class */
    public enum Properties implements DBusProperties.PropertyEnum {
        Adapters
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchAdapter] */
    Path DefaultAdapter();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchAdapter] */
    Path FindAdapter(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed, org.bluez.Error$OutOfMemory] */
    Path[] ListAdapters();

    @DBusInterfaceName("org.bluez.Manager.AdapterAdded")
    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Manager$PropertyChanged.class */
    public static class PropertyChanged extends DBusSignal {
        public PropertyChanged(String path, String name, Variant<Object> value) throws DBusException {
            super(path, new Object[0]);
        }
    }

    @DBusInterfaceName("org.bluez.Manager.AdapterAdded")
    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Manager$AdapterAdded.class */
    public static class AdapterAdded extends DBusSignal {
        public AdapterAdded(String path, Path adapter) throws DBusException {
            super(path, new Object[]{adapter});
        }
    }

    @DBusInterfaceName("org.bluez.Manager.AdapterAdded")
    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Manager$AdapterRemoved.class */
    public static class AdapterRemoved extends DBusSignal {
        public AdapterRemoved(String path, Path adapter) throws DBusException {
            super(path, new Object[]{adapter});
        }
    }

    @DBusInterfaceName("org.bluez.Manager.AdapterAdded")
    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/Manager$DefaultAdapterChanged.class */
    public static class DefaultAdapterChanged extends DBusSignal {
        public DefaultAdapterChanged(String path, Path adapter) throws DBusException {
            super(path, new Object[]{adapter});
        }
    }
}
