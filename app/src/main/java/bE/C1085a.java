package be;

import G.C0043ac;
import G.C0048ah;
import G.aH;
import aP.C0338f;
import aP.cZ;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.fK;
import com.efiAnalytics.ui.fR;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* renamed from: be.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/a.class */
public class C1085a implements fR {

    /* renamed from: b, reason: collision with root package name */
    T f7953b;

    /* renamed from: d, reason: collision with root package name */
    private fK f7951d = new fK(null, C1818g.d());

    /* renamed from: c, reason: collision with root package name */
    G.R f7954c = G.T.a().c();

    /* renamed from: a, reason: collision with root package name */
    C1087c f7952a = new C1087c(this.f7954c);

    public C1085a() {
        this.f7952a.a(this.f7954c);
        this.f7951d.e(this.f7952a);
        this.f7953b = new T(this.f7954c);
        this.f7951d.e(this.f7953b);
        this.f7951d.a(this);
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() {
        try {
            aH aHVarB = this.f7952a.b();
            try {
                C0043ac c0043acB = this.f7953b.b();
                try {
                    C0048ah c0048ahC = this.f7953b.c();
                    if (c0048ahC != null) {
                        c0048ahC.q(true);
                        this.f7954c.a(c0048ahC);
                    }
                    if (c0043acB != null) {
                        c0043acB.q(true);
                        this.f7954c.a(c0043acB);
                    }
                    if (aHVarB != null) {
                        aHVarB.q(true);
                        this.f7954c.a(aHVarB);
                    }
                    return C0338f.a().b(this.f7954c);
                } catch (V.a e2) {
                    Logger.getLogger(C1085a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    bV.d(C1818g.b("Problem with Gauge Template, Message:") + "\n" + e2.getLocalizedMessage(), this.f7951d);
                    return false;
                }
            } catch (V.a e3) {
                Logger.getLogger(C1085a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                bV.d(C1818g.b("Problem with Data log Field, Message:") + "\n" + e3.getLocalizedMessage(), this.f7951d);
                return false;
            }
        } catch (V.g e4) {
            Logger.getLogger(C1085a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            bV.d(C1818g.b("Problem with OutputChannel, Message:") + "\n" + e4.getLocalizedMessage(), this.f7951d);
            return false;
        }
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean a(Container container) {
        if (container.equals(this.f7952a)) {
            return this.f7952a.a();
        }
        if (container.equals(this.f7953b)) {
            return this.f7953b.a();
        }
        return true;
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b(Container container) {
        if (!container.equals(this.f7953b)) {
            return true;
        }
        try {
            this.f7953b.a(this.f7952a.b().aJ());
            return true;
        } catch (V.g e2) {
            Logger.getLogger(C1085a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    public void a() {
        this.f7951d.a(cZ.a().c(), C1818g.b("Add a new Channel"), false).setVisible(true);
    }
}
