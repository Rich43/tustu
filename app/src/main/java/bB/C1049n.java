package bb;

import G.T;
import aP.cZ;
import aP.iK;
import ae.C0499c;
import ae.C0502f;
import c.C1382a;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.fK;
import com.efiAnalytics.ui.fR;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1818g;

/* renamed from: bb.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/n.class */
public class C1049n implements fR {

    /* renamed from: a, reason: collision with root package name */
    fK f7784a;

    /* renamed from: b, reason: collision with root package name */
    ae.p f7785b = new ae.p();

    /* renamed from: c, reason: collision with root package name */
    C1039d f7786c;

    /* renamed from: d, reason: collision with root package name */
    C1028E f7787d;

    /* renamed from: e, reason: collision with root package name */
    C1035L f7788e;

    /* renamed from: f, reason: collision with root package name */
    C1036a f7789f;

    /* renamed from: g, reason: collision with root package name */
    C1038c f7790g;

    /* renamed from: h, reason: collision with root package name */
    v f7791h;

    /* renamed from: i, reason: collision with root package name */
    C1038c f7792i;

    /* renamed from: j, reason: collision with root package name */
    C1046k f7793j;

    /* renamed from: k, reason: collision with root package name */
    C1046k f7794k;

    /* renamed from: l, reason: collision with root package name */
    C1046k f7795l;

    /* renamed from: m, reason: collision with root package name */
    C1046k f7796m;

    /* renamed from: n, reason: collision with root package name */
    x f7797n;

    /* renamed from: o, reason: collision with root package name */
    C0502f f7798o;

    /* renamed from: p, reason: collision with root package name */
    boolean f7799p;

    /* renamed from: q, reason: collision with root package name */
    JDialog f7800q;

    /* renamed from: r, reason: collision with root package name */
    private static File f7801r = new File(C1807j.C(), "firmwareLoader/firmwareLoaderIntro.html");

