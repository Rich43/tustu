package bi;

import G.C0132o;
import G.InterfaceC0131n;

/* renamed from: bi.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bi/e.class */
class C1170e implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1166a f8157a;

    C1170e(C1166a c1166a) {
        this.f8157a = c1166a;
    }

    @Override // G.InterfaceC0131n
    public void e() {
        this.f8157a.f8145b.a("Uploading Replay Data");
        this.f8157a.f8145b.a(0.0d);
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        this.f8157a.f8145b.a(d2);
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        if (c0132o.a() == 1) {
            this.f8157a.f8146c.setEnabled(true);
            this.f8157a.f8147d.setText("Close");
        }
    }
}
