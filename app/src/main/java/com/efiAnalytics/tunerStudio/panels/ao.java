package com.efiAnalytics.tunerStudio.panels;

import W.C0188n;
import com.efiAnalytics.ui.C1605cp;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ao.class */
class ao extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JScrollBar f10078a = new JScrollBar(0);

    /* renamed from: b, reason: collision with root package name */
    JLabel f10079b = new JLabel("  0", 0);

    /* renamed from: c, reason: collision with root package name */
    JLabel f10080c = new JLabel("  0", 0);

    /* renamed from: d, reason: collision with root package name */
    C1605cp f10081d;

    /* renamed from: e, reason: collision with root package name */
    Image f10082e;

    /* renamed from: f, reason: collision with root package name */
    Image f10083f;

    /* renamed from: g, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10084g;

    public ao(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10084g = triggerLoggerPanel;
        this.f10082e = null;
        this.f10083f = null;
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.GRAY);
        setLayout(new BorderLayout());
        add(this.f10078a, BorderLayout.CENTER);
        this.f10078a.addAdjustmentListener(new ap(this, triggerLoggerPanel));
        jPanel.setLayout(new GridLayout(1, 0));
        add(jPanel, "East");
        try {
            this.f10083f = cO.a().a(cO.f11101q, this, 16);
            this.f10082e = cO.a().a(cO.f11136Z, this, 16);
            this.f10081d = new C1605cp(null, this.f10082e, null, eJ.a(16, 16));
            this.f10081d.a(new aq(this, triggerLoggerPanel));
            jPanel.add(this.f10081d);
            this.f10081d.setEnabled(false);
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this);
        }
        JLabel jLabel = new JLabel(" ");
        jLabel.setMinimumSize(eJ.a(16, 16));
        jLabel.setOpaque(true);
        jPanel.add(jLabel);
        C1605cp c1605cp = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("zoomout.gif")), null, eJ.a(16, 16));
        c1605cp.a(new ar(this, triggerLoggerPanel));
        jPanel.add(c1605cp);
        C1605cp c1605cp2 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("zoomin.gif")));
        c1605cp2.a(new as(this, triggerLoggerPanel));
        jPanel.add(c1605cp2);
        jPanel.add(new JLabel(" "));
        jPanel.add(this.f10080c);
        jPanel.add(new JLabel("of", 0));
        jPanel.add(this.f10079b);
        jPanel.add(new JLabel(" "));
        C1605cp c1605cp3 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("home.gif")));
        c1605cp3.a(new at(this, triggerLoggerPanel));
        jPanel.add(c1605cp3);
        C1605cp c1605cp4 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("rewind.gif")));
        c1605cp4.a(new au(this, triggerLoggerPanel));
        jPanel.add(c1605cp4);
        C1605cp c1605cp5 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("fastforward.gif")));
        c1605cp5.a(new av(this, triggerLoggerPanel));
        jPanel.add(c1605cp5);
        C1605cp c1605cp6 = new C1605cp(null, Toolkit.getDefaultToolkit().getImage(getClass().getResource("end.gif")));
        c1605cp6.a(new aw(this, triggerLoggerPanel));
        jPanel.add(c1605cp6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z2) {
        if (z2) {
            this.f10081d.a(this.f10082e);
        } else {
            this.f10081d.a(this.f10083f);
        }
        this.f10081d.repaint();
    }

    protected void a(boolean z2) {
        if (this.f10084g.f9980ah.size() <= 0 || ((C0188n) this.f10084g.f9980ah.get(0)).a("Time") == null) {
            this.f10081d.setEnabled(false);
        } else {
            this.f10081d.setEnabled(z2);
            this.f10081d.repaint();
        }
    }

    public void a() {
        this.f10078a.setMaximum(this.f10084g.f10005B - this.f10084g.s());
        this.f10078a.setMinimum(0);
    }

    public void a(double d2) {
        if (d2 >= 0.0d && d2 <= 1.0d) {
            this.f10078a.setValue((int) (this.f10078a.getMinimum() + (((this.f10078a.getMaximum() + this.f10084g.s()) - this.f10078a.getMinimum()) * d2)));
        } else if (d2 < 0.0d) {
            this.f10078a.setValue(this.f10078a.getMinimum());
        } else {
            bH.C.c("Invalid Percent:" + d2);
        }
    }

    public double b() {
        return (this.f10078a.getValue() - this.f10078a.getMinimum()) / ((this.f10078a.getMaximum() + this.f10084g.s()) - this.f10078a.getMinimum());
    }

    public int c() {
        return this.f10078a.getValue();
    }

    public void a(int i2) {
        this.f10079b.setText("" + i2);
    }

    public void b(int i2) {
        this.f10080c.setText("" + i2);
    }

    public int d() {
        return Integer.parseInt(this.f10080c.getText());
    }
}
