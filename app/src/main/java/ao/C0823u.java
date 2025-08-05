package ao;

import W.C0184j;
import W.C0188n;
import aq.C0833a;
import g.C1733k;
import h.C1737b;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/* renamed from: ao.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/u.class */
public class C0823u extends JPanel implements InterfaceC0758fo, gO, gR, InterfaceC0813k, InterfaceC0814l, InterfaceC1741a, InterfaceC1742b {

    /* renamed from: h, reason: collision with root package name */
    C0804hg f6176h;

    /* renamed from: a, reason: collision with root package name */
    int f6169a = 0;

    /* renamed from: b, reason: collision with root package name */
    C0188n f6170b = null;

    /* renamed from: c, reason: collision with root package name */
    JLabel f6171c = new JLabel();

    /* renamed from: d, reason: collision with root package name */
    C0827y f6172d = new C0827y(this);

    /* renamed from: e, reason: collision with root package name */
    JPanel f6173e = new JPanel();

    /* renamed from: f, reason: collision with root package name */
    JPanel f6174f = new JPanel();

    /* renamed from: g, reason: collision with root package name */
    C0593K f6175g = new C0593K();

    /* renamed from: i, reason: collision with root package name */
    boolean f6177i = true;

    /* renamed from: j, reason: collision with root package name */
    boolean f6178j = true;

    /* renamed from: k, reason: collision with root package name */
    double f6179k = 1.0d;

    /* renamed from: l, reason: collision with root package name */
    int f6180l = 160;

    /* renamed from: m, reason: collision with root package name */
    int f6181m = 25;

    /* renamed from: n, reason: collision with root package name */
    Color f6182n = Color.BLACK;

    /* renamed from: p, reason: collision with root package name */
    private C0583A f6183p = null;

    /* renamed from: o, reason: collision with root package name */
    bz.a f6184o = new bz.a(0, 5, 2, 2);

    public C0823u(C0804hg c0804hg) {
        this.f6176h = null;
        this.f6176h = c0804hg;
        setFont(new Font("Arial Unicode MS", 0, (h.i.a("prefFontSize", com.efiAnalytics.ui.eJ.a(12)) * 11) / 12));
        setLayout(new BorderLayout(2, 2));
        this.f6173e.setLayout(new BorderLayout(2, 2));
        this.f6173e.add(this.f6171c, "West");
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(2);
        this.f6174f.setLayout(flowLayout);
        this.f6174f.add(this.f6175g);
        this.f6173e.add(this.f6174f, BorderLayout.CENTER);
        add(this.f6173e, "North");
        c0804hg.a(this.f6175g);
        this.f6175g.setVisible(false);
        this.f6172d.setLayout(this.f6184o);
        this.f6184o.a(h.i.a(h.i.f12284E, h.i.f12285F));
        add(this.f6172d, BorderLayout.CENTER);
        c0804hg.a((InterfaceC0758fo) this);
    }

    public void a(JPanel jPanel) {
        add(jPanel, BorderLayout.CENTER);
    }

