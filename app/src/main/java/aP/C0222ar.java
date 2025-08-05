package aP;

import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1650eh;
import java.awt.Component;
import javax.swing.DefaultListModel;

/* renamed from: aP.ar, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ar.class */
class C0222ar implements InterfaceC1650eh {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0220ap f2928a;

    C0222ar(C0220ap c0220ap) {
        this.f2928a = c0220ap;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1650eh
    public void a(DefaultListModel defaultListModel) {
        aE.d dVar = new aE.d();
        dVar.a("CAN" + (this.f2928a.f2922a.a().length + 1));
        dVar.b("My CAN Controller");
        this.f2928a.f2922a.a(dVar);
        this.f2928a.f2922a.b(dVar);
        this.f2928a.f2925d.a(dVar);
        this.f2928a.f2925d.a(true);
        C1685fp.a((Component) this.f2928a.f2925d, true);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1650eh
    public boolean a(Object obj, int i2) {
        this.f2928a.f2925d.validate();
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1650eh
    public boolean a(Object obj, int i2, int i3) {
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1650eh
    public void a(Object obj) {
        this.f2928a.f2925d.a((aE.d) obj);
        this.f2928a.f2925d.setEnabled(true);
        if (this.f2928a.getParent() != null) {
            this.f2928a.getParent().validate();
        }
        this.f2928a.f2925d.a(false);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1650eh
    public void b(Object obj) {
        this.f2928a.f2925d.setEnabled(false);
        this.f2928a.f2925d.a();
        this.f2928a.f2925d.c();
        this.f2928a.f2925d.validate();
        if (this.f2928a.getParent() != null) {
            this.f2928a.getParent().validate();
        }
    }
}
