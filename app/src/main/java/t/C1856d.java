package t;

import com.efiAnalytics.apps.ts.dashboard.DashLabel;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.C1566bd;
import com.efiAnalytics.ui.dQ;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import r.C1798a;
import s.C1818g;

/* renamed from: t.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/d.class */
public class C1856d extends AbstractC1827a implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    JPanel f13855a;

    /* renamed from: b, reason: collision with root package name */
    C1566bd f13856b;

    /* renamed from: c, reason: collision with root package name */
    JRadioButton f13857c;

    /* renamed from: d, reason: collision with root package name */
    JRadioButton f13858d;

    /* renamed from: e, reason: collision with root package name */
    JRadioButton f13859e;

    /* renamed from: g, reason: collision with root package name */
    JRadioButton f13860g;

    /* renamed from: h, reason: collision with root package name */
    JRadioButton f13861h;

    /* renamed from: i, reason: collision with root package name */
    JRadioButton f13862i;

    /* renamed from: j, reason: collision with root package name */
    JRadioButton f13863j;

    /* renamed from: k, reason: collision with root package name */
    JRadioButton f13864k;

    /* renamed from: l, reason: collision with root package name */
    JRadioButton f13865l;

    /* renamed from: m, reason: collision with root package name */
    JRadioButton f13866m;

    /* renamed from: n, reason: collision with root package name */
    JRadioButton f13867n;

    /* renamed from: o, reason: collision with root package name */
    JRadioButton f13868o;

    /* renamed from: p, reason: collision with root package name */
    static String f13869p = C1818g.b("Border Color");

    /* renamed from: q, reason: collision with root package name */
    static String f13870q = C1818g.b("Face Color");

    /* renamed from: r, reason: collision with root package name */
    static String f13871r = C1818g.b("Font Color");

    /* renamed from: s, reason: collision with root package name */
    static String f13872s = C1818g.b("Needle Color");

    /* renamed from: t, reason: collision with root package name */
    static String f13873t = C1818g.b("Warning Color");

    /* renamed from: u, reason: collision with root package name */
    static String f13874u = C1818g.b("Critical Color");

    /* renamed from: v, reason: collision with root package name */
    static String f13875v = C1818g.b("On Background");

    /* renamed from: w, reason: collision with root package name */
    static String f13876w = C1818g.b("On Text Color");

    /* renamed from: x, reason: collision with root package name */
    static String f13877x = C1818g.b("Off Background");

    /* renamed from: y, reason: collision with root package name */
    static String f13878y = C1818g.b("Off Text");

    /* renamed from: z, reason: collision with root package name */
    static String f13879z = C1818g.b("Label Background");

    /* renamed from: A, reason: collision with root package name */
    static String f13880A = C1818g.b("Label Text");

    /* renamed from: B, reason: collision with root package name */
    ButtonGroup f13881B;

    /* renamed from: C, reason: collision with root package name */
    ArrayList f13882C;

    /* renamed from: D, reason: collision with root package name */
    ArrayList f13883D;

    /* renamed from: E, reason: collision with root package name */
    ArrayList f13884E;

    /* renamed from: F, reason: collision with root package name */
    ArrayList f13885F;

    /* renamed from: G, reason: collision with root package name */
    String f13886G;

    /* renamed from: H, reason: collision with root package name */
    JDialog f13887H;

    public C1856d(Window window, C1836ai c1836ai, String str) {
        super(window, str);
        this.f13855a = new JPanel();
        this.f13856b = new C1566bd();
        this.f13881B = new ButtonGroup();
        this.f13882C = new ArrayList();
        this.f13883D = new ArrayList();
        this.f13884E = new ArrayList();
        this.f13885F = new ArrayList();
        this.f13886G = f13869p;
        this.f13887H = null;
        a(new dQ(C1798a.a().f13332an, "DashDesignerColorDialog"));
        a(c1836ai);
        setLayout(new BorderLayout());
        add(this.f13855a, BorderLayout.CENTER);
        this.f13855a.setLayout(new BorderLayout());
        this.f13855a.add(this.f13856b, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3, 0));
        C1857e c1857e = new C1857e(this);
        this.f13884E.add(f13879z);
        this.f13884E.add(f13880A);
        this.f13883D.add(f13875v);
        this.f13883D.add(f13876w);
        this.f13883D.add(f13877x);
        this.f13883D.add(f13878y);
        this.f13882C.add(f13869p);
        this.f13882C.add(f13870q);
        this.f13882C.add(f13871r);
        this.f13882C.add(f13872s);
        this.f13882C.add(f13873t);
        this.f13882C.add(f13874u);
        this.f13857c = new JRadioButton(f13869p);
        this.f13881B.add(this.f13857c);
        this.f13857c.addActionListener(c1857e);
        jPanel.add(this.f13857c);
        this.f13858d = new JRadioButton(f13870q);
        this.f13881B.add(this.f13858d);
        this.f13858d.addActionListener(c1857e);
        jPanel.add(this.f13858d);
        this.f13859e = new JRadioButton(f13871r);
        this.f13881B.add(this.f13859e);
        this.f13859e.addActionListener(c1857e);
        jPanel.add(this.f13859e);
        this.f13860g = new JRadioButton(f13872s);
        this.f13881B.add(this.f13860g);
        this.f13860g.addActionListener(c1857e);
        jPanel.add(this.f13860g);
        this.f13861h = new JRadioButton(f13873t);
        this.f13881B.add(this.f13861h);
        this.f13861h.addActionListener(c1857e);
        jPanel.add(this.f13861h);
        this.f13862i = new JRadioButton(f13874u);
        this.f13881B.add(this.f13862i);
        this.f13862i.addActionListener(c1857e);
        jPanel.add(this.f13862i);
        this.f13867n = new JRadioButton(f13879z);
        this.f13881B.add(this.f13867n);
        this.f13867n.addActionListener(c1857e);
        jPanel.add(this.f13867n);
        this.f13868o = new JRadioButton(f13880A);
        this.f13881B.add(this.f13868o);
        this.f13868o.addActionListener(c1857e);
        jPanel.add(this.f13868o);
        this.f13863j = new JRadioButton(f13875v);
        this.f13881B.add(this.f13863j);
        this.f13863j.addActionListener(c1857e);
        jPanel.add(this.f13863j);
        this.f13864k = new JRadioButton(f13876w);
        this.f13881B.add(this.f13864k);
        this.f13864k.addActionListener(c1857e);
        jPanel.add(this.f13864k);
        this.f13865l = new JRadioButton(f13877x);
        this.f13881B.add(this.f13865l);
        this.f13865l.addActionListener(c1857e);
        jPanel.add(this.f13865l);
        this.f13866m = new JRadioButton(f13878y);
        this.f13881B.add(this.f13866m);
        this.f13866m.addActionListener(c1857e);
        jPanel.add(this.f13866m);
        this.f13855a.add(jPanel, "North");
        pack();
        this.f13886G = b().b(JInternalFrame.IS_SELECTED_PROPERTY, f13869p);
        a(this.f13886G);
    }

    public void a(String str) {
        Gauge gaugeB = b(this.f13885F);
        Indicator indicatorC = c(this.f13885F);
        DashLabel dashLabelD = d(this.f13885F);
        this.f13886G = str;
        b().a(JInternalFrame.IS_SELECTED_PROPERTY, str);
        if (str.equals(f13869p)) {
            b().a("lastGaugeRadio", str);
            this.f13856b.a(new C1862j(this));
            if (gaugeB != null) {
                this.f13856b.a(gaugeB.getTrimColor());
            }
        } else if (str.equals(f13870q)) {
            b().a("lastGaugeRadio", str);
            this.f13856b.a(new C1863k(this));
            if (gaugeB != null) {
                this.f13856b.a(gaugeB.getBackColor());
            }
        } else if (str.equals(f13871r)) {
            b().a("lastGaugeRadio", str);
            this.f13856b.a(new C1864l(this));
            if (gaugeB != null) {
                this.f13856b.a(gaugeB.getFontColor());
            }
        } else if (str.equals(f13872s)) {
            b().a("lastGaugeRadio", str);
            this.f13856b.a(new C1865m(this));
            if (gaugeB != null) {
                this.f13856b.a(gaugeB.getNeedleColor());
            }
        } else if (str.equals(f13873t)) {
            b().a("lastGaugeRadio", str);
            this.f13856b.a(new C1866n(this));
            if (gaugeB != null) {
                this.f13856b.a(gaugeB.getWarnColor());
            }
        } else if (str.equals(f13874u)) {
            b().a("lastGaugeRadio", str);
            this.f13856b.a(new C1867o(this));
            if (gaugeB != null) {
                this.f13856b.a(gaugeB.getCriticalColor());
            }
        } else if (str.equals(f13875v)) {
            b().a("lastIndRadio", str);
            this.f13856b.a(new C1868p(this));
            if (indicatorC != null) {
                this.f13856b.a(indicatorC.getOnBackgroundColor());
            }
        } else if (str.equals(f13876w)) {
            b().a("lastIndRadio", str);
            this.f13856b.a(new C1869q(this));
            if (indicatorC != null) {
                this.f13856b.a(indicatorC.getOnTextColor());
            }
        } else if (str.equals(f13877x)) {
            b().a("lastIndRadio", str);
            this.f13856b.a(new C1858f(this));
            if (indicatorC != null) {
                this.f13856b.a(indicatorC.getOffBackgroundColor());
            }
        } else if (str.equals(f13878y)) {
            b().a("lastIndRadio", str);
            this.f13856b.a(new C1859g(this));
            if (indicatorC != null) {
                this.f13856b.a(indicatorC.getOffTextColor());
            }
        } else if (str.equals(f13879z)) {
            b().a("lastLabelRadio", str);
            this.f13856b.a(new C1860h(this));
            if (dashLabelD != null) {
                this.f13856b.a(dashLabelD.getBackgroundColor());
            }
        } else if (str.equals(f13880A)) {
            b().a("lastLabelRadio", str);
            this.f13856b.a(new C1861i(this));
            if (dashLabelD != null) {
                this.f13856b.a(dashLabelD.getTextColor());
            }
        }
        this.f13857c.setEnabled(gaugeB != null);
        this.f13858d.setEnabled(gaugeB != null);
        this.f13859e.setEnabled(gaugeB != null);
        this.f13860g.setEnabled(gaugeB != null);
        this.f13861h.setEnabled(gaugeB != null);
        this.f13862i.setEnabled(gaugeB != null);
        this.f13863j.setEnabled(indicatorC != null);
        this.f13864k.setEnabled(indicatorC != null);
        this.f13865l.setEnabled(indicatorC != null);
        this.f13866m.setEnabled(indicatorC != null);
        this.f13867n.setEnabled(dashLabelD != null);
        this.f13868o.setEnabled(dashLabelD != null);
        boolean z2 = false;
        Enumeration<AbstractButton> elements = this.f13881B.getElements();
        while (true) {
            if (!elements.hasMoreElements()) {
                break;
            }
            JRadioButton jRadioButton = (JRadioButton) elements.nextElement2();
            if (jRadioButton.isEnabled() && jRadioButton.getText().equals(str)) {
                jRadioButton.setSelected(true);
                z2 = true;
                break;
            }
        }
        if (z2) {
            return;
        }
        Enumeration<AbstractButton> elements2 = this.f13881B.getElements();
        while (elements2.hasMoreElements()) {
            JRadioButton jRadioButton2 = (JRadioButton) elements2.nextElement2();
            if (jRadioButton2.isEnabled()) {
                jRadioButton2.setSelected(true);
                return;
            }
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        String strB = this.f13886G;
        if (!arrayList.isEmpty()) {
            if (arrayList.get(0) instanceof Gauge) {
                if (!this.f13882C.contains(strB)) {
                    strB = b().b("lastGaugeRadio", f13869p);
                }
            } else if (arrayList.get(0) instanceof Indicator) {
                if (!this.f13883D.contains(strB)) {
                    strB = b().b("lastIndRadio", f13875v);
                }
            } else if ((arrayList.get(0) instanceof DashLabel) && !this.f13884E.contains(strB)) {
                strB = b().b("lastLabelRadio", f13879z);
            }
        }
        this.f13885F.clear();
        this.f13885F.addAll(arrayList);
        a(strB);
    }
}
