package aP;

import bl.C1190l;
import c.InterfaceC1386e;
import com.efiAnalytics.plugin.ApplicationPlugin;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/gH.class */
class gH implements InterfaceC1386e {

    /* renamed from: a, reason: collision with root package name */
    ApplicationPlugin f3414a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3415b;

    public gH(C0308dx c0308dx, ApplicationPlugin applicationPlugin) {
        this.f3415b = c0308dx;
        this.f3414a = null;
        this.f3414a = applicationPlugin;
    }

    @Override // c.InterfaceC1386e
    public boolean a() {
        try {
            this.f3414a = C1190l.a().b(this.f3414a.getIdName());
            if (C1806i.a().a("09jtrkgds;okfds")) {
                if (this.f3414a.isMenuEnabled()) {
                    return true;
                }
            }
            return false;
        } catch (Error e2) {
            bH.C.a("Error calling isMenuEnabled() on Plugin " + this.f3414a.getIdName() + " by " + this.f3414a.getAuthor() + "\nMessage\n" + e2.getMessage());
            return false;
        } catch (Exception e3) {
            if (this.f3414a != null) {
                bH.C.a("Exception calling isMenuEnabled() on Plugin " + this.f3414a.getIdName() + " by " + this.f3414a.getAuthor() + "\nMessage\n" + e3.getMessage());
                return false;
            }
            bH.C.a("Exception calling isMenuEnabled() on Plugin, It does not seem to be loaded \nMessage\n" + e3.getMessage());
            return false;
        }
    }
}
