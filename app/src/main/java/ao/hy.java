package ao;

import javax.swing.JOptionPane;

/* loaded from: TunerStudioMS.jar:ao/hy.class */
class hy implements gN {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hn f6146a;

    hy(hn hnVar) {
        this.f6146a = hnVar;
    }

    @Override // ao.gN
    public void a(String str, String str2) {
        if (str2 == null || str2.equals("")) {
            h.i.d(str);
        } else {
            h.i.c(str, str2);
        }
        if (0 == JOptionPane.showConfirmDialog(this.f6146a.f6122k, "Must reload Tune Settings to take effect.\nReload now?", "Reload Tune Settings?", 0, 3)) {
            this.f6146a.l();
            this.f6146a.d();
        }
    }

    @Override // ao.gN
    public String a(String str) {
        return h.i.e(str, "");
    }
}
