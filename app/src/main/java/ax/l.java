package aX;

import bH.C;
import java.io.IOException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;

/* loaded from: TunerStudioMS.jar:aX/l.class */
class l extends aC.d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f4024a;

    l(h hVar) {
        this.f4024a = hVar;
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        C.c("Device " + remoteDevice.getBluetoothAddress() + " found. Authenticated: " + remoteDevice.isAuthenticated() + ". Trusted: " + remoteDevice.isTrustedDevice());
        try {
            String friendlyName = remoteDevice.getFriendlyName(false);
            if (friendlyName == null || friendlyName.isEmpty()) {
                C.c("Friendly Name: " + remoteDevice.getFriendlyName(true));
            }
        } catch (IOException e2) {
        }
        m mVar = new m(this.f4024a, remoteDevice);
        if (remoteDevice.isTrustedDevice()) {
            this.f4024a.f4014b.addElement(mVar);
            this.f4024a.f4013a.repaint();
        } else {
            this.f4024a.f4016d.addElement(mVar);
            this.f4024a.f4015c.repaint();
        }
    }
}
