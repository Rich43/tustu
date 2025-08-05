package org.bluez.v4;

import com.intel.bluetooth.DebugLog;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.bluez.BlueZAPI;
import org.bluez.Error;
import org.bluez.dbus.DBusProperties;
import org.bluez.v4.Adapter;
import org.bluez.v4.Device;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/v4/BlueZAPIV4.class */
public class BlueZAPIV4 implements BlueZAPI {
    private DBusConnection dbusConn;
    private Manager dbusManager;
    private Adapter adapter;
    private Path adapterPath;

    public BlueZAPIV4(DBusConnection dbusConn, Manager dbusManager) {
        this.dbusConn = dbusConn;
        this.dbusManager = dbusManager;
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    @Override // org.bluez.BlueZAPI
    public Path findAdapter(String pattern) {
        try {
            return this.dbusManager.FindAdapter(pattern);
        } catch (Error.NoSuchAdapter e2) {
            return null;
        }
    }

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    @Override // org.bluez.BlueZAPI
    public Path defaultAdapter() {
        try {
            return this.dbusManager.DefaultAdapter();
        } catch (Error.NoSuchAdapter e2) {
            return null;
        }
    }

    @Override // org.bluez.BlueZAPI
    public Path getAdapter(int number) {
        Path[] adapters = this.dbusManager.ListAdapters();
        if (adapters == null) {
            throw null;
        }
        if (number < 0 || number >= adapters.length) {
            throw null;
        }
        return adapters[number];
    }

    private String hciID(String adapterPath) {
        String path;
        if (adapterPath.startsWith("/org/bluez/")) {
            path = adapterPath.substring("/org/bluez/".length());
        } else {
            path = adapterPath;
        }
        int lastpart = path.lastIndexOf(47);
        if (lastpart != -1 && lastpart != path.length() - 1) {
            return path.substring(lastpart + 1);
        }
        return path;
    }

    @Override // org.bluez.BlueZAPI
    public List<String> listAdapters() {
        List<String> v2 = new Vector<>();
        Path[] adapters = this.dbusManager.ListAdapters();
        if (adapters != null) {
            for (Path path : adapters) {
                v2.add(hciID(path.getPath()));
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
        return DBusProperties.getStringValue(this.adapter, Adapter.Properties.Address);
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterID() {
        return hciID(this.adapterPath.getPath());
    }

    @Override // org.bluez.BlueZAPI
    public int getAdapterDeviceClass() {
        Integer deviceClass = DBusProperties.getIntValue(this.adapter, Adapter.Properties.Class);
        if (deviceClass == null) {
            return 256;
        }
        return deviceClass.intValue();
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterName() {
        return DBusProperties.getStringValue(this.adapter, Adapter.Properties.Name);
    }

    @Override // org.bluez.BlueZAPI
    public boolean isAdapterDiscoverable() {
        return DBusProperties.getBooleanValue(this.adapter, Adapter.Properties.Discoverable);
    }

    @Override // org.bluez.BlueZAPI
    public int getAdapterDiscoverableTimeout() {
        return DBusProperties.getIntValue(this.adapter, Adapter.Properties.DiscoverableTimeout).intValue();
    }

    @Override // org.bluez.BlueZAPI
    public boolean setAdapterDiscoverable(int mode) throws DBusException {
        switch (mode) {
            case 0:
                this.adapter.SetProperty(DBusProperties.getPropertyName(Adapter.Properties.Discoverable), new Variant<>(Boolean.FALSE));
                return true;
            case 10390272:
                this.adapter.SetProperty(DBusProperties.getPropertyName(Adapter.Properties.DiscoverableTimeout), new Variant<>(new UInt32(180L)));
                this.adapter.SetProperty(DBusProperties.getPropertyName(Adapter.Properties.Discoverable), new Variant<>(Boolean.TRUE));
                return true;
            case 10390323:
                this.adapter.SetProperty(DBusProperties.getPropertyName(Adapter.Properties.DiscoverableTimeout), new Variant<>(new UInt32(0L)));
                this.adapter.SetProperty(DBusProperties.getPropertyName(Adapter.Properties.Discoverable), new Variant<>(Boolean.TRUE));
                return true;
            default:
                if (10390272 <= mode && mode <= 10390335) {
                    return false;
                }
                throw new IllegalArgumentException("Invalid discoverable mode");
        }
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterManufacturer() {
        return null;
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterRevision() {
        return null;
    }

    @Override // org.bluez.BlueZAPI
    public String getAdapterVersion() {
        return null;
    }

    @Override // org.bluez.BlueZAPI
    public boolean isAdapterPowerOn() {
        return DBusProperties.getBooleanValue(this.adapter, Adapter.Properties.Powered);
    }

    private <T extends DBusSignal> void quietRemoveSigHandler(Class<T> type, DBusSigHandler<T> handler) {
        try {
            this.dbusConn.removeSigHandler(type, handler);
        } catch (DBusException e2) {
        }
    }

    @Override // org.bluez.BlueZAPI
    public void deviceInquiry(final BlueZAPI.DeviceInquiryListener listener) throws DBusException, InterruptedException {
        DBusSigHandler<Adapter.DeviceFound> remoteDeviceFound = new DBusSigHandler<Adapter.DeviceFound>() { // from class: org.bluez.v4.BlueZAPIV4.1
            public void handle(Adapter.DeviceFound s2) {
                String deviceName = null;
                int deviceClass = -1;
                boolean paired = false;
                Map<String, Variant<?>> properties = s2.getDevicePoperties();
                if (properties != null) {
                    deviceName = DBusProperties.getStringValue(properties, Device.Properties.Name);
                    deviceClass = DBusProperties.getIntValue(properties, Device.Properties.Class).intValue();
                    paired = DBusProperties.getBooleanValue(properties, Device.Properties.Paired, false);
                }
                listener.deviceDiscovered(s2.getDeviceAddress(), deviceName, deviceClass, paired);
            }
        };
        try {
            this.dbusConn.addSigHandler(Adapter.DeviceFound.class, remoteDeviceFound);
            this.adapter.StartDiscovery();
            listener.deviceInquiryStarted();
            while (DBusProperties.getBooleanValue(this.adapter, Adapter.Properties.Discovering)) {
                Thread.sleep(200L);
            }
            this.adapter.StopDiscovery();
            quietRemoveSigHandler(Adapter.DeviceFound.class, remoteDeviceFound);
        } catch (Throwable th) {
            quietRemoveSigHandler(Adapter.DeviceFound.class, remoteDeviceFound);
            throw th;
        }
    }

    @Override // org.bluez.BlueZAPI
    public void deviceInquiryCancel() throws DBusException {
        this.adapter.StopDiscovery();
    }

    private Device getDevice(String deviceAddress) throws DBusException {
        Path devicePath;
        try {
            devicePath = this.adapter.FindDevice(deviceAddress);
        } catch (Error.DoesNotExist e2) {
            DebugLog.debug("can't get device", (Throwable) e2);
            devicePath = this.adapter.CreateDevice(deviceAddress);
        }
        return (Device) this.dbusConn.getRemoteObject("org.bluez", devicePath.getPath(), Device.class);
    }

    @Override // org.bluez.BlueZAPI
    public String getRemoteDeviceFriendlyName(String deviceAddress) throws DBusException, IOException {
        return DBusProperties.getStringValue(getDevice(deviceAddress), Device.Properties.Name);
    }

    @Override // org.bluez.BlueZAPI
    public List<String> retrieveDevices(boolean preKnown) {
        Path[] devices = this.adapter.ListDevices();
        List<String> addresses = new Vector<>();
        if (devices != null) {
            for (Path devicePath : devices) {
                try {
                    Device device = (Device) this.dbusConn.getRemoteObject("org.bluez", devicePath.getPath(), Device.class);
                    Map<String, Variant<?>> properties = device.GetProperties();
                    if (properties != null) {
                        String address = DBusProperties.getStringValue(properties, Device.Properties.Address);
                        boolean paired = DBusProperties.getBooleanValue(properties, Device.Properties.Paired, false);
                        boolean trusted = DBusProperties.getBooleanValue(properties, Device.Properties.Trusted, false);
                        if (!preKnown || paired || trusted) {
                            addresses.add(address);
                        }
                    }
                } catch (DBusException e2) {
                    DebugLog.debug("can't get device " + ((Object) devicePath), (Throwable) e2);
                }
            }
        }
        return addresses;
    }

    @Override // org.bluez.BlueZAPI
    public boolean isRemoteDeviceConnected(String deviceAddress) throws DBusException {
        return DBusProperties.getBooleanValue(getDevice(deviceAddress), Device.Properties.Connected);
    }

    @Override // org.bluez.BlueZAPI
    public Boolean isRemoteDeviceTrusted(String deviceAddress) throws DBusException {
        return Boolean.valueOf(DBusProperties.getBooleanValue(getDevice(deviceAddress), Device.Properties.Paired));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.freedesktop.dbus.exceptions.DBusException */
    @Override // org.bluez.BlueZAPI
    public void authenticateRemoteDevice(String deviceAddress) throws DBusException {
        throw new DBusException("TODO: How to implement this using Agent?");
    }

    @Override // org.bluez.BlueZAPI
    public boolean authenticateRemoteDevice(String deviceAddress, final String passkey) throws DBusException {
        if (passkey == null) {
            authenticateRemoteDevice(deviceAddress);
            return true;
        }
        Agent agent = new Agent() { // from class: org.bluez.v4.BlueZAPIV4.2
            /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
            @Override // org.bluez.v4.Agent
            public void Authorize(Path device, String uuid) {
            }

            /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
            @Override // org.bluez.v4.Agent
            public void ConfirmModeChange(String mode) {
            }

            @Override // org.bluez.v4.Agent
            public void DisplayPasskey(Path device, UInt32 passkey2, byte entered) {
            }

            /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
            @Override // org.bluez.v4.Agent
            public void RequestConfirmation(Path device, UInt32 passkey2) {
            }

            /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
            @Override // org.bluez.v4.Agent
            public UInt32 RequestPasskey(Path device) {
                return null;
            }

            /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$Rejected, org.bluez.Error$Canceled] */
            @Override // org.bluez.v4.Agent
            public String RequestPinCode(Path device) {
                return passkey;
            }

            @Override // org.bluez.v4.Agent
            public void Cancel() {
            }

            @Override // org.bluez.v4.Agent
            public void Release() {
            }

            public boolean isRemote() {
                return false;
            }
        };
        String agentPath = "/org/bluecove/authenticate/" + getAdapterID() + "/" + deviceAddress.replace(':', '_');
        DebugLog.debug("export Agent", agentPath);
        this.dbusConn.exportObject(agentPath, agent);
        try {
            this.adapter.CreatePairedDevice(deviceAddress, new Path(agentPath), "");
            this.dbusConn.unExportObject(agentPath);
            return true;
        } catch (Throwable th) {
            this.dbusConn.unExportObject(agentPath);
            throw th;
        }
    }

    @Override // org.bluez.BlueZAPI
    public void removeAuthenticationWithRemoteDevice(String deviceAddress) throws DBusException {
        Path devicePath = this.adapter.FindDevice(deviceAddress);
        this.adapter.RemoveDevice(devicePath);
    }

    @Override // org.bluez.BlueZAPI
    public Map<Integer, String> getRemoteDeviceServices(String deviceAddress) throws DBusException {
        Path devicePath;
        try {
            devicePath = this.adapter.FindDevice(deviceAddress);
        } catch (Error.DoesNotExist e2) {
            devicePath = this.adapter.CreateDevice(deviceAddress);
        }
        Device device = (Device) this.dbusConn.getRemoteObject("org.bluez", devicePath.getPath(), Device.class);
        Map<UInt32, String> xmlMap = device.DiscoverServices("");
        Map<Integer, String> xmlRecords = new HashMap<>();
        for (Map.Entry<UInt32, String> record : xmlMap.entrySet()) {
            xmlRecords.put(Integer.valueOf(record.getKey().intValue()), record.getValue());
        }
        return xmlRecords;
    }

    private Service getSDPService() throws DBusException {
        return (Service) this.dbusConn.getRemoteObject("org.bluez", this.adapterPath.getPath(), Service.class);
    }

    @Override // org.bluez.BlueZAPI
    public long registerSDPRecord(String sdpXML) throws DBusException {
        DebugLog.debug("AddRecord", sdpXML);
        UInt32 handle = getSDPService().AddRecord(sdpXML);
        return handle.longValue();
    }

    @Override // org.bluez.BlueZAPI
    public void updateSDPRecord(long handle, String sdpXML) throws DBusException {
        DebugLog.debug("UpdateRecord", sdpXML);
        getSDPService().UpdateRecord(new UInt32(handle), sdpXML);
    }

    @Override // org.bluez.BlueZAPI
    public void unregisterSDPRecord(long handle) throws DBusException {
        DebugLog.debug("RemoveRecord", handle);
        getSDPService().RemoveRecord(new UInt32(handle));
    }
}
