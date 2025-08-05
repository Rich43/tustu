package bt;

import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ah.class */
public class C1299ah extends C1348g implements G.aN, bX, InterfaceC1349h, InterfaceC1385d, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    protected aZ f8828a;

    /* renamed from: b, reason: collision with root package name */
    G.E f8829b;

    /* renamed from: c, reason: collision with root package name */
    G.R f8830c;

    /* renamed from: d, reason: collision with root package name */
    C1366y f8831d;

    /* renamed from: e, reason: collision with root package name */
    boolean f8833e;

    /* renamed from: g, reason: collision with root package name */
    private boolean f8832g = false;

    /* renamed from: f, reason: collision with root package name */
    boolean f8834f = false;

    public C1299ah(G.R r2, G.E e2) {
        this.f8828a = null;
        this.f8830c = null;
        this.f8831d = null;
        this.f8833e = false;
        this.f8830c = r2;
        this.f8829b = e2;
        this.f8833e = e2.e();
        b_(a_());
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        String strL = e2.l();
        strL = strL != null ? C1818g.b(strL) : strL;
        if (strL != null && strL.length() == 0) {
            strL = " ";
        }
        try {
            this.f8828a = (aZ) q.h.a().a(aZ.class);
            this.f8828a.setText(strL);
        } catch (Exception e3) {
            this.f8828a = new aZ(strL);
            bH.C.b("Failed to get SettingsLabel from cache, creating...");
        }
        jPanel.add(BorderLayout.CENTER, this.f8828a);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0, 2, 2));
        if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
            jPanel2.add(new C1291a(this.f8830c, e2.a()));
        }
        jPanel2.add(new C1353l(this.f8830c, e2.a()));
        jPanel.add("West", jPanel2);
        try {
            this.f8831d = (C1366y) q.h.a().a(C1366y.class);
        } catch (Exception e4) {
            this.f8831d = new C1366y();
            bH.C.b("Cache Failed, creating new UI Component");
        }
        c();
        d();
        jPanel.add("East", this.f8831d);
        this.f8831d.a(this);
        try {
            G.aR.a().a(r2.c(), e2.a(), this);
            if (e2.d() != null && e2.d().length() > 0) {
                G.aR.a().a(r2.c(), e2.d(), this);
            }
        } catch (V.a e5) {
            Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        }
        add("North", jPanel);
    }

    private void c() {
        G.R rE = e();
        this.f8831d.removeAllItems();
        String[] strArr = (String[]) bH.R.a((Object[]) rE.s());
        this.f8834f = true;
        for (String str : strArr) {
            G.aH aHVarG = rE.g(str);
            if (aHVarG.b().equals(ControllerParameter.PARAM_CLASS_SCALAR) && (aHVarG.l() != 4 || !this.f8833e)) {
                this.f8831d.a(aHVarG.aJ(), str);
            }
        }
        this.f8834f = false;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8831d.b(this);
        q.h.a().a(this.f8828a);
        q.h.a().a(this.f8831d);
        G.aR.a().a(this);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (this.f8829b.d() == null || this.f8829b.d().length() <= 0 || !this.f8829b.d().equals(str2)) {
            d();
        } else {
            c();
        }
    }

    private void d() {
        G.R rE = e();
        G.aM aMVarC = this.f8830c.c(this.f8829b.a());
        String strA = this.f8831d.a();
        try {
            int iJ = (int) aMVarC.j(this.f8830c.h());
            for (int i2 = 0; i2 < this.f8831d.getItemCount(); i2++) {
                G.aH aHVarG = rE.g(this.f8831d.a(i2));
                if (aHVarG != null && aHVarG.a() == iJ) {
                    this.f8831d.setSelectedIndex(i2);
                    return;
                }
            }
            if (this.f8831d.b(strA)) {
                this.f8831d.a(strA);
            } else {
                this.f8831d.setSelectedIndex(0);
            }
        } catch (V.g e2) {
            Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private G.R e() {
        G.R r2 = this.f8830c;
        if (this.f8829b.d() != null && this.f8829b.d().length() > 0) {
            G.aM aMVarC = this.f8830c.c(this.f8829b.d());
            try {
                boolean z2 = false;
                int iJ = (int) aMVarC.j(this.f8830c.h());
                String[] strArrD = G.T.a().d();
                int length = strArrD.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    G.R rC = G.T.a().c(strArrD[i2]);
                    if (iJ == rC.O().x()) {
                        r2 = rC;
                        z2 = true;
                        break;
                    }
                    i2++;
                }
                if (!z2 && iJ != this.f8830c.O().x()) {
                    aMVarC.a(this.f8830c.h(), this.f8830c.O().x());
                }
            } catch (V.g e2) {
                Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (V.j e3) {
                Logger.getLogger(C1299ah.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        return r2;
    }

    @Override // bt.bX
    public void b(String str) {
        G.R rE = e();
        G.aM aMVarC = this.f8830c.c(this.f8829b.a());
        G.aM aMVarC2 = this.f8830c.c(this.f8829b.c());
        G.aH aHVarG = rE.g(str);
        if (aHVarG == null) {
            com.efiAnalytics.ui.bV.d("Invalid OutputChannel name: " + str, this.f8831d);
            return;
        }
        if (this.f8834f) {
            return;
        }
        try {
            aMVarC.a(this.f8830c.h(), aHVarG.a());
            int iL = aHVarG.l();
            if (aHVarG.t()) {
                iL |= 64;
            }
            if (!aHVarG.p()) {
                iL |= 128;
            }
            aMVarC2.a(this.f8830c.h(), iL);
        } catch (V.g e2) {
        } catch (V.j e3) {
            com.efiAnalytics.ui.bV.d(e3.getMessage(), this.f8831d);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        if (this.f8831d != null) {
            this.f8831d.setFont(font);
        }
        if (this.f8828a != null) {
            this.f8828a.setFont(font);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        a(this, z2);
    }

    private void a(Container container, boolean z2) {
        Component[] components = container.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            components[i2].setEnabled(z2);
            if (components[i2] instanceof Container) {
                a((Container) components[i2], z2);
            }
        }
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (this.f8829b == null || this.f8829b.aH() == null || this.f8831d == null) {
            return;
        }
        try {
            setEnabled(C1007o.a(this.f8829b.aH(), b_()));
        } catch (Exception e2) {
            if (!this.f8832g) {
                com.efiAnalytics.ui.bV.d("Invalid enable condition on offset " + this.f8829b.a() + ":\n { " + a_() + " } ", this);
                this.f8832g = true;
            }
            bH.C.a(e2.getMessage());
        }
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f8830c;
    }
}
