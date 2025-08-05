package ao;

import W.C0188n;
import i.InterfaceC1742b;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.UIManager;

/* renamed from: ao.hi, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hi.class */
public class C0806hi extends JComponent implements InterfaceC1742b {

    /* renamed from: a, reason: collision with root package name */
    long f6077a = 0;

    /* renamed from: b, reason: collision with root package name */
    long f6078b = 800;

    /* renamed from: c, reason: collision with root package name */
    double f6079c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    int f6080d = com.efiAnalytics.ui.eJ.a(4);

    /* renamed from: e, reason: collision with root package name */
    int f6081e = com.efiAnalytics.ui.eJ.a(4);

    /* renamed from: f, reason: collision with root package name */
    ArrayList f6082f = null;

    /* renamed from: g, reason: collision with root package name */
    ArrayList f6083g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    boolean f6084h = true;

    /* renamed from: i, reason: collision with root package name */
    Color f6085i = Color.red;

    /* renamed from: j, reason: collision with root package name */
    C0188n f6086j = null;

    /* renamed from: k, reason: collision with root package name */
    Color f6087k = UIManager.getColor("ProgressBar.foreground");

    public C0806hi() {
        C0807hj c0807hj = new C0807hj(this);
        addMouseListener(c0807hj);
        addMouseMotionListener(c0807hj);
    }

    public void a(boolean z2) {
        this.f6084h = z2;
    }

    private int c() {
        return (getWidth() - this.f6081e) - this.f6080d;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(this.f6087k);
        graphics.fillRect(this.f6081e, com.efiAnalytics.ui.eJ.a(6), ((int) (c() * this.f6079c)) + 1, getHeight() - com.efiAnalytics.ui.eJ.a(12));
        graphics.setColor(Color.gray);
        graphics.draw3DRect(com.efiAnalytics.ui.eJ.a(1), com.efiAnalytics.ui.eJ.a(2), getWidth() - com.efiAnalytics.ui.eJ.a(3), getHeight() - com.efiAnalytics.ui.eJ.a(5), false);
        if (this.f6082f != null) {
            Iterator it = this.f6082f.iterator();
            while (it.hasNext()) {
                double dDoubleValue = ((Double) it.next()).doubleValue();
                graphics.setColor(this.f6085i);
                graphics.drawLine(((int) (c() * dDoubleValue)) + this.f6081e, com.efiAnalytics.ui.eJ.a(6), ((int) (c() * dDoubleValue)) + this.f6081e, getHeight() - com.efiAnalytics.ui.eJ.a(7));
            }
        }
    }

    public void b(double d2) {
        this.f6079c = d2;
        repaint();
    }

    @Override // i.InterfaceC1742b
    public void a() {
        this.f6079c = 0.0d;
        this.f6082f = null;
        d();
    }

    public void c(double d2) {
        if (this.f6082f == null) {
            this.f6082f = new ArrayList();
        }
        this.f6082f.add(Double.valueOf(d2));
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
        HashMap mapA;
        if (System.currentTimeMillis() - this.f6077a > this.f6078b) {
            this.f6079c = d2;
            if (this.f6086j != null && d2 == 1.0d && (mapA = this.f6086j.a()) != null) {
                Iterator it = mapA.keySet().iterator();
                while (it.hasNext()) {
                    try {
                        c(((Integer) it.next()).intValue() / this.f6086j.d());
                    } catch (Exception e2) {
                        System.out.println("Error adding Mark to status bar");
                        e2.printStackTrace();
                    }
                }
            }
            d();
        }
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        this.f6086j = c0188n;
        d();
    }

    private void d() {
        repaint(30L);
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        double d2 = i2 - this.f6081e;
        int iC = c();
        if (d2 / iC >= 0.0d && d2 / iC <= 1.0d) {
            d(d2 / iC);
        }
        C0804hg.a().f();
    }

    private void d(double d2) {
        Iterator it = this.f6083g.iterator();
        while (it.hasNext()) {
            ((InterfaceC0808hk) it.next()).b(d2);
        }
    }

    public void a(InterfaceC0808hk interfaceC0808hk) {
        this.f6083g.add(interfaceC0808hk);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(com.efiAnalytics.ui.eJ.a(50), com.efiAnalytics.ui.eJ.a(15));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(com.efiAnalytics.ui.eJ.a(150), com.efiAnalytics.ui.eJ.a(20));
    }

    @Override // i.InterfaceC1742b
    public void b() {
    }
}
