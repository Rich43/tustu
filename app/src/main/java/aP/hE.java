package aP;

import r.C1798a;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/hE.class */
class hE implements u.g {

    /* renamed from: a, reason: collision with root package name */
    G.R f3514a;

    /* renamed from: b, reason: collision with root package name */
    G.Y f3515b;

    /* renamed from: c, reason: collision with root package name */
    boolean f3516c = false;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ hC f3517d;

    hE(hC hCVar, G.R r2, G.Y y2) {
        this.f3517d = hCVar;
        this.f3514a = r2;
        this.f3515b = y2;
    }

    @Override // u.g
    public String a() {
        return "Use Controller Settings";
    }

    @Override // u.g
    public String b() {
        return C1806i.a().a("-=fds[pfds[pgd-0") ? C1818g.b("Save a Restore Point and Update the " + C1798a.f13268b + " settings with the current Controller settings.") : C1818g.b("Update the " + C1798a.f13268b + " settings with the current Controller settings.");
    }

    @Override // u.g
    public boolean d() {
        if (C1806i.a().a("-=fds[pfds[pgd-0")) {
            C0338f.a().d(this.f3514a, C1818g.b(C1798a.f13268b + " settings before accepting Controller values in a Difference Report."));
        }
        if (!this.f3517d.a(this.f3514a, this.f3515b, false)) {
            C0404hl.a().b("Failed to load the Controller settings. Check logs for details.");
            return false;
        }
        C0404hl.a().b(C1806i.a().a("-=fds[pfds[pgd-0") ? C1818g.b("The Controller settings have been loaded successfully.") + "\n" + C1818g.b("A Restore point has been saved containing the previous " + C1798a.f13268b + " settings.") : C1818g.b("The Controller settings have been loaded successfully."));
        C0338f.a().e(this.f3514a);
        return true;
    }

    @Override // u.g
    public boolean c() {
        return this.f3516c;
    }
}
