package bt;

import G.C0079bl;
import G.C0113cs;
import G.InterfaceC0109co;
import W.C0184j;
import W.C0188n;
import W.InterfaceC0183i;
import bH.C1007o;
import bH.C1012t;
import bH.InterfaceC1014v;
import c.InterfaceC1385d;
import com.efiAnalytics.tuningwidgets.panels.C1483a;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.C1700r;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1648ef;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.eJ;
import i.C1743c;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import k.C1756d;
import org.apache.commons.math3.geometry.VectorFormat;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.al, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/al.class */
public class C1303al extends JPanel implements G.aN, InterfaceC0109co, InterfaceC1282J, InterfaceC1385d, com.efiAnalytics.ui.bS, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f8844a;

    /* renamed from: d, reason: collision with root package name */
    static final String f8847d = C1818g.b("Set X Axis Minimum");

    /* renamed from: e, reason: collision with root package name */
    static final String f8848e = C1818g.b("Set X Axis Maximum");

    /* renamed from: f, reason: collision with root package name */
    static final String f8849f = C1818g.b("Auto Scale X Axis Maximum");

    /* renamed from: g, reason: collision with root package name */
    static final String f8850g = C1818g.b("Set Y Axis Minimum");

    /* renamed from: h, reason: collision with root package name */
    static final String f8851h = C1818g.b("Set Y Axis Maximum");

    /* renamed from: i, reason: collision with root package name */
    static final String f8852i = C1818g.b("Auto Scale Y Axis Maximum");

    /* renamed from: j, reason: collision with root package name */
    static final String f8853j = C1818g.b("Selection Follow Mode") + " (CTRL+F)";

    /* renamed from: k, reason: collision with root package name */
    static final String f8854k = C1818g.b("Set History Timeout");

    /* renamed from: l, reason: collision with root package name */
    static final String f8855l = C1818g.b("Show baseline graph") + " (CTRL+B)";

    /* renamed from: m, reason: collision with root package name */
    static final String f8856m = C1818g.b("Revert to baseline");

    /* renamed from: n, reason: collision with root package name */
    static final String f8857n = C1818g.b("Show X-Y Data Log Plot") + " (CTRL+X)";

    /* renamed from: o, reason: collision with root package name */
    static final String f8858o = C1818g.b("Set Coarse Step Count");

    /* renamed from: Q, reason: collision with root package name */
    private boolean f8860Q;

    /* renamed from: p, reason: collision with root package name */
    com.efiAnalytics.ui.bN f8861p;

    /* renamed from: q, reason: collision with root package name */
    C0079bl f8862q;

    /* renamed from: r, reason: collision with root package name */
    aG f8863r;

    /* renamed from: s, reason: collision with root package name */
    aF f8864s;

    /* renamed from: t, reason: collision with root package name */
    JPanel f8865t;

    /* renamed from: u, reason: collision with root package name */
    aJ f8866u;

    /* renamed from: v, reason: collision with root package name */
    C1290R f8867v;

    /* renamed from: w, reason: collision with root package name */
    C1290R f8868w;

    /* renamed from: x, reason: collision with root package name */
    int[] f8869x;

    /* renamed from: z, reason: collision with root package name */
    InterfaceC1662et f8871z;

    /* renamed from: B, reason: collision with root package name */
    aP f8873B;

    /* renamed from: C, reason: collision with root package name */
    aQ f8874C;

    /* renamed from: E, reason: collision with root package name */
    boolean f8876E;

    /* renamed from: F, reason: collision with root package name */
    boolean f8877F;

    /* renamed from: I, reason: collision with root package name */
    aM f8880I;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f8845b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f8846c = new ArrayList();

    /* renamed from: P, reason: collision with root package name */
    private String f8859P = null;

    /* renamed from: y, reason: collision with root package name */
    boolean f8870y = false;

    /* renamed from: A, reason: collision with root package name */
    aN f8872A = null;

    /* renamed from: D, reason: collision with root package name */
    aS f8875D = null;

    /* renamed from: G, reason: collision with root package name */
    aR f8878G = new aR(this);

    /* renamed from: H, reason: collision with root package name */
    boolean f8879H = false;

    /* renamed from: J, reason: collision with root package name */
    bN f8881J = null;

    /* renamed from: K, reason: collision with root package name */
    aI f8882K = new aI(this);

    /* renamed from: L, reason: collision with root package name */
    C1483a f8883L = null;

    /* renamed from: M, reason: collision with root package name */
    aH f8884M = null;

    /* renamed from: N, reason: collision with root package name */
    InterfaceC0183i f8885N = new aK(this);

    /* renamed from: O, reason: collision with root package name */
    List f8886O = new ArrayList();

    /* JADX WARN: Removed duplicated region for block: B:29:0x01c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public C1303al(G.R r10, G.C0079bl r11, boolean r12, com.efiAnalytics.ui.InterfaceC1662et r13) throws V.a {
        /*
            Method dump skipped, instructions count: 3037
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bt.C1303al.<init>(G.R, G.bl, boolean, com.efiAnalytics.ui.et):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void b(boolean z2) throws V.a {
        this.f8863r = new aG(this);
        for (int i2 = 0; i2 < this.f8862q.j(); i2++) {
            G.aM aMVarC = this.f8844a.c(this.f8862q.d(i2));
            if (aMVarC == null) {
                throw new V.a("CurveGraph X Parameter " + this.f8862q.d(i2) + " not found in the current configuration.");
            }
            this.f8846c.add(aMVarC);
        }
        for (int i3 = 0; i3 < this.f8862q.d(); i3++) {
            this.f8845b.add(this.f8844a.c(this.f8862q.b(i3)));
        }
        if (this.f8845b.size() == 0) {
            com.efiAnalytics.ui.bV.d("No Y Axis Bins for 1D Arrays " + this.f8862q.aJ() + "\nthe Y Axis must have at least 1 Y Axis Bin assigned.", this);
        }
        if (this.f8877F && this.f8876E) {
            try {
                this.f8868w = new C1290R(this.f8844a, this.f8862q, false, z2);
                this.f8868w.a(this.f8878G);
                this.f8868w.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                ArrayList arrayList = new ArrayList();
                arrayList.add(Color.GREEN);
                arrayList.add(Color.RED);
                arrayList.add(Color.CYAN);
                arrayList.add(Color.YELLOW);
                arrayList.add(Color.MAGENTA);
                arrayList.add(Color.WHITE);
                arrayList.add(Color.GRAY);
                arrayList.add(Color.ORANGE);
                arrayList.add(Color.PINK);
                arrayList.add(Color.BLUE);
                if (this.f8862q.d() > 1) {
                    for (int i4 = 0; i4 < this.f8862q.d(); i4++) {
                        this.f8868w.a((Color) arrayList.get(i4 % arrayList.size()));
                    }
                }
                this.f8868w.a(Color.LIGHT_GRAY);
                this.f8863r.setLayout(new GridLayout(1, 1));
                this.f8863r.add(this.f8868w);
                add(BorderLayout.CENTER, this.f8863r);
                this.f8863r.setVisible(this.f8862q.d() <= 1);
                this.f8863r.setVisible(true);
                return;
            } catch (V.g e2) {
                return;
            }
        }
        if (!this.f8877F) {
            this.f8866u = new aJ(this, this.f8863r);
            this.f8861p.add(this.f8866u);
            if (this.f8862q.u()) {
                this.f8866u.setVisible(true);
                this.f8863r.setVisible(true);
            } else {
                this.f8866u.setVisible(false);
                this.f8863r.setVisible(false);
            }
            add(BorderLayout.CENTER, this.f8866u);
            return;
        }
        try {
            this.f8867v = new C1290R(this.f8844a, this.f8862q, !this.f8876E, z2);
            this.f8867v.a(this.f8878G);
            this.f8866u = new aJ(this, this.f8867v);
            if (this.f8862q.d() > 1) {
                for (int i5 = 0; i5 < this.f8862q.d(); i5++) {
                    this.f8867v.a(this.f8861p.a(i5));
                }
                this.f8868w.a(Color.LIGHT_GRAY);
            }
            add(BorderLayout.CENTER, this.f8866u);
            if (this.f8862q.u()) {
                this.f8866u.setVisible(true);
                this.f8863r.setVisible(true);
            } else {
                this.f8866u.setVisible(false);
                this.f8863r.setVisible(false);
            }
        } catch (V.g e3) {
            bH.C.a("Configuration File Error.", e3, this);
        }
    }

    private void p() {
        if (Boolean.parseBoolean(this.f8871z.b(f8857n, "" + this.f8862q.A()))) {
            if (q().isShowing()) {
                return;
            }
            add("North", q());
            validate();
            doLayout();
            return;
        }
        if (this.f8883L == null || !this.f8883L.isShowing()) {
            return;
        }
        remove(this.f8883L);
        doLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public C1483a q() {
        if (this.f8883L == null) {
            this.f8883L = new C1483a();
            this.f8883L.a(new aC(this));
        }
        return this.f8883L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        try {
            C1756d.a().a(str).a(C1743c.a().e(), 0);
            return true;
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Invalid Expression! Expression should reference fields within the data log.") + "\n" + C1818g.b("Example Expression") + ":\n[MAP] > 90", this);
            return false;
        }
    }

    public void b(String str) {
        this.f8884M.a(str);
        q().c(str);
        this.f8861p.o();
        this.f8861p.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        int[] iArrJ;
        double[] dArr;
        if (this.f8861p.A() == null || this.f8861p.A().x() || this.f8861p.B() == null) {
            return;
        }
        C1012t c1012t = new C1012t();
        if (this.f8861p.z() == 0) {
            dArr = new double[this.f8861p.d(0)];
            iArrJ = new int[this.f8861p.d(0)];
            for (int i2 = 0; i2 < iArrJ.length; i2++) {
                iArrJ[i2] = i2;
            }
        } else {
            iArrJ = this.f8861p.j(0);
            dArr = new double[iArrJ.length];
        }
        for (int i3 = 0; i3 < dArr.length; i3++) {
            dArr[i3] = this.f8861p.a(0, iArrJ[i3]);
        }
        List listA = c1012t.a(this.f8861p.A(), this.f8861p.B(), new aD(this, dArr), dArr);
        int iC = this.f8861p.C();
        int iD = this.f8861p.D();
        for (int i4 = 0; i4 < listA.size(); i4++) {
            if (iArrJ[i4] >= iC && iArrJ[i4] <= iD) {
                this.f8861p.b(0, iArrJ[i4], ((InterfaceC1014v) listA.get(i4)).a());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        boolean z2 = false;
        if (this.f8862q.l() != null) {
            String strJ = G.bL.j(this.f8844a, this.f8862q.l());
            z2 = (strJ == null || strJ.isEmpty()) ? false : true;
        }
        boolean z3 = Boolean.parseBoolean(this.f8871z.b(f8857n, "" + (this.f8862q.A() || z2)));
        this.f8861p.i(z3);
        this.f8861p.a((C0184j) null);
        this.f8861p.b((C0184j) null);
        if (!z3) {
            p();
            return;
        }
        C0188n c0188nE = C1743c.a().e();
        if (c0188nE != null) {
            p();
            String strB = this.f8871z.b("xPlotColumnName", this.f8862q.B());
            String strB2 = this.f8871z.b("yPlotColumnName", this.f8862q.C());
            if (strB != null && c0188nE.a(strB) != null) {
                if (this.f8861p.A() != null) {
                    this.f8861p.A().b(this.f8885N);
                }
                C0184j c0184jA = c0188nE.a(strB);
                c0184jA.a(this.f8885N);
                this.f8861p.a(c0184jA);
                q().a(strB);
            }
            if (strB2 != null && c0188nE.a(strB2) != null) {
                if (this.f8861p.B() != null) {
                    this.f8861p.B().b(this.f8885N);
                }
                C0184j c0184jA2 = c0188nE.a(strB2);
                c0184jA2.a(this.f8885N);
                this.f8861p.b(c0184jA2);
                q().b(strB2);
            }
            this.f8884M = new aH(this, c0188nE, this.f8862q.D());
            b(this.f8871z.b("xyPlotFilterExp", ""));
            this.f8861p.a(this.f8884M);
            this.f8861p.o();
            this.f8861p.repaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void t() {
        /*
            r5 = this;
            r0 = r5
            com.efiAnalytics.ui.bN r0 = r0.f8861p
            r0.a()
            r0 = 0
            r6 = r0
        L9:
            r0 = r6
            r1 = r5
            G.bl r1 = r1.f8862q
            int r1 = r1.k()
            if (r0 >= r1) goto L72
            r0 = r5
            G.bl r0 = r0.f8862q
            r1 = r6
            java.lang.String r0 = r0.a(r1)
            r7 = r0
            r0 = 1
            r8 = r0
            r0 = r7
            if (r0 == 0) goto L34
            r0 = r7
            r1 = r5
            G.R r1 = r1.f8844a     // Catch: ax.U -> L39
            double r0 = G.C0126i.a(r0, r1)     // Catch: ax.U -> L39
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L34
            r0 = 1
            goto L35
        L34:
            r0 = 0
        L35:
            r8 = r0
            goto L3b
        L39:
            r9 = move-exception
        L3b:
            r0 = r8
            if (r0 == 0) goto L6c
            r0 = r5
            com.efiAnalytics.ui.bN r0 = r0.f8861p     // Catch: V.g -> L59
            r1 = r5
            G.bl r1 = r1.f8862q     // Catch: V.g -> L59
            r2 = r6
            G.cZ r1 = r1.e(r2)     // Catch: V.g -> L59
            java.lang.String r1 = r1.a()     // Catch: V.g -> L59
            java.lang.String r1 = s.C1818g.b(r1)     // Catch: V.g -> L59
            r0.a(r1)     // Catch: V.g -> L59
            goto L6c
        L59:
            r9 = move-exception
            java.lang.Class<bt.al> r0 = bt.C1303al.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.SEVERE
            r2 = 0
            r3 = r9
            r0.log(r1, r2, r3)
        L6c:
            int r6 = r6 + 1
            goto L9
        L72:
            r0 = r5
            com.efiAnalytics.ui.bN r0 = r0.f8861p
            r0.o()
            r0 = r5
            com.efiAnalytics.ui.bN r0 = r0.f8861p
            r0.repaint()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: bt.C1303al.t():void");
    }

    private double a(ArrayList arrayList) {
        double d2 = Double.MAX_VALUE;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            double dQ = ((G.aM) it.next()).q();
            if (dQ < d2) {
                d2 = dQ;
            }
        }
        return d2;
    }

    private double b(ArrayList arrayList) {
        double d2 = Double.MIN_VALUE;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            double dR = ((G.aM) it.next()).r();
            if (dR > d2) {
                d2 = dR;
            }
        }
        return d2;
    }

    private double c(ArrayList arrayList) {
        double d2 = Double.MIN_VALUE;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            double dR = ((G.aM) it.next()).r();
            if (dR > d2) {
                d2 = dR;
            }
        }
        return d2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u() throws NumberFormatException {
        if (this.f8861p.z() > 0) {
            String strA = com.efiAnalytics.ui.bV.a(VectorFormat.DEFAULT_PREFIX + C1818g.b("Set Selected Cells to") + ":}", true, C1818g.b("Set Cell Values"), true, (Component) this);
            requestFocus();
            if (strA == null || strA.equals("")) {
                return;
            }
            this.f8861p.j(Double.parseDouble(strA));
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (this.f8883L != null && this.f8883L.getPreferredSize().width > 100) {
            Dimension preferredSize2 = this.f8883L.getPreferredSize();
            preferredSize2.width = 20;
            this.f8883L.setPreferredSize(preferredSize2);
            preferredSize = super.getPreferredSize();
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (this.f8883L != null && this.f8883L.getMinimumSize().width > 100) {
            Dimension minimumSize2 = this.f8883L.getMinimumSize();
            minimumSize2.width = 20;
            this.f8883L.setMinimumSize(minimumSize2);
            minimumSize = super.getMinimumSize();
        }
        return minimumSize;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (this.f8868w != null && this.f8868w.isVisible()) {
            Dimension preferredSize = this.f8861p != null ? this.f8861p.getPreferredSize() : new Dimension(0, 0);
            int iA = eJ.a() + 2;
            int size = iA + ((((i5 - (preferredSize.height + this.f8868w.b(iA).height)) * 17) / 100) / (this.f8845b.size() + 1));
            int iRound = Math.round((iA * i4) / r0.width);
            int i6 = iRound > size ? size : iRound;
            int i7 = i6 > 3 * iA ? 3 * iA : i6;
            int i8 = i7 < (iA * 4) / 5 ? (iA * 4) / 5 : i7;
            new aE(this, i8);
            this.f8868w.a(i8);
        }
        super.setBounds(i2, i3, i4, i5);
    }

    public void c() {
        if (this.f8861p != null) {
            if (this.f8881J == null) {
                this.f8881J = new C1279G(this.f8844a, this.f8862q, this);
            } else {
                this.f8881J.b();
            }
            this.f8881J.a();
        }
        if (this.f8867v != null) {
            this.f8867v.a();
        }
        if (this.f8868w != null) {
            this.f8868w.a();
        }
    }

    public void e() {
        if (this.f8881J != null) {
            this.f8881J.b();
        }
        if (this.f8867v != null) {
            this.f8867v.b();
        }
        if (this.f8868w != null) {
            this.f8868w.b();
        }
    }

    public void a(bN bNVar) {
        e();
        this.f8881J = bNVar;
        if (bNVar != null) {
            bNVar.a();
        }
    }

    public void b(bN bNVar) {
        if (this.f8867v != null) {
            this.f8867v.a(bNVar);
        }
        if (this.f8868w != null) {
            this.f8868w.a(bNVar);
        }
    }

    public boolean f() {
        if (!this.f8863r.isVisible() && this.f8861p.w() < 0) {
            this.f8861p.b(this.f8880I.f8763a);
        }
        this.f8863r.setVisible(!this.f8863r.isVisible());
        this.f8871z.a("showText", this.f8863r.isVisible() + "");
        if (this.f8866u != null) {
            this.f8866u.setVisible(this.f8863r.isVisible());
        }
        return this.f8863r.isVisible();
    }

    protected void g() {
        for (int i2 = 0; i2 < this.f8845b.size(); i2++) {
            try {
                double[][] dArrI = ((G.aM) this.f8845b.get(i2)).i(this.f8844a.p());
                for (int i3 = 0; i3 < dArrI.length; i3++) {
                    this.f8861p.b(i2, i3, dArrI[i3][0]);
                    if (dArrI[i3][0] > this.f8861p.j()) {
                        this.f8861p.d(dArrI[i3][0]);
                    }
                    if (dArrI[i3][0] < this.f8861p.k()) {
                        this.f8861p.e(dArrI[i3][0]);
                    }
                }
            } catch (V.g e2) {
                bH.C.a("Error pulling curve y parameter values for CurveGraph: " + ((Object) this.f8861p), e2, this);
            }
        }
        this.f8861p.repaint();
    }

    protected void h() {
        try {
            if (this.f8846c == null || this.f8846c.size() <= 0) {
                double[][] dArr = new double[((G.aM) this.f8845b.get(0)).b()][1];
                for (int i2 = 0; i2 < dArr.length; i2++) {
                    dArr[i2][0] = ((this.f8861p.h() - this.f8861p.i()) * i2) / dArr.length;
                }
            } else {
                for (int i3 = 0; i3 < this.f8846c.size(); i3++) {
                    double[][] dArrI = ((G.aM) this.f8846c.get(i3)).i(this.f8844a.p());
                    for (int i4 = 0; i4 < dArrI.length; i4++) {
                        if (i3 != this.f8846c.size() - 1 || this.f8845b.size() <= this.f8846c.size()) {
                            this.f8861p.a(i3, i4, dArrI[i4][0]);
                        } else {
                            for (int i5 = i3; i5 < this.f8845b.size(); i5++) {
                                this.f8861p.a(i5, i4, dArrI[i4][0]);
                            }
                        }
                        if (dArrI[i4][0] > this.f8861p.h()) {
                            this.f8861p.b(dArrI[i4][0]);
                        }
                        if (dArrI[i4][0] < this.f8861p.i()) {
                            this.f8861p.c(dArrI[i4][0]);
                        }
                    }
                }
            }
        } catch (V.g e2) {
            bH.C.a("Error pulling x curve parameters", e2, this);
        }
        this.f8861p.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(InterfaceC1648ef[] interfaceC1648efArr) {
        if (this.f8863r != null) {
            if (interfaceC1648efArr == null || interfaceC1648efArr.length <= 0) {
                a(this.f8863r, -1);
            } else {
                a(this.f8863r, interfaceC1648efArr[interfaceC1648efArr.length - 1].b());
            }
        }
        if (this.f8867v != null) {
            this.f8867v.a(interfaceC1648efArr);
        }
        if (this.f8868w != null) {
            this.f8868w.a(interfaceC1648efArr);
        }
    }

    private void a(JPanel jPanel, int i2) {
        if (this.f8877F) {
            return;
        }
        for (int i3 = 0; i3 < jPanel.getComponentCount(); i3++) {
            Component component = jPanel.getComponent(i3);
            if (component instanceof C1288P) {
                C1288P c1288p = (C1288P) component;
                c1288p.b(i2);
                JComponent jComponentB = c1288p.b();
                if (jComponentB != null) {
                    Rectangle bounds = jComponentB.getBounds();
                    bounds.f12373y -= 15;
                    bounds.height += 25;
                    this.f8863r.scrollRectToVisible(bounds);
                }
            } else if (component instanceof JPanel) {
                a((JPanel) component, i2);
            }
        }
    }

    private void a(int i2) {
        if (this.f8863r != null) {
            b(this.f8863r, i2);
        }
    }

    private void b(JPanel jPanel, int i2) {
        if (this.f8877F) {
            return;
        }
        for (int i3 = 0; i3 < jPanel.getComponentCount(); i3++) {
            Component component = jPanel.getComponent(i3);
            if (component instanceof C1288P) {
                ((C1288P) component).a(i2);
            } else if (component instanceof JPanel) {
                b((JPanel) component, i2);
            }
        }
    }

    private int a(double d2) {
        double dA = this.f8861p.a(0, 0);
        int i2 = 0;
        while (i2 < this.f8861p.d(0)) {
            double dA2 = this.f8861p.a(0, i2);
            if (dA2 > d2) {
                return (i2 == 0 || Math.abs(dA2 - d2) < Math.abs(d2 - dA)) ? i2 : i2 - 1;
            }
            dA = dA2;
            i2++;
        }
        return this.f8861p.d(0) - 1;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        C0113cs.a().a(this);
        G.aR.a().a(this);
        if (this.f8873B != null) {
            G.aR.a().a(this.f8873B);
        }
        if (this.f8874C != null) {
            G.aR.a().a(this.f8874C);
        }
        if (this.f8863r != null) {
            this.f8863r.a();
        }
        if (this.f8867v != null) {
            this.f8867v.close();
        }
        if (this.f8868w != null) {
            this.f8868w.close();
        }
        e();
        this.f8879H = true;
        if (this.f8882K != null) {
            C1743c.a().b(this.f8882K);
        }
        if (this.f8861p != null && this.f8861p.A() != null) {
            this.f8861p.A().b(this.f8885N);
        }
        if (this.f8861p == null || this.f8861p.B() == null) {
            return;
        }
        this.f8861p.B().b(this.f8885N);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (isEnabled()) {
            int iA = a(d2);
            if (this.f8869x[0] != iA) {
                this.f8869x[0] = iA;
                a(iA);
            }
            if (!this.f8870y || iA == this.f8861p.w()) {
                return;
            }
            int iX = this.f8861p.x() >= 0 ? this.f8861p.x() : 0;
            a(iX, iA);
            a(new C1700r[]{new C1700r(iX, iA)});
            this.f8861p.repaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(boolean z2) {
        this.f8870y = z2;
    }

    private boolean a(ArrayList arrayList, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((G.aM) it.next()).aJ().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (a(this.f8845b, str2)) {
            g();
            return;
        }
        if (a(this.f8846c, str2)) {
            h();
            return;
        }
        this.f8861p.h(((G.aM) this.f8845b.get(0)).r());
        this.f8861p.i(((G.aM) this.f8845b.get(0)).q());
        g();
        h();
        try {
            v();
        } catch (V.g e2) {
            Logger.getLogger(C1303al.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // com.efiAnalytics.ui.bS
    public void a(int i2, int i3, double d2) {
        if (this.f8846c.isEmpty()) {
            return;
        }
        int size = i2 < this.f8846c.size() ? i2 : this.f8846c.size() - 1;
        try {
            if (!this.f8844a.p().j() && C1007o.a(this.f8859P, this.f8844a)) {
                try {
                    ((G.aM) this.f8846c.get(size)).a(this.f8844a.p(), d2, i3, 0);
                } catch (V.g e2) {
                    bH.C.a("Unable to set Curve X value of " + d2, e2, this);
                } catch (V.j e3) {
                    this.f8861p.a(size, i3, e3.c());
                }
            }
        } catch (V.g e4) {
            bH.C.b("Failed to update Curve Array Constant");
        }
    }

    @Override // com.efiAnalytics.ui.bS
    public void b(int i2, int i3, double d2) {
        if (this.f8844a.p().j()) {
            return;
        }
        try {
            ((G.aM) this.f8845b.get(i2)).a(this.f8844a.p(), d2, i3, 0);
        } catch (V.g e2) {
            bH.C.a("Unable to set Curve Y value of " + d2, e2, this);
        } catch (V.j e3) {
            if (w()) {
                this.f8861p.b(i2, i3, e3.c());
            }
        }
    }

    private void v() throws V.g {
        double[][] dArrI;
        boolean z2 = Boolean.parseBoolean(this.f8871z.b(f8852i, "" + this.f8862q.v()));
        boolean z3 = Boolean.parseBoolean(this.f8871z.b(f8849f, "" + this.f8862q.v()));
        this.f8861p.d(z3);
        this.f8861p.e(z2);
        this.f8861p.f();
        String str = null;
        int i2 = 0;
        while (i2 < this.f8845b.size()) {
            String strB = this.f8862q.f(i2) != null ? C1818g.b(this.f8862q.f(i2).a()) : null;
            if (strB == null || strB.length() == 0) {
                strB = this.f8862q.m() > 0 ? this.f8862q.f(this.f8862q.m() - 1).a() : this.f8846c.size() > i2 ? ((G.aM) this.f8846c.get(i2)).aJ() : !this.f8846c.isEmpty() ? ((G.aM) this.f8846c.get(this.f8846c.size() - 1)).aJ() : "";
            }
            if (this.f8846c.size() > 0) {
                int size = i2 < this.f8846c.size() ? i2 : this.f8846c.size() - 1;
                if (!((G.aM) this.f8846c.get(size)).o().equals("")) {
                    strB = strB + " (" + C1818g.b(((G.aM) this.f8846c.get(size)).o()) + ")";
                }
                if (str == null || !str.equals(strB)) {
                    this.f8861p.a((String) strB, i2);
                    str = strB;
                }
                if (i2 == 0) {
                    this.f8861p.g(Math.max(c(this.f8846c), this.f8861p.q()));
                }
            } else {
                this.f8861p.a(strB, i2);
            }
            i2++;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.f8845b.size(); i4++) {
            this.f8861p.b(this.f8862q.c(i4) != null ? C1818g.b(this.f8862q.c(i4).a()) + " " + C1818g.b(((G.aM) this.f8845b.get(i4)).o()) : C1818g.b(((G.aM) this.f8845b.get(i4)).o()), i4);
        }
        double dA = a(f8851h, this.f8862q.c() / 2.0d);
        double dA2 = a(f8850g, this.f8862q.b());
        if (dA2 < this.f8862q.b() || dA2 > this.f8862q.c()) {
            dA2 = this.f8862q.b();
        }
        if (dA == dA2) {
            dA = this.f8862q.c();
            dA2 = this.f8862q.b();
            this.f8871z.a(f8850g, "");
            this.f8871z.a(f8851h, "");
        }
        if (z2) {
            dA = Double.NEGATIVE_INFINITY;
            dA2 = Double.MAX_VALUE;
        }
        double dA3 = a(f8858o, this.f8862q.a());
        for (int i5 = 0; i5 < this.f8845b.size(); i5++) {
            double[][] dArrI2 = ((G.aM) this.f8845b.get(i5)).i(this.f8844a.p());
            if (this.f8846c == null || this.f8846c.size() <= 0) {
                if (this.f8862q.s() == null) {
                    dArrI = new double[((G.aM) this.f8845b.get(i5)).b()][1];
                    for (int i6 = 0; i6 < dArrI.length; i6++) {
                        dArrI[i6][0] = ((this.f8862q.i() - this.f8862q.h()) * i6) / dArrI.length;
                    }
                } else {
                    double[] dArrS = this.f8862q.s();
                    dArrI = new double[dArrS.length][1];
                    for (int i7 = 0; i7 < dArrI.length; i7++) {
                        dArrI[i7][0] = dArrS[i7];
                    }
                }
                this.f8861p.a(true);
            } else {
                dArrI = ((G.aM) this.f8846c.get(i3)).i(this.f8844a.p());
                this.f8861p.a(((G.aM) this.f8846c.get(i3)).A());
                this.f8861p.a(((G.aM) this.f8846c.get(i3)).G());
            }
            if (dArrI2.length != dArrI.length) {
                bH.C.a("SettingsCurve:" + this.f8862q.aJ() + " x/y size mismatch.");
            }
            double dA4 = a(f8848e, this.f8862q.i());
            double dA5 = a(f8847d, this.f8862q.h());
            if (dA4 == dA5) {
                dA4 = this.f8862q.i();
                dA5 = this.f8862q.h();
                this.f8871z.a(f8847d, "");
                this.f8871z.a(f8848e, "");
            }
            if (this.f8846c.size() > 0 && (dA5 < a(this.f8846c) || dA5 > b(this.f8846c))) {
                dA5 = this.f8862q.h();
                this.f8871z.a(f8847d, "" + this.f8862q.h());
            }
            if (!z3 || this.f8846c == null) {
                for (int i8 = 0; i8 < dArrI.length; i8++) {
                    try {
                        this.f8861p.a(i5, dArrI[i8][0], dArrI2[i8][0]);
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        com.efiAnalytics.ui.bV.d("Invalid number of values for curve. All X and Y arrats must be of the same size.", this);
                    }
                }
            } else {
                dA4 = Double.NEGATIVE_INFINITY;
                if (Double.NEGATIVE_INFINITY > this.f8861p.q()) {
                    dA4 = this.f8861p.q();
                } else if (Double.NEGATIVE_INFINITY < this.f8862q.h()) {
                    dA4 = this.f8862q.h();
                }
                for (int i9 = 0; i9 < dArrI.length; i9++) {
                    this.f8861p.a(i5, dArrI[i9][0], dArrI2[i9][0]);
                    if (dArrI[i9][0] > dA4 + (((G.aM) this.f8846c.get(0)).A() * 2.0d)) {
                        dA4 = dArrI[i9][0] + (((G.aM) this.f8846c.get(0)).A() * 2.0d) > this.f8861p.q() ? dArrI[i9][0] : dArrI[i9][0] + (((G.aM) this.f8846c.get(0)).A() * 2.0d);
                    }
                    this.f8861p.c(i5, i9, ((G.aM) this.f8845b.get(i5)).b(i9));
                }
            }
            if (z2) {
                if (dA > this.f8861p.r()) {
                    dA = this.f8861p.r();
                } else if (dA < this.f8862q.b()) {
                    dA = this.f8862q.b();
                }
                for (int i10 = 0; i10 < dArrI2.length; i10++) {
                    if (dArrI2[i10][0] > dA + (((G.aM) this.f8845b.get(i5)).A() * 2.0d)) {
                        dA = dArrI2[i10][0] + (((G.aM) this.f8845b.get(i5)).A() * 2.0d) > this.f8861p.r() ? dArrI2[i10][0] : dArrI2[i10][0] + (((G.aM) this.f8845b.get(i5)).A() * 2.0d);
                    }
                    if (dArrI2[i10][0] < dA2 - (((G.aM) this.f8845b.get(i5)).A() * 2.0d)) {
                        dA2 = dArrI2[i10][0] - (((G.aM) this.f8845b.get(i5)).A() * 2.0d);
                    }
                }
            } else {
                dA = a(f8851h, this.f8862q.c());
            }
            this.f8861p.e(dA2);
            this.f8861p.d(dA);
            this.f8861p.c(dA5);
            this.f8861p.b(dA4);
            this.f8861p.l((int) dA3);
            if (this.f8846c.size() > i3 + 1) {
                i3++;
            }
        }
        if (this.f8862q.z()) {
            this.f8861p.a(true);
        }
    }

    @Override // java.awt.Component
    public boolean hasFocus() {
        return a((Component) this);
    }

    private boolean a(Component component) {
        if (component instanceof Container) {
            for (Component component2 : ((Container) component).getComponents()) {
                if (a(component2)) {
                    return true;
                }
            }
        }
        if (component.equals(this)) {
            return false;
        }
        return component.hasFocus();
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f8844a;
    }

    public com.efiAnalytics.ui.bN i() {
        return this.f8861p;
    }

    public C1290R j() {
        if (this.f8867v != null) {
            return this.f8867v;
        }
        if (this.f8868w != null) {
            return this.f8868w;
        }
        return null;
    }

    public boolean k() {
        return this.f8862q.u() || !(this.f8862q.q() == null || this.f8862q.q().isEmpty());
    }

    public C0079bl l() {
        return this.f8862q;
    }

    @Override // c.InterfaceC1385d
    public String a_() {
        return this.f8859P;
    }

    @Override // c.InterfaceC1385d
    public void b_(String str) {
        this.f8859P = str;
    }

    public void m() {
        if (a_() != null) {
            try {
                setEnabled(C1007o.a(a_(), this.f8844a));
            } catch (Exception e2) {
                bH.C.a(e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    private boolean w() {
        boolean zA = true;
        if (a_() != null) {
            try {
                zA = C1007o.a(a_(), this.f8844a);
            } catch (Exception e2) {
                bH.C.a(e2.getMessage());
                e2.printStackTrace();
            }
        }
        return zA;
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f8886O.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f8886O.remove(interfaceC1281I);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Iterator it = this.f8886O.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) {
        Iterator it = this.f8886O.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return ((G.aM) this.f8845b.get(0)).aJ();
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f8861p.setEnabled(z2);
        C1685fp.a((Component) this.f8861p, z2);
        if (this.f8866u != null) {
            this.f8866u.setEnabled(z2);
        }
        if (this.f8863r != null) {
            this.f8863r.setEnabled(z2);
        }
        if (this.f8867v != null) {
            this.f8867v.setEnabled(z2);
        }
        if (this.f8868w != null) {
            this.f8868w.setEnabled(z2);
        }
    }

    public void n() {
        if (this.f8872A == null || !this.f8872A.isAlive()) {
            this.f8872A = new aN(this);
        }
        this.f8872A.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(String str) throws NumberFormatException {
        if (str.startsWith(f8847d)) {
            String strA = com.efiAnalytics.ui.bV.a((Component) this, true, f8847d, bH.W.a(this.f8861p.i()));
            if (strA != null && !strA.equals("")) {
                double d2 = Double.parseDouble(strA);
                if (d2 < a(this.f8846c) || d2 > b(this.f8846c)) {
                    com.efiAnalytics.ui.bV.d(strA + " is out of range, value must be between " + a(this.f8846c) + " and " + b(this.f8846c), this);
                    return;
                } else {
                    this.f8861p.c(d2);
                    this.f8871z.a(f8847d, d2 + "");
                }
            }
        } else if (str.startsWith(f8848e)) {
            String strA2 = com.efiAnalytics.ui.bV.a((Component) this, true, f8848e, bH.W.a(this.f8861p.h()));
            if (strA2 != null && !strA2.equals("")) {
                double d3 = Double.parseDouble(strA2);
                if (d3 < a(this.f8846c) || d3 > b(this.f8846c)) {
                    com.efiAnalytics.ui.bV.d(strA2 + " is out of range, value must be between " + a(this.f8846c) + " and " + b(this.f8846c), this);
                    return;
                } else {
                    this.f8871z.a(f8849f, "false");
                    this.f8861p.b(d3);
                    this.f8871z.a(f8848e, d3 + "");
                }
            }
        } else if (str.startsWith(f8850g)) {
            String strA3 = com.efiAnalytics.ui.bV.a((Component) this, true, f8850g, bH.W.a(this.f8861p.k()));
            if (strA3 != null && !strA3.equals("")) {
                double d4 = Double.parseDouble(strA3);
                if (d4 < a(this.f8845b) || d4 > b(this.f8845b)) {
                    com.efiAnalytics.ui.bV.d(strA3 + " is out of range, value must be between " + a(this.f8845b) + " and " + b(this.f8845b), this);
                    return;
                } else {
                    this.f8861p.e(d4);
                    this.f8871z.a(f8850g, d4 + "");
                }
            }
        } else if (str.startsWith(f8858o)) {
            String strA4 = com.efiAnalytics.ui.bV.a((Component) this, true, f8858o, bH.W.a(this.f8861p.K()));
            if (strA4 != null && !strA4.equals("")) {
                double d5 = Double.parseDouble(strA4);
                if (d5 < 1.0d) {
                    com.efiAnalytics.ui.bV.d(strA4 + " is out of range, value must be greater than 1", this);
                    return;
                } else {
                    this.f8861p.l((int) d5);
                    this.f8871z.a(f8858o, d5 + "");
                }
            }
        } else if (str.startsWith(f8851h)) {
            String strA5 = com.efiAnalytics.ui.bV.a((Component) this, true, f8851h, bH.W.a(this.f8861p.j()));
            if (strA5 != null && !strA5.equals("")) {
                double d6 = Double.parseDouble(strA5);
                if (d6 < a(this.f8845b) || d6 > b(this.f8845b)) {
                    com.efiAnalytics.ui.bV.d(strA5 + " is out of range, value must be between " + a(this.f8845b) + " and " + b(this.f8845b), this);
                    return;
                } else {
                    this.f8871z.a(f8852i, "false");
                    this.f8861p.d(d6);
                    this.f8871z.a(f8851h, d6 + "");
                }
            }
        } else if (str.equals(f8854k)) {
            String strA6 = com.efiAnalytics.ui.bV.a((Component) this, true, f8854k + " (s)", bH.W.a(this.f8861p.G() / 1000));
            if (strA6 != null && !strA6.equals("")) {
                int i2 = (int) (Double.parseDouble(strA6) * 1000.0d);
                this.f8861p.k(i2);
                this.f8871z.a(f8854k, i2 + "");
            }
        } else if (str.equals(f8856m)) {
            this.f8861p.v();
            this.f8861p.o();
            this.f8861p.repaint();
        } else if (str.equals(f8853j)) {
            c(!this.f8870y);
            this.f8861p.repaint();
        }
        this.f8861p.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        C1305an c1305an = new C1305an(this);
        String strB = this.f8871z.b(f8850g, "");
        JMenuItem jMenuItemAdd = jPopupMenu.add(strB.length() == 0 ? f8850g : f8850g + " (" + strB + ")");
        jMenuItemAdd.setActionCommand(f8850g);
        jMenuItemAdd.addActionListener(c1305an);
        if (strB.length() > 0) {
            jPopupMenu.add("Reset Y Axis Minimum to Default (" + this.f8862q.b() + ")").addActionListener(new C1306ao(this));
        }
        String strB2 = this.f8871z.b(f8851h, "");
        JMenuItem jMenuItemAdd2 = jPopupMenu.add(strB2.length() == 0 ? f8851h : f8851h + " (" + strB2 + ")");
        jMenuItemAdd2.setActionCommand(f8851h);
        jMenuItemAdd2.addActionListener(c1305an);
        if (strB2.length() > 0) {
            jPopupMenu.add("Reset Y Axis Maximum to Default (" + this.f8862q.c() + ")").addActionListener(new C1307ap(this));
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(f8852i, Boolean.parseBoolean(this.f8871z.b(f8852i, "" + this.f8860Q)));
        jCheckBoxMenuItem.addActionListener(new C1308aq(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem);
        String strB3 = this.f8871z.b(f8847d, "");
        JMenuItem jMenuItemAdd3 = jPopupMenu.add(strB3.length() == 0 ? f8847d : f8847d + " (" + strB3 + ")");
        jMenuItemAdd3.setActionCommand(f8847d);
        jMenuItemAdd3.addActionListener(c1305an);
        if (strB3.length() > 0) {
            jPopupMenu.add("Reset X Axis Minimum to Default (" + this.f8862q.h() + ")").addActionListener(new C1309ar(this));
        }
        String strB4 = this.f8871z.b(f8848e, "");
        JMenuItem jMenuItemAdd4 = jPopupMenu.add(strB4.length() == 0 ? f8848e : f8848e + " (" + strB4 + ")");
        jMenuItemAdd4.setActionCommand(f8848e);
        jMenuItemAdd4.addActionListener(c1305an);
        if (strB4.length() > 0) {
            jPopupMenu.add("Reset X Axis Maximum to Default (" + this.f8862q.i() + ")").addActionListener(new C1310as(this));
        }
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem(f8849f, Boolean.parseBoolean(this.f8871z.b(f8849f, "" + this.f8860Q)));
        jCheckBoxMenuItem2.addActionListener(new C1311at(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem2);
        if (C1806i.a().a(";lkdsaop9ewqewqgd")) {
            jPopupMenu.addSeparator();
            jPopupMenu.add(f8854k).addActionListener(c1305an);
            JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem(f8855l, Boolean.parseBoolean(this.f8871z.b(f8855l, "false")));
            jCheckBoxMenuItem3.addActionListener(new C1312au(this));
            jPopupMenu.add((JMenuItem) jCheckBoxMenuItem3);
            jPopupMenu.add(f8856m).addActionListener(c1305an);
            if (C1806i.a().a("67hgyusg432gvrewhgfds")) {
                JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem(f8857n, Boolean.parseBoolean(this.f8871z.b(f8857n, "" + this.f8862q.A())));
                jCheckBoxMenuItem4.addActionListener(new C1313av(this));
                jCheckBoxMenuItem4.setEnabled(C1743c.a().e() != null);
                jPopupMenu.add((JMenuItem) jCheckBoxMenuItem4);
            }
            JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem(f8853j, this.f8870y);
            jCheckBoxMenuItem5.addActionListener(c1305an);
            jPopupMenu.add((JMenuItem) jCheckBoxMenuItem5);
            String strB5 = this.f8871z.b(f8858o, "");
            JMenuItem jMenuItemAdd5 = jPopupMenu.add(strB5.length() == 0 ? f8858o : f8858o + " (" + strB5 + ")");
            jMenuItemAdd5.setActionCommand(f8858o);
            jMenuItemAdd5.addActionListener(c1305an);
            if (strB5.length() > 0) {
                jPopupMenu.add("Reset Coarse Step Count to Default (" + this.f8862q.a() + ")").addActionListener(new C1314aw(this));
            }
        }
        this.f8861p.add(jPopupMenu);
        jPopupMenu.show(this, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void x() {
        boolean z2 = !Boolean.parseBoolean(this.f8871z.b(f8855l, "false"));
        this.f8871z.a(f8855l, z2 + "");
        this.f8861p.f(z2);
        this.f8861p.o();
        this.f8861p.repaint();
    }

    public double a(String str, double d2) {
        String strA = this.f8871z.a(str);
        if (strA == null || strA.equals("")) {
            return d2;
        }
        try {
            return Double.parseDouble(strA);
        } catch (Exception e2) {
            return d2;
        }
    }

    public Double[][] o() {
        Double[][] dArr;
        if (this.f8877F && this.f8867v != null) {
            return this.f8867v.e().i();
        }
        if (this.f8877F && this.f8868w != null) {
            return this.f8868w.e().i();
        }
        int size = 0;
        if (this.f8862q.z()) {
            dArr = new Double[this.f8845b.size()][((G.aM) this.f8845b.get(0)).b()];
        } else {
            dArr = new Double[this.f8845b.size() + this.f8846c.size()][((G.aM) this.f8845b.get(0)).b()];
            for (int i2 = 0; i2 < this.f8846c.size(); i2++) {
                G.aM aMVar = (G.aM) this.f8846c.get(i2);
                try {
                    double[][] dArrI = aMVar.i(this.f8844a.h());
                    for (int i3 = 0; i3 < aMVar.b(); i3++) {
                        dArr[i2][i3] = Double.valueOf(dArrI[i3][0]);
                    }
                } catch (V.g e2) {
                    bH.C.a("Failed to get values for Curve xParam: " + aMVar.aJ());
                    Logger.getLogger(C1303al.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            size = this.f8846c.size();
        }
        for (int i4 = size; i4 < this.f8845b.size(); i4++) {
            G.aM aMVar2 = (G.aM) this.f8845b.get(i4 - size);
            try {
                double[][] dArrI2 = aMVar2.i(this.f8844a.h());
                for (int i5 = 0; i5 < aMVar2.b(); i5++) {
                    dArr[i4][i5] = Double.valueOf(dArrI2[i5][0]);
                }
            } catch (V.g e3) {
                bH.C.a("Failed to get values for Curve yParam: " + aMVar2.aJ());
                Logger.getLogger(C1303al.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        return dArr;
    }

    public void a(Double[][] dArr) {
        if (this.f8877F && this.f8867v != null) {
            this.f8867v.e().a(dArr);
        } else {
            if (!this.f8877F || this.f8868w == null) {
                return;
            }
            this.f8868w.e().a(dArr);
        }
    }

    public void a(boolean z2) {
        this.f8863r.setVisible(z2);
    }

    public void a(int i2, int i3) {
        this.f8861p.b(i2, i3);
    }
}
