package bt;

import G.C0075bh;
import G.C0079bl;
import G.C0088bu;
import G.C0113cs;
import G.C0126i;
import G.InterfaceC0109co;
import bF.C0973d;
import c.InterfaceC1385d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1648ef;
import com.efiAnalytics.ui.InterfaceC1657eo;
import com.efiAnalytics.ui.InterfaceC1662et;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import r.C1806i;

/* renamed from: bt.R, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/R.class */
public class C1290R extends JPanel implements G.aN, InterfaceC0109co, InterfaceC1282J, bN, InterfaceC1385d, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    C0075bh f8697a;

    /* renamed from: b, reason: collision with root package name */
    C0973d f8698b;

    /* renamed from: c, reason: collision with root package name */
    bF.y f8699c;

    /* renamed from: d, reason: collision with root package name */
    G.R f8700d;

    /* renamed from: e, reason: collision with root package name */
    T f8701e = new T(this);

    /* renamed from: f, reason: collision with root package name */
    String f8702f = null;

    /* renamed from: g, reason: collision with root package name */
    bN f8703g = null;

    /* renamed from: h, reason: collision with root package name */
    List f8704h = new ArrayList();

    public C1290R(G.R r2, C0079bl c0079bl, boolean z2, boolean z3) throws V.g {
        String str = c0079bl.aJ() + "_accessoryTable";
        str = z2 ? str + "_vertical" : str;
        C0088bu c0088buC = r2.e().c(str);
        if (c0088buC != null && !(c0088buC instanceof C0075bh)) {
            throw new V.g(str + ", name already used but not defined as a 1DTable");
        }
        this.f8697a = (C0075bh) c0088buC;
        if (this.f8697a == null) {
            this.f8697a = new C0075bh();
            this.f8697a.b(c0079bl.l());
            for (int i2 = 0; i2 < c0079bl.d(); i2++) {
                this.f8697a.a(c0079bl.b(i2), c0079bl.a(i2));
            }
            if (c0079bl.k() > 0) {
                for (int i3 = 0; i3 < c0079bl.k(); i3++) {
                    this.f8697a.a(c0079bl.e(i3));
                }
            } else {
                for (int i4 = 0; i4 < c0079bl.g(); i4++) {
                    this.f8697a.a(c0079bl.c(i4));
                }
            }
            for (int i5 = 0; i5 < c0079bl.k(); i5++) {
                this.f8697a.b(c0079bl.e(i5));
            }
            for (int i6 = 0; i6 < c0079bl.m(); i6++) {
                this.f8697a.c(c0079bl.f(i6));
            }
            for (int i7 = 0; i7 < c0079bl.j(); i7++) {
                this.f8697a.a(c0079bl.d(i7));
            }
            this.f8697a.b(z2);
            this.f8697a.u(c0079bl.aH());
            this.f8697a.g(c0079bl.n());
            this.f8697a.a(c0079bl.z());
            this.f8697a.v(str);
            r2.e().a(this.f8697a);
        } else if (!z3) {
            this.f8697a.b(z2);
            this.f8697a.u(c0079bl.aH());
            this.f8697a.g(c0079bl.n());
            this.f8697a.a(c0079bl.z());
            this.f8697a.v(str);
        }
        a(r2, this.f8697a, z3);
    }

    private void a(G.R r2, C0075bh c0075bh, boolean z2) {
        this.f8700d = r2;
        this.f8697a = c0075bh;
        this.f8702f = c0075bh.aH();
        if (z2) {
            this.f8699c = bO.a().c(r2, c0075bh.aJ(), c0075bh.aK());
        } else {
            this.f8699c = bO.a().c(r2, c0075bh.aJ());
        }
        this.f8698b = new C0973d(this.f8699c);
        this.f8698b.b(C1806i.a().a("joijt;i609tr0932"));
        this.f8698b.e().addFocusListener(new S(this));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f8698b);
        try {
            G.aR.a().a(r2.c(), c0075bh.d(0), this);
        } catch (Exception e2) {
            bH.C.d("No Column Parameter");
        }
        for (int i2 = 0; i2 < this.f8697a.f(); i2++) {
            try {
                C0126i.a(r2.c(), this.f8697a.f(i2), this);
            } catch (V.a e3) {
                Logger.getLogger(C1290R.class.getName()).log(Level.WARNING, "Failed to setup listener for label changes.", (Throwable) e3);
            }
        }
        for (int i3 = 0; i3 < this.f8697a.c(); i3++) {
            try {
                C0126i.a(r2.c(), this.f8697a.e(i3), this);
            } catch (V.a e4) {
                Logger.getLogger(C1290R.class.getName()).log(Level.WARNING, "Failed to setup listener for label changes.", (Throwable) e4);
            }
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f8698b.a(interfaceC1662et);
    }

    public void a(InterfaceC1657eo interfaceC1657eo) {
        this.f8698b.a(interfaceC1657eo);
    }

    public void a(InterfaceC1648ef[] interfaceC1648efArr) {
        this.f8698b.a(interfaceC1648efArr);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f8698b.setEnabled(z2);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        this.f8698b.b();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        G.aR.a().a(this);
        C0113cs.a().a(this);
        b();
    }

    public void c() {
        if (this.f8701e == null) {
            this.f8701e = new T(this);
        }
        this.f8701e.a();
    }

    public void a(Color color) {
        this.f8698b.a(color);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
    }

    public bF.y e() {
        return this.f8698b.a();
    }

    public void a(int i2) {
        this.f8698b.b(i2);
    }

    public Dimension b(int i2) {
        return this.f8698b.c(i2);
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f8700d;
    }

    @Override // c.InterfaceC1385d
    public String a_() {
        return this.f8702f;
    }

    @Override // c.InterfaceC1385d
    public void b_(String str) {
        this.f8702f = str;
    }

    public C0973d f() {
        return this.f8698b;
    }

    @Override // bt.bN
    public void a() {
        if (this.f8703g == null) {
            this.f8703g = new C1278F(this.f8700d, this.f8697a, this);
        } else {
            this.f8703g.b();
        }
        this.f8703g.a();
    }

    @Override // bt.bN
    public void b() {
        if (this.f8703g != null) {
            this.f8703g.b();
        }
    }

    public void a(bN bNVar) {
        b();
        this.f8703g = bNVar;
        if (bNVar != null) {
            bNVar.a();
        }
    }

    public C0075bh g() {
        return this.f8697a;
    }

    protected void c(int i2) {
        this.f8698b.a(i2);
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f8704h.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f8704h.remove(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return this.f8697a.b(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        Iterator it = this.f8704h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f8704h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }
}
