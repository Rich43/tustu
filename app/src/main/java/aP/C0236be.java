package aP;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import s.C1818g;

/* renamed from: aP.be, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/be.class */
class C0236be extends Thread {

    /* renamed from: a, reason: collision with root package name */
    List f3064a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aV f3065b;

    C0236be(aV aVVar, List list) {
        this.f3065b = aVVar;
        this.f3064a = list;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            A.j.a().c();
            A.j.a().c("Scanning for unpaired EFI Analytics Bluetooth Adapters");
            bH.C.d("Searching for unpaired devices.");
            List<RemoteDevice> listC = aC.b.c();
            if (listC.isEmpty()) {
                bH.C.d("Found no unpaired devices.");
            } else {
                bH.C.d("Found " + listC.size() + " unpaired devices.");
                try {
                    A.x xVar = new A.x();
                    xVar.a((aC.a) aV.w.c().a(aC.a.f2289e, "DEFAULT_INSTANCE"));
                    for (RemoteDevice remoteDevice : listC) {
                        this.f3065b.f2859e.setText(C1818g.b("Attempting to Pair with") + " " + aC.b.a(remoteDevice));
                        if (aC.b.a(remoteDevice, "1234")) {
                            bH.C.d("Successfully paired with " + aC.b.a(remoteDevice) + ", adding to search list");
                            xVar.a("Bluetooth Device", remoteDevice.getBluetoothAddress());
                            this.f3065b.f2859e.setText(C1818g.b("Successfully Paired with ") + aC.b.a(remoteDevice));
                        } else {
                            bH.C.d("Failed to pair with " + aC.b.a(remoteDevice));
                            this.f3065b.f2859e.setText(C1818g.b("Failed to Pair with") + " " + aC.b.a(remoteDevice));
                        }
                    }
                    aC.a.t();
                    this.f3064a.add(xVar);
                } catch (IllegalAccessException e2) {
                    Logger.getLogger(aV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                } catch (InstantiationException e3) {
                    Logger.getLogger(aV.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        } finally {
            this.f3065b.f2873t = null;
            A.j.a().d();
        }
    }
}
