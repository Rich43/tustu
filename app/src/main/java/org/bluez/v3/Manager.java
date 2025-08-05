package org.bluez.v3;

import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.bluez.Manager")
/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Manager.class */
public interface Manager extends org.bluez.Manager {
    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    UInt32 InterfaceVersion();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchAdapter] */
    String DefaultAdapter();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchAdapter] */
    String FindAdapter(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$Failed, org.bluez.Error$OutOfMemory] */
    String[] ListAdapters();

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments, org.bluez.Error$NoSuchService] */
    String FindService(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    String[] ListServices();

    String ActivateService(String str);

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Manager$AdapterAdded.class */
    public static class AdapterAdded extends DBusSignal {
        private final String adapterPath;

        public AdapterAdded(String path, String adapterPath) throws DBusException {
            super(path, new Object[]{adapterPath});
            this.adapterPath = adapterPath;
        }

        public String getAdapterPath() {
            return this.adapterPath;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Manager$AdapterRemoved.class */
    public static class AdapterRemoved extends DBusSignal {
        private final String adapterPath;

        public AdapterRemoved(String path, String adapterPath) throws DBusException {
            super(path, new Object[]{adapterPath});
            this.adapterPath = adapterPath;
        }

        public String getAdapterPath() {
            return this.adapterPath;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Manager$DefaultAdapterChanged.class */
    public static class DefaultAdapterChanged extends DBusSignal {
        private final String adapterPath;

        public DefaultAdapterChanged(String path, String adapterPath) throws DBusException {
            super(path, new Object[]{adapterPath});
            this.adapterPath = adapterPath;
        }

        public String getAdapterPath() {
            return this.adapterPath;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Manager$ServiceAdded.class */
    public static class ServiceAdded extends DBusSignal {
        private final String serviceAgentPath;

        public ServiceAdded(String path, String serviceAgentPath) throws DBusException {
            super(path, new Object[]{serviceAgentPath});
            this.serviceAgentPath = serviceAgentPath;
        }

        public String getServiceAgentPath() {
            return this.serviceAgentPath;
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/Manager$ServiceRemoved.class */
    public static class ServiceRemoved extends DBusSignal {
        private final String serviceAgentPath;

        public ServiceRemoved(String path, String serviceAgentPath) throws DBusException {
            super(path, new Object[]{serviceAgentPath});
            this.serviceAgentPath = serviceAgentPath;
        }

        public String getServiceAgentPath() {
            return this.serviceAgentPath;
        }
    }
}
