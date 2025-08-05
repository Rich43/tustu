package aO;

import G.C0096cb;
import G.C0097cc;
import G.C0098cd;
import W.C0184j;
import W.C0188n;
import W.C0189o;
import bH.C;
import com.efiAnalytics.ui.C1699q;
import com.efiAnalytics.ui.InterfaceC1602cm;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.aS;
import com.efiAnalytics.ui.aU;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.eK;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/* loaded from: TunerStudioMS.jar:aO/k.class */
public class k extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    a f2665a;

    /* renamed from: K, reason: collision with root package name */
    private boolean f2697K;

    /* renamed from: D, reason: collision with root package name */
    boolean f2701D;

    /* renamed from: E, reason: collision with root package name */
    JScrollPane f2702E;

    /* renamed from: v, reason: collision with root package name */
    static String f2690v = "IgnitionLoggerCluster";

    /* renamed from: w, reason: collision with root package name */
    static String f2691w = "splitPanePosition";

    /* renamed from: x, reason: collision with root package name */
    static String f2692x = "selectedGaugeTab";

    /* renamed from: y, reason: collision with root package name */
    static String f2693y = "zoom";

    /* renamed from: z, reason: collision with root package name */
    static String f2694z = "pollingDelay";

    /* renamed from: A, reason: collision with root package name */
    public static boolean f2695A = false;

    /* renamed from: B, reason: collision with root package name */
    static String f2696B = "renderAllData";

    /* renamed from: C, reason: collision with root package name */
    static int f2699C = 100;

    /* renamed from: b, reason: collision with root package name */
    JTextArea f2666b = new JTextArea();

    /* renamed from: c, reason: collision with root package name */
    o f2667c = new o(this);

    /* renamed from: d, reason: collision with root package name */
    JTable f2668d = new JTable(this.f2667c);

    /* renamed from: e, reason: collision with root package name */
    n f2669e = new n(this);

    /* renamed from: f, reason: collision with root package name */
    C1699q f2670f = new C1699q();

    /* renamed from: g, reason: collision with root package name */
    JSplitPane f2671g = new JSplitPane();

    /* renamed from: h, reason: collision with root package name */
    aU f2672h = new aU();

    /* renamed from: i, reason: collision with root package name */
    JPanel f2673i = new JPanel();

    /* renamed from: j, reason: collision with root package name */
    JPanel f2674j = new JPanel();

    /* renamed from: k, reason: collision with root package name */
    C0189o f2675k = null;

    /* renamed from: l, reason: collision with root package name */
    boolean f2676l = false;

    /* renamed from: m, reason: collision with root package name */
    q f2677m = new q(this);

    /* renamed from: n, reason: collision with root package name */
    aS f2678n = null;

    /* renamed from: o, reason: collision with root package name */
    C0188n f2679o = null;

    /* renamed from: p, reason: collision with root package name */
    double f2680p = 1.0d;

    /* renamed from: q, reason: collision with root package name */
    int f2681q = 0;

    /* renamed from: r, reason: collision with root package name */
    int f2682r = 0;

    /* renamed from: G, reason: collision with root package name */
    private InterfaceC1662et f2683G = null;

    /* renamed from: s, reason: collision with root package name */
    eK f2684s = new eK();

    /* renamed from: t, reason: collision with root package name */
    boolean f2685t = true;

    /* renamed from: u, reason: collision with root package name */
    C.d f2686u = new C.c();

    /* renamed from: H, reason: collision with root package name */
    private j f2687H = null;

    /* renamed from: I, reason: collision with root package name */
    private String f2688I = this.f2686u.a("Raw Log Page");

    /* renamed from: J, reason: collision with root package name */
    private String f2689J = this.f2686u.a("Data View");

    /* renamed from: L, reason: collision with root package name */
    private boolean f2698L = f2695A;

    /* renamed from: M, reason: collision with root package name */
    private int f2700M = f2699C;

    /* renamed from: F, reason: collision with root package name */
    boolean f2703F = true;

    public k(boolean z2, boolean z3) {
        this.f2665a = null;
        this.f2697K = false;
        this.f2701D = false;
        this.f2701D = z3;
        this.f2697K = z2;
        this.f2665a = new a(this);
        this.f2672h.a(Color.yellow);
        this.f2672h.d(3.0d);
        this.f2672h.c(-1.0d);
        this.f2672h.f(4);
        this.f2672h.a(new p(this));
        this.f2672h.addMouseWheelListener(new m(this));
        this.f2670f.addMouseWheelListener(new m(this));
        setLayout(new BorderLayout());
        this.f2666b.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f2666b.setColumns(35);
        this.f2666b.setTabSize(5);
        this.f2666b.setFont(UIManager.getFont("TextField.font"));
        this.f2666b.setEditable(false);
        this.f2702E = new JScrollPane(this.f2668d);
        JScrollPane jScrollPane = new JScrollPane(this.f2666b);
        if (this.f2703F) {
            this.f2684s.addTab(this.f2689J, this.f2702E);
            this.f2668d.setRowHeight(getFont().getSize() + eJ.a(4));
        }
        this.f2684s.addTab(this.f2688I, jScrollPane);
        this.f2684s.setTabPlacement(3);
        add("North", this.f2665a);
        add(BorderLayout.CENTER, this.f2671g);
        this.f2673i.setLayout(new BorderLayout());
        this.f2674j.setLayout(new BorderLayout());
        this.f2671g.setRightComponent(this.f2673i);
        if (z2) {
            add("South", this.f2677m);
            this.f2671g.setLeftComponent(this.f2684s);
        } else {
            this.f2671g.setLeftComponent(this.f2702E);
        }
        this.f2671g.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new l(this));
        this.f2671g.setOneTouchExpandable(true);
    }

    public void b() throws NumberFormatException {
        if (this.f2683G == null) {
            return;
        }
        this.f2671g.setDividerLocation(Integer.parseInt(a(f2691w, "" + this.f2684s.getPreferredSize().width)));
        a(Double.parseDouble(a(f2693y, "1.0")), 0.0d);
        this.f2684s.g(a(f2692x, this.f2689J));
    }

    private String a(String str, String str2) {
        String strA = null;
        if (this.f2683G != null) {
            strA = this.f2683G.a(str);
        }
        return (strA == null || strA.equals("")) ? str2 : strA;
    }

    private void b(String str, String str2) {
        if (this.f2683G != null) {
            this.f2683G.a(str, str2);
        }
    }

    protected void a(ArrayList arrayList) {
        this.f2665a.a(arrayList);
    }

    protected void a(InterfaceC1602cm interfaceC1602cm) {
        this.f2665a.a(interfaceC1602cm);
    }

    protected void a(C0096cb c0096cb) {
        this.f2669e.clear();
        this.f2666b.setText("");
        if (c0096cb.d().equals(C0096cb.f1065a)) {
            a(this.f2672h);
            this.f2672h.b(c0096cb.g());
            this.f2672h.c(c0096cb.a());
            this.f2672h.a();
            this.f2672h.repaint();
            C0097cc c0097ccA = c0096cb.b().a("ToothTime");
            this.f2672h.d(c0097ccA != null ? c0097ccA.d() : "");
            return;
        }
        a(this.f2670f);
        this.f2670f.b(c0096cb.g());
        this.f2670f.c(c0096cb.a());
        this.f2670f.a();
        this.f2670f.d(0.0d);
        C0097cc c0097ccA2 = c0096cb.b().a("ToothTime");
        if (c0097ccA2 == null) {
            c0097ccA2 = c0096cb.b().a("TriggerTime");
        }
        this.f2670f.e(c0097ccA2 != null ? c0097ccA2.d() : "");
        this.f2670f.repaint();
    }

    void c() {
        try {
            this.f2675k.a();
        } catch (Exception e2) {
        }
        this.f2675k = null;
    }

    private void a(aS aSVar) {
        if (this.f2678n != null) {
            this.f2673i.remove(this.f2678n);
        }
        this.f2678n = aSVar;
        this.f2673i.add(BorderLayout.CENTER, aSVar);
        this.f2673i.doLayout();
    }

    private C0096cb e(C0188n c0188n) {
        Iterator it = this.f2687H.a(c0188n.f()).iterator();
        while (it.hasNext()) {
            C0096cb c0096cb = (C0096cb) it.next();
            if (a(c0188n, c0096cb.b())) {
                return c0096cb;
            }
        }
        return null;
    }

    private boolean a(C0188n c0188n, C0098cd c0098cd) {
        Iterator it = c0098cd.j().iterator();
        while (it.hasNext()) {
            if (c0188n.a((String) it.next()) == null) {
                return false;
            }
        }
        return true;
    }

    public void b(ArrayList arrayList) {
        this.f2685t = false;
        if (arrayList.size() == 0) {
            bV.d("Data set empty, can't load it.", this);
            return;
        }
        C0096cb c0096cbE = e((C0188n) arrayList.get(0));
        if (c0096cbE == null) {
            bV.d("Unable to identify log file type.", this);
            return;
        }
        this.f2665a.b(c0096cbE.g());
        this.f2669e.clear();
        int i2 = 0;
        int iD = this.f2669e.d();
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (i2 == iD) {
                bV.d("Data set size exceeds current maximum of " + iD + ".\nOnly the first " + iD + " will be loaded.", this);
                break;
            } else {
                a((C0188n) it.next());
                i2++;
            }
        }
        a(0);
    }

    public void a(int i2) {
        try {
            f(this.f2669e.a(i2));
        } catch (V.a e2) {
            C.a("Unable to display page " + i2, e2, this);
        }
    }

    public void a(C0188n c0188n) {
        if (this.f2685t) {
            this.f2685t = false;
        } else {
            this.f2669e.add(c0188n);
        }
    }

    protected void d() {
        this.f2677m.a(this.f2669e.size());
        this.f2677m.b(this.f2669e.c() + 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(C0188n c0188n) {
        if (c0188n == null) {
            return;
        }
        this.f2679o = c0188n;
        if (c0188n.d() != this.f2682r) {
            this.f2682r = c0188n.d();
            this.f2677m.b();
        }
        d();
        try {
            i(c0188n);
        } catch (V.a e2) {
            C.a("There was an error reading this log file.", e2, this);
        }
        b(c0188n);
    }

    protected void b(C0188n c0188n) {
        if (j()) {
            d(c0188n);
        } else {
            c(c0188n);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x02e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void c(W.C0188n r9) {
        /*
            Method dump skipped, instructions count: 2464
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aO.k.c(W.n):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x02d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void d(W.C0188n r9) {
        /*
            Method dump skipped, instructions count: 2477
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aO.k.d(W.n):void");
    }

    private double g(C0188n c0188n) {
        C0184j c0184jA = c0188n.a("Time");
        for (int i2 = 0; i2 < c0188n.d(); i2++) {
            if (c0184jA.c(i2) != 0.0f) {
                return c0184jA.c(i2);
            }
        }
        return 0.0d;
    }

    private double h(C0188n c0188n) {
        C0184j c0184jA = c0188n.a("Time");
        for (int iD = c0188n.d() - 1; iD >= 0; iD--) {
            if (c0184jA.c(iD) != 0.0f) {
                return c0184jA.c(iD);
            }
        }
        return 0.0d;
    }

    private double a(C0188n c0188n, double d2) {
        C0184j c0184jA = c0188n.a("Time");
        C0184j c0184jA2 = c0188n.a("SecLevel");
        C0184j c0184jA3 = c0188n.a("Trigger");
        for (int i2 = 0; i2 < c0188n.d(); i2++) {
            if (c0184jA.c(i2) > d2 && c0184jA3.c(i2) == 1.0f) {
                return c0184jA2.c(i2);
            }
        }
        return -1.0d;
    }

    private double b(C0188n c0188n, double d2) {
        C0184j c0184jA = c0188n.a("Time");
        C0184j c0184jA2 = c0188n.a("SecLevel");
        C0184j c0184jA3 = c0188n.a("Trigger");
        for (int iD = c0188n.d() - 1; iD >= 0; iD--) {
            if (c0184jA.c(iD) < d2 && c0184jA3.c(iD) == 1.0f) {
                return c0184jA2.c(iD);
            }
        }
        return -1.0d;
    }

    protected void e() {
        this.f2681q = this.f2677m.d();
        if (this.f2679o != null) {
            b(this.f2679o);
        }
    }

    private void i(C0188n c0188n) throws V.a {
        if (this.f2666b.isDisplayable() || this.f2668d.isDisplayable()) {
            if (c0188n.d() == 0) {
                this.f2666b.setText("Empty read, No Data Received from Controller");
                this.f2667c.getDataVector().clear();
                this.f2667c.fireTableDataChanged();
            } else {
                this.f2666b.setText("");
                Vector vector = new Vector();
                Vector vector2 = new Vector();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(c0188n.g() + "\n");
                for (int i2 = 0; i2 < c0188n.size(); i2++) {
                    C0184j c0184j = (C0184j) c0188n.get(i2);
                    stringBuffer.append(c0184j.a());
                    vector.add(c0184j.a());
                    if (i2 < c0188n.size() - 1) {
                        stringBuffer.append("\t");
                    }
                }
                stringBuffer.append("\n");
                for (int i3 = 0; i3 < c0188n.size(); i3++) {
                    try {
                        stringBuffer.append(((C0184j) c0188n.get(i3)).n());
                        if (i3 < c0188n.size() - 1) {
                            stringBuffer.append("\t");
                        }
                    } catch (Exception e2) {
                        throw new V.a("Invalid units row in log file.");
                    }
                }
                stringBuffer.append("\n");
                if (this.f2703F) {
                    for (int i4 = 0; i4 < c0188n.d(); i4++) {
                        Vector vector3 = new Vector();
                        vector2.add(vector3);
                        for (int i5 = 0; i5 < c0188n.size(); i5++) {
                            vector3.add(Float.valueOf(((C0184j) c0188n.get(i5)).d(i4)));
                        }
                    }
                    synchronized (this.f2667c) {
                        this.f2667c.setDataVector(vector2, vector);
                    }
                }
                if (c0188n.d() > 0) {
                    for (int i6 = 0; i6 < c0188n.d(); i6++) {
                        for (int i7 = 0; i7 < c0188n.size(); i7++) {
                            try {
                                stringBuffer.append(((C0184j) c0188n.get(i7)).c(i6) + "");
                                if (i7 < c0188n.size() - 1) {
                                    stringBuffer.append("\t");
                                }
                            } catch (Exception e3) {
                                throw new V.a("Invalid data found at record: " + i6 + "\nThis file does not appear valid.");
                            }
                        }
                        stringBuffer.append("\n");
                    }
                } else {
                    stringBuffer.append("This read contained no data.");
                    ArrayList arrayList = new ArrayList();
                    for (int i8 = 0; i8 < this.f2669e.size(); i8++) {
                        if (((C0188n) this.f2669e.get(i8)).d() > 0) {
                            arrayList.add(new Integer(i8 + 1));
                        }
                    }
                    if (arrayList.size() > 0) {
                        stringBuffer.append("Data Was receive for pages:\n");
                        for (int i9 = 0; i9 < arrayList.size(); i9++) {
                            stringBuffer.append(arrayList.get(i9)).append("\n");
                        }
                    } else {
                        stringBuffer.append("No viewable Data in entire file");
                    }
                }
                this.f2666b.setText(stringBuffer.toString());
            }
            this.f2666b.setCaretPosition(0);
        }
    }

    protected void a(double d2) {
        if (this.f2680p / 2.0d > 0.0625d) {
            a(this.f2680p / 2.0d, d2);
        } else {
            a(0.0625d, d2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a() {
        return (int) Math.round(this.f2682r * this.f2680p);
    }

    public void a(double d2, double d3) {
        double dA;
        double dC = this.f2677m.c();
        if (d2 == this.f2680p) {
            return;
        }
        if (d2 > this.f2680p) {
            this.f2680p = d2;
            dA = (-((a() * d3) / this.f2682r)) / 2.0d;
        } else {
            dA = ((a() * d3) / this.f2682r) / 2.0d;
            this.f2680p = d2;
        }
        b(f2693y, d2 + "");
        this.f2677m.b();
        this.f2677m.a(dC + dA);
        e();
    }

    protected void b(double d2) {
        if (this.f2680p * 2.0d < 1.0d) {
            a(this.f2680p * 2.0d, d2);
        } else {
            a(1.0d, d2);
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f2683G = interfaceC1662et;
    }

    public boolean f() {
        return this.f2697K;
    }

    public void g() {
        this.f2669e.clear();
        this.f2666b.setText("");
        this.f2678n.c();
        this.f2678n.repaint();
        d();
    }

    public boolean h() {
        return this.f2669e.size() > 0;
    }

    public void a(j jVar) {
        this.f2687H = jVar;
    }

    public int i() {
        return this.f2700M;
    }

    public void b(int i2) {
        this.f2700M = i2;
    }

    public void a(String str) throws IllegalArgumentException {
        this.f2665a.a(str);
    }

    public boolean j() {
        return this.f2698L;
    }

    public void a(boolean z2) {
        this.f2698L = z2;
    }

    protected Component k() {
        return this.f2678n;
    }
}
