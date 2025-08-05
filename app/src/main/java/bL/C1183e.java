package bl;

import G.C0072be;
import G.C0079bl;
import G.C0083bp;
import G.C0088bu;
import G.R;
import G.bL;
import bH.C;
import bt.C1324bf;
import bt.aT;
import com.efiAnalytics.plugin.ecu.servers.UiSettingServer;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;

/* renamed from: bl.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/e.class */
public class C1183e implements UiSettingServer {

    /* renamed from: a, reason: collision with root package name */
    R f8242a;

    /* renamed from: b, reason: collision with root package name */
    List f8243b = null;

    /* renamed from: c, reason: collision with root package name */
    List f8244c = null;

    /* renamed from: d, reason: collision with root package name */
    List f8245d = null;

    public C1183e(R r2) {
        this.f8242a = null;
        this.f8242a = r2;
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.UiSettingServer
    public JComponent getUiComponent(String str) {
        C0083bp c0083bpD;
        if (this.f8242a.c(str) != null && (c0083bpD = bL.d(this.f8242a, str)) != null) {
            return new aT(this.f8242a, c0083bpD);
        }
        if (this.f8242a.e().c(str) != null) {
            return new C1324bf(this.f8242a, this.f8242a.e().c(str));
        }
        C.d("UiSettingServer:: UiComponent not found in Configuration: " + this.f8242a.c());
        return null;
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.UiSettingServer
    public void disposeUiComponent(Component component) {
        a(component);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(Component component) {
        if (component instanceof InterfaceC1565bc) {
            ((InterfaceC1565bc) component).close();
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
                a(container.getComponent(i2));
            }
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.UiSettingServer
    public List getUiPanelNames() {
        if (this.f8243b == null) {
            this.f8243b = new ArrayList();
            Iterator itC = this.f8242a.e().c();
            while (itC.hasNext()) {
                C0088bu c0088bu = (C0088bu) itC.next();
                if (!(c0088bu instanceof C0072be) && !(c0088bu instanceof C0079bl)) {
                    this.f8243b.add(c0088bu.aJ());
                }
            }
        }
        return this.f8243b;
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.UiSettingServer
    public List getUiCurves() {
        if (this.f8245d == null) {
            this.f8245d = new ArrayList();
            Iterator itC = this.f8242a.e().c();
            while (itC.hasNext()) {
                C0088bu c0088bu = (C0088bu) itC.next();
                if (c0088bu instanceof C0079bl) {
                    this.f8245d.add(C1185g.a((C0079bl) c0088bu));
                }
            }
        }
        return this.f8245d;
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.UiSettingServer
    public List getUiTable() {
        if (this.f8244c == null) {
            this.f8244c = new ArrayList();
            Iterator itN = this.f8242a.n();
            while (itN.hasNext()) {
                this.f8244c.add(C1194p.a((C0072be) itN.next()));
            }
        }
        return this.f8244c;
    }
}
