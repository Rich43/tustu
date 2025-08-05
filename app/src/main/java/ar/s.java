package aR;

import G.R;
import G.T;
import G.aA;
import G.bL;
import aP.C0338f;
import aP.cZ;
import bt.C1285M;
import com.efiAnalytics.ui.cY;
import d.InterfaceC1711c;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aR/s.class */
public class s implements cY, InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    d.k f3876a = new d.k();

    /* renamed from: b, reason: collision with root package name */
    d.i f3877b;

    public s() {
        new d.i("ecuConfig", "").a(0);
        this.f3877b = new d.i("settingsPanelName", "");
        this.f3877b.a(0);
        this.f3876a.add(this.f3877b);
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return "Show Settings Dialog";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Settings Dialogs";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return aE.a.A() != null;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty("settingsPanelName");
        if (property == null || property.isEmpty()) {
            throw new d.e("Settings panel name is a required parameter for Show settings Dialog action.");
        }
        String property2 = properties.getProperty("ecuConfig");
        R rC = (property2 == null || property2.isEmpty()) ? T.a().c() : T.a().c(property2);
        C0338f.a().a(rC != null ? rC.c() + "." + property : property, "0", cZ.a().c());
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "showSettingsDialog";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty("settingsPanelName");
        if (property == null || property.isEmpty()) {
            throw new d.e("Settings panel name is a required parameter for Show settings Dialog action.");
        }
        String property2 = properties.getProperty("ecuConfig");
        R rC = (property2 == null || property2.isEmpty()) ? T.a().c() : T.a().c(property2);
        if (rC == null) {
            throw new d.e("EcuConfiguration not found with the provided name.");
        }
        if (rC.e().c(property) == null) {
            throw new d.e("EcuConfiguration not found with the provided name.");
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        ArrayList arrayList = new ArrayList();
        for (aA aAVar : bL.a(T.a().c())) {
            arrayList.add(new d.h(aAVar.d(), aAVar.e()));
        }
        this.f3877b.a(arrayList);
        this.f3877b.c("Select the Dialog you want to open when this action is triggered.");
        return this.f3876a;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // com.efiAnalytics.ui.cY
    public List a(ActionListener actionListener) {
        ArrayList arrayList = new ArrayList();
        String strC = T.a().c().c();
        C1285M c1285m = new C1285M(T.a().c(), -1);
        t tVar = new t(this, strC, actionListener);
        c1285m.setText(C1818g.b(b()));
        c1285m.addActionListener(tVar);
        arrayList.add(c1285m);
        return arrayList;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
