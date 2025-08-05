package be;

import G.C0043ac;
import G.C0048ah;
import G.aH;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuContainer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;

/* renamed from: be.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/s.class */
public class C1103s extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C1087c f7994a = null;

    /* renamed from: b, reason: collision with root package name */
    C1094j f7995b = null;

    /* renamed from: c, reason: collision with root package name */
    y f7996c = null;

    /* renamed from: d, reason: collision with root package name */
    G.R f7997d = null;

    /* renamed from: e, reason: collision with root package name */
    C1106v f7998e = new C1106v(this);

    /* renamed from: h, reason: collision with root package name */
    private C1069B f7999h = null;

    /* renamed from: f, reason: collision with root package name */
    JPanel f8000f = new JPanel();

    /* renamed from: g, reason: collision with root package name */
    JPanel f8001g;

    public C1103s() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f8000f.setLayout(new GridLayout(1, 1));
        jPanel.add(this.f8000f, BorderLayout.CENTER);
        this.f8001g = new JPanel();
        this.f8001g.setLayout(new FlowLayout(1));
        JButton jButton = new JButton(C1818g.b("Ok"));
        jButton.addActionListener(new C1104t(this));
        JButton jButton2 = new JButton(C1818g.b("Cancel"));
        jButton2.addActionListener(new C1105u(this));
        if (bH.I.a()) {
            this.f8001g.add(jButton);
            this.f8001g.add(jButton2);
        } else {
            this.f8001g.add(jButton);
            this.f8001g.add(jButton2);
        }
        jPanel.add(this.f8001g, "South");
        setLayout(new FlowLayout(1));
        add(jPanel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() throws NumberFormatException {
        if (d() instanceof C1087c) {
            C1087c c1087c = (C1087c) d();
            if (!c1087c.a()) {
                return;
            }
            try {
                aH aHVarG = this.f7997d.g(c1087c.b().aJ());
                if (aHVarG != null && !aHVarG.aL() && !bV.a(C1818g.b("There is an existing OuputChannel in the main config by that name.") + "\n" + C1818g.b("Are you sure you want to over-ride that channel?"), (Component) this, true)) {
                    return;
                } else {
                    this.f7999h.a(c1087c.b());
                }
            } catch (V.g e2) {
                bV.d(e2.getMessage(), this);
                Logger.getLogger(C1103s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                return;
            }
        } else if (d() instanceof C1094j) {
            C1091g c1091g = ((C1094j) d()).f7984d;
            R rB = c1091g.b();
            if (!rB.a()) {
                bV.d(C1818g.b(rB.d()), this);
                return;
            }
            try {
                this.f7999h.a(c1091g.a());
            } catch (V.a e3) {
                Logger.getLogger(C1103s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                bV.d(C1818g.b(e3.getLocalizedMessage()), this);
                return;
            }
        } else if (d() instanceof y) {
            y yVar = (y) d();
            R rB2 = yVar.b();
            if (!rB2.a()) {
                bV.d(C1818g.b(rB2.d()), this);
                return;
            }
            try {
                this.f7999h.a(yVar.a());
            } catch (V.a e4) {
                Logger.getLogger(C1103s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                bV.d(C1818g.b(e4.getLocalizedMessage()), this);
                return;
            }
        }
        this.f8000f.removeAll();
        C1685fp.a((Container) this.f8001g, false);
        validate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f8000f.removeAll();
        C1685fp.a((Container) this.f8001g, false);
        validate();
    }

    public void a(G.Q q2) throws NumberFormatException {
        if (c()) {
            MenuContainer menuContainerD = d();
            if ((!(menuContainerD instanceof InterfaceC1099o) || ((InterfaceC1099o) menuContainerD).c()) && bV.a(C1818g.b("Would you like to save the current settings?"), (Component) this, true)) {
                a();
            } else {
                b();
            }
        }
        if (q2 instanceof aH) {
            this.f8000f.add(f());
            f().a((aH) q2);
        } else if (q2 instanceof C0048ah) {
            this.f8000f.add(g());
            g().a((C0048ah) q2);
        } else if (q2 instanceof C0043ac) {
            this.f8000f.add(e());
            e().a((C0043ac) q2);
            e().a(((C0043ac) q2).a() == null);
        }
        C1685fp.a((Container) this.f8001g, this.f8000f.getComponentCount() > 0);
        this.f8000f.doLayout();
        doLayout();
        validate();
    }

    private boolean c() {
        return this.f8000f.getComponentCount() > 0;
    }

    private Component d() {
        if (this.f8000f.getComponentCount() > 0) {
            return this.f8000f.getComponent(0);
        }
        return null;
    }

    private C1094j e() {
        if (this.f7995b == null) {
            this.f7995b = new C1094j(this.f7997d);
        }
        return this.f7995b;
    }

    private C1087c f() {
        if (this.f7994a == null) {
            this.f7994a = new C1087c(this.f7997d);
        }
        return this.f7994a;
    }

    private y g() {
        if (this.f7996c == null) {
            this.f7996c = new y(this.f7997d);
            if (!C1806i.a().a("09ggdslkmkgoiu")) {
                C1685fp.a((Component) this.f7996c, false);
            }
        }
        return this.f7996c;
    }

    public void a(G.R r2) {
        this.f7997d = r2;
        if (this.f7994a != null) {
            this.f7994a.a(r2);
        }
        if (this.f7995b != null) {
            this.f7995b.a(r2);
        }
        if (this.f7996c != null) {
            this.f7996c.a(r2);
        }
    }

    public void a(C1069B c1069b) {
        if (c1069b != null) {
            c1069b.b(this.f7998e);
        }
        this.f7999h = c1069b;
        c1069b.a(this.f7998e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        a(this.f7997d);
    }
}
