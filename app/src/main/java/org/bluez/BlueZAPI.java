package org.bluez;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.exceptions.DBusExecutionException;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/BlueZAPI.class */
public interface BlueZAPI {

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/BlueZAPI$DeviceInquiryListener.class */
    public interface DeviceInquiryListener {
        void deviceInquiryStarted();

        void deviceDiscovered(String str, String str2, int i2, boolean z2);
    }

    List<String> listAdapters();

    Path getAdapter(int i2);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    Path findAdapter(String str);

    /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$InvalidArguments] */
    Path defaultAdapter();

    void selectAdapter(Path path) throws DBusException;

    String getAdapterID();

    String getAdapterAddress();

    int getAdapterDeviceClass();

    String getAdapterName();

    boolean isAdapterDiscoverable();

    int getAdapterDiscoverableTimeout();

    String getAdapterVersion();

    String getAdapterRevision();

    String getAdapterManufacturer();

    boolean isAdapterPowerOn();

    boolean setAdapterDiscoverable(int i2) throws DBusException;

    void deviceInquiry(DeviceInquiryListener deviceInquiryListener) throws DBusException, InterruptedException;

    void deviceInquiryCancel() throws DBusException;

    String getRemoteDeviceFriendlyName(String str) throws DBusException, IOException;

    List<String> retrieveDevices(boolean z2);

    boolean isRemoteDeviceConnected(String str) throws DBusException;

    Boolean isRemoteDeviceTrusted(String str) throws DBusException;

    Map<Integer, String> getRemoteDeviceServices(String str) throws DBusException;

    void authenticateRemoteDevice(String str) throws DBusException;

    boolean authenticateRemoteDevice(String str, String str2) throws DBusException;

    void removeAuthenticationWithRemoteDevice(String str) throws DBusException;

    long registerSDPRecord(String str) throws DBusException, DBusExecutionException;

    void updateSDPRecord(long j2, String str) throws DBusException, DBusExecutionException;

    void unregisterSDPRecord(long j2) throws DBusException, DBusExecutionException;
}
