package aP;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.List;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aP/hG.class */
class hG implements u.g {

    /* renamed from: a, reason: collision with root package name */
    G.R f3520a;

    /* renamed from: b, reason: collision with root package name */
    G.Y f3521b;

    /* renamed from: c, reason: collision with root package name */
    File f3522c;

    /* renamed from: d, reason: collision with root package name */
    boolean f3523d = false;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ hC f3524e;

    hG(hC hCVar, G.R r2, G.Y y2, File file) {
        this.f3524e = hCVar;
        this.f3520a = r2;
        this.f3521b = y2;
        this.f3522c = file;
    }

    @Override // u.g
    public String a() {
        return "Load Settings from " + this.f3522c.getName();
    }

    @Override // u.g
    public String b() {
        return "Will load all differences in " + this.f3522c.getName() + " to replace current settings.";
    }

    @Override // u.g
    public boolean d() throws HeadlessException {
        if (!com.efiAnalytics.ui.bV.a("Are you sure you want to load all settings from " + this.f3522c.getName() + "?", (Component) cZ.a().c(), true)) {
            return false;
        }
        if (C1806i.a().a("-=fds[pfds[pgd-0")) {
            C0338f.a().d(this.f3520a, "Save before loading " + this.f3522c.getName() + " from a Difference report.");
        }
        new C0436ir(this.f3520a, cZ.a().b(), 0).a();
        if (this.f3522c == null || !this.f3522c.exists()) {
            for (int i2 = 0; i2 < this.f3521b.e(); i2++) {
                try {
                    this.f3520a.h().a(i2, 0, this.f3521b.b(i2));
                    this.f3520a.h().g();
                } catch (V.g e2) {
                    e2.printStackTrace();
                    com.efiAnalytics.ui.bV.d("A strange error occured!\nI am not sure what would cause this, but it appears that it happened.\nPlease report it.", null);
                    return false;
                }
            }
            this.f3520a.I();
        } else {
            C0338f.a().a(cZ.a().c(), this.f3520a, this.f3522c.getAbsolutePath(), (List) null);
        }
        this.f3523d = true;
        return true;
    }

    @Override // u.g
    public boolean c() {
        return this.f3523d;
    }
}
