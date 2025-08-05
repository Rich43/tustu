package I;

import G.C0113cs;
import G.C0132o;
import G.InterfaceC0131n;

/* loaded from: TunerStudioMS.jar:I/i.class */
public class i implements InterfaceC0131n {
    @Override // G.InterfaceC0131n
    public void e() {
        C0113cs.a().a("interrogationProgress", 0.0d);
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        C0113cs.a().a("interrogationProgress", d2 * 100.0d);
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        C0113cs.a().a("interrogationProgress", 100.0d);
    }
}
