package aO;

import bH.C;
import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.Cdo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/* loaded from: TunerStudioMS.jar:aO/q.class */
class q extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JScrollBar f2710a = new JScrollBar(0);

    /* renamed from: b, reason: collision with root package name */
    JLabel f2711b = new JLabel("  0", 0);

    /* renamed from: c, reason: collision with root package name */
    Cdo f2712c = new Cdo("  0", 0);

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ k f2713d;

    public q(k kVar) {
        this.f2713d = kVar;
        JPanel jPanel = new JPanel();
        Color background = jPanel.getBackground();
        if (background.getBlue() < 50 && background.getGreen() < 50 && background.getRed() < 50) {
            jPanel.setBackground(Color.GRAY);
        }
        setLayout(new BorderLayout());
        add(this.f2710a, BorderLayout.CENTER);
        this.f2710a.addAdjustmentListener(new r(this, kVar));
        jPanel.setLayout(new GridLayout(1, 0));
        add(jPanel, "East");
        C1605cp c1605cp = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("zoomout.gif")), null, new Dimension(16, 16));
        c1605cp.a(new s(this, kVar));
        jPanel.add(c1605cp);
        C1605cp c1605cp2 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("zoomin.gif")));
        c1605cp2.a(new t(this, kVar));
        jPanel.add(c1605cp2);
        jPanel.add(new JLabel(""));
        jPanel.add(this.f2712c);
        jPanel.add(new JLabel("of", 0));
        jPanel.add(this.f2711b);
        jPanel.add(new JLabel(""));
        this.f2712c.addFocusListener(new u(this, kVar));
        this.f2712c.addKeyListener(new v(this, kVar));
        C1605cp c1605cp3 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("home.gif")));
        c1605cp3.a(new w(this, kVar));
        jPanel.add(c1605cp3);
        C1605cp c1605cp4 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("rewind.gif")));
        c1605cp4.a(new x(this, kVar));
        jPanel.add(c1605cp4);
        C1605cp c1605cp5 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("fastforward.gif")));
        c1605cp5.a(new y(this, kVar));
        jPanel.add(c1605cp5);
        C1605cp c1605cp6 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("end.gif")));
        c1605cp6.a(new z(this, kVar));
        jPanel.add(c1605cp6);
    }

    public void a() {
        if (!this.f2713d.h()) {
            this.f2712c.setText("0");
            return;
        }
        try {
            this.f2713d.f(this.f2713d.f2669e.a(((int) Double.parseDouble(this.f2712c.getText())) - 1));
        } catch (V.a e2) {
            this.f2712c.setText((this.f2713d.f2669e.c() + 1) + "");
        }
    }

    public void b() {
        this.f2710a.setMaximum(this.f2713d.f2682r - this.f2713d.a());
        this.f2710a.setMinimum(0);
    }

    public void a(double d2) {
        if (d2 >= 0.0d && d2 <= 1.0d) {
            this.f2710a.setValue((int) (this.f2710a.getMinimum() + (((this.f2710a.getMaximum() + this.f2713d.a()) - this.f2710a.getMinimum()) * d2)));
        } else if (d2 < 0.0d) {
            this.f2710a.setValue(this.f2710a.getMinimum());
        } else {
            C.c("Invalid Percent:" + d2);
        }
    }

    public double c() {
        return (this.f2710a.getValue() - this.f2710a.getMinimum()) / ((this.f2710a.getMaximum() + this.f2713d.a()) - this.f2710a.getMinimum());
    }

    public int d() {
        return this.f2710a.getValue();
    }

    public void a(int i2) {
        this.f2711b.setText("" + i2);
    }

    public void b(int i2) {
        this.f2712c.setText("" + i2);
    }
}
