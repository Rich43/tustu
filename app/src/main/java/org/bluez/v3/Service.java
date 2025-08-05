package org.bluez.v3;

import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.bluez.Service")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Service.class */
public interface Service extends DBusInterface {
    Map GetInfo();

    String GetIdentifier();

    String GetName();

    String GetDescription();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$NotAvailable] */
    String GetBusName();

    void Start();

    void Stop();

    boolean IsRunning();

    boolean IsExternal();

    String[] ListTrusts();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$AlreadyExists] */
    void SetTrusted(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    boolean IsTrusted(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$DoesNotExist] */
    void RemoveTrust(String str);

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Service$Started.class */
    public static class Started extends DBusSignal {
        public Started(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Service$Stopped.class */
    public static class Stopped extends DBusSignal {
        public Stopped(String path) throws DBusException {
            super(path, new Object[0]);
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Service$TrustAdded.class */
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

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Service$TrustRemoved.class */
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
