package aR;

import G.T;
import aP.C0338f;
import aP.C0404hl;
import aP.cZ;
import d.InterfaceC1711c;
import java.awt.Frame;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:aR/p.class */
public class p implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return "Show Communications Dialog";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Project";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        if (T.a().c() != null) {
            C0338f.a().e((Frame) cZ.a().c());
        } else {
            C0404hl.a().a("No Project open, cannot display Comms settings.");
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "showCommsSettings";
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) {
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        return null;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return false;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return false;
    }
}
