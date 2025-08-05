package org.bluez;

import com.intel.bluetooth.DebugLog;
import org.bluez.v3.BlueZAPIV3;
import org.bluez.v4.BlueZAPIV4;
import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/BlueZAPIFactory.class */
public abstract class BlueZAPIFactory {
    private BlueZAPIFactory() {
    }

    public static BlueZAPI getBlueZAPI(DBusConnection dbusConn) throws DBusException {
        org.bluez.v3.Manager dbusManagerV3 = (org.bluez.v3.Manager) dbusConn.getRemoteObject("org.bluez", "/org/bluez", org.bluez.v3.Manager.class);
        try {
            dbusManagerV3.InterfaceVersion();
            return new BlueZAPIV3(dbusConn, dbusManagerV3);
        } catch (DBus.Error.UnknownMethod e2) {
            DebugLog.debug("Switch to bluez D-Bus for version 4");
            org.bluez.v4.Manager dbusManagerV4 = (org.bluez.v4.Manager) dbusConn.getRemoteObject("org.bluez", "/", org.bluez.v4.Manager.class);
            return new BlueZAPIV4(dbusConn, dbusManagerV4);
        }
    }
}
