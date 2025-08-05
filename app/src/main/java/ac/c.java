package aC;

import java.io.IOException;
import java.util.List;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;

/* loaded from: TunerStudioMS.jar:aC/c.class */
final class c extends d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ List f2295a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ boolean f2296b;

    c(List list, boolean z2) {
        this.f2295a = list;
        this.f2296b = z2;
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        System.out.println("Device " + remoteDevice.getBluetoothAddress() + " found. Authenticated: " + remoteDevice.isAuthenticated() + ". Trusted: " + remoteDevice.isTrustedDevice());
        this.f2295a.add(remoteDevice);
        try {
            String friendlyName = remoteDevice.getFriendlyName(false);
            if (friendlyName == null || friendlyName.isEmpty() || this.f2296b) {
                friendlyName = remoteDevice.getFriendlyName(true);
            }
            System.out.println("     name " + friendlyName);
        } catch (IOException e2) {
            System.out.println("Failed to get friendly name");
        }
    }
}
