package be;

import G.aH;
import com.efiAnalytics.ui.C1685fp;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import r.C1806i;
import s.C1818g;
import sun.security.validator.Validator;

/* renamed from: be.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/c.class */
public class C1087c extends JPanel implements InterfaceC1099o {

    /* renamed from: a, reason: collision with root package name */
    static String f7956a = "Advanced";

    /* renamed from: b, reason: collision with root package name */
    static String f7957b = Validator.TYPE_SIMPLE;

    /* renamed from: e, reason: collision with root package name */
    C1080M f7960e;

    /* renamed from: f, reason: collision with root package name */
    C1107w f7961f;

    /* renamed from: g, reason: collision with root package name */
    JRadioButton f7962g;

    /* renamed from: h, reason: collision with root package name */
    JRadioButton f7963h;

    /* renamed from: i, reason: collision with root package name */
    G.R f7964i;

    /* renamed from: c, reason: collision with root package name */
    CardLayout f7958c = new CardLayout();

    /* renamed from: d, reason: collision with root package name */
    JPanel f7959d = new JPanel();

    /* renamed from: j, reason: collision with root package name */
    aH f7965j = null;

    /* renamed from: k, reason: collision with root package name */
    private boolean f7966k = true;

    public C1087c(G.R r2) {
        this.f7964i = r2;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1));
        C1088d c1088d = new C1088d(this);
        ButtonGroup buttonGroup = new ButtonGroup();
        this.f7962g = new JRadioButton(C1818g.b(f7957b));
        this.f7962g.setActionCommand(f7957b);
        buttonGroup.add(this.f7962g);
        this.f7962g.addActionListener(c1088d);
        jPanel.add(this.f7962g);
        this.f7963h = new JRadioButton(C1818g.b(f7956a));
        this.f7963h.setActionCommand(f7956a);
        buttonGroup.add(this.f7963h);
        this.f7963h.setEnabled(C1806i.a().a("09ggdslkmkgoiu"));
        this.f7963h.addActionListener(c1088d);
        jPanel.add(this.f7963h);
        add(jPanel, "North");
        this.f7959d.setLayout(this.f7958c);
        this.f7960e = new C1080M();
        this.f7960e.a(r2);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.add(this.f7960e);
        this.f7959d.add(jPanel2, f7957b);
        this.f7961f = new C1107w(r2);
        this.f7959d.add(this.f7961f, f7956a);
        add(this.f7959d, BorderLayout.CENTER);
        this.f7961f.b(new C1090f(this));
        this.f7961f.a(new C1089e(this));
        if (!C1806i.a().a("09ggdslkmkgoiu")) {
            C1685fp.a((Component) this.f7961f, false);
        }
        this.f7962g.setSelected(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        this.f7958c.show(this.f7959d, str);
        if (!str.equals(f7956a)) {
            this.f7960e.a(this.f7961f.b(), d());
            this.f7960e.a(this.f7961f.e());
            return;
        }
        this.f7961f.a(this.f7960e.b(), d());
        this.f7961f.b(this.f7960e.e());
        try {
            String strD = this.f7960e.d();
            if (strD.endsWith("- 0.0 )/( 0.0 - 0.0 ))")) {
                strD = "";
            }
            this.f7961f.a(strD);
        } catch (Exception e2) {
        }
    }

    public void a(G.R r2) {
        this.f7960e.a(r2);
        this.f7961f.a(r2);
        this.f7964i = r2;
    }

    public boolean a() {
        return this.f7962g.isSelected() ? this.f7960e.a() : this.f7961f.a();
    }

    public aH b() throws V.g {
        if (this.f7965j == null) {
            this.f7965j = new aH(this.f7964i.ac());
        }
        this.f7965j.a("formula");
        if (this.f7962g.isSelected()) {
            this.f7965j.v(this.f7960e.b());
            this.f7965j.e(this.f7960e.d());
            this.f7965j.c(this.f7960e.e());
        } else {
            this.f7965j.v(this.f7961f.b());
            this.f7965j.e(this.f7961f.d());
            this.f7965j.c(this.f7961f.e());
        }
        return this.f7965j;
    }

    public void a(aH aHVar) {
        this.f7965j = aHVar;
        if (aHVar.b() == null) {
            a(true);
            try {
                this.f7965j.a("formula");
            } catch (V.g e2) {
                Logger.getLogger(C1087c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        } else {
            a(false);
        }
        if (this.f7965j.k() == null || this.f7960e.b(this.f7965j.k())) {
            this.f7962g.setSelected(true);
            a(f7957b);
        } else {
            this.f7963h.setSelected(true);
            a(f7956a);
        }
        if (this.f7962g.isSelected()) {
            if (this.f7965j.k() != null) {
                this.f7960e.a(this.f7965j.k(), this.f7965j.e());
            }
            this.f7960e.a(this.f7965j.aJ(), d());
        } else {
            this.f7961f.a(this.f7965j.aJ(), d());
            this.f7961f.a(this.f7965j.k());
            this.f7961f.b(this.f7965j.e());
        }
        this.f7960e.f();
        this.f7961f.f();
    }

    @Override // be.InterfaceC1099o
    public boolean c() {
        return this.f7961f.c() || this.f7960e.c();
    }

    public boolean d() {
        return this.f7966k;
    }

    public void a(boolean z2) {
        this.f7966k = z2;
    }
}
