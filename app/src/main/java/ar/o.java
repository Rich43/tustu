package aR;

import aP.C0338f;
import aP.C0404hl;
import d.InterfaceC1711c;
import java.util.Properties;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aR/o.class */
public class o implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return "Show Action Management Dialog";
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
        if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            C0338f.a().O();
        } else {
            C0404hl.a().a("cannot display Action Manager settings.");
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "showActionManagement";
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
