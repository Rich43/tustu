package ao;

import W.C0188n;
import com.efiAnalytics.ui.C1596cg;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/* renamed from: ao.em, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/em.class */
public class C0729em extends JPanel implements InterfaceC1742b {

    /* renamed from: e, reason: collision with root package name */
    JSplitPane f5644e;

    /* renamed from: g, reason: collision with root package name */
    fX f5646g;

    /* renamed from: a, reason: collision with root package name */
    JPanel f5640a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    C0625ap f5641b = new C0625ap(C0804hg.a());

    /* renamed from: c, reason: collision with root package name */
    C1596cg f5642c = new C1596cg();

    /* renamed from: d, reason: collision with root package name */
    C0188n f5643d = null;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f5645f = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    RunnableC0735es f5647h = new RunnableC0735es(this);

    public C0729em() {
        this.f5644e = null;
        f();
        this.f5646g = new fX();
        this.f5646g.a(new com.efiAnalytics.ui.dQ(h.i.f12258e, "TableGenSetting"));
        C0804hg.a().a(this.f5646g);
        C0804hg.a().a(this);
        this.f5641b.i(true);
        this.f5640a.setLayout(new BorderLayout());
        this.f5640a.add(BorderLayout.CENTER, this.f5646g);
        setLayout(new BorderLayout());
        this.f5644e = new JSplitPane();
        this.f5644e.setOneTouchExpandable(true);
        this.f5644e.setOrientation(0);
        add(BorderLayout.CENTER, this.f5640a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f5641b);
        jPanel.add("South", this.f5642c);
        add("South", jPanel);
        com.efiAnalytics.ui.cK cKVar = new com.efiAnalytics.ui.cK();
        cKVar.a(new C0730en(this));
        jPanel.add("North", cKVar);
        Dimension dimension = new Dimension(com.efiAnalytics.ui.eJ.a(150), com.efiAnalytics.ui.eJ.a(150));
        this.f5641b.setMinimumSize(dimension);
        this.f5641b.setPreferredSize(dimension);
        this.f5641b.setMaximumSize(dimension);
        this.f5641b.a(Color.BLACK);
        this.f5641b.b(Color.lightGray);
        this.f5641b.e(false);
        C0804hg.a().a((InterfaceC1741a) this.f5641b);
        bL bLVar = new bL(C0804hg.a());
        this.f5641b.addMouseListener(bLVar);
        this.f5641b.addMouseMotionListener(bLVar);
        this.f5641b.h(false);
        this.f5642c.a(new C0734er(this));
        this.f5646g.a(new C0733eq(this));
    }

    public void c() {
        f();
        for (int i2 = 0; i2 < 0; i2++) {
            if (c(i2) != null) {
            }
        }
    }

    private C0732ep c(int i2) {
        if (this.f5645f.size() == 0) {
            return null;
        }
        return (C0732ep) this.f5645f.get(i2 % this.f5645f.size());
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
        this.f5647h.a(d2);
        if (this.f5647h.b()) {
            this.f5647h.a(false);
            SwingUtilities.invokeLater(this.f5647h);
        }
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        this.f5643d = c0188n;
        SwingUtilities.invokeLater(new RunnableC0731eo(this, c0188n));
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    @Override // i.InterfaceC1742b
    public void b() {
        this.f5641b.b();
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        d();
    }

    public double d() {
        double width = 1.0d;
        if (this.f5643d != null && this.f5643d.d() > 0) {
            width = this.f5641b.getWidth() / this.f5643d.d();
        }
        this.f5641b.b(width);
        return width;
    }

    public void a(int i2) {
        this.f5646g.e(i2);
        this.f5641b.h(i2);
        this.f5641b.repaint();
    }

    public void b(int i2) {
        this.f5646g.d(i2);
        this.f5641b.i(i2);
        this.f5641b.repaint();
    }

    private void f() {
        this.f5645f.clear();
        C0732ep c0732ep = new C0732ep(this);
        c0732ep.a(h.g.a().a(h.g.f12245d));
        c0732ep.b(h.g.a().a(h.g.f12251j));
        c0732ep.c(h.g.a().a(h.g.f12249h));
        this.f5645f.add(c0732ep);
        C0732ep c0732ep2 = new C0732ep(this);
        c0732ep2.a(h.g.a().a(h.g.f12248g));
        c0732ep2.b(h.g.a().a(h.g.f12251j));
        c0732ep2.c(h.g.a().a(h.g.f12249h));
        this.f5645f.add(c0732ep2);
    }

    public Component e() {
        return this.f5646g;
    }
}
