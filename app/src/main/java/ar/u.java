package aR;

import aP.C0338f;
import d.InterfaceC1711c;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:aR/u.class */
public class u implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return "Exit and Shutdown";
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Dashboard Functions";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        try {
            C0338f.a().t();
        } catch (Exception e2) {
            throw new d.e(e2.getLocalizedMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "exitAndShutdown";
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
