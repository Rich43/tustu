package aC;

import bH.C;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.ServiceRecord;

/* loaded from: TunerStudioMS.jar:aC/d.class */
public abstract class d implements DiscoveryListener {
    @Override // javax.bluetooth.DiscoveryListener
    public void serviceSearchCompleted(int i2, int i3) {
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void servicesDiscovered(int i2, ServiceRecord[] serviceRecordArr) {
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void inquiryCompleted(int i2) {
        C.c("Device Inquiry completed!");
        synchronized (b.f2294a) {
            b.f2294a.notifyAll();
        }
    }
}
