package org.bluez.v3;

import com.intel.bluetooth.BlueCoveImpl;
import com.intel.bluetooth.DebugLog;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.bluez.BlueZAPI;
import org.bluez.Error;
import org.bluez.v3.Adapter;
import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.exceptions.DBusExecutionException;
import sun.java2d.marlin.MarlinConst;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v3/BlueZAPIV3.class */
public class BlueZAPIV3 implements BlueZAPI {
    private DBusConnection dbusConn;
    private Manager dbusManager;
    private Adapter adapter;
    private Path adapterPath;
    private long lastDeviceDiscoveryTime = 0;

    public BlueZAPIV3(DBusConnection dbusConn, Manager dbusManager) {
        this.dbusConn = dbusConn;
        this.dbusManager = dbusManager;
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    @Override // org.bluez.BlueZAPI
    public Path findAdapter(String pattern) {
        try {
            String path = this.dbusManager.FindAdapter(pattern);
            if (path == null) {
                return null;
            }
            return new Path(path);
        } catch (Error.NoSuchAdapter e2) {
            return null;
        }
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    @Override // org.bluez.BlueZAPI
    public Path defaultAdapter() {
        try {
            String path = this.dbusManager.DefaultAdapter();
            if (path == null) {
                return null;
            }
            return new Path(path);
        } catch (Error.NoSuchAdapter e2) {
            return null;
        }
    }

    @Override // org.bluez.BlueZAPI
    public Path getAdapter(int number) {
        String[] adapters = this.dbusManager.ListAdapters();
        if (adapters == null) {
            throw null;
        }
        if (number < 0 || number >= adapters.length) {
            throw null;
        }
        return new Path(String.valueOf(adapters[number]));
    }

    @Override // org.bluez.BlueZAPI
    public List<String> listAdapters() {
        List<String> v2 = new Vector<>();
        String[] adapters = this.dbusManager.ListAdapters();
        if (adapters != null) {
            for (String str : adapters) {
                String adapterId = String.valueOf(str);
                if (adapterId.startsWith("/org/bluez/")) {
                    adapterId = adapterId.substring("/org/bluez/".length());
                }
                v2.add(adapterId);
            }
        }
        return v2;
    }

    @Override // org.bluez.BlueZAPI
    public void selectAdapter(Path adapterPath) throws DBusException {
        DebugLog.debug("selectAdapter", adapterPath.getPath());
        this.adapter = (Adapter) this.dbusConn.getRemoteObject("org.bluez", adapterPath.getPath(), Adapter.class);
        this.adapterPath = adapterPath;
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterAddress() {
        return this.adapter.GetAddress();
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterID() {
        if (this.adapterPath.getPath().startsWith("/org/bluez/")) {
            return this.adapterPath.getPath().substring("/org/bluez/".length());
        }
        return this.adapterPath.getPath();
    }

    @Override // org.bluez.BlueZAPI
    public int getAdapterDeviceClass() {
        int record;
        int record2 = 0;
        String major = this.adapter.GetMajorClass();
        if ("computer".equals(major)) {
            record2 = 0 | 256;
        } else {
            DebugLog.debug("Unknown MajorClass", major);
        }
        String minor = this.adapter.GetMinorClass();
        if (minor.equals("uncategorized")) {
            record = record2 | 0;
        } else if (minor.equals("desktop")) {
            record = record2 | 4;
        } else if (minor.equals("server")) {
            record = record2 | 8;
        } else if (minor.equals("laptop")) {
            record = record2 | 12;
        } else if (minor.equals("handheld")) {
            record = record2 | 16;
        } else if (minor.equals("palm")) {
            record = record2 | 20;
        } else if (minor.equals("wearable")) {
            record = record2 | 24;
        } else {
            DebugLog.debug("Unknown MinorClass", minor);
            record = record2 | 0;
        }
        String[] srvc = this.adapter.GetServiceClasses();
        if (srvc != null) {
            for (String serviceClass : srvc) {
                if (serviceClass.equals("positioning")) {
                    record |= 65536;
                } else if (serviceClass.equals("networking")) {
                    record |= 131072;
                } else if (serviceClass.equals("rendering")) {
                    record |= 262144;
                } else if (serviceClass.equals("capturing")) {
                    record |= 524288;
                } else if (serviceClass.equals("object transfer")) {
                    record |= 1048576;
                } else if (serviceClass.equals("audio")) {
                    record |= 2097152;
                } else if (serviceClass.equals("telephony")) {
                    record |= 4194304;
                } else if (serviceClass.equals("information")) {
                    record |= 8388608;
                } else {
                    DebugLog.debug("Unknown ServiceClasses", serviceClass);
                }
            }
        }
        return record;
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterName() {
        try {
            return this.adapter.GetName();
        } catch (Error.Failed e2) {
            return null;
        } catch (Error.NotReady e3) {
            return null;
        }
    }

    @Override // org.bluez.BlueZAPI
    public boolean isAdapterDiscoverable() {
        return this.adapter.IsDiscoverable();
    }

    @Override // org.bluez.BlueZAPI
    public int getAdapterDiscoverableTimeout() {
        return this.adapter.GetDiscoverableTimeout().intValue();
    }

    @Override // org.bluez.BlueZAPI
    public boolean setAdapterDiscoverable(int mode) throws DBusException {
        String modeStr;
        switch (mode) {
            case 0:
                modeStr = "connectable";
                break;
            case 10390272:
                modeStr = "limited";
                break;
            case 10390323:
                modeStr = "discoverable";
                break;
            default:
                if (10390272 <= mode && mode <= 10390335) {
                    return false;
                }
                throw new IllegalArgumentException("Invalid discoverable mode");
        }
        this.adapter.SetMode(modeStr);
        return true;
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterManufacturer() {
        return this.adapter.GetManufacturer();
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterRevision() {
        return this.adapter.GetRevision();
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterVersion() {
        return this.adapter.GetVersion();
    }

    @Override // org.bluez.BlueZAPI
    public boolean isAdapterPowerOn() {
        return !"off".equals(this.adapter.GetMode());
    }

    private <T extends DBusSignal> void quietRemoveSigHandler(Class<T> type, DBusSigHandler<T> handler) {
        try {
            this.dbusConn.removeSigHandler(type, handler);
        } catch (DBusException e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasBonding(String deviceAddress) {
        try {
            return this.adapter.HasBonding(deviceAddress);
        } catch (Throwable th) {
            return false;
        }
    }

    @Override // org.bluez.BlueZAPI
    public void deviceInquiry(final BlueZAPI.DeviceInquiryListener listener) throws DBusException, InterruptedException {
        final Object discoveryCompletedEvent = new Object();
        DBusSigHandler<Adapter.DiscoveryCompleted> discoveryCompleted = new DBusSigHandler<Adapter.DiscoveryCompleted>() { // from class: org.bluez.v3.BlueZAPIV3.1
            public void handle(Adapter.DiscoveryCompleted s2) {
                DebugLog.debug("discoveryCompleted.handle()");
                synchronized (discoveryCompletedEvent) {
                    discoveryCompletedEvent.notifyAll();
                }
            }
        };
        DBusSigHandler<Adapter.DiscoveryStarted> discoveryStarted = new DBusSigHandler<Adapter.DiscoveryStarted>() { // from class: org.bluez.v3.BlueZAPIV3.2
            public void handle(Adapter.DiscoveryStarted s2) {
                DebugLog.debug("device discovery procedure has been started.");
            }
        };
        DBusSigHandler<Adapter.RemoteDeviceFound> remoteDeviceFound = new DBusSigHandler<Adapter.RemoteDeviceFound>() { // from class: org.bluez.v3.BlueZAPIV3.3
            public void handle(Adapter.RemoteDeviceFound s2) {
                listener.deviceDiscovered(s2.getDeviceAddress(), null, s2.getDeviceClass().intValue(), BlueZAPIV3.this.hasBonding(s2.getDeviceAddress()));
            }
        };
        DBusSigHandler<Adapter.RemoteNameUpdated> remoteNameUpdated = new DBusSigHandler<Adapter.RemoteNameUpdated>() { // from class: org.bluez.v3.BlueZAPIV3.4
            public void handle(Adapter.RemoteNameUpdated s2) {
                listener.deviceDiscovered(s2.getDeviceAddress(), s2.getDeviceName(), -1, false);
            }
        };
        DBusSigHandler<Adapter.RemoteClassUpdated> remoteClassUpdated = new DBusSigHandler<Adapter.RemoteClassUpdated>() { // from class: org.bluez.v3.BlueZAPIV3.5
            public void handle(Adapter.RemoteClassUpdated s2) {
                listener.deviceDiscovered(s2.getDeviceAddress(), null, s2.getDeviceClass().intValue(), BlueZAPIV3.this.hasBonding(s2.getDeviceAddress()));
            }
        };
        try {
            this.dbusConn.addSigHandler(Adapter.DiscoveryCompleted.class, discoveryCompleted);
            this.dbusConn.addSigHandler(Adapter.DiscoveryStarted.class, discoveryStarted);
            this.dbusConn.addSigHandler(Adapter.RemoteDeviceFound.class, remoteDeviceFound);
            this.dbusConn.addSigHandler(Adapter.RemoteNameUpdated.class, remoteNameUpdated);
            this.dbusConn.addSigHandler(Adapter.RemoteClassUpdated.class, remoteClassUpdated);
            long sinceDiscoveryLast = System.currentTimeMillis() - this.lastDeviceDiscoveryTime;
            if (sinceDiscoveryLast < MarlinConst.statDump) {
                Thread.sleep(MarlinConst.statDump - sinceDiscoveryLast);
            }
            synchronized (discoveryCompletedEvent) {
                this.adapter.DiscoverDevices();
                listener.deviceInquiryStarted();
                DebugLog.debug("wait for device inquiry to complete...");
                discoveryCompletedEvent.wait();
            }
        } finally {
            quietRemoveSigHandler(Adapter.RemoteClassUpdated.class, remoteClassUpdated);
            quietRemoveSigHandler(Adapter.RemoteNameUpdated.class, remoteNameUpdated);
            quietRemoveSigHandler(Adapter.RemoteDeviceFound.class, remoteDeviceFound);
            quietRemoveSigHandler(Adapter.DiscoveryStarted.class, discoveryStarted);
            quietRemoveSigHandler(Adapter.DiscoveryCompleted.class, discoveryCompleted);
        }
    }

    @Override // org.bluez.BlueZAPI
    public void deviceInquiryCancel() throws DBusException {
        this.adapter.CancelDiscovery();
    }

    @Override // org.bluez.BlueZAPI
    public String getRemoteDeviceFriendlyName(final String deviceAddress) throws DBusException, IOException {
        String str;
        final Object discoveryCompletedEvent = new Object();
        final Vector<String> namesFound = new Vector<>();
        DBusSigHandler<Adapter.DiscoveryCompleted> discoveryCompleted = new DBusSigHandler<Adapter.DiscoveryCompleted>() { // from class: org.bluez.v3.BlueZAPIV3.6
            public void handle(Adapter.DiscoveryCompleted s2) {
                DebugLog.debug("discoveryCompleted.handle()");
                synchronized (discoveryCompletedEvent) {
                    discoveryCompletedEvent.notifyAll();
                }
            }
        };
        DBusSigHandler<Adapter.RemoteNameUpdated> remoteNameUpdated = new DBusSigHandler<Adapter.RemoteNameUpdated>() { // from class: org.bluez.v3.BlueZAPIV3.7
            public void handle(Adapter.RemoteNameUpdated s2) {
                if (deviceAddress.equals(s2.getDeviceAddress())) {
                    if (s2.getDeviceName() != null) {
                        namesFound.add(s2.getDeviceName());
                        synchronized (discoveryCompletedEvent) {
                            discoveryCompletedEvent.notifyAll();
                        }
                        return;
                    }
                    DebugLog.debug("device name is null");
                    return;
                }
                DebugLog.debug("ignore device name " + s2.getDeviceAddress() + " " + s2.getDeviceName());
            }
        };
        try {
            this.dbusConn.addSigHandler(Adapter.DiscoveryCompleted.class, discoveryCompleted);
            this.dbusConn.addSigHandler(Adapter.RemoteNameUpdated.class, remoteNameUpdated);
            synchronized (discoveryCompletedEvent) {
                this.adapter.DiscoverDevices();
                DebugLog.debug("wait for device inquiry to complete...");
                try {
                    discoveryCompletedEvent.wait();
                    DebugLog.debug(namesFound.size() + " device name(s) found");
                    if (namesFound.size() == 0) {
                        throw new IOException("Can't retrive device name");
                    }
                    str = namesFound.get(namesFound.size() - 1);
                } catch (InterruptedException e2) {
                    throw new InterruptedIOException();
                }
            }
            return str;
        } finally {
            quietRemoveSigHandler(Adapter.RemoteNameUpdated.class, remoteNameUpdated);
            quietRemoveSigHandler(Adapter.DiscoveryCompleted.class, discoveryCompleted);
        }
    }

    @Override // org.bluez.BlueZAPI
    public List<String> retrieveDevices(boolean preKnown) {
        if (!preKnown) {
            return null;
        }
        List<String> addresses = new Vector<>();
        String[] bonded = this.adapter.ListBondings();
        if (bonded != null) {
            for (String str : bonded) {
                addresses.add(str);
            }
        }
        String[] trusted = this.adapter.ListTrusts();
        if (trusted != null) {
            for (String str2 : trusted) {
                addresses.add(str2);
            }
        }
        return addresses;
    }

    @Override // org.bluez.BlueZAPI
    public boolean isRemoteDeviceConnected(String deviceAddress) throws DBusException {
        return this.adapter.IsConnected(deviceAddress);
    }

    @Override // org.bluez.BlueZAPI
    public Boolean isRemoteDeviceTrusted(String deviceAddress) throws DBusException {
        return Boolean.valueOf(this.adapter.HasBonding(deviceAddress));
    }

    @Override // org.bluez.BlueZAPI
    public void authenticateRemoteDevice(String deviceAddress) throws DBusException {
        this.adapter.CreateBonding(deviceAddress);
    }

    @Override // org.bluez.BlueZAPI
    public boolean authenticateRemoteDevice(final String deviceAddress, final String passkey) throws DBusException {
        if (passkey == null) {
            authenticateRemoteDevice(deviceAddress);
            return true;
        }
        PasskeyAgent passkeyAgent = new PasskeyAgent() { // from class: org.bluez.v3.BlueZAPIV3.8
            /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
            @Override // org.bluez.v3.PasskeyAgent
            public String Request(String path, String address) {
                if (deviceAddress.equals(address)) {
                    DebugLog.debug("PasskeyAgent.Request");
                    return passkey;
                }
                return "";
            }

            public boolean isRemote() {
                return false;
            }

            @Override // org.bluez.v3.PasskeyAgent
            public void Cancel(String path, String address) {
            }

            @Override // org.bluez.v3.PasskeyAgent
            public void Release() {
            }
        };
        DebugLog.debug("get security on path", this.adapterPath.getPath());
        Security security = (Security) this.dbusConn.getRemoteObject("org.bluez", this.adapterPath.getPath(), Security.class);
        String passkeyAgentPath = "/org/bluecove/authenticate/" + getAdapterID() + "/" + deviceAddress.replace(':', '_');
        DebugLog.debug("export passkeyAgent", passkeyAgentPath);
        this.dbusConn.exportObject(passkeyAgentPath, passkeyAgent);
        boolean useDefaultPasskeyAgentBug = BlueCoveImpl.getConfigProperty("bluecove.bluez.registerDefaultPasskeyAgent", false);
        try {
            if (useDefaultPasskeyAgentBug) {
                security.RegisterDefaultPasskeyAgent(passkeyAgentPath);
            } else {
                security.RegisterPasskeyAgent(passkeyAgentPath, deviceAddress);
            }
            this.adapter.CreateBonding(deviceAddress);
            return true;
        } finally {
            try {
                if (useDefaultPasskeyAgentBug) {
                    security.UnregisterDefaultPasskeyAgent(passkeyAgentPath);
                } else {
                    security.UnregisterPasskeyAgent(passkeyAgentPath, deviceAddress);
                }
            } catch (DBusExecutionException e2) {
            }
            this.dbusConn.unExportObject(passkeyAgentPath);
        }
    }

    @Override // org.bluez.BlueZAPI
    public void removeAuthenticationWithRemoteDevice(String deviceAddress) throws DBusException {
        this.adapter.RemoveBonding(deviceAddress);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.freedesktop.dbus.exceptions.DBusException */
    @Override // org.bluez.BlueZAPI
    public Map<Integer, String> getRemoteDeviceServices(String deviceAddress) throws DBusException {
        try {
            UInt32[] serviceHandles = this.adapter.GetRemoteServiceHandles(deviceAddress, "");
            if (serviceHandles == null) {
                throw new DBusException("Recived no records");
            }
            Map<Integer, String> xmlRecords = new HashMap<>();
            for (int i2 = 0; i2 < serviceHandles.length; i2++) {
                xmlRecords.put(Integer.valueOf(serviceHandles[i2].intValue()), this.adapter.GetRemoteServiceRecordAsXML(deviceAddress, serviceHandles[i2]));
            }
            return xmlRecords;
        } catch (DBus.Error.NoReply e2) {
            return null;
        }
    }

    private Database getSDPService() throws DBusException {
        return (Database) this.dbusConn.getRemoteObject("org.bluez", "/org/bluez", Database.class);
    }

    @Override // org.bluez.BlueZAPI
    public long registerSDPRecord(String sdpXML) throws DBusException {
        DebugLog.debug("AddServiceRecordFromXML", sdpXML);
        UInt32 handle = getSDPService().AddServiceRecordFromXML(sdpXML);
        return handle.longValue();
    }

    @Override // org.bluez.BlueZAPI
    public void updateSDPRecord(long handle, String sdpXML) throws DBusException {
        DebugLog.debug("UpdateServiceRecordFromXML", sdpXML);
        getSDPService().UpdateServiceRecordFromXML(new UInt32(handle), sdpXML);
    }

    @Override // org.bluez.BlueZAPI
    public void unregisterSDPRecord(long handle) throws DBusException {
        DebugLog.debug("RemoveServiceRecord", handle);
        getSDPService().RemoveServiceRecord(new UInt32(handle));
    }
}
