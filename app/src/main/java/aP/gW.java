package aP;

import br.C1233K;
import com.efiAnalytics.apps.ts.dashboard.C1389ab;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.tuningViews.C1441n;
import com.efiAnalytics.tunerStudio.search.ContinuousIpSearchPanel;
import com.efiAnalytics.tuningwidgets.panels.C1487ad;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/gW.class */
public class gW extends JPanel implements G.S, aE.e {

    /* renamed from: a, reason: collision with root package name */
    C1425x f3450a;

    /* renamed from: b, reason: collision with root package name */
    bT f3451b;

    /* renamed from: c, reason: collision with root package name */
    C0238bg f3452c;

    /* renamed from: d, reason: collision with root package name */
    C0289dd f3453d;

    /* renamed from: f, reason: collision with root package name */
    C1233K f3455f;

    /* renamed from: g, reason: collision with root package name */
    jG f3456g;

    /* renamed from: h, reason: collision with root package name */
    C1441n f3457h;

    /* renamed from: i, reason: collision with root package name */
    ContinuousIpSearchPanel f3458i;

    /* renamed from: j, reason: collision with root package name */
    com.efiAnalytics.ui.eD f3459j;

    /* renamed from: m, reason: collision with root package name */
    public static String f3462m = C1818g.b("Gauge Cluster");

    /* renamed from: n, reason: collision with root package name */
    public static String f3463n = C1818g.b("Diagnostics & High Speed Loggers");

    /* renamed from: o, reason: collision with root package name */
    public static String f3464o = C1818g.b("Tuning");

    /* renamed from: p, reason: collision with root package name */
    public static String f3465p = C1818g.b("Tune Analyze Live! - Tune For You");

    /* renamed from: q, reason: collision with root package name */
    public static String f3466q = C1818g.b("Notes");

    /* renamed from: r, reason: collision with root package name */
    public static String f3467r = C1818g.b("Graphing & Logging");

    /* renamed from: s, reason: collision with root package name */
    public static String f3468s = C1818g.b("Tuning & Dyno Views");

    /* renamed from: t, reason: collision with root package name */
    public static String f3469t = C1818g.b("Register");

    /* renamed from: u, reason: collision with root package name */
    public static String f3470u = C1818g.b("Devices");

    /* renamed from: v, reason: collision with root package name */
    public static String f3471v = C1818g.b("Shop");

    /* renamed from: e, reason: collision with root package name */
    n.n f3454e = new n.n();

    /* renamed from: k, reason: collision with root package name */
    ArrayList f3460k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    ArrayList f3461l = new ArrayList();

