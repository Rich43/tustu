package aX;

import com.efiAnalytics.ui.bV;
import java.io.IOException;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aX/k.class */
class k implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f4023a;

    k(h hVar) {
        this.f4023a = hVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        String friendlyName;
        this.f4023a.f4017e.setEnabled(true);
        this.f4023a.f4018f.c();
        if (this.f4023a.f4016d.isEmpty()) {
            bV.d(C1818g.b("No Un-Paired Bluetooth Adapters Found!"), this.f4023a.f4017e);
            return;
        }
        for (int i2 = 0; i2 < this.f4023a.f4016d.size(); i2++) {
            try {
                friendlyName = ((m) this.f4023a.f4016d.elementAt(i2)).a().getFriendlyName(false);
            } catch (IOException e2) {
            }
            if (friendlyName != null && (friendlyName.toLowerCase().startsWith("efi anal") || friendlyName.toLowerCase().startsWith("efianal"))) {
                this.f4023a.f4015c.setSelectedIndex(i2);
                return;
            }
        }
    }
}
