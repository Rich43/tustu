package ao;

import W.C0188n;
import com.efiAnalytics.ui.C1596cg;
import com.efiAnalytics.ui.C1672fc;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/* renamed from: ao.eb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/eb.class */
public class C0718eb extends JPanel implements InterfaceC1742b {

    /* renamed from: f, reason: collision with root package name */
    JSplitPane f5620f;

    /* renamed from: i, reason: collision with root package name */
    JButton f5623i;

    /* renamed from: a, reason: collision with root package name */
    Map f5615a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    JPanel f5616b = new JPanel();

    /* renamed from: c, reason: collision with root package name */
    C0625ap f5617c = new C0625ap(C0804hg.a());

    /* renamed from: d, reason: collision with root package name */
    C1596cg f5618d = new C1596cg();

    /* renamed from: e, reason: collision with root package name */
    C0188n f5619e = null;

    /* renamed from: g, reason: collision with root package name */
    ArrayList f5621g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    JPanel f5622h = new JPanel();

    public C0718eb() {
        this.f5620f = null;
        this.f5623i = null;
        g();
        a(h.i.b("scatterPlotRows", 1), h.i.b("scatterPlotCols", 2));
        setLayout(new BorderLayout());
        if (0 != 0) {
            this.f5620f = new JSplitPane();
            this.f5620f.setOneTouchExpandable(true);
            this.f5620f.setOrientation(0);
            add(BorderLayout.CENTER, this.f5620f);
            this.f5620f.setTopComponent(this.f5616b);
        } else {
            add(BorderLayout.CENTER, this.f5616b);
        }
        this.f5622h.setLayout(new BorderLayout());
        this.f5622h.add(BorderLayout.CENTER, this.f5617c);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f5623i = new JButton(null, new ImageIcon(com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/table16.png")), jPanel)));
        this.f5623i.setFocusable(false);
        this.f5623i.setToolTipText("Close");
        this.f5623i.addActionListener(new C0719ec(this));
        this.f5623i.setPreferredSize(new Dimension(com.efiAnalytics.ui.eJ.a(24), com.efiAnalytics.ui.eJ.a(24)));
        jPanel.add("East", this.f5623i);
        jPanel.add(BorderLayout.CENTER, this.f5618d);
        this.f5622h.add("South", jPanel);
        if (0 != 0) {
            this.f5620f.setBottomComponent(this.f5622h);
        } else {
            add("South", this.f5622h);
        }
        com.efiAnalytics.ui.cK cKVar = new com.efiAnalytics.ui.cK();
        cKVar.a(new C0720ed(this));
        this.f5622h.add("North", cKVar);
        Dimension dimension = new Dimension(com.efiAnalytics.ui.eJ.a(150), com.efiAnalytics.ui.eJ.a(150));
        this.f5617c.setMinimumSize(dimension);
        this.f5617c.setPreferredSize(dimension);
        this.f5617c.setMaximumSize(dimension);
        this.f5617c.a(Color.BLACK);
        this.f5617c.b(Color.lightGray);
        this.f5617c.e(false);
        C0804hg.a().a((InterfaceC1741a) this.f5617c);
        bL bLVar = new bL(C0804hg.a());
        this.f5617c.addMouseListener(bLVar);
        this.f5617c.addMouseMotionListener(bLVar);
        this.f5617c.h(false);
        this.f5618d.a(new C0728el(this));
        C0804hg.a().a(new C0726ej(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        C1672fc c1672fc = new C1672fc();
        c1672fc.pack();
        c1672fc.setLocation((this.f5623i.getLocationOnScreen().f12370x + this.f5623i.getWidth()) - c1672fc.getWidth(), this.f5623i.getLocationOnScreen().f12371y - c1672fc.getHeight());
        c1672fc.a(new C0721ee(this));
        c1672fc.setVisible(true);
    }

    public void a(int i2, int i3) {
        this.f5616b.removeAll();
        this.f5617c.p();
        this.f5615a.clear();
        this.f5616b.setLayout(new GridLayout(i2, i3));
        for (int i4 = 0; i4 < i2; i4++) {
            for (int i5 = 0; i5 < i3; i5++) {
                C0764fu c0764fu = new C0764fu();
                this.f5616b.add(c0764fu);
                this.f5615a.put(b(i4, i5), c0764fu);
                c0764fu.a(new C0727ek(this, i5 * (i4 + 1)));
                c0764fu.a(new com.efiAnalytics.ui.dQ(h.i.f12258e, "curve_" + i4 + "_" + i5));
            }
        }
        if (this.f5619e != null) {
            a(1.0d);
            a(this.f5619e);
        }
    }

    public void c() {
        g();
        int i2 = 0;
        for (C0764fu c0764fu : this.f5615a.values()) {
            C0725ei c0725eiC = c(i2);
            if (this.f5619e != null) {
                c0764fu.a(this.f5619e);
            }
            if (c0725eiC != null) {
                c0764fu.a(c0725eiC.a());
                c0764fu.b(c0725eiC.b());
                if (c0725eiC.c() != null) {
                    c0764fu.c(c0725eiC.c());
                }
            }
            c0764fu.e();
            i2++;
        }
    }

    private C0725ei c(int i2) {
        if (this.f5621g.size() == 0) {
            return null;
        }
        return (C0725ei) this.f5621g.get(i2 % this.f5621g.size());
    }

    public Component d() {
        return this.f5616b;
    }

    private String b(int i2, int i3) {
        return "scatterPlot_" + i2 + "_" + i3;
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
        if (d2 == 1.0d) {
            SwingUtilities.invokeLater(new RunnableC0722ef(this));
        } else {
            SwingUtilities.invokeLater(new RunnableC0723eg(this, d2));
        }
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        this.f5619e = c0188n;
        new C0724eh(this, c0188n).start();
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    @Override // i.InterfaceC1742b
    public void b() {
        Iterator it = this.f5615a.keySet().iterator();
        while (it.hasNext()) {
            ((C0764fu) this.f5615a.get(it.next())).repaint();
        }
        this.f5617c.b();
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        e();
    }

    public double e() {
        double width = 1.0d;
        if (this.f5619e != null && this.f5619e.d() > 0) {
            width = this.f5617c.getWidth() / this.f5619e.d();
        }
        this.f5617c.b(width);
        return width;
    }

    public void a(int i2) {
        Iterator it = this.f5615a.values().iterator();
        while (it.hasNext()) {
            ((C0764fu) it.next()).c(i2);
        }
        this.f5617c.h(i2);
        this.f5617c.repaint();
    }

    public void b(int i2) {
        Iterator it = this.f5615a.values().iterator();
        while (it.hasNext()) {
            ((C0764fu) it.next()).b(i2);
        }
        this.f5617c.i(i2);
        this.f5617c.repaint();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        minimumSize.width = 300;
        minimumSize.height = 200;
        return minimumSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = 300;
        return preferredSize;
    }

    private void g() {
        this.f5621g.clear();
        C0725ei c0725ei = new C0725ei(this);
        c0725ei.a(h.g.a().a(h.g.f12245d));
        c0725ei.b(h.g.a().a(h.g.f12251j));
        c0725ei.c(h.g.a().a(h.g.f12249h));
        this.f5621g.add(c0725ei);
        C0725ei c0725ei2 = new C0725ei(this);
        c0725ei2.a(h.g.a().a(h.g.f12248g));
        c0725ei2.b(h.g.a().a(h.g.f12251j));
        c0725ei2.c(h.g.a().a(h.g.f12249h));
        this.f5621g.add(c0725ei2);
    }
}