    public void c(C0188n c0188n) {
        if (this.f6170b == null || c0188n == null || !this.f6170b.equals(c0188n)) {
            this.f6170b = c0188n;
            if (this.f6183p != null) {
                for (int i2 = 0; i2 < this.f6172d.getComponentCount(); i2++) {
                    Component component = this.f6172d.getComponent(i2);
                    if (component instanceof C0620ak) {
                        C0620ak c0620ak = (C0620ak) component;
                        this.f6183p.b(c0620ak);
                        c0620ak.a((C0583A) null);
                    }
                }
            }
            this.f6172d.removeAll();
            ArrayList arrayList = new ArrayList();
            if (C1737b.a().a("selectableFields")) {
                List listA = C0833a.a(h.i.e(C0833a.f6200b, ""));
                for (int i3 = 0; i3 < this.f6170b.size(); i3++) {
                    if (!listA.contains(((C0184j) this.f6170b.get(i3)).a()) || ((C0184j) this.f6170b.get(i3)).a().equals("Engine")) {
                        arrayList.add(this.f6170b.get(i3));
                    }
                }
            } else {
                arrayList.addAll(this.f6170b);
            }
            if (h.i.a(h.i.f12284E, h.i.f12285F)) {
                Collections.sort(arrayList, new C0824v(this));
            }
            for (C0184j c0184j : (C0184j[]) arrayList.toArray(new C0184j[arrayList.size()])) {
                C0620ak c0620ak2 = new C0620ak(c0184j);
                if (a(c0184j)) {
                    c0620ak2.b(this.f6178j);
                    c0620ak2.c(this.f6177i);
                } else {
                    c0620ak2.c(false);
                    c0620ak2.e(true);
                    c0620ak2.b(true);
                }
                c0620ak2.b(0);
                if (c0184j.u() == null || c0184j.a().equals(c0184j.u())) {
                    c0620ak2.setToolTipText(null);
                } else {
                    c0620ak2.setToolTipText("Standardized Name: " + c0184j.a() + ", Name in File: " + c0184j.u());
                }
                if (this.f6183p != null) {
                    c0620ak2.a(this.f6183p);
                    this.f6183p.a(c0620ak2);
                }
                this.f6172d.add(c0620ak2);
                if (c0184j.a().equalsIgnoreCase("Engine")) {
                    this.f6175g.a(c0184j);
                }
            }
            this.f6180l = e();
            this.f6172d.a(this.f6172d.getWidth());
            this.f6175g.setVisible(this.f6170b.a("Engine") != null);
            validate();
            this.f6172d.validate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(C0184j c0184j) {
        return c0184j.p() == 0 || c0184j.p() == 2 || c0184j.p() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int e() {
        C0620ak c0620ak;
        int iD;
        int i2 = 30;
        Component[] components = this.f6172d.getComponents();
        for (int i3 = 0; i3 < components.length; i3++) {
            if ((components[i3] instanceof C0620ak) && i2 < (iD = (c0620ak = (C0620ak) components[i3]).d(c0620ak.getHeight()))) {
                i2 = iD;
            }
        }
        return i2 + (i2 > com.efiAnalytics.ui.eJ.a(120) ? 0 : com.efiAnalytics.ui.eJ.a(20));
    }

    private void c(int i2) {
        Component[] components = this.f6172d.getComponents();
        for (int i3 = 0; i3 < components.length; i3++) {
            if (components[i3] instanceof C0620ak) {
                ((C0620ak) components[i3]).b(i2);
            }
        }
        int iE = e();
        if (iE > this.f6180l + com.efiAnalytics.ui.eJ.a(4)) {
            this.f6180l = iE;
            this.f6172d.doLayout();
        }
    }

    @Override // ao.gO
    public void b(boolean z2) {
        c(z2);
    }

    public void c(boolean z2) {
        this.f6172d.setVisible(z2);
        C1733k.b(this);
        Frame frameA = C1733k.a(this.f6172d.getParent());
        if (frameA != null) {
            frameA.validate();
        }
    }

    public void c() throws IllegalArgumentException {
        if (this.f6170b != null) {
            String strB = this.f6170b.b(this.f6169a);
            String strD = this.f6170b.d(this.f6169a);
            String strD2 = C0804hg.a().s() == null ? null : C0804hg.a().s().d(this.f6169a - C0645bi.a().g().a());
            String strC = this.f6170b.c(this.f6169a);
            String str = this.f6170b.d() > 0 ? "" + (this.f6169a + 1) : "0";
            if (strB != null) {
                this.f6171c.setText(strB);
                return;
            }
            if (strD == null && strD2 == null) {
                if (strC != null) {
                    this.f6171c.setText("Record " + str + " of " + this.f6170b.d() + " : " + strC + "                    ");
                    return;
                } else {
                    this.f6171c.setText("Record " + str + " of " + this.f6170b.d() + " - Zoom: " + bH.W.b(this.f6176h.t(), this.f6176h.t() < 0.02d ? 3 : 2) + "x - Play speed: " + (this.f6179k * 100.0d) + "%                                   ");
                    return;
                }
            }
            if (strD2 == null) {
                this.f6171c.setText("Record " + str + " of " + this.f6170b.d() + " : " + strD + "                    ");
            } else if (strD == null) {
                this.f6171c.setText("Record " + str + " of " + this.f6170b.d() + ", Compare: " + strD2 + "                    ");
            } else {
                this.f6171c.setText("Record " + str + " of " + this.f6170b.d() + " : " + strD + ", Compare:  " + strD2);
            }
        }
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) throws IllegalArgumentException {
        this.f6169a = i2;
        c();
        c(i2);
        repaint();
    }

    @Override // ao.InterfaceC0758fo
    public void b(double d2) throws IllegalArgumentException {
        c();
    }

    @Override // ao.gR
    public void c(double d2) throws IllegalArgumentException {
        this.f6179k = d2;
        c();
    }

    public void d(boolean z2) {
        Component[] components = this.f6172d.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof C0620ak) {
                ((C0620ak) components[i2]).b(z2);
            }
        }
        this.f6178j = z2;
    }

    public void e(boolean z2) {
        Component[] components = this.f6172d.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof C0620ak) {
                ((C0620ak) components[i2]).c(z2);
            }
        }
        this.f6177i = z2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension preferredSize = this.f6173e.getPreferredSize();
        preferredSize.width = 200;
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = 200;
        return preferredSize;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        RunnableC0825w runnableC0825w = new RunnableC0825w(this);
        super.setBounds(i2, i3, i4, i5);
        SwingUtilities.invokeLater(runnableC0825w);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    public JPanel d() {
        return this.f6172d;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        super.setForeground(color);
        this.f6182n = color;
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
    }

    public void a(C0583A c0583a) {
        this.f6183p = c0583a;
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        SwingUtilities.invokeLater(new RunnableC0826x(this, c0188n));
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    @Override // i.InterfaceC1742b
    public void b() {
    }

    @Override // ao.InterfaceC0814l
    public void b(int i2) {
        for (Component component : this.f6172d.getComponents()) {
            if (component instanceof C0620ak) {
                ((C0620ak) component).c(i2);
            }
        }
    }

    @Override // ao.InterfaceC0814l
    public void a(boolean z2) {
        for (Component component : this.f6172d.getComponents()) {
            if (component instanceof C0620ak) {
                ((C0620ak) component).a(z2);
            }
        }
    }
}