    public gW() {
        this.f3450a = null;
        this.f3451b = null;
        this.f3452c = null;
        this.f3453d = null;
        this.f3455f = null;
        this.f3456g = null;
        this.f3457h = null;
        this.f3458i = null;
        this.f3459j = null;
        setLayout(new BorderLayout());
        cZ.a().a(this);
        cZ.a().a(this.f3454e);
        if (C1806i.a().a("-0spofdspo09432")) {
            add(BorderLayout.CENTER, this.f3454e);
            a(f3469t, new C1487ad());
            return;
        }
        C0338f.a().a(this);
        boolean z2 = C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt);
        if (C1798a.a().c(C1798a.f13386bp, false)) {
            this.f3450a = new C1425x();
            this.f3450a.setName(C1818g.b("Main Dashboard"));
            cZ.a().a(this.f3450a);
            this.f3450a = C1389ab.a(this.f3450a);
            add(BorderLayout.CENTER, this.f3450a);
            this.f3450a.c();
        } else {
            add(BorderLayout.CENTER, this.f3454e);
            if (C1806i.a().a("64865e43s5hjhcurd")) {
                this.f3451b = new bT();
                this.f3460k.add(this.f3451b);
                cZ.a().a(this.f3451b);
                this.f3450a = new C1425x();
                this.f3450a.setName(C1818g.b("Main Dashboard"));
                cZ.a().a(this.f3450a);
                this.f3450a = C1389ab.a(this.f3450a);
                this.f3451b.a(this.f3450a, C1818g.b("Main Dashboard"));
                this.f3450a.c();
                this.f3451b.a(new gX(this));
                a(f3462m, this.f3451b);
                this.f3451b.a();
            } else {
                this.f3450a = new C1425x();
                this.f3450a.setName("Main Dashboard");
                cZ.a().a(this.f3450a);
                this.f3450a = C1389ab.a(this.f3450a);
                this.f3450a.c();
                a(f3462m, this.f3450a);
            }
            if (!z2) {
                if (!C1806i.a().a(";oAW:OD iqw [PZSD]")) {
                    this.f3457h = new C1441n();
                    a(f3468s, this.f3457h);
                    cZ.a().a(this.f3457h);
                    com.efiAnalytics.apps.ts.tuningViews.z zVar = new com.efiAnalytics.apps.ts.tuningViews.z(this.f3457h);
                    this.f3457h.a(zVar);
                    C0338f.a().a(zVar);
                    G.T.a().a(this.f3457h);
                    this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, false);
                    this.f3457h.addChangeListener(new iW(this.f3457h));
                }
                if (!C1806i.a().a(" j;awerf90wer09uavn")) {
                    this.f3453d = new C0289dd();
                    a(f3467r, this.f3453d);
                    this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, false);
                }
                if (C1806i.a().a("oijmoijmf boij reoij")) {
                    this.f3452c = new C0238bg();
                    a(f3463n, this.f3452c);
                    this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, false);
                }
                if (C1806i.a().a("-0ofdspok54sg")) {
                    this.f3455f = new C1233K();
                    a(f3465p, this.f3455f);
                    this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, false);
                    cZ.a().a(this.f3455f);
                } else {
                    this.f3455f = new C1233K();
                    a(f3465p, this.f3455f);
                    this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, false);
                }
                this.f3456g = new jG();
                G.T.a().a(new gY(this));
                a(f3466q, this.f3456g);
                this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, false);
            }
            if (C1806i.a().a("5fdsrewpok3=+3vcx-")) {
                try {
                    this.f3459j = new com.efiAnalytics.ui.eD();
                    a(f3471v, this.f3459j);
                } catch (Error e2) {
                    bH.C.b("No JavaFX, skipping Shop Tab.");
                } catch (Exception e3) {
                    bH.C.b("No JavaFX, skipping Shop Tab.");
                }
            }
            this.f3454e.addChangeListener(new iW(this.f3454e));
            c();
        }
        if (C1806i.a().a("kjlkgoi098")) {
            this.f3458i = new ContinuousIpSearchPanel();
            a(f3470u, this.f3458i);
            this.f3454e.setEnabledAt(this.f3454e.getTabCount() - 1, true);
            this.f3454e.g(f3470u);
        }
        setOpaque(true);
    }

    public void a(String str, Component component) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(0, 0));
        jPanel.add(BorderLayout.CENTER, component);
        com.efiAnalytics.ui.fG fGVar = new com.efiAnalytics.ui.fG();
        fGVar.setForeground(new Color(0, 0, 192));
        G.aB.a().a(new C0393ha(this, fGVar));
        this.f3460k.add(component);
        jPanel.add("North", fGVar);
        this.f3454e.addTab(str, jPanel);
    }

    public n.n a() {
        return this.f3454e;
    }

    @Override // G.S
    public void a(G.R r2) {
        this.f3450a.a(r2);
    }

    @Override // G.S
    public void b(G.R r2) {
    }

    public void c() {
        int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
        Font font = this.f3454e.getFont();
        Font font2 = new Font(font.getFamily(), font.getStyle(), iA);
        this.f3454e.setFont(font2);
        this.f3454e.validate();
        if (this.f3451b != null) {
            this.f3451b.setFont(font2);
            this.f3451b.validate();
        }
    }

    @Override // G.S
    public void c(G.R r2) {
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        SwingUtilities.invokeLater(new gZ(this, r2, aVar));
    }

    @Override // aE.e
    public void e_() {
        Iterator it = this.f3460k.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof aE.e) {
                try {
                    ((aE.e) next).e_();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        if (this.f3454e != null) {
            Iterator it2 = this.f3461l.iterator();
            while (it2.hasNext()) {
                this.f3454e.removeTabAt(this.f3454e.f((String) it2.next()));
            }
            this.f3461l.clear();
            this.f3454e.a(f3465p, false);
            this.f3454e.a(f3463n, false);
            this.f3454e.a(f3467r, false);
            this.f3454e.a(f3466q, false);
            this.f3454e.a(f3468s, false);
            C0338f.a().Q();
        }
    }

    @Override // aE.e
    public void a(aE.a aVar) {
        if (this.f3456g != null) {
            this.f3456g.b();
        }
    }
}
