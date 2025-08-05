package aR;

import aP.C0338f;
import aP.cZ;
import d.InterfaceC1711c;
import java.awt.Window;
import java.util.Properties;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aR/z.class */
public class z implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return C1818g.b("Stop Data Logging");
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Data Logging";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return aE.a.A() != null;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        C0338f.a().g((Window) cZ.a().c());
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "stopDatalogging";
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
        return true;
    }
}
