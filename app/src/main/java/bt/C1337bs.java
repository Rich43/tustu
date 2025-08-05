package bt;

import G.C0072be;
import G.C0126i;
import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1705w;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1670fa;
import com.efiAnalytics.ui.InterfaceC1698p;
import com.efiAnalytics.ui.fE;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.bs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bs.class */
public class C1337bs extends JPanel implements G.aN, InterfaceC1282J, InterfaceC1349h, InterfaceC1385d, InterfaceC1565bc, InterfaceC1698p, TableModelListener {

    /* renamed from: a, reason: collision with root package name */
    G.R f9056a;

    /* renamed from: b, reason: collision with root package name */
    G.aM f9057b;

    /* renamed from: c, reason: collision with root package name */
    G.aM f9058c;

    /* renamed from: d, reason: collision with root package name */
    G.aM f9059d;

    /* renamed from: e, reason: collision with root package name */
    fE f9060e;

    /* renamed from: f, reason: collision with root package name */
    C1705w f9061f;

    /* renamed from: g, reason: collision with root package name */
    JLabel f9062g;

    /* renamed from: h, reason: collision with root package name */
    C0072be f9063h;

    /* renamed from: l, reason: collision with root package name */
    private String f9064l;

    /* renamed from: i, reason: collision with root package name */
    bN f9065i;

    /* renamed from: j, reason: collision with root package name */
    List f9066j;

    /* renamed from: m, reason: collision with root package name */
    private static double f9067m = 100.0d;

    /* renamed from: k, reason: collision with root package name */
    InterfaceC1670fa f9068k;

    public C1337bs(G.R r2, C0072be c0072be) {
        this(r2, c0072be, true);
    }

