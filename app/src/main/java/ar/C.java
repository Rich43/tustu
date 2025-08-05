package aR;

import aP.C0338f;
import aP.C0404hl;
import aP.cZ;
import d.InterfaceC1711c;
import java.awt.Window;
import java.util.Properties;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aR/C.class */
public class C implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3852a = "silent";

    @Override // d.InterfaceC1711c
    public String b() {
        return C1818g.b("Toggle Data Logging");
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Data Logging";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) {
        try {
            if (ac.r.a()) {
                C0338f.a().g((Window) cZ.a().c());
            } else {
                C0338f.a().o();
            }
        } catch (V.a e2) {
            C0404hl.a().d(e2.getLocalizedMessage());
            bH.C.d(e2.getLocalizedMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "toggleDatalogging";
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