    public C1049n() {
        this.f7784a = null;
        this.f7786c = new C1039d(this.f7785b, !C1806i.a().a(";'gdfdhg-0hg"));
        this.f7787d = new C1028E();
        this.f7788e = new C1035L();
        this.f7789f = new C1036a();
        this.f7790g = new C1038c(true);
        this.f7791h = new v();
        this.f7792i = new C1038c(false);
        this.f7793j = new C1046k("Install & Update Firmware", false);
        this.f7794k = new C1046k("Firmware Read Me", false);
        this.f7795l = new C1046k("Firmware License", true);
        this.f7796m = new C1046k("Firmware Release Notes", false);
        this.f7797n = new x();
        this.f7798o = null;
        this.f7799p = false;
        this.f7800q = null;
        this.f7784a = new fK("Firmware Update Utility", C1818g.d());
        this.f7793j.a(true);
        try {
            this.f7793j.a(f7801r);
            this.f7784a.e(this.f7793j);
        } catch (Exception e2) {
            Logger.getLogger(C1049n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            this.f7784a.e(new u(C1818g.d()));
        }
        if (aE.a.A() != null && C1806i.a().a("09RGDKDG;LKIGD")) {
            this.f7784a.e(this.f7789f);
        }
        this.f7784a.e(this.f7786c);
        this.f7784a.e(this.f7787d);
        this.f7784a.e(this.f7788e);
        this.f7784a.e(this.f7795l);
        this.f7784a.e(this.f7794k);
        this.f7784a.e(this.f7796m);
        this.f7784a.e(this.f7790g);
        this.f7784a.e(this.f7791h);
        if (T.a().c() != null) {
            this.f7784a.e(this.f7797n);
        }
        this.f7784a.a(this);
        try {
            iK.a().a(new C1050o(this));
        } catch (V.a e3) {
            Logger.getLogger(C1049n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() {
        cZ.a().b(this.f7800q);
        for (int i2 = 0; i2 < this.f7784a.e(); i2++) {
            if (this.f7784a.a(i2) instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) this.f7784a.a(i2)).close();
            }
        }
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
        cZ.a().b(this.f7800q);
        for (int i2 = 0; i2 < this.f7784a.e(); i2++) {
            if (this.f7784a.a(i2) instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) this.f7784a.a(i2)).close();
            }
        }
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean a(Container container) {
        if (container.equals(this.f7786c)) {
            if (this.f7786c.g() == null) {
                bV.d(C1818g.b("Please detect Hardware before continuing."), container);
                return false;
            }
            if (this.f7786c.g().e() == null) {
                return bV.a((C1818g.b("Unknown Hardware detected.") + "\n") + C1818g.b("Are you sure you wish to continue?"), (Component) container, true);
            }
            this.f7786c.h();
            this.f7799p = this.f7797n.a();
            return true;
        }
        if (!container.equals(this.f7787d)) {
            if (container.equals(this.f7795l)) {
                if (!this.f7795l.a()) {
                    return true;
                }
                bV.d(C1818g.b("You must accept the Firmware License Agreement to proceed."), container);
                return false;
            }
            if (container.equals(this.f7788e)) {
                if (this.f7788e.a() != null) {
                    return true;
                }
                bV.d(C1818g.b("No Firmware Loader selected, cannot continue."), container);
                return false;
            }
            if (container.equals(this.f7790g)) {
                return this.f7790g.a();
            }
            if (container.equals(this.f7792i)) {
                return this.f7792i.a();
            }
            if (container.equals(this.f7791h)) {
                return this.f7791h.a();
            }
            if (container.equals(this.f7797n)) {
                return this.f7797n.e() ? !this.f7797n.d() : bV.a(C1818g.b("Are you sure you want to exit without restoring your Tune Settings?"), (Component) container, true);
            }
            return true;
        }
        ae.k kVarA = this.f7787d.a();
        ae.m mVarE = this.f7786c.g().e();
        if (kVarA == null) {
            bV.d(C1818g.b("No Firmware Selected!") + "\n" + C1818g.b("You must select a firmware to load."), container);
            return false;
        }
        if (!kVarA.a()) {
            bV.d(C1818g.b("No valid Firmware file in selected package!") + "\n" + C1818g.b("Please select a valid firmware to load."), container);
            return false;
        }
        List listA = ae.r.a().a(kVarA, mVarE);
        if (listA.size() != 1) {
            this.f7788e.a(listA);
            this.f7784a.b(this.f7788e);
            return true;
        }
        this.f7784a.a(this.f7788e);
        ae.q qVar = (ae.q) listA.get(0);
        this.f7788e.a(listA);
        File fileG = kVarA.g();
        if (fileG == null) {
            bV.d(C1818g.b("You must select a Firmware File to proceed."), container);
            return false;
        }
        if (qVar.b(mVarE, fileG)) {
            return true;
        }
        String str = C1818g.b(C1798a.f13268b + " cannot validate that the selected firmware File is correct for your " + C1382a.a(this.f7786c.g() != null ? C1382a.a(this.f7786c.g().b(), C1798a.f13272f) : "", C1798a.f13272f) + ".") + "\n" + C1818g.b("Only proceed if you are confident the select file is correct.") + "\n";
        if (qVar.b(mVarE) != null) {
            str = (str + "\n" + C1818g.b("Detected Controller:") + " - " + mVarE.a()) + "\n" + C1818g.b("Expected Firmware File:") + " - " + qVar.b(mVarE) + "\n";
        }
        return bV.b(str + "\n" + C1818g.b("Are you sure you want to load the selected firmware?"), container, true);
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b(Container container) throws IllegalArgumentException {
        if (container.equals(this.f7788e)) {
            this.f7788e.a(this.f7787d.a(), this.f7786c.g().e());
            if (this.f7788e.b() == 1) {
            }
            return true;
        }
        if (container.equals(this.f7786c)) {
            if (aE.a.A() == null) {
                this.f7786c.d();
                return true;
            }
            this.f7786c.c();
            this.f7786c.f();
            return true;
        }
        if (container.equals(this.f7787d)) {
            this.f7787d.a(this.f7786c.g().e());
            return true;
        }
        if (container.equals(this.f7794k)) {
            ae.k kVarA = this.f7787d.a();
            if (kVarA.e() == null || !kVarA.e().exists()) {
                return false;
            }
            try {
                this.f7794k.a(kVarA.e());
                return true;
            } catch (V.a e2) {
                bV.d(C1818g.b("Firmware ReadMe File not found."), container);
                Logger.getLogger(C1049n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return true;
            }
        }
        if (container.equals(this.f7795l)) {
            ae.k kVarA2 = this.f7787d.a();
            if (kVarA2.f() == null || !kVarA2.f().exists()) {
                this.f7795l.f7775b.setSelected(true);
                this.f7784a.a(this.f7795l);
                return false;
            }
            try {
                this.f7795l.a(kVarA2.f());
                return true;
            } catch (V.a e3) {
                bV.d(C1818g.b("Firmware License File not found."), container);
                Logger.getLogger(C1049n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                return true;
            }
        }
        if (container.equals(this.f7796m)) {
            ae.k kVarA3 = this.f7787d.a();
            if (kVarA3.i() == null || !kVarA3.i().exists()) {
                return false;
            }
            try {
                this.f7796m.a(kVarA3.i());
                return true;
            } catch (V.a e4) {
                bV.d(C1818g.b("Release Notes File not found."), container);
                Logger.getLogger(C1049n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                return true;
            }
        }
        if (container.equals(this.f7790g)) {
            ae.q qVarA = this.f7788e.a();
            if (qVarA == null) {
                bV.d(C1818g.b("No valid Firmware Loader Driver found or selected."), container);
                return false;
            }
            a();
            ae.k kVarA4 = this.f7787d.a();
            if (!C0499c.c(qVarA.c(), kVarA4)) {
                this.f7784a.a(container);
                return false;
            }
            this.f7790g.a(qVarA, kVarA4);
            this.f7784a.b(container);
            return true;
        }
        if (!container.equals(this.f7792i)) {
            if (container.equals(this.f7791h)) {
                this.f7791h.a(a());
                this.f7784a.a(false);
                return true;
            }
            if (!container.equals(this.f7797n)) {
                return true;
            }
            this.f7797n.a(this.f7787d.a(), this.f7788e.a(), this.f7785b);
            return true;
        }
        ae.q qVarA2 = this.f7788e.a();
        if (qVarA2 == null) {
            bV.d(C1818g.b("No valid Firmware Loader Driver found or selected."), container);
            return false;
        }
        ae.k kVarA5 = this.f7787d.a();
        if (!C0499c.d(qVarA2.d(), kVarA5)) {
            this.f7784a.a(container);
            return false;
        }
        this.f7784a.b(container);
        this.f7792i.a(qVarA2, kVarA5);
        return true;
    }

    public void a(Window window) {
        this.f7800q = this.f7784a.a(window, C1818g.b("Firmware Update Utility"));
        cZ.a().a(this.f7800q);
        this.f7800q.setVisible(true);
    }

    private C0502f a() {
        if (this.f7798o == null) {
            ae.q qVarA = this.f7788e.a();
            qVarA.a(this.f7786c.g().b());
            this.f7798o = new C0502f(qVarA, this.f7787d.a(), this.f7786c.i());
        } else {
            ae.q qVarA2 = this.f7788e.a();
            qVarA2.a(this.f7786c.g().b());
            this.f7798o.a(qVarA2);
            this.f7798o.a(this.f7787d.a());
            this.f7798o.a(this.f7786c.i());
        }
        return this.f7798o;
    }
}
