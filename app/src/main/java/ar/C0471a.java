package aR;

import aP.C0338f;
import aP.cZ;
import aP.hY;
import com.efiAnalytics.ui.bV;
import d.InterfaceC1711c;
import java.awt.Window;
import java.util.Properties;

/* renamed from: aR.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aR/a.class */
public class C0471a implements InterfaceC1711c {
    @Override // d.InterfaceC1711c
    public String b() {
        return "Browse Projects";
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
    public void a(Properties properties) throws d.e {
        try {
            if (!bV.f()) {
                bV.a(new hY());
            }
            C0338f.a().c((Window) cZ.a().c());
        } catch (Exception e2) {
            throw new d.e(e2.getLocalizedMessage());
        }
    }

    @Override // d.InterfaceC1711c
    public String a() {
        return "browseProjects";
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
