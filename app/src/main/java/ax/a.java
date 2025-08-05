package aX;

import A.s;
import aP.cZ;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.fK;
import com.efiAnalytics.ui.fR;
import java.awt.Container;
import java.awt.Window;
import javax.bluetooth.RemoteDevice;
import javax.swing.JDialog;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aX/a.class */
public class a implements fR {

    /* renamed from: a, reason: collision with root package name */
    fK f3995a = new fK();

    /* renamed from: e, reason: collision with root package name */
    private aW.a f3996e = null;

    /* renamed from: b, reason: collision with root package name */
    JDialog f3997b = null;

    /* renamed from: c, reason: collision with root package name */
    h f3998c = new h();

    /* renamed from: d, reason: collision with root package name */
    c f3999d = new c();

    public a() {
        this.f3995a.e(this.f3998c);
        this.f3998c.a();
        this.f3995a.e(this.f3999d);
        this.f3995a.a(this);
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() {
        if (this.f3996e == null) {
            return true;
        }
        this.f3996e.a();
        if ((this.f3996e.b() instanceof aC.a) && this.f3998c.d() != null) {
            try {
                ((aC.a) this.f3996e.b()).a("Bluetooth Device", this.f3998c.d().getBluetoothAddress());
            } catch (s e2) {
            }
        }
        this.f3996e.a();
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
        this.f3998c.c();
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean a(Container container) {
        if (!container.equals(this.f3998c)) {
            if (!container.equals(this.f3999d)) {
                return true;
            }
            if (!this.f3999d.c()) {
                this.f3999d.a();
            }
            return this.f3999d.c();
        }
        this.f3998c.c();
        RemoteDevice remoteDeviceD = this.f3998c.d();
        if (remoteDeviceD == null) {
            bV.d(C1818g.b("You must select an Un-Paired Device."), container);
            return false;
        }
        this.f3999d.a(remoteDeviceD);
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b(Container container) {
        if (!container.equals(this.f3998c)) {
            return true;
        }
        this.f3998c.a();
        return true;
    }

    public void a(aW.a aVar) {
        this.f3996e = aVar;
    }

    public void a(Window window) {
        this.f3997b = this.f3995a.a(window, C1818g.b("Pair Bluetooth Adapter"));
        cZ.a().a(this.f3997b);
        this.f3997b.setVisible(true);
    }
}
