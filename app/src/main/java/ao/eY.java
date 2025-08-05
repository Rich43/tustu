package ao;

import g.C1727e;
import g.C1733k;
import g.InterfaceC1735m;
import k.C1756d;

/* loaded from: TunerStudioMS.jar:ao/eY.class */
class eY implements InterfaceC1735m {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5605a;

    eY(C0737eu c0737eu) {
        this.f5605a = c0737eu;
    }

    @Override // g.InterfaceC1735m
    public boolean a(String str) {
        if (str.equals("")) {
            return true;
        }
        try {
            C1756d.a().a(C1727e.b(this.f5605a.f5676l, str, 20)).d();
            return true;
        } catch (Exception e2) {
            C1733k.a("Invalid condition:" + str + "\nPlease check your syntax.\nClear field and press Ok to disable custom condition", this.f5605a.getParent());
            return false;
        }
    }
}
