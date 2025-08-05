package bk;

import G.C0113cs;
import G.InterfaceC0109co;
import G.R;
import aI.w;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bk/h.class */
public class h extends JPanel implements InterfaceC0109co, aI.q, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    static final String f8192a = C1818g.b("SD Logging Active");

    /* renamed from: b, reason: collision with root package name */
    static final String f8193b = C1818g.b("SD Logging Idle");

    /* renamed from: c, reason: collision with root package name */
    static final String f8194c = C1818g.b("SD Card Ready");

    /* renamed from: d, reason: collision with root package name */
    static final String f8195d = C1818g.b("SD Card not Ready");

    /* renamed from: e, reason: collision with root package name */
    static final String f8196e = C1818g.b("SD Error!");

    /* renamed from: f, reason: collision with root package name */
    static final String f8197f = C1818g.b("No SD Errors");

    /* renamed from: g, reason: collision with root package name */
    static final String f8198g = C1818g.b("SD Card In");

    /* renamed from: h, reason: collision with root package name */
    static final String f8199h = C1818g.b("No SD Card");

    /* renamed from: i, reason: collision with root package name */
    R f8200i;

    /* renamed from: j, reason: collision with root package name */
    aI.p f8201j;

    /* renamed from: k, reason: collision with root package name */
    o f8202k;

    /* renamed from: l, reason: collision with root package name */
    C1176a f8203l;

    /* renamed from: m, reason: collision with root package name */
    JButton f8204m;

    /* renamed from: n, reason: collision with root package name */
    JButton f8205n;

    /* renamed from: o, reason: collision with root package name */
    JButton f8206o;

    /* renamed from: p, reason: collision with root package name */
    JButton f8207p;

    /* renamed from: q, reason: collision with root package name */
    q f8208q;

    /* renamed from: r, reason: collision with root package name */
    q f8209r;

    /* renamed from: s, reason: collision with root package name */
    q f8210s;

    /* renamed from: t, reason: collision with root package name */
    q f8211t;

    /* renamed from: u, reason: collision with root package name */
    JPanel f8212u;

    /* renamed from: z, reason: collision with root package name */
    private aI.l f8213z = null;

    /* renamed from: v, reason: collision with root package name */
    n f8214v = new n(this);

    /* renamed from: w, reason: collision with root package name */
    dO f8215w = new dO();

    /* renamed from: x, reason: collision with root package name */
    boolean f8216x = false;

    /* renamed from: y, reason: collision with root package name */
    boolean f8217y = false;

    public h(R r2, boolean z2) throws V.a, IllegalArgumentException {
        this.f8200i = null;
        this.f8201j = null;
        this.f8202k = null;
        this.f8203l = null;
        this.f8204m = null;
        this.f8205n = null;
        this.f8206o = null;
        this.f8207p = null;
        this.f8208q = null;
        this.f8209r = null;
        this.f8210s = null;
        this.f8211t = null;
        this.f8212u = null;
        this.f8200i = r2;
        this.f8201j = new aI.p(r2);
        setBorder(BorderFactory.createTitledBorder(C1818g.b("SD Logging Controls")));
        setLayout(new BorderLayout(5, 5));
        this.f8202k = new o(r2);
        add("North", this.f8202k);
        this.f8212u = new JPanel();
        this.f8212u.setLayout(new GridLayout(0, 1, 5, 5));
        this.f8204m = new JButton(C1818g.b("Re-initalize Card"));
        this.f8204m.addActionListener(new i(this));
        this.f8212u.add(this.f8204m);
        this.f8205n = new JButton(C1818g.b("Reset"));
        this.f8205n.addActionListener(new j(this));
        this.f8212u.add(this.f8205n);
        this.f8206o = new JButton(C1818g.b("Reset and wait"));
        this.f8206o.addActionListener(new k(this));
        this.f8212u.add(this.f8206o);
        this.f8207p = new JButton(C1818g.b("Format SD Card"));
        this.f8207p.addActionListener(new l(this));
        this.f8212u.add(this.f8207p);
        this.f8207p.setEnabled(false);
        add(BorderLayout.CENTER, this.f8212u);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 5, 5));
        this.f8208q = new q();
        this.f8208q.a(f8192a);
        this.f8208q.b(f8193b);
        this.f8208q.setText(f8193b);
        jPanel.add(this.f8208q);
        this.f8211t = new q();
        this.f8211t.a(f8198g);
        this.f8211t.b(f8199h);
        this.f8211t.setText(f8199h);
        this.f8211t.b(false);
        jPanel.add(this.f8211t);
        this.f8209r = new q();
        this.f8209r.a(f8194c);
        this.f8209r.b(f8195d);
        this.f8209r.setText(f8195d);
        this.f8209r.b(false);
        jPanel.add(this.f8209r);
        this.f8210s = new q();
        this.f8210s.setBackground(Color.red);
        this.f8210s.a(f8196e);
        this.f8210s.b(f8197f);
        this.f8210s.setText(f8197f);
        this.f8210s.b(false);
        jPanel.add(this.f8210s);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(BorderLayout.CENTER, jPanel);
        this.f8203l = new C1176a(r2, true);
        jPanel2.add("South", this.f8203l);
        if (z2) {
            add("South", jPanel2);
        }
        this.f8201j.a(this);
        C0113cs.a().a(r2.c(), aI.d.f2436a, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        try {
            this.f8201j.e();
        } catch (w e2) {
            bV.d(e2.getMessage(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        try {
            this.f8201j.d();
        } catch (w e2) {
            bV.d(e2.getMessage(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        try {
            this.f8201j.f();
        } catch (w e2) {
            bV.d(e2.getMessage(), this);
        }
    }

    public void a() {
        if (bV.a(C1818g.b("Are you sure you want to format the SD card?") + "\n" + C1818g.b("All files will be deleted."), (Component) this, true)) {
            new m(this).start();
        }
    }

    public void b() {
        JRootPane rootPane = getRootPane();
        if (rootPane.getGlassPane() instanceof dO) {
            this.f8215w = (dO) rootPane.getGlassPane();
        } else {
            this.f8215w.b(true);
            rootPane.setGlassPane(this.f8215w);
            rootPane.getGlassPane().setVisible(true);
        }
        this.f8215w.a(C1818g.b("Formatting SD Card, Please wait"));
        this.f8215w.setVisible(true);
    }

    public void c() {
        this.f8215w.setVisible(false);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) throws IllegalArgumentException {
        boolean z2 = (((int) d2) & aI.d.f2439d) == aI.d.f2439d;
        boolean z3 = (((int) d2) & aI.d.f2438c) == aI.d.f2438c;
        this.f8209r.a(z3);
        this.f8202k.setEnabled(z3 || z2);
        this.f8210s.a((((int) d2) & aI.d.f2440e) == aI.d.f2440e);
        boolean z4 = (((int) d2) & aI.d.f2437b) == aI.d.f2437b;
        this.f8211t.a(z4);
        if (z4 != this.f8204m.isEnabled()) {
            this.f8204m.setEnabled(z4);
            this.f8205n.setEnabled(z4);
            this.f8206o.setEnabled(z4);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8202k.close();
        this.f8208q.close();
        this.f8203l.close();
        this.f8201j.b(this);
        C0113cs.a().a(this);
    }

    @Override // aI.q
    public void a(boolean z2) {
        this.f8217y = z2;
        g();
    }

    @Override // aI.q
    public void b(boolean z2) throws IllegalArgumentException {
        this.f8216x = z2;
        this.f8208q.a(z2);
        g();
    }

    public void a(aI.l lVar) {
        this.f8213z = lVar;
        g();
    }

    private void g() {
        this.f8207p.setEnabled((this.f8213z == null || this.f8216x || !this.f8217y) ? false : true);
    }
}