    public C1337bs(G.R r2, C0072be c0072be, boolean z2) throws IllegalArgumentException {
        String strB;
        String strB2;
        this.f9056a = null;
        this.f9057b = null;
        this.f9058c = null;
        this.f9059d = null;
        this.f9060e = null;
        this.f9061f = null;
        this.f9062g = null;
        this.f9063h = null;
        this.f9064l = null;
        this.f9065i = null;
        this.f9066j = new ArrayList();
        this.f9068k = new C1338bt(this);
        this.f9056a = r2;
        this.f9063h = c0072be;
        this.f9064l = c0072be.aH();
        setLayout(new BorderLayout());
        this.f9061f = new C1705w();
        this.f9061f.setName(c0072be.aJ());
        this.f9061f.h().e(C1365x.a());
        setName(c0072be.aJ());
        this.f9061f.a(new C1339bu(this));
        this.f9057b = r2.c(c0072be.c());
        this.f9058c = r2.c(c0072be.b());
        this.f9059d = r2.c(c0072be.a());
        try {
            C0126i.a(r2.c(), this.f9058c, this);
            C0126i.a(r2.c(), this.f9057b, this);
            C0126i.a(r2.c(), this.f9058c.p(), this);
            C0126i.a(r2.c(), this.f9059d.E(), this);
            if (c0072be.p() != null) {
                C0126i.a(r2.c(), c0072be.p(), this);
            }
            if (c0072be.q() != null) {
                C0126i.a(r2.c(), c0072be.q(), this);
            }
        } catch (V.a e2) {
            Logger.getLogger(C1337bs.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        C1701s c1701sA = null;
        try {
            c1701sA = z2 ? bO.a().a(r2, c0072be.aJ(), c0072be.l(), c0072be.aJ()) : bO.a().b(r2, c0072be.aJ());
            c1701sA.addTableModelListener(this);
            c1701sA.a(this.f9068k);
            this.f9061f.h().a(this.f9057b.u());
            this.f9061f.b(this.f9058c.u());
            this.f9061f.a(this.f9059d.u());
            this.f9061f.h().c(a(this.f9058c) <= 3 && this.f9059d.u() < 1 && a(this.f9057b) <= 3 && bH.I.a());
            this.f9061f.a(c1701sA);
            this.f9061f.h().f(this.f9057b.A());
            if (c0072be.i() > 0) {
                this.f9061f.c(c0072be.i());
                this.f9061f.a(false);
            } else {
                this.f9061f.c(C1798a.a().a(C1798a.f13352aH, this.f9061f.getFont().getSize() + C1798a.a().p()));
            }
            this.f9061f.a(this);
        } catch (V.g e3) {
            bH.C.a("Unable to load table. See log for details.", e3, this);
        }
        String[] strArrP = this.f9057b.P();
        if (strArrP.length == 1 || strArrP.length == 2) {
            this.f9061f.a(new bW(r2, c0072be, c1701sA));
        }
        this.f9061f.h().addFocusListener(new C1340bv(this));
        add(BorderLayout.CENTER, this.f9061f);
        this.f9062g = new JLabel();
        this.f9062g.setHorizontalAlignment(0);
        add("South", this.f9062g);
        this.f9060e = new fE();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        if (c0072be.c() != null) {
            jPanel2.setLayout(new GridLayout(2, 1));
            jPanel2.add(new C1291a(r2, c0072be.c()));
            jPanel2.add(new C1353l(r2, c0072be.c()));
            jPanel.add("North", jPanel2);
        }
        jPanel.add(BorderLayout.CENTER, this.f9060e);
        add("West", jPanel);
        if (c1701sA.w() != null) {
            if (c0072be.p() == null) {
                strB2 = C1818g.b(c1701sA.w());
            } else {
                try {
                    strB2 = C1818g.b(c0072be.p().a());
                } catch (V.g e4) {
                    Logger.getLogger(C1337bs.class.getName()).log(Level.WARNING, "Failed to resolve X Axis Label", (Throwable) e4);
                    strB2 = C1818g.b(c1701sA.w());
                }
            }
            this.f9062g.setText(strB2);
        }
        if (c1701sA.v() != null) {
            if (c0072be.q() == null) {
                strB = C1818g.b(c1701sA.v());
            } else {
                try {
                    strB = C1818g.b(c0072be.q().a());
                } catch (V.g e5) {
                    Logger.getLogger(C1337bs.class.getName()).log(Level.WARNING, "Unable to resolve Y Axis Label", (Throwable) e5);
                    strB = C1818g.b(c1701sA.v());
                }
            }
            if (this.f9058c.o() != null && !this.f9058c.o().equals("")) {
                strB = strB + " " + C1818g.b((C1806i.a().a("645fds645fds  fdsd098532#@") && C1798a.a().c(C1798a.cc, C1798a.cd) && this.f9058c.o().toLowerCase().equals("kpa")) ? "PSI" : this.f9058c.o());
            }
            this.f9060e.setText(C1818g.b(strB));
        }
        if (C1806i.a().a("reoirew rew oiurew")) {
            this.f9061f.c(true);
        } else {
            this.f9061f.c(true);
        }
        if (C1806i.a().a("jheoibnw747d mds 982")) {
            this.f9061f.c();
        }
        this.f9061f.a(new C1343by(this));
        this.f9061f.a(new C1342bx(this));
        if (c0072be.m() || c0072be.o()) {
            this.f9061f.b(false);
        }
        c();
        a(r2, this.f9058c);
    }

    private void a(G.R r2, G.aM aMVar) {
        if (aMVar.o() == null || !aMVar.o().equalsIgnoreCase("kpa")) {
            this.f9061f.o();
            this.f9061f.d(false);
            return;
        }
        try {
            double[] dArrH = aMVar.h(r2.h());
            for (int i2 = 0; i2 < dArrH.length; i2++) {
                double d2 = dArrH[(dArrH.length - 1) - i2];
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append(bH.W.b(d2, aMVar.u())).append(" kPa:<br>");
                sb.append(bH.W.b(d2 * 0.145038d, 2)).append(" ").append(C1818g.b("PSI Absolute")).append("<br>");
                sb.append(bH.W.b((d2 - f9067m) * 0.145038d, 2)).append(" ").append(C1818g.b("PSI Relative to " + f9067m + " kPa")).append("<br>");
                sb.append(bH.W.b((d2 - f9067m) * 0.29538d, 2)).append(" ").append(C1818g.b("inHg Relative to " + f9067m + " kPa")).append("<br>");
                this.f9061f.a(sb.toString(), i2);
            }
        } catch (V.g e2) {
            Logger.getLogger(C1337bs.class.getName()).log(Level.WARNING, "Failed to set Y Axis tooltips", (Throwable) e2);
        }
        SwingUtilities.invokeLater(new RunnableC1341bw(this));
    }

    private int a(G.aM aMVar) {
        return ((int) Math.log10(aMVar.r())) + aMVar.u();
    }

    public void c() {
        if (this.f9065i == null) {
            this.f9065i = new C1280H(this.f9056a, this.f9063h, this.f9061f);
        } else {
            this.f9065i.b();
        }
        this.f9065i.a();
    }

    public void e() {
        if (this.f9065i != null) {
            this.f9065i.b();
        }
    }

    public void a(bN bNVar) {
        e();
        this.f9065i = bNVar;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public boolean requestFocusInWindow() {
        return this.f9061f.requestFocusInWindow();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1698p
    public void a(int i2, String str, String str2) {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1698p
    public void b(int i2, String str, String str2) {
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        if (tableModelEvent.getFirstRow() == -1 || tableModelEvent.getColumn() == -1) {
            return;
        }
        try {
            this.f9061f.repaint();
        } catch (Exception e2) {
            e2.printStackTrace();
            com.efiAnalytics.ui.bV.d("Error updating table:\n" + e2.getMessage() + "\nSee log for more detail.", this);
        }
    }

    public void a(double d2, double d3) throws NumberFormatException {
        this.f9061f.h().a(Double.toString(d2), Double.toString(d3));
    }

    public void a(float[] fArr, float[] fArr2) {
        this.f9061f.h().a(fArr, fArr2);
    }

    public int f() {
        return this.f9061f.h().J();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        e();
        this.f9061f.g().removeTableModelListener(this);
        G.aR.a().a(this);
        this.f9061f.g().b(this.f9068k);
    }

    public void a(Double[][] dArr) {
        this.f9061f.g().a(dArr);
    }

    public Double[][] g() {
        return this.f9061f.g().s();
    }

    @Override // G.aN
    public void a(String str, String str2) throws IllegalArgumentException {
        String strB;
        String strB2;
        this.f9061f.b(this.f9058c.u());
        this.f9061f.h().a(this.f9057b.u());
        this.f9061f.h().b(this.f9059d.u());
        if (this.f9061f.g().v() != null) {
            if (this.f9063h.q() == null) {
                strB2 = C1818g.b(this.f9061f.g().v());
            } else {
                try {
                    strB2 = C1818g.b(this.f9063h.q().a());
                } catch (V.g e2) {
                    Logger.getLogger(C1337bs.class.getName()).log(Level.WARNING, "Unable to resolve Y Axis Label", (Throwable) e2);
                    strB2 = C1818g.b(this.f9061f.g().v());
                }
            }
            if (this.f9058c.o() != null && !this.f9058c.o().equals("")) {
                strB2 = strB2 + " " + C1818g.b(this.f9058c.o());
            }
            this.f9060e.setText(C1818g.b(strB2));
            this.f9060e.repaint();
        }
        if (this.f9061f.g().w() != null) {
            if (this.f9063h.p() == null) {
                strB = C1818g.b(this.f9061f.g().w());
            } else {
                try {
                    strB = C1818g.b(this.f9063h.p().a());
                } catch (V.g e3) {
                    Logger.getLogger(C1337bs.class.getName()).log(Level.WARNING, "Failed to resolve X Axis Label", (Throwable) e3);
                    strB = C1818g.b(this.f9061f.g().w());
                }
            }
            this.f9062g.setText(strB);
        }
        if (this.f9058c.aJ().equals(str2)) {
            a(this.f9056a, this.f9058c);
        }
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (a_() != null) {
            try {
                setEnabled(C1007o.a(a_(), this.f9056a));
            } catch (Exception e2) {
                bH.C.a(e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    @Override // c.InterfaceC1385d
    public String a_() {
        return this.f9064l;
    }

    @Override // c.InterfaceC1385d
    public void b_(String str) {
        this.f9064l = str;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        this.f9062g.setEnabled(z2);
        this.f9060e.setEnabled(z2);
        this.f9061f.setEnabled(z2);
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f9056a;
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f9066j.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f9066j.remove(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return this.f9057b.aJ();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        Iterator it = this.f9066j.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f9066j.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }

    public static void a(double d2) {
        f9067m = d2;
    }
}
